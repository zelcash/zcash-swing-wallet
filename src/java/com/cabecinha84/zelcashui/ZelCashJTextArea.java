package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import com.vaklinov.zcashui.LanguageUtil;

public class ZelCashJTextArea extends JTextArea {
	private Color backGroundColor = ZelCashUI.textarea;
	private Color textColor = ZelCashUI.text;
	private static LanguageUtil langUtil = LanguageUtil.instance();
	public ZelCashJTextArea() {
		super();
		this.setBackground(backGroundColor);
	}

	public ZelCashJTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addClipBoardMenuOptions();
	}

	public ZelCashJTextArea(Document doc) {
		super(doc);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addClipBoardMenuOptions();
	}

	public ZelCashJTextArea(int rows, int columns) {
		super(rows, columns);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addClipBoardMenuOptions();
	}

	public ZelCashJTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addClipBoardMenuOptions();
	}

	public ZelCashJTextArea(String text) {
		super(text);
		this.setBackground(backGroundColor);
		this.setForeground(textColor);
		this.addClipBoardMenuOptions();
	}
	
	private void addClipBoardMenuOptions() {

 		this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent e) {
            	int accelaratorKeyMask = Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask();
            	if(SwingUtilities.isRightMouseButton(e)) {
	                if (e.isPopupTrigger()) {
	                    final ZelCashJTextField component = (ZelCashJTextField)e.getComponent();
	                    final ZelCashJPopupMenu menu = new ZelCashJPopupMenu();
	                    ZelCashJMenuItem item;
	                    item = new ZelCashJMenuItem(new DefaultEditorKit.CopyAction());
	                    item.setText(langUtil.getString("copy"));
	                    item.setEnabled(component.getSelectionStart() != component.getSelectionEnd());
	                    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, accelaratorKeyMask));
	                    menu.add(item);
	                    item = new ZelCashJMenuItem(new DefaultEditorKit.CutAction());
	                    item.setText(langUtil.getString("cut"));
	                    item.setEnabled(component.isEditable() && component.getSelectionStart() != component.getSelectionEnd());
	                    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, accelaratorKeyMask));
	                    menu.add(item);
	                    item = new ZelCashJMenuItem(new DefaultEditorKit.PasteAction());
	                    item.setText(langUtil.getString("paste"));
	                    item.setEnabled(component.isEditable());
	                    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, accelaratorKeyMask));
	                    menu.add(item);
	                    menu.show(e.getComponent(), e.getX(), e.getY());
	                }
            	}
            }
        });

 		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.copyAction);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.cutAction);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.pasteAction);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.selectAllAction);
	}	
		
}

