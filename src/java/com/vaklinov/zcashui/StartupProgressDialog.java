// Code was originally written by developer - https://github.com/zlatinb
// Taken from repository https://github.com/zlatinb/zcash-swing-wallet-ui under an MIT license
package com.vaklinov.zcashui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.cabecinha84.zelcashui.ZelCashJFrame;
import com.cabecinha84.zelcashui.ZelCashJLabel;
import com.cabecinha84.zelcashui.ZelCashJPanel;
import com.cabecinha84.zelcashui.ZelCashJProgressBar;
import com.cabecinha84.zelcashui.ZelCashUI;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.vaklinov.zcashui.OSUtil.OS_TYPE;
import com.vaklinov.zcashui.ZCashClientCaller.WalletCallException;


public class StartupProgressDialog extends ZelCashJFrame {
    

    private static final int POLL_PERIOD = 1500;
    private static final int STARTUP_ERROR_CODE = -28;
    
    private BorderLayout borderLayout1 = new BorderLayout();
    private ZelCashJLabel imageLabel = new ZelCashJLabel();
    private ZelCashJLabel progressLabel = new ZelCashJLabel();
    private ZelCashJPanel southPanel = new ZelCashJPanel();
    private BorderLayout southPanelLayout = new BorderLayout();
    private ZelCashJProgressBar progressBar = new ZelCashJProgressBar();
    private ImageIcon imageIcon;
    private static Color backGroundColor = ZelCashUI.progressbar;
    
    private final ZCashClientCaller clientCaller;
    
    public StartupProgressDialog(ZCashClientCaller clientCaller) 
    {
        this.clientCaller = clientCaller;
        
        URL iconUrl = this.getClass().getClassLoader().getResource("images/ZelCash-yellow.orange-logo.png");
        imageIcon = new ImageIcon(iconUrl);
        imageLabel.setIcon(imageIcon);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(16, 40, 8, 40));
        Container contentPane = getContentPane();
        contentPane.setBackground(backGroundColor);
        contentPane.setLayout(borderLayout1);
        southPanel.setLayout(southPanelLayout);
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        contentPane.add(imageLabel, BorderLayout.NORTH);
        ZelCashJLabel zcashWalletLabel = new ZelCashJLabel(LanguageUtil.instance().getString("startup.progress.dialog.label"));
		zcashWalletLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		// todo - place in a panel with flow center
		ZelCashJPanel tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
		tempPanel.add(zcashWalletLabel);
		contentPane.add(tempPanel, BorderLayout.CENTER);
        contentPane.add(southPanel, BorderLayout.SOUTH);
        progressBar.setIndeterminate(true);
        southPanel.add(progressBar, BorderLayout.NORTH);
        progressLabel.setText(LanguageUtil.instance().getString("startup.progress.dialog.progressbar.label"));
        southPanel.add(progressLabel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void waitForStartup() throws IOException,
        InterruptedException,WalletCallException,InvocationTargetException {
        
        // special handling of Windows/macOS app launch
    	OS_TYPE os = OSUtil.getOSType();
        if ((os == OS_TYPE.WINDOWS) || (os == OS_TYPE.MAC_OS)) 
        {
            ProvingKeyFetcher keyFetcher = new ProvingKeyFetcher();
            keyFetcher.fetchIfMissing(this);
        }
        
        Log.info("Splash: checking if zelcashd is already running...");
        boolean shouldStartZCashd = false;
        try {
            clientCaller.getDaemonRawRuntimeInfo();
        } catch (IOException e) { 
        	// Relying on a general exception may be unreliable
        	// may be thrown for an unexpected reason!!! - so message is checked
        	if (e.getMessage() != null && 
        		e.getMessage().toLowerCase(Locale.ROOT).contains("error: couldn't connect to server"))
        	{
        		shouldStartZCashd = true;
        	}
        }
        
        if (!shouldStartZCashd) {
        	Log.info("Splash: zelcashd already running...");
            // What if started by hand but taking long to initialize???
//            doDispose();
//            return;
        } else
        {
        	Log.info("Splash: zelcashd will be started...");
        }
        
        final Process daemonProcess = 
        	shouldStartZCashd ? clientCaller.startDaemon() : null;
        
        Thread.sleep(POLL_PERIOD); // just a little extra
        
        int iteration = 0;
        while(true) {
        	iteration++;
            Thread.sleep(POLL_PERIOD);
            
            JsonObject info = null;
            
            try
            {
            	info = clientCaller.getDaemonRawRuntimeInfo();
            } catch (IOException e)
            {
            	if (iteration > 4)
            	{
            		throw e;
            	} else
            	{
            		continue;
            	}
            }
            
            JsonValue code = info.get("code");
            if (code == null || (code.asInt() != STARTUP_ERROR_CODE))
                break;
            final String message = info.getString("message", "???");
            setProgressText(message);
            
        }

        // doDispose(); - will be called later by the main GUI
        
        if (daemonProcess != null) // Shutdown only if we started it
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	Log.info("Stopping zelcashd because we started it - now it is alive: " + 
                		           StartupProgressDialog.this.isAlive(daemonProcess));
                try 
                {
                    clientCaller.stopDaemon();
	                long start = System.currentTimeMillis();
	                
	                while (!StartupProgressDialog.this.waitFor(daemonProcess, 3000))
	                {
	                	long end = System.currentTimeMillis();
	                	Log.info("Waiting for " + ((end - start) / 1000) + " seconds for zcashd to exit...");
	                	
	                	if (end - start > 15 * 1000)
	                	{
	                		clientCaller.stopDaemon();
	                		daemonProcess.destroy();
	                	}
	                	
	                	if (end - start > 1 * 60 * 1000)
	                	{
	                		break;
	                	}
	                }
	            
	                if (StartupProgressDialog.this.isAlive(daemonProcess)) {
	                	Log.info("zelcashd is still alive although we tried to stop it. " +
	                                           "Hopefully it will stop later!");
	                        //System.out.println("zcashd is still alive, killing forcefully");
	                        //daemonProcess.destroyForcibly();
	                    } else
	                    	Log.info("zelcashd shut down successfully");
                } catch (Exception bad) {
                	Log.error("Couldn't stop zelcashd!", bad);
                }
            }
        });
        
    }
    
    public void doDispose() {
        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setVisible(false);
				dispose();
			}
		});
    }
    
    public void setProgressText(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Log.info("Startup progress info: {0}", text);
				progressLabel.setText(text);
			}
	     });
    }
    
    
    // Custom code - to allow JDK7 compilation.
    public boolean isAlive(Process p) 
    {
    	if (p == null)
    	{
    		return false;
    	}
    	
        try 
        {
            int val = p.exitValue();
            
            return false;
        } catch (IllegalThreadStateException itse) 
        {
            return true;
        }
    }
    
    
    // Custom code - to allow JDK7 compilation.
    public boolean waitFor(Process p, long interval)
    {
		synchronized (this) 
		{
			long startWait = System.currentTimeMillis();
			long endWait = startWait;
			do
			{
				boolean ended = !isAlive(p);
				
				if (ended)
				{
					return true; // End here
				}
				
				try
				{
					this.wait(100);
				} catch (InterruptedException ie)
				{
					// One of the rare cases where we do nothing
					Log.error("Unexpected error: ", ie);
				}
				
				endWait = System.currentTimeMillis();
			} while ((endWait - startWait) <= interval);
		}
		
		return false;
    }
}
