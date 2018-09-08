package com.egrasoft.ss.translater.service;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationService {
    private static final String RESOURCE_BUNDLE_LOCATION = "translater/localization/locales";

    public String getString(String key) {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_LOCATION).getString(key);
    }

    public String getString(String key, Locale locale) {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_LOCATION, locale).getString(key);
    }

    public static LocalizationService getInstance() {
        return SingletonHelper.instance;
    }

    private LocalizationService() {}

    private static class SingletonHelper {
        private static final LocalizationService instance = new LocalizationService();
    }
}
