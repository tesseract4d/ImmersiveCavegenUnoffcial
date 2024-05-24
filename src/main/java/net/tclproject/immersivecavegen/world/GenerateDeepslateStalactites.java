package net.tclproject.immersivecavegen.world;

import java.util.Random;
import net.minecraft.world.World;
import net.tclproject.immersivecavegen.blocks.BlockInit;

public class GenerateDeepslateStalactites extends GenerateStoneStalactite {
  public GenerateDeepslateStalactites() {
    super(BlockInit.deepslateStalactiteBlock);
  }
  
  public void generateBottom(World world, Random random, int x, int y, int z, int distance, int maxLength) {}
  
  public void generateTop(World world, Random random, int x, int y, int z, int distance, int maxLength) {}
}
