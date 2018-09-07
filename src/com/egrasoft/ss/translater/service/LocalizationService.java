package com.egrasoft.ss.translater.service;

import java.util.Locale;

public interface LocalizationService {
    String getString(String key);
    String getString(String key, Locale locale);
}
