package com.cabecinha84.zelcashui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vaklinov.zcashui.LanguageUtil;
import com.vaklinov.zcashui.Log;
import com.vaklinov.zcashui.OSUtil;

/**
 * Dialog showing QR Code
 */
public class ZelCashQRCodeDialog
	extends ZelCashJDialog
{
	
	public ZelCashQRCodeDialog(String qrCodeMessage)
			throws IOException
	{

		LanguageUtil langUtil = LanguageUtil.instance();
		this.setTitle(langUtil.getString("dialog.zelcashqrcode.title"));
		this.setSize(255, 300);
	    this.setLocation(100, 100);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


		ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(this.generateQRCodeImage(qrCodeMessage)));
		ZelCashJLabel qrcode = new ZelCashJLabel(new ImageIcon(img));
	    tempPanel.add(qrcode, BorderLayout.CENTER);
		this.getContentPane().add(tempPanel, BorderLayout.CENTER);
		

		ZelCashJPanel closePanel = new ZelCashJPanel();
		closePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		ZelCashJButton closeButon = new ZelCashJButton(langUtil.getString("dialog.about.button.close.text"));
		closePanel.add(closeButon);
		
		this.getContentPane().add(closePanel, BorderLayout.SOUTH);
		
		closeButon.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					ZelCashQRCodeDialog.this.setVisible(false);
					ZelCashQRCodeDialog.this.dispose();
				}
		});
		this.repaint();
		
	}
	
	private byte[] generateQRCodeImage(String myCodeText)
    {
		Log.info("Generating QR Code for text: "+myCodeText);
		int size = 250;
		byte[] pngData = null;
		try {
		
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
		    BitMatrix bitMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, 250, 250);
		    
		    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
		    pngData = pngOutputStream.toByteArray(); 

		}
		catch (Exception ex) {
			Log.warning("Error Generating QR CODE: "+ex.getMessage());
		}
		return pngData;
	}

} 
