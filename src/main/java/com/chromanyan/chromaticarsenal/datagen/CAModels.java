package com.chromanyan.chromaticarsenal.datagen;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CAModels extends ItemModelProvider {

    public CAModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ChromaticArsenal.MODID, existingFileHelper);
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
        // crafting materials
        basicModel("chroma_shard");
        basicModel("ascension_essence");
        basicModel("spicy_coal");
        basicModel("magmatic_scrap");
        basicModel("champion_catalyst");

        // regular chromatic curios
        basicModelWithSuper("glass_shield");
        basicModelWithSuper("ward_crystal");
        basicModelWithSuper("shadow_treads");
        basicModel("duality_rings");
        basicModelWithSuper("friendly_fire_flower");
        basicModelWithSuper("lunar_crystal");
        basicModel("cryo_ring");
        basicModel("bubble_amulet");
        basicModel("momentum_stone");
        basicModel("advancing_heart");

        // super curios
        basicModel("super_golden_heart"); // can't datagen the regular golden heart because it does special stuff

        // challenge curios
        basicModel("ascended_star");
        basicModel("world_anchor");
        basicModel("cursed_totem");

        // utility curios
        basicModel("gravity_stone");
        basicModel("vertical_stasis_stone");
        basicModel("anonymity_umbrella");

        // basic curios
        basicModel("amethyst_ring");

        // other stuff
        basicModel("magic_garlic_bread");
        basicModel("cosmicola");
        basicModel("chroma_salvager");
        basicModel("viewer_item");

        // miscellaneous
        basicModel("golden_heart_nyans");
        basicModel("super_glow_ring_active");
        basicModel("ca_book");
        basicModel("lore_book");
    }

}
