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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import com.cabecinha84.zelcashui.ZelCashJButton;
import com.cabecinha84.zelcashui.ZelCashJDialog;
import com.cabecinha84.zelcashui.ZelCashJFrame;
import com.cabecinha84.zelcashui.ZelCashJLabel;
import com.cabecinha84.zelcashui.ZelCashJPanel;
import com.cabecinha84.zelcashui.ZelCashJTextArea;
import com.cabecinha84.zelcashui.ZelCashJTextField;
import com.vaklinov.zcashui.LanguageUtil;


/**
 * Dialog showing the information about a user's identity
 */
public class IdentityInfoDialog
	extends ZelCashJDialog
{
	protected ZelCashJFrame parentFrame;
	protected MessagingIdentity identity;
	
	protected ZelCashJLabel infoLabel;
	
	protected ZelCashJPanel buttonPanel;
	

	protected ZelCashJTextField nicknameTextField;
	protected ZelCashJTextArea sendreceiveaddressTextField;
	protected ZelCashJTextField senderidaddressTextField;
	protected ZelCashJTextField firstnameTextField;
	protected ZelCashJTextField middlenameTextField;
	protected ZelCashJTextField surnameTextField;
	protected ZelCashJTextField emailTextField;
	protected ZelCashJTextField streetaddressTextField;
	protected ZelCashJTextField facebookTextField;
	protected ZelCashJTextField twitterTextField;
		
	private static LanguageUtil langUtil = LanguageUtil.instance();
	
	public IdentityInfoDialog(ZelCashJFrame parentFrame, MessagingIdentity identity)
	{
		
		this.parentFrame = parentFrame;
		this.identity    = identity;
		
		this.setTitle(langUtil.getString("dialog.identity.info.title",  identity.getDiplayString()));
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
		this.getContentPane().setLayout(new BorderLayout(0, 0));
			
		ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		infoLabel = new ZelCashJLabel(langUtil.getString("dialog.identity.info.infolabel", identity.getNickname()));
	    tempPanel.add(infoLabel, BorderLayout.CENTER);
		this.getContentPane().add(tempPanel, BorderLayout.NORTH);
			
		ZelCashJPanel detailsPanel = new ZelCashJPanel();
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
		
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.nickname"),  nicknameTextField = new ZelCashJTextField(40));
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.firstname"), firstnameTextField = new ZelCashJTextField(40));
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.middlename"), middlenameTextField = new ZelCashJTextField(40));
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.surname"),    surnameTextField = new ZelCashJTextField(40));
		
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.email"),         emailTextField = new ZelCashJTextField(40));
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.streetaddress"), streetaddressTextField = new ZelCashJTextField(40));
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.facebook"),  facebookTextField = new ZelCashJTextField(40));
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.twitter"),   twitterTextField = new ZelCashJTextField(40));
		
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.sendert"), senderidaddressTextField = new ZelCashJTextField(40));
		addFormField(detailsPanel, langUtil.getString("dialog.identity.info.senderz"), sendreceiveaddressTextField = new ZelCashJTextArea(2, 40));
		sendreceiveaddressTextField.setLineWrap(true);
		

		nicknameTextField.setText(this.identity.getNickname());
		firstnameTextField.setText(this.identity.getFirstname());
		middlenameTextField.setText(this.identity.getMiddlename());
		surnameTextField.setText(this.identity.getSurname());
		emailTextField.setText(this.identity.getEmail());
		streetaddressTextField.setText(this.identity.getStreetaddress());
		facebookTextField.setText(this.identity.getFacebook());
		twitterTextField.setText(this.identity.getTwitter());
		senderidaddressTextField.setText(this.identity.getSenderidaddress());
		sendreceiveaddressTextField.setText(this.identity.getSendreceiveaddress());
		
		nicknameTextField.setEditable(false);
		firstnameTextField.setEditable(false);
		middlenameTextField.setEditable(false);
		surnameTextField.setEditable(false);
		emailTextField.setEditable(false);
		streetaddressTextField.setEditable(false);
		facebookTextField.setEditable(false);
		twitterTextField.setEditable(false);
		senderidaddressTextField.setEditable(false);
		sendreceiveaddressTextField.setEditable(false);
		
		detailsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.getContentPane().add(detailsPanel, BorderLayout.CENTER);

		// Lower buttons - by default only close is available
		buttonPanel = new ZelCashJPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 3));
		ZelCashJButton closeButon = new ZelCashJButton(langUtil.getString("dialog.identity.info.button.close"));
		buttonPanel.add(closeButon);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		closeButon.addActionListener(new ActionListener()
		{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					IdentityInfoDialog.this.setVisible(false);
					IdentityInfoDialog.this.dispose();
				}
		});

		this.pack();
		this.setLocation(100, 100);
		this.setLocationRelativeTo(parentFrame);
	}

	
	
	private void addFormField(ZelCashJPanel detailsPanel, String name, JComponent field)
	{
		ZelCashJPanel tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
		ZelCashJLabel tempLabel = new ZelCashJLabel(name, JLabel.RIGHT);
		// TODO: hard sizing of labels may not scale!
		final int width = new ZelCashJLabel(langUtil.getString("dialog.identity.info.sender.id")).getPreferredSize().width + 10;
		tempLabel.setPreferredSize(new Dimension(width, tempLabel.getPreferredSize().height));
		tempPanel.add(tempLabel);
		tempPanel.add(field);
		detailsPanel.add(tempPanel);
	}
	
} // End public class IdentityInfoDialog
