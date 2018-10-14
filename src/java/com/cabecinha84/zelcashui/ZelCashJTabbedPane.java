package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.JTabbedPane;

public class ZelCashJTabbedPane extends JTabbedPane {
	private static Color backGroundColor = ZelCashUI.tabbedpane;
	private static Color textColor = ZelCashUI.text;
	public ZelCashJTabbedPane() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJTabbedPane(int tabPlacement) {
		super(tabPlacement);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}
}

