package com.cabecinha84.zelcashui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

import com.eclipsesource.json.JsonObject;
import com.vaklinov.zcashui.LanguageUtil;
import com.vaklinov.zcashui.Log;
import com.vaklinov.zcashui.OSUtil;
import com.vaklinov.zcashui.PasswordDialog;
import com.vaklinov.zcashui.ZCashClientCaller;
import com.vaklinov.zcashui.ZCashClientCaller.WalletCallException;
import com.vaklinov.zcashui.ZCashInstallationObserver;
import com.vaklinov.zcashui.ZCashUI;

/**
 * Dialog showing the information about Sprout to Sapling Migration
 */
public class ZelCashSproutToSaplingDialog
	extends ZelCashJDialog
{
	protected ZelCashJTextField enabled;
	private ZelCashJComboBox<String> destination_address;
	protected ZelCashJTextField unmigrated_amount;
	protected ZelCashJTextField unfinalized_migrated_amount;
	protected ZelCashJTextField finalized_migrated_amount;
	protected ZelCashJTextField finalized_migration_transactions;
	protected ZelCashJTextField time_started;
	protected ZelCashJTextArea migration_txids;
	private boolean migrationEnabled = false;

	private ZCashUI parentFrame;
	
	private static ZelCashJButton saveButton;
	
	private ZCashClientCaller clientCaller;
	private static ZCashInstallationObserver installationObserver;
	private List<String> listOfSapling;
	private List<String> listOfSaplingWithLabels;
	
	final LanguageUtil langUtil = LanguageUtil.instance();
	
	public ZelCashSproutToSaplingDialog(ZCashUI parent, final ZCashClientCaller clientCaller, final ZCashInstallationObserver installationObserver, final List<String> listOfSapling, List<String> listOfSaplingWithLabels)
			throws IOException
	{
		parentFrame = parent;
		this.clientCaller = clientCaller;
		this.installationObserver = installationObserver;
		this.listOfSapling = listOfSapling;
		this.listOfSaplingWithLabels = listOfSaplingWithLabels;
		
		this.setTitle(langUtil.getString("dialog.zelcashsprouttosaplingdialog.title"));
		this.setSize(650, 550);
		this.setLocation(100, 100);
	    this.setLocationRelativeTo(parentFrame);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		ZelCashJLabel infoLabel = new ZelCashJLabel(langUtil.getString("dialog.zelcashsprouttosaplingdialog.info"));
		tempPanel.add(infoLabel, BorderLayout.CENTER);
		this.getContentPane().add(tempPanel, BorderLayout.NORTH);
		
		
		ZelCashJPanel detailsPanel = new ZelCashJPanel();
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
		destination_address = new ZelCashJComboBox<String>(listOfSaplingWithLabels.toArray());
		destination_address.setPreferredSize(new Dimension(600,destination_address.getPreferredSize().height));
		
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashsprouttosaplingdialog.enabled"),  enabled = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashsprouttosaplingdialog.destination_address"),  destination_address);
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashsprouttosaplingdialog.unmigrated_amount"),  unmigrated_amount = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashsprouttosaplingdialog.unfinalized_migrated_amount"),  unfinalized_migrated_amount = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashsprouttosaplingdialog.finalized_migrated_amount"),  finalized_migrated_amount = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashsprouttosaplingdialog.finalized_migration_transactions"),  finalized_migration_transactions = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashsprouttosaplingdialog.time_started"),  time_started = new ZelCashJTextField(50));
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashsprouttosaplingdialog.migration_txids"),  migration_txids = new ZelCashJTextArea(3,50));	

		this.enabled.setEditable(false);
		this.unmigrated_amount.setEditable(false);
		this.unfinalized_migrated_amount.setEditable(false);
		this.finalized_migrated_amount.setEditable(false);
		this.finalized_migration_transactions.setEditable(false);
		this.time_started.setEditable(false);
		this.migration_txids.setEditable(false);
		
		detailsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.getContentPane().add(detailsPanel, BorderLayout.CENTER);

		getMigrationInfo();
		
		ZelCashJPanel closePanel = new ZelCashJPanel();
		closePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		ZelCashJButton closeButton = new ZelCashJButton(langUtil.getString("dialog.about.button.close.text"));
		closePanel.add(closeButton);
		if(migrationEnabled) {
			saveButton = new ZelCashJButton(langUtil.getString("dialog.zelcashsprouttosaplingdialog.disable"));
		}
		else {
			saveButton = new ZelCashJButton(langUtil.getString("dialog.zelcashsprouttosaplingdialog.enable"));
			
		}
		closePanel.add(saveButton);
		this.getContentPane().add(closePanel, BorderLayout.SOUTH);
		
		closeButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					ZelCashSproutToSaplingDialog.this.parentFrame.repaint();
					ZelCashSproutToSaplingDialog.this.setVisible(false);
					ZelCashSproutToSaplingDialog.this.dispose();
				}
		});
		
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try {
					if (ZelCashSproutToSaplingDialog.this.clientCaller.isWalletEncrypted())
					{
						if(!migrationEnabled) {
							int index = destination_address.getSelectedIndex();
							if(index == 0) {
								JOptionPane.showMessageDialog(
										ZelCashSproutToSaplingDialog.this.parentFrame, 
										langUtil.getString("dialog.zelcashsprouttosaplingdialog.noaddress.message"),
										langUtil.getString("dialog.zelcashsprouttosaplingdialog.noaddress.title"),
										JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
						boolean passwordOk = false;
						int retrys = 0;
						while(!passwordOk && retrys<3) {
							++retrys;
							PasswordDialog pd = new PasswordDialog((ZelCashJFrame)(ZelCashSproutToSaplingDialog.this.parentFrame));
							pd.setVisible(true);

							if (!pd.isOKPressed())
							{
								return;
							}
							try {
								ZelCashSproutToSaplingDialog.this.clientCaller.unlockWallet(pd.getPassword());
								passwordOk = true;
							}
							catch (Exception ex) {
								Log.error("Error unlocking wallet:"+ex.getMessage());
								JOptionPane.showMessageDialog(
										ZelCashSproutToSaplingDialog.this.parentFrame, 
										langUtil.getString("encryption.error.unlocking.message", ex.getMessage()),
										langUtil.getString("encryption.error.unlocking.title"),
										JOptionPane.ERROR_MESSAGE);
							}
						}
						if(!passwordOk) {
							Log.info("Failed to enter correct password for third time, wallet will close.");
							System.exit(1);
						}
					}
					removeMigratioInfoFromConfFile();
					
					FileInputStream fis = null;
					FileWriter fw = null;
					Properties confProps = new Properties();
					String blockchainDir = null;
					String property = null;
					blockchainDir = OSUtil.getBlockchainDirectory();
					File ZelCashConf = new File(blockchainDir + File.separator + "zelcash.conf");
					fis = new FileInputStream(ZelCashConf);
					fw = new FileWriter(ZelCashConf,true); //the true will append the new data
					confProps.load(fis);
					if(!migrationEnabled) {
						try
						{
							int index = destination_address.getSelectedIndex();
							String address = listOfSapling.get(index);
							
							property = confProps.getProperty("migration");
							if(property == null) {
								fw.write(System.getProperty("line.separator") + "migration=1"); 
								Log.info("Adding migration=1");
							}
							
							property = confProps.getProperty("migrationdestaddress");
							if(property == null) {
								fw.write(System.getProperty("line.separator") + "migrationdestaddress="+address); 
								Log.info("Adding migrationdestaddress="+ address);
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
					}
					
					restart();
				}
				catch(Exception ex) {
					Log.error("Error saving button, error:" +ex.toString());
				}
			}
		});

		pack();
	}
	
	private void restart() throws IOException, InterruptedException, WalletCallException {
		Log.info("Restarting.");
		this.setVisible(false);
		ZelCashSproutToSaplingDialog.this.parentFrame.restartDaemon(false, false);
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
	
	private void getMigrationInfo() {
		Log.info("getMigrationInfo - start");
		JsonObject migrationInfo;
		try {
			migrationInfo = this.clientCaller.getMigrationInfo();
			String status = migrationInfo.get("enabled").toString().toUpperCase().replaceAll("[\n\r\"]", "");
			if("TRUE".equals(status.toUpperCase())) {
				this.enabled.setText(langUtil.getString("dialog.zelcashsprouttosaplingdialog.enabled.true"));
				this.migrationEnabled = true;
				this.destination_address.setEnabled(false);
			}
			else {
				this.destination_address.setEnabled(true);
				this.migrationEnabled = false;
				this.enabled.setText(langUtil.getString("dialog.zelcashsprouttosaplingdialog.enabled.false"));
			}
			this.unmigrated_amount.setText(migrationInfo.get("unmigrated_amount").toString().toUpperCase().replaceAll("[\n\r\"]", ""));
			this.unfinalized_migrated_amount.setText(migrationInfo.get("unfinalized_migrated_amount").toString().toUpperCase().replaceAll("[\n\r\"]", ""));
			this.finalized_migrated_amount.setText(migrationInfo.get("finalized_migrated_amount").toString().toUpperCase().replaceAll("[\n\r\"]", ""));
			this.finalized_migration_transactions.setText(migrationInfo.get("finalized_migration_transactions").toString().toUpperCase().replaceAll("[\n\r\"]", ""));
			String time_started = migrationInfo.get("time_started") == null ? "" : migrationInfo.get("time_started").toString().toUpperCase().replaceAll("[\n\r\"]", "");
			if("".equals(time_started)) {
				this.time_started.setText("");
			}
			else {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date aux = new Date(Long.parseLong(time_started) * 1000);
				this.time_started.setText(formatter.format(aux));
			}
			String destination_address = migrationInfo.get("destination_address").toString().replaceAll("[\n\r\"]", "");
			for(int i=0; i<listOfSapling.size(); ++i) {
				if(destination_address.equals(listOfSapling.get(i))) {
					this.destination_address.setSelectedIndex(i);
					break;
				}
			}
			this.migration_txids.setText(migrationInfo.get("migration_txids").toString().replaceAll("[\n\r\"]", "").replaceAll(",", ",\n"));
		} catch (WalletCallException | IOException | InterruptedException e) {
			Log.error("getMigrationInfo error:"+e.toString());
		}
		
		Log.info("getMigrationInfo - end");
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
	
	public void disposeMenu() {
		this.setVisible(false);
		this.dispose();	
	}
	
	public static void removeMigratioInfoFromConfFile() {
		Log.info("Removing migration attributes from zelcash.conf");
		removeEmptyLinesFromConfigurationFile();
	    try {
	    	String blockchainDir = OSUtil.getBlockchainDirectory();
			File ZelCashConf = new File(blockchainDir + File.separator + "zelcash.conf");
		  if (!ZelCashConf.exists()) {
			  Log.error("Could not find file: {0} !", ZelCashConf.getAbsolutePath());
			  return;
	      }
		  BufferedReader br = new BufferedReader(new FileReader(ZelCashConf));
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
						if(!st.toLowerCase().startsWith("migration") && !st.toLowerCase().startsWith("migrationdestaddress")) {
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
		  FileWriter writer = new FileWriter(ZelCashConf);
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
	    	Log.error("Error deleting migratioInfo:"+ ex.getMessage());
	    }
	    finally {
			Log.info("Migration removed from configuration file.");
		}
	}
	
	public static void removeEmptyLinesFromConfigurationFile() {
		try {
	    	String blockchainDir = OSUtil.getBlockchainDirectory();
			File ZelCashConf = new File(blockchainDir + File.separator + "zelcash.conf");
		  if (!ZelCashConf.exists()) {
			  Log.error("Could not find file: {0} !", ZelCashConf.getAbsolutePath());
			  return;
	      }
		  BufferedReader br = new BufferedReader(new FileReader(ZelCashConf));
		  ArrayList<String> coll = new ArrayList<String>();
		  try {
			  
		      String st = null;

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
		  FileWriter writer = new FileWriter(ZelCashConf);
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
	    	Log.error("Error deleting empty lines from zelcash.conf:" + ex.getMessage());
	    }
	}
		    
} 