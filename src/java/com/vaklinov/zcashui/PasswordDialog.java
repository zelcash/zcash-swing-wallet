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


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import com.cabecinha84.zelcashui.ZelCashJButton;
import com.cabecinha84.zelcashui.ZelCashJDialog;
import com.cabecinha84.zelcashui.ZelCashJFrame;
import com.cabecinha84.zelcashui.ZelCashJLabel;
import com.cabecinha84.zelcashui.ZelCashJPanel;
import com.cabecinha84.zelcashui.ZelCashJPasswordField;


/**
 * Dialog to get the user password for encrypted wallets - for unlock.
 */
public class PasswordDialog
	extends ZelCashJDialog
{
	protected boolean isOKPressed = false;
	protected String  password    = null;
	
	protected ZelCashJLabel     passwordLabel = null;
	protected ZelCashJPasswordField passwordField = null;
	
	protected ZelCashJLabel upperLabel;
	protected ZelCashJLabel lowerLabel;
	
	protected ZelCashJPanel freeSlotPanel;
	protected ZelCashJPanel freeSlotPanel2;
	private LanguageUtil langUtil;
	
	public PasswordDialog(ZelCashJFrame parent)
	{
		super(parent);
		langUtil = LanguageUtil.instance();
		this.setTitle(langUtil.getString("dialog.password.title"));
	    this.setLocation(parent.getLocation().x + 50, parent.getLocation().y + 50);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		ZelCashJPanel controlsPanel = new ZelCashJPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		controlsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.add(this.upperLabel = new ZelCashJLabel(langUtil.getString("dialog.password.temp.panel.upper.label.text")), BorderLayout.CENTER);
		controlsPanel.add(tempPanel);
		
		ZelCashJLabel dividerLabel = new ZelCashJLabel("   ");
		dividerLabel.setFont(new Font("Helvetica", Font.PLAIN, 8));
		controlsPanel.add(dividerLabel);
		
		tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		tempPanel.add(passwordLabel = new ZelCashJLabel(langUtil.getString("dialog.password.temp.panel.password.label.text")));
		tempPanel.add(passwordField = new ZelCashJPasswordField(30));
		controlsPanel.add(tempPanel);
		
		dividerLabel = new ZelCashJLabel("   ");
		dividerLabel.setFont(new Font("Helvetica", Font.PLAIN, 8));
		controlsPanel.add(dividerLabel);
		
		this.freeSlotPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		controlsPanel.add(this.freeSlotPanel);
		
		this.freeSlotPanel2 = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		controlsPanel.add(this.freeSlotPanel2);

		tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.add(this.lowerLabel = new ZelCashJLabel(langUtil.getString("dialog.password.temp.panel.lower.label.text")), BorderLayout.CENTER);
		controlsPanel.add(tempPanel);
		
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		this.getContentPane().add(controlsPanel, BorderLayout.NORTH);

		// Form buttons
		ZelCashJPanel buttonPanel = new ZelCashJPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		ZelCashJButton okButon = new ZelCashJButton(langUtil.getString("dialog.password.buton.ok.text"));
		buttonPanel.add(okButon);
		buttonPanel.add(new ZelCashJLabel("   "));
		ZelCashJButton cancelButon = new ZelCashJButton(langUtil.getString("dialog.password.buton.cancel.text"));
		buttonPanel.add(cancelButon);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		okButon.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				PasswordDialog.this.processOK();
			}
		});
		
		cancelButon.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				PasswordDialog.this.setVisible(false);
				PasswordDialog.this.dispose();
				
				PasswordDialog.this.isOKPressed = false;
				PasswordDialog.this.password = null;
			}
		});
		
		this.setSize(450, 190);
		this.validate();
		this.repaint();
	}
	
	
	protected void processOK()
	{
		String pass = PasswordDialog.this.passwordField.getText();
		
		if ((pass == null) || (pass.trim().length() <= 0))
		{
			JOptionPane.showMessageDialog(
				PasswordDialog.this.getParent(), 
				langUtil.getString("dialog.password.option.pane.process.text"),
				langUtil.getString("dialog.password.option.pane.process.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		PasswordDialog.this.setVisible(false);
		PasswordDialog.this.dispose();
		
		PasswordDialog.this.isOKPressed = true;
		PasswordDialog.this.password = pass;
	}
	
	
	public boolean isOKPressed()
	{
		return this.isOKPressed;
	}
	
	
	public String getPassword()
	{
		return this.password;
	}
}
