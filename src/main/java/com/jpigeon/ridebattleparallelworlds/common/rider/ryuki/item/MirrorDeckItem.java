package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattleparallelworlds.client.anim.rider.MirrorAnimations;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.DeckPackets;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModItems;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.armor.VBuckleItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * 镜世界卡盒物品。
 * <p>
 * 内部绑定一个 {@code formId}，即该卡盒使用的卡池 key。主手蹲下右键打开卡牌编排界面，
 * 副手右键在未变身时装配 V-Buckle。
 * <p>
 * formId 用 {@link Supplier} 传，避免 {@code ModItems.Ryuki.<clinit>} 与
 * {@code MirrorConfig.<clinit>} 之间的循环。
 */
public class MirrorDeckItem extends Item {

    private final Supplier<ResourceLocation> formIdSupplier;

    public MirrorDeckItem(@NotNull Supplier<ResourceLocation> formIdSupplier, Properties properties) {
        super(properties.stacksTo(1));
        this.formIdSupplier = formIdSupplier;
    }

    /** 该卡盒对应的卡池 key；空卡盒返回 null。 */
    public @Nullable ResourceLocation getFormId() {
        return formIdSupplier.get();
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           Player player,
                                                           @NotNull InteractionHand usedHand) {
        ItemStack deck = player.getItemInHand(usedHand);

        // ========== 主手 + 蹲下 → 打开卡牌编排界面（不要求变身） ==========
        if (usedHand == InteractionHand.MAIN_HAND && player.isCrouching()) {
            ResourceLocation formId = getFormId();
            if (formId == null) return InteractionResultHolder.pass(deck);
            if (level.isClientSide()) {
                PacketDistributor.sendToServer(new DeckPackets.OpenDeckPacket(formId));
            }
            return InteractionResultHolder.success(deck);
        }

        // ========== 副手 → 未变身时装配 V-Buckle ==========
        if (usedHand != InteractionHand.OFF_HAND) {
            return InteractionResultHolder.fail(deck);
        }
        if (RideBattleAPI.isTransformed(player)) {
            return InteractionResultHolder.pass(deck);
        }

        ItemStack leg = player.getItemBySlot(EquipmentSlot.LEGS);
        if (leg.getItem() instanceof VBuckleItem) {
            return InteractionResultHolder.success(deck);
        }
        player.getCooldowns().addCooldown(deck.getItem(), 30);
        player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.Ryuki.V_BUCKLE.get()));
        if (player instanceof LocalPlayer p) {
            MirrorAnimations.V_BUCKLE.play(p);
        }
        if (!leg.isEmpty() && !leg.is(Items.AIR)) {
            if (!player.getInventory().add(leg)) {
                player.drop(leg, false);
            }
        }
        return InteractionResultHolder.pass(deck);
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.mirrordeck.tutorial"));
        }
    }
}