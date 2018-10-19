package com.cabecinha84.zelcashui;

import java.awt.Color;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;

public class ZelCashJProgressBar extends JProgressBar {
	private Color backGroundColor = ZelCashUI.progressbar;
	private Color foreGroundColor = ZelCashUI.progressbarForeground;
	public ZelCashJProgressBar() {
		super();
		this.setBackground(backGroundColor);
		this.setForeground(foreGroundColor);
	}

	public ZelCashJProgressBar(BoundedRangeModel newModel) {
		super(newModel);
		this.setBackground(backGroundColor);
		this.setForeground(foreGroundColor);
	}

	public ZelCashJProgressBar(int orient, int min, int max) {
		super(orient, min, max);
		this.setBackground(backGroundColor);
		this.setForeground(foreGroundColor);
	}

	public ZelCashJProgressBar(int min, int max) {
		super(min, max);
		this.setBackground(backGroundColor);
		this.setForeground(foreGroundColor);
	}

	public ZelCashJProgressBar(int orient) {
		super(orient);
		this.setBackground(backGroundColor);
		this.setForeground(foreGroundColor);
	}

		
}

