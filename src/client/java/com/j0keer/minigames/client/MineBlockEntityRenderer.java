package com.j0keer.minigames.client;

import com.j0keer.minigames.blockentity.MineBlockEntity;
import com.j0keer.minigames.blocks.MineBlock;
import com.j0keer.minigames.blocks.MineState;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;

public class MineBlockEntityRenderer implements BlockEntityRenderer<MineBlockEntity> {
    private final TextRenderer textRenderer;
    private final Text tntText;

    public MineBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.textRenderer = ctx.getTextRenderer();
        tntText = Text.literal("§c§l+");
    }

    @Override
    public void render(MineBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getWorld() != null ? entity.getWorld().getBlockState(entity.getPos()) : entity.getCachedState();
        MineState state = blockState.contains(MineBlock.STATE) ? blockState.get(MineBlock.STATE) : entity.getState();
        boolean hasMine = blockState.contains(MineBlock.HAS_MINE) && blockState.get(MineBlock.HAS_MINE);

        MinecraftClient client = MinecraftClient.getInstance();
        boolean canSeeMines = client.player != null && client.player.isCreative() && (client.player.getMainHandStack().isOf(Items.TNT) || client.player.getOffHandStack().isOf(Items.TNT));

        if (state == MineState.HIDDEN && !(canSeeMines && hasMine)) return;

        Text text = (canSeeMines && hasMine) ? tntText : state.getDisplayText();
        if (text == null || text.getString().isEmpty()) return;

        matrices.push();
        matrices.translate(0.5, 1.02, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        matrices.scale(0.08f, 0.08f, 0.08f);

        float x = -textRenderer.getWidth(text) / 2.0f;
        float y = -textRenderer.fontHeight / 2.0f;
        textRenderer.draw(text, x, y, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        matrices.pop();
    }
}
