package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

public class ZelCashJLabel extends JLabel {

	private static Color textColor = ZelCashUI.text;
	
	public ZelCashJLabel() {
		super();
		this.setForeground(textColor);
	}

	public ZelCashJLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		this.setForeground(textColor);
	}

	public ZelCashJLabel(Icon image) {
		super(image);
		this.setForeground(textColor);
	}

	public ZelCashJLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		this.setForeground(textColor);
	}

	public ZelCashJLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		this.setForeground(textColor);
	}

	public ZelCashJLabel(String text) {
		super(text);
		this.setForeground(textColor);
	}
}

