package com.jpigeon.ridebattleparallelworlds.core.client;

import com.jpigeon.ridebattlelib.client.cache.ClientTransformedCache;
import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.MirrorConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.event.GeoRenderEvent;
import software.bernie.geckolib.util.RenderUtil;

import java.util.HashMap;
import java.util.Map;

public class RenderHandler {
    private static final Map<ResourceLocation, ItemStack> DECK_FORM_MAP = new HashMap<>();

    public static void registerDeckFormMap() {
        DECK_FORM_MAP.put(MirrorConfig.RYUKI_BASE_ID, ModItems.RYUKI_DECK.toStack());
    }

    private static ItemStack getItemToRender(ResourceLocation formId) {
        return DECK_FORM_MAP.get(formId);
    }

    @SubscribeEvent
    public static void onRenderGeo(GeoRenderEvent.Armor.Post event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (RideBattleAPI.isSpecificRider(player, RiderIds.MIRROR_SYSTEM_ID)) {
            handleVBuckleDeckRender(player, event);
        }
    }

    private static void handleVBuckleDeckRender(Player player, GeoRenderEvent.Armor.Post event) {

        ResourceLocation formId = ClientTransformedCache.getCurrentFormId(player.getUUID());
        if (formId == null) return;

        ItemStack stackToRender = getItemToRender(formId);
        if (stackToRender == null) return;

        GeoBone bone = event.getModel().getBone("armorBody").orElse(null);
        if (bone == null) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getBufferSource();
        int packedLight = event.getPackedLight();

        poseStack.pushPose();

        // 局部微调（根据你的模型）
        if (player.isCrouching()) poseStack.translate(0, 1.24, 0.152);
        else poseStack.translate(0, 1.086, -0.15);

        poseStack.scale(-0.32F, -0.32F, 0.5F);

        // 转移到骨骼空间
        RenderUtil.translateAndRotateMatrixForBone(poseStack, bone);

        // 渲染物品
        Minecraft.getInstance().getItemRenderer().renderStatic(
                player,
                stackToRender,
                ItemDisplayContext.FIXED,
                false,
                poseStack,
                bufferSource,
                player.level(),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }
}

