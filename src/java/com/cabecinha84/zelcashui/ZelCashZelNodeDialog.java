package com.cabecinha84.zelcashui;

import java.awt.BorderLayout;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;

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

	private ZelCashJFrame parentFrame;
	
	private static ZelCashJButton saveButton;
	
	private ZCashClientCaller clientCaller;
	private ZCashInstallationObserver installationObserver;
	private String aliastoEdit;
	protected LabelStorage labelStorage;
	
	final LanguageUtil langUtil = LanguageUtil.instance();
	
	public ZelCashZelNodeDialog(ZelCashJFrame parent, final ZCashClientCaller clientCaller, final ZCashInstallationObserver installationObserver, String aliasToEdit, final LabelStorage labelStorage)
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
					if(st.contains("#") || emptyLine.equals("")) {
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
								for(int i=0; i< details.size(); ++i) {
									JsonObject obj = details.get(i).asObject();
									vout = obj.get("vout").toString().replaceAll("[\n\r\"]", "");
									category = obj.get("category").toString().replaceAll("[\n\r\"]", "");
									if(vout.equals(zelNodeInfo[4]) && "send".equals(category)) {
										detailAmount = obj.get("amount").toString().replaceAll("[\n\r\"]", "").substring(1);
										address = obj.get("address").toString().replaceAll("[\n\r\"]", "");
										break;
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
								for(int i=0; i< details.size(); ++i) {
									JsonObject obj = details.get(i).asObject();
									vout = obj.get("vout").toString().replaceAll("[\n\r\"]", "");
									category = obj.get("category").toString().replaceAll("[\n\r\"]", "");
									
									if(vout.equals(outputinfo[1]) && "send".equals(category)) {
										detailAmount = obj.get("amount").toString().replaceAll("[\n\r\"]", "").substring(1);
										address = obj.get("address").toString().replaceAll("[\n\r\"]", "");
										break;
									}
								}
								if ((address != null) && (address.length() > 0))
								{
									String label = labelStorage.getLabel(address);
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
			Log.info("Now adding info needed to run zelNode to zelcash conf file if needed");
			File zelcashConf = new File(blockchainDir + File.separator + "zelcash.conf");
			Properties confProps = new Properties();
			FileInputStream fis = null;
			String property = null;
			boolean daemonNeedsToBeRestarted = false;
			try
			{
				fis = new FileInputStream(zelcashConf);
				fw = new FileWriter(zelcashConf,true); //the true will append the new data
				confProps.load(fis);
				property = confProps.getProperty("server");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "server=1"); 
					Log.info("Adding server=1");
					daemonNeedsToBeRestarted = true;
				}
				property = confProps.getProperty("daemon");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "daemon=1"); 
					Log.info("Adding server=1");
					daemonNeedsToBeRestarted = true;
				}
				property = confProps.getProperty("txindex");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "txindex=1"); 
					Log.info("Adding txindex=1");
					daemonNeedsToBeRestarted = true;
				}
				property = confProps.getProperty("logtimestamps");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "logtimestamps=1"); 
					Log.info("Adding logtimestamps=1");
					daemonNeedsToBeRestarted = true;
				}
				property = confProps.getProperty("maxconnections");
				if(property == null) {
					fw.write(System.getProperty("line.separator") + "maxconnections=256"); 
					Log.info("Adding maxconnections=256");
					daemonNeedsToBeRestarted = true;
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
			/*if(daemonNeedsToBeRestarted) {
			//nice to have, restart your wallet here if it`s needed.
			JOptionPane.showMessageDialog(null,
					LanguageUtil.instance().getString("wallet.zelnodes.restart.message"),
					LanguageUtil.instance().getString("wallet.zelnodes.restart.title"),
					JOptionPane.INFORMATION_MESSAGE);

			}*/
			if(this.aliastoEdit==null) {
				if(this.installationObserver.isOnTestNet()) {
					JOptionPane.showMessageDialog(null,
							LanguageUtil.instance().getString("wallet.zelnodes.restart.testnet.message"),
							LanguageUtil.instance().getString("wallet.zelnodes.restart.title"),
							JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null,
							LanguageUtil.instance().getString("wallet.zelnodes.restart.message"),
							LanguageUtil.instance().getString("wallet.zelnodes.restart.title"),
							JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			

			Log.info("Restarting the wallet.");
			ZCashUI z = new ZCashUI(null);
			ZelCashZelNodeDialog.this.parentFrame.setVisible(false);
			ZelCashZelNodeDialog.this.parentFrame.dispose();
			ZelCashZelNodeDialog.this.parentFrame = z;	
			ZelCashZelNodeDialog.this.parentFrame.repaint();
			ZelCashZelNodeDialog.this.parentFrame.setVisible(true);
			ZelCashZelNodeDialog.this.setVisible(false);
			ZelCashZelNodeDialog.this.dispose();


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
	
	public static void removeZelNode(String zelNodeAlias) {
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
					if(st.contains("#") || "".equals(emptyLine)) {
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
