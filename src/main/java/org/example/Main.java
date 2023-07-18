package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Hello world!");
            new DemoWebDriver("Chrome", "/Users/arthuryueh/Downloads/chromedriver_mac_arm64/chromedriver").open_website("https://www.google.com/");
            Thread.sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}