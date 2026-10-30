package dev.mrainator.tbsru;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.chat.Component;

/** Exact sign and book lines. Names and glitch tokens are left as written in the json. */
public final class RuLines {
    private static Map<String, String> map = Map.of();
    private static boolean loaded;

    private RuLines() {
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
        Map<String, String> parsed = new HashMap<>();
        try (InputStream in = RuLines.class.getResourceAsStream("/assets/tbsru/lines.json")) {
            if (in == null) {
                map = Map.of();
                return;
            }
            JsonObject obj = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                if (entry.getValue().isJsonPrimitive()) parsed.put(entry.getKey(), entry.getValue().getAsString());
            }
        } catch (Exception ignored) {
            parsed.clear();
        }
        map = parsed;
    }
}
