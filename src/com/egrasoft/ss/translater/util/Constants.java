package com.egrasoft.ss.translater.util;

public class Constants {
    public static class Translation {
        public static final String RULES_DELIMITER_PATTERN = "[\n ,]+";
        public static final String TOKEN_PATTERN = "([\\p{L}\\d']+)";
        public static final String RULES_PATTERN = TOKEN_PATTERN + "=" + TOKEN_PATTERN;
    }

    public static class Frame {
        public static final String FRAME_TITLE_KEY = "frame.title";
    }

    public static class Dialogs {
        public static final String ABOUT_TITLE_KEY = "dialog.about.title";
        public static final String ABOUT_CONTENT_TEXT_KEY = "dialog.about.contenttext";
        public static final String FILE_ERROR_TITLE_KEY = "dialog.file.error.title";
        public static final String FILE_OPEN_TITLE_KEY = "dialog.file.open.title";
        public static final String FILE_OPEN_ERROR_CONTENT_TEXT_KEY = "dialog.file.open.error.contenttext";
        public static final String FILE_SAVE_TITLE_KEY = "dialog.file.save.title";
        public static final String FILE_SAVE_ERROR_CONTENT_TEXT_KEY = "dialog.file.save.error.contenttext";
        public static final String FILE_TEXT_EXTENSIONS_DESCRIPTION = "dialog.file.textextensions.description";
        public static final String ASK_FOR_SAVING_TITLE_KEY = "dialog.askforsaving.title";
        public static final String ASK_FOR_SAVING_CONTENT_TEXT_KEY = "dialog.askforsaving.contenttext";
        public static final String ASK_FOR_SAVING_CANCEL_KEY = "dialog.askforsaving.cancel";
        public static final String ASK_FOR_SAVING_SAVE_KEY = "dialog.askforsaving.save";
        public static final String ASK_FOR_SAVING_DONT_SAVE_KEY = "dialog.askforsaving.dontsave";
    }

    public static class Settings {
        public static final String SETTINGS_TITLE_KEY = "settings.title";
    }
}
