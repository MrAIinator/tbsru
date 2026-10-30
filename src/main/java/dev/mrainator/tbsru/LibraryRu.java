package dev.mrainator.tbsru;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

/** Russian pages for the library index. Shown only when the game language is ru_ru. */
public final class LibraryRu {
    private static boolean loaded;
    private static final Map<Integer, List<String>> books = new HashMap<>();
    private static Map<String, String> overlays = Map.of();

    private LibraryRu() {
    }

    public static boolean russian() {
        try {
            return "ru_ru".equals(Minecraft.getInstance().getLanguageManager().getSelected());
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static String page(int bookId, int page) {
        if (!russian()) return null;
        load();
        List<String> pages = books.get(bookId);
        if (pages == null || page < 1 || page > pages.size()) return null;
        return pages.get(page - 1);
    }

    public static String overlay(String raw) {
        if (raw == null || !russian()) return raw;
        load();
        String hit = overlays.get(raw);
        return hit != null ? hit : raw;
    }

    private static void load() {
        if (loaded) return;
        loaded = true;
        for (int i = 1; i <= 45; i++) {
            try (InputStream in = LibraryRu.class.getResourceAsStream("/assets/tbsru/library/" + i + ".json")) {
                if (in == null) continue;
                JsonObject obj = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
                if (!obj.has("pages") || !obj.get("pages").isJsonArray()) continue;
                List<String> pages = new ArrayList<>();
                for (JsonElement el : obj.getAsJsonArray("pages")) {
                    pages.add(el.isJsonPrimitive() ? el.getAsString() : "");
                }
                books.put(i, List.copyOf(pages));
            } catch (Exception ignored) {
            }
        }
        try (InputStream in = LibraryRu.class.getResourceAsStream("/assets/tbsru/library/overlays.json")) {
            if (in == null) return;
            JsonObject obj = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            Map<String, String> map = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                if (entry.getValue().isJsonPrimitive()) map.put(entry.getKey(), entry.getValue().getAsString());
            }
            overlays = map;
        } catch (Exception ignored) {
        }
    }
}
