package net.tclproject.immersivecavegen.world;

import cpw.mods.fml.common.Loader;
import ganymedes01.etfuturum.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateStoneStalactite {
  public final Block blockId;

  public static List blockWhiteList = new ArrayList();

  public GenerateStoneStalactite() {
    this(Blocks.stone);
  }

  public GenerateStoneStalactite(Block toGen) {
    this.blockId = toGen;
  }

  public void generate(World world, Random random, int x, int y, int z, int distance, int maxLength) {
    boolean deepslateBelow = false;
    boolean deepslateAbove = false;
    if (Loader.isModLoaded("etfuturum")) {
      deepslateBelow = CavesDecorator.isBlockDeepslate(world, x, y - distance, z);
      deepslateAbove = CavesDecorator.isBlockDeepslate(world, x, y + 1, z);
    }
    if (distance <= 1) {
      if (!world.isAirBlock(x, y + 1, z) && !(world.getBlock(x, y - 1, z) instanceof net.minecraft.block.BlockLiquid)) {
        if (deepslateAbove && deepslateBelow) {
          world.setBlock(x, y, z, ModBlocks.DEEPSLATE.get(), 0, 3);
          return;
        }
        world.setBlock(x, y, z, this.blockId, 0, 3);
      }
    } else {
      int j = 0;
      int topY = y;
      int botY = y - distance + 1;
      if (world.getBlock(x, botY - 1, z).isNormalCube() && random.nextInt(10) > 7) {
        if (deepslateBelow) {
          world.setBlock(x, botY, z, ModBlocks.DEEPSLATE.get(), CavesDecorator.randomChoice(9, 10), 3);
          return;
        }
        world.setBlock(x, botY, z, this.blockId, CavesDecorator.randomChoice(9, 10), 3);
        if (this.blockId == Blocks.sandstone) {
          if (world.getBlock(x, botY - 1, z) == Blocks.stone)
            world.setBlock(x, botY - 1, z, Blocks.sandstone, 0, 2);
          CavesDecorator.convertToSandType(world, random, x, botY, z);
        } else if (this.blockId == Blocks.netherrack) {
          if (world.getBlock(x, botY - 1, z) == Blocks.stone || world.getBlock(x, botY - 1, z) == Blocks.sandstone)
            world.setBlock(x, botY - 1, z,  Blocks.netherrack, 0, 2);
          CavesDecorator.convertToLavaType(world, random, x, botY, z);
        }
        return;
      }
      if (world.getBlock(x, y + 1, z).isNormalCube() && random.nextInt(10) > 7) {
        if (deepslateAbove) {
          world.setBlock(x, topY, z, ModBlocks.DEEPSLATE.get(), CavesDecorator.randomChoice(1, 2), 3);
          return;
        }
        world.setBlock(x, topY, z, this.blockId, CavesDecorator.randomChoice(1, 2), 3);
        if (this.blockId == Blocks.sandstone) {
          CavesDecorator.convertToSandType(world, random, x, topY, z);
        } else if (this.blockId == Blocks.netherrack) {
          CavesDecorator.convertToLavaType(world, random, x, topY, z);
        }
        return;
      }
      boolean gen = false;
      boolean genStala = false;
      if (world.getBlock(x, y + 1, z).isNormalCube()) {
        if (deepslateAbove) {
          world.setBlock(x, y, z, ModBlocks.DEEPSLATE.get(), 3, 3);
        } else {
          world.setBlock(x, y, z, this.blockId, 3, 3);
        }
        genStala = true;
      }
      if (!world.getBlock(x, botY, z).getMaterial().isLiquid() && deepslateBelow) {
        int aux = CavesDecorator.randomChoice(new int[] { -1, 8 });
        if (aux != -1) {
          world.setBlock(x, botY, z, ModBlocks.DEEPSLATE.get(), aux, 3);
          gen = true;
        }
      } else if (!world.getBlock(x, botY, z).getMaterial().isLiquid() && blockWhiteList.contains(world.getBlock(x, botY - 1, z))) {
        int aux = CavesDecorator.randomChoice(new int[] { -1, 8 });
        if (aux != -1) {
          world.setBlock(x, botY, z, this.blockId, aux, 3);
          gen = true;
          if (this.blockId == Blocks.sandstone) {
            if (world.getBlock(x, botY - 1, z) == Blocks.stone)
              world.setBlock(x, botY - 1, z, Blocks.sandstone, 0, 2);
            CavesDecorator.convertToSandType(world, random, x, botY, z);
          } else if (this.blockId == Blocks.netherrack) {
            if (world.getBlock(x, botY - 1, z) == Blocks.stone || world.getBlock(x, botY - 1, z) == Blocks.sandstone)
              world.setBlock(x, botY - 1, z, Blocks.netherrack, 0, 2);
            CavesDecorator.convertToLavaType(world, random, x, botY, z);
          }
        }
      }
      if (genStala)
        while (true) {
          int bottomMetadata = world.getBlockMetadata(x, botY, z);
          if (topY < botY || j >= distance)
            return;
          if (bottomMetadata == 8 && topY - 1 == botY) {
            world.setBlock(x, topY, z, deepslateAbove ? ModBlocks.DEEPSLATE.get() : this.blockId, CavesDecorator.randomChoice(4, 5, 7, 6), 3);
            break;
          }
          topY--;
          j++;
          int aux = random.nextInt(4);
          if (world.getBlock(x, topY, z) instanceof net.minecraft.block.BlockLiquid || world.getBlock(x, topY, z) instanceof net.minecraft.block.BlockStaticLiquid) {
            world.setBlock(x, topY + 1, z, deepslateAbove ? ModBlocks.DEEPSLATE.get() : this.blockId, CavesDecorator.randomChoice(11), 3);
            return;
          }
          if (aux != 2 && !world.getBlock(x, topY, z).isNormalCube()) {
            if (world.isAirBlock(x, topY, z))
              world.setBlock(x, topY, z, deepslateAbove ? ModBlocks.DEEPSLATE.get() : this.blockId, CavesDecorator.randomChoice(4, 5), 3);
            continue;
          }
          if (world.isAirBlock(x, topY, z))
            world.setBlock(x, topY, z, deepslateAbove ? ModBlocks.DEEPSLATE.get() : this.blockId, CavesDecorator.randomChoice(11), 3);
          break;
        }
      if (gen)
        while (true) {
          int topMetadata = world.getBlockMetadata(x, y, z);
          if (topY < botY || j >= distance)
            return;
          if (topMetadata == 3 && botY + 1 == y) {
            world.setBlock(x, botY, z, deepslateBelow ? ModBlocks.DEEPSLATE.get() : this.blockId, CavesDecorator.randomChoice(4, 5, 6, 7), 3);
            break;
          }
          j++;
          botY++;
          if (world.getBlock(x, botY, z) != null) {
            if (world.getBlockMetadata(x, botY, z) == 4 || world.getBlockMetadata(x, botY, z) == 5) {
              world.setBlock(x, botY - 1, z, deepslateBelow ? ModBlocks.DEEPSLATE.get() : this.blockId, CavesDecorator.randomChoice(4, 5), 3);
              break;
            }
            if (world.getBlockMetadata(x, botY, z) == 11) {
              world.setBlock(x, botY - 1, z, deepslateBelow ? ModBlocks.DEEPSLATE.get() : this.blockId, CavesDecorator.randomChoice(12), 3);
              break;
            }
          }
          int aux = random.nextInt(3);
          if (aux != 2) {
            if (world.isAirBlock(x, botY, z))
              world.setBlock(x, botY, z, deepslateBelow ? ModBlocks.DEEPSLATE.get() : this.blockId, CavesDecorator.randomChoice(4, 5), 3);
            continue;
          }
          if (world.isAirBlock(x, botY, z))
            world.setBlock(x, botY, z, deepslateBelow ? ModBlocks.DEEPSLATE.get() : this.blockId, CavesDecorator.randomChoice( 12  ), 3);
          break;
        }
    }
  }
}
