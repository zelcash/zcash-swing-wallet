package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JSplitPane;

public class ZelCashJSplitPane extends JSplitPane {
	private static Color backGroundColor = ZelCashUI.splitpane;
	public ZelCashJSplitPane() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJSplitPane(int newOrientation, boolean newContinuousLayout, Component newLeftComponent,
			Component newRightComponent) {
		super(newOrientation, newContinuousLayout, newLeftComponent, newRightComponent);
		this.setBackground(backGroundColor);
	}

	public ZelCashJSplitPane(int newOrientation, boolean newContinuousLayout) {
		super(newOrientation, newContinuousLayout);
		this.setBackground(backGroundColor);
	}

	public ZelCashJSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
		super(newOrientation, newLeftComponent, newRightComponent);
		this.setBackground(backGroundColor);
	}

	public ZelCashJSplitPane(int newOrientation) {
		super(newOrientation);
		this.setBackground(backGroundColor);
	}


	
}

