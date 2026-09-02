package com.j0keer.minigames.blockentity;

import com.j0keer.minigames.blocks.AbstractMarkerBlock;
import com.j0keer.minigames.registries.EntityRegistries;
import com.mojang.logging.LogUtils;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class MarkerBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final int MAX_PATTERN_COUNT = 6;
    private static final String PATTERNS_KEY = "patterns";
    @Nullable
    private Text customName;
    private DyeColor baseColor;
    private BannerPatternsComponent patterns;

    public MarkerBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistries.MARKER, pos, state);
        this.patterns = BannerPatternsComponent.DEFAULT;
        this.baseColor = ((AbstractMarkerBlock)state.getBlock()).getColor();
    }

    public MarkerBlockEntity(BlockPos pos, BlockState state, DyeColor baseColor) {
        this(pos, state);
        this.baseColor = baseColor;
    }

    public void readFrom(ItemStack stack, DyeColor baseColor) {
        this.baseColor = baseColor;
        this.readComponents(stack);
    }

    public Text getName() {
        return (Text)(this.customName != null ? this.customName : Text.translatable("block.minecraft.banner"));
    }

    @Nullable
    public Text getCustomName() {
        return this.customName;
    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (!this.patterns.equals(BannerPatternsComponent.DEFAULT)) {
            nbt.put("patterns", (NbtElement)BannerPatternsComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), this.patterns).getOrThrow());
        }

        if (this.customName != null) {
            nbt.putString("CustomName", Text.Serialization.toJsonString(this.customName, registryLookup));
        }

    }

    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("CustomName", 8)) {
            this.customName = tryParseCustomName(nbt.getString("CustomName"), registryLookup);
        }

        if (nbt.contains("patterns")) {
            BannerPatternsComponent.CODEC.parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("patterns")).resultOrPartial((patterns) -> LOGGER.error("Failed to parse banner patterns: '{}'", patterns)).ifPresent((patterns) -> this.patterns = patterns);
        }

    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
    }

    public BannerPatternsComponent getPatterns() {
        return this.patterns;
    }

    public ItemStack getPickStack() {
        ItemStack itemStack = new ItemStack(BannerBlock.getForColor(this.baseColor));
        itemStack.applyComponentsFrom(this.createComponentMap());
        return itemStack;
    }

    public DyeColor getColorForState() {
        return this.baseColor;
    }

    protected void readComponents(BlockEntity.ComponentsAccess components) {
        super.readComponents(components);
        this.patterns = (BannerPatternsComponent)components.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);
        this.customName = (Text)components.get(DataComponentTypes.CUSTOM_NAME);
    }

    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(DataComponentTypes.BANNER_PATTERNS, this.patterns);
        componentMapBuilder.add(DataComponentTypes.CUSTOM_NAME, this.customName);
    }

    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        nbt.remove("patterns");
        nbt.remove("CustomName");
    }
}
