package com.vaklinov.zcashui;

import java.util.Locale;

import javax.swing.Icon;

import com.cabecinha84.zelcashui.ZelCashJRadioButtonMenuItem;

public class LanguageMenuItem extends ZelCashJRadioButtonMenuItem {

    private Locale locale;

    public LanguageMenuItem(String text, Icon icon, Locale locale) {
        super(text,icon);
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }
}
