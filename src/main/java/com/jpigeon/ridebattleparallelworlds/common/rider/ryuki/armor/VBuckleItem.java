package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.armor;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.rideevolutionlib.compat.geckoLib.armor.BaseRiderArmorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;

public class VBuckleItem extends BaseRiderArmorItem {
    public VBuckleItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(RideBattleParallelWorlds.MODID, "mirror", "v-buckle", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "appear", createOnceController("appear"));
    }

    public void triggerAppear() {
        setAnimState("appear");
        RideBattleAPI.scheduleTicks(32, this::setIdle);
    }

    public void setIdle() {
        setAnimState("idle");
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        tooltip.add(Component.translatable("tooltip.v-buckle.description").withStyle(ChatFormatting.RED));
    }

    @Override
    public @NotNull Holder<SoundEvent> getEquipSound() {
        return Holder.direct(ModSounds.SUMMON_V_BUCKLE.get());
    }
}
