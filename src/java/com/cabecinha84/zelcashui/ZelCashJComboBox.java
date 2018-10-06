package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class ZelCashJComboBox<T> extends JComboBox {
	private static Color backGroundColor = ZelCashUI.combobox;
	public ZelCashJComboBox() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJComboBox(ComboBoxModel aModel) {
		super(aModel);
		this.setBackground(backGroundColor);
	}

	public ZelCashJComboBox(Object[] items) {
		super(items);
		this.setBackground(backGroundColor);
	}

	public ZelCashJComboBox(Vector items) {
		super(items);
		this.setBackground(backGroundColor);
	}

		
}

