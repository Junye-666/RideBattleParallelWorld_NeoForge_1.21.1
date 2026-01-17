package com.jpigeon.ridebattleparallelworlds.core.debug;

import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.api.ParallelWorldsApi;
import com.jpigeon.ridebattleparallelworlds.core.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.core.attachment.PWData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

import static com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds.fromString;

public class PWCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ridebattleparallelworlds")
                .requires(source -> source.hasPermission(1))
                .then(Commands.argument("riderName", StringArgumentType.string())
                        .then(Commands.literal("unlockForm")
                                .then(Commands.argument("formName", StringArgumentType.string())
                                        .executes(context -> unlockForm(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "riderName"),
                                                StringArgumentType.getString(context, "formName")
                                        ))
                                )
                        )
                )
                .then(Commands.literal("checkData")
                        .executes(context -> checkData(context.getSource()))
                )
        );
    }

    private static int unlockForm(CommandSourceStack source, String riderName, String formName) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;

        ResourceLocation riderId = fromString(riderName);
        ResourceLocation formId = fromString(formName);

        // 解锁前状态
        PWData dataBefore = player.getData(PWAttachments.PW_DATA);
        boolean wasUnlocked = dataBefore.isFormUnlocked(riderId, formId);

        if (Config.DEBUG_MODE.get()) {
            source.sendSuccess(() ->
                    Component.literal("解锁前: " + (wasUnlocked ? "已解锁" : "未解锁"))
                            .withStyle(ChatFormatting.YELLOW), false);
        }


        // 执行解锁
        boolean success = ParallelWorldsApi.unlockForm(player, riderId, formId);

        if (success && Config.DEBUG_MODE.get()) {
            source.sendSuccess(() ->
                    Component.literal("成功解锁形态" + formId.getPath())
                            .withStyle(ChatFormatting.GREEN), false);
        } else if (Config.DEBUG_MODE.get()){
            source.sendFailure(Component.literal("解锁失败或形态已解锁"));
        }

        return success ? 1 : 0;
    }

    private static int checkData(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;

        PWData data = player.getData(PWAttachments.PW_DATA);
        Map<ResourceLocation, Map<ResourceLocation, Boolean>> allData =
                data.getFormUnlockData().getAllUnlockData();

        source.sendSuccess(() ->
                Component.literal("=== 形态解锁数据 ===")
                        .withStyle(ChatFormatting.GOLD), false);

        for (Map.Entry<ResourceLocation, Map<ResourceLocation, Boolean>> riderEntry : allData.entrySet()) {
            source.sendSuccess(() ->
                    Component.literal("骑士: " + riderEntry.getKey())
                            .withStyle(ChatFormatting.AQUA), false);

            for (Map.Entry<ResourceLocation, Boolean> formEntry : riderEntry.getValue().entrySet()) {
                source.sendSuccess(() ->
                        Component.literal("  " + formEntry.getKey().getPath() + ": " +
                                        (formEntry.getValue() ? "✅" : "❌"))
                                .withStyle(formEntry.getValue() ? ChatFormatting.GREEN : ChatFormatting.RED), false);
            }
        }

        return 1;
    }
}