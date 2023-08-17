package org.example;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MarkdownDocument {

    @Getter
    private List<String> stringList;


    public MarkdownDocument() {
        stringList = new ArrayList<>();
    }

    public void setContent(String... content) {
        StringBuilder tmp = new StringBuilder();
        for (String s: content) {
            tmp.append(s);
        }
        tmp.append("\n");
        this.stringList.add(tmp.toString());
    }

    public String getMarkdownContent() {
        String tmpString = "";
        for (String s: this.stringList) {
            tmpString += s;
        }
        return tmpString;
    }
}
