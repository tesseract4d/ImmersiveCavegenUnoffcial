package net.tclproject.immersivecavegen.world.biomes;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class MinableWorldGenerator extends WorldGenerator {
  private Block replacedWith;
  
  private int replaceWithBlockMetadata = 0;
  
  private int numberOfBlocks;
  
  private Block toBeReplaced;
  
  public MinableWorldGenerator(Block block1, int par2) {
    this.replacedWith = block1;
    this.numberOfBlocks = par2;
    this.toBeReplaced = Blocks.stone;
  }
  
  public MinableWorldGenerator(Block block1, int par2, Block block2) {
    this.replacedWith = block1;
    this.numberOfBlocks = par2;
    this.toBeReplaced = block2;
  }
  
  public boolean generate(World par1World, Random par2Random, int x, int y, int z) {
    float var6 = par2Random.nextFloat() * 3.1415927F;
    double var7 = ((x + 8) + MathHelper.sin(var6) * this.numberOfBlocks / 8.0F);
    double var9 = ((x + 8) - MathHelper.sin(var6) * this.numberOfBlocks / 8.0F);
    double var11 = ((z + 8) + MathHelper.cos(var6) * this.numberOfBlocks / 8.0F);
    double var13 = ((z + 8) - MathHelper.cos(var6) * this.numberOfBlocks / 8.0F);
    double var15 = (y + par2Random.nextInt(3) - 2);
    double var17 = (y + par2Random.nextInt(3) - 2);
    for (int var19 = 0; var19 <= this.numberOfBlocks; var19++) {
      double var20 = var7 + (var9 - var7) * var19 / this.numberOfBlocks;
      double var22 = var15 + (var17 - var15) * var19 / this.numberOfBlocks;
      double var24 = var11 + (var13 - var11) * var19 / this.numberOfBlocks;
      double var26 = par2Random.nextDouble() * this.numberOfBlocks / 16.0D;
      double var28 = (MathHelper.sin(var19 * 3.1415927F / this.numberOfBlocks) + 1.0F) * var26 + 1.0D;
      double var30 = (MathHelper.sin(var19 * 3.1415927F / this.numberOfBlocks) + 1.0F) * var26 + 1.0D;
      int var32 = MathHelper.floor_double(var20 - var28 / 2.0D);
      int var33 = MathHelper.floor_double(var22 - var30 / 2.0D);
      int var34 = MathHelper.floor_double(var24 - var28 / 2.0D);
      int var35 = MathHelper.floor_double(var20 + var28 / 2.0D);
      int var36 = MathHelper.floor_double(var22 + var30 / 2.0D);
      int var37 = MathHelper.floor_double(var24 + var28 / 2.0D);
      for (int var38 = var32; var38 <= var35; var38++) {
        double var39 = (var38 + 0.5D - var20) / var28 / 2.0D;
        if (var39 * var39 < 1.0D)
          for (int var41 = var33; var41 <= var36; var41++) {
            double var42 = (var41 + 0.5D - var22) / var30 / 2.0D;
            if (var39 * var39 + var42 * var42 < 1.0D)
              for (int var44 = var34; var44 <= var37; var44++) {
                double var45 = (var44 + 0.5D - var24) / var28 / 2.0D;
                Block block = par1World.getBlock(var38, var41, var44);
                if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0D && block != null && (block == this.toBeReplaced || (this.toBeReplaced == Blocks.dirt && block == Blocks.grass)))
                  if (par1World.isAirBlock(var38, var41 + 1, var44) && this.replacedWith == Blocks.dirt) {
                    par1World.setBlock(var38, var41, var44, (Block)Blocks.grass, this.replaceWithBlockMetadata, 3);
                  } else {
                    par1World.setBlock(var38, var41, var44, this.replacedWith, this.replaceWithBlockMetadata, 3);
                  }  
              }  
          }  
      } 
    } 
    return true;
  }
}
