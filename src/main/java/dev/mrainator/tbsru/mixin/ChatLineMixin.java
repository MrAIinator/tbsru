package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.RuChat;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(net.minecraft.client.gui.components.ChatComponent.class)
public class ChatLineMixin {
    @ModifyVariable(method = "addMessage(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), argsOnly = true)
    private Component tbsru$line(Component message) {
        return RuChat.translate(message);
    }
}
