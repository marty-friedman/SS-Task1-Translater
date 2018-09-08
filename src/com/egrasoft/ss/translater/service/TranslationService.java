package com.egrasoft.ss.translater.service;

import com.egrasoft.ss.translater.util.Constants;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationService {
    private TranslationRuleService translationRuleService = TranslationRuleService.getInstance();

    private Map<String, String> rules;
    private Pattern tokenPattern = Pattern.compile(Constants.Translation.TOKEN_PATTERN);

    public String translate(String source) {
        StringBuilder stringBuilder = new StringBuilder(source);
        Matcher matcher = tokenPattern.matcher(stringBuilder);

        while(matcher.find()) {
            String token = matcher.group();
            stringBuilder.replace(matcher.start(), matcher.end(), translateWord(token));
        }
        return stringBuilder.toString();
    }

    public void updateRules(Map<String, String> newRules) throws IllegalArgumentException {
        translationRuleService.validateRules(newRules);
        rules = newRules;
        try {
            translationRuleService.saveRules(rules);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Map<String, String> getRules() {
        return rules;
    }

    private String translateWord(String source) {
        String target = recursiveTranslateToken(source.toLowerCase());
        if (target != null) {
            if (Character.isUpperCase(source.charAt(0)))
                return target.substring(0, 1).toUpperCase() + target.substring(1);
            return target;
        }
        return source;
    }

    private String recursiveTranslateToken(String source) {
        if (rules.containsKey(source))
            return rules.get(source);
        int len = source.length();
        for (int i = len-1; i > 0; i--) {
            String prefixTarget = rules.get(source.substring(0, i));
            if (prefixTarget != null) {
                String postfixTarget = recursiveTranslateToken(source.substring(i));
                if (postfixTarget != null)
                    return prefixTarget + postfixTarget;
            }
        }
        return null;
    }

    public static TranslationService getInstance() {
        return SingletonHelper.instance;
    }

    private TranslationService() {
        rules = translationRuleService.readRules();
    }

    private static class SingletonHelper {
        private static final TranslationService instance = new TranslationService();
    }
}
