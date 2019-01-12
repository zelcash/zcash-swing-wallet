package com.cabecinha84.zelcashui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import com.google.zxing.BarcodeFormat;
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
	
	public ZelCashQRCodeDialog(ZelCashJFrame parent, String qrCodeMessage)
			throws IOException
	{
		this.generateQRCodeImage(qrCodeMessage);
		LanguageUtil langUtil = LanguageUtil.instance();
		this.setTitle(langUtil.getString("dialog.zelcashqrcode.title"));
		this.setSize(255, 300);
	    this.setLocation(100, 100);
	    this.setLocationRelativeTo(parent);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


		ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		ZelCashJLabel qrcode = new ZelCashJLabel(new ImageIcon(OSUtil.getSettingsDirectory()+File.separator+"MyQRCode.png"));
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
		
	}
	
	private void generateQRCodeImage(String myCodeText)
    {
		Log.info("Generating QR Code for text: "+myCodeText);
		int size = 250;
		String fileType = "png";
		try {
			String settingsDir = OSUtil.getSettingsDirectory();
			File zelcashQRCodeFile = new File(settingsDir + File.separator + "MyQRCode.png");
			if (!zelcashQRCodeFile.exists())
			{
				Log.warning("Could not find file: {0}!", zelcashQRCodeFile.getAbsolutePath());
				
				
			} 
			
			Log.info("File MyQRCode.png found");
		
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			Log.info("qrCodeWriter created");
			BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
					size);
			Log.info("byteMatrix created");
			int CrunchifyWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
					BufferedImage.TYPE_INT_RGB);
			Log.info("BufferedImage created");
			image.createGraphics();
			Log.info("createGraphics created");
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
			graphics.setColor(Color.BLACK);
		
			for (int i = 0; i < CrunchifyWidth; i++) {
				for (int j = 0; j < CrunchifyWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			ImageIO.write(image, fileType, zelcashQRCodeFile);
		   Log.info("QR Code Saved");
		}
		catch (Exception ex) {
			Log.warning("Error Generating QR CODE: "+ex.getMessage());
		}
	}

} 
