package com.jpigeon.ridebattleparallelworlds.core.datagen;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RideBattleParallelWorlds.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // basicItem(ModItems.WORLDS_FRAGMENT.get());
        // 空我
        basicItem(ModItems.CHOGODAI_ELEMENT.get());
        basicItem(ModItems.MIGHTY_ELEMENT.get());
        basicItem(ModItems.DRAGON_ELEMENT.get());
        basicItem(ModItems.PEGASUS_ELEMENT.get());
        basicItem(ModItems.TITAN_ELEMENT.get());
        basicItem(ModItems.RISING_MIGHTY_ELEMENT.get());
        basicItem(ModItems.RISING_DRAGON_ELEMENT.get());
        basicItem(ModItems.RISING_PEGASUS_ELEMENT.get());
        basicItem(ModItems.RISING_TITAN_ELEMENT.get());
        basicItem(ModItems.AMAZING_MIGHTY_ELEMENT.get());
        basicItem(ModItems.ULTIMATE_ELEMENT.get());

        // 盔甲
        basicItem(ModItems.GROWING_HELMET.get());
        basicItem(ModItems.GROWING_CHESTPLATE.get());
        basicItem(ModItems.GROWING_BOOTS.get());
        basicItem(ModItems.MIGHTY_HELMET.get());
        basicItem(ModItems.MIGHTY_CHESTPLATE.get());
        basicItem(ModItems.MIGHTY_BOOTS.get());
        basicItem(ModItems.DRAGON_HELMET.get());
        basicItem(ModItems.DRAGON_CHESTPLATE.get());
        basicItem(ModItems.DRAGON_BOOTS.get());
        basicItem(ModItems.PEGASUS_HELMET.get());
        basicItem(ModItems.PEGASUS_CHESTPLATE.get());
        basicItem(ModItems.PEGASUS_BOOTS.get());
        basicItem(ModItems.TITAN_HELMET.get());
        basicItem(ModItems.TITAN_CHESTPLATE.get());
        basicItem(ModItems.TITAN_BOOTS.get());
        basicItem(ModItems.RISING_MIGHTY_HELMET.get());
        basicItem(ModItems.RISING_MIGHTY_CHESTPLATE.get());
        basicItem(ModItems.RISING_MIGHTY_BOOTS.get());
        basicItem(ModItems.RISING_DRAGON_HELMET.get());
        basicItem(ModItems.RISING_DRAGON_CHESTPLATE.get());
        basicItem(ModItems.RISING_DRAGON_BOOTS.get());

        // 驱动器
        basicItem(ModItems.ARCLE.get());


    }


}