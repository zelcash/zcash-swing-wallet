package com.cabecinha84.zelcashui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Stroke;

public class ZelCashPresentationPanel extends ZelCashJPanel {
	static final Color  colorBorder = ZelCashUI.presentationpanelBorder;
	static final Color  backGroundColor    = ZelCashUI.presentationpanel;
	static final int GRADIENT_EXTENT = 17;

	static final Stroke edgeStroke  = new BasicStroke(1);

	public ZelCashPresentationPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 6));
	}
	
	
	public void paintComponent(Graphics graphics) 
	{
		int h = getHeight();
		int w =  getWidth();
		
		if (h < GRADIENT_EXTENT + 1) 
		{
			super.paintComponent(graphics);
			return;
		}
		
		float percentageOfGradient = (float) GRADIENT_EXTENT / h;
		
		if (percentageOfGradient > 0.49f)
		{
			percentageOfGradient = 0.49f;
		}
		
		Graphics2D graphics2D = (Graphics2D) graphics;
		
		float fractions[] = new float[] 
		{ 
			0, percentageOfGradient, 1 - percentageOfGradient, 1f 
		};
		
		Color colors[] = new Color[] 
		{ 
			backGroundColor, backGroundColor, backGroundColor, backGroundColor 
		};
		
		LinearGradientPaint paint = new LinearGradientPaint(0, 0, 0, h - 1, fractions, colors);
		
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setPaint(paint);
		graphics2D.fillRoundRect(0, 0, w - 1, h - 1, GRADIENT_EXTENT, GRADIENT_EXTENT);
		graphics2D.setColor(colorBorder);
		graphics2D.setStroke(edgeStroke);
		graphics2D.drawRoundRect(0, 0, w - 1, h - 1, GRADIENT_EXTENT, GRADIENT_EXTENT);
	}

	
}

