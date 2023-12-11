package com.chromanyan.chromaticarsenal.blocks;

import com.chromanyan.chromaticarsenal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockBlahaj extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape DEFAULT_AABB = Shapes.box(0.2D, 0.0D, 0.2D, 0.8D, 0.8D, 0.8D);
    protected static final VoxelShape NORTH_AABB = Shapes.box(0.375D, 0.0D, 0.046875D, 0.625D, 0.25D, 0.921875D);
    protected static final VoxelShape SOUTH_AABB = Shapes.box(0.375D, 0.0D, 0.078125D, 0.625D, 0.25D, 0.953125D);
    protected static final VoxelShape WEST_AABB = Shapes.box(0.046875D, 0.0D, 0.375D, 0.921875D, 0.25D, 0.625D);
    protected static final VoxelShape EAST_AABB = Shapes.box(0.078125D, 0.0D, 0.375D, 0.953125D, 0.25D, 0.625D);

    public BlockBlahaj() {
        super(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_LIGHT_BLUE)
                .strength(0.8F)
                .sound(SoundType.WOOL)
                .noOcclusion()
                .isSuffocating(ModBlocks::never)
                .isViewBlocking(ModBlocks::never));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public BlockBlahaj(BlockBehaviour.Properties bb) { // second constructor for chromanyan plush to use
        super(bb);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @SuppressWarnings("deprecation") // if farmer's delight uses it then i will too, i don't give a damn
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_60550_) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState p_49928_, @NotNull BlockGetter p_49929_, @NotNull BlockPos p_49930_) {
        return true;
    }

    @SuppressWarnings("deprecation") // see above comment
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_48781_) {
        return this.defaultBlockState().setValue(FACING, p_48781_.getHorizontalDirection().getOpposite());
    }
}
