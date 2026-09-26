package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.server.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 降临卡基类。
 * <p>
 * 卡牌物品 id 形如 {@code ryuki_sword_vent_card}，
 * 对应 skillId 就是去掉 {@code _card} 后缀：
 * {@code ridebattleparallelworlds:ryuki_sword_vent}。
 * <p>
 * 玩家右键使用后自动分发 skill；skill 成功触发则消耗该卡。
 * 卡本身没有耐久 / 无堆叠上限 = 1。
 */
public class VentCardItem extends Item {
    private static final String CARD_SUFFIX = "_card";
    private final String ventPath;


    /**
     * @param ventPath 共享翻译键的短名，例如 "final_vent"。
     *                 完整 key = {@code card.ridebattleparallelworlds.<ventPath>}
     */
    public VentCardItem(String ventPath, Properties properties) {
        super(properties.stacksTo(1));
        this.ventPath = ventPath;
    }

    @Override
    public @NotNull String getDescriptionId() {
        String vent = !Objects.equals(ventPath, "ad") ? ventPath + "_vent" : "advent";
        return "card." + RideBattleParallelWorlds.MODID + "." + vent;
    }

    /**
     * 由自身 item id 推导 skillId。
     */
    protected ResourceLocation resolveSkillId() {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(this);
        String path = itemId.getPath();
        if (path.endsWith(CARD_SUFFIX)) {
            path = path.substring(0, path.length() - CARD_SUFFIX.length());
        }
        return ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), path);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player,
                                                           @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide()) return InteractionResultHolder.success(stack);
        if (!RideBattleAPI.isTransformed(player)) return InteractionResultHolder.pass(stack);

        ResourceLocation skillId = resolveSkillId();
        boolean triggered = RideBattleAPI.triggerSkill(
                player, skillId, SkillEvent.SkillTriggerType.ITEM);

        if (triggered) {
            stack.shrink(1);
        }
        return InteractionResultHolder.success(stack);
    }
}
