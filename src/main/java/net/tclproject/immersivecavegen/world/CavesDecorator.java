package net.tclproject.immersivecavegen.world;

import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ganymedes01.etfuturum.configuration.configs.ConfigWorld;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.tclproject.immersivecavegen.WGConfig;
import net.tclproject.immersivecavegen.blocks.BlockInit;
import net.tclproject.immersivecavegen.world.biomes.caves.*;
import net.tclproject.immersivecavegen.fixes.MysteriumPatchesFixesCave;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Random;

public class CavesDecorator {
  public static List freezable = Arrays.asList(new Block[] { Blocks.stone, Blocks.dirt, Blocks.gravel, (Block)Blocks.grass });

  public static int maxGenHeight = 80;

  public static int maxLength = 8;

  private static int timesPerChunck = 50;

  private static final GenerateJungleCaves jungleGen = new GenerateJungleCaves();

  private static final GenerateWaterCaves waterGen = new GenerateWaterCaves();

  private static final GenerateSandCaves sandGen = new GenerateSandCaves();

  private static final GeneratePlainCaves plainGen = new GeneratePlainCaves();

  private static final GenerateIceCaves iceGen = new GenerateIceCaves();

  private static final GenerateFireCaves fireGen = new GenerateFireCaves();

  private static final GenerateLivingCaves livingGen = new GenerateLivingCaves();

  @SubscribeEvent
  public void decorate(DecorateBiomeEvent.Post decorationEvent) {
    for (String str : WGConfig.dimblacklist) {
      if (decorationEvent.world != null && String.valueOf(decorationEvent.world.provider.dimensionId).equalsIgnoreCase(str))
        return;
    }
    generate(decorationEvent.rand, decorationEvent.chunkX + 8, decorationEvent.chunkZ + 8, decorationEvent.world);
  }

  public void generate(Random random, int blockX, int blockZ, World world) {
    if (MysteriumPatchesFixesCave.oceanAvg == -1) {
      int addedTimes = 1;
      int result = 0;
      int interResult = 0;
      for (int j = 0; j < 32; j++) {
        for (int k = 0; k < 32; k++) {
          int Ycoord, Xcoord = blockX + j;
          int Zcoord = blockZ + k;
          if (world.provider.isHellWorld && !WGConfig.netherCaves)
            return;
          if (!world.provider.isHellWorld) {
            for (Ycoord = Math.min(world.getHeightValue(Xcoord, Zcoord) - 1, random.nextInt(maxGenHeight)); Ycoord > 10 && (!GenerateStoneStalactite.blockWhiteList.contains(world.getBlock(Xcoord, Ycoord + 1, Zcoord)) || !world.isAirBlock(Xcoord, Ycoord, Zcoord)); Ycoord--);
          } else {
            Ycoord = Math.min(world.getHeightValue(Xcoord, Zcoord) - 1, random.nextInt(maxGenHeight));
          }
          if (Ycoord > 10) {
            interResult = getNumEmptyBlocks(world, Xcoord, Ycoord, Zcoord) / 2;
            result += interResult;
            if (interResult >= 3)
              addedTimes++;
          }
        }
      }
      int finalResult = result / addedTimes;
      finalResult = (finalResult >= 25) ? (25 - random.nextInt(10)) : ((finalResult <= 10) ? (10 + random.nextInt(7)) : finalResult);
      MysteriumPatchesFixesCave.oceanAvg = finalResult;
    }
    for (int i = 0; i < timesPerChunck; i++) {
      int Ycoord, Xcoord = blockX + random.nextInt(16);
      int Zcoord = blockZ + random.nextInt(16);
      if (world.provider.isHellWorld && !WGConfig.netherCaves)
        return;
      if (!world.provider.isHellWorld) {
        for (Ycoord = Math.min(world.getHeightValue(Xcoord, Zcoord) - 1, random.nextInt(maxGenHeight)); Ycoord > 10 && (!GenerateStoneStalactite.blockWhiteList.contains(world.getBlock(Xcoord, Ycoord + 1, Zcoord)) || !world.isAirBlock(Xcoord, Ycoord, Zcoord)); Ycoord--);
      } else {
        Ycoord = Math.min(world.getHeightValue(Xcoord, Zcoord) - 1, random.nextInt(maxGenHeight));
      }
      if (Ycoord > 10 &&
        random.nextFloat() < WGConfig.generationDensity) {
        BiomeGenBase biome = world.getBiomeGenForCoords(Xcoord, Zcoord);
        if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FROZEN) && WGConfig.icaves) {
          iceGen.generate(world, random, Xcoord, Ycoord, Zcoord);
        } else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SWAMP) && WGConfig.lcaves) {
          livingGen.generate(world, random, Xcoord, Ycoord, Zcoord);
        } else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.NETHER) || (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SPARSE) && WGConfig.fcaves)) {
          fireGen.generate(world, random, Xcoord, Ycoord, Zcoord);
        } else if (biome.temperature > 1.5F && biome.rainfall < 0.1F && WGConfig.scaves) {
          sandGen.generate(world, random, Xcoord, Ycoord, Zcoord);
        } else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MUSHROOM) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE) || (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.DENSE) && WGConfig.jcaves)) {
          jungleGen.generate(world, random, Xcoord, Ycoord, Zcoord);
        } else if ((!biome.isHighHumidity() && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.WATER)) || !WGConfig.wcaves) {
          plainGen.generate(world, random, Xcoord, Ycoord, Zcoord);
        } else {
          waterGen.generate(world, random, Xcoord, Ycoord, Zcoord);
        }
      }
    }
  }

  @Method(modid = "etfuturum")
  public static boolean isBlockDeepslate(World world, int x, int y, int z) {
    return (y < ConfigWorld.deepslateMaxY && ConfigWorld.deepslateGenerationMode == 0);
  }

  public static boolean shouldGenerateStone(World world, int origX, int origZ) {
    for (int p_72807_1_ = origX - 1; p_72807_1_ <= origX + 1; p_72807_1_++) {
      if (p_72807_1_ != origX) {
        int i = origZ;
        if (world.blockExists(p_72807_1_, 0, i)) {
          Chunk chunk = world.getChunkFromBlockCoords(p_72807_1_, i);
          try {
            BiomeGenBase b = chunk.getBiomeGenForWorldCoords(p_72807_1_ & 0xF, i & 0xF, world.provider.worldChunkMgr);
            int count = 0;
            for (String str : WGConfig.secondYLevelList) {
              if (!b.biomeName.equalsIgnoreCase(str))
                count++;
            }
            if (count == WGConfig.secondYLevelList.length)
              return true;
          } catch (Throwable throwable) {
            throwable.printStackTrace();
          }
        } else {
          BiomeGenBase b = world.provider.worldChunkMgr.getBiomeGenAt(p_72807_1_, i);
          int count = 0;
          for (String str : WGConfig.secondYLevelList) {
            if (!b.biomeName.equalsIgnoreCase(str))
              count++;
          }
          if (count == WGConfig.secondYLevelList.length)
            return true;
        }
      }
    }
    for (int p_72807_2_ = origZ - 1; p_72807_2_ <= origZ + 1; p_72807_2_++) {
      if (p_72807_2_ != origZ) {
        int i = origX;
        if (world.blockExists(i, 0, p_72807_2_)) {
          Chunk chunk = world.getChunkFromBlockCoords(i, p_72807_2_);
          try {
            BiomeGenBase b = chunk.getBiomeGenForWorldCoords(i & 0xF, p_72807_2_ & 0xF, world.provider.worldChunkMgr);
            int count = 0;
            for (String str : WGConfig.secondYLevelList) {
              if (!b.biomeName.equalsIgnoreCase(str))
                count++;
            }
            if (count == WGConfig.secondYLevelList.length)
              return true;
          } catch (Throwable throwable) {
            throwable.printStackTrace();
          }
        } else {
          BiomeGenBase b = world.provider.worldChunkMgr.getBiomeGenAt(i, p_72807_2_);
          int count = 0;
          for (String str : WGConfig.secondYLevelList) {
            if (!b.biomeName.equalsIgnoreCase(str))
              count++;
          }
          if (count == WGConfig.secondYLevelList.length)
            return true;
        }
      }
    }
    return false;
  }

  public static void generateFloodedCaves(World world, Random random, int x, int y, int z) {
    int vary = getNumEmptyBlocks(world, x, y, z);
    if (vary != 0)
      y = y - vary + 1;
    if (world.getBlock(x, y - 1, z).isNormalCube() && WGConfig.waterCaves) {
      world.setBlock(x, y, z, (Block)Blocks.flowing_water, 0, 3);
      return;
    }
  }

  public static void convertToSandType(World world, Random random, int x, int y, int z) {
    int height = random.nextInt(5) + 3;
    int length = random.nextInt(5) + 3;
    int width = random.nextInt(5) + 3;
    int newX = x - length / 2;
    int newY = y + height / 2;
    int newZ = z - width / 2;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < length; j++) {
        for (int k = 0; k < width; k++) {
          if (weightedChoice(0.7F, 0.3F, 0.0F, 0.0F, 0.0F, 0.0F) == 1) {
            IdentityHashMap<Object, Object> sandEquivalent = new IdentityHashMap<>(8);
            sandEquivalent.put(Blocks.stone, Blocks.sandstone);
            sandEquivalent.put(Blocks.dirt, Blocks.sand);
            sandEquivalent.put(Blocks.gravel, Blocks.sand);
            Block aux = (Block)sandEquivalent.get(world.getBlock(newX + j, newY - i, newZ + k));
            if (aux != null)
              world.setBlock(newX + j, newY - i, newZ + k, aux, 0, 2);
          }
        }
      }
    }
  }

  public static boolean generateGlowcaps(World world, Random random, int x, int y, int z) {
    int vary = getNumEmptyBlocks(world, x, y, z);
    if (vary != 0)
      y = y - vary + 1;
    if (world.getBlock(x, y - 1, z).isOpaqueCube()) {
      int glowcapNum = randomChoice(new int[] { 0, 1, 2, 3 });
      world.setBlock(x, y, z, BlockInit.cavePlantBlock, glowcapNum, 3);
      return true;
    }
    return false;
  }

  public static boolean generateSmallMushrooms(World world, Random random, int x, int y, int z) {
    int vary = getNumEmptyBlocks(world, x, y, z);
    if (vary != 0)
      y = y - vary + 1;
    if (world.getBlock(x, y - 1, z).isOpaqueCube()) {
      int glowcapNum = randomChoice(new int[] { 0, 1 });
      world.setBlock(x, y, z, (glowcapNum == 0) ? (Block)Blocks.brown_mushroom : (Block)Blocks.red_mushroom, 0, 2);
      return true;
    }
    return false;
  }

  public static boolean generateVegetation(World world, Random random, int x, int y, int z, int vary) {
    if (vary != 0)
      y = y - vary + 1;
    if (world.getBlock(x, y - 1, z).isOpaqueCube()) {
      Block b;
      int selection = randomChoice(0, 1, 2, 3);
        b = switch (selection) {
            case 0 -> Blocks.red_flower;
            case 1 -> Blocks.yellow_flower;
            case 2 -> Blocks.deadbush;
            default -> Blocks.tallgrass;
        };
      world.setBlock(x, y, z, b, 0, 2);
      if (random.nextFloat() > 0.5F || b != Blocks.deadbush)
        world.setBlock(x, y - 1, z, Blocks.dirt, 0, 2);
      if (WGConfig.livingCavesLush)
        convertToLushType(world, random, x, y, z);
      return true;
    }
    return false;
  }

  public static void convertToLushType(World world, Random random, int x, int y, int z) {
    int height = random.nextInt(5) + 3;
    int length = random.nextInt(10) + 3;
    int width = random.nextInt(10) + 3;
    int newX = x - length / 2;
    int newY = y + height / 2;
    int newZ = z - width / 2;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < length; j++) {
        for (int k = 0; k < width; k++) {
          if (weightedChoice(0.8F, 0.2F, 0.0F, 0.0F, 0.0F, 0.0F) == 1 && ((j != length - 1 && j != 0) || (k != width - 1 && k != 0))) {
            Block aux = world.getBlock(newX + j, newY - i, newZ + k);
            if (freezable.contains(aux))
              if (world.isAirBlock(newX + j, newY - i + 1, newZ + k)) {
                world.setBlock(newX + j, newY - i, newZ + k, (Block)Blocks.grass, 0, 2);
                if (random.nextFloat() > 0.66F) {
                  Block b;
                  int selection = randomChoice(0, 1, 2, 3, 4, 5);
                    b = switch (selection) {
                        case 0 -> Blocks.red_flower;
                        case 1 -> Blocks.yellow_flower;
                        case 2 -> Blocks.tallgrass;
                        default -> Blocks.tallgrass;
                    };
                  world.setBlock(newX + j, newY - i + 1, newZ + k, b, 1, 2);
                }
              } else {
                world.setBlock(newX + j, newY - i, newZ + k, Blocks.dirt, 0, 2);
              }
          }
        }
      }
    }
  }

  public static boolean generateTree(World world, Random random, int x, int y, int z, int vary) {
    if (vary != 0)
      y = y - vary + 1;
    if (WGConfig.livingCavesLush)
      convertToLushType(world, random, x, y, z);
    if (world.getBlock(x, y - 1, z).isOpaqueCube()) {
      if (random.nextFloat() > WGConfig.treeChance) {
        world.setBlock(x, y, z, Blocks.log, 0, 2);
        world.setBlock(x, y + 1, z, Blocks.log, 0, 2);
        if (random.nextFloat() > 0.5D) {
          world.setBlock(x, y + 2, z, Blocks.log, 0, 2);
          if (random.nextFloat() > 0.8D) {
            world.setBlock(x, y + 3, z, Blocks.log, 0, 2);
            if (random.nextFloat() > 0.95D)
              world.setBlock(x, y + 4, z, Blocks.log, 0, 2);
          }
        }
      } else {
        safeSetblock(world, x, y, z, Blocks.log, 0, 2);
        safeSetblock(world, x, y + 1, z, Blocks.log, 0, 2);
        surroundByLeaves(world, x, y + 1, z);
        if (random.nextFloat() > 0.5D) {
          safeSetblock(world, x, y + 2, z, Blocks.log, 0, 2);
          surroundByLeaves(world, x, y + 2, z);
          if (random.nextFloat() > 0.7D) {
            safeSetblock(world, x, y + 3, z, Blocks.log, 0, 2);
            surroundByLeaves(world, x, y + 3, z);
            if (random.nextFloat() > 0.85D) {
              safeSetblock(world, x, y + 4, z, Blocks.log, 0, 2);
              surroundByLeaves(world, x, y + 4, z);
              safeSetblock(world, x, y + 5, z, (Block)Blocks.leaves, 0, 2);
            } else {
              safeSetblock(world, x, y + 4, z, (Block)Blocks.leaves, 0, 2);
            }
          } else {
            safeSetblock(world, x, y + 3, z, (Block)Blocks.leaves, 0, 2);
          }
        } else {
          safeSetblock(world, x, y + 2, z, (Block)Blocks.leaves, 0, 2);
        }
      }
      return true;
    }
    return false;
  }

  public static void surroundByLeaves(World world, int x, int y, int z) {
    safeSetblock(world, x + 1, y, z, (Block)Blocks.leaves, 0, 2);
    safeSetblock(world, x - 1, y, z, (Block)Blocks.leaves, 0, 2);
    safeSetblock(world, x, y, z - 1, (Block)Blocks.leaves, 0, 2);
    safeSetblock(world, x, y, z + 1, (Block)Blocks.leaves, 0, 2);
  }

  public static void safeSetblock(World world, int x, int y, int z, Block b, int meta, int flags) {
    if (world.getBlock(x, y, z).canBeReplacedByLeaves((IBlockAccess)world, x, y, z))
      world.setBlock(x, y, z, b, meta, flags);
  }

  public static void generateIceshrooms(World world, Random random, int x, int y, int z) {
    int vary = getNumEmptyBlocks(world, x, y, z);
    if (vary != 0)
      y = y - vary + 1;
    if (!world.isAirBlock(x, y - 1, z)) {
      if (!world.getBlock(x, y, z).getMaterial().isLiquid()) {
        world.setBlock(x, y - 1, z, Blocks.ice, 0, 2);
        world.setBlock(x, y, z, BlockInit.cavePlantBlock, randomChoice(0, 1, 4, 5), 3);
      }
      convertToFrozenType(world, random, x, y, z);
    }
  }

  public static void generateLavashrooms(World world, Random random, int x, int y, int z) {
    int vary = getNumEmptyBlocks(world, x, y, z);
    if (vary != 0)
      y = y - vary + 1;
    if (!world.isAirBlock(x, y - 1, z)) {
      if (!world.getBlock(x, y, z).getMaterial().isLiquid()) {
        world.setBlock(x, y - 1, z, BlockInit.scorchedStone, 0, 2);
        world.setBlock(x, y, z, BlockInit.cavePlantBlock, randomChoice(0, 1, 6, 7), 3);
      }
      convertToLavaType(world, random, x, y, z);
    }
  }

  public static void generateIcicles(World world, Random random, int x, int y, int z, int distance) {
    world.setBlock(x, y + 1, z, Blocks.ice, 0, 2);
    world.setBlock(x, y, z, BlockInit.iceStalactiteBlock, randomChoice(0, 1, 2), 3);
    convertToFrozenType(world, random, x, y, z);
    int botY = y - distance + 1;
    if (distance != 0 && !world.getBlock(x, botY, z).getMaterial().isLiquid())
      convertToFrozenType(world, random, x, botY, z);
  }

  public static void generateMushrooms(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_, int distance) {
    if (p_76484_4_ > 56)
      return;
    int l = p_76484_2_.nextInt(2);
    boolean stone = (p_76484_2_.nextInt(5) > 2);
    if (distance != 0)
      p_76484_4_ = p_76484_4_ - distance + 1;
    if (!p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_)) {
      int i1 = p_76484_2_.nextInt(3) + 4;
      boolean flag = true;
      if (p_76484_4_ >= 1 && p_76484_4_ + i1 + 1 < 256) {
        for (int j1 = p_76484_4_; j1 <= p_76484_4_ + 1 + i1; j1++) {
          byte b0 = 3;
          if (j1 <= p_76484_4_ + 3)
            b0 = 0;
          for (int i = p_76484_3_ - b0; i <= p_76484_3_ + b0 && flag; i++) {
            for (int l1 = p_76484_5_ - b0; l1 <= p_76484_5_ + b0 && flag; l1++) {
              if (j1 >= 0 && j1 < 256) {
                Block block = p_76484_1_.getBlock(i, j1, l1);
                if (!block.isAir((IBlockAccess)p_76484_1_, i, j1, l1) && !block.isLeaves((IBlockAccess)p_76484_1_, i, j1, l1))
                  flag = true;
                if (block instanceof net.tclproject.immersivecavegen.blocks.BlockHugeGlowingMushroom || block instanceof net.tclproject.immersivecavegen.blocks.BlockHugeGlowingMushroom2)
                  flag = false;
              } else {
                flag = true;
              }
            }
          }
        }
        if (!flag)
          return;
        Block block1 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);
        if (block1 != Blocks.dirt && block1 != Blocks.stone && block1 != Blocks.mycelium)
          return;
        int k2 = p_76484_4_ + i1;
        if (l == 1)
          k2 = p_76484_4_ + i1 - 3;
        int k1;
        for (k1 = k2; k1 <= p_76484_4_ + i1; k1++) {
          int l1 = 1;
          if (k1 < p_76484_4_ + i1)
            l1++;
          if (l == 0)
            l1 = 3;
          for (int l2 = p_76484_3_ - l1; l2 <= p_76484_3_ + l1; l2++) {
            for (int i2 = p_76484_5_ - l1; i2 <= p_76484_5_ + l1; i2++) {
              int j2 = 5;
              if (l2 == p_76484_3_ - l1)
                j2--;
              if (l2 == p_76484_3_ + l1)
                j2++;
              if (i2 == p_76484_5_ - l1)
                j2 -= 3;
              if (i2 == p_76484_5_ + l1)
                j2 += 3;
              if (l == 0 || k1 < p_76484_4_ + i1) {
                if ((l2 == p_76484_3_ - l1 || l2 == p_76484_3_ + l1) && (i2 == p_76484_5_ - l1 || i2 == p_76484_5_ + l1))
                  continue;
                if (l2 == p_76484_3_ - l1 - 1 && i2 == p_76484_5_ - l1)
                  j2 = 1;
                if (l2 == p_76484_3_ - l1 && i2 == p_76484_5_ - l1 - 1)
                  j2 = 1;
                if (l2 == p_76484_3_ + l1 - 1 && i2 == p_76484_5_ - l1)
                  j2 = 3;
                if (l2 == p_76484_3_ + l1 && i2 == p_76484_5_ - l1 - 1)
                  j2 = 3;
                if (l2 == p_76484_3_ - l1 - 1 && i2 == p_76484_5_ + l1)
                  j2 = 7;
                if (l2 == p_76484_3_ - l1 && i2 == p_76484_5_ + l1 - 1)
                  j2 = 7;
                if (l2 == p_76484_3_ + l1 - 1 && i2 == p_76484_5_ + l1)
                  j2 = 9;
                if (l2 == p_76484_3_ + l1 && i2 == p_76484_5_ + l1 - 1)
                  j2 = 9;
              }
              if (j2 == 5 && k1 < p_76484_4_ + i1)
                j2 = 0;
              if ((j2 != 0 || p_76484_4_ >= p_76484_4_ + i1 - 1) && p_76484_1_.getBlock(l2, k1, i2).canBeReplacedByLeaves((IBlockAccess)p_76484_1_, l2, k1, i2))
                p_76484_1_.setBlock(l2, k1, i2, stone ? Blocks.stone : ((l == 0) ? BlockInit.mushroomBlockBlue : BlockInit.mushroomBlockGreen), j2, 2);
              continue;
            }
          }
        }
        for (k1 = 0; k1 < i1; k1++) {
          Block block2 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + k1, p_76484_5_);
          if (block2.canBeReplacedByLeaves((IBlockAccess)p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_))
            p_76484_1_.setBlock(p_76484_3_, p_76484_4_ + k1, p_76484_5_, stone ? Blocks.stone : ((l == 0) ? BlockInit.mushroomBlockBlue : BlockInit.mushroomBlockGreen), 10, 2);
        }
        return;
      }
      return;
    }
  }

  public static void generateSkulls(World world, Random random, int x, int y, int z, int numEmptyBlocks) {
    if (numEmptyBlocks > 0 && noLiquidUnderneath(world, x, y - numEmptyBlocks + 1, z)) {
      int auxY = y - numEmptyBlocks + 1;
      if (auxY > 0 && numEmptyBlocks > 0) {
        world.setBlock(x, auxY, z, Blocks.skull, 1, 3);
        TileEntity skullTE = world.getTileEntity(x, auxY, z);
        if (skullTE instanceof TileEntitySkull)
          ((TileEntitySkull)skullTE).func_145903_a(random.nextInt(360));
      }
    }
  }

  public static void generateGlowLily(World world, Random random, int x, int y, int z, int numEmptyBlocks) {
    if (isGoodForLily(world, x, y - numEmptyBlocks, z))
      world.setBlock(x, y - numEmptyBlocks + 1, z, (random.nextFloat() > 0.5F) ? BlockInit.glowLily : BlockInit.glowLilyBlue, 0, 2);
  }

  public static void generateLily(World world, Random random, int x, int y, int z, int numEmptyBlocks) {
    if (world.getBlock(x, y - numEmptyBlocks, z) == Blocks.water)
      world.setBlock(x, y - numEmptyBlocks + 1, z, Blocks.waterlily);
  }

  public static boolean isGoodForLily(World world, int x, int y, int z) {
    return (world.getBlock(x, y, z).getMaterial() == Material.water && world.getBlockMetadata(x, y, z) == 0 && ((world.getBlock(x + 1, y, z).getMaterial() == Material.water && world.getBlockMetadata(x + 1, y, z) == 0) || (world.getBlock(x - 1, y, z).getMaterial() == Material.water && world.getBlockMetadata(x - 1, y, z) == 0)));
  }

  public static boolean noLiquidUnderneath(World world, int x, int y, int z) {
    return !(world.getBlock(x, y - 1, z) instanceof net.minecraft.block.BlockLiquid);
  }

  public static void generateVines(World world, Random random, int x, int y, int z) {
    int aux, distance = getNumEmptyBlocks(world, x, y, z);
    if (distance > 5) {
      aux = random.nextInt(distance - 5) + 5;
    } else {
      aux = distance;
    }
    int side = random.nextInt(4) + 2;
    for (int i = 0; i < aux && !world.getBlock(x, y - i, z).getMaterial().isLiquid(); i++)
      world.setBlock(x, y - i, z, Blocks.vine, 1 << Direction.facingToDirection[side], 0);
  }

  public static int getNumEmptyBlocks(World world, int x, int y, int z) {
    int dist;
    for (dist = 0; y > 5 && !world.isBlockNormalCubeDefault(x, y, z, true) && world.isAirBlock(x, y, z); dist++)
      y--;
    return dist;
  }

  public static void convertToFrozenType(World world, Random random, int x, int y, int z) {
    int height = random.nextInt(5) + 3;
    int length = random.nextInt(5) + 3;
    int width = random.nextInt(5) + 3;
    int newX = x - length / 2;
    int newY = y + height / 2;
    int newZ = z - width / 2;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < length; j++) {
        for (int k = 0; k < width; k++) {
          if (weightedChoice(0.8F, 0.2F, 0.0F, 0.0F, 0.0F, 0.0F) == 1) {
            Block aux = world.getBlock(newX + j, newY - i, newZ + k);
            if (freezable.contains(aux))
              world.setBlock(newX + j, newY - i, newZ + k, Blocks.ice, 0, 2);
          }
        }
      }
    }
  }

  public static void convertToLavaType(World world, Random random, int x, int y, int z) {
    int height = random.nextInt(5) + 3;
    int length = random.nextInt(5) + 3;
    int width = random.nextInt(5) + 3;
    int newX = x - length / 2;
    int newY = y + height / 2;
    int newZ = z - width / 2;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < length; j++) {
        for (int k = 0; k < width; k++) {
          if (weightedChoice(0.8F, 0.2F, 0.0F, 0.0F, 0.0F, 0.0F) == 1) {
            Block aux = world.getBlock(newX + j, newY - i, newZ + k);
            if (freezable.contains(aux) && !world.isAirBlock(newX + j, newY - i + 1, newZ + k))
              world.setBlock(newX + j, newY - i, newZ + k, BlockInit.scorchedStone, 0, 2);
          }
        }
      }
    }
  }

  public static int randomChoice(int... val) {
    Random random = new Random();
    return val[random.nextInt(val.length)];
  }

  public static int weightedChoice(String[] values) {
    if (values.length != 6)
      throw new RuntimeException("Incorrect amount of arguments into weightedChoice! Must be 6!");
    float[] fResult = new float[values.length];
    for (int i = 0; i < values.length; i++)
      fResult[i] = Float.parseFloat(values[i]);
    return weightedChoice(fResult[0], fResult[1], fResult[2], fResult[3], fResult[4], fResult[5]);
  }

  public static int weightedChoice(float par1, float par2, float par3, float par4, float par5, float par6) {
    float total = par1 + par2 + par3 + par4 + par5 + par6;
    float val = (new Random()).nextFloat();
    par1 /= total;
    par2 /= total;
    par3 /= total;
    par4 /= total;
    par5 /= total;
    if (val < par1)
      return 1;
    if (val < par2 + par1)
      return 2;
    float previous = par1 + par2;
    if (val < par3 + previous)
      return 3;
    previous += par3;
    if (val < par4 + previous)
      return 4;
    previous += par4;
    return (val < par5 + previous) ? 5 : 6;
  }

  public static void generateScorchedLavaStone(World world, Random random, int x, int y, int z, int distance) {
    if (world.getBlock(x, y - 1, z).isNormalCube()) {
      world.setBlock(x, y - 1, z, BlockInit.scorchedLavaStone);
    } else if (world.getBlock(x, y - distance, z).isNormalCube()) {
      world.setBlock(x, y - distance, z, BlockInit.scorchedLavaStone);
    }
  }
}
