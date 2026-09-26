package com.jpigeon.ridebattleparallelworlds.client.screen;

import com.jpigeon.ridebattleparallelworlds.common.network.packet.DeckPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MirrorCardScreen extends Screen {

    private static final int ROW_H = 22;
    private static final int WIDTH = 240;
    private static final int PADDING_TOP = 24;
    private static final int PADDING_BOTTOM = 24;

    private final ResourceLocation riderId;
    private final List<ResourceLocation> order;
    private final Set<ResourceLocation> drawn;
    private final List<ResourceLocation> original;
    private boolean dirty;

    public MirrorCardScreen(ResourceLocation riderId,
                            List<ResourceLocation> order,
                            Set<ResourceLocation> drawn) {
        super(Component.translatable("screen.ridebattleparallelworlds.mirro_deck"));
        this.riderId = riderId;
        this.order = new ArrayList<>(order);
        this.original = new ArrayList<>(order);
        this.drawn = new HashSet<>(drawn);
    }

    @Override
    protected void init() {
        super.init();
        int left = (width - WIDTH) / 2;
        int top = (height - (order.size() * ROW_H + PADDING_TOP + PADDING_BOTTOM)) / 2 + PADDING_TOP;

        for (int i = 0; i < order.size(); i++) {
            final int idx = i;
            addRenderableWidget(Button.builder(Component.literal("↑"),
                            b -> swap(idx, idx - 1))
                    .pos(left + WIDTH - 44, top + i * ROW_H + 1)
                    .size(20, 20).build());

            addRenderableWidget(Button.builder(Component.literal("↓"),
                            b -> swap(idx, idx + 1))
                    .pos(left + WIDTH - 22, top + i * ROW_H + 1)
                    .size(20, 20).build());
        }
    }

    private void swap(int a, int b) {
        if (b < 0 || b >= order.size()) return;
        ResourceLocation t = order.get(a);
        order.set(a, order.get(b));
        order.set(b, t);
        dirty = true;
        rebuildWidgets();
    }

    @Override
    public void render(@NotNull GuiGraphics g, int mouseX, int mouseY, float partial) {
        super.render(g, mouseX, mouseY, partial);

        int left = (width - WIDTH) / 2;
        int top = (height - (order.size() * ROW_H + PADDING_TOP + PADDING_BOTTOM)) / 2 + PADDING_TOP;

        g.drawString(font, title, left, top - 14, 0xFFFFFF);

        for (int i = 0; i < order.size(); i++) {
            ResourceLocation cardId = order.get(i);
            int y = top + i * ROW_H;
            boolean isDrawn = drawn.contains(cardId);

            g.fill(left, y, left + WIDTH - 48, y + ROW_H - 2,
                    isDrawn ? 0x30303030 : 0x80000000);

            Item item = BuiltInRegistries.ITEM.get(cardId);
            ItemStack stack = new ItemStack(item == Items.AIR ? Items.PAPER : item);
            g.renderItem(stack, left + 4, y + 3);

            Component cardName = stack.getHoverName();
            g.drawString(font, cardName, left + 28, y + 7,
                    isDrawn ? 0x606060 : 0xFFFFFF);
        }
    }

    @Override
    public void onClose() {
        if (dirty && !order.equals(original)) {
            PacketDistributor.sendToServer(
                    new DeckPackets.SaveDeckOrderPacket(riderId, order));
        }
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
