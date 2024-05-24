package net.tclproject.immersivecavegen.fixes;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.tclproject.immersivecavegen.WGConfig;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;

public class MysteriumPatchesFixesSand {
  private static Block sandBlock;

  private static Block solidBlock;

  private static int radius;

  @Fix(targetMethod = "<init>")
  public static void WorldGenSand(WorldGenSand s, Block p_i45462_1_, int p_i45462_2_) {
    sandBlock = p_i45462_1_;
    if (sandBlock == Blocks.sand) {
      solidBlock = Blocks.sandstone;
    } else if (sandBlock == Blocks.gravel) {
      solidBlock = Blocks.stone;
    } else {
      solidBlock = sandBlock;
    }
    radius = p_i45462_2_;
  }

  @Fix(returnSetting = EnumReturnSetting.ALWAYS)
  public static boolean generate(WorldGenSand s, World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
    for (String str : WGConfig.dimblacklist) {
      if (p_76484_1_ != null && String.valueOf(p_76484_1_.provider.dimensionId).equalsIgnoreCase(str))
        return false;
    }
    if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_).getMaterial() != Material.water)
      return false;
    int var6 = p_76484_2_.nextInt(radius - 2) + 2;
    byte var7 = 2;
    for (int var8 = p_76484_3_ - var6; var8 <= p_76484_3_ + var6; var8++) {
      for (int var9 = p_76484_5_ - var6; var9 <= p_76484_5_ + var6; var9++) {
        int var10 = var8 - p_76484_3_;
        int var11 = var9 - p_76484_5_;
        if (var10 * var10 + var11 * var11 <= var6 * var6)
          for (int var12 = p_76484_4_ - var7; var12 <= p_76484_4_ + var7; var12++) {
            Block var13 = p_76484_1_.getBlock(var8, var12, var9);
            if (var13 == Blocks.dirt || var13 == Blocks.grass)
              if (p_76484_1_.getBlock(var8, var12 - 1, var9) == Blocks.air) {
                if (var12 >= 62 && p_76484_1_.getBlock(var8, var12 - 2, var9) != Blocks.air) {
                  p_76484_1_.setBlock(var8, var12, var9, sandBlock, 0, 2);
                } else {
                  p_76484_1_.setBlock(var8, var12, var9, solidBlock, 0, 2);
                }
              } else {
                p_76484_1_.setBlock(var8, var12, var9, sandBlock, 0, 2);
              }
          }
      }
    }
    return true;
  }
}
