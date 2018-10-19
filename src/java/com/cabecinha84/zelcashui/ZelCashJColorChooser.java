package com.cabecinha84.zelcashui;

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import com.vaklinov.zcashui.OSUtil;
import com.vaklinov.zcashui.OSUtil.OS_TYPE;

public class ZelCashJColorChooser {

	public static Color showDialog(Component component, String title, Color initialColor) throws HeadlessException {
		final JColorChooser chooser = new JColorChooser(initialColor != null ? initialColor : Color.white);

		OS_TYPE os = OSUtil.getOSType();
    	
    	if (os == OS_TYPE.WINDOWS)
    	{
    		AbstractColorChooserPanel[] panels=chooser.getChooserPanels();
            for(AbstractColorChooserPanel p:panels){
                String displayName=p.getDisplayName();
                switch (displayName) {
    	            case "RGB":
    	            	chooser.removeChooserPanel(p);
    	                break;
                    case "HSV":
                    	chooser.removeChooserPanel(p);
                        break;
                    case "HSL":
                    	chooser.removeChooserPanel(p);
                        break;
                    case "CMYK":
                    	chooser.removeChooserPanel(p);
                        break;
                }
            }
    	}

		// creating dialog
		ColorTracker ok = new ColorTracker(chooser);
		JDialog dialog = JColorChooser.createDialog(component, title, true, chooser, ok, null);
		dialog.setVisible(true); // blocks until user brings dialog down...
		return ok.getColor();
	}
	
	/**
	 * Small helper class for the color selection dialog
	 */
	private static class ColorTracker implements ActionListener {

		JColorChooser chooser;
		Color color;

		public ColorTracker(JColorChooser c) {
			chooser = c;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			color = chooser.getColor();
		}

		public Color getColor() {
			return color;
		}
	}
}
