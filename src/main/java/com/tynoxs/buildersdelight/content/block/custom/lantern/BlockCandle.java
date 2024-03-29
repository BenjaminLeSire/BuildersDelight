package com.tynoxs.buildersdelight.content.block.custom.lantern;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockCandle extends LanternLightable {
    protected final ParticleOptions flameParticle;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    protected static final VoxelShape HANGING = Block.box(0, 0, 0, 16, 16, 16);
    protected static final VoxelShape STANDING = Block.box(5, 0, 5, 11, 15, 11);
    protected static final VoxelShape MOUNTED_NORTH = Block.box(4, 1, 6, 12, 15, 16);
    protected static final VoxelShape MOUNTED_SOUTH = Block.box(4, 1, 0, 12, 15, 10);
    protected static final VoxelShape MOUNTED_WEST = Block.box(6, 1, 4, 16, 15, 12);
    protected static final VoxelShape MOUNTED_EAST = Block.box(0, 1, 4, 10, 15, 12);

    public BlockCandle(BlockBehaviour.Properties properties, ParticleOptions particle) {
        super(properties);
        this.flameParticle = particle;
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(LIT, false));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, FACE, WATERLOGGED, LIT);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidstate = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;

        for(Direction direction : blockPlaceContext.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, blockPlaceContext.getHorizontalDirection()).setValue(WATERLOGGED, Boolean.valueOf(flag));
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite()).setValue(WATERLOGGED, Boolean.valueOf(flag));
            }

            if (blockstate.canSurvive(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos())) {
                return blockstate;
            }
        }
        return null;
    }

    public @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = blockState.getValue(FACING);
        switch(blockState.getValue(FACE)) {
            case FLOOR:
                return STANDING;
            case WALL:
                return switch (direction) {
                    case EAST -> MOUNTED_EAST;
                    case WEST -> MOUNTED_WEST;
                    case SOUTH -> MOUNTED_SOUTH;
                    default -> MOUNTED_NORTH;
                };
            case CEILING:
            default: return HANGING;
        }
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if (stateIn.getValue(LIT) && !stateIn.getValue(WATERLOGGED)) {
            double d0, d1, d2, d3, d4, d5, d6, d7;
            Direction dir1 = stateIn.getValue(FACING);

            switch (stateIn.getValue(FACE)) {
                default -> {
                    d0 = pos.getX() + 0.5D;
                    d1 = pos.getY() + 1.01D;
                    d2 = pos.getZ() + 0.5D;
                    worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0, 0, 0);
                    worldIn.addParticle(this.flameParticle, d0, d1, d2, 0, 0, 0);
                }
                case WALL -> {
                    double xo1 = -dir1.getStepX() * 0.11;
                    double zo2 = -dir1.getStepZ() * 0.11;
                    d0 = pos.getX() + 0.5D + xo1;
                    d1 = pos.getY() + 0.92D;
                    d2 = pos.getZ() + 0.5D + zo2;
                    worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0, 0, 0);
                    worldIn.addParticle(this.flameParticle, d0, d1, d2, 0, 0, 0);
                }
                case CEILING -> {

                    d0 = pos.getX() + 0.5D;
                    d1 = pos.getY() + 0.75D;
                    d2 = pos.getZ() + 0.15D;
                    d3 = pos.getX() + 0.15D;
                    d4 = pos.getZ() + 0.5D;
                    d5 = pos.getX() + 0.85D;
                    d6 = pos.getY() + 0.75D;
                    d7 = pos.getZ() + 0.85D;

                    worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0, 0, 0);
                    worldIn.addParticle(this.flameParticle, d0, d1, d2, 0, 0, 0);

                    worldIn.addParticle(ParticleTypes.SMOKE, d3, d1, d4, 0, 0, 0);
                    worldIn.addParticle(this.flameParticle, d3, d1, d4, 0, 0, 0);

                    worldIn.addParticle(ParticleTypes.SMOKE, d5, d6, d4, 0, 0, 0);
                    worldIn.addParticle(this.flameParticle, d5, d6, d4, 0, 0, 0);

                    worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d7, 0, 0, 0);
                    worldIn.addParticle(this.flameParticle, d0, d1, d7, 0, 0, 0);
                }
            }
        }
    }
}
