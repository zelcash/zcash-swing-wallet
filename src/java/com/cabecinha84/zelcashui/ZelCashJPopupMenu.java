package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.JPopupMenu;

public class ZelCashJPopupMenu extends JPopupMenu {
	private Color backGroundColor = ZelCashUI.popupmenu;
	public ZelCashJPopupMenu() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJPopupMenu(String label) {
		super(label);
		this.setBackground(backGroundColor);
	}		
}

