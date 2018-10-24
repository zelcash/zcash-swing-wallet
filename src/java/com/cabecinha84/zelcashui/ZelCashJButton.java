package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class ZelCashJButton extends JButton {
	private Color backGroundColor = ZelCashUI.button;
	private Color textColor = ZelCashUI.text;
	public ZelCashJButton() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJButton(Action a) {
		super(a);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJButton(Icon icon) {
		super(icon);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJButton(String text, Icon icon) {
		super(text, icon);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJButton(String text) {
		super(text);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}


	
}

