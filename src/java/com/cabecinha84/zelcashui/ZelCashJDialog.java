package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;

import javax.swing.JDialog;

public class ZelCashJDialog extends JDialog {
	private Color backGroundColor = ZelCashUI.dialog;
	public ZelCashJDialog() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Dialog owner, String title) {
		super(owner, title);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Dialog owner) {
		super(owner);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Frame owner, boolean modal) {
		super(owner, modal);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Frame owner, String title) {
		super(owner, title);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Frame owner) {
		super(owner);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Window owner, ModalityType modalityType) {
		super(owner, modalityType);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Window owner, String title, ModalityType modalityType) {
		super(owner, title, modalityType);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Window owner, String title) {
		super(owner, title);
		this.setBackground(backGroundColor);
	}

	public ZelCashJDialog(Window owner) {
		super(owner);
		this.setBackground(backGroundColor);
	}
		
}

