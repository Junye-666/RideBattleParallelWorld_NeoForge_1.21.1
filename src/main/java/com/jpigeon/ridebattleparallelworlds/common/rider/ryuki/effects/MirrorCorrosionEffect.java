package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * 镜世界腐蚀效果：纯计时器，Expired 时由 MirrorEffectHandler 处理传送。
 */
public class MirrorCorrosionEffect extends MobEffect {
    public MirrorCorrosionEffect() {
        super(MobEffectCategory.NEUTRAL, 0x8B8BFF);
    }
}
