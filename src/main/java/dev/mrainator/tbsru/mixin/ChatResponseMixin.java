package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.RuPhrases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.thebrokenscript.brokencore.api.responses.ChatResponse")
public class ChatResponseMixin {
    @ModifyVariable(method = "check", at = @At("HEAD"), argsOnly = true)
    private String tbsru$alias(String message) {
        return RuPhrases.toEnglish(message);
    }
}
