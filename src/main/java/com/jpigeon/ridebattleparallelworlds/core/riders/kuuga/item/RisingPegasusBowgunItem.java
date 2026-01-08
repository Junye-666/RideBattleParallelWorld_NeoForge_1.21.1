package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
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

public class RisingPegasusBowgunItem extends BaseKamenRiderGeoItem {
    public enum AnimState {IDLE, PULL, RELEASE, SHOOT}
    public RisingPegasusBowgunItem(Properties properties) {
        super("kuuga", "rising_pegasus_bowgun", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "pull", createLoopController("pull"));
        addController(registrar, "release", createLoopController("release"));
        addController(registrar, "shoot", createOnceController("shoot"));
    }

    public void triggerShoot() {
        setAnimState("shoot");
    }

    public void triggerPull() {
        setAnimState("pull");
    }

    public void triggerRelease() {
        setAnimState("release");
    }

    public void setCurrentState(PegasusBowgunItem.AnimState state){
        setAnimState(state.name().toLowerCase());
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
            if (RiderManager.isSpecificForm(player, KuugaConfig.RISING_PEGASUS_ID)) {
                player.getCooldowns().addCooldown(this, 210);
                RiderManager.triggerSkill(player, RiderSkills.RISING_BLAST_PEGASUS, SkillEvent.SkillTriggerType.WEAPON);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
