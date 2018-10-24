package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

public class ZelCashJCheckBox extends JCheckBox {
	private Color backGroundColor = ZelCashUI.checkbox;
	private Color textColor = ZelCashUI.text;
	public ZelCashJCheckBox() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJCheckBox(Action a) {
		super(a);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJCheckBox(Icon icon, boolean selected) {
		super(icon, selected);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJCheckBox(Icon icon) {
		super(icon);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJCheckBox(String text, boolean selected) {
		super(text, selected);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJCheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJCheckBox(String text, Icon icon) {
		super(text, icon);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJCheckBox(String text) {
		super(text);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

		
}

