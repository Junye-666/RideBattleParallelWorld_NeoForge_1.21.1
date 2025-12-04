package com.jpigeon.ridebattleparallelworlds.core.datagen;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RideBattleParallelWorlds.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // basicItem(ModItems.WORLDS_FRAGMENT.get());
        basicItem(ModItems.MIGHTY_HELMET.get());
        basicItem(ModItems.MIGHTY_CHESTPLATE.get());
        basicItem(ModItems.MIGHTY_BOOTS.get());
        basicItem(ModItems.DRAGON_HELMET.get());
        basicItem(ModItems.DRAGON_CHESTPLATE.get());
        basicItem(ModItems.DRAGON_BOOTS.get());
        basicItem(ModItems.ARCLE.get());
        basicItem(ModItems.CHOGODAI_ELEMENT.get());
        basicItem(ModItems.MIGHTY_ELEMENT.get());
        basicItem(ModItems.DRAGON_ELEMENT.get());
    }
}