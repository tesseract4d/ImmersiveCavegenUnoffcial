package net.tclproject.immersivecavegen.fixes;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.common.eventhandler.Event;
import java.util.Random;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
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
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.tclproject.immersivecavegen.WGConfig;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;
import net.tclproject.mysteriumlib.asm.fixes.MysteriumPatchesFixesSnow;

public class MysteriumPatchesFixesPop {
  public static Random lakeRand = new Random();

  @Fix(returnSetting = EnumReturnSetting.ON_TRUE, booleanAlwaysReturned = false)
  public static boolean generate(WorldGenLiquids l, World world, Random random, int x, int y, int z) {
    for (String str : WGConfig.dimblacklist) {
      if (String.valueOf(world.provider.dimensionId).equalsIgnoreCase(str))
        return false;
    }
    boolean type = (l.field_150521_a.getMaterial() == Material.water);
    if (WGConfig.disableSourceWater && type && (!WGConfig.disableSourceUnderground || y < 64))
      return true;
    if (WGConfig.disableSourceLava && !type && (!WGConfig.disableSourceUnderground || y < 64))
      return true;
    return false;
  }

  @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
  public static boolean populate(ChunkProviderGenerate g, IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
    BiomeGenBase biomegenbase;
    int k = p_73153_2_ * 16;
    int l = p_73153_3_ * 16;
    boolean flag = false;
    WorldServer worldServer = DimensionManager.getWorld(0);
    if (worldServer == null)
      return true;
    for (String str : WGConfig.dimblacklist) {
      if (String.valueOf(((World)worldServer).provider.dimensionId).equalsIgnoreCase(str))
        return false;
    }
    BlockFalling.fallInstantly = true;
    try {
      biomegenbase = worldServer.getBiomeGenForCoordsBody(k + 16, l + 16);
    } catch (Exception e) {
      biomegenbase = null;
    }
    g.rand.setSeed(worldServer.getSeed());
    long i1 = g.rand.nextLong() / 2L * 2L + 1L;
    long j1 = g.rand.nextLong() / 2L * 2L + 1L;
    g.rand.setSeed(p_73153_2_ * i1 + p_73153_3_ * j1 ^ worldServer.getSeed());
    lakeRand.setSeed(p_73153_2_ * i1 + p_73153_3_ * j1 ^ worldServer.getSeed());
    MinecraftForge.EVENT_BUS.post((Event)new PopulateChunkEvent.Pre(p_73153_1_, (World)worldServer, g.rand, p_73153_2_, p_73153_3_, flag));
    if (worldServer.getWorldInfo().isMapFeaturesEnabled()) {
      try {
        if (WGConfig.enableMineshaftSpawn)
          g.mineshaftGenerator.generateStructuresInChunk((World)worldServer, g.rand, p_73153_2_, p_73153_3_);
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        flag = WGConfig.enableVillageSpawn ? g.villageGenerator.generateStructuresInChunk((World)worldServer, g.rand, p_73153_2_, p_73153_3_) : false;
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        if (WGConfig.enableStrongholdSpawn)
          g.strongholdGenerator.generateStructuresInChunk((World)worldServer, g.rand, p_73153_2_, p_73153_3_);
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        if (!WGConfig.enableDesolateSpawn)
          g.scatteredFeatureGenerator.generateStructuresInChunk((World)worldServer, g.rand, p_73153_2_, p_73153_3_);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && !flag && g.rand.nextInt(WGConfig.waterLakesChance) == 0 &&
      TerrainGen.populate(p_73153_1_, (World)worldServer, g.rand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAKE)) {
      int i = k + g.rand.nextInt(16) + 8;
      int l1 = g.rand.nextInt(256);
      int i2 = l + g.rand.nextInt(16) + 8;
      (new WorldGenLakes(Blocks.water)).generate((World)worldServer, g.rand, i, l1, i2);
      if (lakeRand.nextInt(WGConfig.waterLakesChance) == 0) {
        i = k + lakeRand.nextInt(16) + 8;
        l1 = lakeRand.nextInt(256);
        i2 = l + lakeRand.nextInt(16) + 8;
        (new WorldGenLakes(Blocks.water)).generate((World)worldServer, lakeRand, i, l1, i2);
      }
      if (lakeRand.nextInt(WGConfig.waterLakesChance) == 0) {
        i = k + lakeRand.nextInt(16) + 8;
        l1 = lakeRand.nextInt(256);
        i2 = l + lakeRand.nextInt(16) + 8;
        (new WorldGenLakes(Blocks.water)).generate((World)worldServer, lakeRand, i, l1, i2);
      }
    }
    /*
    if (TerrainGen.populate(p_73153_1_, (World)worldServer, g.rand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAVA) && !flag) {
      int i = k + g.rand.nextInt(16) + 8;
      int l1 = g.rand.nextInt(g.rand.nextInt(248) + 8);
      int i2 = l + g.rand.nextInt(16) + 8;
      if (l1 < 63 || g.rand.nextInt(WGConfig.lavaLakesChance) == 0) {
        (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, g.rand, i, l1, i2);
      } else if (lakeRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
        (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, lakeRand, i, l1, i2);
      }
      i = k + lakeRand.nextInt(16) + 8;
      l1 = lakeRand.nextInt(g.rand.nextInt(248) + 8);
      i2 = l + lakeRand.nextInt(16) + 8;
      if (l1 < 63 || lakeRand.nextInt(WGConfig.lavaLakesChance) == 0) {
        (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, lakeRand, i, l1, i2);
      } else if (lakeRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
        (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, lakeRand, i, l1, i2);
      }
      i = k + lakeRand.nextInt(16) + 8;
      l1 = lakeRand.nextInt(g.rand.nextInt(248) + 8);
      i2 = l + lakeRand.nextInt(16) + 8;
      if (l1 < 63 || lakeRand.nextInt(WGConfig.lavaLakesChance) == 0) {
        (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, lakeRand, i, l1, i2);
      } else if (lakeRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
        (new WorldGenLakes(Blocks.lava)).generate((World)worldServer, lakeRand, i, l1, i2);
      }
    }
    if (TerrainGen.populate(p_73153_1_, (World)worldServer, g.rand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAVA)) {
      int i = k + g.rand.nextInt(16) + 8;
      int l1 = g.rand.nextInt(g.rand.nextInt(248) + 8);
      int i2 = l + g.rand.nextInt(16) + 8;
      if (l1 < 63 || g.rand.nextInt(WGConfig.waterLakesChance) == 0) {
        (new WorldGenLakesUnderground(Blocks.lava)).generate((World)worldServer, g.rand, i, l1, i2);
      } else if (g.rand.nextInt(WGConfig.undergWaterLakesChance) == 0) {
        (new WorldGenLakesUnderground(Blocks.lava)).generate((World)worldServer, g.rand, i, l1, i2);
      }
      i = k + g.rand.nextInt(16) + 8;
      l1 = g.rand.nextInt(g.rand.nextInt(248) + 8);
      i2 = l + g.rand.nextInt(16) + 8;
      if (l1 < 63 || g.rand.nextInt(WGConfig.waterLakesChance) == 0) {
        (new WorldGenLakesUnderground(Blocks.lava)).generate((World)worldServer, g.rand, i, l1, i2);
      } else if (g.rand.nextInt(WGConfig.undergWaterLakesChance) == 0) {
        (new WorldGenLakesUnderground(Blocks.lava)).generate((World)worldServer, g.rand, i, l1, i2);
      }
      i = k + g.rand.nextInt(16) + 8;
      l1 = g.rand.nextInt(g.rand.nextInt(248) + 8);
      i2 = l + g.rand.nextInt(16) + 8;
      if (l1 < 63 || g.rand.nextInt(WGConfig.waterLakesChance) == 0) {
        (new WorldGenLakesUnderground(Blocks.lava)).generate((World)worldServer, g.rand, i, l1, i2);
      } else if (g.rand.nextInt(WGConfig.undergWaterLakesChance) == 0) {
        (new WorldGenLakesUnderground(Blocks.lava)).generate((World)worldServer, g.rand, i, l1, i2);
      }
    }*/
    boolean doGen = TerrainGen.populate(p_73153_1_, (World)worldServer, g.rand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.DUNGEON);
    int k1;
    for (k1 = 0; doGen && k1 < 8; k1++) {
      int l1 = k + g.rand.nextInt(16) + 8;
      int i2 = g.rand.nextInt(256);
      int j2 = l + g.rand.nextInt(16) + 8;
      if (WGConfig.enableDungeonSpawn)
        (new WorldGenDungeons()).generate((World)worldServer, g.rand, l1, i2, j2);
    }
    if (biomegenbase != null) {
      biomegenbase.decorate((World)worldServer, g.rand, k, l);
      if (TerrainGen.populate(p_73153_1_, (World)worldServer, g.rand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.ANIMALS))
        SpawnerAnimals.performWorldGenSpawning((World)worldServer, biomegenbase, k + 8, l + 8, 16, 16, g.rand);
    }
    k += 8;
    l += 8;
    doGen = TerrainGen.populate(p_73153_1_, (World)worldServer, g.rand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.ICE);
    for (k1 = 0; doGen && k1 < 16; k1++) {
      for (int l1 = 0; l1 < 16; l1++) {
        int i2 = worldServer.getPrecipitationHeight(k + k1, l + l1);
        if (worldServer.isBlockFreezable(k1 + k, i2 - 1, l1 + l))
          worldServer.setBlock(k1 + k, i2 - 1, l1 + l, Blocks.ice, 0, 2);
        if (worldServer.func_147478_e(k1 + k, i2, l1 + l, true))
          worldServer.setBlock(k1 + k, i2, l1 + l, Blocks.snow_layer, 0, 2);
      }
    }
    MinecraftForge.EVENT_BUS.post((Event)new PopulateChunkEvent.Post(p_73153_1_, (World)worldServer, g.rand, p_73153_2_, p_73153_3_, flag));
    BlockFalling.fallInstantly = false;
    if (Loader.isModLoaded("immersivesnow"))
      loadImmersiveSnowCompat(g, p_73153_1_, p_73153_2_, p_73153_3_);
    return true;
  }

  @Fix(targetMethod = "<init>", insertOnExit = true)
  public static void Corridor(StructureMineshaftPieces.Corridor r, int p_i2035_1_, Random p_i2035_2_, StructureBoundingBox p_i2035_3_, int p_i2035_4_) {
    if (!WGConfig.enableCaveSpiderSpawnerSpawn) {
      r.hasSpiders = false;
    } else if (WGConfig.enableCaveSpiderSpawnerSpawnAlways) {
      r.hasSpiders = true;
    }
  }

  @Fix(targetMethod = "<init>", insertOnExit = true)
  public static void ChunkProviderGenerate(ChunkProviderGenerate gen, World p_i2006_1_, long p_i2006_2_, boolean p_i2006_4_) {
    gen.caveGenerator = TerrainGen.getModdedMapGen(gen.caveGenerator, InitMapGenEvent.EventType.CAVE);
    gen.strongholdGenerator = (MapGenStronghold)TerrainGen.getModdedMapGen((MapGenBase)gen.strongholdGenerator, InitMapGenEvent.EventType.STRONGHOLD);
    gen.villageGenerator = (MapGenVillage)TerrainGen.getModdedMapGen((MapGenBase)gen.villageGenerator, InitMapGenEvent.EventType.VILLAGE);
    gen.mineshaftGenerator = (MapGenMineshaft)TerrainGen.getModdedMapGen((MapGenBase)gen.mineshaftGenerator, InitMapGenEvent.EventType.MINESHAFT);
    gen.scatteredFeatureGenerator = (MapGenScatteredFeature)TerrainGen.getModdedMapGen((MapGenBase)gen.scatteredFeatureGenerator, InitMapGenEvent.EventType.SCATTERED_FEATURE);
    gen.ravineGenerator = TerrainGen.getModdedMapGen(gen.ravineGenerator, InitMapGenEvent.EventType.RAVINE);
  }

  @Method(modid = "immersivesnow")
  public static void loadImmersiveSnowCompat(ChunkProviderGenerate t, IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
    boolean population = MysteriumPatchesFixesSnow.populate(t, p_73153_1_, p_73153_2_, p_73153_3_);
  }
}
