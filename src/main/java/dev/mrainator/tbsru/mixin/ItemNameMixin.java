package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.LibraryRu;
import dev.mrainator.tbsru.RuBooks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemNameMixin {
    @Inject(method = "getHoverName()Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
    private void tbsru$hoverName(CallbackInfoReturnable<Component> cir) {
        if (!LibraryRu.russian()) return;
        Component name = cir.getReturnValue();
        if (name == null) return;
        String translated = RuBooks.title(name.getString());
        if (translated == null) return;
        cir.setReturnValue(Component.literal(translated).withStyle(name.getStyle()));
    }
}
