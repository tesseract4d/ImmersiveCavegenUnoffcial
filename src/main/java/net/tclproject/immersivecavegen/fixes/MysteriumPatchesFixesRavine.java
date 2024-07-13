package net.tclproject.immersivecavegen.fixes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenRavine;
import net.tclproject.immersivecavegen.WGConfig;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;

import java.util.Random;

public class MysteriumPatchesFixesRavine {
  @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
  public static boolean func_151540_a(MapGenRavine instance, long p_151540_1_, int p_151540_3_, int p_151540_4_, Block[] p_151540_5_, double p_151540_6_, double p_151540_8_, double p_151540_10_, float p_151540_12_, float p_151540_13_, float p_151540_14_, int p_151540_15_, int p_151540_16_, double p_151540_17_) {
    for (String str : WGConfig.dimblacklist) {
      if (instance.worldObj != null && String.valueOf(instance.worldObj.provider.dimensionId).equalsIgnoreCase(str))
        return false;
    }
    Random var19 = new Random(p_151540_1_);
    double var20 = (p_151540_3_ * 16 + 8);
    double var22 = (p_151540_4_ * 16 + 8);
    float var24 = 0.0F;
    float var25 = 0.0F;
    if (p_151540_16_ <= 0) {
      int var53 = instance.range * 16 - 16;
      p_151540_16_ = var53 - var19.nextInt(var53 / 4);
    }
    boolean var61 = false;
    if (p_151540_15_ == -1) {
      p_151540_15_ = p_151540_16_ / 2;
      var61 = true;
    }
    float var27 = 1.0F;
    for (int var54 = 0; var54 < 256; var54++) {
      if (var54 == 0 || var19.nextInt(3) == 0)
        var27 = 1.0F + var19.nextFloat() * var19.nextFloat() * 1.0F;
      instance.field_75046_d[var54] = var27 * var27;
    }
    for (; p_151540_15_ < p_151540_16_; p_151540_15_++) {
      double var62 = 1.5D + (MathHelper.sin(p_151540_15_ * 3.1415927F / p_151540_16_) * p_151540_12_ * 1.0F);
      double var30 = var62 * p_151540_17_;
      var62 *= var19.nextFloat() * 0.25D + 0.75D;
      var30 *= var19.nextFloat() * 0.25D + 0.75D;
      float var32 = MathHelper.cos(p_151540_14_);
      float var33 = MathHelper.sin(p_151540_14_);
      p_151540_6_ += (MathHelper.cos(p_151540_13_) * var32);
      p_151540_8_ += var33;
      p_151540_10_ += (MathHelper.sin(p_151540_13_) * var32);
      p_151540_14_ *= 0.7F;
      p_151540_14_ += var25 * 0.05F;
      p_151540_13_ += var24 * 0.05F;
      var25 *= 0.8F;
      var24 *= 0.5F;
      var25 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 2.0F;
      var24 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 4.0F;
      if (var61 || var19.nextInt(4) != 0) {
        double var34 = p_151540_6_ - var20;
        double var36 = p_151540_10_ - var22;
        double var38 = (p_151540_16_ - p_151540_15_);
        double var40 = (p_151540_12_ + 2.0F + 16.0F);
        if (var34 * var34 + var36 * var36 - var38 * var38 > var40 * var40)
          return true;
        if (p_151540_6_ >= var20 - 16.0D - var62 * 2.0D && p_151540_10_ >= var22 - 16.0D - var62 * 2.0D && p_151540_6_ <= var20 + 16.0D + var62 * 2.0D && p_151540_10_ <= var22 + 16.0D + var62 * 2.0D) {
          int var55 = MathHelper.floor_double(p_151540_6_ - var62) - p_151540_3_ * 16 - 1;
          int var35 = MathHelper.floor_double(p_151540_6_ + var62) - p_151540_3_ * 16 + 1;
          int var56 = MathHelper.floor_double(p_151540_8_ - var30) - 1;
          int var37 = MathHelper.floor_double(p_151540_8_ + var30) + 1;
          int var57 = MathHelper.floor_double(p_151540_10_ - var62) - p_151540_4_ * 16 - 1;
          int var39 = MathHelper.floor_double(p_151540_10_ + var62) - p_151540_4_ * 16 + 1;
          if (var55 < 0)
            var55 = 0;
          if (var35 > 16)
            var35 = 16;
          if (var56 < 1)
            var56 = 1;
          if (var37 > 248)
            var37 = 248;
          if (var57 < 0)
            var57 = 0;
          if (var39 > 16)
            var39 = 16;
          boolean var58 = false;
          int var41;
          for (var41 = var55; !var58 && var41 < var35; var41++) {
            for (int var59 = var57; !var58 && var59 < var39; var59++) {
              for (int var43 = var37 + 1; !var58 && var43 >= var56 - 1; var43--) {
                int var44 = (var41 * 16 + var59) * 256 + var43;
                if (var43 >= 0 && var43 < 256) {
                  Block var60 = p_151540_5_[var44];
                  if (var60 == Blocks.flowing_water || var60 == Blocks.water)
                    var58 = true;
                  if (var43 != var56 - 1 && var41 != var55 && var41 != var35 - 1 && var59 != var57 && var59 != var39 - 1)
                    var43 = var56;
                }
              }
            }
          }
          if (!var58) {
            for (var41 = var55; var41 < var35; var41++) {
              double var63 = ((var41 + p_151540_3_ * 16) + 0.5D - p_151540_6_) / var62;
              for (int var44 = var57; var44 < var39; var44++) {
                double var64 = ((var44 + p_151540_4_ * 16) + 0.5D - p_151540_10_) / var62;
                int var47 = (var41 * 16 + var44) * 256 + var37;
                boolean var48 = false;
                if (var63 * var63 + var64 * var64 < 1.0D)
                  for (int var49 = var37 - 1; var49 >= var56; var49--) {
                    double var50 = (var49 + 0.5D - p_151540_8_) / var30;
                    if ((var63 * var63 + var64 * var64) * instance.field_75046_d[var49] + var50 * var50 / 6.0D < 1.0D) {
                      Block var52 = p_151540_5_[var47];
                      if (var52 == Blocks.grass)
                        var48 = true;
                      if (var52 == Blocks.stone || var52 == Blocks.dirt || var52 == Blocks.grass)
                        if (var49 < 10) {
                          p_151540_5_[var47] = Blocks.flowing_lava;
                        } else {
                          p_151540_5_[var47] = null;
                          if (var48 && p_151540_5_[var47 - 1] == Blocks.dirt)
                            p_151540_5_[var47 - 1] = (instance.worldObj.getBiomeGenForCoordsBody(var41 + p_151540_3_ * 16, var44 + p_151540_4_ * 16)).topBlock;
                        }
                    }
                    var47--;
                  }
              }
            }
            if (var61)
              break;
          }
        }
      }
    }
    return true;
  }

  @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
  public static boolean func_151538_a(MapGenRavine instance, World p_151538_1_, int p_151538_2_, int p_151538_3_, int p_151538_4_, int p_151538_5_, Block[] p_151538_6_) {
    for (String str : WGConfig.dimblacklist) {
      if (p_151538_1_ != null && String.valueOf(p_151538_1_.provider.dimensionId).equalsIgnoreCase(str))
        return false;
    }
    return true;
  }
}
