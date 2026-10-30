package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.LibraryRu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.thebrokenscript.client.gui.LibraryBookScreen", remap = false)
public class LibraryBookMixin {
    @Shadow
    private int bookId;

    @Shadow
    private Button homeButton;

    @Inject(method = "loadPage", at = @At("RETURN"), cancellable = true)
    private void tbsru$page(Object bookData, int page, CallbackInfoReturnable<Component> cir) {
        String ru = LibraryRu.page(bookId, page);
        if (ru == null) return;
        cir.setReturnValue(Component.literal(ru));
    }

    @Inject(method = "createButtons", at = @At("RETURN"))
    private void tbsru$index(CallbackInfo ci) {
        if (!LibraryRu.russian() || this.homeButton == null) return;
        this.homeButton.setMessage(Component.literal("Индекс"));
        this.homeButton.setX(this.homeButton.getX() - 16);
        this.homeButton.setWidth(this.homeButton.getWidth() + 16);
    }
}
