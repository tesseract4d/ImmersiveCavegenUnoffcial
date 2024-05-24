package net.tclproject.immersivecavegen.fixes;

import cpw.mods.fml.common.eventhandler.Event;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.tclproject.immersivecavegen.ImmersiveCavegen;
import net.tclproject.immersivecavegen.WGConfig;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;

public class MysteriumPatchesFixesOld {
  protected static Random noiseGen = new Random();

  protected static Random b = new Random();

  protected static float[] ravineData = new float[128];

  protected static World world;

  protected static long seedX;

  protected static long seedZ;

  protected static long worldSeed;

  protected static long chunkSeed;

  protected static int colossalCaveChance = 26 - WGConfig.giantCaveChance / 4;

  protected static boolean genCaves = false;

  protected static boolean genFillerCaves = false;

  @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
  public static boolean func_151539_a(MapGenBase instance, IChunkProvider p_151539_1_, World par2World, int chunkX, int chunkZ, Block[] par5ArrayOfBlock) {
    for (String str : WGConfig.dimblacklist) {
      if (world != null && String.valueOf(world.provider.dimensionId).equalsIgnoreCase(str))
        return false;
    }
    if (instance instanceof net.minecraft.world.gen.MapGenCaves) {
      world = par2World;
      worldSeed = world.getSeed();
      b.setSeed(worldSeed);
      noiseGen.setSeed(worldSeed);
      seedX = b.nextLong();
      seedZ = b.nextLong();
      for (int varChunkX = chunkX - 12; varChunkX <= chunkX + 12; varChunkX++) {
        for (int varChunkZ = chunkZ - 12; varChunkZ <= chunkZ + 12; varChunkZ++) {
          chunkSeed = varChunkX * seedX ^ varChunkZ * seedZ ^ worldSeed;
          boolean genRavines = validCaveLocation(varChunkX, varChunkZ);
          generateCaves(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
          int hj;
          for (hj = 1; hj < WGConfig.cavesSpawnMultiplier; ) {
            generateCaves(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
            hj++;
          }
          if (genRavines) {
            generateRavines(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
            for (hj = 1; hj < WGConfig.cavernsSpawnMultiplier; ) {
              generateRavines(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
              hj++;
            }
          }
          generateColossalCaves(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
          for (hj = 1; hj < WGConfig.oldBigCavesSpawnMultiplier; ) {
            generateColossalCaves(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
            hj++;
          }
        }
      }
      return true;
    }
    return false;
  }

  protected static void generateLargeCaveNode(long par1, int par3, int par4, Block[] par5ArrayOfBlock, double par6, double par8, double par10) {
    generateCaveNode(par1, par3, par4, par5ArrayOfBlock, par6, par8, par10, 1.0F + b.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D, 0);
  }

  protected static void generateCaveNode(long par1, int par3, int par4, Block[] par5ArrayOfBlock, double par6, double par8, double par10, float par12, float par13, float par14, int par15, int par16, double par17, int bigCave) {
    if (bigCave == 0) {
      Random r = new Random();
      if (r.nextInt(100) < WGConfig.giantCaveChance)
        bigCave = 3;
    }
    double var19 = (par3 * 16 + 8);
    double var21 = (par4 * 16 + 8);
    float var23 = 0.0F;
    float var24 = 0.0F;
    Random var25 = new Random(par1);
    float curviness1 = 0.1F;
    float curviness2 = 0.1F;
    if (par16 <= 0)
      par16 = 112 - var25.nextInt(28);
    int var27 = var25.nextInt(par16 / 2) + par16 / 4;
    double minWidth = 1.5D;
    if (bigCave != 1 && bigCave != 2) {
      if (bigCave == 3) {
        par17 = (1.0F - par12 / 100.0F);
        minWidth = 2.0D;
      }
    } else {
      if (bigCave == 1) {
        par16 = 112 + var25.nextInt(var25.nextInt(337) + 1);
        if (par16 > 336)
          par16 = 336;
        if (var25.nextBoolean()) {
          float multiplier = var25.nextFloat() * par16 / 96.0F + (672 - par16) / 672.0F;
          if (multiplier > 1.0F)
            par12 *= multiplier;
        } else {
          par12 *= var25.nextFloat() + 1.0F;
        }
      } else {
        par16 = 224 + var25.nextInt(113);
      }
      var27 = var25.nextInt(par16 / 4) + par16 / 2;
      curviness1 = par16 / 3360.0F + 0.05F;
      if (curviness1 < 0.1F)
        curviness2 = curviness1;
      par17 = (1.0F - par12 / 100.0F);
      minWidth = 2.5D;
    }
    boolean var54 = false;
    if (par15 == -1) {
      par15 = par16 / 2;
      var54 = true;
    }
    int skipCount = 999;
    for (boolean var28 = (var25.nextInt(6) == 0); par15 < par16; par15++) {
      double var29 = minWidth + (MathHelper.sin(par15 * 3.141593F / par16) * par12);
      double var31 = var29 * par17;
      float var33 = MathHelper.cos(par14);
      float var34 = MathHelper.sin(par14);
      par6 += (MathHelper.cos(par13) * var33);
      par8 += var34;
      par10 += (MathHelper.sin(par13) * var33);
      if (var28) {
        par14 *= 0.92F;
      } else {
        par14 *= 0.7F;
      }
      par14 += var24 * curviness2;
      par13 += var23 * curviness1;
      var24 *= 0.9F;
      var23 *= 0.75F;
      var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
      var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;
      if (!var54 && bigCave < 3 && par15 == var27 && par12 > 1.0F && par16 > 0) {
        if (bigCave == 0) {
          generateCaveNode(var25.nextLong(), par3, par4, par5ArrayOfBlock, par6, par8, par10, var25.nextFloat() * 0.5F + 0.5F, par13 - 1.5707964F, par14 / 3.0F, par15, par16, 1.0D, 0);
          generateCaveNode(var25.nextLong(), par3, par4, par5ArrayOfBlock, par6, par8, par10, var25.nextFloat() * 0.5F + 0.5F, par13 + 1.5707964F, par14 / 3.0F, par15, par16, 1.0D, 0);
          return;
        }
        generateCaveNode(var25.nextLong(), par3, par4, par5ArrayOfBlock, par6, par8, par10, var25.nextFloat() * par12 / 3.0F + par12 / 3.0F, par13 - 1.5707964F, par14 / 3.0F, par15, par16, 1.0D, 3);
        generateCaveNode(var25.nextLong(), par3, par4, par5ArrayOfBlock, par6, par8, par10, var25.nextFloat() * par12 / 3.0F + par12 / 3.0F, par13 + 1.5707964F, par14 / 3.0F, par15, par16, 1.0D, 3);
        return;
      }
      if (!var54 && var25.nextInt(4) != 0 && skipCount <= (int)var29 / 2) {
        skipCount++;
      } else {
        double var35 = par6 - var19;
        double var37 = par10 - var21;
        double var39 = (par16 - par15) + (par12 + 18.0F);
        if (var35 * var35 + var37 * var37 > var39 * var39)
          return;
        skipCount = 0;
        double var29_2 = var29 * 2.0D;
        if (par6 >= var19 - 16.0D - var29_2 && par10 >= var21 - 16.0D - var29_2 && par6 <= var19 + 16.0D + var29_2 && par10 <= var21 + 16.0D + var29_2) {
          int var55 = MathHelper.floor_double(par6 - var29) - par3 * 16 - 1;
          int var36 = MathHelper.floor_double(par6 + var29) - par3 * 16 + 1;
          int var57 = MathHelper.floor_double(par8 - var31) - 1;
          int var38 = MathHelper.floor_double(par8 + var31) + 1;
          int var56 = MathHelper.floor_double(par10 - var29) - par4 * 16 - 1;
          int var40 = MathHelper.floor_double(par10 + var29) - par4 * 16 + 1;
          if (var55 < 0)
            var55 = 0;
          if (var36 > 16)
            var36 = 16;
          if (var57 < 0)
            var57 = 0;
          if (var38 > 126)
            var38 = 126;
          if (var56 < 0)
            var56 = 0;
          if (var40 > 16)
            var40 = 16;
          double noiseMultiplier = 0.55D / (var29_2 - 2.0D);
          if (noiseMultiplier > 0.3D)
            noiseMultiplier = 0.3D;
          for (int var42 = var55; var42 < var36; var42++) {
            double var59 = ((var42 + par3 * 16) + 0.5D - par6) / var29;
            var59 *= var59;
            for (int var45 = var56; var45 < var40; var45++) {
              double var46 = ((var45 + par4 * 16) + 0.5D - par10) / var29;
              var46 = var46 * var46 + var59;
              int var48 = var42 << 12 | var45 << 8 | var38;
              int yIndex = var38;
              int grassMycelium = 0;
              if (var46 < 1.0D)
                for (int var50 = var38 - 1; var50 >= var57; var50--) {
                  double var51 = (var50 + 0.5D - par8) / var31;
                  if (var51 > -0.7D && var51 * var51 + var46 + (noiseGen.nextInt(3) - 1) * noiseMultiplier < 1.0D && waterCheck(var42, yIndex, var45, par5ArrayOfBlock)) {
                    Block var53 = par5ArrayOfBlock[var48];
                    if (var53 != null && var53 != Blocks.bedrock) {
                      int biome = (world.getBiomeGenForCoordsBody(var42 + par3 * 16, var45 + par4 * 16)).biomeID;
                      if (var50 < 60 || biome != 16) {
                        if (var53 == Blocks.grass)
                          grassMycelium = 1;
                        if (var53 == Blocks.mycelium)
                          grassMycelium = 2;
                        if (var50 < 10) {
                          par5ArrayOfBlock[var48] = Blocks.lava;
                        } else {
                          par5ArrayOfBlock[var48] = null;
                          if (grassMycelium > 0 && par5ArrayOfBlock[var48 - 1] == Blocks.dirt) {
                            if (grassMycelium == 1)
                              par5ArrayOfBlock[var48 - 1] = (Block)Blocks.grass;
                            if (grassMycelium == 2)
                              par5ArrayOfBlock[var48 - 1] = (Block)Blocks.mycelium;
                          }
                          Block fallingBlock = par5ArrayOfBlock[var48 + 1];
                          if (fallingBlock != Blocks.sand) {
                            if (fallingBlock == Blocks.gravel)
                              par5ArrayOfBlock[var48 + 1] = Blocks.stone;
                          } else if ((biome < 36 || biome > 39) && (biome < 164 || biome > 167)) {
                            par5ArrayOfBlock[var48 + 1] = Blocks.sandstone;
                          } else {
                            par5ArrayOfBlock[var48 + 1] = Blocks.stained_hardened_clay;
                          }
                        }
                      }
                    }
                  }
                  var48--;
                  yIndex--;
                }
            }
          }
          if (var54)
            break;
        }
      }
    }
  }

  protected static boolean validGiantCaveLocation(int varChunkX, int varChunkZ) {
    int chunkModX = varChunkX & 0xF;
    int chunkModZ = varChunkZ & 0xF;
    if ((chunkModX != 0 || chunkModZ != 0) && (chunkModX != 8 || chunkModZ != 8))
      return false;
    if (varChunkX * varChunkX + varChunkZ * varChunkZ <= 128)
      return false;
    b.setSeed(varChunkX * seedX ^ varChunkZ * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) <= colossalCaveChance / 2)
      return false;
    b.setSeed((varChunkX - 16) * seedX ^ varChunkZ * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX + 16) * seedX ^ varChunkZ * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed(varChunkX * seedX ^ (varChunkZ - 16) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed(varChunkX * seedX ^ (varChunkZ + 16) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX - 8) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX + 8) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX - 8) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX + 8) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX - 24) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX - 16) * seedX ^ (varChunkZ - 16) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX - 8) * seedX ^ (varChunkZ - 24) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX + 24) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX + 16) * seedX ^ (varChunkZ - 16) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX + 8) * seedX ^ (varChunkZ - 24) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX - 24) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX - 16) * seedX ^ (varChunkZ + 16) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX - 8) * seedX ^ (varChunkZ + 24) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX + 24) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX + 16) * seedX ^ (varChunkZ + 16) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed((varChunkX + 8) * seedX ^ (varChunkZ + 24) * seedZ ^ worldSeed);
    if (b.nextInt(colossalCaveChance) == 0)
      return false;
    b.setSeed(varChunkX * seedX ^ varChunkZ * seedZ ^ worldSeed);
    int tmp = b.nextInt(2) + b.nextInt(2) + b.nextInt(2);
    return true;
  }

  protected static boolean validCaveLocation(int chunkX, int chunkZ) {
    int caveCount = 0;
    genCaves = true;
    genFillerCaves = true;
    for (int cx = -6; cx <= 6; cx++) {
      for (int cz = -6; cz <= 6; cz++) {
        if (cx * cx + cz * cz <= 36 && genCaves) {
          if (validGiantCaveLocation(chunkX + cx, chunkZ + cz)) {
            genCaves = false;
            genFillerCaves = false;
            return false;
          }
          if (genFillerCaves) {
            b.setSeed((chunkX + cx) * seedX ^ (chunkZ + cz) * seedZ ^ worldSeed);
            int size = b.nextInt(b.nextInt(b.nextInt(40) + 1) + 1);
            if (b.nextInt(15) == 0)
              caveCount += size;
            if (caveCount > 1)
              genFillerCaves = false;
          }
        }
      }
    }
    return true;
  }

  protected static void generateRavine(long par1, int par3, int par4, Block[] par5ArrayOfBlock, double par6, double par8, double par10, float par12, float par13, float par14, double height, int bigRavine) {
    Random var19 = new Random(par1);
    noiseGen.setSeed(par1);
    double var20 = (par3 * 16 + 8);
    double var22 = (par4 * 16 + 8);
    float var24 = 0.0F;
    float var25 = 0.0F;
    float curviness = 0.05F;
    int par16 = 112 - var19.nextInt(28);
    if (bigRavine > 0) {
      par16 = 112 + noiseGen.nextInt(noiseGen.nextInt(225) + 1);
      par12 *= noiseGen.nextFloat() * noiseGen.nextFloat() * 1.5F + 1.0F;
      if (bigRavine == 2) {
        par16 += 100 + noiseGen.nextInt(61);
        if (par16 > 336)
          par16 = 336;
        par12 += 3.0F + noiseGen.nextFloat() * 2.0F;
        if (par12 > 15.0F)
          par12 = 15.0F;
      }
      curviness = par16 / 2240.0F;
    }
    float var27 = 1.0F;
    int skipCount;
    for (skipCount = 0; skipCount < 128; skipCount++) {
      if (skipCount == 0 || var19.nextInt(3) == 0)
        var27 += var19.nextFloat() * var19.nextFloat();
    }
    skipCount = 5;
    float ravineDataMultiplier = 1.1F - (par12 - 2.0F) * 0.07F;
    if (ravineDataMultiplier < 0.6F)
      ravineDataMultiplier = 0.6F;
    int par15;
    for (par15 = 0; par15 < 128; par15++) {
      skipCount++;
      if (skipCount > 1 && (skipCount > 3 || noiseGen.nextInt(3) == 0)) {
        skipCount = 0;
        var27 = (1.0F + noiseGen.nextFloat() * noiseGen.nextFloat() * ravineDataMultiplier) * (0.95F + noiseGen.nextInt(2) * 0.1F);
      }
      ravineData[par15] = var27 * var27;
    }
    skipCount = 999;
    for (par15 = 0; par15 < par16; par15++) {
      double var53 = 1.5D + (MathHelper.sin(par15 * 3.141593F / par16) * par12);
      if (bigRavine > 0) {
        height = 3.416667D - var53 / 18.0D;
        if (height > 3.0D)
          height = 3.0D;
      }
      double var30 = var53 * height;
      var53 *= var19.nextFloat() * 0.25D + 0.75D;
      var30 *= var19.nextFloat() * 0.25D + 0.75D;
      float var32 = MathHelper.cos(par14);
      float var33 = MathHelper.sin(par14);
      par6 += (MathHelper.cos(par13) * var32);
      par8 += var33;
      par10 += (MathHelper.sin(par13) * var32);
      par14 *= 0.7F;
      par14 += var25 * curviness;
      par13 += var24 * curviness;
      var25 *= 0.8F;
      var24 *= 0.5F;
      var25 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 2.0F;
      var24 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 4.0F;
      if (var19.nextInt(4) != 0 && skipCount < (int)var53 / 2) {
        skipCount++;
      } else {
        double var34 = par6 - var20;
        double var36 = par10 - var22;
        double var38 = (par16 - par15) + (par12 + 18.0F);
        if (var34 * var34 + var36 * var36 > var38 * var38)
          return;
        skipCount = 0;
        double noiseMultiplier = 0.3333333D / (var53 - 0.5D);
        double var53_2 = var53 * 2.0D;
        if (par6 >= var20 - 16.0D - var53_2 && par10 >= var22 - 16.0D - var53_2 && par6 <= var20 + 16.0D + var53_2 && par10 <= var22 + 16.0D + var53_2) {
          int var56 = MathHelper.floor_double(par6 - var53) - par3 * 16 - 1;
          int var35 = MathHelper.floor_double(par6 + var53) - par3 * 16 + 1;
          int var55 = MathHelper.floor_double(par8 - var30) - 1;
          int var37 = MathHelper.floor_double(par8 + var30) + 1;
          int var57 = MathHelper.floor_double(par10 - var53) - par4 * 16 - 1;
          int var39 = MathHelper.floor_double(par10 + var53) - par4 * 16 + 1;
          if (var56 < 0)
            var56 = 0;
          if (var35 > 16)
            var35 = 16;
          if (var55 < 0)
            var55 = 0;
          if (var37 > 120)
            var37 = 120;
          if (var57 < 0)
            var57 = 0;
          if (var39 > 16)
            var39 = 16;
          for (int var41 = var56; var41 < var35; var41++) {
            double var59 = ((var41 + par3 * 16) + 0.5D - par6) / var53;
            var59 *= var59;
            for (int var44 = var57; var44 < var39; var44++) {
              double var45 = ((var44 + par4 * 16) + 0.5D - par10) / var53;
              var45 = var45 * var45 + var59;
              int var47 = var41 << 12 | var44 << 8 | var37;
              int yIndex = var37;
              int grassMycelium = 0;
              if (var45 < 1.0D)
                for (int var49 = var37 - 1; var49 >= var55; var49--) {
                  double var50 = (var49 + 0.5D - par8) / var30;
                  if (var45 * ravineData[var49] + var50 * var50 / 6.0D + (noiseGen.nextInt(3) - 1) * noiseMultiplier < 1.0D && waterCheck(var41, yIndex, var44, par5ArrayOfBlock)) {
                    Block var52 = par5ArrayOfBlock[var47];
                    if (var52 != null && var52 != Blocks.bedrock) {
                      int biome = (world.getBiomeGenForCoordsBody(var41 + par3 * 16, var44 + par4 * 16)).biomeID;
                      if (var50 < 60.0D || biome != 16) {
                        if (var52 == Blocks.grass)
                          grassMycelium = 1;
                        if (var52 == Blocks.mycelium)
                          grassMycelium = 2;
                        if (var49 < 10) {
                          par5ArrayOfBlock[var47] = Blocks.lava;
                        } else {
                          par5ArrayOfBlock[var47] = null;
                          if (grassMycelium > 0 && par5ArrayOfBlock[var47 - 1] == Blocks.dirt) {
                            if (grassMycelium == 1)
                              par5ArrayOfBlock[var47 - 1] = (Block)Blocks.grass;
                            if (grassMycelium == 2)
                              par5ArrayOfBlock[var47 - 1] = (Block)Blocks.mycelium;
                          }
                          Block fallingBlock = par5ArrayOfBlock[var47 + 1];
                          if (fallingBlock != Blocks.sand) {
                            if (fallingBlock == Blocks.gravel)
                              par5ArrayOfBlock[var47 + 1] = Blocks.dirt;
                          } else if ((biome < 36 || biome > 39) && (biome < 164 || biome > 167)) {
                            par5ArrayOfBlock[var47 + 1] = Blocks.sandstone;
                          } else {
                            par5ArrayOfBlock[var47 + 1] = Blocks.stained_hardened_clay;
                          }
                        }
                      }
                    }
                  }
                  var47--;
                  yIndex--;
                }
            }
          }
        }
      }
    }
  }

  @Fix(returnSetting = EnumReturnSetting.ON_TRUE, booleanAlwaysReturned = false)
  public static boolean generate(WorldGenLiquids l, World world, Random random, int x, int y, int z) {
    boolean type = (l.field_150521_a.getMaterial() == Material.water);
    if (WGConfig.disableSourceWater && type && (!WGConfig.disableSourceUnderground || y < 64))
      return true;
    if (WGConfig.disableSourceLava && !type && (!WGConfig.disableSourceUnderground || y < 64))
      return true;
    return false;
  }

  private static Random newRand = new Random();

  public static MapGenStronghold strongholdGenerator = new MapGenStronghold();

  public static MapGenVillage villageGenerator = new MapGenVillage();

  public static MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();

  public static MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();

  @Fix(returnSetting = EnumReturnSetting.ALWAYS)
  public static void populate(ChunkProviderGenerate g, IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
    int k = p_73153_2_ * 16;
    int l = p_73153_3_ * 16;
    boolean flag = false;
    WorldServer worldServer = DimensionManager.getWorld(0);
    if (worldServer != null) {
      BlockFalling.fallInstantly = true;
      BiomeGenBase biomegenbase = worldServer.getBiomeGenForCoordsBody(k + 16, l + 16);
      newRand.setSeed(worldServer.getSeed());
      long i1 = newRand.nextLong() / 2L * 2L + 1L;
      long j1 = newRand.nextLong() / 2L * 2L + 1L;
      newRand.setSeed(p_73153_2_ * i1 + p_73153_3_ * j1 ^ worldServer.getSeed());
      MinecraftForge.EVENT_BUS.post((Event)new PopulateChunkEvent.Pre(p_73153_1_, (World)worldServer, newRand, p_73153_2_, p_73153_3_, flag));
      if (worldServer.getWorldInfo().isMapFeaturesEnabled()) {
        if (WGConfig.enableMineshaftSpawn)
          mineshaftGenerator.generateStructuresInChunk((World)worldServer, newRand, p_73153_2_, p_73153_3_);
        flag = WGConfig.enableVillageSpawn ? villageGenerator.generateStructuresInChunk((World)worldServer, newRand, p_73153_2_, p_73153_3_) : false;
        if (WGConfig.enableStrongholdSpawn)
          strongholdGenerator.generateStructuresInChunk((World)worldServer, newRand, p_73153_2_, p_73153_3_);
        if (!WGConfig.enableDesolateSpawn)
          scatteredFeatureGenerator.generateStructuresInChunk((World)worldServer, newRand, p_73153_2_, p_73153_3_);
      }
      if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && !flag && newRand.nextInt(WGConfig.waterLakesChance) == 0 &&
        TerrainGen.populate(p_73153_1_, (World)worldServer, newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAKE)) {
        int i = k + newRand.nextInt(16) + 8;
        int l1 = newRand.nextInt(256);
        int i2 = l + newRand.nextInt(16) + 8;
        (new WorldGenLakes(Blocks.water)).generate((World)worldServer, newRand, i, l1, i2);
        if (newRand.nextInt(WGConfig.waterLakesChance) == 0) {
          i = k + newRand.nextInt(16) + 8;
          l1 = newRand.nextInt(256);
          i2 = l + newRand.nextInt(16) + 8;
          (new WorldGenLakes(Blocks.water)).generate((World)worldServer, newRand, i, l1, i2);
        }
        if (newRand.nextInt(WGConfig.waterLakesChance) == 0) {
          i = k + newRand.nextInt(16) + 8;
          l1 = newRand.nextInt(256);
          i2 = l + newRand.nextInt(16) + 8;
          (new WorldGenLakes(Blocks.water)).generate((World)worldServer, newRand, i, l1, i2);
        }
      }
      if (TerrainGen.populate(p_73153_1_, (World)worldServer, newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAVA) && !flag) {
        int i = k + newRand.nextInt(16) + 8;
        int l1 = newRand.nextInt(newRand.nextInt(248) + 8);
        int i2 = l + newRand.nextInt(16) + 8;
        if (l1 < 63 || newRand.nextInt(WGConfig.lavaLakesChance) == 0) {
          (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, newRand, i, l1, i2);
        } else if (newRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
          (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, newRand, i, l1, i2);
        }
        i = k + newRand.nextInt(16) + 8;
        l1 = newRand.nextInt(newRand.nextInt(248) + 8);
        i2 = l + newRand.nextInt(16) + 8;
        if (l1 < 63 || newRand.nextInt(WGConfig.lavaLakesChance) == 0) {
          (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, newRand, i, l1, i2);
        } else if (newRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
          (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, newRand, i, l1, i2);
        }
        i = k + newRand.nextInt(16) + 8;
        l1 = newRand.nextInt(newRand.nextInt(248) + 8);
        i2 = l + newRand.nextInt(16) + 8;
        if (l1 < 63 || newRand.nextInt(WGConfig.lavaLakesChance) == 0) {
          (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, newRand, i, l1, i2);
        } else if (newRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
          (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, newRand, i, l1, i2);
        }
      }
      if (TerrainGen.populate(p_73153_1_, (World)worldServer, newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAVA) && newRand.nextInt(WGConfig.undergWaterLakesChance) == 0) {
        int i = k + newRand.nextInt(16) + 8;
        int l1 = newRand.nextInt(newRand.nextInt(248) + 8);
        int i2 = l + newRand.nextInt(16) + 8;
        if (l1 < 63)
          (new WorldGenLakes(Blocks.water)).generate((World)worldServer, newRand, i, l1, i2);
        if (newRand.nextInt(WGConfig.undergWaterLakesChance) == 0) {
          i = k + newRand.nextInt(16) + 8;
          l1 = newRand.nextInt(newRand.nextInt(248) + 8);
          i2 = l + newRand.nextInt(16) + 8;
          if (l1 < 63)
            (new WorldGenLakes(Blocks.water)).generate((World)worldServer, newRand, i, l1, i2);
        }
        if (newRand.nextInt(WGConfig.undergWaterLakesChance) == 0) {
          i = k + newRand.nextInt(16) + 8;
          l1 = newRand.nextInt(newRand.nextInt(248) + 8);
          i2 = l + newRand.nextInt(16) + 8;
          if (l1 < 63)
            (new WorldGenLakes(Blocks.water)).generate((World)worldServer, newRand, i, l1, i2);
        }
      }
      boolean doGen = TerrainGen.populate(p_73153_1_, (World)worldServer, newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.DUNGEON);
      int k1;
      for (k1 = 0; doGen && k1 < 8; k1++) {
        int l1 = k + newRand.nextInt(16) + 8;
        int i2 = newRand.nextInt(256);
        int j2 = l + newRand.nextInt(16) + 8;
        if (WGConfig.enableDungeonSpawn)
          (new WorldGenDungeons()).generate((World)worldServer, newRand, l1, i2, j2);
      }
      biomegenbase.decorate((World)worldServer, newRand, k, l);
      if (TerrainGen.populate(p_73153_1_, (World)worldServer, newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.ANIMALS))
        SpawnerAnimals.performWorldGenSpawning((World)worldServer, biomegenbase, k + 8, l + 8, 16, 16, newRand);
      k += 8;
      l += 8;
      doGen = TerrainGen.populate(p_73153_1_, (World)worldServer, newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.ICE);
      for (k1 = 0; doGen && k1 < 16; k1++) {
        for (int l1 = 0; l1 < 16; l1++) {
          int i2 = worldServer.getPrecipitationHeight(k + k1, l + l1);
          if (worldServer.isBlockFreezable(k1 + k, i2 - 1, l1 + l))
            worldServer.setBlock(k1 + k, i2 - 1, l1 + l, Blocks.ice, 0, 2);
          if (worldServer.func_147478_e(k1 + k, i2, l1 + l, true))
            worldServer.setBlock(k1 + k, i2, l1 + l, Blocks.snow_layer, 0, 2);
        }
      }
      MinecraftForge.EVENT_BUS.post((Event)new PopulateChunkEvent.Post(p_73153_1_, (World)worldServer, newRand, p_73153_2_, p_73153_3_, flag));
      BlockFalling.fallInstantly = false;
    }
  }

  protected static void generateColossalCaves(int varChunkX, int varChunkZ, int chunkX, int chunkZ, Block[] par6ArrayOfBlock) {
    if (validGiantCaveLocation(varChunkX, varChunkZ)) {
      int centerX = varChunkX * 16 + 8;
      int centerZ = varChunkZ * 16 + 8;
      int subCenterX = 0;
      int subCenterZ = 0;
      int caveType = b.nextInt(4);
      for (int caveSystemCount = 0; caveSystemCount < 8; caveSystemCount++) {
        switch (caveType) {
          case 0:
            subCenterX = centerX + b.nextInt(33) - 40;
            subCenterZ = centerZ + caveSystemCount * 15 + b.nextInt(17) - 84;
            break;
          case 1:
            subCenterX = centerX + caveSystemCount * 15 + b.nextInt(17) - 84;
            subCenterZ = centerZ + b.nextInt(33) - 40;
            break;
          case 2:
            subCenterX = centerX - caveSystemCount * 15 + b.nextInt(17) + 20;
            subCenterZ = centerZ + caveSystemCount * 15 + b.nextInt(17) - 84;
            break;
          case 3:
            subCenterX = centerX + caveSystemCount * 15 + b.nextInt(17) - 84;
            subCenterZ = centerZ + caveSystemCount * 15 + b.nextInt(17) - 84;
            break;
        }
        generateCaveSystem(20, subCenterX, subCenterZ, chunkX, chunkZ, par6ArrayOfBlock, 48);
        for (int i = 0; i < 5; i++)
          generateCaveNode(b.nextLong(), chunkX, chunkZ, par6ArrayOfBlock, (subCenterX + b.nextInt(48)), (b.nextInt(6) + 10), (subCenterZ + b.nextInt(48)), b.nextFloat() * b.nextFloat() * b.nextFloat() * 6.0F, b.nextFloat() * 6.283185F, (b.nextFloat() - 0.5F) / 4.0F, 0, 0, 1.0D, 0);
      }
    }
  }

  protected static void generateCaves(int par2, int par3, int par4, int par5, Block[] par6ArrayOfBlock) {
    b.setSeed(chunkSeed);
    int caveSize = b.nextInt(b.nextInt(b.nextInt(40) + 1) + 1);
    int genCave = b.nextInt(15);
    if (genCave == 0 && genCaves) {
      generateCaveSystem(caveSize, par2 * 16, par3 * 16, par4, par5, par6ArrayOfBlock, 16);
      if (caveSize > 4 && caveSize < 20) {
        int caveCount = b.nextInt(b.nextInt(b.nextInt(caveSize / 5 * 2 + 2) + 1) + 1);
        for (int cx = 0; cx < caveCount; cx++) {
          int cz = 1;
          if (b.nextInt(4) == 0)
            cz += b.nextInt(4);
          for (int LL = 0; LL < cz; LL++)
            generateCaveNode(b.nextLong(), par4, par5, par6ArrayOfBlock, (par2 * 16 + b.nextInt(16)), (b.nextInt(6) + 10), (par3 * 16 + b.nextInt(16)), b.nextFloat() * b.nextFloat() * b.nextFloat() * 6.0F, b.nextFloat() * 6.283185F, (b.nextFloat() - 0.5F) / 4.0F, 0, 0, 1.0D, 0);
        }
      }
      if (((par2 % 18 == 0 && par3 % 18 == 0) || (par2 % 18 == 9 && par3 % 18 == 9)) && par2 * par2 + par3 * par3 >= 1024) {
        generateCaveNode(b.nextLong(), par4, par5, par6ArrayOfBlock, (par2 * 16 + 8), (b.nextInt(11) + 20), (par3 * 16 + 8), b.nextFloat() * 12.0F + b.nextFloat() * 6.0F + 3.0F, b.nextFloat() * 6.283185F, (b.nextFloat() - 0.5F) / 4.0F, 0, 0, 1.0D, 2);
      } else if (caveSize > 4 && caveSize < 15 && b.nextBoolean()) {
        generateCaveNode(b.nextLong(), par4, par5, par6ArrayOfBlock, (par2 * 16 + 8), (b.nextInt(11) + 20), (par3 * 16 + 8), b.nextFloat() * b.nextFloat() * b.nextFloat() * 8.0F + 2.0F, b.nextFloat() * 6.283185F, (b.nextFloat() - 0.5F) / 4.0F, 0, 0, 1.0D, 1);
      }
    } else if (genCave == 5 || genCave == 10) {
      int caveCount = (world.getBiomeGenForCoordsBody(par2 * 16 + 8, par3 * 16 + 8)).biomeID;
      caveSize = 0;
      if (caveCount != 3 && caveCount != 34 && caveCount != 131 && caveCount != 162) {
        if ((caveCount < 36 || caveCount > 39) && (caveCount < 164 || caveCount > 167)) {
          if (caveCount == 17)
            caveSize = 5;
        } else {
          caveSize = 10;
        }
      } else {
        caveSize = 15;
      }
      if (!genCaves)
        caveSize /= 2;
      if (caveSize > 0) {
        int cx = b.nextInt(b.nextInt(caveSize) + 1);
        for (int cz = 0; cz < cx; cz++)
          generateCaveNode(b.nextLong(), par4, par5, par6ArrayOfBlock, (par2 * 16 + b.nextInt(16)), (b.nextInt(50) + 40), (par3 * 16 + b.nextInt(16)), b.nextFloat() * b.nextFloat() * b.nextFloat() * 6.0F, b.nextFloat() * 6.283185F, (b.nextFloat() - 0.5F) / 4.0F, 0, 0, 1.0D, 0);
      }
    }
    if (genFillerCaves) {
      int caveCount = 0;
      for (int cx = -1; cx <= 1; cx++) {
        for (int cz = -1; cz <= 1; cz++) {
          if (cx != 0 && cz != 0) {
            validCaveLocation(par2 + cx, par3 + cz);
            if (genFillerCaves)
              caveCount++;
          }
        }
      }
      if (caveCount == 0) {
        caveCount = 1;
      } else if (caveCount > 2) {
        caveCount = 8;
      }
      b.setSeed(chunkSeed);
      if (b.nextInt(caveCount) == 0) {
        generateCaveSystem(b.nextInt(b.nextInt(9) + 1) + 3, par2 * 16, par3 * 16, par4, par5, par6ArrayOfBlock, 16);
        if (b.nextBoolean())
          generateCaveNode(b.nextLong(), par4, par5, par6ArrayOfBlock, (par2 * 16 + b.nextInt(16)), (b.nextInt(6) + 10), (par3 * 16 + b.nextInt(16)), b.nextFloat() * b.nextFloat() * b.nextFloat() * 6.0F, b.nextFloat() * 6.283185F, (b.nextFloat() - 0.5F) / 4.0F, 0, 0, 1.0D, 0);
        if (b.nextInt(4) == 0)
          generateCaveNode(b.nextLong(), par4, par5, par6ArrayOfBlock, (par2 * 16 + 8), (b.nextInt(11) + 20), (par3 * 16 + 8), b.nextFloat() * b.nextFloat() * b.nextFloat() * 8.0F + 2.0F, b.nextFloat() * 6.283185F, (b.nextFloat() - 0.5F) / 4.0F, 0, 0, 1.0D, 1);
      }
    }
  }

  protected static void generateCaveSystem(int numberOfCaves, int par2, int par3, int par4, int par5, Block[] par6ArrayOfBlock, int spread) {
    for (int var8 = 0; var8 < numberOfCaves; var8++) {
      double var9 = (par2 + b.nextInt(spread));
      double var11 = b.nextInt(b.nextInt(120) + 8);
      double var13 = (par3 + b.nextInt(spread));
      int var15 = 1;
      if (b.nextInt(4) == 0) {
        generateLargeCaveNode(b.nextLong(), par4, par5, par6ArrayOfBlock, var9, var11, var13);
        var15 += b.nextInt(4);
      }
      for (int var16 = 0; var16 < var15; var16++) {
        float var17 = b.nextFloat() * 3.1415927F * 2.0F;
        float var18 = (b.nextFloat() - 0.5F) / 4.0F;
        float var19 = b.nextFloat() * 2.0F + b.nextFloat();
        if (b.nextInt(10) == 0)
          var19 *= b.nextFloat() * b.nextFloat() * 4.0F + 1.0F;
        generateCaveNode(b.nextLong(), par4, par5, par6ArrayOfBlock, var9, var11, var13, var19, var17, var18, 0, 0, 1.0D, 0);
      }
    }
  }

  protected static void generateRavines(int par2, int par3, int par4, int par5, Block[] par6ArrayOfBlock) {
    ImmersiveCavegen.rand.setSeed(chunkSeed);
    if (ImmersiveCavegen.rand.nextInt(50) == 0) {
      double var7 = (par2 * 16 + ImmersiveCavegen.rand.nextInt(16));
      double var9 = (ImmersiveCavegen.rand.nextInt(ImmersiveCavegen.rand.nextInt(40) + 8) + 20);
      double var11 = (par3 * 16 + ImmersiveCavegen.rand.nextInt(16));
      float var15 = ImmersiveCavegen.rand.nextFloat() * 3.1415927F * 2.0F;
      float var16 = (ImmersiveCavegen.rand.nextFloat() - 0.5F) / 4.0F;
      float var17 = (ImmersiveCavegen.rand.nextFloat() * 2.0F + ImmersiveCavegen.rand.nextFloat()) * 2.0F;
      long ravineSeed = ImmersiveCavegen.rand.nextLong();
      int bigRavine = 0;
      double height = 3.0D;
      if (ravineSeed % 50L == 0L && par2 * par2 + par3 * par3 >= 1024) {
        bigRavine = 2;
      } else if (ravineSeed % 8L == 0L) {
        bigRavine = 1;
      } else {
        int biome = (world.getBiomeGenForCoordsBody(par2 * 16 + 8, par3 * 16 + 8)).biomeID;
        if ((biome >= 36 && biome <= 39) || (biome >= 164 && biome <= 167)) {
          if (var9 < 40.0D)
            var9 += ImmersiveCavegen.rand.nextInt(16);
          height += (ImmersiveCavegen.rand.nextInt(2) + 1);
        }
      }
      generateRavine(ravineSeed, par4, par5, par6ArrayOfBlock, var7, var9, var11, var17, var15, var16, height, bigRavine);
    }
  }

  public static boolean waterCheck(int blockX, int blockY, int blockZ, Block[] blockData) {
    if (blockY >= 25 && blockY <= 62) {
      int x;
      for (x = blockX - 1; x <= blockX + 1; x++) {
        if (x >= 0 && x <= 15)
          for (int z = blockZ - 1; z <= blockZ + 1; z++) {
            if (z >= 0 && z <= 15) {
              int xyz = x << 12 | z << 8;
              for (int y = blockY - 1; y <= blockY + 1; y++) {
                if (blockData[xyz + y] == Blocks.water)
                  return false;
              }
            }
          }
      }
      for (x = blockZ - 1; x <= blockZ + 1; x++) {
        if (x >= 0 && x <= 15) {
          int xyz = blockX - 2;
          if (xyz >= 0 && blockData[xyz << 12 | x << 8 | blockY] == Blocks.water)
            return false;
          xyz = blockX + 2;
          if (xyz <= 15 && blockData[xyz << 12 | x << 8 | blockY] == Blocks.water)
            return false;
        }
      }
      for (x = blockX - 1; x <= blockX + 1; x++) {
        if (x >= 0 && x <= 15) {
          int xyz = blockZ - 2;
          if (xyz >= 0 && blockData[x << 12 | xyz << 8 | blockY] == Blocks.water)
            return false;
          xyz = blockZ + 2;
          if (xyz <= 15 && blockData[x << 12 | xyz << 8 | blockY] == Blocks.water)
            return false;
        }
      }
      if (blockData[blockX << 12 | blockZ << 8 | blockY - 2] == Blocks.water)
        return false;
      if (blockData[blockX << 12 | blockZ << 8 | blockY + 2] == Blocks.water)
        return false;
    }
    return true;
  }

  public static boolean validGiantCaveLocation(int varChunkX, int varChunkZ, long seedX, long seedZ, long worldSeed) {
    int chunkModX = varChunkX & 0xF;
    int chunkModZ = varChunkZ & 0xF;
    if ((chunkModX != 0 || chunkModZ != 0) && (chunkModX != 8 || chunkModZ != 8))
      return false;
    if (varChunkX * varChunkX + varChunkZ * varChunkZ <= 512)
      return false;
    ImmersiveCavegen.rand.setSeed(varChunkX * seedX ^ varChunkZ * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) != 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX - 16) * seedX ^ varChunkZ * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX + 16) * seedX ^ varChunkZ * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed(varChunkX * seedX ^ (varChunkZ - 16) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed(varChunkX * seedX ^ (varChunkZ + 16) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX - 8) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX + 8) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX - 8) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX + 8) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX - 24) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX - 16) * seedX ^ (varChunkZ - 16) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX - 8) * seedX ^ (varChunkZ - 24) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX + 24) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX + 16) * seedX ^ (varChunkZ - 16) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX + 8) * seedX ^ (varChunkZ - 24) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX - 24) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX - 16) * seedX ^ (varChunkZ + 16) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX - 8) * seedX ^ (varChunkZ + 24) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX + 24) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX + 16) * seedX ^ (varChunkZ + 16) * seedZ ^ worldSeed);
    if (ImmersiveCavegen.rand.nextInt(colossalCaveChance) == 0)
      return false;
    ImmersiveCavegen.rand.setSeed((varChunkX + 8) * seedX ^ (varChunkZ + 24) * seedZ ^ worldSeed);
    return (ImmersiveCavegen.rand.nextInt(colossalCaveChance) != 0);
  }

  public static boolean didSpawn(MapGenMineshaft instance, int chunkX, int chunkZ) {
    if (((chunkX + 2000000) % 14 != 0 || (chunkZ + 2000000) % 14 != 0) && ((chunkX + 2000000) % 14 != 7 || (chunkZ + 2000000) % 14 != 7))
      return false;
    long worldSeed = ((MapGenBase)instance).worldObj.getSeed();
    ImmersiveCavegen.rand.setSeed(worldSeed);
    long seedX = ImmersiveCavegen.rand.nextLong();
    long seedZ = ImmersiveCavegen.rand.nextLong();
    int caveCount = 0;
    for (int cx = -6; cx <= 6; cx++) {
      for (int cz = -6; cz <= 6; cz++) {
        if (cx * cx + cz * cz <= 36) {
          ImmersiveCavegen.rand.setSeed((chunkX + cx) * seedX ^ (chunkZ + cz) * seedZ ^ worldSeed);
          int size = ImmersiveCavegen.rand.nextInt(ImmersiveCavegen.rand.nextInt(ImmersiveCavegen.rand.nextInt(40) + 1) + 1);
          if (ImmersiveCavegen.rand.nextInt(15) == 0)
            caveCount += size;
          if (caveCount > 33 || validGiantCaveLocation(chunkX + cx, chunkZ + cz, seedX, seedZ, worldSeed))
            return false;
        }
      }
    }
    if (caveCount >= 10)
      return true;
    if (caveCount >= 5 && ((MapGenBase)instance).rand.nextBoolean())
      return true;
    return (((MapGenBase)instance).rand.nextInt(4) == 0);
  }

  @Fix(returnSetting = EnumReturnSetting.ON_TRUE, anotherMethodReturned = "didSpawn")
  public static boolean canSpawnStructureAtCoords(MapGenMineshaft instance, int chunkX, int chunkZ) {
    if (WGConfig.enableBetterMineshafts)
      return true;
    return false;
  }

  @Fix(returnSetting = EnumReturnSetting.ALWAYS)
  public static boolean generate(WorldGenSand instance, World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
    if (WGConfig.enableBetterSand) {
      if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_).getMaterial() != Material.water)
        return false;
      int var6 = p_76484_2_.nextInt(instance.radius - 2) + 2;
      byte var7 = 2;
      for (int var8 = p_76484_3_ - var6; var8 <= p_76484_3_ + var6; var8++) {
        for (int var9 = p_76484_5_ - var6; var9 <= p_76484_5_ + var6; var9++) {
          int var10 = var8 - p_76484_3_;
          int var11 = var9 - p_76484_5_;
          if (var10 * var10 + var11 * var11 <= var6 * var6)
            for (int var12 = p_76484_4_ - var7; var12 <= p_76484_4_ + var7; var12++) {
              Block var13 = p_76484_1_.getBlock(var8, var12, var9);
              if (var13 == Blocks.dirt || var13 == Blocks.grass)
                if (p_76484_1_.getBlock(var8, var12 - 1, var9) != Blocks.air) {
                  p_76484_1_.setBlock(var8, var12, var9, instance.field_150517_a, 0, 2);
                } else if (instance.field_150517_a == Blocks.sand) {
                  p_76484_1_.setBlock(var8, var12, var9, Blocks.sandstone, 0, 2);
                } else if (instance.field_150517_a == Blocks.gravel) {
                  p_76484_1_.setBlock(var8, var12, var9, Blocks.stone, 0, 2);
                } else {
                  p_76484_1_.setBlock(var8, var12, var9, instance.field_150517_a, 0, 2);
                }
            }
        }
      }
      return true;
    }
    if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_).getMaterial() != Material.water)
      return false;
    int l = p_76484_2_.nextInt(instance.radius - 2) + 2;
    byte b0 = 2;
    for (int i1 = p_76484_3_ - l; i1 <= p_76484_3_ + l; i1++) {
      for (int j1 = p_76484_5_ - l; j1 <= p_76484_5_ + l; j1++) {
        int k1 = i1 - p_76484_3_;
        int l1 = j1 - p_76484_5_;
        if (k1 * k1 + l1 * l1 <= l * l)
          for (int i2 = p_76484_4_ - b0; i2 <= p_76484_4_ + b0; i2++) {
            Block block = p_76484_1_.getBlock(i1, i2, j1);
            if (block == Blocks.dirt || block == Blocks.grass)
              p_76484_1_.setBlock(i1, i2, j1, instance.field_150517_a, 0, 2);
          }
      }
    }
    return true;
  }
}
