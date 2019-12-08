/************************************************************************************************                                    
 * Copyright (c) 2019 The ZelCash Developers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 **********************************************************************************/
package com.cabecinha84.zelcashui;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import com.cabecinha84.zelcashui.ZelCashJButton;
import com.cabecinha84.zelcashui.ZelCashJDialog;
import com.cabecinha84.zelcashui.ZelCashJFrame;
import com.cabecinha84.zelcashui.ZelCashJLabel;
import com.cabecinha84.zelcashui.ZelCashJPanel;
import com.cabecinha84.zelcashui.ZelCashJProgressBar;
import com.cabecinha84.zelcashui.ZelCashJTextField;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.vaklinov.zcashui.LanguageUtil;
import com.vaklinov.zcashui.Log;
import com.vaklinov.zcashui.Util;
import com.vaklinov.zcashui.ZCashClientCaller;
import com.vaklinov.zcashui.ZCashUI;
import com.vaklinov.zcashui.ZCashClientCaller.WalletCallException;


/**
 * Dialog to enter a single private key to import
 */
public class ZelcashRescanDialog
	extends ZelCashJDialog
{
	protected boolean isOKPressed = false;
	
	private static final int POLL_PERIOD = 5000;
    private static final int STARTUP_ERROR_CODE = -28;
	
	protected ZelCashJLabel upperLabel;
	protected ZelCashJLabel lowerLabel;
	
	protected ZelCashJProgressBar progress = null;
	
	protected ZCashClientCaller caller;

	private LanguageUtil langUtil;
	
	ZelCashJButton okButon;
	ZelCashJButton cancelButon;
	private ZCashUI parent;
		
	public ZelcashRescanDialog(ZCashUI parent, ZCashClientCaller caller)
	{
		super(parent);
		this.parent = parent;
		this.caller = caller;
		langUtil = LanguageUtil.instance();
		this.setTitle(langUtil.getString("wallet.operations.dialog.rescan.title"));
	    this.setLocation(parent.getLocation().x + 50, parent.getLocation().y + 50);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		ZelCashJPanel controlsPanel = new ZelCashJPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		controlsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.add(this.upperLabel = new ZelCashJLabel(
				langUtil.getString("wallet.operations.dialog.rescan.message")),
				BorderLayout.CENTER);
		controlsPanel.add(tempPanel);
		
		ZelCashJLabel dividerLabel = new ZelCashJLabel("   ");
		dividerLabel.setFont(new Font("Helvetica", Font.PLAIN, 8));
		controlsPanel.add(dividerLabel);
			
		tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		tempPanel.add(progress = new ZelCashJProgressBar());
		controlsPanel.add(tempPanel);
		
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		this.getContentPane().add(controlsPanel, BorderLayout.NORTH);

		// Form buttons
		ZelCashJPanel buttonPanel = new ZelCashJPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		okButon = new ZelCashJButton(langUtil.getString("wallet.operations.dialog.rescan.title"));
		buttonPanel.add(okButon);
		buttonPanel.add(new ZelCashJLabel("   "));
		cancelButon = new ZelCashJButton(langUtil.getString("single.key.import.dialog.tmp.panel.cancel.button.text"));
		buttonPanel.add(cancelButon);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		okButon.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ZelcashRescanDialog.this.processOK();
			}
		});
		
		cancelButon.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ZelcashRescanDialog.this.setVisible(false);
				ZelcashRescanDialog.this.dispose();
				
				ZelcashRescanDialog.this.isOKPressed = false;
			}
		});
		
		this.setSize(740, 210);
		this.validate();
		this.repaint();
		
		this.pack();
	}
	
	
	protected void processOK()
	{
		ZelcashRescanDialog.this.isOKPressed = true;

		// Start import
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.progress.setIndeterminate(true);
		this.progress.setValue(1);
			
		this.okButon.setEnabled(false);
		this.cancelButon.setEnabled(false);
			
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				parent.restartDaemon(false, true);
				try {
					restartAfterRescan();
				} catch (IOException | InterruptedException | InvocationTargetException | WalletCallException e1) {
					Log.error("Error restarting the UI, the wallet will be closed. Error:"+e1.getMessage());
					JOptionPane.showMessageDialog(null,
							LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.2.text",
									e1.getMessage()),
							LanguageUtil.instance()
									.getString("main.frame.option.pane.wallet.critical.error.2.title"),
							JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				} finally
				{
					ZelcashRescanDialog.this.setVisible(false);
					ZelcashRescanDialog.this.dispose();
				}
			}
		}).start();
	}
	
	
	public boolean isOKPressed()
	{
		return this.isOKPressed;
	}
	
	
	public void restartAfterRescan() throws IOException, InterruptedException, WalletCallException, InvocationTargetException {
		Log.info("Waiting for rescan complete.");
	    while(true) {
	        Thread.sleep(POLL_PERIOD);
	        
	        JsonObject info = null;
	        
	        try
	        {
	        	info = caller.getDaemonRawRuntimeInfo();
	        } catch (IOException e)
	        {
        		throw e;
	        }
	        
	        JsonValue code = info.get("code");
	        Log.debug("clientCaller:"+info.toString());
	        if (code == null || (code.asInt() != STARTUP_ERROR_CODE))
	            break;        
	    }
	    Log.info("Rescan complete.");
	    
	    JOptionPane.showMessageDialog(this.parent, 
	    		langUtil.getString("wallet.operations.dialog.rescan.complete.message"), 
	    		langUtil.getString("wallet.operations.dialog.rescan.complete.title"), 
	    		JOptionPane.INFORMATION_MESSAGE);
	    	
	    this.parent.setVisible(false);
		this.parent.dispose();
		
		ZCashUI z = new ZCashUI(null);
		this.parent = z;	
		this.parent.repaint();
		this.parent.setVisible(true);
	}
}
