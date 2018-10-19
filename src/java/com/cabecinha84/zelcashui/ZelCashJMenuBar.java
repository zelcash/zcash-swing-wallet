package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.JMenuBar;

public class ZelCashJMenuBar extends JMenuBar {
	private Color backGroundColor = ZelCashUI.menubar;
	private Color textColor = ZelCashUI.text;
	public ZelCashJMenuBar() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

			
}

