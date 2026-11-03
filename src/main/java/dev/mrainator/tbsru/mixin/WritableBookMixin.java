package dev.mrainator.tbsru.mixin;

import dev.mrainator.tbsru.LibraryRu;
import dev.mrainator.tbsru.RuBooks;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.world.item.component.WritableBookContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WritableBookContent.class)
public class WritableBookMixin {
    @Inject(method = "getPages(Z)Ljava/util/stream/Stream;", at = @At("RETURN"), cancellable = true)
    private void tbsru$writable(boolean filtered, CallbackInfoReturnable<Stream<String>> cir) {
        if (!LibraryRu.russian()) return;
        Stream<String> pages = cir.getReturnValue();
        if (pages == null) return;
        List<String> source = pages.toList();
        List<String> next = new ArrayList<>(source.size());
        boolean any = false;
        for (String page : source) {
            String translated = RuBooks.page(page);
            if (translated != null) {
                next.add(translated);
                any = true;
            } else {
                next.add(page);
            }
        }
        if (any) cir.setReturnValue(next.stream());
        else cir.setReturnValue(source.stream());
    }
}
