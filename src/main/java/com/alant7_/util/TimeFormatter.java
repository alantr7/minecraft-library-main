package com.alant7_.util;

public class TimeFormatter {

    private int seconds;
    private int minutes;
    private int hours;
    private int days;

    public TimeFormatter(int d, int h, int m, int s) {
        days = d;
        hours = h;
        minutes = m;
        seconds = s;
    }

    public static TimeFormatter fromMillis(long ms) {

        long seconds = ms / 1000;
        long minutes = seconds / 60;
        seconds -= 60 * minutes;

        long hours = minutes / 60;
        minutes -= 60 * hours;

        long days = hours / 24;
        hours -= 24 * days;

        return new TimeFormatter((int)days, (int)hours, (int)minutes, (int)seconds);

    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getDays() {
        return days;
    }

    public int getTotalSeconds() {
        return seconds + getTotalMinutes() * 60;
    }

    public int getTotalMinutes() {
        return minutes + 60 * getTotalHours();
    }

    public int getTotalHours() {
        return hours + days * 24;
    }

    public String format(String format) {

        return format
                .replace("{D}", formatNumber(1, getDays()))
                .replace("{DD}", formatNumber(2, getDays()))
                .replace("{H}", formatNumber(1, getHours()))
                .replace("{HH}", formatNumber(2, getHours()))
                .replace("{M}", formatNumber(1, getMinutes()))
                .replace("{MM}", formatNumber(2, getMinutes()))
                .replace("{S}", formatNumber(1, getSeconds()))
                .replace("{SS}", formatNumber(2, getSeconds()));

    }

    private String formatNumber(int digits, int number) {
        StringBuilder formatted = new StringBuilder(String.valueOf(number));
        while (formatted.length() < digits)
            formatted.insert(0, "0");

        return formatted.toString();
    }

}
