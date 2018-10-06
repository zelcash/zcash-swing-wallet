package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

public class ZelCashJTextPane extends JTextPane {
	private static Color backGroundColor = ZelCashUI.textpane;
	public ZelCashJTextPane() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextPane(StyledDocument doc) {
		super(doc);
		this.setBackground(backGroundColor);
	}

		
}

