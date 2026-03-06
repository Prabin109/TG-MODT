package com.tgmotd.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFormatter {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static Component format(String text) {
        return format(text, "MINIMESSAGE");
    }

    public static Component format(String text, String formatter) {
        if ("LEGACY".equalsIgnoreCase(formatter)) {
            // Convert hex colors to MiniMessage format
            text = convertHexToMiniMessage(text);
            return MINI_MESSAGE.deserialize(text);
        }
        return MINI_MESSAGE.deserialize(text);
    }

    private static String convertHexToMiniMessage(String text) {
        // Convert &#RRGGBB to <color:#RRGGBB>
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(result, "<color:#" + hex + ">");
        }
        matcher.appendTail(result);
        
        // Convert legacy codes to MiniMessage
        text = result.toString();
        text = text.replace("&l", "<bold>")
                   .replace("&m", "<strikethrough>")
                   .replace("&n", "<underlined>")
                   .replace("&o", "<italic>")
                   .replace("&r", "<reset>")
                   .replace("&0", "<black>")
                   .replace("&1", "<dark_blue>")
                   .replace("&2", "<dark_green>")
                   .replace("&3", "<dark_aqua>")
                   .replace("&4", "<dark_red>")
                   .replace("&5", "<dark_purple>")
                   .replace("&6", "<gold>")
                   .replace("&7", "<gray>")
                   .replace("&8", "<dark_gray>")
                   .replace("&9", "<blue>")
                   .replace("&a", "<green>")
                   .replace("&b", "<aqua>")
                   .replace("&c", "<red>")
                   .replace("&d", "<light_purple>")
                   .replace("&e", "<yellow>")
                   .replace("&f", "<white>");
        
        return text;
    }

    public static String replacePlaceholders(String text, int online, int max) {
        return text.replace("{online}", String.valueOf(online))
                   .replace("{max}", String.valueOf(max));
    }

    public static String loadFavicon(File iconFile) throws IOException {
        if (!iconFile.exists()) {
            return null;
        }

        BufferedImage image = ImageIO.read(iconFile);
        if (image == null) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", output);
        byte[] imageBytes = output.toByteArray();
        
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }
}

