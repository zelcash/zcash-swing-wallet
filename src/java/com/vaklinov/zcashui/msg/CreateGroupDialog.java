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
package com.vaklinov.zcashui.msg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.cabecinha84.zelcashui.ZelCashJButton;
import com.cabecinha84.zelcashui.ZelCashJDialog;
import com.cabecinha84.zelcashui.ZelCashJFrame;
import com.cabecinha84.zelcashui.ZelCashJLabel;
import com.cabecinha84.zelcashui.ZelCashJPanel;
import com.cabecinha84.zelcashui.ZelCashJProgressBar;
import com.cabecinha84.zelcashui.ZelCashJTextField;
import com.vaklinov.zcashui.LabelStorage;
import com.vaklinov.zcashui.LanguageUtil;
import com.vaklinov.zcashui.Log;
import com.vaklinov.zcashui.StatusUpdateErrorReporter;
import com.vaklinov.zcashui.Util;
import com.vaklinov.zcashui.ZCashClientCaller;
import com.vaklinov.zcashui.ZCashClientCaller.WalletCallException;


/**
 * Dialog showing the messaging options and allowing them to be edited.
 */
public class CreateGroupDialog
	extends ZelCashJDialog
{
	protected MessagingPanel msgPanel;
	protected ZelCashJFrame parentFrame;
	protected MessagingStorage storage;
	protected StatusUpdateErrorReporter errorReporter;
	protected ZCashClientCaller caller;
	
	protected boolean isOKPressed = false;
	protected String  key    = null;
	
	protected ZelCashJLabel     keyLabel = null;
	protected ZelCashJTextField keyField = null;
	
	protected ZelCashJLabel upperLabel;
	protected ZelCashJLabel lowerLabel;
	
	protected ZelCashJProgressBar progress = null;
	
	private static LanguageUtil langUtil = LanguageUtil.instance();
	
	ZelCashJButton okButon;
	ZelCashJButton cancelButon;
	
	LabelStorage labelStorage;
	
	protected MessagingIdentity createdGroup = null;
	
	public CreateGroupDialog(MessagingPanel msgPanel, ZelCashJFrame parentFrame, MessagingStorage storage, 
			                 StatusUpdateErrorReporter errorReporter, ZCashClientCaller caller,
			                 LabelStorage labelStorage)
		throws IOException
	{
		super(parentFrame);
		
		this.msgPanel      = msgPanel;
		this.parentFrame   = parentFrame;
		this.storage       = storage;
		this.errorReporter = errorReporter;
		this.caller = caller;
		this.labelStorage = labelStorage;
		
		this.setTitle(langUtil.getString("create.group.dialog.title"));
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		ZelCashJPanel controlsPanel = new ZelCashJPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		controlsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.add(this.upperLabel = new ZelCashJLabel(langUtil.getString("create.group.dialog.upperLabel")), BorderLayout.CENTER);
		controlsPanel.add(tempPanel);
		
		ZelCashJLabel dividerLabel = new ZelCashJLabel("   ");
		dividerLabel.setFont(new Font("Helvetica", Font.PLAIN, 8));
		controlsPanel.add(dividerLabel);
		
		tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		tempPanel.add(keyField = new ZelCashJTextField(60));
		controlsPanel.add(tempPanel);
		
		dividerLabel = new ZelCashJLabel("   ");
		dividerLabel.setFont(new Font("Helvetica", Font.PLAIN, 8));
		controlsPanel.add(dividerLabel);

		tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.add(this.lowerLabel = new ZelCashJLabel(
				langUtil.getString("create.group.dialog.lowerLabel")), 
			BorderLayout.CENTER);
		controlsPanel.add(tempPanel);
		
		dividerLabel = new ZelCashJLabel("   ");
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
		okButon = new ZelCashJButton(langUtil.getString("create.group.dialog.button.create"));
		buttonPanel.add(okButon);
		buttonPanel.add(new ZelCashJLabel("   "));
		cancelButon = new ZelCashJButton(langUtil.getString("create.group.dialog.button.cancel"));
		buttonPanel.add(cancelButon);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		okButon.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CreateGroupDialog.this.processOK();
			}
		});
		
		cancelButon.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CreateGroupDialog.this.setVisible(false);
				CreateGroupDialog.this.dispose();
				
				CreateGroupDialog.this.isOKPressed = false;
				CreateGroupDialog.this.key = null;
			}
		});
		
		
		this.pack();
		this.setLocation(100, 100);
		this.setLocationRelativeTo(parentFrame);
	}

	
	protected void processOK()
	{
		final String keyPhrase = CreateGroupDialog.this.keyField.getText();
		
		if ((keyPhrase == null) || (keyPhrase.trim().length() <= 0))
		{
			JOptionPane.showMessageDialog(
				CreateGroupDialog.this.getParent(), 
				langUtil.getString("create.group.dialog.error.empty.group.key"), langUtil.getString("create.group.dialog.error.empty"), 
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		CreateGroupDialog.this.isOKPressed = true;
		CreateGroupDialog.this.key = keyPhrase;
				
		// Start import
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.progress.setIndeterminate(true);
		this.progress.setValue(1);
			
		this.okButon.setEnabled(false);
		this.cancelButon.setEnabled(false);
		
		CreateGroupDialog.this.keyField.setEditable(false);
			
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				try
				{
					createGroupForKeyPhrase(keyPhrase);	
				} catch (Exception e)
				{
					Log.error("An error occurred when importing private key for group phrase", e);
					
					JOptionPane.showMessageDialog(
							CreateGroupDialog.this.getRootPane().getParent(),
						langUtil.getString("create.group.dialog.error.importing.privatekeys", e.getClass().getName(), e.getMessage())
						, 
						langUtil.getString("create.group.dialog.error.importing"), JOptionPane.ERROR_MESSAGE);
				} finally
				{
					CreateGroupDialog.this.setVisible(false);
					CreateGroupDialog.this.dispose();
				}
			}
		}).start();
	}
	
	
	public boolean isOKPressed()
	{
		return this.isOKPressed;
	}
	
	
	public String getKey()
	{
		return this.key;
	}
	
	
	public MessagingIdentity getCreatedGroup()
	{
		return this.createdGroup;
	}
	
	
	private void createGroupForKeyPhrase(String keyPhrase)
		throws IOException, InterruptedException, WalletCallException
	{
		String key = Util.convertGroupPhraseToZPrivateKey(keyPhrase);
		
		// There is no way (it seems) to find out what Z address was added - we need to
		// analyze which one it is.
		// TODO: This relies that noone is importing keys at the same time!
		Set<String> addressesBeforeAddition = new HashSet<String>();
		for (String address: this.caller.getWalletZAddresses())
		{
			addressesBeforeAddition.add(address);
		}
		
		CreateGroupDialog.this.caller.importPrivateKey(key);
		
		Set<String> addressesAfterAddition = new HashSet<String>();
		for (String address: this.caller.getWalletZAddresses())
		{
			addressesAfterAddition.add(address);
		}

		addressesAfterAddition.removeAll(addressesBeforeAddition);
		
		String ZAddress = (addressesAfterAddition.size() > 0) ?
			addressesAfterAddition.iterator().next() :
			this.findZAddressForImportKey(key);
		MessagingIdentity existingIdentity = this.findExistingGroupBySendReceiveAddress(ZAddress);
		
		if (existingIdentity == null)
		{
			Log.info("Newly added messaging group \"{0}\" address is: {1}", keyPhrase, ZAddress);
			// Add a group personality etc.
			MessagingIdentity newID = new MessagingIdentity();
			newID.setGroup(true);
			newID.setNickname(keyPhrase);
			newID.setSendreceiveaddress(ZAddress);
			newID.setSenderidaddress("");
			newID.setFirstname("");
			newID.setMiddlename("");
			newID.setSurname("");
			newID.setEmail("");
			newID.setStreetaddress("");
			newID.setFacebook("");
			newID.setTwitter("");
			
			this.storage.addContactIdentity(newID);
			
			CreateGroupDialog.this.createdGroup = newID;
			
			JOptionPane.showMessageDialog(
				CreateGroupDialog.this, 
				langUtil.getString("create.group.dialog.group.added", keyPhrase, ZAddress),
				langUtil.getString("create.group.dialog.group.added.successfully"),
				JOptionPane.INFORMATION_MESSAGE);
		} else
		{
			CreateGroupDialog.this.createdGroup = existingIdentity;
			// TODO: Group was already added it seems - see if it can be made more reliable
			JOptionPane.showMessageDialog(
				CreateGroupDialog.this,  
				langUtil.getString("create.group.dialog.group.already", keyPhrase),
				langUtil.getString("create.group.dialog.group.already.exists"),
				JOptionPane.INFORMATION_MESSAGE);
		}	
		
		// In any case set the label
		if (!Util.stringIsEmpty(ZAddress))
		{
			this.labelStorage.setLabel(ZAddress, keyPhrase);
		}
		
		SwingUtilities.invokeLater(new Runnable() 
		{	
			@Override
			public void run() 
			{
				try
				{
					CreateGroupDialog.this.msgPanel.getContactList().reloadMessagingIdentities();
				} catch (Exception e)
				{
					Log.error("Unexpected error in reloading contacts after gathering messages: ", e);
					CreateGroupDialog.this.errorReporter.reportError(e);
				}
			}
		});
	}
	
	
	/**
	 * Finds a group identity for a send/receive address.
	 *  
	 * @param address
	 * 
	 * @return identity for the address or null
	 */
	private MessagingIdentity findExistingGroupBySendReceiveAddress(String address)
		 throws IOException
	{
		MessagingIdentity identity = null;
		
		for (MessagingIdentity id : this.storage.getContactIdentities(false))
		{
			if (id.isGroup())
			{
				if (id.getSendreceiveaddress().equals(address))
				{
					identity = id;
					break;
				}
			}
		}
		
		return identity;
	}
	
	
	/**
	 * Checks the wallet's private keys to find what address corresponds to a key.
	 * 
	 * @param key to search for
	 * 
	 * @return address for the key or null;
	 */
	private String findZAddressForImportKey(String key)
		throws InterruptedException, WalletCallException, IOException
	{
		String address = null;
		
		for (String zAddr : this.caller.getWalletZAddresses())
		{
			String privKey = this.caller.getZPrivateKey(zAddr);
			if (privKey.equals(key))
			{
				address = zAddr;
				break;
			}
		}
		
		return address;
	}
} 
