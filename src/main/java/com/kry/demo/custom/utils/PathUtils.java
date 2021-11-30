package com.kry.demo.custom.utils;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/30
 * **********学海无涯苦作舟**********
 */
public class PathUtils {

    private static final String separator = ".";

    private static final String win = "\\";
    private static final String win_sys = "windows";

    private static final String mac = "/";
    private static final String mac_sys = "mac";

    /**
     * . to / or \\
     */
    public static final String forward = "forward";

    /**
     * / or \\ to .
     */
    public static final String reverse = "reverse";

    public static String replaceUrlPath(String val) {
        return val.replace(separator, mac);
    }

    public static String replace(String val, String direction) {
        String os = System.getProperty("os.name");
        String format;
        if (os.toLowerCase().startsWith(win_sys)) {
            format = win;
        } else {
            format = mac;
        }
        return forward.equals(direction) ? val.replace(separator, format) : val.replace(format, separator);
    }
}
