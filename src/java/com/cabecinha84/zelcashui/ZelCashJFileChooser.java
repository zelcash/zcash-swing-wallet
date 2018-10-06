package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;


public class ZelCashJFileChooser extends JFileChooser {
	private static Color backGroundColor = ZelCashUI.filechooser;
	
	public ZelCashJFileChooser() {
		super();
		this.setBackground(backGroundColor);
		changeColor(this.getComponents());
	}

	public ZelCashJFileChooser(File currentDirectory, FileSystemView fsv) {
		super(currentDirectory, fsv);
		this.setBackground(backGroundColor);
		changeColor(this.getComponents());
	}

	public ZelCashJFileChooser(File currentDirectory) {
		super(currentDirectory);
		this.setBackground(backGroundColor);
		changeColor(this.getComponents());
	}

	public ZelCashJFileChooser(FileSystemView fsv) {
		super(fsv);
		this.setBackground(backGroundColor);
		changeColor(this.getComponents());
	}

	public ZelCashJFileChooser(String currentDirectoryPath, FileSystemView fsv) {
		super(currentDirectoryPath, fsv);
		this.setBackground(backGroundColor);
		changeColor(this.getComponents());
	}

	public ZelCashJFileChooser(String currentDirectoryPath) {
		super(currentDirectoryPath);
		this.setBackground(backGroundColor);
		changeColor(this.getComponents());
	}
	
	private void changeColor(Component[] comp)
    {
         for(int x=0; x<comp.length; x++)
         {
              try
              {
                   comp[x].setBackground(backGroundColor);
              }
              catch(Exception e) {}
         if(comp[x] instanceof Container)
              changeColor(((Container)comp[x]).getComponents());
         }
    }
		
}

