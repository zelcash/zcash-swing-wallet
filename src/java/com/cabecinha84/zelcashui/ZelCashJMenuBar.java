package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.JMenuBar;

public class ZelCashJMenuBar extends JMenuBar {
	private static Color backGroundColor = ZelCashUI.menubar;
	private static Color textColor = ZelCashUI.text;
	public ZelCashJMenuBar() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

			
}

