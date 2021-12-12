package ru.itmo.sd.graph.io;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ParametersData {

    public static ParametersData parse(String... tokens) {
        return new ParametersData(tokens);
    }

    static final Map<String, Parameter> PARAM_BY_KEY = new HashMap<>();

    static {
        Parameter.values();
    }

    final Map<String, String> VALUES = new HashMap<>();

    private ParametersData(String... tokens) {
        this.initByTokens(Arrays.asList(tokens));
    }

    private void initByTokens(List<String> tokens) {
        StringJoiner sj = new StringJoiner(" ");
        Parameter current = null;

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.charAt(0) == '-') {
                if (!PARAM_BY_KEY.containsKey(token) && i + 1 < tokens.size()) {
                    do {
                        token = tokens.get(i + 1);
                        if (token.charAt(0) == '-') {
                            break;
                        }
                        i += 1;
                    } while (i + 1 < tokens.size());
                    continue;
                }
                if (current != null && sj.length() > 0) {
                    VALUES.put(current.KEY, sj.toString());
                }
                current = PARAM_BY_KEY.get(token);
                sj = new StringJoiner(" ");
            } else {
                sj.add(token);
            }
        }

        if (current != null && sj.length() > 0) {
            VALUES.put(current.KEY, sj.toString());
        }
    }

    @Override
    public String toString() {
        return VALUES.toString();
    }

    public String getValue(Parameter parameter) {
        return parameter == null ? "" : parameter.get(this);
    }

}
