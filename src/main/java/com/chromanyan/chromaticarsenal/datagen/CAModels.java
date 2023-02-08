package com.chromanyan.chromaticarsenal.datagen;

import com.chromanyan.chromaticarsenal.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CAModels extends ItemModelProvider {

    public CAModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    public void basicModel(String name) {
        this.singleTexture(name,
                mcLoc("item/generated"),
                "layer0",
                modLoc("items/" + name));
    }

    public void basicModelWithSuper(String name) {
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
        basicModel("shadow_treads");
        basicModel("duality_rings");
        basicModel("friendly_fire_flower");
        basicModel("lunar_crystal");
        basicModel("harpy_feather");

        basicModel("magic_garlic_bread");
        basicModel("cosmicola");
    }

}
