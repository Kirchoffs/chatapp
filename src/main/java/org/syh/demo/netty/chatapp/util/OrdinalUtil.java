package org.syh.demo.netty.chatapp.util;

public class OrdinalUtil {
    public static String getOrdinal(int cardinal) {
        String suffix = "th";

        switch(cardinal) {
            case 1:
                suffix = "st";
                break;
            case 2:
                suffix = "nd";
                break;
            case 3:
                suffix = "rd";
                break;
            default:
                suffix = "th";
        }

        return String.format("%d%s", cardinal, suffix);
    }
}
