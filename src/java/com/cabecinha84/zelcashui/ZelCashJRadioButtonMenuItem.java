package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButtonMenuItem;

public class ZelCashJRadioButtonMenuItem extends JRadioButtonMenuItem {
	private static Color backGroundColor = ZelCashUI.radiobutton;

	public ZelCashJRadioButtonMenuItem() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJRadioButtonMenuItem(Action a) {
		super(a);
		this.setBackground(backGroundColor);
	}

	public ZelCashJRadioButtonMenuItem(Icon icon, boolean selected) {
		super(icon, selected);
		this.setBackground(backGroundColor);
	}

	public ZelCashJRadioButtonMenuItem(Icon icon) {
		super(icon);
		this.setBackground(backGroundColor);
	}

	public ZelCashJRadioButtonMenuItem(String text, boolean selected) {
		super(text, selected);
		this.setBackground(backGroundColor);
	}

	public ZelCashJRadioButtonMenuItem(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		this.setBackground(backGroundColor);
	}

	public ZelCashJRadioButtonMenuItem(String text, Icon icon) {
		super(text, icon);
		this.setBackground(backGroundColor);
	}

	public ZelCashJRadioButtonMenuItem(String text) {
		super(text);
		this.setBackground(backGroundColor);
	}
	
}

