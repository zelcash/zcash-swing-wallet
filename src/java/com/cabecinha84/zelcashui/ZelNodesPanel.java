package com.cabecinha84.zelcashui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.vaklinov.zcashui.LabelStorage;
import com.vaklinov.zcashui.LanguageUtil;
import com.vaklinov.zcashui.Log;
import com.vaklinov.zcashui.OSUtil;
import com.vaklinov.zcashui.StatusUpdateErrorReporter;
import com.vaklinov.zcashui.WalletTabPanel;
import com.vaklinov.zcashui.ZCashClientCaller;
import com.vaklinov.zcashui.ZCashClientCaller.WalletCallException;
import com.vaklinov.zcashui.ZCashInstallationObserver;
import com.vaklinov.zcashui.ZCashUI;

/**
 * Provides the functionality for sending cash
 */
public class ZelNodesPanel extends WalletTabPanel {
	private StatusUpdateErrorReporter errorReporter;
	private ZCashInstallationObserver installationObserver;
	private LabelStorage labelStorage;
	private LanguageUtil langUtil;
	private ZCashUI parentFrame;
	private ZCashClientCaller clientCaller;
	private ZelCashJTable zelNodesTable = null;
	private ZelCashJScrollPane zelNodesTablePane = null;

	private ZelCashJTable myZelNodesTable = null;
	private ZelCashJScrollPane myZelNodesTablePane = null;
	
	private ZelCashJLabel zelnodescount = null;
	private ZelCashJLabel myzelnodescount = null;

	private ZelCashJPanel myzelnodes = null;
	private ZelCashJPanel zelnodes = null;
	
	protected int lastRow = -1;
	protected int lastColumn = -1;
	
	protected ZelCashJPopupMenu popupMenu;
	
	ZelCashJButton refresh;
	ZelCashJButton collectZelNodeReward;
	
	private int utxoRewardCount = 0;
	private static double zelnodeRewardAvailable = 0;
	private static int MAX_UTXO_TXN = 100;

	public ZelNodesPanel(ZCashUI parentFrame, ZelCashJTabbedPane parentTabs, ZCashClientCaller clientCaller,
			StatusUpdateErrorReporter errorReporter, LabelStorage labelStorage)
			throws IOException, InterruptedException, WalletCallException {
		this.parentFrame = parentFrame;
		this.clientCaller = clientCaller;
		this.errorReporter = errorReporter;
		this.labelStorage = labelStorage;
		this.langUtil = LanguageUtil.instance();

		// Build content
		ZelCashJPanel zelNodesPanel = this;
		zelNodesPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		zelNodesPanel.setLayout(new BorderLayout(0, 0));
		
		ZelCashJPanel detailsPanel = new ZelCashJPanel();
		detailsPanel.setLayout(new GridLayout(1,2));
		
		myzelnodes = new ZelCashJPanel();
		myzelnodescount = new ZelCashJLabel();
		myzelnodescount.setText(langUtil.getString("zelnodespanel.myzelnodes.count","0"));
		myzelnodes.add(myzelnodescount);
		myZelNodesTablePane = new ZelCashJScrollPane();
		Vector<Vector<String>> dataList = new Vector<>();
		Vector<String> columnNames = new Vector<>();
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.alias"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.ip"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.status"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.tier"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.activetime"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.laspaid"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.rank"));
		
		myZelNodesTable = new ZelCashJTable(dataList, columnNames);
		myZelNodesTable.setAutoCreateRowSorter(true);
		

		this.getMyZelNodeList();
		myZelNodesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		myzelnodes.add(myZelNodesTablePane = new ZelCashJScrollPane(myZelNodesTable), BorderLayout.CENTER);
		
		detailsPanel.add(myzelnodes);
		
		
		zelnodes = new ZelCashJPanel();
		zelnodescount = new ZelCashJLabel();
		zelnodescount.setText(langUtil.getString("zelnodespanel.zelnodes.count","0", "0", "0", "0"));
		zelnodes.add(zelnodescount);
		zelNodesTablePane = new ZelCashJScrollPane();
		
		dataList = new Vector<>();
		columnNames = new Vector<>();
		//columnNames.add(langUtil.getString("zelnodespanel.zelnodes.rank"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.ip"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.status"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.tier"));
		//columnNames.add(langUtil.getString("zelnodespanel.zelnodes.version"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.activetime"));
		//columnNames.add(langUtil.getString("zelnodespanel.zelnodes.lastseen"));
		//columnNames.add(langUtil.getString("zelnodespanel.zelnodes.laspaid"));

		zelNodesTable = new ZelCashJTable(dataList, columnNames);
		zelNodesTable.setAutoCreateRowSorter(true);
		
		this.gelZelNodeList();
		zelNodesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		zelnodes.add(zelNodesTablePane = new ZelCashJScrollPane(zelNodesTable), BorderLayout.CENTER);

		
		detailsPanel.add(zelnodes);
		
		zelNodesPanel.add(detailsPanel,BorderLayout.CENTER);
		
		// Build panel of buttons
		ZelCashJPanel buttonPanel = new ZelCashJPanel();
		buttonPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		collectZelNodeReward = new ZelCashJButton(langUtil.getString("panel.zelnodespanel.button.colletReward"));
		buttonPanel.add(collectZelNodeReward);
		refresh = new ZelCashJButton(langUtil.getString("panel.address.button.refresh"));
		buttonPanel.add(refresh);
		zelNodesPanel.add(buttonPanel,BorderLayout.SOUTH);

		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Cursor oldCursor = ZelNodesPanel.this.getCursor();
				try {
					ZelNodesPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					refresh.setText(langUtil.getString("zelnodespanel.zelnodes.button.loading"));
					refresh.setEnabled(false);
					refreshZelNodesTables();
					refresh.setText(langUtil.getString("panel.address.button.refresh"));
					refresh.setEnabled(true);
					
				}
				finally {
					ZelNodesPanel.this.setCursor(oldCursor);
				}
				
				
			}
		});
		
		collectZelNodeReward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Cursor oldCursor = ZelNodesPanel.this.getCursor();
				try {
					ZelNodesPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					getZelNodesRewards();	
				}
				finally {
					ZelNodesPanel.this.setCursor(oldCursor);
				}
				
				
			}
		});
	}
	
	private void getZelNodesRewards() {
		try {
			JsonArray ja = clientCaller.getCollectableZelNodeRewardsInformation();
			utxoRewardCount = 0;
			zelnodeRewardAvailable = 0;
			boolean generated = false;
			boolean spendable = false;
			for(int i=0; i < ja.size(); ++i) {
				JsonObject jsonObj = ja.get(i).asObject();
				generated = Boolean.parseBoolean(jsonObj.get("generated").toString());
				spendable = Boolean.parseBoolean(jsonObj.get("spendable").toString());
				if(generated && spendable) {
					utxoRewardCount++;
					zelnodeRewardAvailable+= Double.parseDouble(jsonObj.get("amount").toString().replaceAll("[\n\r\"]", ""));
				}
				
			}
			
			if(utxoRewardCount == 0) {
				JOptionPane.showMessageDialog(null,
						LanguageUtil.instance().getString("panel.zelnodespanel.no.utxo.to.collect"),
						LanguageUtil.instance().getString("panel.zelnodespanel.no.utxo.to.collect.title"),
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			// Z Addresses - they are OK
			String[] zAddresses = clientCaller.getWalletZAddresses();
			if(zAddresses.length == 0) {
				JOptionPane.showMessageDialog(null,
						LanguageUtil.instance().getString("panel.zelnodespanel.no.zaddress.collect"),
						LanguageUtil.instance().getString("panel.zelnodespanel.no.zaddress.collect.title"),
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String [] comboBoxItems = new String[zAddresses.length];
			String label;
			String address;
			for (int i = 0; i < zAddresses.length; i++)
			{
				label = this.labelStorage.getLabel(zAddresses[i]);
				address = zAddresses[i];
				if ((label != null) && (label.length() > 0))
				{
					address = label + " - " + address;
				}
				comboBoxItems[i] = address;
			}
			
			ZelCashJComboBox zAddressesCombo = new ZelCashJComboBox<>(comboBoxItems);
				
			ZelCashJPanel myPanel = new ZelCashJPanel();
        	myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        	
        	ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
    		tempPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    		ZelCashJLabel infoLabel = new ZelCashJLabel(langUtil.getString("panel.zelnodespanel.collect.zelnodereward.info", new DecimalFormat("########0.00######").format(zelnodeRewardAvailable), utxoRewardCount));
        	tempPanel.add(infoLabel, BorderLayout.CENTER);
    	    
    	    ZelCashJPanel detailsPanel = new ZelCashJPanel();
    		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
    	    
        	zAddressesCombo.setPreferredSize(new Dimension(600,zAddressesCombo.getPreferredSize().height));
        	detailsPanel.add(zAddressesCombo, BorderLayout.CENTER);

        	myPanel.add(tempPanel);
        	myPanel.add(detailsPanel);
        	
            int result = JOptionPane.showConfirmDialog(ZelNodesPanel.this,
            		myPanel,
                    langUtil.getString("panel.zelnodespanel.collect.zelnodereward.info.title"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.CANCEL_OPTION);
            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
            	return;
            }
            
            String amountShielded;
            try {
            	String zAddressSelected = zAddresses[zAddressesCombo.getSelectedIndex()];
            	JsonObject shieldResult = clientCaller.zShieldCoinBase(zAddressSelected, MAX_UTXO_TXN);
            	amountShielded = shieldResult.get("shieldingValue").toString();
            }
            catch (Exception e) {
            	JOptionPane.showMessageDialog(null,
            			LanguageUtil.instance().getString("panel.zelnodespanel.shielding.error", e.getMessage()),
    					LanguageUtil.instance().getString("panel.zelnodespanel.shielding.error.title"),
    					JOptionPane.ERROR_MESSAGE);
            	throw e;
			}
            
            String message = "";
            if(utxoRewardCount<MAX_UTXO_TXN) {
            	message = LanguageUtil.instance().getString("panel.zelnodespanel.shielding.success");
            }
            else {
            	message = LanguageUtil.instance().getString("panel.zelnodespanel.shielding.success.more", amountShielded, utxoRewardCount - MAX_UTXO_TXN);
            }
            JOptionPane.showMessageDialog(null,
            		message,
					LanguageUtil.instance().getString("panel.zelnodespanel.shielding.success.title"),
					JOptionPane.INFORMATION_MESSAGE);

		} catch (WalletCallException | IOException | InterruptedException e ) {
			Log.error("Error on getZelNodesRewards:" +e.getMessage());
		}

	}
	
	private void refreshZelNodesTables() {
		long start = System.currentTimeMillis();
		getMyZelNodeList();
		gelZelNodeList();
		ZelNodesPanel.this.revalidate();
		ZelNodesPanel.this.repaint();
		long end = System.currentTimeMillis();
		Log.info("refresh ZelNodesTables data done in " + (end - start) + "ms." );
		
	}

	private void getMyZelNodeList() {
		long start = System.currentTimeMillis();
		ZelCashJTable table = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String format = null;
		Vector<Vector<String>> dataList = new Vector<>();
		int myZelNodeCount = 0;
		try {
			String blockchainDir = OSUtil.getBlockchainDirectory();
			File zelnodeConf = new File(blockchainDir + File.separator + "zelnode.conf");
			if (!zelnodeConf.exists())
			{
				Log.info("Could not find file: {0} !", zelnodeConf.getAbsolutePath());
			} 
			else {
				Log.info("File zelnode.conf found");
				BufferedReader br = new BufferedReader(new FileReader(zelnodeConf)); 
				DefaultTableModel dtm = (DefaultTableModel) myZelNodesTable.getModel();
				
				try {
					String st; 
					String emptyLine;
					String alias;
					String zelnodekey;
					String[] zelNodeInfo;
					Vector<String> data;
					dtm.setRowCount(0);
					while ((st = br.readLine()) != null) {
						emptyLine = st.replaceAll(" ", "").replaceAll("(?m)^\\\\s*\\\\r?\\\\n|\\\\r?\\\\n\\\\s*(?!.*\\\\r?\\\\n)", "");						
						if(st.startsWith("#") || emptyLine.equals("")) {
							continue;
						}
						else {
							++myZelNodeCount;
							data = new Vector<>();
							zelNodeInfo = st.split("\\s+");
							data.add(zelNodeInfo[0]);
							data.add(zelNodeInfo[1]);
							gelZelNodeStatus(zelNodeInfo[0], zelNodeInfo[3], data);
							dtm.addRow(data);
							dataList.add(data);
						}
					}
				}
				finally {
					dtm.fireTableDataChanged();
					if(br!=null) {
						br.close();
					}
				}
				
			} 
			
			
			myzelnodescount.setText(langUtil.getString("zelnodespanel.myzelnodes.count",dataList.size()));
			
			popupMenu = new ZelCashJPopupMenu();
			int accelaratorKeyMask = Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask();
			
			ZelCashJMenuItem startZelnode = new ZelCashJMenuItem(langUtil.getString("zelnodespanel.myzelnodes.mouse.start"));
	        popupMenu.add(startZelnode);
	        
	        startZelnode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, accelaratorKeyMask));
	        startZelnode.addActionListener(new ActionListener() 
	        {	
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					int row = ZelNodesPanel.this.myZelNodesTable.getSelectedRow();
					String zelNodeALias = ZelNodesPanel.this.myZelNodesTable.getValueAt(row, 0).toString();
				
					try {
						long start = System.currentTimeMillis();
						JsonObject response = clientCaller.startZelNode(zelNodeALias);
						long end = System.currentTimeMillis();
						Log.info("Start zelnodealias: "+zelNodeALias+" done in " + (end - start) + "ms.");
						JsonArray detailResponse = response.get("detail").asArray();
						JsonObject jsonObj = detailResponse.get(0).asObject();
						String result = jsonObj.get("result").toString().toUpperCase().replaceAll("[\n\r\"]", "");
						if(result.contains("FAILED")) {
							String error = jsonObj.get("errorMessage").toString().replaceAll("[\n\r\"]", "");
							JOptionPane.showMessageDialog(
			                        null,
			                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.start.error", zelNodeALias, error),
			                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.start.title"),
			                        JOptionPane.ERROR_MESSAGE);
						}
						else {
							JOptionPane.showMessageDialog(
			                        null,
			                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.start.success", zelNodeALias),
			                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.start.title"),
			                        JOptionPane.INFORMATION_MESSAGE);
						}
						getMyZelNodeList();
						ZelNodesPanel.this.revalidate();
						ZelNodesPanel.this.repaint();
					} catch (WalletCallException | IOException | InterruptedException e1) {
						Log.error("Error calling startZelNode: "+e1.getMessage());
					}
				}
			});
	        
	        ZelCashJMenuItem edit = new ZelCashJMenuItem(langUtil.getString("zelnodespanel.myzelnodes.mouse.edit"));
	        popupMenu.add(edit);
	        
	        edit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, accelaratorKeyMask));
	        edit.addActionListener(new ActionListener() 
	        {	
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					int row = ZelNodesPanel.this.myZelNodesTable.getSelectedRow();
					String zelNodeAlias = ZelNodesPanel.this.myZelNodesTable.getValueAt(row, 0).toString();
					try {
						ZelCashZelNodeDialog ad = new ZelCashZelNodeDialog(ZelNodesPanel.this.parentFrame, clientCaller, installationObserver, zelNodeAlias, labelStorage);
						ad.setVisible(true);
						getMyZelNodeList();
						ZelNodesPanel.this.revalidate();
						ZelNodesPanel.this.repaint();
					} catch (Exception uee) {
						Log.error("Unexpected error: ", uee);
					}

				}
			});
	        
	        ZelCashJMenuItem delete = new ZelCashJMenuItem(langUtil.getString("zelnodespanel.myzelnodes.mouse.delete"));
	        popupMenu.add(delete);
	        
	        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, accelaratorKeyMask));
	        delete.addActionListener(new ActionListener() 
	        {	
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					int row = ZelNodesPanel.this.myZelNodesTable.getSelectedRow();
					String zelNodeAlias = ZelNodesPanel.this.myZelNodesTable.getValueAt(lastRow, 0).toString();
					Object[] options = 
			        	{ 
			        		langUtil.getString("send.cash.panel.option.pane.confirm.operation.button.yes"),
			        		langUtil.getString("send.cash.panel.option.pane.confirm.operation.button.no")
			        	};
					int option;
					option = JOptionPane.showOptionDialog(
		    				ZelNodesPanel.this.getRootPane().getParent(), 
		    				langUtil.getString("dialog.zelcashnewzelnode.delete.message", 
		    						           zelNodeAlias), 
		    			    langUtil.getString("zelnodespanel.myzelnodes.mouse.delete"),
		    			    JOptionPane.DEFAULT_OPTION, 
		    			    JOptionPane.QUESTION_MESSAGE,
		    			    null, 
		    			    options, 
		    			    options[1]);
					
					if (option == 0)
		    	    {
						ZelCashZelNodeDialog.removeZelNode(zelNodeAlias);
						getMyZelNodeList();
						ZelNodesPanel.this.revalidate();
						ZelNodesPanel.this.repaint();
						option = JOptionPane.showOptionDialog(null,
								LanguageUtil.instance().getString("wallet.zelnodes.delete.restart.message"),
								LanguageUtil.instance().getString("wallet.zelnodes.delete.restart.title"),
								JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
						if (option == 0) {
							JOptionPane.showMessageDialog(null,
									LanguageUtil.instance().getString("wallet.restart.message"),
									LanguageUtil.instance().getString("wallet.reindex.restart.title"),
									JOptionPane.INFORMATION_MESSAGE);
							ZelNodesPanel.this.parentFrame.restartDaemon(false);
							try {
								restartUI();
							} catch (IOException | InterruptedException | WalletCallException e1) {
								Log.error("Error restarting the UI, the wallet will be closed. Error:"+e1.getMessage());
								System.exit(1);
							}
						}
		    	    }
				}
			});
	              
	        
	        ZelNodesPanel.this.myZelNodesTable.addMouseListener(new MouseAdapter()
	        {
	        	public void mousePressed(MouseEvent e)
	        	{
	                if ((!e.isConsumed()) && e.isPopupTrigger())
	                {
	                	ZelCashJTable table = (ZelCashJTable)e.getSource();
	                    lastColumn = table.columnAtPoint(e.getPoint());
	                    lastRow = table.rowAtPoint(e.getPoint());
	                    
	                    if (!table.isRowSelected(lastRow))
	                    {
	                        table.changeSelection(lastRow, lastColumn, false, false);
	                    }

	                	popupMenu.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
	                    e.consume();
	                    
	                } else
	                {
	                	lastColumn = -1;
	                	lastRow    = -1;
	                }
	        	}
	        	
	            public void mouseReleased(MouseEvent e)
	            {
	            	if ((!e.isConsumed()) && e.isPopupTrigger())
	            	{
	            		mousePressed(e);
	            	}
	            }
	        });
		} catch (IOException e1) {
			Log.error("Error obtaining zelNodeList. " + e1.getMessage());
		}
		
		long end = System.currentTimeMillis();
		Log.info("refresh MyZelNodeList data done in " + (end - start) + "ms. myZelNodeCount:"+myZelNodeCount);
		
	}
	
	private void gelZelNodeStatus(String zelnodeAlias, String zelNodeTxHash, Vector<String> data) {
		long start = System.currentTimeMillis();
		ZelCashJTable table = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String format = null;
		try {
			JsonArray ja = clientCaller.getZelNodeStatus(zelNodeTxHash.trim());
			if(ja.size()>0) {
				JsonObject jsonObj = ja.get(0).asObject();
				data.add(jsonObj.get("status").toString().replaceAll("[\n\r\"]", ""));
				data.add(jsonObj.get("tier").toString().replaceAll("[\n\r\"]", ""));
				String activeTime = jsonObj.get("activetime").toString().replaceAll("[\n\r\"]", "");
				if(activeTime == null || activeTime == "" || activeTime == "0") {
					data.add("-1");
				}
				else {
					Date aux = new Date( System.currentTimeMillis() - (Long.parseLong(activeTime) * 1000));
					format = formatter.format(aux);
					data.add(format);
				}
				String lastPaid = jsonObj.get("lastpaid").toString().replaceAll("[\n\r\"]", "");
				if(lastPaid == null || lastPaid == "" || lastPaid == "0") {
					data.add("-1");
				}
				else {
					Date aux = new Date(Long.parseLong(lastPaid) * 1000);
					format = formatter.format(aux);
					data.add(format);
				}
				data.add(jsonObj.get("rank").toString().replaceAll("[\n\r\"]", ""));
				return;
			}

			data.add(langUtil.getString("zelnodespanel.myzelnodes.status.missing"));
			data.add("-1");
			data.add("-1");
			data.add("-1");
			data.add("-1");

		} catch (WalletCallException | IOException | InterruptedException e1) {
			Log.error("Error obtaining zelnode "+zelnodeAlias+" status. Error:" + e1.getMessage());
		}
		finally {
			long end = System.currentTimeMillis();
			Log.info("gelZelNodeStatus "+zelnodeAlias+" status refresh in " + (end - start) + "ms." );
		}	
	}

	
	private void gelZelNodeList() {
		long start = System.currentTimeMillis();
		ZelCashJTable table = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String format = null;
		DefaultTableModel dtm = (DefaultTableModel) zelNodesTable.getModel();
		int basicCount=0;
		int superCount=0;
		int bamfCount=0;
		int totalNodes=0;
		String tier;
		try {
			JsonArray ja = clientCaller.getZelNodeList();
			Vector<Vector<String>> dataList = new Vector<>();
			dtm.setRowCount(0);
			for (int i = 0; i < ja.size(); i++) {

				JsonObject jsonObj = ja.get(i).asObject();
				Vector<String> data = new Vector<>();
				
				//data.add(jsonObj.get("rank").toString().replaceAll("[\n\r\"]", ""));
				data.add(jsonObj.get("ipaddress").toString().replaceAll("[\n\r\"]", ""));
				
				data.add(jsonObj.get("status").toString().replaceAll("[\n\r\"]", ""));
				//data.add(jsonObj.get("version").toString().replaceAll("[\n\r\"]", ""));
				
				tier = jsonObj.get("tier").toString().replaceAll("[\n\r\"]", "");
				data.add(tier);
				
				String activeTime = jsonObj.get("activetime").toString().replaceAll("[\n\r\"]", "");
				if(activeTime == null || activeTime == "" || activeTime == "0") {
					data.add("-1");
				}
				else {
					Date aux = new Date( System.currentTimeMillis() - (Long.parseLong(activeTime) * 1000));
					format = formatter.format(aux);
					data.add(format);
				}
				
				/*String lastpaid = jsonObj.get("lastpaid").toString().replaceAll("[\n\r\"]", "");
				if(lastpaid == null || lastpaid == "" || lastpaid == "0") {
					data.add("-1");
				}
				else {
					Date aux = new Date(Long.parseLong(lastpaid) * 1000);
					format = formatter.format(aux);
					data.add(format);
				}*/
				
				dataList.add(data);
				dtm.addRow(data);
				if("basic".equals(tier.toLowerCase())) {
					++basicCount;
				}
				else if("super".equals(tier.toLowerCase())) {
					++superCount;
				}
				else {
					++bamfCount;
				}
			}
			totalNodes = dataList.size();
			zelnodescount.setText(langUtil.getString("zelnodespanel.zelnodes.count",totalNodes, basicCount, superCount, bamfCount));

		} catch (WalletCallException | IOException | InterruptedException e1) {
			Log.error("Error obtaining zelNodeList. " + e1.getMessage());
		}
		finally {
			dtm.fireTableDataChanged();
			long end = System.currentTimeMillis();
			Log.info("refresh ZelNodeList data done in " + (end - start) + "ms. count:" + totalNodes);
		}
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
	
	public void restartUI() throws IOException, InterruptedException, WalletCallException {
		Log.info("Restarting the UI.");
		ZCashUI z = new ZCashUI(null);
		this.parentFrame.setVisible(false);
		this.parentFrame.dispose();
		this.parentFrame = z;	
		this.parentFrame.repaint();
		this.parentFrame.setVisible(true);
		this.setVisible(false);
	}

}
