package net.tclproject.immersivecavegen.world.biomes.caves;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tclproject.immersivecavegen.WGConfig;
import net.tclproject.immersivecavegen.world.CavesDecorator;
import net.tclproject.immersivecavegen.world.GenerateStoneStalactite;

public final class GenerateIceCaves extends WorldGenerator {
  public boolean generate(World world, Random random, int x, int y, int z) {
    switch (CavesDecorator.weightedChoice(WGConfig.icaveslist)) {
      case 1:
        CavesDecorator.generateIceshrooms(world, random, x, y, z);
        generateLiliesOther(world, random, x, y, z);
        return true;
      case 2:
        if (world.getBlock(x, y + 1, z).isNormalCube())
          world.setBlock(x, y, z, Blocks.web, 0, 3); 
        generateLiliesOther(world, random, x, y, z);
        return true;
      case 3:
        CavesDecorator.generateIcicles(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
        generateLiliesOther(world, random, x, y, z);
        return true;
      case 4:
        CavesDecorator.generateSkulls(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
        generateLiliesOther(world, random, x, y, z);
        return true;
    } 
    (new GenerateStoneStalactite()).generate(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z), 8);
    generateLiliesOther(world, random, x, y, z);
    return true;
  }
  
  public static void generateLiliesOther(World world, Random random, int x, int y, int z) {
    if (WGConfig.liliesChance > random.nextFloat())
      if (WGConfig.glowLilies == 2) {
        CavesDecorator.generateGlowLily(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
      } else if (WGConfig.glowLilies == 3 || WGConfig.glowLilies == 1) {
        CavesDecorator.generateLily(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
      }  
  }
}
