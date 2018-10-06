package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

public class ZelCashJCheckBox extends JCheckBox {
	private static Color backGroundColor = ZelCashUI.checkbox;
	public ZelCashJCheckBox() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJCheckBox(Action a) {
		super(a);
		this.setBackground(backGroundColor);
	}

	public ZelCashJCheckBox(Icon icon, boolean selected) {
		super(icon, selected);
		this.setBackground(backGroundColor);
	}

	public ZelCashJCheckBox(Icon icon) {
		super(icon);
		this.setBackground(backGroundColor);
	}

	public ZelCashJCheckBox(String text, boolean selected) {
		super(text, selected);
		this.setBackground(backGroundColor);
	}

	public ZelCashJCheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		this.setBackground(backGroundColor);
	}

	public ZelCashJCheckBox(String text, Icon icon) {
		super(text, icon);
		this.setBackground(backGroundColor);
	}

	public ZelCashJCheckBox(String text) {
		super(text);
		this.setBackground(backGroundColor);
	}

		
}

