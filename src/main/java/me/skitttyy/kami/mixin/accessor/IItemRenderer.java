package me.skitttyy.kami.mixin.accessor;

import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

// renderBakedItemModel and builtinModelItemRenderer were removed in 1.21.4
// IItemRenderer is kept as an empty stub so existing references compile
@Mixin(ItemRenderer.class)
public interface IItemRenderer
{
}
