package org.develnext.jphp.genapi;

import org.develnext.jphp.core.tokenizer.GrammarUtils;

import java.util.*;

public class DocAnnotations {
    protected final String text;
    protected String description;
    protected Map<String, Parameter> parameters;

    public DocAnnotations(String docComment) {
        this.text = docComment;
        parameters = new LinkedHashMap<String, Parameter>();
        parse();
    }

    protected int parseParameter(String source, int offset) {
        String name = null;
        int valueOffset = 0;
        for(int i = offset; i < source.length() + 1; i++) {
            char ch = i < source.length() ? source.charAt(i) : '\0';
            char prev_ch = i < 1 ? '\0' : source.charAt(i - 1);

            if (name == null && GrammarUtils.isSpace(ch)) {
                name = source.substring(offset, i);
                valueOffset = i + 1;
            }
            if (ch == '\0' || (ch == '@' && GrammarUtils.isNewline(prev_ch))) {
                if (name != null) {
                    String value = source.substring(valueOffset, i);
                    Parameter parameter = parameters.get(name.toLowerCase());
                    if (parameter == null)
                        parameters.put(name.toLowerCase(), parameter = new Parameter());

                    parameter.addValue(value);
                }
                return i - 1;
            }
        }

        return source.length() - 1;
    }

    protected void parse(){
        int len = text.length();
        for(int i = 0; i < len; i++) {
            char ch = text.charAt(i);
            char prev_ch = i < 1 ? '\0' : text.charAt(i - 1);

            if (ch == '@' && (GrammarUtils.isNewline(prev_ch) || i == 0)) {
                if (description == null) {
                    description = i == 0 ? "" : text.substring(0, i - 1);
                }
                i = parseParameter(text, i + 1);
            }
        }
    }

    public String getDescription() {
        return description;
    }

    public boolean hasParameter(String name) {
        return parameters.containsKey(name.toLowerCase());
    }

    public Parameter getParameter(String name) {
        return parameters.get(name.toLowerCase());
    }

    public class Parameter {
        protected List<String> values;

        public Parameter() {
            values = new ArrayList<String>();
        }

        public String addValue(String value) {
            values.add(value);
            return value;
        }

        public Collection<String> values() {
            return values;
        }

        public String value() {
            return values.get(0);
        }
    }
}
