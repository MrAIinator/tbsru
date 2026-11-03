package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.RuBooks;
import dev.mrainator.tbsru.RuLines;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.WrittenBookContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WrittenBookContent.class)
public class BookPageMixin {
    @Inject(method = "getPages(Z)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void tbsru$pages(boolean filtered, CallbackInfoReturnable<List<Component>> cir) {
        if (!"ru_ru".equals(Minecraft.getInstance().getLanguageManager().getSelected())) return;
        List<Component> pages = cir.getReturnValue();
        if (pages == null || pages.isEmpty()) return;
        boolean any = false;
        List<Component> next = new ArrayList<>(pages.size());
        for (Component page : pages) {
            String whole = page == null ? null : RuBooks.page(page.getString());
            if (whole != null) {
                next.add(Component.literal(whole).withStyle(page.getStyle()));
                any = true;
                continue;
            }
            Component translated = RuLines.translate(page);
            if (translated != null) {
                next.add(translated);
                any = true;
            } else {
                next.add(page);
            }
        }
        if (any) cir.setReturnValue(next);
    }
}
