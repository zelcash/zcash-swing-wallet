/************************************************************************************************
 *   ____________ _   _  _____          _      _____ _    _ _______          __   _ _      _   
 *  |___  /  ____| \ | |/ ____|        | |    / ____| |  | |_   _\ \        / /  | | |    | |  
 *     / /| |__  |  \| | |     __ _ ___| |__ | |  __| |  | | | |  \ \  /\  / /_ _| | | ___| |_ 
 *    / / |  __| | . ` | |    / _` / __| '_ \| | |_ | |  | | | |   \ \/  \/ / _` | | |/ _ \ __|
 *   / /__| |____| |\  | |___| (_| \__ \ | | | |__| | |__| |_| |_   \  /\  / (_| | | |  __/ |_ 
 *  /_____|______|_| \_|\_____\__,_|___/_| |_|\_____|\____/|_____|   \/  \/ \__,_|_|_|\___|\__|
 *                                       
 * Copyright (c) 2016-2018 The ZEN Developers
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
package com.vaklinov.zcashui;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cabecinha84.zelcashui.AppLock;
import com.cabecinha84.zelcashui.ZelCashJFrame;
import com.cabecinha84.zelcashui.ZelCashJMenu;
import com.cabecinha84.zelcashui.ZelCashJMenuBar;
import com.cabecinha84.zelcashui.ZelCashJMenuItem;
import com.cabecinha84.zelcashui.ZelCashJTabbedPane;
import com.cabecinha84.zelcashui.ZelCashZelNodeDialog;
import com.cabecinha84.zelcashui.ZelCashUI;
import com.cabecinha84.zelcashui.ZelCashUIEditDialog;
import com.cabecinha84.zelcashui.ZelNodesPanel;
import com.vaklinov.zcashui.OSUtil.OS_TYPE;
import com.vaklinov.zcashui.ZCashClientCaller.NetworkAndBlockchainInfo;
import com.vaklinov.zcashui.ZCashClientCaller.WalletCallException;
import com.vaklinov.zcashui.ZCashInstallationObserver.DAEMON_STATUS;
import com.vaklinov.zcashui.ZCashInstallationObserver.DaemonInfo;
import com.vaklinov.zcashui.ZCashInstallationObserver.InstallationDetectionException;
import com.vaklinov.zcashui.msg.MessagingPanel;

/**
 * Main ZelCash Window.
 */
public class ZCashUI extends ZelCashJFrame {
	public static final long THREAD_WAIT_1_SECOND = 1000;
	public static final long THREAD_WAIT_5_SECONDS = 5000;
	private static final String REGEXIPV4IPV6 = "/((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))/";
	private ZCashInstallationObserver installationObserver;
	private ZCashClientCaller clientCaller;
	private LabelStorage labelStorage;
	private StatusUpdateErrorReporter errorReporter;

	private WalletOperations walletOps;

	private ZelCashJMenuItem menuItemExit;
	private ZelCashJMenuItem menuItemAbout;
	private ZelCashJMenuItem menuItemZelcashUI;
	private ZelCashJMenuItem menuItemEncrypt;
	private ZelCashJMenuItem menuItemChangePassword;
	private ZelCashJMenuItem menuItemBackup;
	private ZelCashJMenuItem menuItemExportKeys;
	private ZelCashJMenuItem menuItemImportKeys;
	private ZelCashJMenuItem menuItemShowPrivateKey;
	private ZelCashJMenuItem menuItemImportOnePrivateKey;
	private ZelCashJMenuItem menuItemOwnIdentity;
	private ZelCashJMenuItem menuItemExportOwnIdentity;
	private ZelCashJMenuItem menuItemImportContactIdentity;
	private ZelCashJMenuItem menuItemAddMessagingGroup;
	private ZelCashJMenuItem menuItemRemoveContactIdentity;
	private ZelCashJMenuItem menuItemMessagingOptions;
	private ZelCashJMenuItem menuItemShareFileViaIPFS;
	private ZelCashJMenuItem menuItemExportToArizen;
	private ZelCashJMenuItem menuItemNewZelnode;

	public DashboardPanel dashboard;
	public TransactionsDetailPanel transactionDetailsPanel;
	public AddressesPanel addresses;
	public SendCashPanel sendPanel;
	public AddressBookPanel addressBookPanel;
	public MessagingPanel messagingPanel;
	public ZelNodesPanel zelNodesPanel;
	
	private LanguageUtil langUtil;

	private static File walletLock;
	private static FileChannel channel;
	private static FileLock lock;

	ZelCashJTabbedPane tabs;

	public ZCashUI(StartupProgressDialog progressDialog) throws IOException, InterruptedException, WalletCallException {
		langUtil = LanguageUtil.instance();

		this.setTitle(langUtil.getString("main.frame.title"));

		if (progressDialog != null) {
			progressDialog.setProgressText(langUtil.getString("main.frame.progressbar"));
		}

		ClassLoader cl = this.getClass().getClassLoader();

		this.setIconImage(new ImageIcon(cl.getResource("images/ZelCash-yellow.orange-logo.png")).getImage());

		Container contentPane = this.getContentPane();
		contentPane.setBackground(ZelCashUI.container);
		errorReporter = new StatusUpdateErrorReporter(this);
		installationObserver = new ZCashInstallationObserver(OSUtil.getProgramDirectory());
		clientCaller = new ZCashClientCaller(OSUtil.getProgramDirectory());

		if (installationObserver.isOnTestNet()) {
			this.setTitle(this.getTitle() + langUtil.getString("main.frame.title.testnet"));
		}

		// Build content
		tabs = new ZelCashJTabbedPane();
		Font oldTabFont = tabs.getFont();
		Font newTabFont = new Font(oldTabFont.getName(), Font.BOLD | Font.ITALIC, oldTabFont.getSize() * 57 / 50);
		tabs.setFont(newTabFont);
		BackupTracker backupTracker = new BackupTracker(this);
		labelStorage = new LabelStorage();
		tabs.addTab(langUtil.getString("main.frame.tab.overview.title"),
				new ImageIcon(cl.getResource("zelcashImages/overview.png")), dashboard = new DashboardPanel(this,
						installationObserver, clientCaller, errorReporter, backupTracker, labelStorage));
		tabs.addTab(langUtil.getString("main.frame.tab.transactions.title"),
				new ImageIcon(cl.getResource("zelcashImages/transactions.png")),
				transactionDetailsPanel = new TransactionsDetailPanel(this, tabs, installationObserver, clientCaller,
						errorReporter, dashboard.getTransactionGatheringThread(), labelStorage));
		this.dashboard.setDetailsPanelForSelection(this.transactionDetailsPanel);
		tabs.addTab(langUtil.getString("main.frame.tab.own.address.title"),
				new ImageIcon(cl.getResource("zelcashImages/own-addresses.png")),
				addresses = new AddressesPanel(this, clientCaller, errorReporter, labelStorage, installationObserver));
		tabs.addTab(langUtil.getString("main.frame.tab.send.cash.title"),
				new ImageIcon(cl.getResource("zelcashImages/send.png")),
				sendPanel = new SendCashPanel(clientCaller, errorReporter, installationObserver, backupTracker));
		tabs.addTab(langUtil.getString("main.frame.tab.address.book.title"),
				new ImageIcon(cl.getResource("zelcashImages/address-book.png")),
				addressBookPanel = new AddressBookPanel(sendPanel, tabs, labelStorage));
		tabs.addTab(langUtil.getString("main.frame.tab.messaging.title"),
				new ImageIcon(cl.getResource("zelcashImages/messaging.png")),
				messagingPanel = new MessagingPanel(this, sendPanel, tabs, clientCaller, errorReporter, labelStorage));
		tabs.addTab(langUtil.getString("main.frame.tab.zelnodes.title"),
				new ImageIcon(cl.getResource("zelcashImages/zelNodes.png")),
				zelNodesPanel = new ZelNodesPanel(this, tabs, clientCaller, errorReporter, labelStorage));
		contentPane.add(tabs);

		this.walletOps = new WalletOperations(this, tabs, dashboard, addresses, sendPanel, installationObserver,
				clientCaller, errorReporter, backupTracker);

		// Build menu
		ZelCashJMenuBar mb = new ZelCashJMenuBar();
		ZelCashJMenu file = new ZelCashJMenu(langUtil.getString("menu.label.main"));
		file.setMnemonic(KeyEvent.VK_M);
		int accelaratorKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		file.add(menuItemZelcashUI = new ZelCashJMenuItem(langUtil.getString("menu.label.zelcashui"), KeyEvent.VK_U));
		menuItemZelcashUI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, accelaratorKeyMask));
		file.add(menuItemAbout = new ZelCashJMenuItem(langUtil.getString("menu.label.about"), KeyEvent.VK_T));
		menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, accelaratorKeyMask));
		file.addSeparator();
		file.add(menuItemExit = new ZelCashJMenuItem(langUtil.getString("menu.label.quit"), KeyEvent.VK_Q));
		menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, accelaratorKeyMask));
		mb.add(file);

		ZelCashJMenu wallet = new ZelCashJMenu(langUtil.getString("menu.label.wallet"));
		wallet.setMnemonic(KeyEvent.VK_W);
		wallet.add(menuItemBackup = new ZelCashJMenuItem(langUtil.getString("menu.label.backup"), KeyEvent.VK_B));
		menuItemBackup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, accelaratorKeyMask));
		wallet.add(menuItemEncrypt = new
		ZelCashJMenuItem(langUtil.getString("menu.label.encrypt"), KeyEvent.VK_E));
		menuItemEncrypt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
		accelaratorKeyMask));
		wallet.add(menuItemChangePassword = new
				ZelCashJMenuItem(langUtil.getString("menu.label.changepassword"), KeyEvent.VK_J));
		menuItemChangePassword.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,
				accelaratorKeyMask));
		wallet.add(menuItemExportKeys = new ZelCashJMenuItem(langUtil.getString("menu.label.export.private.keys"),
				KeyEvent.VK_K));
		menuItemExportKeys.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, accelaratorKeyMask));
		wallet.add(menuItemImportKeys = new ZelCashJMenuItem(langUtil.getString("menu.label.import.private.keys"),
				KeyEvent.VK_I));
		menuItemImportKeys.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, accelaratorKeyMask));
		wallet.add(menuItemShowPrivateKey = new ZelCashJMenuItem(langUtil.getString("menu.label.show.private.key"),
				KeyEvent.VK_P));
		menuItemShowPrivateKey.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, accelaratorKeyMask));
		wallet.add(menuItemImportOnePrivateKey = new ZelCashJMenuItem(
				langUtil.getString("menu.label.import.one.private.key"), KeyEvent.VK_N));
		menuItemImportOnePrivateKey.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, accelaratorKeyMask));
		// wallet.add(menuItemExportToArizen = new
		// ZelCashJMenuItem(langUtil.getString("menu.label.export.to.arizen"),
		// KeyEvent.VK_A));
		// menuItemExportToArizen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
		// accelaratorKeyMask));
		mb.add(wallet);

		ZelCashJMenu messaging = new ZelCashJMenu(langUtil.getString("menu.label.messaging"));
		messaging.setMnemonic(KeyEvent.VK_S);
		messaging.add(menuItemOwnIdentity = new ZelCashJMenuItem(langUtil.getString("menu.label.own.identity"),
				KeyEvent.VK_D));
		menuItemOwnIdentity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, accelaratorKeyMask));
		messaging.add(menuItemExportOwnIdentity = new ZelCashJMenuItem(
				langUtil.getString("menu.label.export.own.identity"), KeyEvent.VK_L));
		menuItemExportOwnIdentity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, accelaratorKeyMask));
		messaging.add(menuItemAddMessagingGroup = new ZelCashJMenuItem(
				langUtil.getString("menu.label.add.messaging.group"), KeyEvent.VK_G));
		menuItemAddMessagingGroup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, accelaratorKeyMask));
		messaging.add(menuItemImportContactIdentity = new ZelCashJMenuItem(
				langUtil.getString("menu.label.import.contact.identity"), KeyEvent.VK_Y));
		menuItemImportContactIdentity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, accelaratorKeyMask));
		messaging.add(menuItemRemoveContactIdentity = new ZelCashJMenuItem(
				langUtil.getString("menu.label.remove.contact"), KeyEvent.VK_R));
		menuItemRemoveContactIdentity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, accelaratorKeyMask));
		messaging.add(menuItemMessagingOptions = new ZelCashJMenuItem(langUtil.getString("menu.label.options"),
				KeyEvent.VK_O));
		menuItemMessagingOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, accelaratorKeyMask));

		ZelCashJMenu shareFileVia = new ZelCashJMenu(langUtil.getString("menu.label.share.file"));
		shareFileVia.setMnemonic(KeyEvent.VK_V);
		// TODO: uncomment this for IPFS integration
		// messaging.add(shareFileVia);
		shareFileVia.add(
				menuItemShareFileViaIPFS = new ZelCashJMenuItem(langUtil.getString("menu.label.ipfs"), KeyEvent.VK_F));
		menuItemShareFileViaIPFS.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, accelaratorKeyMask));

		mb.add(messaging);
		
		ZelCashJMenu zelNodes = new ZelCashJMenu(langUtil.getString("menu.label.zelnodes"));
		zelNodes.setMnemonic(KeyEvent.VK_Z);
		zelNodes.add(menuItemNewZelnode = new ZelCashJMenuItem(langUtil.getString("menu.label.zelnodes.new"),
				KeyEvent.VK_Z));
		menuItemNewZelnode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, accelaratorKeyMask));
		
		mb.add(zelNodes);

		ActionListener languageSelectionAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Log.info("Action [" + e.getActionCommand() + "] performed");
					LanguageMenuItem item = (LanguageMenuItem) e.getSource();
					langUtil.updatePreferredLanguage(item.getLocale());
					JOptionPane.showMessageDialog(ZCashUI.this.getRootPane().getParent(),
							langUtil.getString("dialog.message.language.prefs.update"),
							langUtil.getString("dialog.message.language.prefs.update.title"),
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		ZelCashJMenu languageMenu = new ZelCashJMenu(langUtil.getString("menu.label.language"));
		// only english translation available
		/*
		 * LanguageMenuItem italian = new
		 * LanguageMenuItem(langUtil.getString("menu.label.language.italian"), new
		 * ImageIcon(cl.getResource("images/italian.png")), Locale.ITALY);
		 * italian.setHorizontalTextPosition(ZelCashJMenuItem.RIGHT);
		 * 
		 * italian.addActionListener(languageSelectionAction);
		 */

		LanguageMenuItem english = new LanguageMenuItem(langUtil.getString("menu.label.language.english"),
				new ImageIcon(cl.getResource("images/uk.png")), Locale.US);
		english.setHorizontalTextPosition(ZelCashJMenuItem.RIGHT);

		english.addActionListener(languageSelectionAction);

		ButtonGroup group = new ButtonGroup();
		// group.add(italian);
		group.add(english);

		// languageMenu.add(italian);
		languageMenu.add(english);

		// Temporarily disabled till translations are completed
		// mb.add(languageMenu);

		this.setJMenuBar(mb);

		// Add listeners etc.
		menuItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.exitProgram();
			}
		});

		menuItemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					AboutDialog ad = new AboutDialog(ZCashUI.this);
					ad.setVisible(true);
				} catch (UnsupportedEncodingException uee) {
					Log.error("Unexpected error: ", uee);
					ZCashUI.this.errorReporter.reportError(uee);
				}
			}
		});

		menuItemZelcashUI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ZelCashUIEditDialog ad = new ZelCashUIEditDialog(ZCashUI.this);
					ad.setVisible(true);
				} catch (UnsupportedEncodingException uee) {
					Log.error("Unexpected error: ", uee);
					ZCashUI.this.errorReporter.reportError(uee);
				}
			}
		});
		
		menuItemNewZelnode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ZelCashZelNodeDialog ad = new ZelCashZelNodeDialog(ZCashUI.this, clientCaller, installationObserver, null, labelStorage);
					ad.setVisible(true);
				} catch (Exception uee) {
					Log.error("Unexpected error: ", uee);
					ZCashUI.this.errorReporter.reportError(uee);
				}
			}
		});

		menuItemBackup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.walletOps.backupWallet();
			}
		});

		
		 menuItemEncrypt.addActionListener( new ActionListener() {  
			 @Override 
			 public void actionPerformed(ActionEvent e) {
		            ZCashUI.this.walletOps.encryptWallet(); } } );

		 menuItemChangePassword.addActionListener( new ActionListener() {  
			 @Override 
			 public void actionPerformed(ActionEvent e) {
		            ZCashUI.this.walletOps.changeWalletPassword(); } } );
		 
		menuItemExportKeys.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.walletOps.exportWalletPrivateKeys();
			}
		});

		menuItemImportKeys.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.walletOps.importWalletPrivateKeys();
			}
		});

		menuItemShowPrivateKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.walletOps.showPrivateKey();
			}
		});

		menuItemImportOnePrivateKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.walletOps.importSinglePrivateKey();
			}
		});

		menuItemOwnIdentity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.messagingPanel.openOwnIdentityDialog();
			}
		});

		menuItemExportOwnIdentity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.messagingPanel.exportOwnIdentity();
			}
		});

		menuItemImportContactIdentity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.messagingPanel.importContactIdentity();
			}
		});

		menuItemAddMessagingGroup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.messagingPanel.addMessagingGroup();
			}
		});

		menuItemRemoveContactIdentity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.messagingPanel.removeSelectedContact();
			}
		});

		menuItemMessagingOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.messagingPanel.openOptionsDialog();
			}
		});

		menuItemShareFileViaIPFS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ZCashUI.this.messagingPanel.shareFileViaIPFS();
			}
		});
		/*
		 * menuItemExportToArizen.addActionListener( new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * ZCashUI.this.walletOps.exportToArizenWallet(); } } );
		 */
		// Close operation
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ZCashUI.this.exitProgram();
			}
		});

		// Show initial message
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					String userDir = OSUtil.getSettingsDirectory();
					File warningFlagFile = new File(userDir + File.separator + "initialInfoShown_0.82.flag");
					if (warningFlagFile.exists()) {
						return;
					}
					;

					Object[] options = { langUtil.getString("main.frame.disclaimer.button.agree"),
							langUtil.getString("main.frame.disclaimer.button.disagree") };

					int option = JOptionPane.showOptionDialog(ZCashUI.this.getRootPane().getParent(),
							langUtil.getString("main.frame.disclaimer.text"),
							langUtil.getString("main.frame.disclaimer.title"), JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

					if (option == 0) {
						warningFlagFile.createNewFile();
					} else {
						ZCashUI.this.exitProgram();
					}

				} catch (IOException ioe) {
					/* TODO: report exceptions to the user */
					Log.error("Unexpected error: ", ioe);
				}
			}
		});

		// Finally dispose of the progress dialog
		if (progressDialog != null) {
			progressDialog.doDispose();
		}

		// Notify the messaging TAB that it is being selected - every time
		tabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ZelCashJTabbedPane tabs = (ZelCashJTabbedPane) e.getSource();
				if (tabs.getSelectedIndex() == 5) {
					ZCashUI.this.messagingPanel.tabSelected();
				}
			}
		});

		this.validate();
		this.repaint();

		this.pack();
		Dimension currentSize = this.getSize();

		OS_TYPE os = OSUtil.getOSType();
		int width = 1040;
		if (os == OS_TYPE.MAC_OS) {
			width += 100; // Needs to be wider on macOS
		}

		this.setSize(new Dimension(width, currentSize.height));
		this.validate();
		this.repaint();
	}

	public void exitProgram() {
		Log.info("Exiting ...");

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		this.dashboard.stopThreadsAndTimers();
		this.transactionDetailsPanel.stopThreadsAndTimers();
		this.addresses.stopThreadsAndTimers();
		this.sendPanel.stopThreadsAndTimers();
		this.messagingPanel.stopThreadsAndTimers();

		ZCashUI.this.setVisible(false);
		ZCashUI.this.dispose();

		System.exit(0);
	}
	
	public void stopTimers() {
		Log.info("stopTimers ...");

		this.dashboard.stopThreadsAndTimers();
		this.transactionDetailsPanel.stopThreadsAndTimers();
		this.addresses.stopThreadsAndTimers();
		this.sendPanel.stopThreadsAndTimers();
		this.messagingPanel.stopThreadsAndTimers();

	}
	
	public void restartDaemon(boolean reindex) {
		Log.info("restartDaemon ...");

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			this.dashboard.stopThreadsAndTimers();
			this.transactionDetailsPanel.stopThreadsAndTimers();
			this.addresses.stopThreadsAndTimers();
			this.sendPanel.stopThreadsAndTimers();
			this.messagingPanel.stopThreadsAndTimers();
			Thread.sleep(ZCashUI.THREAD_WAIT_1_SECOND);
			
			this.clientCaller.stopDaemon();
			ZCashInstallationObserver initialInstallationObserver;
			DaemonInfo zcashdInfo;
			for (int i = 0; i < 10; ++i) {
				Log.info("Check if Daemon is stopped");
				initialInstallationObserver = new ZCashInstallationObserver(OSUtil.getProgramDirectory());
				zcashdInfo = initialInstallationObserver.getDaemonInfo();
				initialInstallationObserver = null;
				if (zcashdInfo.status != DAEMON_STATUS.RUNNING) {
					Log.info("Daemon stopped.");
					break;
				}
				Thread.sleep(ZCashUI.THREAD_WAIT_1_SECOND);
			}
			this.clientCaller.startDaemon(reindex);
			for (int i = 0; i < 10; ++i) {
				Log.info("Check if Daemon is running");
				initialInstallationObserver = new ZCashInstallationObserver(OSUtil.getProgramDirectory());
				zcashdInfo = initialInstallationObserver.getDaemonInfo();
				initialInstallationObserver = null;
				if (zcashdInfo.status == DAEMON_STATUS.RUNNING) {
					Log.info("Daemon running.");
					break;
				}
				Thread.sleep(ZCashUI.THREAD_WAIT_1_SECOND);
			}
			for (int i = 0; i < 30; ++i) {
				Log.info("Checking if Daemon is ready for gui wallet.");
				try {
					clientCaller.getNetworkAndBlockchainInfo();
					Log.info("Daemon is ready.");
				}
				catch(Exception e) {
					Log.info("Daemon not ready.");
					Thread.sleep(ZCashUI.THREAD_WAIT_1_SECOND);
				}
			}
			Log.info("restartDaemon finished ...");
		}
		catch (Exception e) {
			Log.error("Error on restartDaemon: "+e.getMessage());
		}
		
	}
	
	public static void main(String argv[]) throws IOException {
		ZCashUI ui = null;
		StartupProgressDialog startupBar = null;
		ZCashClientCaller initialClientCaller = null;
		ZCashInstallationObserver initialInstallationObserver = null;
		DaemonInfo zcashdInfo = null;
		boolean reindex = false;
		for (int i = 0; i < argv.length; i++) {
			if("reindex".equals(argv[i])) {
				reindex = true;
			}
		}
		try {
			new ZelCashUI();
			OS_TYPE os = OSUtil.getOSType();

			if ((os == OS_TYPE.WINDOWS) || (os == OS_TYPE.MAC_OS)) {
				possiblyCreateZelCashConfigFile();
			}

			LanguageUtil langUtil = LanguageUtil.instance();

			Log.info("Starting ZelCash Swing Wallet ...");
			Log.info("OS: " + System.getProperty("os.name") + " = " + os);
			Log.info("Current directory: " + new File(".").getCanonicalPath());
			Log.info("Class path: " + System.getProperty("java.class.path"));
			Log.info("Environment PATH: " + System.getenv("PATH"));

			// Look and feel settings - a custom OS-look and feel is set for Windows
			if (os == OS_TYPE.WINDOWS) {
				// Custom Windows L&F and font settings
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

				// This font looks good but on Windows 7 it misses some chars like the stars...
				// FontUIResource font = new FontUIResource("Lucida Sans Unicode", Font.PLAIN,
				// 11);
				// UIManager.put("Table.font", font);
			} else if (os == OS_TYPE.MAC_OS) {
				// The MacOS L&F is active by default - the property sets the menu bar Mac style
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.mrj.application.apple.menu.about.name",
						LanguageUtil.instance().getString("apple.menu.about.name"));
			} else {
				for (LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels()) {
					Log.info("Available look and feel: " + lf.getName() + " " + lf.getClassName());
					if (lf.getName().equals("Nimbus")) {
						Log.info("Setting look and feel: {0}", lf.getClassName());
						UIManager.setLookAndFeel(lf.getClassName());
						break;
					}
					;
				}
			}
			
			Log.info("Checking if zelnodes.conf exists and is properly set...");
			String blockchainDir = OSUtil.getBlockchainDirectory();
			File zelnodeConf = new File(blockchainDir + File.separator + "zelnode.conf");
			if (!zelnodeConf.exists())
			{
				Log.info("Could not find file: {0} !", zelnodeConf.getAbsolutePath());
			} 
			else {
				BufferedReader br = new BufferedReader(new FileReader(zelnodeConf)); 
				String emptyLine;
				String st; 
				String[] zelNodeInfo;
				while ((st = br.readLine()) != null) {
					emptyLine = st.replaceAll(" ", "").replaceAll("(?m)^\\\\s*\\\\r?\\\\n|\\\\r?\\\\n\\\\s*(?!.*\\\\r?\\\\n)", "");						
					if(st.startsWith("#") || emptyLine.equals("")) {
						continue;
					}
					else {
						zelNodeInfo = st.split("\\s+");
						if(zelNodeInfo.length != 5) {
							Log.error("Zelnode.conf file not ok. One of the lines doesn`t have 5 strings.");
							JOptionPane.showMessageDialog(null,
									LanguageUtil.instance().getString("parsing.error.zelnodesconf.wrong.number"),
									LanguageUtil.instance().getString("parsing.error.zelnodesconf.title"),
									JOptionPane.ERROR_MESSAGE);
							System.exit(1);
						}
						else {
							
							if(!zelNodeInfo[1].endsWith(":16125") && !zelNodeInfo[1].endsWith(":26125")) {
								Log.error("Zelnode.conf file not ok. ip not ending with correct port: "+zelNodeInfo[1]);
								JOptionPane.showMessageDialog(null,
										LanguageUtil.instance().getString("parsing.error.zelnodesconf.wrong.port", zelNodeInfo[1]),
										LanguageUtil.instance().getString("parsing.error.zelnodesconf.title"),
										JOptionPane.ERROR_MESSAGE);
								System.exit(1);
							}
							
							String ip = zelNodeInfo[1].replaceAll(":16125", "").replaceAll(":26125", "");

							try {
								Inet4Address address = (Inet4Address) Inet4Address.getByName(ip);
							}
							catch (Exception e) {
								try {
									Inet6Address address = (Inet6Address) Inet6Address.getByName(ip);
								}
								catch (Exception ex) {
									Log.error("Zelnode.conf file not ok. ip not valid for ipv4 and ipv6:"+ip);
									JOptionPane.showMessageDialog(null,
											LanguageUtil.instance().getString("parsing.error.zelnodesconf.wrong.ip", ip),
											LanguageUtil.instance().getString("parsing.error.zelnodesconf.title"),
											JOptionPane.ERROR_MESSAGE);
									System.exit(1);
								}							
							}
							
							
						}
					}
				}
				Log.info("zelnodes.conf exists and is properly set...");
			}

			// If zelcashd is currently not running, do a startup of the daemon as a child
			// process
			// It may be started but not ready - then also show dialog
			initialInstallationObserver = new ZCashInstallationObserver(OSUtil.getProgramDirectory());
			zcashdInfo = initialInstallationObserver.getDaemonInfo();
			initialInstallationObserver = null;

			initialClientCaller = new ZCashClientCaller(OSUtil.getProgramDirectory());
			boolean daemonStartInProgress = false;
			try {
				if (zcashdInfo.status == DAEMON_STATUS.RUNNING) {
					NetworkAndBlockchainInfo info = initialClientCaller.getNetworkAndBlockchainInfo();
					// If more than 20 minutes behind in the blockchain - startup in progress
					if ((System.currentTimeMillis() - info.lastBlockDate.getTime()) > (20 * 60 * 1000)) {
						Log.info(
								"Current blockchain synchronization date is " + new Date(info.lastBlockDate.getTime()));
						daemonStartInProgress = true;
					}
				}
			} catch (WalletCallException wce) {
				if ((wce.getMessage().indexOf("{\"code\":-28") != -1) || // Started but not ready
						(wce.getMessage().indexOf("error code: -28") != -1)) {
					Log.info("zelcashd is currently starting...");
					daemonStartInProgress = true;
				}
			}
			if (false == AppLock.lock()) {
				throw new Exception(LanguageUtil.instance().getString("duplicate.instante.detected"));
			}
			installShutdownHook();

			if ((zcashdInfo.status != DAEMON_STATUS.RUNNING) || (daemonStartInProgress)) {
				Log.info(
						"zelcashd is not running at the moment or has not started/synchronized 100% - showing splash...");
				startupBar = new StartupProgressDialog(initialClientCaller);
				startupBar.setVisible(true);
				startupBar.waitForStartup(reindex);
			}
			initialClientCaller = null;

			// Main GUI is created here
			ui = new ZCashUI(startupBar);
			ui.setVisible(true);

		} catch (InstallationDetectionException ide) {
			Log.error("Unexpected error: ", ide);
			JOptionPane.showMessageDialog(null,
					LanguageUtil.instance().getString("main.frame.option.pane.installation.error.text",
							OSUtil.getProgramDirectory(), ide.getMessage()),
					LanguageUtil.instance().getString("main.frame.option.pane.installation.error.title"),
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} catch (WalletCallException wce) {
			Log.error("Unexpected error: ", wce);

			if ((wce.getMessage().indexOf("{\"code\":-28,\"message\"") != -1)
					|| (wce.getMessage().indexOf("error code: -28") != -1)) {
				JOptionPane.showMessageDialog(null,
						LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.text"),
						LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.title"),
						JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.2.text",
								wce.getMessage()),
						LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.2.title"),
						JOptionPane.ERROR_MESSAGE);
			}

			System.exit(2);
		} catch (Exception e) {
			Log.error("Unexpected error: ", e);
			if (e.getMessage().equals(LanguageUtil.instance().getString("duplicate.instante.detected"))) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.title"),
						JOptionPane.ERROR_MESSAGE);
				System.exit(3);
			} 
			else if(e.getMessage().contains("(code 1)")) {
				Object[] options = { LanguageUtil.instance().getString("main.frame.reindex.button.agree"),
						LanguageUtil.instance().getString("main.frame.reindex.button.disagree") };

				int option = JOptionPane.showOptionDialog(null,
						LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.code1.text",
								e.getMessage()),
						LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.title"),
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				if (option == 0) {
					try {
						
						if (initialClientCaller == null) {
							initialClientCaller = new ZCashClientCaller(OSUtil.getProgramDirectory());
						}
						initialClientCaller.stopDaemon();
						
						JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("wallet.reindex.restart.message"),
								LanguageUtil.instance().getString("wallet.reindex.restart.title"),
								JOptionPane.INFORMATION_MESSAGE);

						if (startupBar != null) {
							startupBar.dispose();
						}
						for (int i = 0; i < 5; ++i) {
							Log.info("Check if Daemon is stopped");
							initialInstallationObserver = new ZCashInstallationObserver(OSUtil.getProgramDirectory());
							zcashdInfo = initialInstallationObserver.getDaemonInfo();
							initialInstallationObserver = null;
							if (zcashdInfo.status != DAEMON_STATUS.RUNNING) {
								Log.info("Daemon stopped.");
								break;
							}
							Thread.sleep(ZCashUI.THREAD_WAIT_1_SECOND);
						}
						Log.info("Restarting the wallet.");
						String args[] = {"reindex"};
						ZCashUI.main(args);
					} catch (Exception errr) {
						JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.2.text",
										errr.getMessage()),
								LanguageUtil.instance()
										.getString("main.frame.option.pane.wallet.critical.error.2.title"),
								JOptionPane.ERROR_MESSAGE);
						System.exit(5);
					} catch (Error errX) {
						// Last resort catch for unexpected problems - just to inform the user
						errX.printStackTrace();
						JOptionPane.showMessageDialog(null,
								LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.2.text",
										errX.getMessage()),
								LanguageUtil.instance()
										.getString("main.frame.option.pane.wallet.critical.error.2.title"),
								JOptionPane.ERROR_MESSAGE);
						System.exit(6);
					}
				} else {
					System.exit(3);
				}	
			}
			else {
				Log.error("Unexpected error: ", e);
	            JOptionPane.showMessageDialog(
	                null,
	                LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.text", e.getMessage()),
	                LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.title"),
	                JOptionPane.ERROR_MESSAGE);
	            System.exit(3);
			}

		} catch (Error err) {
			// Last resort catch for unexpected problems - just to inform the user
			err.printStackTrace();
			JOptionPane.showMessageDialog(null,
					LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.2.text",
							err.getMessage()),
					LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.2.title"),
					JOptionPane.ERROR_MESSAGE);
			System.exit(4);
		}
	}

	public static void possiblyCreateZelCashConfigFile() throws IOException {
		String blockchainDir = OSUtil.getBlockchainDirectory();
		File dir = new File(blockchainDir);

		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				Log.error("ERROR: Could not create settings directory: " + dir.getCanonicalPath());
				throw new IOException("Could not create settings directory: " + dir.getCanonicalPath());
			}
		}

		File zelcashConfigFile = new File(dir, "zelcash.conf");

		if (!zelcashConfigFile.exists()) {
			Log.info("ZelCash configuration file " + zelcashConfigFile.getCanonicalPath()
					+ " does not exist. It will be created with default settings.");

			Random r = new Random(System.currentTimeMillis());

			PrintStream configOut = new PrintStream(new FileOutputStream(zelcashConfigFile));

			configOut.println("#############################################################################");
			configOut.println("#                         ZelCash configuration file                        #");
			configOut.println("#############################################################################");
			configOut.println("# This file has been automatically generated by the ZelCash GUI wallet with #");
			configOut.println("# default settings. It may be further cutsomized by hand only.              #");
			configOut.println("#############################################################################");
			configOut.println("# Creation date: " + new Date().toString());
			configOut.println("#############################################################################");
			configOut.println("");
			configOut.println("rpcallowip=127.0.0.1");
			configOut.println("server=1");
			configOut.println("addnode=node.zel.cash");
			configOut.println("addnode=explorer.zel.cash");
			configOut.println("addnode=explorer2.zel.cash");
			configOut.println("addnode=explorer-asia.zel.cash");
			configOut.println("addnode=explorer.zelcash.online");
			configOut.println("addnode=explorer.zel.zelcore.io");
			configOut.println("# The rpcuser/rpcpassword are used for the local call to zelcashd");
			configOut.println("rpcuser=User" + Math.abs(r.nextInt()));
			configOut.println("rpcpassword=Pass" + Math.abs(r.nextInt()) + "" + Math.abs(r.nextInt()) + ""
					+ Math.abs(r.nextInt()));
			/*
			 * This is not necessary as of release:
			 * https://github.com/ZencashOfficial/zen/releases/tag/v2.0.9-3-b8d2ebf
			 * configOut.
			 * println("# Well-known nodes to connect to - to speed up acquiring initial connections"
			 * ); configOut.println("addnode=zpool.blockoperations.com");
			 * configOut.println("addnode=luckpool.org:8333");
			 * configOut.println("addnode=zencash.cloud");
			 * configOut.println("addnode=zen.suprnova.cc");
			 * configOut.println("addnode=zen.bitfire.one");
			 * configOut.println("addnode=zenmine.pro");
			 */

			configOut.close();
		}
	}

	private static void installShutdownHook() {

		Runnable runner = new Runnable() {
			@Override
			public void run() {
				AppLock.unlock();
			}
		};
		Runtime.getRuntime().addShutdownHook(new Thread(runner, "Window Prefs Hook"));
	}
}
