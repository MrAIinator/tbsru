package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.RuLines;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignText.class)
public class SignTextMixin {
    @Inject(method = "getMessage(IZ)Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
    private void tbsru$sign(int line, boolean filtered, CallbackInfoReturnable<Component> cir) {
        if (!russian()) return;
        Component next = RuLines.translate(cir.getReturnValue());
        if (next != null) cir.setReturnValue(next);
    }

    private static boolean russian() {
        return "ru_ru".equals(Minecraft.getInstance().getLanguageManager().getSelected());
    }
}
