package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class ZelCashJFrame extends JFrame {
	private static Color backGroundColor = ZelCashUI.frame;
	public ZelCashJFrame() throws HeadlessException {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJFrame(GraphicsConfiguration gc) {
		super(gc);
		this.setBackground(backGroundColor);
	}

	public ZelCashJFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		this.setBackground(backGroundColor);
	}

	public ZelCashJFrame(String title) throws HeadlessException {
		super(title);
		this.setBackground(backGroundColor);
	}
	
}

