package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.text.Document;

public class ZelCashJTextArea extends JTextArea {
	private static Color backGroundColor = ZelCashUI.textarea;
	public ZelCashJTextArea() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextArea(Document doc) {
		super(doc);
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextArea(int rows, int columns) {
		super(rows, columns);
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextArea(String text) {
		super(text);
		this.setBackground(backGroundColor);
	}

		
		
}

