package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.LibraryRu;
import dev.mrainator.tbsru.RuBooks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityNameMixin {
    @Inject(method = "getName()Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
    private void tbsru$entityName(CallbackInfoReturnable<Component> cir) {
        if (!LibraryRu.russian()) return;
        Component name = cir.getReturnValue();
        if (name == null) return;
        String translated = RuBooks.title(name.getString());
        if (translated == null) return;
        cir.setReturnValue(Component.literal(translated).withStyle(name.getStyle()));
    }
}
