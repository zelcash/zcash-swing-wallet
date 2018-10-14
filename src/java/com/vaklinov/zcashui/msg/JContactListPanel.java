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
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.cabecinha84.zelcashui.ZelCashJButton;
import com.cabecinha84.zelcashui.ZelCashJFrame;
import com.cabecinha84.zelcashui.ZelCashJLabel;
import com.cabecinha84.zelcashui.ZelCashJMenuItem;
import com.cabecinha84.zelcashui.ZelCashJPanel;
import com.cabecinha84.zelcashui.ZelCashJPopupMenu;
import com.cabecinha84.zelcashui.ZelCashJScrollPane;
import com.cabecinha84.zelcashui.ZelCashUI;
import com.vaklinov.zcashui.Log;
import com.vaklinov.zcashui.StatusUpdateErrorReporter;


/**
 * Main panel for messaging
 */
public class JContactListPanel
	extends ZelCashJPanel
{
	private MessagingPanel   parent;
	private MessagingStorage mesagingStorage;
	private ContactList      list;
	private StatusUpdateErrorReporter errorReporter;
	private ZelCashJFrame           parentFrame;
	
	private ZelCashJPopupMenu popupMenu;
	
	public JContactListPanel(MessagingPanel parent, 
			ZelCashJFrame parentFrame,
			                 MessagingStorage messagingStorage, 
			                 StatusUpdateErrorReporter errorReporter)
		throws IOException
	{
		super();
		
		this.parent = parent;
		this.parentFrame     = parentFrame;
		this.mesagingStorage = messagingStorage;
		this.errorReporter   = errorReporter;
		
		this.setLayout(new BorderLayout(0, 0));
		list = new ContactList();
		list.setIdentities(this.mesagingStorage.getContactIdentities(true));
		this.add(new ZelCashJScrollPane(list), BorderLayout.CENTER);
		
		ZelCashJPanel upperPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		upperPanel.add(new ZelCashJLabel(
			"<html><span style=\"font-size:1.2em;font-style:italic;\">Contact list: &nbsp;</span></html>"),
			BorderLayout.WEST);
		URL addIconUrl = this.getClass().getClassLoader().getResource("images/add12.png");
        ImageIcon addIcon = new ImageIcon(addIconUrl);
        URL removeIconUrl = this.getClass().getClassLoader().getResource("images/remove12.png");
        ImageIcon removeIcon = new ImageIcon(removeIconUrl);
        ZelCashJButton addButton = new ZelCashJButton(addIcon);
        addButton.setToolTipText("Add contact...");
        ZelCashJButton removeButton = new ZelCashJButton(removeIcon);
        removeButton.setToolTipText("Remove contact...");
        ZelCashJButton addGroupButton = new ZelCashJButton(
        	"<html><span style=\"font-size:0.7em;\">Group</span></html>", addIcon);
        addGroupButton.setToolTipText("Add group...");
        ZelCashJPanel tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        tempPanel.add(removeButton);
        tempPanel.add(addButton);
        tempPanel.add(addGroupButton);
        upperPanel.add(tempPanel, BorderLayout.EAST);
        ZelCashJLabel contactList = new ZelCashJLabel(
    			"<html><span style=\"font-size:1.6em;font-style:italic;\">&nbsp;</span>");
        upperPanel.add(contactList, BorderLayout.CENTER);
		upperPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		this.add(upperPanel, BorderLayout.NORTH);
		
		// Add a listener for adding a contact
		addButton.addActionListener(new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JContactListPanel.this.parent.importContactIdentity();
			}
		});
		
		// Add a listener for adding a group
		addGroupButton.addActionListener(new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JContactListPanel.this.parent.addMessagingGroup();
			}
		});

		
		// Add a listener for removing a contact
		removeButton.addActionListener(new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JContactListPanel.this.parent.removeSelectedContact();
			}
		});
		
		// Take care of updating the messages on selection
		list.addListSelectionListener(new ListSelectionListener() 
		{	
			@Override
			public void valueChanged(ListSelectionEvent e) 
			{
				try
				{
					if (e.getValueIsAdjusting())
					{
						return; // Change is not final
					}
					
					MessagingIdentity id = JContactListPanel.this.list.getSelectedValue();
					
					if (id == null)
					{
						return; // Nothing selected
					}
					
					Cursor oldCursor = JContactListPanel.this.parentFrame.getCursor();
					try
					{
						JContactListPanel.this.parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	     				JContactListPanel.this.parent.displayMessagesForContact(id);
					} finally
					{
						JContactListPanel.this.parentFrame.setCursor(oldCursor);
					}
				} catch (IOException ioe)
				{
					Log.error("Unexpected error: ", ioe);
					JContactListPanel.this.errorReporter.reportError(ioe, false);
				}
			}
		});
		
		// Mouse listener is used to show the popup menu
		list.addMouseListener(new MouseAdapter()
        {
        	public void mousePressed(MouseEvent e)
        	{
                if ((!e.isConsumed()) && e.isPopupTrigger())
                {
                    ContactList list = (ContactList)e.getSource();
                    if (list.getSelectedValue() != null)
                    {
                    	popupMenu.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
                    	e.consume();
                    }
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
		
		
		// Actions of the popup menu
		this.popupMenu = new ZelCashJPopupMenu();
		int accelaratorKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
		ZelCashJMenuItem showDetails = new ZelCashJMenuItem("Show details...");
        popupMenu.add(showDetails);
        showDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, accelaratorKeyMask));
        showDetails.addActionListener(new ActionListener() 
        {	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				// Show a messaging identity dialog
				if (list.getSelectedValue() != null)
				{
					IdentityInfoDialog iid = new IdentityInfoDialog(
						JContactListPanel.this.parentFrame, list.getSelectedValue());
					iid.setVisible(true);
				}
			}
		});
        
        ZelCashJMenuItem removeContact = new ZelCashJMenuItem("Remove...");
        popupMenu.add(removeContact);
        removeContact.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, accelaratorKeyMask));
        removeContact.addActionListener(new ActionListener() 
        {	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JContactListPanel.this.parent.removeSelectedContact();
			}
		});

        ZelCashJMenuItem sendContactDetails = new ZelCashJMenuItem("Send contact details...");
        popupMenu.add(sendContactDetails);
        sendContactDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, accelaratorKeyMask));
        sendContactDetails.addActionListener(new ActionListener() 
        {	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JContactListPanel.this.sendContactDetailsToSelectedContact();
			}
		});
	}
	
	
	public void sendContactDetailsToSelectedContact()
	{
		try
		{
			MessagingIdentity id = this.list.getSelectedValue();
			
			if (id == null)
			{
		        JOptionPane.showMessageDialog(
			        this.parentFrame,
			        "No messaging contact is selected in the contact list (on the right side of the UI).\n" +
			        "In order to send contact details you need to select a contact first!",
				    "No messaging contact is selected...", JOptionPane.ERROR_MESSAGE);					
				return;
			}
			
			if (id.isAnonymous())
			{
		        int reply = JOptionPane.showConfirmDialog(
			        this.parentFrame, 
			        "The contact: " + id.getDiplayString() + "\n" +
			        "is anonymous. Sending your contact details to him will reveal your messaging\n" +
			        "identity! Are you sure you want to send your contact details to him?", 
			        "Are you sure you want to send your contact details", 
			        JOptionPane.YES_NO_OPTION);
			        
			    if (reply == JOptionPane.NO_OPTION) 
			    {
			      	return;
			    }
			}
			
			this.parent.sendIdentityMessageTo(id);
			
		} catch (Exception ioe)
		{
			Log.error("Unexpected error: ", ioe);
			JContactListPanel.this.errorReporter.reportError(ioe, false);
		}
	}
	
	
	public void reloadMessagingIdentities()
		throws IOException
	{
		list.setIdentities(this.mesagingStorage.getContactIdentities(true));
		list.revalidate();
	}
	
	
	public int getNumberOfContacts()
	{
		return list.getModel().getSize();
	}
	
	
	// Null if nothing selected
	public MessagingIdentity getSelectedContact()
	{
		return this.list.getSelectedValue();
	}
	
	
	private static class ContactList
		extends JList<MessagingIdentity>
	{
		ImageIcon contactBlackIcon;
		ImageIcon contactGroupBlackIcon;
		ZelCashJLabel    renderer;
		private static Color backGroundColor = ZelCashUI.startup;
		public ContactList()
		{
			super();
			this.setBackground(backGroundColor);
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
	        URL iconUrl = this.getClass().getClassLoader().getResource("images/contact-black.png");
	        contactBlackIcon = new ImageIcon(iconUrl);
	        URL groupIconUrl = this.getClass().getClassLoader().getResource("images/contact-group-black.png");
	        contactGroupBlackIcon = new ImageIcon(groupIconUrl);
	        
	        renderer = new ZelCashJLabel();
	        renderer.setOpaque(true);
		}
		
		
		public void setIdentities(List<MessagingIdentity> identities)
		{
			List<MessagingIdentity> localIdentities = new ArrayList<MessagingIdentity>();
			localIdentities.addAll(identities);
			
			Collections.sort(
				localIdentities,
				new Comparator<MessagingIdentity>() 
				{ 
					@Override
					public int compare(MessagingIdentity o1, MessagingIdentity o2) 
					{
						if (o1.isGroup() != o2.isGroup())
						{
							return o1.isGroup() ? -1 : +1;
						} else
						{						
							return o1.getDiplayString().toUpperCase().compareTo(
								   o2.getDiplayString().toUpperCase());
						}
					}
				}
			);
			
			DefaultListModel<MessagingIdentity> newModel = new DefaultListModel<MessagingIdentity>();
			for (MessagingIdentity id : localIdentities)
			{
				newModel.addElement(id);
			}
			
			this.setModel(newModel);
		}
		
		
		@Override
		public ListCellRenderer<MessagingIdentity> getCellRenderer() 
		{
			return new ListCellRenderer<MessagingIdentity>() 
			{
				@Override
				public Component getListCellRendererComponent(JList<? extends MessagingIdentity> list,
						MessagingIdentity id, int index, boolean isSelected, boolean cellHasFocus) 
				{					
					renderer.setText(id.getDiplayString());
					if (!id.isGroup())
					{
						renderer.setIcon(contactBlackIcon);
					} else
					{
						renderer.setIcon(contactGroupBlackIcon);
					}
					
					if (isSelected) 
					{
						renderer.setBackground(list.getSelectionBackground());
					} else 
					{
						// TODO: list background issues on Linux - if used directly
						renderer.setBackground(new Color(list.getBackground().getRGB()));  
					}
					
					return renderer;
				}
			};
		}
	} // End private static class ContactList
	
} // End public class JContactListPanel
