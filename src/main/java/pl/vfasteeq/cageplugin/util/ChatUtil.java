package pl.vfasteeq.cageplugin.util;

/**
 * @author vFasteeQ
 * @since 24.11.2022
 */

public class ChatUtil {
    public static String fixColor(String message) {
        return message.replace("&", "§")
                .replace(">>", "»");
    }
}
