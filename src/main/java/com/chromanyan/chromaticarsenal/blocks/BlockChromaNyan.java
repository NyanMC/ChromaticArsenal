package com.chromanyan.chromaticarsenal.blocks;

import com.chromanyan.chromaticarsenal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockChromaNyan extends BlockBlahaj {

    // ugly trinkets hitboxes, makes for consistency though
    protected static final VoxelShape DEFAULT_AABB = Shapes.box(0.2D, 0.0D, 0.2D, 0.8D, 0.8D, 0.8D);
    protected static final VoxelShape NORTH_AABB = Shapes.box(0.2D, 0.0D, 0.15D, 0.8D, 0.8D, 0.85D);
    protected static final VoxelShape SOUTH_AABB = Shapes.box(0.2D, 0.0D, 0.15D, 0.8D, 0.8D, 0.85D);
    protected static final VoxelShape WEST_AABB = Shapes.box(0.15D, 0.0D, 0.2D, 0.85D, 0.8D, 0.8D);
    protected static final VoxelShape EAST_AABB = Shapes.box(0.15D, 0.0D, 0.2D, 0.85D, 0.8D, 0.8D);

    public BlockChromaNyan() {
        super(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_PINK)
                .strength(0.8F)
                .sound(SoundType.WOOL)
                .noOcclusion()
                .isSuffocating(ModBlocks::never)
                .isViewBlocking(ModBlocks::never));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_AABB;
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            case EAST -> EAST_AABB;
            default -> DEFAULT_AABB;
        };
    }

}
