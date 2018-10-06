package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class ZelCashJButton extends JButton {
	private static Color backGroundColor = ZelCashUI.button;
	public ZelCashJButton() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJButton(Action a) {
		super(a);
		this.setBackground(backGroundColor);
	}

	public ZelCashJButton(Icon icon) {
		super(icon);
		this.setBackground(backGroundColor);
	}

	public ZelCashJButton(String text, Icon icon) {
		super(text, icon);
		this.setBackground(backGroundColor);
	}

	public ZelCashJButton(String text) {
		super(text);
		this.setBackground(backGroundColor);
	}


	
}

