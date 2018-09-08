package com.egrasoft.ss.translater.throwable;

import java.util.Map;

public class RuleParseException extends IllegalArgumentException {
    private Map.Entry<String, String> rule;

    public RuleParseException(String message, Map.Entry<String, String> rule) {
        super(message);
        this.rule = rule;
    }

    public Map.Entry<String, String> getRule() {
        return rule;
    }
}
