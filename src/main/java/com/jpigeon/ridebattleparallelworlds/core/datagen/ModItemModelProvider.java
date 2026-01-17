package com.jpigeon.ridebattleparallelworlds.core.datagen;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
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
        // 空我
    }


}