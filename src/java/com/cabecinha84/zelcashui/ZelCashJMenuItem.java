package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

public class ZelCashJMenuItem extends JMenuItem {
	private static Color backGroundColor = ZelCashUI.menuitem;
	public ZelCashJMenuItem() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJMenuItem(Action a) {
		super(a);
		this.setBackground(backGroundColor);
	}

	public ZelCashJMenuItem(Icon icon) {
		super(icon);
		this.setBackground(backGroundColor);
	}

	public ZelCashJMenuItem(String text, Icon icon) {
		super(text, icon);
		this.setBackground(backGroundColor);
	}

	public ZelCashJMenuItem(String text, int mnemonic) {
		super(text, mnemonic);
		this.setBackground(backGroundColor);
	}

	public ZelCashJMenuItem(String text) {
		super(text);
		this.setBackground(backGroundColor);
	}
	
}

