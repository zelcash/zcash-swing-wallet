package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class ZelCashJPanel extends JPanel {
	private static Color backGroundColor = ZelCashUI.panel;
	public ZelCashJPanel() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		this.setBackground(backGroundColor);
	}

	public ZelCashJPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.setBackground(backGroundColor);
	}

	public ZelCashJPanel(LayoutManager layout) {
		super(layout);
		this.setBackground(backGroundColor);
	}
	
}

