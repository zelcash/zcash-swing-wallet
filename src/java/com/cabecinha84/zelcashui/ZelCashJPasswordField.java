package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.JPasswordField;
import javax.swing.text.Document;

public class ZelCashJPasswordField extends JPasswordField {
	private static Color backGroundColor = ZelCashUI.passwordfield;
	private static Color textColor = ZelCashUI.text;
	public ZelCashJPasswordField() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJPasswordField(Document doc, String txt, int columns) {
		super(doc, txt, columns);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJPasswordField(int columns) {
		super(columns);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJPasswordField(String text, int columns) {
		super(text, columns);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJPasswordField(String text) {
		super(text);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

		
}

