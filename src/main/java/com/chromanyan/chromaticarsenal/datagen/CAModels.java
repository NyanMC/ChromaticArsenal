package com.chromanyan.chromaticarsenal.datagen;

import com.chromanyan.chromaticarsenal.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CAModels extends ItemModelProvider {

    public CAModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    private void basicModel(String name) {
        this.singleTexture(name,
                mcLoc("item/generated"),
                "layer0",
                modLoc("items/" + name));
    }

    private void basicModelWithSuper(String name) {
        basicModel(name);
        basicModel("super_" + name);
    }

    public void registerModels() {
        basicModel("chroma_shard");
        basicModel("ascension_essence");
        basicModel("spicy_coal");
        basicModel("magmatic_scrap");

        basicModelWithSuper("golden_heart");
        basicModelWithSuper("glass_shield");
        basicModelWithSuper("ward_crystal");
        basicModelWithSuper("shadow_treads");
        basicModel("duality_rings");
        basicModel("friendly_fire_flower");
        basicModelWithSuper("lunar_crystal");
        basicModel("cryo_ring");
        basicModel("harpy_feather");

        basicModel("super_glow_ring");

        basicModel("ascended_star");
        basicModel("world_anchor");

        basicModel("magic_garlic_bread");
        basicModel("cosmicola");
        basicModel("chroma_salvager");
        basicModel("viewer_item");
    }

}
