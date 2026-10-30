package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.LibraryRu;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.thebrokenscript.client.TBSLibraryAnimations", remap = false)
public class LibraryAnimMixin {
    @ModifyVariable(
            method = "jitteryText(Lnet/minecraft/client/gui/GuiGraphics;Ljava/lang/String;Lnet/minecraft/client/gui/Font;IIFLnet/thebrokenscript/client/util/LibraryTextUtils;)V",
            at = @At("HEAD"),
            argsOnly = true)
    private String tbsru$jitter(String text) {
        return LibraryRu.overlay(text);
    }

    @ModifyVariable(method = "simpleText", at = @At("HEAD"), argsOnly = true)
    private String tbsru$simple(String text) {
        return LibraryRu.overlay(text);
    }

    @ModifyVariable(method = {"heartText", "lungText"}, at = @At("HEAD"), argsOnly = true)
    private String tbsru$organ(String text) {
        return LibraryRu.overlay(text);
    }

    @ModifyVariable(method = "clockText", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String tbsru$clock0(String text) {
        return LibraryRu.overlay(text);
    }

    @ModifyVariable(method = "clockText", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private String tbsru$clock1(String text) {
        return LibraryRu.overlay(text);
    }

    @ModifyVariable(method = "clockText", at = @At("HEAD"), argsOnly = true, ordinal = 2)
    private String tbsru$clock2(String text) {
        return LibraryRu.overlay(text);
    }

    @ModifyVariable(method = "simpleTextStack", at = @At("HEAD"), argsOnly = true)
    private List<String> tbsru$stack(List<String> lines) {
        if (lines == null || !LibraryRu.russian()) return lines;
        List<String> next = new ArrayList<>(lines.size());
        for (String line : lines) next.add(LibraryRu.overlay(line));
        return next;
    }
}
