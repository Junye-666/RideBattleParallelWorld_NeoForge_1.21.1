package com.jpigeon.ridebattleparallelworlds.core.riders.agito.item;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.item.BaseKamenRiderGeoItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.item.GenericItemModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.item.GenericItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class StormHalberdItem extends BaseKamenRiderGeoItem {
    public enum AnimState {IDLE, OPEN}

    public StormHalberdItem(Properties properties) {
        super("agito", "storm_halberd", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "open", createHoldController("open"));
    }

    public void triggerOpen(){
        setAnimState("open");
    }
    public void setClose(){
        setAnimState("idle");
    }

    @Override
    protected GeoItemRenderer<BaseKamenRiderGeoItem> createRenderer() {
        return new GenericItemRenderer(
                new GenericItemModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide() && RiderManager.isTransformed(player)) {
            if (RiderManager.isSpecificForm(player, AgitoConfig.STORM_ID) || RiderManager.isSpecificForm(player, AgitoConfig.TRINITY_ID)) {
                if (usedHand.equals(InteractionHand.MAIN_HAND)) {
                    player.getCooldowns().addCooldown(this, 310);
                    triggerOpen();
                    RiderManager.triggerSkill(player, RiderSkills.HALBERD_SPIN, SkillEvent.SkillTriggerType.WEAPON);
                } else {
                    return InteractionResultHolder.pass(itemStack);
                }

            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
