package com.jpigeon.ridebattleparallelworlds.datagen;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.item.ModItems;
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
        basicItem(ModItems.KUUGA_HELMET.get());
        basicItem(ModItems.KUUGA_CHESTPLATE.get());
        basicItem(ModItems.KUUGA_BOOTS.get());
        basicItem(ModItems.ARCLE.get());
        basicItem(ModItems.CHOGODAI_ELEMENT.get());
        basicItem(ModItems.MIGHTY_ELEMENT.get());
    }
}