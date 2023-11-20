package com.chromanyan.chromaticarsenal.client.renderer;

import com.chromanyan.chromaticarsenal.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@OnlyIn(Dist.CLIENT)
public class CuriosRenderers {
    public static void register() {
        CuriosRendererRegistry.register(ModItems.BLAHAJ.get(), () -> new BlahajRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(BlahajRenderer.LAYER)));
    }

    public static void onLayerRegister(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BlahajRenderer.LAYER, () -> LayerDefinition.create(BlahajRenderer.mesh(), 1, 1));
    }
}
