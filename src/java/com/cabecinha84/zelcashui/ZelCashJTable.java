package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class ZelCashJTable extends JTable {
	private Color backGroundColor = ZelCashUI.table;
	private Color headerBackGroundColor = ZelCashUI.tableHeader;
	private Color textColor = ZelCashUI.text;
	public ZelCashJTable() {
		super();
		this.setBackground(backGroundColor);
		this.getTableHeader().setBackground(headerBackGroundColor);
		this.setForeground(textColor);
		this.getTableHeader().setForeground(textColor);
	}

	public ZelCashJTable(int numRows, int numColumns) {
		super(numRows, numColumns);
		this.setBackground(backGroundColor);
		this.getTableHeader().setBackground(headerBackGroundColor);
		this.setForeground(textColor);
		this.getTableHeader().setForeground(textColor);
	}

	public ZelCashJTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		this.setBackground(backGroundColor);
		this.getTableHeader().setBackground(headerBackGroundColor);
		this.setForeground(textColor);
		this.getTableHeader().setForeground(textColor);
	}

	public ZelCashJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
		this.setBackground(backGroundColor);
		this.getTableHeader().setBackground(headerBackGroundColor);
		this.setForeground(textColor);
		this.getTableHeader().setForeground(textColor);
	}

	public ZelCashJTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
		this.setBackground(backGroundColor);
		this.getTableHeader().setBackground(headerBackGroundColor);
		this.setForeground(textColor);
		this.getTableHeader().setForeground(textColor);
	}

	public ZelCashJTable(TableModel dm) {
		super(dm);
		this.setBackground(backGroundColor);
		this.getTableHeader().setBackground(headerBackGroundColor);
		this.setForeground(textColor);
		this.getTableHeader().setForeground(textColor);
	}

	public ZelCashJTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
		super(rowData, columnNames);
		this.setBackground(backGroundColor);
		this.getTableHeader().setBackground(headerBackGroundColor);
		this.setForeground(textColor);
		this.getTableHeader().setForeground(textColor);
	}


	
}

