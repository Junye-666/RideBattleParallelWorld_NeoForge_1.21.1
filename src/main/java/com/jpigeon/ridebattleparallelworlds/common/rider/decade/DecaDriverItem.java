package com.jpigeon.ridebattleparallelworlds.common.rider.decade;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.compat.geckoLib.armor.BaseRiderArmorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;
import java.util.Map;

public class DecaDriverItem extends BaseRiderArmorItem {
    public DecaDriverItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(RideBattleParallelWorlds.MODID, "decade", "deca_driver", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        // 持续状态：驱动器展开
        addController(registrar, "opened", createLoopController("opened"));

        // 持续状态：驱动器关闭
        addController(registrar, "closed", createLoopController("closed"));

        // 一次性动作：开合动画
        addController(registrar, "actions",
                createTriggerController(
                        "actions",
                        Map.of(
                                "open",
                                RawAnimation.begin().then(
                                        "open",
                                        Animation.LoopType.PLAY_ONCE
                                ),
                                "close",
                                RawAnimation.begin().then(
                                        "close",
                                        Animation.LoopType.PLAY_ONCE
                                )
                        )
                )
        );
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        tooltip.add(Component.translatable("tooltip.decaDriver.description").withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
