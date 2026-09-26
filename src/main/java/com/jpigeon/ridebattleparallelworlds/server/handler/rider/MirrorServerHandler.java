package com.jpigeon.ridebattleparallelworlds.server.handler.rider;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.server.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.server.event.HenshinEvent;
import com.jpigeon.ridebattlelib.server.event.SlotExtractionEvent;
import com.jpigeon.ridebattlelib.server.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder.card.CardDeckRegistry;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModEntities;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.MirrorConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.entity.RyukiHenshinEffect;
import com.jpigeon.ridebattleparallelworlds.server.handler.MirrorDeckHandler;
import com.jpigeon.ridebattleparallelworlds.server.util.PWSkillUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.completeIn;
import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.scheduleTicks;
import static com.jpigeon.ridebattleparallelworlds.server.util.RiderUtils.playSound;

public class MirrorServerHandler implements IRiderServerHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.MIRROR_SYSTEM_ID;
    }

    @Override
    public void onHenshinPre(HenshinEvent.@NotNull Pre event) {
        Player player = event.getPlayer();
        ResourceLocation formId = event.getFormId();

        // TODO : 镜系统完善
        playSound(player, ModSounds.MIRROR_HENSHIN.get());
        Level level = player.level();

        if (formId.equals(MirrorConfig.RYUKI_BASE_ID)) {
            RyukiHenshinEffect effect = new RyukiHenshinEffect(
                    ModEntities.RYUKI_HENSHIN_EFFECT.get(),
                    level
            );
            effect.setPos(player.position());
            effect.setYRot(player.getYRot());
            effect.setOwner(player);

            scheduleTicks(10, () -> level.addFreshEntity(effect));
            completeIn(22, player);
        }
    }

    @Override
    public void onHenshinPost(HenshinEvent.@NotNull Post event) {
        Player player = event.getPlayer();

        PWSkillUtils.addSaturation(player, 40);
        PWSkillUtils.addRegeneration(player, 20);
    }

    @Override
    public void onSwitchPre(FormSwitchEvent.@NotNull Pre event) {
        // TODO：生存形态
    }

    @Override
    public void onUnhenshinPost(UnhenshinEvent.@NotNull Post event) {
        Player player = event.getPlayer();

        // 重置抽卡记录
        MirrorDeckHandler.resetDrawn(player, MirrorConfig.RYUKI_BASE_ID);

        // 清掉玩家背包里所有残留的 VentCardItem
        removeVentCards(player);

        ResourceLocation vDeck = MirrorConfig.V_DECK;
        if (RideBattleAPI.getItemForSlot(player, vDeck) != null) {
            RideBattleAPI.extractItemFromSlot(player, vDeck);
        }
        player.setItemSlot(EquipmentSlot.LEGS, Items.AIR.getDefaultInstance());
    }

    /**
     * 拦截 V_DECK 槽位里 RYUKI_DECK 的取出：
     * 取消取出，改为抽一张卡。
     * <p>
     * 触发时机：玩家按 X（返还物品）时，DriverSystem 会遍历所有槽位逐个 extract，
     * 走到 V_DECK 时先发 SlotExtractionEvent.Pre 给我们。
     */
    public static void onExtractPre(SlotExtractionEvent.Pre event) {
        Player player = event.getPlayer();
        if (!(player instanceof net.minecraft.server.level.ServerPlayer)) return;
        if (!RideBattleAPI.isTransformed(player) || RideBattleAPI.isTransforming(player)) return;
        if (!event.getSlotId().equals(MirrorConfig.V_DECK)) return;

        // 用当前形态决定抽哪个卡池 空则走正常取出
        ResourceLocation formId = RideBattleAPI.getCurrentFormId(player);
        if (formId == null) return;
        if (CardDeckRegistry.getCards(formId).isEmpty()) return;

        event.setCanceled(true);
        MirrorDeckHandler.onDraw(player, formId);
    }

    private static void removeVentCards(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.item.VentCardItem) {
                player.getInventory().setItem(i, ItemStack.EMPTY);
            }
        }
    }
}
