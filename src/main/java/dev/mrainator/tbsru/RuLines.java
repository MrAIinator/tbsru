package dev.mrainator.tbsru;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.chat.Component;

/** Sign lines. A whole sign is translated together, so the sentence stays in order. */
public final class RuLines {
    private static Map<String, String> map = Map.of();
    private static Map<String, String[]> signs = Map.of();
    private static boolean loaded;

    private RuLines() {
    }

    public static String signLine(String[] lines, int index) {
        load();
        if (lines == null || index < 0 || index >= lines.length || signs.isEmpty()) return null;
        String[] hit = signs.get(key(lines, false));
        if (hit == null) hit = signs.get(key(lines, true));
        if (hit == null || index >= hit.length) return null;
        return hit[index];
    }

    private static String key(String[] lines, boolean trim) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) sb.append('\n');
            String line = lines[i] == null ? "" : lines[i];
            sb.append(trim ? line.trim() : line);
        }
        return sb.toString();
    }

    public static Component translate(Component component) {
        if (component == null) return null;
        String next = translateText(component.getString());
        if (next == null) return null;
        return Component.literal(next).withStyle(component.getStyle());
    }

    public static String translateText(String raw) {
        load();
        if (raw == null || raw.isEmpty() || map.isEmpty()) return null;
        String hit = map.get(raw);
        if (hit != null) return hit;
        String trimmed = raw.trim();
        if (!trimmed.equals(raw)) {
            hit = map.get(trimmed);
            if (hit != null) return hit;
        }
        if (raw.indexOf('\n') < 0) return null;
        String[] lines = raw.split("\n", -1);
        boolean any = false;
        for (int i = 0; i < lines.length; i++) {
            String line = map.get(lines[i]);
            if (line == null && !lines[i].equals(lines[i].trim())) line = map.get(lines[i].trim());
            if (line != null) {
                lines[i] = line;
                any = true;
            }
        }
        return any ? String.join("\n", lines) : null;
    }

    private static void load() {
        if (loaded) return;
        loaded = true;
        map = readFlat("/assets/tbsru/lines.json");
        signs = readSigns("/assets/tbsru/signs.json");
    }

    private static Map<String, String> readFlat(String path) {
        Map<String, String> parsed = new HashMap<>();
        try (InputStream in = RuLines.class.getResourceAsStream(path)) {
            if (in == null) return Map.of();
            JsonObject obj = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                if (entry.getValue().isJsonPrimitive()) parsed.put(entry.getKey(), entry.getValue().getAsString());
            }
        } catch (Exception ignored) {
            return Map.of();
        }
        return parsed;
    }

    private static Map<String, String[]> readSigns(String path) {
        Map<String, String[]> parsed = new HashMap<>();
        try (InputStream in = RuLines.class.getResourceAsStream(path)) {
            if (in == null) return Map.of();
            JsonObject obj = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                if (!entry.getValue().isJsonArray()) continue;
                JsonArray array = entry.getValue().getAsJsonArray();
                String[] lines = new String[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    lines[i] = array.get(i).isJsonPrimitive() ? array.get(i).getAsString() : "";
                }
                parsed.put(entry.getKey(), lines);
            }
        } catch (Exception ignored) {
            return Map.of();
        }
        return parsed;
    }
}
