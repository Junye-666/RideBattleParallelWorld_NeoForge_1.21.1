package com.jpigeon.ridebattleparallelworlds.core.attachment;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class PWAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RideBattleParallelWorlds.MODID);

    public static final Supplier<AttachmentType<PWData>> PW_DATA =
            ATTACHMENTS.register("pw_data",
                    () -> AttachmentType.builder(
                                    () -> new PWData(new FormUnlockData())
                            )
                            .serialize(PWData.CODEC)
                            .copyOnDeath()
                            .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }
}
