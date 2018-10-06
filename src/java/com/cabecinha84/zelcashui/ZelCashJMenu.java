package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.JMenu;

public class ZelCashJMenu extends JMenu {
	private static Color backGroundColor = ZelCashUI.menu;
	public ZelCashJMenu() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJMenu(Action a) {
		super(a);
		this.setBackground(backGroundColor);
	}

	public ZelCashJMenu(String s, boolean b) {
		super(s, b);
		this.setBackground(backGroundColor);
	}

	public ZelCashJMenu(String s) {
		super(s);
		this.setBackground(backGroundColor);
	}

	
	
}

