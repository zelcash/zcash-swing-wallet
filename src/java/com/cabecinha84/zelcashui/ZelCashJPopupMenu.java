package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.JPopupMenu;

public class ZelCashJPopupMenu extends JPopupMenu {
	private static Color backGroundColor = ZelCashUI.popupmenu;
	public ZelCashJPopupMenu() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJPopupMenu(String label) {
		super(label);
		this.setBackground(backGroundColor);
	}		
}

