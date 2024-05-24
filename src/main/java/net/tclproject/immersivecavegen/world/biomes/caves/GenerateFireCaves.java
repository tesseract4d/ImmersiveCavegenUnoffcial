package net.tclproject.immersivecavegen.world.biomes.caves;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tclproject.immersivecavegen.WGConfig;
import net.tclproject.immersivecavegen.world.CavesDecorator;
import net.tclproject.immersivecavegen.world.GenerateNetherStalactites;

public class GenerateFireCaves extends WorldGenerator {
  public boolean generate(World world, Random random, int x, int y, int z) {
    switch (CavesDecorator.weightedChoice(WGConfig.fcaveslist)) {
      case 1:
        (new GenerateNetherStalactites()).generate(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z), 8);
        generateLiliesOther(world, random, x, y, z);
        return true;
      case 2:
        CavesDecorator.generateLavashrooms(world, random, x, y, z);
        generateLiliesOther(world, random, x, y, z);
        return true;
      case 3:
        CavesDecorator.generateScorchedLavaStone(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
        generateLiliesOther(world, random, x, y, z);
        return true;
      case 4:
        CavesDecorator.generateSkulls(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
        generateLiliesOther(world, random, x, y, z);
        return true;
    } 
    return false;
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
