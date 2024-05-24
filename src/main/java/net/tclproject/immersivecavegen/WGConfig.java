package net.tclproject.immersivecavegen;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class WGConfig {
  public static Configuration config;
  
  public static boolean oneBigCave = false;
  
  public static boolean oldGen = false;
  
  public static boolean enableBetterSand = true;
  
  public static boolean enableBetterCaverns = true;
  
  public static boolean enableBetterMineshafts = true;
  
  public static boolean enablePopChanges = true;
  
  public static boolean turnOffVanillaCaverns = false;
  
  public static boolean turnOffVanillaUndergroundDirt = false;
  
  public static boolean caveDecorations = true;
  
  public static boolean realisticCobwebs = true;
  
  public static int widthAdditionNormal = 0;
  
  public static int widthAdditionColossal = 0;
  
  public static int widthAdditionRegional = 0;
  
  public static int widthAdditionCircular = 0;
  
  public static int widthAdditionRavineCave = 0;
  
  public static int widthAdditionVertical = 0;
  
  public static int widthAdditionMaze = 0;
  
  public static int widthAdditionBranchPoint = 0;
  
  public static int widthAdditionDirectional = 0;
  
  public static int additionalCavesInClusterLength = 0;
  
  public static int additionalCavesInClusterAmount = 0;
  
  public static boolean hardLimitsEnabled = false;
  
  public static int widthMaxNormal = 0;
  
  public static int widthMaxColossal = 0;
  
  public static int widthMaxRegional = 0;
  
  public static int widthMaxCircular = 0;
  
  public static int widthMaxRavineCave = 0;
  
  public static int widthMaxVertical = 0;
  
  public static int widthMaxMaze = 0;
  
  public static int widthMaxBranchPoint = 0;
  
  public static int widthMaxDirectional = 0;
  
  public static int maxCavesInClusterLength = 0;
  
  public static int maxCavesInClusterAmount = 0;
  
  public static int widthMinNormal = 0;
  
  public static int widthMinColossal = 0;
  
  public static int widthMinRegional = 0;
  
  public static int widthMinCircular = 0;
  
  public static int widthMinRavineCave = 0;
  
  public static int widthMinVertical = 0;
  
  public static int widthMinMaze = 0;
  
  public static int widthMinBranchPoint = 0;
  
  public static int widthMinDirectional = 0;
  
  public static int minCavesInClusterLength = 0;
  
  public static int minCavesInClusterAmount = 0;
  
  public static int waterLakesChance = 12;
  
  public static int lavaLakesChance = 30;
  
  public static int caveNormalReductionPercentage = 0;
  
  public static int caveColossalReductionPercentage = 0;
  
  public static int caveRegionalReductionPercentage = 0;
  
  public static int caveCircularRoomReductionPercentage = 0;
  
  public static int caveRavineCaveReductionPercentage = 0;
  
  public static int caveVerticalReductionPercentage = 0;
  
  public static int caveMazeReductionPercentage = 0;
  
  public static int undergWaterLakesChance = 24;
  
  public static int undergLavaLakesChance = 24;
  
  public static boolean waterCaves = true;
  
  public static boolean netherCaves = false;
  
  public static boolean jumpscare = true;
  
  public static boolean serveronly = false;
  
  public static float liliesChance = 0.5F;
  
  public static float treeChance = 0.5F;
  
  public static int glowLilies = 1;
  
  public static boolean fcaves;
  
  public static boolean icaves;
  
  public static boolean jcaves;
  
  public static boolean scaves;
  
  public static boolean wcaves;
  
  public static boolean lcaves;
  
  public static boolean disableSourceWater;
  
  public static boolean disableSourceLava;
  
  public static boolean disableSourceUnderground;
  
  public static float generationDensity = 0.8F;
  
  public static boolean livingCavesLush;
  
  public static boolean enableMineshaftSpawn;
  
  public static boolean enableDungeonSpawn;
  
  public static boolean enableStrongholdSpawn;
  
  public static boolean enableVillageSpawn;
  
  public static boolean enableCaveSpiderSpawnerSpawn;
  
  public static boolean enableCaveSpiderSpawnerSpawnAlways;
  
  public static boolean enableDesolateSpawn;
  
  public static boolean secondYLevel = true;
  
  public static boolean enableEntities = true;
  
  public static boolean glowSlimesSpawnWater = true;
  
  public static int floodLevel = 4;
  
  public static int floodMech = 1;
  
  public static String[] fcaveslist;
  
  public static String[] icaveslist;
  
  public static String[] jcaveslist;
  
  public static String[] pcaveslist;
  
  public static String[] scaveslist;
  
  public static String[] wcaveslist;
  
  public static String[] lcaveslist;
  
  public static String[] dimblacklist;
  
  public static String[] secondYLevelList;
  
  public static int cavernsSpawnMultiplier = 1;
  
  public static int cavesSpawnMultiplier = 1;
  
  public static int oldBigCavesSpawnMultiplier = 1;
  
  public static int giantCaveChance = 0;
  
  public static int entitySmBrSpID = -1;
  
  public static int entityBrSpID = -1;
  
  public static int entityLBrSpID = -1;
  
  public static int entityGlSlID = -1;
  
  public static int mobSpawnChance = 30;
  
  public static String CATEGORY_LIMITS = "Hard Limits";
  
  public static String CATEGORY_WORLD = "World Generation";
  
  public static String CATEGORY_TYPES = "Cave Types";
  
  public static String CATEGORY_DECO = "Cave Decoration Chances";
  
  public static String CATEGORY_MOBS = "Entities";
  
  public static String CATEGORY_REDUX = "Reduction";
  
  public static String CATEGORY_MODIF = "Cave Generation Modifiers";
  
  public static void init(String configDir, FMLPreInitializationEvent event) {
    FMLCommonHandler.instance().bus().register(new WGConfig());
    if (config == null) {
      File path = new File(configDir + "/immersivecavegen.cfg");
      config = new Configuration(path);
      loadConfiguration();
    } 
  }
  
  public static void loadConfiguration() {
    config.load();
    enableEntities = config.getBoolean("Enable Mobs", CATEGORY_MOBS, true, "An option to disable the mod's mobs as a whole.");
    glowSlimesSpawnWater = config.getBoolean("Glowslimes Spawn Water", CATEGORY_MOBS, true, "An option to disable the special ability of glowslimes to spawn water.");
    mobSpawnChance = config.getInt("Mob Spawn Chance", CATEGORY_MOBS, 30, 1, 1000, "How often custom mobs spawn, 1 being the caves are absolutely flooded with them, 10 being the default and 1000 being almost never.");
    entitySmBrSpID = config.getInt("Small Brown Spider ID", CATEGORY_MOBS, -1, -1, 1000000, "-1 is random free ID. Set to something else if you want a fixed entity ID.");
    entityBrSpID = config.getInt("Brown Spider ID", CATEGORY_MOBS, -1, -1, 1000000, "-1 is random free ID. Set to something else if you want a fixed entity ID.");
    entityLBrSpID = config.getInt("Large Brown Spider ID", CATEGORY_MOBS, -1, -1, 1000000, "-1 is random free ID. Set to something else if you want a fixed entity ID.");
    entityGlSlID = config.getInt("Glowslime ID", CATEGORY_MOBS, -1, -1, 1000000, "-1 is random free ID. Set to something else if you want a fixed entity ID.");
    oneBigCave = config.getBoolean("Exteme Cave Gen", CATEGORY_WORLD, false, "Makes the world underground a HUGE cavern system. A bit extreme, not many will like it, but the option is there. (Note: Does not work with Old Cave Gen option)");
    enableBetterCaverns = config.getBoolean("Enable Caverns Replacer", CATEGORY_WORLD, true, "Replaces the vanilla caverns with custom.");
    enableBetterSand = config.getBoolean("Enable Better Sand Generation", CATEGORY_WORLD, true, "Improved sand generation.");
    enableBetterMineshafts = config.getBoolean("Enable Better Mineshafts", CATEGORY_WORLD, true, "Makes mineshafts spawn less chaotically and in better mineshaft systems.");
    enablePopChanges = config.getBoolean("Enable World Population Changes", CATEGORY_WORLD, true, "Disables liquid source block and lake spawn changes and toggles for stucture spawns, for better mod compatibility. If something conflicts with another mod, the first thing you should try disabling is this. The second is 'Separate Flooded Y Level'.");
    turnOffVanillaCaverns = config.getBoolean("Disable Vanilla Caverns", CATEGORY_WORLD, false, "Disables vanilla, non-custom cavern spawning completely.");
    turnOffVanillaUndergroundDirt = config.getBoolean("Disable Vanilla Underground Dirt", CATEGORY_WORLD, false, "Prevents vanilla from spawning patches of dirt underground. Please note this will likely have no effect on vanilla gen replacers, such as CoFH Core.");
    oldGen = config.getBoolean("Old Cave Gen", CATEGORY_WORLD, false, "Instead of using TMCU4 cave gen, uses TMCU2 cave gen.");
    cavernsSpawnMultiplier = config.getInt("Caverns Spawn Frequency Multiplier", CATEGORY_WORLD, 1, 0, 50, "Value from 0 to 50. The default is 1, which is how they generate normally.");
    cavesSpawnMultiplier = config.getInt("Caves Spawn Frequency Multiplier", CATEGORY_WORLD, 1, 0, 50, "Value from 0 to 50, 0 totally turning off caves and 50 being a world totally filled with them. The default is 1, which is how they generate in vanilla minecraft.");
    oldBigCavesSpawnMultiplier = config.getInt("Old Big Caves Spawn Frequency Multiplier", CATEGORY_WORLD, 1, 0, 50, "Big caves frequency multiplier that only works with 'Old Cave Gen' as true.");
    giantCaveChance = config.getInt("Extra Mega Cave Chance", CATEGORY_WORLD, 0, 0, 100, "Note: This will only affect caves that *can* be big. There are going to be some small caves even if you turn this to 100. Note 2: This is NOT percentages. 100 does not mean caves have 100% chance to be big, it simply means they're 100 'x' more likely to be big.");
    waterCaves = config.getBoolean("Water Flooded Caves", CATEGORY_WORLD, true, "Some water-flooded caves will spawn. if you think it's not immersive but annoying, you can turn it off :p");
    netherCaves = config.getBoolean("Enable Nether Decoration", CATEGORY_WORLD, false, "Applies the nether stalactites, plants etc. in worldgen to the nether as well. A bit wacky, so off by default.");
    jumpscare = config.getBoolean("Effects", CATEGORY_WORLD, true, "Surprise! (Only keep reading if you hate surprises)(Randomly (but rarely) applies blindness below y=35 and plays a sound).");
    widthAdditionNormal = config.getInt("Normal Cave Width Modifier", CATEGORY_MODIF, 0, -12, 12, "Value from -5 to 12, manually modifies the width of a specific type of caves. Doesn't work with Old Cave Gen option.");
    widthAdditionColossal = config.getInt("Colossal Cave Width Modifier", CATEGORY_MODIF, 0, -12, 12, "Value from -5 to 12, manually modifies the width of a specific type of caves. Doesn't work with Old Cave Gen option.");
    widthAdditionRegional = config.getInt("Regional Cave Width Modifier", CATEGORY_MODIF, 0, -12, 12, "Value from -5 to 12, manually modifies the width of a specific type of caves. Doesn't work with Old Cave Gen option.");
    widthAdditionCircular = config.getInt("Circular Cave Room Width Modifier", CATEGORY_MODIF, 0, -12, 12, "Value from -5 to 12, manually modifies the width of a specific type of caves. Doesn't work with Old Cave Gen option.");
    widthAdditionRavineCave = config.getInt("Ravine Cave Width Modifier", CATEGORY_MODIF, 0, -12, 12, "Value from -5 to 12, manually modifies the width of a specific type of caves. Doesn't work with Old Cave Gen option.");
    widthAdditionVertical = config.getInt("Vertical Cave Width Modifier", CATEGORY_MODIF, 0, -12, 12, "Value from -5 to 12, manually modifies the width of a specific type of caves. Doesn't work with Old Cave Gen option.");
    widthAdditionMaze = config.getInt("Maze Cave Width Modifier", CATEGORY_MODIF, 0, -12, 12, "Value from -5 to 12, manually modifies the width of a specific type of caves. Doesn't work with Old Cave Gen option.");
    widthAdditionBranchPoint = config.getInt("Cave Branch Point Width Modifier", CATEGORY_MODIF, 0, -12, 12, "Value from -5 to 12, modifies the width of a specific type of caves. Doesn't work with Old Cave Gen option.");
    widthAdditionDirectional = config.getInt("Directional Cave Width Modifier", CATEGORY_MODIF, 0, -12, 12, "Value from -5 to 12, manually modifies the width of a specific type of caves. Doesn't work with Old Cave Gen option.");
    additionalCavesInClusterLength = config.getInt("Cave Length in Cluster Modifier", CATEGORY_MODIF, 0, -50, 100, "Value from -3 to 50, manually modifies the cave length in a cave cluster. Doesn't work with Old Cave Gen option.");
    additionalCavesInClusterAmount = config.getInt("Caves in Cluster Modifier", CATEGORY_MODIF, 0, -12, 50, "Value from -3 to 12, manually modifies the amount of caves in a cave cluster. Doesn't work with Old Cave Gen option.");
    widthMaxNormal = config.getInt("Normal Cave Max Width", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMaxColossal = config.getInt("Colossal Cave Max Width", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMaxRegional = config.getInt("Regional Cave Max Width", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMaxCircular = config.getInt("Circular Cave Room Max Width", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMaxRavineCave = config.getInt("Ravine Cave Max Width", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMaxVertical = config.getInt("Vertical Cave Max Width", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMaxMaze = config.getInt("Maze Cave Max Width", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMaxBranchPoint = config.getInt("Cave Branch Point Max Width", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMaxDirectional = config.getInt("Directional Cave Max Width", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    maxCavesInClusterLength = config.getInt("Max Cave Length in Cluster", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max cave length in a cave cluster. Doesn't work with Old Cave Gen option.");
    maxCavesInClusterAmount = config.getInt("Max Caves in Cluster", CATEGORY_LIMITS, 1000, 0, 1000, "Allows to set max amount of caves in a cave cluster. Doesn't work with Old Cave Gen option.");
    widthMinNormal = config.getInt("Normal Cave Min Width", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMinColossal = config.getInt("Colossal Cave Min Width", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMinRegional = config.getInt("Regional Cave Min Width", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMinCircular = config.getInt("Circular Cave Room Min Width", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMinRavineCave = config.getInt("Ravine Cave Min Width", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMinVertical = config.getInt("Vertical Cave Min Width", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMinMaze = config.getInt("Maze Cave Min Width", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMinBranchPoint = config.getInt("Cave Branch Point Min Width", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    widthMinDirectional = config.getInt("Directional Cave Min Width", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min width of a specific type of cave. Doesn't work with Old Cave Gen option.");
    minCavesInClusterLength = config.getInt("Min Cave Length in Cluster", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min cave length in a cave cluster. Doesn't work with Old Cave Gen option.");
    minCavesInClusterAmount = config.getInt("Min Caves in Cluster", CATEGORY_LIMITS, 0, 0, 1000, "Allows to set min amount of caves in a cave cluster. Doesn't work with Old Cave Gen option.");
    hardLimitsEnabled = config.getBoolean("Enable Hard Limits", CATEGORY_LIMITS, false, "Turning this on is required for the min-max settings to take effect. Treat this as a 'I know what I'm doing!' switch.");
    caveDecorations = config.getBoolean("Spawn Cave Decorations", CATEGORY_WORLD, true, "Stalactites, stalagmites, vines etc. will not spawn if you turn this off.");
    realisticCobwebs = config.getBoolean("Realistic Cobwebs", CATEGORY_WORLD, true, "Makes it possible to burn cobwebs with torches/flint and steel. Backport of the 1.14+ Realistic Cobwebs mod.");
    serveronly = config.getBoolean("Server Only", CATEGORY_WORLD, false, "Custom items, blocks etc. will not be registered effectively allowing the mod to be server-only. Note: Because of https://github.com/MinecraftForge/FML/issues/510 vanilla clients can't join. Nothing I can do, if you know a way around this please notify me.");
    disableSourceLava = config.getBoolean("Disable Lava Source Block Gen", CATEGORY_WORLD, false, "Don't know about you, but I hate it when a random source block sets a forest on fire.");
    disableSourceWater = config.getBoolean("Disable Water Source Block Gen", CATEGORY_WORLD, false, "Don't know about you, but I find random water falling down on lava lakes in fire caves pretty annoying and immersion-ruining. Note: Does NOT affect 'Water Flooded Caves', you can turn that off separately.");
    disableSourceUnderground = config.getBoolean("Only Disable Source Gen Underground", CATEGORY_WORLD, false, "If set to true, changes in 'disable lava source block gen' and 'disable water source block gen' will only apply if underground.");
    secondYLevel = config.getBoolean("Separate Flooded Y Level", CATEGORY_WORLD, true, "As an attempt to create something similar to subterranean waters, some caves in and around 'deep ocean's have a separate fixed average Y level below which they're flooded with water.");
    livingCavesLush = config.getBoolean("Lush Living Caves", CATEGORY_TYPES, false, "Living caves that spawn under swamps not only have some occasional trees and flowers but also dirt, grass etc.");
    enableMineshaftSpawn = config.getBoolean("Enable Mineshafts Spawn", CATEGORY_WORLD, true, "Gives option to disable mineshafts from spawning.");
    enableStrongholdSpawn = config.getBoolean("Enable Stronghold Spawn", CATEGORY_WORLD, true, "Gives option to disable strongholds from spawning.");
    enableDungeonSpawn = config.getBoolean("Enable Dungeon Spawn", CATEGORY_WORLD, true, "Gives option to disable dungeons from spawning.");
    enableVillageSpawn = config.getBoolean("Enable Village Spawn", CATEGORY_WORLD, true, "Gives option to disable villages from spawning.");
    enableCaveSpiderSpawnerSpawn = config.getBoolean("Enable Cave Spider Spawner Spawn", CATEGORY_WORLD, true, "Gives option to disable cave spider spawners in mineshafts from spawning.");
    enableCaveSpiderSpawnerSpawnAlways = config.getBoolean("Cave Spider Spawner Spawns Always", CATEGORY_WORLD, false, "Makes EVERY mineshaft corridor spawn with a cave spider spawner and cobwebs. The hardcore version of mineshafts.");
    enableDesolateSpawn = config.getBoolean("Plain World", CATEGORY_WORLD, false, "Gives option to disable scattered features from spawning.");
    floodLevel = config.getInt("Filled Caves Y Level", CATEGORY_WORLD, 4, 1, 256, "Air blocks under this point will always be replaced with water/lava (depends on the other config settings). NOTE: As many things, doesn't support old cave gen option.");
    floodMech = config.getInt("Filled Caves Spawn Mechanism", CATEGORY_WORLD, 1, 1, 2, "1 - Lava, 2 - Water. NOTE: As many things, doesn't support old cave gen option.");
    waterLakesChance = config.getInt("Water Lakes Chance", CATEGORY_WORLD, 12, 1, 100000, "Allows to control water lake frequencies. The less the number, the more frequently they spawn, so 1 = they spawn more oftenly, 10000 = close to never.");
    lavaLakesChance = config.getInt("Lava Lakes Chance", CATEGORY_WORLD, 30, 1, 100000, "Allows to control lava lake frequencies. The less the number, the more frequently they spawn, so 1 = they spawn more oftenly, 10000 = close to never.");
    caveNormalReductionPercentage = config.getInt("Normal Cave Reduction Percentage", CATEGORY_REDUX, 0, 0, 100, "Allows to make normal caves even less common.");
    caveColossalReductionPercentage = config.getInt("Colossal Cave Reduction Percentage", CATEGORY_REDUX, 0, 0, 100, "Allows to make colossal caves even less common.");
    caveRegionalReductionPercentage = config.getInt("Regional Cave Reduction Percentage", CATEGORY_REDUX, 0, 0, 100, "Allows to make regional caves even less common.");
    caveCircularRoomReductionPercentage = config.getInt("Circular Cave Room Reduction Percentage", CATEGORY_REDUX, 0, 0, 100, "Allows to make circular cave rooms even less common.");
    caveRavineCaveReductionPercentage = config.getInt("Ravine Cave Reduction Percentage", CATEGORY_REDUX, 0, 0, 100, "Allows to make normal ravine caves even less common. Note: Ravine caves are different from ravines/caverns! They're not the same thing!");
    caveVerticalReductionPercentage = config.getInt("Vertical Cave Reduction Percentage", CATEGORY_REDUX, 0, 0, 100, "Allows to make normal vertical even less common.");
    caveMazeReductionPercentage = config.getInt("Maze Cave Reduction Percentage", CATEGORY_REDUX, 0, 0, 100, "Allows to make maze caves even less common.");
    undergWaterLakesChance = config.getInt("Underground Water Lakes Chance", CATEGORY_WORLD, 24, 1, 100000, "Allows to control underground water lake frequencies. The less the number, the more frequently they spawn, so 1 = they spawn more oftenly, 10000 = close to never.");
    undergLavaLakesChance = config.getInt("Underground Lava Lakes Chance", CATEGORY_WORLD, 24, 1, 100000, "Allows to control underground lava lake frequencies. The less the number, the more frequently they spawn, so 1 = they spawn more oftenly, 10000 = close to never.");
    generationDensity = config.getFloat("Decoration Density Modifier", CATEGORY_WORLD, 0.8F, 0.0F, 1.0F, "The bigger the number, the more likely a stalactite/staalagmite/glowshroom/other decoration will spawn for every block of a cave.");
    treeChance = config.getFloat("Tree Type Spawn Change", CATEGORY_WORLD, 0.5F, 0.0F, 1.0F, "If living caves are enabled, trees can spawn in them. The closer this number to 0, the more likely a tree will be dead, the closer to 1, the more likely it will be alive.");
    liliesChance = config.getFloat("Underground Lilies Spawn Change", CATEGORY_WORLD, 0.5F, 0.0F, 1.0F, "How often a lily will try to spawn underground. 1F does *not* mean every underground water block will have lily.");
    glowLilies = config.getInt("Underground Lilies Spawn Mode", CATEGORY_WORLD, 1, 1, 3, "1 - Glow in mushroom caves, normal everywhere else. 2 - Glow everywhere. 3 - Normal everywhere. (To turn off completely, use 'Chance' setting).");
    fcaves = config.getBoolean("Generate Fire Caves", CATEGORY_TYPES, true, "If false, other applicable cave types will generate instead.");
    icaves = config.getBoolean("Generate Ice Caves", CATEGORY_TYPES, true, "If false, other applicable cave types will generate instead.");
    jcaves = config.getBoolean("Generate Mushroom Caves", CATEGORY_TYPES, true, "If false, other applicable cave types will generate instead.");
    scaves = config.getBoolean("Generate Sand Caves", CATEGORY_TYPES, true, "If false, other applicable cave types will generate instead.");
    wcaves = config.getBoolean("Generate Water Caves", CATEGORY_TYPES, true, "If false, other applicable cave types will generate instead.");
    lcaves = config.getBoolean("Generate Living Caves", CATEGORY_TYPES, true, "If false, other applicable cave types will generate instead.");
    fcaveslist = config.getStringList("Fire Caves Decoration", CATEGORY_DECO, new String[] { "0.5F", "0.5F", "0.5F", "1.0E-2F", "0.0F", "0.0F" }, "The chances of x decoration spawning, as follows: [Lava stalactites, fiery cave plants, scorched lava stone, skulls, nothing, nothing]");
    icaveslist = config.getStringList("Ice Caves Decoration", CATEGORY_DECO, new String[] { "0.3F", "0.2F", "0.6F", "1.0E-2F", "0.5F", "0.0F" }, "The chances of x decoration spawning, as follows: [Iceshrooms, web, icicles, skulls, stalactites, stalactites]");
    jcaveslist = config.getStringList("Mushroom Caves Decoration", CATEGORY_DECO, new String[] { "0.5F", "0.6F", "0.15F", "0.4F", "0.5F", "0.3F" }, "The chances of x decoration spawning, as follows: [Glowshrooms, vines, web, huge glowshrooms, mushrooms, stalactites]");
    pcaveslist = config.getStringList("Plain Caves Decoration", CATEGORY_DECO, new String[] { "0.15F", "0.2F", "0.5F", "0.1F", "1.0E-2F", "0.0F" }, "The chances of x decoration spawning, as follows: [Vines, web, stalactites, glowshrooms, skulls, nothing]");
    scaveslist = config.getStringList("Sand Caves Decoration", CATEGORY_DECO, new String[] { "0.5F", "0.2F", "0.5F", "1.0E-2F", "0.5F", "0.0F" }, "The chances of x decoration spawning, as follows: [Sand stalactites, web, nothing, skulls, stalactites, nothing]");
    wcaveslist = config.getStringList("Water Caves Decoration", CATEGORY_DECO, new String[] { "0.3F", "0.4F", "0.15F", "0.2F", "1.0E-2F", "0.5F" }, "The chances of x decoration spawning, as follows: [Glowshrooms, water, vines, web, skulls, stalactites]");
    lcaveslist = config.getStringList("Living Caves Decoration", CATEGORY_DECO, new String[] { "0.7F", "0.6F", "0.15F", "0.4F", "0.4F", "0.3F" }, "The chances of x decoration spawning, as follows: [Flowers and dead shrubs, vines, web, trees, mushrooms, stalactites]");
    dimblacklist = config.getStringList("Dimension Blacklist", CATEGORY_REDUX, new String[] { 
          "", "", "", "", "", "", "", "", "", "", 
          "", "", "", "", "", "", "", "", "", "", 
          "", "" }, "Dimensions the changes will mostly not affect.");
    secondYLevelList = config.getStringList("Biomes Second Y Level Affects", CATEGORY_WORLD, new String[] { "Deep Ocean", "Deep Ocean", "Deep Ocean", "Deep Ocean", "Deep Ocean", "Deep Ocean", "Deep Ocean" }, "Biomes that the 'second Y level' will apply to. Replace one or more of the 'Deep Ocean's with another biome to use.");
    if (config.hasChanged())
      config.save(); 
  }
  
  @SubscribeEvent
  public void onConfigChange(ConfigChangedEvent event) {
    if (event.modID.equalsIgnoreCase("immersivecavegen"))
      loadConfiguration(); 
  }
  
  public static Configuration getConfiguration() {
    return config;
  }
}
