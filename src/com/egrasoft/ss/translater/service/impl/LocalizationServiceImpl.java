package com.egrasoft.ss.translater.service.impl;

import com.egrasoft.ss.translater.service.LocalizationService;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationServiceImpl implements LocalizationService {
    private static final String RESOURCE_BUNDLE_NAME = "translater/localization/locales";

    @Override
    public String getString(String key) {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString(key);
    }

    @Override
    public String getString(String key, Locale locale) {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, locale).getString(key);
    }

    public static LocalizationService getInstance() {
        return SingletonHelper.instance;
    }

    private static class SingletonHelper {
        private static final LocalizationService instance = new LocalizationServiceImpl();
    }
}
