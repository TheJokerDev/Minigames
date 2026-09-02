package com.j0keer.minigames.client;

import com.j0keer.minigames.blockentity.MarkerBlockEntity;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.*;

public class MarkerBlockEntityRenderer implements BlockEntityRenderer<MarkerBlockEntity> {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 40;
    private static final int ROTATIONS = 16;
    public static final String BANNER = "flag";
    private static final String PILLAR = "pole";
    private static final String CROSSBAR = "bar";
    private final ModelPart banner;
    private final ModelPart pillar;
    private final ModelPart crossbar;

    public MarkerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getLayerModelPart(EntityModelLayers.BANNER);
        this.banner = modelPart.getChild("flag");
        this.pillar = modelPart.getChild("pole");
        this.crossbar = modelPart.getChild("bar");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("flag", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F), ModelTransform.NONE);
        modelPartData.addChild("pole", ModelPartBuilder.create().uv(44, 0).cuboid(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F), ModelTransform.NONE);
        modelPartData.addChild("bar", ModelPartBuilder.create().uv(0, 42).cuboid(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 64);
    }

    public void render(MarkerBlockEntity markerBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        float g = 0.6666667F;
        boolean bl = markerBlockEntity.getWorld() == null;
        matrixStack.push();
        long l;
        if (bl) {
            l = 0L;
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            this.pillar.visible = true;
        } else {
            l = markerBlockEntity.getWorld().getTime();
            BlockState blockState = markerBlockEntity.getCachedState();
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            float h = -RotationPropertyHelper.toDegrees((Integer)blockState.get(BannerBlock.ROTATION));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
            this.pillar.visible = true;
        }

        matrixStack.push();
        matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
        this.pillar.render(matrixStack, vertexConsumer, i, j);
        this.crossbar.render(matrixStack, vertexConsumer, i, j);
        BlockPos blockPos = markerBlockEntity.getPos();
        float k = ((float)Math.floorMod((long)(blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13) + l, 100L) + f) / 100.0F;
        this.banner.pitch = (-0.0125F + 0.01F * MathHelper.cos(((float)Math.PI * 2F) * k)) * (float)Math.PI;
        this.banner.pivotY = -32.0F;
        renderCanvas(matrixStack, vertexConsumerProvider, i, j, this.banner, ModelLoader.BANNER_BASE, true, markerBlockEntity.getColorForState(), markerBlockEntity.getPatterns());
        matrixStack.pop();
        matrixStack.pop();
    }

    public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, DyeColor color, BannerPatternsComponent patterns) {
        renderCanvas(matrices, vertexConsumers, light, overlay, canvas, baseSprite, isBanner, color, patterns, false);
    }

    public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, DyeColor color, BannerPatternsComponent patterns, boolean glint) {
        canvas.render(matrices, baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light, overlay);
        renderLayer(matrices, vertexConsumers, light, overlay, canvas, isBanner ? TexturedRenderLayers.BANNER_BASE : TexturedRenderLayers.SHIELD_BASE, color);

        for(int i = 0; i < 16 && i < patterns.layers().size(); ++i) {
            BannerPatternsComponent.Layer layer = (BannerPatternsComponent.Layer)patterns.layers().get(i);
            SpriteIdentifier spriteIdentifier = isBanner ? TexturedRenderLayers.getBannerPatternTextureId(layer.pattern()) : TexturedRenderLayers.getShieldPatternTextureId(layer.pattern());
            renderLayer(matrices, vertexConsumers, light, overlay, canvas, spriteIdentifier, layer.color());
        }

    }

    private static void renderLayer(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier textureId, DyeColor color) {
        int i = color.getEntityColor();
        canvas.render(matrices, textureId.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline), light, overlay, i);
    }
}
