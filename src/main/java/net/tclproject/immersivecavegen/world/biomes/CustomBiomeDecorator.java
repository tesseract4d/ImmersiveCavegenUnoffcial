package net.tclproject.immersivecavegen.world.biomes;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

public class CustomBiomeDecorator extends BiomeDecorator {
  public CustomBiomeDecorator(BiomeGenBase biomeGenBase, int trees, int grass, int flowers, int plants) {
    this.flowersPerChunk = flowers;
    this.sandPerChunk2 = 0;
    this.sandPerChunk = 15;
    this.generateLakes = false;
  }

  public CustomBiomeDecorator(BiomeGenBase biomeGenBase, int trees, int grass, int flowers) {
    this(biomeGenBase, trees, grass, flowers, 0);
  }

  public void decorateChunk(World world, Random random, BiomeGenBase biome, int x, int z) {
    if (this.currentWorld != null)
      throw new RuntimeException("The biome is already being decorated! This is occuring at:" + x + "," + z);
    super.decorateChunk(world, random, biome, x, z);
    this.currentWorld = world;
    this.randomGenerator = random;
    this.chunk_X = x;
    this.chunk_Z = z;
    this.currentWorld = null;
    this.randomGenerator = null;
  }

  public void generateOre(World world, Random random, int x, int z, int timesPerChunk, WorldGenerator oreGen, int minH, int maxH) {
    for (int var5 = 0; var5 < timesPerChunk; var5++) {
      int var6 = x + random.nextInt(16);
      int var7 = random.nextInt(maxH - minH) + minH;
      int var8 = z + random.nextInt(16);
      oreGen.generate(world, random, var6, var7, var8);
    }
  }

  public static WorldGenerator sand = (WorldGenerator)new WorldGenMinable((Block)Blocks.sand, 32);

  public static WorldGenerator ice;

  public static WorldGenerator water;

  public static WorldGenerator lava;

  public static WorldGenerator obsidian;

  public static WorldGenerator netherrack = (WorldGenerator)new WorldGenMinable(Blocks.netherrack, 64);

  public static WorldGenerator stone;

  public static WorldGenerator dirt;

  static {
    ice = (WorldGenerator)new WorldGenMinable(Blocks.ice, 32);
    water = new MinableWorldGenerator(Blocks.water, 4);
    lava = new MinableWorldGenerator(Blocks.lava, 8);
    dirt = new MinableWorldGenerator(Blocks.dirt, 72, (Block)Blocks.sand);
    stone = new MinableWorldGenerator(Blocks.stone, 72, Blocks.dirt);
    obsidian = (WorldGenerator)new WorldGenMinable(Blocks.obsidian, 8);
  }
}
