package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ZelCashJMenuItem extends JMenuItem {
	private Color backGroundColor = ZelCashUI.menuitem;
	private Color textColor = ZelCashUI.text;
	private final MouseListener mouseAction = new MouseAdapter() { //i use this to apply the mouse event
	    @Override
	    public void mouseEntered(MouseEvent e) {
	        ZelCashJMenuItem item = (ZelCashJMenuItem)e.getSource(); //is this implementation correct ?
	        item.setSelected(true);
	    };

	    @Override
	    public void mouseExited(MouseEvent e) {
	    	ZelCashJMenuItem item = (ZelCashJMenuItem)e.getSource(); 
	    	item.setSelected(false);
	    };
	};
	public ZelCashJMenuItem() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addMouseListener(mouseAction);
	}

	public ZelCashJMenuItem(Action a) {
		super(a);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addMouseListener(mouseAction);
	}

	public ZelCashJMenuItem(Icon icon) {
		super(icon);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addMouseListener(mouseAction);
	}

	public ZelCashJMenuItem(String text, Icon icon) {
		super(text, icon);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addMouseListener(mouseAction);
	}

	public ZelCashJMenuItem(String text, int mnemonic) {
		super(text, mnemonic);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addMouseListener(mouseAction);
	}

	public ZelCashJMenuItem(String text) {
		super(text);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addMouseListener(mouseAction);
	}

}

