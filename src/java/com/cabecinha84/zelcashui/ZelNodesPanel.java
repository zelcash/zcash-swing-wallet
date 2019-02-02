package com.cabecinha84.zelcashui;

import java.awt.BorderLayout;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
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

/**
 * Provides the functionality for sending cash
 */
public class ZelNodesPanel extends WalletTabPanel {
	private StatusUpdateErrorReporter errorReporter;
	private ZCashInstallationObserver installationObserver;
	private LabelStorage labelStorage;
	private LanguageUtil langUtil;
	private ZelCashJFrame parentFrame;
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

	public ZelNodesPanel(ZelCashJFrame parentFrame, ZelCashJTabbedPane parentTabs, ZCashClientCaller clientCaller,
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
		columnNames.add(langUtil.getString("dialog.zelcashnewzelnode.name"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.ip"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.status"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.tier"));
		columnNames.add(langUtil.getString("zelnodespanel.zelnodes.activetime"));
		//columnNames.add(langUtil.getString("zelnodespanel.zelnodes.laspaid"));
		
		myZelNodesTable = new ZelCashJTable(dataList, columnNames);
		myZelNodesTable.setAutoCreateRowSorter(true);
		

		this.gelMyZelNodeList();
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
		refresh = new ZelCashJButton(langUtil.getString("panel.address.button.refresh"));
		buttonPanel.add(refresh);
		zelNodesPanel.add(buttonPanel,BorderLayout.SOUTH);

		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refresh.setText(langUtil.getString("zelnodespanel.zelnodes.button.loading"));
				refresh.setEnabled(false);
				refreshZelNodesTables();
				refresh.setText(langUtil.getString("panel.address.button.refresh"));
				refresh.setEnabled(true);
				
			}
		});
	}
	
	private void refreshZelNodesTables() {
		Log.info("refreshZelNodesTables start");
		gelMyZelNodeList();
		gelZelNodeList();
		ZelNodesPanel.this.revalidate();
		ZelNodesPanel.this.repaint();
		Log.info("refreshZelNodesTables end");
	}

	private void gelMyZelNodeList() {
		Log.info("gelMyZelNodeList start");
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
						if(st.contains("#") || emptyLine.equals("")) {
							continue;
						}
						else {
							++myZelNodeCount;
							data = new Vector<>();
							zelNodeInfo = st.split("\\s+");
							data.add(zelNodeInfo[0]);
							data.add(zelNodeInfo[1]);
							gelZelNodeStatus(zelNodeInfo[3], data);
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
			
			ZelCashJMenuItem start = new ZelCashJMenuItem(langUtil.getString("zelnodespanel.myzelnodes.mouse.start"));
	        popupMenu.add(start);
	        
	        start.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, accelaratorKeyMask));
	        start.addActionListener(new ActionListener() 
	        {	
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					if ((lastRow >= 0) && (lastColumn >= 0))
					{
						String zelNodeALias = ZelNodesPanel.this.myZelNodesTable.getValueAt(lastRow, 0).toString();
					
						try {
							JsonObject response = clientCaller.startZelNode(zelNodeALias);
							JsonArray detailResponse = response.get("detail").asArray();
							JsonObject jsonObj = detailResponse.get(0).asObject();
							String result = jsonObj.get("result").toString().toUpperCase().replaceAll("[\n\r\"]", "");
							if(result.contains("FAILED")) {
								String error = jsonObj.get("errorMessage").toString().replaceAll("[\n\r\"]", "");
								JOptionPane.showMessageDialog(
				                        null,
				                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.start.error", error),
				                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.start.title"),
				                        JOptionPane.ERROR_MESSAGE);
							}
							else {
								JOptionPane.showMessageDialog(
				                        null,
				                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.start.success"),
				                        LanguageUtil.instance().getString("dialog.zelcashnewzelnode.start.title"),
				                        JOptionPane.INFORMATION_MESSAGE);
							}
							refreshZelNodesTables();
						} catch (WalletCallException | IOException | InterruptedException e1) {
							Log.error("Error calling startZelNode: "+e1.getMessage());
						}
					} else
					{
						// Log perhaps
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
					if ((lastRow >= 0) && (lastColumn >= 0))
					{
						String zelNodeAlias = ZelNodesPanel.this.myZelNodesTable.getValueAt(lastRow, 0).toString();
						try {
							ZelCashZelNodeDialog ad = new ZelCashZelNodeDialog(ZelNodesPanel.this.parentFrame, clientCaller, installationObserver, zelNodeAlias, labelStorage);
							ad.setVisible(true);
							refreshZelNodesTables();
						} catch (Exception uee) {
							Log.error("Unexpected error: ", uee);
						}
					} else
					{
						// Log perhaps
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
					if ((lastRow >= 0) && (lastColumn >= 0))
					{
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
						
						if (option == 1)
			    	    {
			    	    	return;
			    	    }
						ZelCashZelNodeDialog.removeZelNode(zelNodeAlias);
						refreshZelNodesTables();
					} else
					{
						// Log perhaps
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
		
		Log.info("gelMyZelNodeList end - myZelNodeCount:"+myZelNodeCount);
	}
	
	private void gelZelNodeStatus(String zelNodeTxHash, Vector<String> data) {
		Log.info("gelZelNodeStatus start");
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
				return;
			}

			data.add(langUtil.getString("zelnodespanel.myzelnodes.status.missing"));
			data.add("-1");
			data.add("-1");

		} catch (WalletCallException | IOException | InterruptedException e1) {
			Log.error("Error obtaining zelNodeList. " + e1.getMessage());
		}
		Log.info("gelZelNodeStatus end");
	}

	
	private void gelZelNodeList() {
		Log.info("gelZelNodeList start");
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
				data.add(jsonObj.get("ip").toString().replaceAll("[\n\r\"]", ""));
				
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
			Log.info("gelZelNodeList end - count:" + totalNodes);
		}
	}

}
