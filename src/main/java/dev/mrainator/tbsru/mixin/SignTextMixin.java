package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.RuLines;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignText.class)
public class SignTextMixin {
    @Shadow
    @Final
    private Component[] messages;

    @Shadow
    @Final
    private Component[] filteredMessages;

    @Inject(method = "getMessage(IZ)Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
    private void tbsru$sign(int line, boolean filtered, CallbackInfoReturnable<Component> cir) {
        if (!russian()) return;
        Component[] src = filtered ? this.filteredMessages : this.messages;
        if (src != null && line >= 0 && line < src.length) {
            String[] lines = new String[src.length];
            for (int i = 0; i < src.length; i++) {
                lines[i] = src[i] == null ? "" : src[i].getString();
            }
            String grouped = RuLines.signLine(lines, line);
            if (grouped != null) {
                Component current = cir.getReturnValue();
                cir.setReturnValue(current == null
                        ? Component.literal(grouped)
                        : Component.literal(grouped).withStyle(current.getStyle()));
                return;
            }
        }
        Component next = RuLines.translate(cir.getReturnValue());
        if (next != null) cir.setReturnValue(next);
    }

    private static boolean russian() {
        return "ru_ru".equals(Minecraft.getInstance().getLanguageManager().getSelected());
    }
}
