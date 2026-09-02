package com.j0keer.minigames.client;

import com.j0keer.minigames.items.MarkerItem;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class MarkerItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private ModelPart flag;
    private ModelPart pole;
    private ModelPart bar;

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (this.flag == null) {
            ModelPart root = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.BANNER);
            this.flag = root.getChild("flag");
            this.pole = root.getChild("pole");
            this.bar = root.getChild("bar");
        }

        DyeColor color = stack.getItem() instanceof MarkerItem markerItem ? markerItem.getColor() : DyeColor.WHITE;
        BannerPatternsComponent patterns = stack.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);

        matrices.push();
        matrices.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);

        if (mode == ModelTransformationMode.GUI) {
            matrices.translate(1.3, 0, 0);
        } else {
            matrices.translate(0.4, 0, -1);
        }

        this.pole.visible = true;
        this.bar.visible = false;
        this.pole.render(matrices, vertexConsumer, light, overlay);

        this.flag.pitch = 0.0F;
        this.flag.roll = (float) Math.toRadians(90);
        this.flag.pivotY = -22;
        this.flag.pivotX = 2;
        this.flag.pivotZ = 5.0F;
        this.flag.xScale = 0.6F;
        this.flag.yScale = 0.6F;
        this.flag.zScale = 3.0F;

        MarkerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay, this.flag, ModelLoader.BANNER_BASE, true, color, patterns);
        matrices.pop();
    }
}
