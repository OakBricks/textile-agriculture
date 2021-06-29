package org.oakbricks.textileagriculture.util;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.oakbricks.textileagriculture.TextileAgricultureMain;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class AvocadoTrunkPlacer extends TrunkPlacer {
    // Use the fillTrunkPlacerFields to create our codec
    public static final Codec<AvocadoTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillTrunkPlacerFields(instance).apply(instance, AvocadoTrunkPlacer::new));

    public AvocadoTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return TextileAgricultureMain.AVOCADO_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
        // Set the ground beneath the trunk to dirt
        this.setToDirt(world, replacer, random, startPos.down(), config);

        // Iterate until the trunk height limit and place two blocks using the getAndSetState method from TrunkPlacer
        for (int i = 0; i < height; i++) {
            this.getAndSetState(world, replacer, random, startPos.up(i), config);
            this.getAndSetState(world, replacer, random, startPos.up(i).east().north(), config);
        }

        // We create two TreeNodes - one for the first trunk, and the other for the second
        // Put the highest block in the trunk as the center position for the FoliagePlacer to use
        return ImmutableList.of(new FoliagePlacer.TreeNode(startPos.up(height), 0, false),
                new FoliagePlacer.TreeNode(startPos.east().north().up(height), 0, false));
    }
}
