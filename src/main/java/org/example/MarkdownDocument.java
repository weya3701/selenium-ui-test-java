package org.example;

import lombok.Getter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkdownDocument {

    @Getter
    private List<String> stringList;


    public MarkdownDocument() {
        stringList = new ArrayList<>();
    }

    public void setContent(String... content) {
        String tmp = "";
        for (String s: content) {
            tmp += s;
        }
        tmp += "\n";
        this.stringList.add(tmp);
    }

    public String getMarkdownContent() {
        String tmpString = "";
        for (String s: this.stringList) {
            tmpString += s;
        }
        return tmpString;
    }
}
