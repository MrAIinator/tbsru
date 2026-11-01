package dev.mrainator.tbsru;

import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;

/** Rewrites hardcoded English chat lines once they reach the client. */
public final class RuChat {
    private static final Map<String, String> LINES = Map.ofEntries(
            Map.entry("This command must be executed by a player!", "Эту команду может ввести только игрок."),
            Map.entry("Reputation value:", "Значение:"),
            Map.entry("Reputation:", "Репутация:"),
            Map.entry(" (hover for more information)", " (наведи, там подробнее)"),
            Map.entry("BAD", "ПЛОХАЯ"),
            Map.entry("NORMAL", "НОРМА"),
            Map.entry("GOOD", "ХОРОШАЯ"),
            Map.entry(
                    "Null is pissed and his hatred for you is at maximum. Expect hell from null. Null will now more often attack the player.",
                    "Null в ярости, ненависть к тебе на пределе. Жди от него ада. Теперь он нападает чаще."),
            Map.entry(
                    "Null and you are neutral between each other. Null can sometimes hurt the player.",
                    "Вы с Null друг другу нейтральны. Иногда он всё же может ударить."),
            Map.entry(
                    "Null is cool and doesn't care about you being here. Null will rarely hurt the player.",
                    "Null спокоен и ему плевать, что ты здесь. Он почти тебя не трогает."));

    private RuChat() {
    }

    public static Component translate(Component message) {
        if (message == null || !LibraryRu.russian()) return message;
        String all = message.getString();
        if (!all.contains("Reputation") && !all.contains("Null is") && !all.contains("Null and you")
                && !all.contains("hover for more") && !all.contains("must be executed by a player")) {
            return message;
        }
        return rewrite(message);
    }

    private static Component rewrite(Component in) {
        String raw = in.getContents() instanceof PlainTextContents plain ? plain.text() : "";
        String mapped = LINES.getOrDefault(raw, raw);
        MutableComponent out = Component.literal(mapped).setStyle(restyle(in.getStyle()));
        for (Component sibling : in.getSiblings()) out.append(rewrite(sibling));
        return out;
    }

    private static Style restyle(Style style) {
        if (style == null) return Style.EMPTY;
        HoverEvent hover = style.getHoverEvent();
        if (hover == null || hover.getAction() != HoverEvent.Action.SHOW_TEXT) return style;
        Component shown = hover.getValue(HoverEvent.Action.SHOW_TEXT);
        if (shown == null) return style;
        return style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, rewrite(shown)));
    }
}
