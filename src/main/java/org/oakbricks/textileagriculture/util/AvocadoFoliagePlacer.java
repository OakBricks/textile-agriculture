package org.oakbricks.textileagriculture.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import org.oakbricks.textileagriculture.TextileAgricultureMain;

import java.util.Random;
import java.util.function.BiConsumer;

public class AvocadoFoliagePlacer extends FoliagePlacer {
    // For the foliageHeight we use a codec generated by IntProvider.createValidatingCodec
    // As the method's arguments, we pass in the minimum and maximum value of the IntProvider
    // To add more fields into your TrunkPlacer/FoliagePlacer/TreeDecorator etc., use multiple .and calls
    //
    // For an example of creating your own type of codec, see the IntProvider.createValidatingCodec method's source
    public static final Codec<AvocadoFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillFoliagePlacerFields(instance)
                    .and(IntProvider.createValidatingCodec(1, 512).fieldOf("foliage_height").forGetter(AvocadoFoliagePlacer::getFoliageHeight)
                            .apply(instance, AvocadoFoliagePlacer::new));

    private final IntProvider foliageHeight;

    public AvocadoFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider foliageHeight) {
        super(radius, offset);

        this.foliageHeight = foliageHeight;
    }

    public IntProvider getFoliageHeight() {
        return this.foliageHeight;
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return TextileAgricultureMain.AVOCADO_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
        BlockPos.Mutable center = treeNode.getCenter().mutableCopy();

        for (
            // Start from X: center - radius
                Vec3i vec = center.subtract(new Vec3i(radius, 0, 0));
            // End in X: center + radius
                vec.compareTo(center.add(new Vec3i(radius, 0, 0))) == 0;
            // Move by 1 each time
                vec.add(1, 0, 0)) {
            this.placeFoliageBlock(world, replacer, random, config, new BlockPos(vec));
        }

        for (Vec3i vec = center.subtract(new Vec3i(0, radius, 0)); vec.compareTo(center.add(new Vec3i(0, radius, 0))) == 0; vec.add(0, 1, 0)) {
            this.placeFoliageBlock(world, replacer, random, config, new BlockPos(vec));
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        // Just pick the random height using the IntProvider
        return foliageHeight.get(random);
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        // Our FoliagePlacer doesn't set any restrictions on leaves
        return false;
    }
}
