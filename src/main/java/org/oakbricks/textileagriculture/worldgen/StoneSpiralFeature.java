package org.oakbricks.textileagriculture.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.oakbricks.textileagriculture.blocks.berries.HollyBerryBush;
import org.oakbricks.textileagriculture.init.ModBlocks;
import org.oakbricks.textileagriculture.init.ModItems;

import java.util.Random;

public abstract class StoneSpiralFeature extends Feature<DefaultFeatureConfig> {
    public StoneSpiralFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos,
                            DefaultFeatureConfig config) {
        BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos);
        Direction offset = Direction.NORTH;

        for (int y = 1; y <= 15; y++) {
            offset = offset.rotateYClockwise();
            world.setBlockState(topPos.up(y).offset(offset), Blocks.STONE.getDefaultState(), 3);
        }

        return true;
    }
}
