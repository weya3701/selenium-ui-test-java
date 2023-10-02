package org.example;

import lombok.Data;

@Data
public class Step {
    String elementName;
    String desc;
    Integer interval;
    String url;
    String storeKey;
    String by;
    String pattern;
    String result;
    Integer tab;
    String frame;
    String key;
    String fileName;
    String module;
    String tabName;

    public void replaceSymbol() {
        this.elementName = this.elementName.replace("&amp;lt", "<");
        this.elementName = this.elementName.replace("&amp;gt;", ">");
        this.elementName = this.elementName.replace("&amp;quot;", "\"");
    }
}
