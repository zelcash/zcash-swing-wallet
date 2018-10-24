package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JScrollPane;

public class ZelCashJScrollPane extends JScrollPane {
	private Color backGroundColor = ZelCashUI.scrollpane;
	public ZelCashJScrollPane() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		this.setBackground(backGroundColor);
	}

	public ZelCashJScrollPane(Component view) {
		super(view);
		this.setBackground(backGroundColor);
	}

	public ZelCashJScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
		this.setBackground(backGroundColor);
	}

		
}

