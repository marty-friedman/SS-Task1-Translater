package com.egrasoft.ss.translater.throwable;

import java.util.Map;

public class RuleParseException extends IllegalArgumentException {
    private Map.Entry<String, String> rule;

    public RuleParseException(String message, Map.Entry<String, String> rule) {
        super(message);
        this.rule = rule;
    }

    public String getRuleDescription() {
        String key = rule.getKey();
        String value = rule.getValue();
        if (rule.getKey() == null || rule.getKey().equals(""))
            key = "<empty>";
        if (rule.getValue() == null || rule.getValue().equals(""))
            value = "<empty>";
        return key + " : " + value;
    }
}
