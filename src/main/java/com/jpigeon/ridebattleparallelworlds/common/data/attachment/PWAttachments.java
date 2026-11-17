package com.jpigeon.ridebattleparallelworlds.common.data.attachment;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder.FormUnlockData;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder.card.CardData;
import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorCoverageData;
import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorDirtyData;
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
                                    () -> new PWData(new FormUnlockData(), new CardData())
                            )
                            .serialize(PWData.CODEC)
                            .copyOnDeath()
                            .build());

    /**
     * 主世界镜像覆盖记录。
     */
    public static final Supplier<AttachmentType<MirrorCoverageData>> MIRROR_COVERAGE =
            ATTACHMENTS.register(
                    "mirror_coverage",
                    () -> AttachmentType.builder(MirrorCoverageData::new)
                            .serialize(MirrorCoverageData.CODEC)
                            .build()
            );

    /**
     * 主世界镜像 Dirty 记录。
     */
    public static final Supplier<AttachmentType<MirrorDirtyData>> MIRROR_DIRTY =
            ATTACHMENTS.register(
                    "mirror_dirty",
                    () -> AttachmentType.builder(MirrorDirtyData::new)
                            .serialize(MirrorDirtyData.CODEC)
                            .build()
            );

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }
}
