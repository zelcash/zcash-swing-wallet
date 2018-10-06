package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class ZelCashJTextField extends JTextField {
	private static Color backGroundColor = ZelCashUI.textarea;
	public ZelCashJTextField() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextField(int columns) {
		super(columns);
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextField(String text, int columns) {
		super(text, columns);
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextField(String text) {
		super(text);
		this.setBackground(backGroundColor);
	}

	
		
}

