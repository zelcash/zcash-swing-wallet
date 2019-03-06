package com.cabecinha84.zelcashui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.vaklinov.zcashui.LabelStorage;
import com.vaklinov.zcashui.LanguageUtil;
import com.vaklinov.zcashui.Log;
import com.vaklinov.zcashui.OSUtil;
import com.vaklinov.zcashui.ZCashClientCaller;
import com.vaklinov.zcashui.ZCashClientCaller.WalletCallException;
import com.vaklinov.zcashui.ZCashInstallationObserver.DAEMON_STATUS;
import com.vaklinov.zcashui.ZCashInstallationObserver;
import com.vaklinov.zcashui.ZCashUI;

/**
 * Dialog showing the information about a user's identity
 */
public class ZelCashZelNodeDialog
	extends ZelCashJDialog
{
	protected ZelCashJTextField zelNodeName;
	protected ZelCashJTextField zelNodeIP;
	protected ZelCashJTextField zelNodeKey;
	private ZelCashJComboBox<String> zelNodeOutput;
	protected ZelCashJTextField zelNodeOutputText;
	protected ZelCashJTextField zelNodeAmount;
	protected ZelCashJTextField zelNodeAddress;

	private ZCashUI parentFrame;
	
	private static ZelCashJButton saveButton;
	
	private ZCashClientCaller clientCaller;
	private static ZCashInstallationObserver installationObserver;
	private String aliastoEdit;
	protected LabelStorage labelStorage;
	
	final LanguageUtil langUtil = LanguageUtil.instance();
	
	public ZelCashZelNodeDialog(ZCashUI parent, final ZCashClientCaller clientCaller, final ZCashInstallationObserver installationObserver, String aliasToEdit, final LabelStorage labelStorage)
			throws IOException
	{
		parentFrame = parent;
		this.clientCaller = clientCaller;
		this.aliastoEdit = aliasToEdit;
		this.installationObserver = installationObserver;
		this.labelStorage = labelStorage;
		
		this.setTitle(langUtil.getString("dialog.zelcashnewzelnode.title"));
		this.setSize(900, 650);
	    this.setLocation(100, 100);
		this.setLocationRelativeTo(parent);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		ZelCashJLabel infoLabel = new ZelCashJLabel(langUtil.getString("dialog.zelcashnewzelnode.info"));
	    tempPanel.add(infoLabel, BorderLayout.CENTER);
		this.getContentPane().add(tempPanel, BorderLayout.NORTH);
		
		
		ZelCashJPanel detailsPanel = new ZelCashJPanel();
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
		
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashnewzelnode.name"),  zelNodeName = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashnewzelnode.ip"),  zelNodeIP = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashnewzelnode.key"),  zelNodeKey = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashnewzelnode.output"),  zelNodeOutput = new ZelCashJComboBox<String>());
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashnewzelnode.address"),  zelNodeAddress = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashnewzelnode.amount"),  zelNodeAmount = new ZelCashJTextField(50));
		zelNodeName.setEditable(true);
		zelNodeIP.setEditable(true);
		zelNodeKey.setEditable(false);
		zelNodeOutput.setEnabled(false);
		zelNodeAmount.setEditable(false);
		zelNodeAddress.setEditable(false);
		
		getZelNodeOutputs();
		if(this.aliastoEdit != null) {
			zelNodeName.setText(this.aliastoEdit);
			
			String blockchainDir = OSUtil.getBlockchainDirectory();
			File zelnodeConf = new File(blockchainDir + File.separator + "zelnode.conf");
			if (!zelnodeConf.exists())
			{
				Log.info("Could not find file: {0} !", zelnodeConf.getAbsolutePath());
			} 
			else {
				Log.info("File zelnode.conf found");
				BufferedReader br = new BufferedReader(new FileReader(zelnodeConf)); 
				String st; 
				String emptyLine;
				while ((st = br.readLine()) != null) {
					emptyLine = st.replaceAll(" ", "").replaceAll("(?m)^\\\\s*\\\\r?\\\\n|\\\\r?\\\\n\\\\s*(?!.*\\\\r?\\\\n)", "");						
					if(st.startsWith("#") || emptyLine.equals("")) {
						continue;
					}
					else {
						String[] zelNodeInfo = st.split("\\s+");
						if(zelNodeInfo[0].equals(this.aliastoEdit)) {
							zelNodeIP.setText(zelNodeInfo[1]);
							zelNodeKey.setText(zelNodeInfo[2]);
							String output = zelNodeInfo[3] + " " + zelNodeInfo[4];
							zelNodeOutput.setSelectedItem(output);
							try {
								JsonObject txinfo = clientCaller.getTransactionInfo(zelNodeInfo[3]);
								JsonArray details = txinfo.get("details").asArray();
								String vout;
								String category;
								String detailAmount="0"; 
								String address ="";
								boolean addressFound = false;
								for(int i=0; i< details.size(); ++i) {
									JsonObject obj = details.get(i).asObject();
									vout = obj.get("vout").toString().replaceAll("[\n\r\"]", "");
									category = obj.get("category").toString().replaceAll("[\n\r\"]", "");
									if(vout.equals(zelNodeInfo[4]) && "send".equals(category)) {
										detailAmount = obj.get("amount").toString().replaceAll("[\n\r\"]", "").substring(1);
										address = obj.get("address").toString().replaceAll("[\n\r\"]", "");
										addressFound = true;
										break;
									}
								}
								if(!addressFound) {
									for(int i=0; i< details.size(); ++i) {
										JsonObject obj = details.get(i).asObject();
										vout = obj.get("vout").toString().replaceAll("[\n\r\"]", "");
										category = obj.get("category").toString().replaceAll("[\n\r\"]", "");

										if(vout.equals(zelNodeInfo[4]) && "receive".equals(category)) {
											detailAmount = obj.get("amount").toString().replaceAll("[\n\r\"]", "");
											address = obj.get("address").toString().replaceAll("[\n\r\"]", "");
											break;
										}
									}
								}
								if ((address != null) && (address.length() > 0))
								{
									String label = this.labelStorage.getLabel(address);
									if ((label != null) && (label.length() > 0))
									{
										address = label + " - " + address;
									}
								}
								
								Float floatAmount=Float.parseFloat(detailAmount);
								DecimalFormat df = new DecimalFormat("0.00");
								df.setMaximumFractionDigits(2);
								zelNodeAmount.setText(df.format(floatAmount));
								zelNodeAddress.setText(address);
				            } catch (WalletCallException | IOException | InterruptedException e) {
								Log.error("Error calling getRawTransactionDetails:"+e.getMessage());
							}
							break;
						}
					}
				}
			}
		}
		else {
			gelZelNodeKey();
		}
		

		detailsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.getContentPane().add(detailsPanel, BorderLayout.CENTER);

		ZelCashJPanel closePanel = new ZelCashJPanel();
		closePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		ZelCashJButton closeButon = new ZelCashJButton(langUtil.getString("dialog.about.button.close.text"));
		closePanel.add(closeButon);
		saveButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.save"));
		closePanel.add(saveButton);
		this.getContentPane().add(closePanel, BorderLayout.SOUTH);
		
		closeButon.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					ZelCashZelNodeDialog.this.parentFrame.repaint();
					ZelCashZelNodeDialog.this.setVisible(false);
					ZelCashZelNodeDialog.this.dispose();
				}
		});
		
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if("".equals(zelNodeName.getText().replaceAll(" ", "")) ||
						"".equals(zelNodeIP.getText().replaceAll(" ", "")) || 
						"".equals(zelNodeKey.getText().replaceAll(" ", "")) ||
						zelNodeOutput == null ||
						zelNodeOutput.getItemCount()==0 ||
						langUtil.getString("dialog.zelcashnewzelnode.select.output").equals(zelNodeOutput.getSelectedItem().toString()) 
						) {
					JOptionPane.showMessageDialog(
	                        null,
	                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.missing"),
	                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.missing.title"),
	                        JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (!zelNodeName.getText().matches("\\w+"))
				{
					JOptionPane.showMessageDialog(
	                        null,
	                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.alias.wrong"),
	                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.error.adding.title"),
	                        JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!zelNodeIP.getText().endsWith(":16125") && !zelNodeIP.getText().endsWith(":26125"))
				{
					JOptionPane.showMessageDialog(
	                        null,
	                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.ip.wrong"),
	                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.error.adding.title"),
	                        JOptionPane.ERROR_MESSAGE);
					return;
				}

				String ip = zelNodeIP.getText().replaceAll(":16125", "").replaceAll(":26125", "");

				try {
					Inet4Address address = (Inet4Address) Inet4Address.getByName(ip);
				}
				catch (Exception ex1) {
					try {
						Inet6Address address = (Inet6Address) Inet6Address.getByName(ip);
					}
					catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("parsing.error.zelnodesconf.wrong.ip", ip),
								LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.error.adding.title"),
								JOptionPane.ERROR_MESSAGE);
						return;
					}							
				}
				saveSettings();
			}
		});
		
		zelNodeOutput.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {
				if(event.getStateChange() == ItemEvent.SELECTED) {
					Object source = event.getSource();
		            if (source instanceof ZelCashJComboBox) {
		            	ZelCashJComboBox<String> cb = (ZelCashJComboBox<String>)source;
		            	int index = cb.getSelectedIndex();
		            	if(index!=0) {
		            		zelNodeAmount.setText(langUtil.getString("zelnodespanel.zelnodes.button.loading"));
		            		zelNodeAddress.setText(langUtil.getString("zelnodespanel.zelnodes.button.loading"));
		            		String[] outputinfo = cb.getSelectedItem().toString().split(" ");
			                try {
			                	JsonObject txinfo = clientCaller.getTransactionInfo(outputinfo[0]);
			                	JsonArray details = txinfo.get("details").asArray();
								String vout;
								String category;
								String detailAmount="0"; 
								String address ="";
								Log.debug("details array size:"+details.size());
								boolean addressFound = false;
								for(int i=0; i< details.size(); ++i) {
									JsonObject obj = details.get(i).asObject();
									vout = obj.get("vout").toString().replaceAll("[\n\r\"]", "");
									category = obj.get("category").toString().replaceAll("[\n\r\"]", "");
									
									if(vout.equals(outputinfo[1]) && "send".equals(category)) {
										detailAmount = obj.get("amount").toString().replaceAll("[\n\r\"]", "").substring(1);
										address = obj.get("address").toString().replaceAll("[\n\r\"]", "");
										addressFound = true;
										break;
									}
								}
								if(!addressFound) {
									for(int i=0; i< details.size(); ++i) {
										JsonObject obj = details.get(i).asObject();
										vout = obj.get("vout").toString().replaceAll("[\n\r\"]", "");
										category = obj.get("category").toString().replaceAll("[\n\r\"]", "");

										if(vout.equals(outputinfo[1]) && "receive".equals(category)) {
											detailAmount = obj.get("amount").toString().replaceAll("[\n\r\"]", "");
											address = obj.get("address").toString().replaceAll("[\n\r\"]", "");
											break;
										}
									}
								}
								if ((address != null) && (address.length() > 0))
								{
									String label = labelStorage.getLabel(address);
									if ((label != null) && (label.length() > 0))
									{
										Log.debug("found the address label:"+label);
										address = label + " - " + address;
									}
								}
								Float floatAmount=Float.parseFloat(detailAmount);
								DecimalFormat df = new DecimalFormat("0.00");
								df.setMaximumFractionDigits(2);
								zelNodeAmount.setText(df.format(floatAmount));
								zelNodeAddress.setText(address);
			                } catch (WalletCallException | IOException | InterruptedException e) {
								Log.error("Error calling getRawTransactionDetails:"+e.getMessage());
							}
		            	}
		                
		            }
				}
				
			}
		});
		
		pack();
	}
	
	private void gelZelNodeKey() {
		Log.info("gelZelNodeKey start");
		try {
			zelNodeKey.setText(clientCaller.getZelNodeKey().replaceAll("[\n\r\"]", ""));
		} catch (WalletCallException | IOException | InterruptedException e) {
			Log.error("Error obtaining gelZelNodeKey. " + e.getMessage());
		} finally {
			Log.info("gelZelNodeKey end");
		}
	}
	
	private void getZelNodeOutputs() {
		Log.info("getZelNodeOutputs start");
		int outputsCount=0;
		try {
			JsonArray ja = clientCaller.getZelNodeOutputs();
			outputsCount = ja.size();
			zelNodeOutput.removeAllItems();
			zelNodeOutput.addItem(this.langUtil.getString("dialog.zelcashnewzelnode.select.output"));
			for (int i = 0; i < ja.size(); ++i) {
				JsonObject obj = ja.get(i).asObject();
				String txhash = obj.get("txhash").toString().replaceAll("[\n\r\"]", "");
				String outputidx = obj.get("outputidx").toString().replaceAll("[\n\r\"]", "");
				zelNodeOutput.addItem(txhash + " " + outputidx);
			}
			zelNodeOutput.setEnabled(true);
		} catch (WalletCallException | IOException | InterruptedException e1) {
			Log.error("Error obtaining zelNodeOutputs. " + e1.getMessage());
		} finally {
			Log.info("getZelNodeOutputs end - outputscount:" +outputsCount);
		}
	}
	
	private void addFormField(ZelCashJPanel detailsPanel, String name, JComponent field, ZelCashJButton button)
	{
		ZelCashJPanel tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
		ZelCashJLabel tempLabel = new ZelCashJLabel(name, JLabel.RIGHT);
		// TODO: hard sizing of labels may not scale!
		final int width = new ZelCashJLabel("Sender identification T address:").getPreferredSize().width + 10;
		tempLabel.setPreferredSize(new Dimension(width, tempLabel.getPreferredSize().height));
		tempPanel.add(tempLabel);
		tempPanel.add(field);
		tempPanel.add(button);
		detailsPanel.add(tempPanel);
	}
	
	private void addFormField(ZelCashJPanel detailsPanel, String name, JComponent field)
	{
		ZelCashJPanel tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
		ZelCashJLabel tempLabel = new ZelCashJLabel(name, JLabel.RIGHT);
		// TODO: hard sizing of labels may not scale!
		final int width = new ZelCashJLabel("Sender identification T address:").getPreferredSize().width + 10;
		tempLabel.setPreferredSize(new Dimension(width, tempLabel.getPreferredSize().height));
		tempPanel.add(tempLabel);
		tempPanel.add(field);
		detailsPanel.add(tempPanel);
	}
	

	
	private void saveSettings() {
		Log.info("Starting save zelnode");
		try {
			removeEmptyLinesFromNodesConfigurationFile();
			if(this.aliastoEdit!=null) {
				removeZelNode(this.aliastoEdit);
			}
			FileWriter fw = null;
			String blockchainDir = OSUtil.getBlockchainDirectory();
			File zelnodeConf = new File(blockchainDir + File.separator + "zelnode.conf");
			if (!zelnodeConf.exists())
			{
				Log.info("Could not find file: {0} !", zelnodeConf.getAbsolutePath());
				zelnodeConf.createNewFile();
				Log.info("File zelnodes.conf created");
			} 
			else {
				Log.info("File zelnode.conf found");
			}
			
			if(this.aliastoEdit==null) {
				//checking for duplications
			    Scanner scanner = new Scanner(zelnodeConf);
	
			    while (scanner.hasNextLine()) {
			        String line = scanner.nextLine();
			        if(!line.startsWith("#") && line.contains(zelNodeOutput.getSelectedItem().toString())) { 
			        	JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.output.duplicated"),
								LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.error.adding.title"),
								JOptionPane.ERROR_MESSAGE);
						return;
			        }
			        
			        if(!line.startsWith("#") && line.toLowerCase().contains(zelNodeName.getText().toLowerCase().replaceAll(" ", "").replaceAll("[\n\r\"]", ""))) { 
			        	JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.alias.duplicated"),
								LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.error.adding.title"),
								JOptionPane.ERROR_MESSAGE);
						return;
			        }
			        
			        if(!line.startsWith("#") && line.toLowerCase().contains(zelNodeIP.getText().toLowerCase().replaceAll(" ", "").replaceAll("[\n\r\"]", ""))) { 
			        	JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.ip.duplicated"),
								LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.error.adding.title"),
								JOptionPane.ERROR_MESSAGE);
						return;
			        }
			        
			        if(!line.startsWith("#") && line.contains(zelNodeKey.getText())) { 
			        	JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.key.duplicated"),
								LanguageUtil.instance().getString("dialog.zelcashnewzelnode.fields.error.adding.title"),
								JOptionPane.ERROR_MESSAGE);
						return;
			        }
			    }
			}


			Log.info("Now adding info needed to run zelNode to zelcash conf file if needed");
			File zelcashConf = new File(blockchainDir + File.separator + "zelcash.conf");
			Properties confProps = new Properties();
			FileInputStream fis = null;
			String property = null;
			boolean daemonNeedsToBeReindexed = false;
			try
			{
				fis = new FileInputStream(zelcashConf);
				fw = new FileWriter(zelcashConf,true); //the true will append the new data
				confProps.load(fis);
				property = confProps.getProperty("server");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "server=1"); 
					Log.info("Adding server=1");
					daemonNeedsToBeReindexed = true;
				}
				property = confProps.getProperty("daemon");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "daemon=1"); 
					Log.info("Adding server=1");
					daemonNeedsToBeReindexed = true;
				}
				property = confProps.getProperty("txindex");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "txindex=1"); 
					Log.info("Adding txindex=1");
					daemonNeedsToBeReindexed = true;
				}
				property = confProps.getProperty("logtimestamps");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "logtimestamps=1"); 
					Log.info("Adding logtimestamps=1");
					daemonNeedsToBeReindexed = true;
				}
				property = confProps.getProperty("maxconnections");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "maxconnections=256"); 
					Log.info("Adding maxconnections=256");
					daemonNeedsToBeReindexed = true;
				}
				
			} finally
			{
				if (fw != null) {
					fw.close();
				}
				if (fis != null)
				{
					fis.close();
				}
			}

			try
			{
				fw = new FileWriter(zelnodeConf,true); //the true will append the new data
			    fw.write(zelNodeName.getText().replaceAll(" ", "").replaceAll("[\n\r\"]", "") + " " + zelNodeIP.getText().replaceAll(" ", "").replaceAll("[\n\r\"]", "") + " " + zelNodeKey.getText() + " " + zelNodeOutput.getSelectedItem().toString() +System.getProperty("line.separator"));//appends the string to the file
			    Log.info("File zelnode.conf saved with new ZelNode: " +zelNodeName);
			} finally
			{
				if (fw != null) {
					fw.close();
				}
			}
			
			ZCashInstallationObserver initialInstallationObserver = new ZCashInstallationObserver(OSUtil.getProgramDirectory());
			if(initialInstallationObserver.isOnTestNet()) {
				initialInstallationObserver = null;
				JOptionPane.showMessageDialog(null,
						LanguageUtil.instance().getString("wallet.zelnodes.restart.testnet.message"),
						LanguageUtil.instance().getString("wallet.zelnodes.restart.title"),
						JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				initialInstallationObserver = null;
				Object[] options = { LanguageUtil.instance().getString("ipfs.wrapper.options.yes"),
						LanguageUtil.instance().getString("ipfs.wrapper.options.no") };
				if(daemonNeedsToBeReindexed) {					
					int option = JOptionPane.showOptionDialog(null,
							LanguageUtil.instance().getString("wallet.zelnodes.restart.reindex.message"),
							LanguageUtil.instance().getString("wallet.zelnodes.restart.title"),
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
					if (option == 0) {
						JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("wallet.reindex.restart.message"),
								LanguageUtil.instance().getString("wallet.reindex.restart.title"),
								JOptionPane.INFORMATION_MESSAGE);
						this.setVisible(false);
						ZelCashZelNodeDialog.this.parentFrame.restartDaemon(true);
					}
				}
				else {
					int option = JOptionPane.showOptionDialog(null,
							LanguageUtil.instance().getString("wallet.zelnodes.restart.message"),
							LanguageUtil.instance().getString("wallet.zelnodes.restart.title"),
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
					if (option == 0) {
						JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("wallet.restart.message"),
								LanguageUtil.instance().getString("wallet.reindex.restart.title"),
								JOptionPane.INFORMATION_MESSAGE);
						this.setVisible(false);
						ZelCashZelNodeDialog.this.parentFrame.restartDaemon(false);
					} 
				}
			}

			restartUI();
		}
		catch (WalletCallException wce)
        {
        	Log.error("Unexpected error: ", wce);

            if ((wce.getMessage().indexOf("{\"code\":-28,\"message\"") != -1) ||
            	(wce.getMessage().indexOf("error code: -28") != -1))
            {
                JOptionPane.showMessageDialog(
                        null,
                        LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.text"),
                        LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.title"),
                        JOptionPane.ERROR_MESSAGE);
            } else
            {
                JOptionPane.showMessageDialog(
                    null,
                        LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.2.text", wce.getMessage()),
                        LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.2.title"),
                    JOptionPane.ERROR_MESSAGE);
            }

            System.exit(2);
        } catch (Exception ex)
        {
        	Log.error("Unexpected error: ", ex);
            JOptionPane.showMessageDialog(
                null,
                LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.text", ex.getMessage()),
                LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.title"),
                JOptionPane.ERROR_MESSAGE);
            System.exit(3);
        } catch (Error err)
        {
        	// Last resort catch for unexpected problems - just to inform the user
            err.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                    LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.2.text", err.getMessage()),
                    LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.2.title"),
                    JOptionPane.ERROR_MESSAGE);
            System.exit(4);
        }
		
	}
	
	public void restartUI() throws IOException, InterruptedException, WalletCallException {
		Log.info("Restarting the UI.");
		this.parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.setVisible(false);
		ZCashUI z = new ZCashUI(null);
		this.parentFrame.setVisible(false);
		this.parentFrame.dispose();
		this.parentFrame = z;	
		this.parentFrame.repaint();
		this.parentFrame.setVisible(true);
		this.dispose();
	}
	
	public void disposeMenu() {
		this.setVisible(false);
		this.dispose();	
	}
	
	public static void removeZelNode(String zelNodeAlias) {
		Log.info("Removing zelnode alias:"+zelNodeAlias);
		removeEmptyLinesFromNodesConfigurationFile();
	    try {
	    	String blockchainDir = OSUtil.getBlockchainDirectory();
			File zelnodeConf = new File(blockchainDir + File.separator + "zelnode.conf");
		  if (!zelnodeConf.exists()) {
			  Log.error("Could not find file: {0} !", zelnodeConf.getAbsolutePath());
			  return;
	      }
		  BufferedReader br = new BufferedReader(new FileReader(zelnodeConf));
		  ArrayList<String> coll = new ArrayList<String>();
		  try {
			  
		      String st = null;
		      String[] zelNodeInfo;
		      String emptyLine = null;
		      while ((st = br.readLine()) != null) {
					emptyLine = st.replaceAll(" ", "").replaceAll("(?m)^\\\\s*\\\\r?\\\\n|\\\\r?\\\\n\\\\s*(?!.*\\\\r?\\\\n)", "");						
					if(st.startsWith("#") || "".equals(emptyLine)) {
					  coll.add(st);
					}
					else {
						zelNodeInfo = st.split("\\s+");
						if(!zelNodeAlias.equals(zelNodeInfo[0])) {
							coll.add(st);
						}
					}
		          
		      }
		  }
		  finally {
			  	if(br != null) {
		    		br.close();
		    	}
			}
		  FileWriter writer = new FileWriter(zelnodeConf);
		  try {
		      for (String line : coll) {
		          writer.write(line+System.getProperty("line.separator"));
		      }
		  }
		  finally {
			  if(writer != null) {
			    	writer.close();
			    }
		  }
	    }
	    catch(Exception ex) {
	    	Log.error("Error deleting zelnode "+ zelNodeAlias + ":" + ex.getMessage());
	    }
	    finally {
			Log.info("Zelnode removed from configuration file. Alias removed:"+zelNodeAlias);
		}
	}
	
	public static void removeEmptyLinesFromNodesConfigurationFile() {
		try {
	    	String blockchainDir = OSUtil.getBlockchainDirectory();
			File zelnodeConf = new File(blockchainDir + File.separator + "zelnode.conf");
		  if (!zelnodeConf.exists()) {
			  Log.error("Could not find file: {0} !", zelnodeConf.getAbsolutePath());
			  return;
	      }
		  BufferedReader br = new BufferedReader(new FileReader(zelnodeConf));
		  ArrayList<String> coll = new ArrayList<String>();
		  try {
			  
		      String st = null;
		      String[] zelNodeInfo;
		      String emptyLine = null;
		      while ((st = br.readLine()) != null) {
					emptyLine = st.replaceAll(" ", "").replaceAll("(?m)^\\\\s*\\\\r?\\\\n|\\\\r?\\\\n\\\\s*(?!.*\\\\r?\\\\n)", "");						
					if(!"".equals(emptyLine)) {
					  coll.add(st);
					}	          
		      }
		  }
		  finally {
			  	if(br != null) {
		    		br.close();
		    	}
			}
		  FileWriter writer = new FileWriter(zelnodeConf);
		  try {
		      for (String line : coll) {
		          writer.write(line+System.getProperty("line.separator"));
		      }
		  }
		  finally {
			  if(writer != null) {
			    	writer.close();
			    }
		  }
	    }
	    catch(Exception ex) {
	    	Log.error("Error deleting empty lines from zelnodes.conf:" + ex.getMessage());
	    }
	}
	    
} 
