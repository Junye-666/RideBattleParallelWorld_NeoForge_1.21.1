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
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DragonRodItem extends BaseKamenRiderGeoItem {
    public enum AnimState {IDLE, SPIN_MAIN_HAND, SPIN_OFF_HAND}

    public DragonRodItem(Properties properties) {
        super("kuuga", "dragon_rod", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "spin_main", createOnceController("spin_main"));
        addController(registrar, "spin_off", createOnceController("spin_off"));

    }

    public void triggerMainSpin() {
        setAnimState("spin_main");
    }

    public void triggerOffSpin() {
        setAnimState("spin_off");
    }

    public void setCurrentState(AnimState state){
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
            if (RiderManager.isSpecificForm(player, KuugaConfig.DRAGON_ID)) {
                player.getCooldowns().addCooldown(this, 310);
                RiderManager.triggerSkill(player, RiderSkills.SPLASH_DRAGON, SkillEvent.SkillTriggerType.WEAPON);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
