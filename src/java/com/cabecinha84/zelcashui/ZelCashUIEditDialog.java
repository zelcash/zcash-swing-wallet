package com.cabecinha84.zelcashui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

import com.vaklinov.zcashui.LanguageUtil;
import com.vaklinov.zcashui.Log;
import com.vaklinov.zcashui.OSUtil;
import com.vaklinov.zcashui.ZCashClientCaller.WalletCallException;
import com.vaklinov.zcashui.ZCashUI;

/**
 * Dialog showing the information about a user's identity
 */
public class ZelCashUIEditDialog
	extends ZelCashJDialog
{
	private static final String WHITETIER1DEFAULT = "#ffffff";
	private static final String WHITETIER2DEFAULT = "#cceeff";
	private static final String WHITETIER3DEFAULT = "#0069cc";
	private static final String WHITETEXTDEFAULT = "#000000";
	private static final String WHITEMESSAGESENTDEFAULT = "#0000ff";
	private static final String WHITEMESSAGERECEIVEDDEFAULT = "#ff0000";
	
	private static final String BLUETIER1DEFAULT = "#0073e6";
	private static final String BLUETIER2DEFAULT = "#0033ff";
	private static final String BLUETIER3DEFAULT = "#0033cc";
	private static final String BLUETEXTDEFAULT = "#ffffff";
	private static final String BLUEMESSAGESENTDEFAULT = "#00ff33";
	private static final String BLUEMESSAGERECEIVEDDEFAULT = "#ff0000";
	
	private static final String GREENTIER1DEFAULT = "#b3e6b3";
	private static final String GREENTIER2DEFAULT = "#ffecb3";
	private static final String GREENTIER3DEFAULT = "#39ac39";
	private static final String GREENTEXTDEFAULT = "#000000";
	private static final String GREENMESSAGESENTDEFAULT = "#0000ff";
	private static final String GREENMESSAGERECEIVEDDEFAULT = "#ff0000";
	
	private static final String REDTIER1DEFAULT = "#ff0000";
	private static final String REDTIER2DEFAULT = "#ff4d4d";
	private static final String REDTIER3DEFAULT = "#ffffff";
	private static final String REDTEXTDEFAULT = "#ffffff";
	private static final String REDMESSAGESENTDEFAULT = "#00ff33";
	private static final String REDMESSAGERECEIVEDDEFAULT = "#1a1aff";
	
	private static final String GREYTIER1DEFAULT = "#52527a";
	private static final String GREYTIER2DEFAULT = "#8585ad";
	private static final String GREYTIER3DEFAULT = "#ffffff";
	private static final String GREYTEXTDEFAULT = "#ffffff";
	private static final String GREYMESSAGESENTDEFAULT = "#00ff33";
	private static final String GREYMESSAGERECEIVEDDEFAULT = "#1a1aff";
	
	private static final String THUNDERTIER1DEFAULT = "#212121";
	private static final String THUNDERTIER2DEFAULT = "#484848";
	private static final String THUNDERTIER3DEFAULT = "#484848";
	private static final String THUNDERTEXTDEFAULT = "#4dd0e1";
	private static final String THUNDERMESSAGESENTDEFAULT = "#00e676";
	private static final String THUNDERMESSAGERECEIVEDDEFAULT = "#40c4ff";
	
	protected ZelCashJTextField tierOneColor;
	protected ZelCashJTextField tierTwoColor;
	protected ZelCashJTextField tierThreeColor;
	protected ZelCashJTextField textColorTextField;
	protected ZelCashJTextField messageSentTextField;
	protected ZelCashJTextField messageReceivedTextField;
	protected ZelCashJComboBox currencyOptions;
	
	private String currency;
	private String tier1Color;
	private String tier2Color;
	private String tier3Color;
	private String textColor;
	private String messageSentColor;
	private String messageReceivedColor;
	
	private Color color1;
	private Color color2;
	private Color color3;
	private Color colorText;
	private Color colorMessageSent;
	private Color colorMessageReceived;
	
	private ZelCashJFrame parentFrame;
	
	private static ZelCashJButton thunderThemeButton;
	private static ZelCashJButton greyThemeButton;
	private static ZelCashJButton whiteThemeButton;
	private static ZelCashJButton blueThemeButton;
	private static ZelCashJButton greenThemeButton;
	private static ZelCashJButton redThemeButton;
	private static ZelCashJButton saveButton;
	private static ZelCashJButton tier1Button;
	private static ZelCashJButton tier2Button;
	private static ZelCashJButton tier3Button;
	private static ZelCashJButton textButton;
	private static ZelCashJButton messageSentButton;
	private static ZelCashJButton messageReceivedButton;
	
	
	public ZelCashUIEditDialog(ZelCashJFrame parent)
			throws UnsupportedEncodingException
	{
		parentFrame = parent;
		loadZelCashUISettings();
		LanguageUtil langUtil = LanguageUtil.instance();
		this.setTitle(langUtil.getString("dialog.zelcashuiedit.title"));
		this.setSize(620, 440);
	    this.setLocation(100, 100);
		this.setLocationRelativeTo(parent);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


		ZelCashJPanel tempPanel = new ZelCashJPanel(new BorderLayout(0, 0));
		tempPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		ZelCashJLabel infoLabel = new ZelCashJLabel(langUtil.getString("dialog.zelcashuiedit.info"));
	    tempPanel.add(infoLabel, BorderLayout.CENTER);
		this.getContentPane().add(tempPanel, BorderLayout.NORTH);
		
		tier1Button = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.selectColor"));
		tier2Button = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.selectColor"));
		tier3Button = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.selectColor"));
		textButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.selectColor"));
		messageSentButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.selectColor"));
		messageReceivedButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.selectColor"));
		currencyOptions = new ZelCashJComboBox<>(ZelCashUI.currencys);
		currencyOptions.setSelectedItem(currency);
		
		ZelCashJPanel detailsPanel = new ZelCashJPanel();
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

		addFormField(detailsPanel, langUtil.getString("dialog.zelcashuiedit.currency"),  currencyOptions);
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashuiedit.textcolor"),  textColorTextField = new ZelCashJTextField(7), textButton);
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashuiedit.message.sent.color"),  messageSentTextField = new ZelCashJTextField(7), messageSentButton);
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashuiedit.message.received.color"),  messageReceivedTextField = new ZelCashJTextField(7), messageReceivedButton);
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashuiedit.tier1color"),  tierOneColor = new ZelCashJTextField(7), tier1Button);
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashuiedit.tier2color"),  tierTwoColor = new ZelCashJTextField(7), tier2Button);
		addFormField(detailsPanel, langUtil.getString("dialog.zelcashuiedit.tier3color"),  tierThreeColor = new ZelCashJTextField(7), tier3Button);

		textColorTextField.setText(textColor);
		messageSentTextField.setText(messageSentColor);
		messageReceivedTextField.setText(messageReceivedColor);
		tierOneColor.setText(tier1Color);
		tierTwoColor.setText(tier2Color);
		tierThreeColor.setText(tier3Color);
		textColorTextField.setEditable(false);
		messageSentTextField.setEditable(false);
		messageReceivedTextField.setEditable(false);
		tierOneColor.setEditable(false);
		tierTwoColor.setEditable(false);
		tierThreeColor.setEditable(false);
		textColorTextField.setForeground(colorText);
		messageSentTextField.setForeground(colorMessageSent);
		messageReceivedTextField.setForeground(colorMessageReceived);
		tierOneColor.setBackground(color1);
		tierTwoColor.setBackground(color2);
		tierThreeColor.setBackground(color3);
		detailsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		ZelCashJPanel themePanel = new ZelCashJPanel();
		themePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		whiteThemeButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.set.white.theme"));
		themePanel.add(whiteThemeButton);
		blueThemeButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.set.blue.theme"));
		themePanel.add(blueThemeButton);
		greenThemeButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.set.green.theme"));
		themePanel.add(greenThemeButton);
		themePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		detailsPanel.add(themePanel);
		
		
		ZelCashJPanel themePanel2 = new ZelCashJPanel();
		themePanel2.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		redThemeButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.set.red.theme"));
		themePanel2.add(redThemeButton);
		greyThemeButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.set.grey.theme"));
		themePanel2.add(greyThemeButton);
		thunderThemeButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.set.thunder.theme"));
		themePanel2.add(thunderThemeButton);
		
		detailsPanel.add(themePanel2);
		
		this.getContentPane().add(detailsPanel, BorderLayout.CENTER);
	
		

		ZelCashJPanel closePanel = new ZelCashJPanel();
		closePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		ZelCashJButton closeButon = new ZelCashJButton(langUtil.getString("dialog.about.button.close.text"));
		closePanel.add(closeButon);
		saveButton = new ZelCashJButton(langUtil.getString("dialog.zelcashuiedit.save"));
		closePanel.add(saveButton);
		this.getContentPane().add(closePanel, BorderLayout.SOUTH);
		
		closeButon.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					ZelCashUIEditDialog.this.parentFrame.repaint();
					ZelCashUIEditDialog.this.setVisible(false);
					ZelCashUIEditDialog.this.dispose();
				}
		});
		
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveSettings();
				saveButton.setSelected(false);
				saveButton.setFocusable(false);
			}
		});
		
		
		whiteThemeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tier1Color = WHITETIER1DEFAULT;
				tier2Color = WHITETIER2DEFAULT;
				tier3Color = WHITETIER3DEFAULT;
				textColor = WHITETEXTDEFAULT;
				messageSentColor = WHITEMESSAGESENTDEFAULT;
				messageReceivedColor = WHITEMESSAGERECEIVEDDEFAULT;
				color1 = Color.decode(tier1Color);
				color2 = Color.decode(tier2Color);
				color3 = Color.decode(tier3Color);
				colorText = Color.decode(textColor);
				colorMessageSent = Color.decode(messageSentColor);
				colorMessageReceived = Color.decode(messageReceivedColor);
				tierOneColor.setBackground(color1);
				tierTwoColor.setBackground(color2);
				tierThreeColor.setBackground(color3);
				textColorTextField.setForeground(colorText);
				messageSentTextField.setForeground(colorMessageSent);
				messageReceivedTextField.setForeground(colorMessageReceived);			
				saveSettings();
				whiteThemeButton.setSelected(false);
				whiteThemeButton.setFocusable(false);
			}
		});
		
		thunderThemeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tier1Color = THUNDERTIER1DEFAULT;
				tier2Color = THUNDERTIER2DEFAULT;
				tier3Color = THUNDERTIER3DEFAULT;
				textColor = THUNDERTEXTDEFAULT;
				messageSentColor = THUNDERMESSAGESENTDEFAULT;
				messageReceivedColor = THUNDERMESSAGERECEIVEDDEFAULT;
				color1 = Color.decode(tier1Color);
				color2 = Color.decode(tier2Color);
				color3 = Color.decode(tier3Color);
				colorText = Color.decode(textColor);
				colorMessageSent = Color.decode(messageSentColor);
				colorMessageReceived = Color.decode(messageReceivedColor);
				tierOneColor.setBackground(color1);
				tierTwoColor.setBackground(color2);
				tierThreeColor.setBackground(color3);
				textColorTextField.setForeground(colorText);
				messageSentTextField.setForeground(colorMessageSent);
				messageReceivedTextField.setForeground(colorMessageReceived);		
				saveSettings();
				thunderThemeButton.setSelected(false);
				thunderThemeButton.setFocusable(false);
			}
		});
		
		greenThemeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tier1Color = GREENTIER1DEFAULT;
				tier2Color = GREENTIER2DEFAULT;
				tier3Color = GREENTIER3DEFAULT;
				textColor = GREENTEXTDEFAULT;
				messageSentColor = GREENMESSAGESENTDEFAULT;
				messageReceivedColor = GREENMESSAGERECEIVEDDEFAULT;
				color1 = Color.decode(tier1Color);
				color2 = Color.decode(tier2Color);
				color3 = Color.decode(tier3Color);
				colorText = Color.decode(textColor);
				colorMessageSent = Color.decode(messageSentColor);
				colorMessageReceived = Color.decode(messageReceivedColor);
				tierOneColor.setBackground(color1);
				tierTwoColor.setBackground(color2);
				tierThreeColor.setBackground(color3);
				textColorTextField.setForeground(colorText);
				messageSentTextField.setForeground(colorMessageSent);
				messageReceivedTextField.setForeground(colorMessageReceived);			
				saveSettings();
				greenThemeButton.setSelected(false);
				greenThemeButton.setFocusable(false);
			}
		});
		
		redThemeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tier1Color = REDTIER1DEFAULT;
				tier2Color = REDTIER2DEFAULT;
				tier3Color = REDTIER3DEFAULT;
				textColor = REDTEXTDEFAULT;
				messageSentColor = REDMESSAGESENTDEFAULT;
				messageReceivedColor = REDMESSAGERECEIVEDDEFAULT;
				color1 = Color.decode(tier1Color);
				color2 = Color.decode(tier2Color);
				color3 = Color.decode(tier3Color);
				colorText = Color.decode(textColor);
				colorMessageSent = Color.decode(messageSentColor);
				colorMessageReceived = Color.decode(messageReceivedColor);
				tierOneColor.setBackground(color1);
				tierTwoColor.setBackground(color2);
				tierThreeColor.setBackground(color3);
				textColorTextField.setForeground(colorText);
				messageSentTextField.setForeground(colorMessageSent);
				messageReceivedTextField.setForeground(colorMessageReceived);
				saveSettings();
				redThemeButton.setSelected(false);
				redThemeButton.setFocusable(false);
			}
		});
		
		blueThemeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tier1Color = BLUETIER1DEFAULT;
				tier2Color = BLUETIER2DEFAULT;
				tier3Color = BLUETIER3DEFAULT;
				textColor = BLUETEXTDEFAULT;
				messageSentColor = BLUEMESSAGESENTDEFAULT;
				messageReceivedColor = BLUEMESSAGERECEIVEDDEFAULT;
				color1 = Color.decode(tier1Color);
				color2 = Color.decode(tier2Color);
				color3 = Color.decode(tier3Color);
				colorText = Color.decode(textColor);
				colorMessageSent = Color.decode(messageSentColor);
				colorMessageReceived = Color.decode(messageReceivedColor);
				tierOneColor.setBackground(color1);
				tierTwoColor.setBackground(color2);
				tierThreeColor.setBackground(color3);
				textColorTextField.setForeground(colorText);
				messageSentTextField.setForeground(colorMessageSent);
				messageReceivedTextField.setForeground(colorMessageReceived);
				saveSettings();
				blueThemeButton.setSelected(false);
				blueThemeButton.setFocusable(false);
			}
		});
		
		greyThemeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tier1Color = GREYTIER1DEFAULT;
				tier2Color = GREYTIER2DEFAULT;
				tier3Color = GREYTIER3DEFAULT;
				textColor = GREYTEXTDEFAULT;
				messageSentColor = GREYMESSAGESENTDEFAULT;
				messageReceivedColor = GREYMESSAGERECEIVEDDEFAULT;
				color1 = Color.decode(tier1Color);
				color2 = Color.decode(tier2Color);
				color3 = Color.decode(tier3Color);
				colorText = Color.decode(textColor);
				colorMessageSent = Color.decode(messageSentColor);
				colorMessageReceived = Color.decode(messageReceivedColor);
				tierOneColor.setBackground(color1);
				tierTwoColor.setBackground(color2);
				tierThreeColor.setBackground(color3);
				textColorTextField.setForeground(colorText);
				messageSentTextField.setForeground(colorMessageSent);
				messageReceivedTextField.setForeground(colorMessageReceived);
				saveSettings();
				greyThemeButton.setSelected(false);
				greyThemeButton.setFocusable(false);
			}
		});
		
		textButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color color = ZelCashJColorChooser.showDialog(ZelCashUIEditDialog.this, "Pick Color", colorText);
				ZelCashUIEditDialog.this.repaint();
				ZelCashUIEditDialog.this.parentFrame.repaint();
				if(color == null) {
					colorText = Color.decode(textColor);
				}
				else {
					textColor = toHexString(color);
					textColorTextField.setForeground(color);
					textColorTextField.setText(textColor);
				}
				textButton.setSelected(false);
				textButton.setFocusable(false);
				
			}
		});
		
		messageSentButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color color = ZelCashJColorChooser.showDialog(ZelCashUIEditDialog.this, "Pick Color", colorMessageSent);
				ZelCashUIEditDialog.this.repaint();
				ZelCashUIEditDialog.this.parentFrame.repaint();
				if(color == null) {
					colorMessageSent = Color.decode(messageSentColor);
				}
				else {
					messageSentColor = toHexString(color);
					messageSentTextField.setForeground(color);
					messageSentTextField.setText(messageSentColor);
				}
				messageSentButton.setSelected(false);
				messageSentButton.setFocusable(false);
				
			}
		});
		
		messageReceivedButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color color = ZelCashJColorChooser.showDialog(ZelCashUIEditDialog.this, "Pick Color", colorMessageReceived);
				ZelCashUIEditDialog.this.repaint();
				ZelCashUIEditDialog.this.parentFrame.repaint();
				if(color == null) {
					colorMessageReceived = Color.decode(messageReceivedColor);
				}
				else {
					messageReceivedColor = toHexString(color);
					messageReceivedTextField.setForeground(color);
					messageReceivedTextField.setText(messageReceivedColor);
				}
				messageReceivedButton.setSelected(false);
				messageReceivedButton.setFocusable(false);
				
			}
		});
		
		tier1Button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color color = ZelCashJColorChooser.showDialog(ZelCashUIEditDialog.this, "Pick Color", color1);
				ZelCashUIEditDialog.this.repaint();
				ZelCashUIEditDialog.this.parentFrame.repaint();
				if(color == null) {
                    color1 = Color.decode(tier1Color);
				}
				else {
					tier1Color = toHexString(color);
					tierOneColor.setText(tier1Color);
					tierOneColor.setBackground(color);
				}
				tier1Button.setSelected(false);
				tier1Button.setFocusable(false);
			}
		});
		
		tier2Button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color color = ZelCashJColorChooser.showDialog(ZelCashUIEditDialog.this, "Pick Color", color2);
				ZelCashUIEditDialog.this.repaint();
				ZelCashUIEditDialog.this.parentFrame.repaint();
				if(color == null) {
                    color2 = Color.decode(tier2Color);
				}
				else {
					tier2Color = toHexString(color);
					tierTwoColor.setText(tier2Color);
					tierTwoColor.setBackground(color);
				}
				tier2Button.setSelected(false);
				tier2Button.setFocusable(false);
			}
		});
		
		tier3Button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color color = ZelCashJColorChooser.showDialog(ZelCashUIEditDialog.this, "Pick Color", color3);
				ZelCashUIEditDialog.this.repaint();
				ZelCashUIEditDialog.this.parentFrame.repaint();
				if(color == null) {
                    color3 = Color.decode(tier3Color);
				}
				else {
					tier3Color = toHexString(color);
					tierThreeColor.setText(tier3Color);
					tierThreeColor.setBackground(color);
				}
				tier3Button.setSelected(false);
				tier3Button.setFocusable(false);
			}
		});
		
		pack();
	}
	
	public final static String toHexString(Color colour) throws NullPointerException {
		  String hexColour = Integer.toHexString(colour.getRGB() & 0xffffff);
		  if (hexColour.length() < 6) {
		    hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
		  }
		  return "#" + hexColour;
		}
	
	private void addFormField(ZelCashJPanel detailsPanel, String name, JComponent field, ZelCashJButton button)
	{
		ZelCashJPanel tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
		ZelCashJLabel tempLabel = new ZelCashJLabel(name, JLabel.RIGHT);
		// TODO: hard sizing of labels may not scale!
		final int width = new ZelCashJLabel("Sender identification T address:").getPreferredSize().width + 10;
		tempLabel.setPreferredSize(new Dimension(width, tempLabel.getPreferredSize().height));
		tempPanel.add(tempLabel);
		tempPanel.add(field);
		tempPanel.add(button);
		detailsPanel.add(tempPanel);
	}
	
	private void addFormField(ZelCashJPanel detailsPanel, String name, JComponent field)
	{
		ZelCashJPanel tempPanel = new ZelCashJPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
		ZelCashJLabel tempLabel = new ZelCashJLabel(name, JLabel.RIGHT);
		// TODO: hard sizing of labels may not scale!
		final int width = new ZelCashJLabel("Sender identification T address:").getPreferredSize().width + 10;
		tempLabel.setPreferredSize(new Dimension(width, tempLabel.getPreferredSize().height));
		tempPanel.add(tempLabel);
		tempPanel.add(field);
		detailsPanel.add(tempPanel);
	}
	

	private void loadZelCashUISettings() {
		try {
			
			String settingsDir = OSUtil.getSettingsDirectory();
			File zelcashConf = new File(settingsDir + File.separator + "zelcash_ui.properties");
			if (!zelcashConf.exists())
			{
				Log.error("Could not find file: {0} !", zelcashConf.getAbsolutePath());
				
			} 
			Log.info("File zelcash_ui.properties found");
			Properties confProps = new Properties();
			FileInputStream fis = null;
			try
			{
				Log.info("Lets parse all the ui settings");
				fis = new FileInputStream(zelcashConf);
				confProps.load(fis);
				textColor = confProps.getProperty(ZelCashUI.TEXT_PROPERTY_COLOR)!= null? confProps.getProperty(ZelCashUI.TEXT_PROPERTY_COLOR).trim():ZelCashUI.DEFAULT_COLOR_BLACK; 
				messageSentColor = confProps.getProperty(ZelCashUI.MESSAGE_SENT_PROPERTY_COLOR)!= null? confProps.getProperty(ZelCashUI.MESSAGE_SENT_PROPERTY_COLOR).trim():ZelCashUI.DEFAULT_COLOR_BLUE; 
				messageReceivedColor = confProps.getProperty(ZelCashUI.MESSAGE_RECEIVED_PROPERTY_COLOR)!= null? confProps.getProperty(ZelCashUI.MESSAGE_RECEIVED_PROPERTY_COLOR).trim():ZelCashUI.DEFAULT_COLOR_RED; 
				tier1Color = confProps.getProperty(ZelCashUI.FRAME_PROPERTY_COLOR)!= null? confProps.getProperty(ZelCashUI.FRAME_PROPERTY_COLOR).trim():ZelCashUI.DEFAULT_COLOR; 
				tier2Color = confProps.getProperty(ZelCashUI.TABLE_HEADER_PROPERTY_COLOR)!= null? confProps.getProperty(ZelCashUI.TABLE_HEADER_PROPERTY_COLOR).trim():ZelCashUI.DEFAULT_COLOR; 
				tier3Color = confProps.getProperty(ZelCashUI.PROGRESSBAR_FOREGROUND_PROPERTY_COLOR)!= null? confProps.getProperty(ZelCashUI.PROGRESSBAR_FOREGROUND_PROPERTY_COLOR).trim():ZelCashUI.DEFAULT_COLOR; 
				currency = confProps.getProperty(ZelCashUI.CURRENCY)!= null? confProps.getProperty(ZelCashUI.CURRENCY).toUpperCase().trim():ZelCashUI.DEFAULT_CURRENCY; 
				color1 = Color.decode(tier1Color);
				color2 = Color.decode(tier2Color);
				color3 = Color.decode(tier3Color);
				colorText = Color.decode(textColor);
				colorMessageSent = Color.decode(messageSentColor);
				colorMessageReceived = Color.decode(messageReceivedColor);
				
			} finally
			{
				Log.info("File parsed");
				if (fis != null)
				{
					fis.close();
				}
			}
		}
		catch(Exception e) {
			Log.warning("Error obtaining properties from zelcash_ui.properties file due to: {0} {1}",
					e.getClass().getName(), e.getMessage());
		}	
	}

	private void saveZelCashUISettings() {
		try {
			
			String settingsDir = OSUtil.getSettingsDirectory();
			File zelcashConf = new File(settingsDir + File.separator + "zelcash_ui.properties");
			if (!zelcashConf.exists())
			{
				Log.error("Could not find file: {0} !", zelcashConf.getAbsolutePath());
				
			} 
			Log.info("File zelcash_ui.properties found");
			Properties confProps = new Properties();
			FileInputStream fis = null;
			FileOutputStream fr = null;  
	        
			try
			{
				Log.info("Lets parse all the ui settings");
				fis = new FileInputStream(zelcashConf);
				fr = new FileOutputStream(zelcashConf);
				confProps.load(fis);
				confProps.setProperty(ZelCashUI.BUTTON_PROPERTY_COLOR, tier2Color);
				confProps.setProperty(ZelCashUI.BUTTON_SELECT_PROPERTY_COLOR, tier2Color); 
				confProps.setProperty(ZelCashUI.CHECKBOX_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.CHECKBOX_SELECT_PROPERTY_COLOR, tier2Color);
				confProps.setProperty(ZelCashUI.COLORCHOOSER_PROPERTY_COLOR, tier1Color);
				confProps.setProperty(ZelCashUI.COMBOBOX_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.CONTAINER_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.DIALOG_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.FILECHOOSER_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.FRAME_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.LIST_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.MENU_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.MENU_SELECTION_PROPERTY_COLOR, tier2Color); 
				confProps.setProperty(ZelCashUI.MENUBAR_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.MENUITEM_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.MENUITEM_SELECTION_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.PANEL_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.PASSWORDFIELD_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.POPUPMENU_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.PRESENTATIONPANEL_PROPERTY_COLOR, tier2Color); 
				confProps.setProperty(ZelCashUI.PRESENTATIONPANEL_BORDER_PROPERTY_COLOR, tier2Color); 
				confProps.setProperty(ZelCashUI.PROGRESSBAR_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.PROGRESSBAR_FOREGROUND_PROPERTY_COLOR, tier3Color); 
				confProps.setProperty(ZelCashUI.RADIOBUTTON_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.SCROLLBAR_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.SCROLLBAR_FOREGROUND_PROPERTY_COLOR, tier3Color); 
				confProps.setProperty(ZelCashUI.SCROLLBAR_THUMB_PROPERTY_COLOR, tier3Color); 
				confProps.setProperty(ZelCashUI.SCROLLPANE_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.SPLITPANE_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.STARTUP_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.TABBEDPANE_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.TABBEDPANE_UNSELECTED_PROPERTY_COLOR, tier2Color); 
				confProps.setProperty(ZelCashUI.TABLE_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.TABLE_HEADER_PROPERTY_COLOR, tier2Color); 
				confProps.setProperty(ZelCashUI.TEXTAREA_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.TEXTFIELD_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.TEXTPANE_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.TOOLTIP_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.VIEWPORT_PROPERTY_COLOR, tier1Color); 
				confProps.setProperty(ZelCashUI.TEXT_PROPERTY_COLOR, textColor); 
				confProps.setProperty(ZelCashUI.MESSAGE_SENT_PROPERTY_COLOR, messageSentColor);
				confProps.setProperty(ZelCashUI.MESSAGE_RECEIVED_PROPERTY_COLOR, messageReceivedColor);
				confProps.setProperty(ZelCashUI.CURRENCY, currencyOptions.getSelectedItem().toString());  
				confProps.store(fr, "Save zelcash_ui.properties file");
				
			} finally
			{
				if (fr != null) {
					fr.close();
				}
				if (fis != null)
				{
					fis.close();
				}
			}
		}
		catch(Exception e) {
			Log.warning("Error obtaining properties from zelcash_ui.properties file due to: {0} {1}",
					e.getClass().getName(), e.getMessage());
		}	
	}
	
	private void saveSettings() {
		ZelCashUIEditDialog.this.saveZelCashUISettings();			
		new ZelCashUI();
		try {
			ZCashUI z = new ZCashUI(null);
			ZelCashUIEditDialog.this.parentFrame.setVisible(false);
			ZelCashUIEditDialog.this.parentFrame.dispose();
			ZelCashUIEditDialog.this.parentFrame = z;	
			ZelCashUIEditDialog.this.parentFrame.repaint();
			ZelCashUIEditDialog.this.parentFrame.setVisible(true);
			ZelCashUIEditDialog dialog = new ZelCashUIEditDialog(ZelCashUIEditDialog.this.parentFrame);
			ZelCashUIEditDialog.this.setVisible(false);
			ZelCashUIEditDialog.this.dispose();
			dialog.setVisible(true);
		}
		catch (WalletCallException wce)
        {
        	Log.error("Unexpected error: ", wce);

            if ((wce.getMessage().indexOf("{\"code\":-28,\"message\"") != -1) ||
            	(wce.getMessage().indexOf("error code: -28") != -1))
            {
                JOptionPane.showMessageDialog(
                        null,
                        LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.text"),
                        LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.title"),
                        JOptionPane.ERROR_MESSAGE);
            } else
            {
                JOptionPane.showMessageDialog(
                    null,
                        LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.2.text", wce.getMessage()),
                        LanguageUtil.instance().getString("main.frame.option.pane.wallet.communication.error.2.title"),
                    JOptionPane.ERROR_MESSAGE);
            }

            System.exit(2);
        } catch (Exception ex)
        {
        	Log.error("Unexpected error: ", ex);
            JOptionPane.showMessageDialog(
                null,
                LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.text", ex.getMessage()),
                LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.title"),
                JOptionPane.ERROR_MESSAGE);
            System.exit(3);
        } catch (Error err)
        {
        	// Last resort catch for unexpected problems - just to inform the user
            err.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                    LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.2.text", err.getMessage()),
                    LanguageUtil.instance().getString("main.frame.option.pane.wallet.critical.error.2.title"),
                    JOptionPane.ERROR_MESSAGE);
            System.exit(4);
        }
		
	}

} // End public class ZelCashUIEditDialog
