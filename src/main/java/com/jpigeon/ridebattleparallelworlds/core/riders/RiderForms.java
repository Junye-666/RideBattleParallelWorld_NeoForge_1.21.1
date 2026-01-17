package com.jpigeon.ridebattleparallelworlds.core.riders;

import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class RiderForms {
    public static final List<ResourceLocation> KUUGA_FORMS = List.of(
            KuugaConfig.GROWING_ID,
            KuugaConfig.MIGHTY_ID, KuugaConfig.DRAGON_ID, KuugaConfig.PEGASUS_ID, KuugaConfig.TITAN_ID,
            KuugaConfig.RISING_MIGHTY_ID, KuugaConfig.RISING_DRAGON_ID, KuugaConfig.RISING_PEGASUS_ID, KuugaConfig.RISING_TITAN_ID,
            KuugaConfig.AMAZING_MIGHTY_ID, KuugaConfig.ULTIMATE_ID
    );
    public static final List<ResourceLocation> AGITO_FORMS = List.of(
            AgitoConfig.GROUND_ID,
            AgitoConfig.FLAME_ID, AgitoConfig.STORM_ID
    );
}
