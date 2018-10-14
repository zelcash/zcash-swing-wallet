package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class ZelCashJComboBox<T> extends JComboBox {
	private static Color backGroundColor = ZelCashUI.combobox;
	private static Color textColor = ZelCashUI.text;
	public ZelCashJComboBox() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJComboBox(ComboBoxModel aModel) {
		super(aModel);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJComboBox(Object[] items) {
		super(items);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

	public ZelCashJComboBox(Vector items) {
		super(items);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
	}

		
}

