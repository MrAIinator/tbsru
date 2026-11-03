package dev.mrainator.tbsru;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/** World books and renamed items. Russian only when the game language is ru_ru. */
public final class RuBooks {
    private static boolean loaded;
    private static Map<String, String> titles = Map.of();
    private static Map<String, String> pages = Map.of();

    private RuBooks() {
    }

    public static String title(String raw) {
        if (!active() || raw == null || raw.isEmpty()) return null;
        load();
        String hit = titles.get(raw);
        if (hit != null) return hit;
        String trimmed = raw.trim();
        return trimmed.equals(raw) ? null : titles.get(trimmed);
    }

    public static String page(String raw) {
        if (!active() || raw == null || raw.isEmpty()) return null;
        load();
        String hit = pages.get(raw);
        if (hit != null) return hit;
        if (raw.indexOf('\r') >= 0) {
            hit = pages.get(raw.replace("\r\n", "\n").replace('\r', '\n'));
            if (hit != null) return hit;
        }
        String trimmed = raw.trim();
        return trimmed.equals(raw) ? null : pages.get(trimmed);
    }

    private static boolean active() {
        return LibraryRu.russian();
    }

    private static void load() {
        if (loaded) return;
        loaded = true;
        try (InputStream in = RuBooks.class.getResourceAsStream("/assets/tbsru/worldbooks.json")) {
            if (in == null) return;
            JsonObject obj = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            titles = readMap(obj.get("titles"));
            pages = readMap(obj.get("pages"));
        } catch (Exception ignored) {
            titles = Map.of();
            pages = Map.of();
        }
    }

    private static Map<String, String> readMap(JsonElement element) {
        Map<String, String> parsed = new HashMap<>();
        if (element == null || !element.isJsonObject()) return parsed;
        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            if (entry.getValue().isJsonPrimitive()) parsed.put(entry.getKey(), entry.getValue().getAsString());
        }
        return parsed;
    }
}
