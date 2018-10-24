package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.JMenu;

public class ZelCashJMenu extends JMenu {
	private Color backGroundColor = ZelCashUI.menu;
	private Color textColor = ZelCashUI.text;
	public ZelCashJMenu() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJMenu(Action a) {
		super(a);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJMenu(String s, boolean b) {
		super(s, b);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJMenu(String s) {
		super(s);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	
	
}

