package eu.midnightdust.roundtrees.mixin;

import eu.midnightdust.roundtrees.RoundTreesClient;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PillarBlock.class)
public abstract class MixinLogBlock extends Block {
    private static final VoxelShape CUBE_SHAPE;
    private static final VoxelShape X_SHAPE;
    private static final VoxelShape Y_SHAPE;
    private static final VoxelShape Z_SHAPE;

    public MixinLogBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (state.getBlock().toString().toLowerCase().contains("log") && RoundTreesClient.RT_CONFIG.round_box) {
            switch (state.get(PillarBlock.AXIS)) {
                case X:
                    return X_SHAPE;
                case Y:
                    return Y_SHAPE;
                case Z:
                    return Z_SHAPE;
                default:
                    return Y_SHAPE;
            }
        }
        else { return CUBE_SHAPE; }

    }
    static {
        VoxelShape cube = createCuboidShape(0, 0, 0, 16, 16, 16);
        VoxelShape y1 = createCuboidShape(-1, 0, 2, 17, 16, 14);
        VoxelShape y2 = createCuboidShape(2, 0, -1, 14, 16, 17);
        VoxelShape y3 = createCuboidShape(-2, 0, 4, 18, 16, 12);
        VoxelShape y4 = createCuboidShape(4, 0, -2, 12, 16, 18);

        VoxelShape x1 = createCuboidShape(0,-1,  2, 16,17,  14);
        VoxelShape x2 = createCuboidShape(0,2,  -1, 16,14,  17);
        VoxelShape x3 = createCuboidShape(0,-2,  4, 16,18,  12);
        VoxelShape x4 = createCuboidShape(0,4,  -2, 16,12,  18);

        VoxelShape z1 = createCuboidShape(-1, 2, 0, 17, 14, 16);
        VoxelShape z2 = createCuboidShape(2, -1, 0, 14, 17, 16);
        VoxelShape z3 = createCuboidShape(-2, 4, 0, 18, 12, 16);
        VoxelShape z4 = createCuboidShape(4, -2, 0, 12, 18, 16);

        VoxelShape y_shape = VoxelShapes.union(cube, y1, y2, y3, y4);
        VoxelShape x_shape = VoxelShapes.union(cube, x1, x2, x3, x4);
        VoxelShape z_shape = VoxelShapes.union(cube, z1, z2, z3, z4);

        CUBE_SHAPE = cube;
        X_SHAPE = x_shape;
        Y_SHAPE = y_shape;
        Z_SHAPE = z_shape;
    }
}
