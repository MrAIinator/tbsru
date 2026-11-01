package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.LibraryRu;
import java.lang.reflect.Field;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The library screen is Kotlin. Shadowing its fields made the whole mixin
 * fail, so the English page stayed and only the floating words changed.
 * Fields are read by reflection, and the text is swapped at the draw call.
 */
@Mixin(targets = "net.thebrokenscript.client.gui.LibraryBookScreen", remap = false)
public class LibraryBookMixin {
    private static final Field BOOK_ID = field("bookId");
    private static final Field PAGE = field("currentPage");
    private static final Field HOME = field("homeButton");

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawWordWrap(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/FormattedText;IIII)V"),
            index = 1)
    private FormattedText tbsru$drawn(FormattedText text) {
        String ru = LibraryRu.page(number(BOOK_ID), number(PAGE) + 1);
        return ru == null ? text : Component.literal(ru);
    }

    @Inject(method = "createButtons", at = @At("RETURN"))
    private void tbsru$index(CallbackInfo ci) {
        if (!LibraryRu.russian() || HOME == null) return;
        try {
            Object button = HOME.get(this);
            if (button instanceof Button home) {
                home.setMessage(Component.literal("Индекс"));
                home.setX(home.getX() - 16);
                home.setWidth(home.getWidth() + 16);
            }
        } catch (IllegalAccessException ignored) {
        }
    }

    private int number(Field field) {
        if (field == null) return -1;
        try {
            return field.getInt(this);
        } catch (IllegalAccessException ignored) {
            return -1;
        }
    }

    private static Field field(String name) {
        try {
            Field field = Class.forName("net.thebrokenscript.client.gui.LibraryBookScreen").getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception ignored) {
            return null;
        }
    }
}
