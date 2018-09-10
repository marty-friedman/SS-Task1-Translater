package com.egrasoft.ss.translater.service;

import com.egrasoft.ss.translater.throwable.RuleParseException;
import com.egrasoft.ss.translater.util.Constants;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TranslationRuleService {
    private static final String RULES_LOCATION = "translater/settings/rules";

    private Pattern rulesPattern = Pattern.compile(Constants.Translation.RULES_PATTERN);

    Map<String, String> readRules() {
        Map<String, String> rules = new HashMap<>();
        Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream(RULES_LOCATION));
        scanner.useDelimiter(Constants.Translation.RULES_DELIMITER_PATTERN);
        while(scanner.hasNext()) {
            Matcher matcher = rulesPattern.matcher(scanner.next().toLowerCase());
            if (!matcher.matches())
                throw new IllegalStateException("Invalid rule settings file syntax");
            rules.put(matcher.group(1), matcher.group(2));
        }
        scanner.close();
        return rules;
    }

    void validateRules(Map<String, String> rules) throws IllegalArgumentException {
        if (rules == null)
            throw new NullPointerException("Null rules entity");
        for (Map.Entry<String, String> rule : rules.entrySet()) {
            if (!rule.getKey().matches(Constants.Translation.TOKEN_PATTERN) || !rule.getValue().matches(Constants.Translation.TOKEN_PATTERN))
                throw new RuleParseException("Parse syntax error", rule);
        }
    }

    void saveRules(Map<String, String> rules) throws IOException, URISyntaxException {
        FileWriter ruleWriter = new FileWriter(new File(getClass().getClassLoader().getResource(RULES_LOCATION).toURI()));
        Iterator<Map.Entry<String, String>> iterator = rules.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            ruleWriter.write(next.getKey().toLowerCase() + "=" + next.getValue().toLowerCase());
            if (iterator.hasNext())
                ruleWriter.write(", ");
        }
        ruleWriter.flush();
        ruleWriter.close();
    }

    static TranslationRuleService getInstance() {
        return SingletonHelper.instance;
    }

    private TranslationRuleService() {}

    private static class SingletonHelper {
        private static final TranslationRuleService instance = new TranslationRuleService();
    }
}
