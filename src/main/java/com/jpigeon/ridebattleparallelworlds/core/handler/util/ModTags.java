package com.jpigeon.ridebattleparallelworlds.core.handler.util;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items{
        public static final TagKey<Item> KAMEN_RIDE_CARDS = createTag("kamen_ride_cards");
        public static final TagKey<Item> FORM_RIDE_CARDS = createTag("form_ride_cards");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, name));
        }
    }
}
