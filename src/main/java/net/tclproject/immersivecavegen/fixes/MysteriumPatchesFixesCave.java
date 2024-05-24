package net.tclproject.immersivecavegen.fixes;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.tclproject.immersivecavegen.WGConfig;
import net.tclproject.immersivecavegen.world.CavesDecorator;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;

public class MysteriumPatchesFixesCave {
    private static final Random rand = new Random();

    private static final Random noiseGen = new Random();

    private static final Random caveRNG = new Random();

    public static final Random newRand = new Random();

    private static final Random largeCaveRNG = new Random();

    private static long seedMultiplier;

    private static long regionalCaveSeedMultiplier;

    private static long colossalCaveSeedMultiplier;

    private static int caveOffsetX;

    private static World worldObj;

    private static int caveOffsetZ;

    private static int mineshaftOffsetX;

    private static int mineshaftOffsetZ;

    private static int chunkX_16;

    private static int chunkZ_16;

    private static double chunkCenterX;

    private static double chunkCenterZ;

    private static Block[] chunkData;

    private static final byte[] caveDataArray = new byte[1369];

    private static final float[] ravineData = new float[128];

    private static final float[] ravineHeightLookup = new float[181];

    private static final byte[] biomeList = new byte[256];

    private static final float[] SINE_TABLE = new float[1024];

    private static boolean isInitialized;

    public static MapGenStronghold strongholdGenerator = new MapGenStronghold();

    public static MapGenVillage villageGenerator = new MapGenVillage();

    public static MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();

    public static MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();

    public static int oceanAvg = -1;

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static void func_151542_a(MapGenCaves c, long p_151542_1_, int p_151542_3_, int p_151542_4_, Block[] p_151542_5_, double p_151542_6_, double p_151542_8_, double p_151542_10_) {
    }

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static void func_151541_a(MapGenCaves c, long p_151541_1_, int p_151541_3_, int p_151541_4_, Block[] p_151541_5_, double p_151541_6_, double p_151541_8_, double p_151541_10_, float p_151541_12_, float p_151541_13_, float p_151541_14_, int p_151541_15_, int p_151541_16_, double p_151541_17_) {
    }

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static void func_151538_a(MapGenCaves c, World p_151538_1_, int p_151538_2_, int p_151538_3_, int p_151538_4_, int p_151538_5_, Block[] p_151538_6_) {
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
    public static boolean func_151539_a(MapGenBase instance, IChunkProvider p_151539_1_, World world, int chunkX, int chunkZ, Block[] data) {
        for (String str : WGConfig.dimblacklist) {
            if (world != null && String.valueOf(world.provider.dimensionId).equalsIgnoreCase(str)) return false;
        }
        if (instance instanceof MapGenCaves) {
            if (!isInitialized) initialize(world);
            generate(chunkX, chunkZ, data);
            return true;
        }
        return false;
    }

    private static void initialize(World world) {
        isInitialized = true;
        worldObj = world;
        rand.setSeed(worldObj.getSeed());
        newRand.setSeed(worldObj.getSeed());
        seedMultiplier = rand.nextLong() / 2L * 2L + 1L;
        caveOffsetX = rand.nextInt(128) + 2000000;
        caveOffsetZ = rand.nextInt(128) + 2000000;
        mineshaftOffsetX = rand.nextInt(7) + 2000000;
        mineshaftOffsetZ = rand.nextInt(7) + 2000000;
        byte range = 66;
        int i;
        label36:
        for (i = 0; i < 100; i += 2) {
            colossalCaveSeedMultiplier = seedMultiplier + i;
            for (int z = -range; z <= range; z++) {
                for (int x = -range; x <= range; x++) {
                    if (validColossalCaveLocation(x, z, x * x + z * z)) break label36;
                }
            }
        }
        for (i = 0; i < 100; i += 2) {
            regionalCaveSeedMultiplier = seedMultiplier + i;
            for (int z = -range; z <= range; z += 12) {
                for (int x = -range; x <= range; x += 12) {
                    if (validRegionalCaveLocation(x, z, x * x + z * z) && isGiantCaveRegion(x, z)) return;
                }
            }
        }
    }

    public static void generate(int chunkX, int chunkZ, Block[] data) {
        chunkData = data;
        chunkX_16 = chunkX * 16;
        chunkZ_16 = chunkZ * 16;
        chunkCenterX = (chunkX_16 + 8);
        chunkCenterZ = (chunkZ_16 + 8);
        noiseGen.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
        initializeCaveData(chunkX, chunkZ);
        int index;
        for (index = -12; index <= 12; index++) {
            for (int z = -12; z <= 12; z++) {
                int index1 = index * index + z * z;
                if (index1 <= 145) {
                    int y = chunkX + index;
                    int cz = chunkZ + z;
                    if (y != 0 || cz != 0) {
                        long chunkSeed = (y * 341873128712L + cz * 132897987541L) * seedMultiplier;
                        int genCaves = validCaveLocation(index, z);
                        if (genCaves != 2 && genCaves < 6) {
                            if (genCaves > 0) {
                                if (genCaves == 3) {
                                    rand.setSeed(chunkSeed);
                                    for (int j = 0; j < WGConfig.cavesSpawnMultiplier; ) {
                                        generateRegionalCaves(y, cz);
                                        j++;
                                    }
                                }
                                rand.setSeed(chunkSeed);
                                int i;
                                for (i = 0; i < WGConfig.cavesSpawnMultiplier; ) {
                                    generateCaves(y, cz, index1, genCaves);
                                    i++;
                                }
                                rand.setSeed(chunkSeed);
                                for (i = 0; i < WGConfig.cavernsSpawnMultiplier; ) {
                                    generateRavines(y, cz, (index1 <= 20), genCaves);
                                    i++;
                                }
                            } else if (genCaves < 0 && Math.abs(index) <= 9 && Math.abs(z) <= 9) {
                                rand.setSeed(chunkSeed);
                                for (int i = 0; i < WGConfig.cavesSpawnMultiplier; ) {
                                    generateColossalCaveSystem(y, cz);
                                    i++;
                                }
                            }
                        } else if (index1 <= 65) {
                            rand.setSeed(chunkSeed);
                            for (int i = 0; i < WGConfig.cavesSpawnMultiplier; ) {
                                generateSpecialCaveSystems(y, cz, genCaves);
                                i++;
                            }
                        }
                    }
                }
            }
        }
        for (index = 0; index < 16; index++) {
            for (int z = 0; z < 16; z++) {
                int index1 = index << 12 | z << 8;
                for (int y = 1; y <= 4; y++) {
                    if (chunkData[index1 | y] == Blocks.bedrock) chunkData[index1 | y] = Blocks.stone;
                }
            }
        }
        if ((chunkX & 0x1) == (chunkZ & 0x1) && (biomeList[((worldObj.getBiomeGenForCoordsBody(chunkX_16 + 8, chunkZ_16 + 8)).biomeID > 255) ? 20 : (worldObj.getBiomeGenForCoordsBody(chunkX_16 + 8, chunkZ_16 + 8)).biomeID] & 0x10) != 0) {
            index = rand.nextInt(16) << 12 | rand.nextInt(16) << 8 | rand.nextInt(3) + 1;
            if (chunkData[index] == Blocks.stone) chunkData[index] = Blocks.emerald_ore;
        }
    }

    private static void generateCaves(int chunkX, int chunkZ, int flag, int genCaves) {
        if (rand.nextInt(100) + 1 <= WGConfig.caveNormalReductionPercentage) return;
        int chance = rand.nextInt(15);
        int caveSize = 0;
        if (genCaves == 1 || genCaves == 0) {
            Random r = new Random();
            r.setSeed(worldObj.getSeed());
            if (r.nextInt(100) < WGConfig.giantCaveChance) {
                genCaves = 3;
                chance = 0;
            }
        }
        if (chance == 0) {
            caveSize = rand.nextInt(rand.nextInt(rand.nextInt(40) + 1) + 1);
            boolean blockX = true;
            if (caveSize > 0) {
                int blockZ = chunkX * 16;
                int type = chunkZ * 16;
                int range = chunkX + caveOffsetX + 4;
                int i = chunkZ + caveOffsetZ + 4;
                int y = -1;
                boolean genNormalCaves = (genCaves != 3 && genCaves != 4);
                boolean applyCaveVariation = false;
                long regionSeed = 0L;
                if (blockX && genCaves < 5) {
                    caveRNG.setSeed(((range / 16) * 341873128712L + (i / 16) * 132897987541L) * seedMultiplier);
                    largeCaveRNG.setSeed(rand.nextLong());
                    if (!genNormalCaves || caveRNG.nextInt(4) != 0) {
                        y = (1 << caveRNG.nextInt(3)) - 1;
                        applyCaveVariation = true;
                        regionSeed = caveRNG.nextLong();
                    }
                    if (genCaves != 3 || !isGiantCaveRegion(chunkX, chunkZ))
                        if ((range & 0x7) == 0 && (i & 0x7) == 0 && (range & 0x8) == (i & 0x8)) {
                            generateLargeCave(chunkX, chunkZ, 0);
                        } else if (caveSize <= 3 && largeCaveRNG.nextInt(4) <= y) {
                            int direction = validLargeCaveLocation(chunkX, chunkZ, caveSize);
                            if (direction > 0) {
                                int x = 1;
                                int largerLargeCaves = 30;
                                int y1 = 15;
                                if (largeCaveRNG.nextInt(10) == 0) x += 1 + largeCaveRNG.nextInt(3);
                                for (int curviness = 0; curviness < x; curviness++) {
                                    int z = generateLargeCave(chunkX, chunkZ, x);
                                    largerLargeCaves = Math.min(largerLargeCaves, z);
                                    y1 = Math.max(y1, z);
                                }
                                if (x > 1) {
                                    if (largerLargeCaves < y1)
                                        generateVerticalCave(largeCaveRNG.nextLong(), (blockZ + 8), largerLargeCaves, y1, (type + 8), -1.0F, (blockZ + 8), (type + 8), 1);
                                } else {
                                    generateHorizontalCave(largeCaveRNG.nextLong(), (chunkX * 16 + 8), (y1 & 0xFF), (chunkZ * 16 + 8), largeCaveRNG.nextFloat() * 0.5F + 0.25F, (y1 / 256) / 1024.0F + 3.1415927F, (largeCaveRNG.nextFloat() - 0.5F) / 4.0F, 0, largeCaveRNG.nextInt(direction * 4 + 4) + direction * 20 + 20, 0);
                                }
                            }
                        }
                }
                if (flag <= 40) {
                    boolean var29 = false;
                    int x = 4;
                    boolean var32 = false;
                    y = 10;
                    float var33 = 1.0F;
                    float var35 = 0.1F;
                    boolean var36 = false;
                    boolean seaLevelCaves = false;
                    if (applyCaveVariation) {
                        caveRNG.setSeed(regionSeed);
                        if (caveSize < 20 && genCaves < 5) {
                            var29 = caveRNG.nextBoolean();
                            x = 2 << caveRNG.nextInt(2) + caveRNG.nextInt(2);
                            var32 = caveRNG.nextBoolean();
                            y = 5 << caveRNG.nextInt(2) + caveRNG.nextInt(2);
                        }
                        if (caveRNG.nextBoolean()) {
                            var33 += caveRNG.nextFloat();
                            if (caveRNG.nextBoolean()) var33 /= 2.0F;
                        }
                        if (caveRNG.nextBoolean()) {
                            if (caveRNG.nextBoolean()) var35 /= 2.0F;
                            if (rand.nextBoolean()) {
                                var35 += rand.nextFloat() * var35;
                            } else {
                                var35 += caveRNG.nextFloat() * var35;
                            }
                        }
                        if (caveSize >= 20) {
                            float branchPoint = (caveSize / 10);
                            var33 = (var33 + branchPoint - 1.0F) / branchPoint;
                            var35 = (var35 + (branchPoint - 1.0F) / 10.0F) / branchPoint;
                        }
                        var36 = caveRNG.nextBoolean();
                        seaLevelCaves = caveRNG.nextBoolean();
                    }
                    if (genNormalCaves) {
                        int var38 = caveSize;
                        if (caveSize >= 10) if (var33 > 1.5F) {
                            var38 = caveSize / 2 + 1;
                        } else if (var33 > 1.25F) {
                            var38 = caveSize * 3 / 4 + 1;
                        }
                        if (genCaves == 5 && var38 > 2) var38 = var38 / 4 + 2;
                        Random r = new Random();
                        r.setSeed(worldObj.getSeed());
                        if (r.nextInt(100) < WGConfig.giantCaveChance) {
                            var32 = true;
                            y = 1;
                        }
                        generateCaveSystem(var38, blockZ, type, var33, var35, var32, y, var29, x);
                    }
                    caveSize /= 5;
                    if (caveSize > 0) {
                        if (caveSize < 4) {
                            if (var36) {
                                int var38 = rand.nextInt(rand.nextInt(caveSize + 3) + 1);
                                if (var38 > 2) var38 = 2;
                                for (int length = 0; length < var38; length++) {
                                    int width = Math.max(1, 1 + WGConfig.widthAdditionNormal);
                                    if (WGConfig.hardLimitsEnabled) {
                                        width = Math.min(width, WGConfig.widthMaxNormal);
                                        width = Math.max(width, WGConfig.widthMinNormal);
                                    }
                                    if (rand.nextInt(4) == 0) width += rand.nextInt(4);
                                    for (int LL = 0; LL < width; LL++)
                                        generateSingleCave(blockZ, 3, type, var35);
                                }
                            }
                            if (seaLevelCaves) {
                                int var38 = rand.nextInt(caveSize * 2 + 1);
                                if (var38 > caveSize) var38 = caveSize;
                                int length = 23 - var38 * 5;
                                if (WGConfig.hardLimitsEnabled) {
                                    var38 = Math.min(var38, WGConfig.widthMaxNormal);
                                    var38 = Math.max(var38, WGConfig.widthMinNormal);
                                }
                                for (int width = 0; width < var38; width++)
                                    generateSingleCave(blockZ, rand.nextInt(length) + width * 5 + 40, type, var35);
                            }
                        } else {
                            caveSize /= 2;
                        }
                    } else {
                        caveSize = rand.nextInt(2);
                    }
                }
                if (!blockX && flag <= 65) {
                    float var30 = getDirection(chunkX, chunkZ);
                    if (var30 > -5.0F) {
                        double var31 = (chunkX * 16 + 8);
                        double var34 = (largeCaveRNG.nextInt(20) + 15);
                        double var37 = (chunkZ * 16 + 8);
                        int var38 = 96 + largeCaveRNG.nextInt(32);
                        int length = var38 + 24 + largeCaveRNG.nextInt(16);
                        float var39 = Math.max(1.0F, largeCaveRNG.nextFloat() + 1.0F + WGConfig.widthAdditionNormal);
                        if (WGConfig.hardLimitsEnabled) {
                            var39 = Math.min(var39, WGConfig.widthMaxNormal);
                            var39 = Math.max(var39, WGConfig.widthMinNormal);
                        }
                        generateHorizontalCave(largeCaveRNG.nextLong(), var31, var34, var37, var39, var30, (largeCaveRNG.nextFloat() - 0.5F) / 4.0F, var38, length, 1);
                        generateHorizontalCave(largeCaveRNG.nextLong(), var31, var34, var37, Math.min(var39 * 0.75F, largeCaveRNG.nextFloat() * var39 * 0.75F + var39 * 0.25F), var30 - 1.5707964F, (largeCaveRNG.nextFloat() - 0.5F) / 4.0F, 0, length - var38, -1);
                        generateHorizontalCave(largeCaveRNG.nextLong(), var31, var34, var37, Math.min(var39 * 0.75F, largeCaveRNG.nextFloat() * var39 * 0.75F + var39 * 0.25F), var30 + 1.5707964F, (largeCaveRNG.nextFloat() - 0.5F) / 4.0F, 0, length - var38, -1);
                    }
                }
            } else if (flag <= 65 && blockX && (genCaves == 1 || (genCaves == 3 && !isGiantCaveRegion(chunkX, chunkZ)))) {
                int blockZ = validCaveClusterLocation(chunkX, chunkZ, true, true);
                if (blockZ > 0) generateCaveCluster(chunkX, chunkZ, (genCaves == 3) ? 1 : blockZ);
            }
        }
        if (chance <= 1 && flag <= 40) {
            if (chance == 1) {
                caveSize = rand.nextInt(rand.nextInt(rand.nextInt(40) + 1) + 1) / 5;
                if (caveSize > 3) caveSize /= 2;
            }
            if (caveSize > 0) {
                int type, var28 = chunkX * 16;
                int blockZ = chunkZ * 16;
                try {
                    type = biomeList[(worldObj.getBiomeGenForCoordsBody(var28 + 8, blockZ + 8)).biomeID] & 0x3;
                } catch (ArrayIndexOutOfBoundsException e) {
                    type = 1;
                }
                switch (type) {
                    case 1:
                        caveSize += rand.nextInt(caveSize * (rand.nextInt(2) + 1) + 2);
                        break;
                    case 2:
                        caveSize += rand.nextInt(caveSize + rand.nextInt(2) + 1);
                        break;
                }
                if (caveSize > 0) {
                    if (caveSize > 9) caveSize = 9;
                    int range = 50 - caveSize * 5;
                    chance = 50 + chance * 10;
                    for (int i = 0; i < caveSize; i++) {
                        int y = rand.nextInt(range) + chance + i * 5;
                        if (y < 80 || type < 4) generateSingleCave(var28, y, blockZ, 0.1F);
                    }
                }
            }
        }
    }

    public static int validLargeCaveLocation(int chunkX, int chunkZ, int caves) {
        int flag = 3;
        int caves2 = caves;
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                int x2z2 = x * x + z * z;
                if (x2z2 > 0 && x2z2 <= 10) {
                    caveRNG.setSeed(((chunkX + x) * 341873128712L + (chunkZ + z) * 132897987541L) * seedMultiplier);
                    if (caveRNG.nextInt(15) == 0) {
                        int c = caveRNG.nextInt(caveRNG.nextInt(caveRNG.nextInt(40) + 1) + 1);
                        if (x2z2 <= 5) {
                            caves += c;
                            if (caves > 12) return 0;
                        }
                        caves2 += c;
                        if (caves2 > 6) flag = (caves2 > 12) ? 1 : 2;
                    }
                }
            }
        }
        return flag;
    }

    private static float getDirection(int chunkX, int chunkZ) {
        int caveCountEast = 0;
        int caveCountWest = 0;
        int caveCountSouth = 0;
        int caveCountNorth = 0;
        int caveCountCenter = 0;
        int direction;
        for (direction = -4; direction <= 4; direction++) {
            for (int x = -4; x <= 4; x++) {
                int x2z2 = x * x + direction * direction;
                if (x2z2 <= 17) {
                    largeCaveRNG.setSeed(((chunkX + x) * 341873128712L + (chunkZ + direction) * 132897987541L) * seedMultiplier);
                    if (largeCaveRNG.nextInt(15) == 0) {
                        int caves = largeCaveRNG.nextInt(largeCaveRNG.nextInt(largeCaveRNG.nextInt(40) + 1) + 1);
                        if (x > 0) {
                            caveCountEast += caves;
                        } else if (x < 0) {
                            caveCountWest += caves;
                        }
                        if (direction > 0) {
                            caveCountSouth += caves;
                        } else if (direction < 0) {
                            caveCountNorth += caves;
                        }
                        if (x2z2 <= 4) {
                            caveCountCenter += caves;
                            if (caveCountCenter > 8) return -10.0F;
                        }
                    }
                }
            }
        }
        if (caveCountCenter < 3) return -10.0F;
        direction = 0;
        if (caveCountEast > 0) direction++;
        if (caveCountSouth > 0) direction += 2;
        if (caveCountWest > 0) direction += 4;
        if (caveCountNorth > 0) direction += 8;
        switch (direction) {
            case 0:
                return largeCaveRNG.nextFloat() * 6.2831855F;
            case 1:
                return 3.1415927F + (largeCaveRNG.nextFloat() - 0.5F) * 1.5707964F;
            case 2:
                return 4.712389F + (largeCaveRNG.nextFloat() - 0.5F) * 1.5707964F;
            case 3:
                return 3.926991F + (largeCaveRNG.nextFloat() - 0.5F) * 0.7853982F;
            case 4:
                return (largeCaveRNG.nextFloat() - 0.5F) * 1.5707964F;
            case 5:
                return 1.5707964F + largeCaveRNG.nextInt(2) * 3.1415927F;
            case 6:
                return 5.497787F + (largeCaveRNG.nextFloat() - 0.5F) * 0.7853982F;
            case 7:
                return 4.712389F;
            case 8:
                return 1.5707964F + (largeCaveRNG.nextFloat() - 0.5F) * 1.5707964F;
            case 9:
                return 2.356194F + (largeCaveRNG.nextFloat() - 0.5F) * 0.7853982F;
            case 10:
                return largeCaveRNG.nextInt(2) * 3.1415927F;
            case 11:
                return 3.1415927F;
            case 12:
                return 0.7853982F + (largeCaveRNG.nextFloat() - 0.5F) * 0.7853982F;
            case 13:
                return 1.5707964F;
            case 14:
                return 0.0F;
        }
        return -10.0F;
    }

    private static void generateCaveCluster(int centerX, int centerZ, int clusterType) {
        int caveType, var34;
        centerX = centerX * 16 + 8;
        centerZ = centerZ * 16 + 8;
        boolean comboCave = (clusterType == 3);
        boolean linkCave = (clusterType == 1 || clusterType == 2);
        int quadrant = rand.nextInt(4);
        double y = 3.0D;
        boolean size = true;
        double yInc = 53.0D;
        int mazeIndex1 = -1;
        int mazeIndex2 = -1;
        int mazeType = 0;
        byte centerOffset = 4;
        int caveCount = Math.max(1, 1 + WGConfig.additionalCavesInClusterAmount);
        if (WGConfig.hardLimitsEnabled) {
            caveCount = Math.min(caveCount, WGConfig.maxCavesInClusterAmount);
            caveCount = Math.max(caveCount, WGConfig.minCavesInClusterAmount);
        }
        if (comboCave) {
            var34 = rand.nextInt(6) + 24;
            caveType = rand.nextInt(3);
            mazeIndex1 = rand.nextInt(var34);
            mazeIndex2 = (mazeIndex1 + var34 / 4 + rand.nextInt(var34 / 2)) % var34;
            mazeType = rand.nextInt(2);
            yInc /= (var34 - 1);
            centerOffset = 8;
        } else {
            var34 = 1;
            caveType = rand.nextInt(4);
        }
        for (int caveIndex = 0; caveIndex < var34; caveIndex++) {
            int x, y2, z, i, z1;
            float height;
            int d;
            switch (caveType) {
                case 0:
                    x = 0;
                    if (!comboCave) {
                        x = rand.nextInt(35);
                        y = x;
                        caveCount = rand.nextInt(4) + 3 + WGConfig.additionalCavesInClusterAmount;
                        if (WGConfig.hardLimitsEnabled) {
                            caveCount = Math.min(caveCount, WGConfig.maxCavesInClusterAmount);
                            caveCount = Math.max(caveCount, WGConfig.minCavesInClusterAmount);
                        }
                    }
                    y2 = caveCount;
                    while (y2 > 0) {
                        double var35 = (centerX + (centerOffset + rand.nextInt(5)) * getQuadrantX(y2 + quadrant));
                        double var36 = comboCave ? (y + (rand.nextFloat() * 2.0F - 1.0F)) : y;
                        double var38 = (centerZ + (centerOffset + rand.nextInt(5)) * getQuadrantZ(y2 + quadrant));
                        generateCircularRoom(var35, var36, var38, rand.nextFloat() * rand.nextFloat() * (comboCave ? 9.0F : 6.0F) + 3.0F);
                        generateDirectionalCave((int) (var35 + 0.5D), (int) (var36 + 0.5D), (int) (var38 + 0.5D), centerX, centerZ, 0);
                        if (!comboCave) {
                            y += (rand.nextInt(4) + 2);
                            if (y2 == 1)
                                generateVerticalCave(rand.nextLong(), (centerX + rand.nextInt(5) - 2), x, (int) (var36 + 0.5D), (centerZ + rand.nextInt(5) - 2), 0.0F, centerX, centerZ, 8);
                        }
                        if (linkCave && y2 == caveCount / 2)
                            generateHorizontalLinkCave((int) var35, (int) var36, (int) var38, centerX, centerZ, 2 - clusterType);
                        y2--;
                    }
                    break;
                case 1:
                    if (!comboCave) {
                        y = rand.nextInt(35);
                        caveCount = rand.nextInt(4) + 3 + WGConfig.additionalCavesInClusterAmount;
                        if (WGConfig.hardLimitsEnabled) {
                            caveCount = Math.min(caveCount, WGConfig.maxCavesInClusterAmount);
                            caveCount = Math.max(caveCount, WGConfig.minCavesInClusterAmount);
                        }
                    }
                    y2 = caveCount;
                    while (y2 > 0) {
                        double var35 = (centerX + (centerOffset + rand.nextInt(5)) * getQuadrantX(y2 + quadrant));
                        double var36 = comboCave ? (y + (rand.nextFloat() * 2.0F - 1.0F)) : y;
                        double var38 = (centerZ + (centerOffset + rand.nextInt(5)) * getQuadrantZ(y2 + quadrant));
                        generateRavineCave(var35, var36, var38, comboCave ? 2.0F : 1.0F);
                        if (!comboCave) y += (rand.nextInt(3) + 3);
                        if (linkCave && y2 == caveCount / 2)
                            generateHorizontalLinkCave((int) var35, (int) var36, (int) var38, centerX, centerZ, 4 - clusterType);
                        y2--;
                    }
                    break;
                case 2:
                    if (!comboCave) {
                        y = rand.nextInt(8);
                        caveCount = rand.nextInt(4) + 3 + WGConfig.additionalCavesInClusterAmount;
                        if (WGConfig.hardLimitsEnabled) {
                            caveCount = Math.min(caveCount, WGConfig.maxCavesInClusterAmount);
                            caveCount = Math.max(caveCount, WGConfig.minCavesInClusterAmount);
                        }
                    }
                    y2 = caveCount;
                    while (y2 > 0) {
                        double var35 = (centerX + (centerOffset + rand.nextInt(5)) * getQuadrantX(y2 + quadrant));
                        int j = Math.round(comboCave ? (((float) y + rand.nextFloat() * 2.0F - 1.0F) / 1.5F) : (float) y);
                        double var37 = (centerZ + (centerOffset + rand.nextInt(5)) * getQuadrantZ(y2 + quadrant));
                        int length = Math.max(0, j - rand.nextInt(5) + WGConfig.additionalCavesInClusterLength);
                        if (WGConfig.hardLimitsEnabled) {
                            length = Math.min(length, WGConfig.maxCavesInClusterLength);
                            length = Math.max(length, WGConfig.minCavesInClusterLength);
                        }
                        int dirSwitch = j + Math.min(40 + rand.nextInt(16), 24 + rand.nextInt(36 - j / 2));
                        float var39 = rand.nextFloat() * rand.nextFloat() * 2.0F;
                        if (rand.nextInt(10) == 0)
                            var39 = Math.min(8.0F, var39 * (rand.nextFloat() * 3.0F + 1.0F) + 2.0F);
                        if ((y2 + caveIndex & 0x1) == 0) {
                            generateVerticalCave(rand.nextLong(), var35, length, dirSwitch, var37, var39, centerX, centerZ, comboCave ? 24 : 16);
                        } else {
                            generateVerticalCave(rand.nextLong(), var35, dirSwitch - length, 0, var37, var39, centerX, centerZ, comboCave ? 24 : 16);
                        }
                        if (!comboCave) y += (rand.nextInt(4) + 3);
                        if (linkCave && y2 == caveCount / 2)
                            generateHorizontalLinkCave(centerX, rand.nextInt(10) + 10, centerZ, centerX, centerZ, 4 - clusterType);
                        y2--;
                    }
                    break;
                case 3:
                    y2 = comboCave ? (rand.nextInt(4) * 2 + mazeType) : rand.nextInt(8);
                    z = centerX + (4 + rand.nextInt(5)) * getQuadrantX(quadrant);
                    i = comboCave ? Math.max(3, Math.round((float) y)) : (rand.nextInt(50) + 3);
                    z1 = centerZ + (4 + rand.nextInt(5)) * getQuadrantZ(quadrant);
                    height = (i == 3) ? 2.625F : 1.625F;
                    for (d = 0; d < 4; d++) {
                        y2 += 2;
                        int length = Math.max(0, rand.nextInt(8) + WGConfig.additionalCavesInClusterLength);
                        if (WGConfig.hardLimitsEnabled) {
                            length = Math.min(length, WGConfig.maxCavesInClusterLength);
                            length = Math.max(length, WGConfig.minCavesInClusterLength);
                        }
                        if ((y2 & 0x1) == 0) {
                            length += 24;
                        } else {
                            length += 17;
                        }
                        generateMazeCaveSegment(z, i, z1, y2, length, height);
                        int dirSwitch = rand.nextInt(2) * 4 + 2;
                        int maxOffset = length * 3 / 4;
                        int offset;
                        for (offset = length / 5 + rand.nextInt(length / 4); offset < maxOffset; offset += length / 6 + rand.nextInt(length / 4) + 1) {
                            dirSwitch += 4;
                            int x2 = getOffsetX(z, y2, offset);
                            int z2 = getOffsetZ(z1, y2, offset);
                            int direction2 = y2 + dirSwitch;
                            int length2 = length / 3 + rand.nextInt(length / 3);
                            if ((direction2 & 0x1) == 1) length2 -= offset / 4;
                            generateMazeCaveSegment(x2, i, z2, direction2, length2, height);
                            if (linkCave && d == 0) {
                                linkCave = false;
                                x2 = getOffsetX(x2, direction2, length2);
                                z2 = getOffsetZ(z2, direction2, length2);
                                generateHorizontalLinkCave(x2, i, z2, centerX, centerZ, 2 - clusterType);
                            }
                            if (offset > length / 2) {
                                offset += rand.nextInt(length / 6) + 2;
                                x2 = getOffsetX(z, y2, offset);
                                z2 = getOffsetZ(z1, y2, offset);
                                direction2 += 4;
                                length2 = length / 3 + rand.nextInt(length / 3);
                                if ((direction2 & 0x1) == 1) length2 -= offset / 4;
                                generateMazeCaveSegment(x2, i, z2, direction2, length2, height);
                            }
                        }
                    }
                    mazeType++;
                    break;
            }
            quadrant++;
            y += yInc;
            if (caveIndex == mazeIndex1) {
                caveType = 3;
                x = centerX + (centerOffset + rand.nextInt(5)) * getQuadrantX(quadrant);
                y2 = rand.nextInt(16) + 16;
                z = centerZ + (centerOffset + rand.nextInt(5)) * getQuadrantZ(quadrant);
                generateVerticalCave(rand.nextLong(), x, y2, 100, z, rand.nextFloat(), centerX, centerZ, 24);
                generateHorizontalLinkCave(x, y2, z, centerX, centerZ, 0);
                generateCircularRoom(x, y2, z, rand.nextFloat() * rand.nextFloat() * 4.0F + 4.0F);
                quadrant++;
                x = centerX + (centerOffset + rand.nextInt(5)) * getQuadrantX(quadrant);
                y2 = rand.nextInt(16) + 32;
                z = centerZ + (centerOffset + rand.nextInt(5)) * getQuadrantZ(quadrant);
                generateVerticalCave(rand.nextLong(), x, y2, 100, z, rand.nextFloat(), centerX, centerZ, 24);
                quadrant++;
                if (mazeIndex1 > 0 && mazeIndex2 > 0) for (i = rand.nextInt(3) + 4; i > 0; i--) {
                    x = centerX + (centerOffset + 8 + rand.nextInt(16)) * getQuadrantX(quadrant);
                    z = centerZ + (centerOffset + 8 + rand.nextInt(16)) * getQuadrantZ(quadrant);
                    generateDirectionalCave(x, 3, z, centerX, centerZ, 0);
                    quadrant++;
                }
            } else if (caveIndex == mazeIndex2) {
                caveType = 3;
            } else {
                caveType = (caveType + 1) % 3;
            }
        }
    }

    private static int validCaveClusterLocation(int chunkX, int chunkZ, boolean includeCaves, boolean firstCheck) {
        byte type = 3;
        int count = 0;
        int radius2;
        for (radius2 = -3; radius2 <= 3; radius2++) {
            for (int x = -3; x <= 3; x++) {
                int z = radius2 * radius2 + x * x;
                if (z <= 13) {
                    int x2z2 = chunkX + radius2;
                    int chunkOffZ = chunkZ + x;
                    if (includeCaves && z != 0) {
                        caveRNG.setSeed((x2z2 * 341873128712L + chunkOffZ * 132897987541L) * seedMultiplier);
                        if (caveRNG.nextInt(15) == 0) {
                            int chunkModX = caveRNG.nextInt(caveRNG.nextInt(caveRNG.nextInt(40) + 1) + 1);
                            if (chunkModX > 0) {
                                if (!firstCheck || z <= 5) return 0;
                                count += chunkModX;
                                if (type == 3) type = 2;
                                if (type == 2 && count > 10) type = 1;
                            }
                        }
                    }
                    caveRNG.setSeed((x2z2 * 341873128712L + chunkOffZ * 132897987541L) * seedMultiplier);
                    if (caveRNG.nextInt(20) == 15) {
                        int chunkModX = x2z2 + caveOffsetX + 4;
                        int chunkModZ = chunkOffZ + caveOffsetZ + 4;
                        boolean ravine = false;
                        if ((chunkModX & 0x7) == 0 && (chunkModZ & 0x7) == 0 && (chunkModX & 0x8) != (chunkModZ & 0x8)) {
                            ravine = true;
                        } else if (caveRNG.nextInt(25) < 19 && chunkModX % 3 == 0 && chunkModZ % 3 == 0 && (chunkModX / 3 & 0x1) == (chunkModZ / 3 & 0x1)) {
                            ravine = true;
                        }
                        if (ravine || caveRNG.nextInt(30) < 11) {
                            if (!firstCheck || z <= 5) return 0;
                            count += 6;
                            if (type == 3) type = 2;
                            if (type == 2 && count > 10) type = 1;
                        }
                    }
                    x2z2 += mineshaftOffsetX;
                    chunkOffZ += mineshaftOffsetZ;
                    if ((x2z2 / 7 & 0x1) != (chunkOffZ / 7 & 0x1)) {
                        int chunkModX = x2z2 % 7;
                        int chunkModZ = chunkOffZ % 7;
                        if (chunkModX <= 2 && chunkModZ <= 2) {
                            caveRNG.setSeed(((x2z2 / 7) * 341873128712L + (chunkOffZ / 7) * 132897987541L) * seedMultiplier);
                            if (chunkModX == caveRNG.nextInt(3) && chunkModZ == caveRNG.nextInt(3)) {
                                if (!firstCheck || z <= 5) return 0;
                                count += 6;
                                if (type == 3) type = 2;
                                if (type == 2 && count > 10) type = 1;
                            }
                        }
                    }
                }
            }
        }
        if (!includeCaves) return 1;
        if (type > 1 && firstCheck) {
            radius2 = type * type + 1;
            for (int x = -type; x <= type; x++) {
                for (int z = -type; z <= type; z++) {
                    int x2z2 = x * x + z * z;
                    if (x2z2 > 0 && x2z2 <= radius2) {
                        caveRNG.setSeed(((chunkX + x) * 341873128712L + (chunkZ + z) * 132897987541L) * seedMultiplier);
                        if (caveRNG.nextInt(15) == 0 && caveRNG.nextInt(caveRNG.nextInt(caveRNG.nextInt(40) + 1) + 1) == 0 && validCaveClusterLocation(chunkX + x, chunkZ + z, true, false) == 3)
                            return (type == 2) ? 4 : 1;
                    }
                }
            }
        }
        return type;
    }

    private static void generateCaveSystem(int size, int centerX, int centerZ, float widthMultiplier, float curviness, boolean largerLargeCaves, int largeCaveChance, boolean largerCircularRooms, int circularRoomChance) {
        byte spread = 16;
        if (curviness >= 0.15F) {
            spread = 32;
            centerX -= 8;
            centerZ -= 8;
        }
        for (int i = 0; i < size; i++) {
            double x = (centerX + rand.nextInt(spread));
            double y = (rand.nextInt(rand.nextInt(120) + 8) - 7);
            double z = (centerZ + rand.nextInt(spread));
            int caves = 1;
            if (rand.nextInt(circularRoomChance) == 0) {
                int startDirection = rand.nextInt(4);
                caves += startDirection;
                float j = rand.nextFloat() * 6.0F + 1.0F;
                if (largerCircularRooms && rand.nextInt(16 / circularRoomChance) == 0) {
                    j = j * (rand.nextFloat() * rand.nextFloat() + 1.0F) + 3.0F;
                    if (j > 8.5F) {
                        caves += 2;
                        if (y < 4.5D) {
                            y += 12.0D;
                        } else if (y > 62.5D) {
                            y -= 47.0D;
                            if (y > 62.5D) y -= 20.0D;
                        }
                    }
                }
                if (widthMultiplier < 1.0F) {
                    if (j > 8.5F) j *= widthMultiplier;
                } else {
                    j *= widthMultiplier;
                    if (startDirection == 0 && j > 10.0F && j < 17.0F) {
                        float width = (j - 10.0F) / 7.0F + WGConfig.widthAdditionNormal;
                        width = Math.max((j - 10.0F) / 10.0F, width);
                        if (WGConfig.hardLimitsEnabled) {
                            width = Math.min(width, WGConfig.widthMaxNormal);
                            width = Math.max(width, WGConfig.widthMinNormal);
                        }
                        j *= rand.nextFloat() * (1.0F - width) + 1.0F + width;
                    }
                    if (j > 15.5F) {
                        if (y < 4.5D) {
                            y += 12.0D;
                        } else if (y > 62.5D) {
                            y -= 47.0D;
                            if (y > 62.5D) y -= 20.0D;
                        }
                        if (y < 9.5D) {
                            y += (j / 8.0F + 0.5F);
                        } else if (y > 52.5D) {
                            y -= (j / 4.0F + 0.5F);
                        }
                    }
                }
                generateCircularRoom(x, y, z, j);
            }
            float var23 = rand.nextFloat() * 6.2831855F;
            for (int var24 = 0; var24 < caves; var24++) {
                float width = rand.nextFloat() * 2.0F + rand.nextFloat() + WGConfig.widthAdditionNormal;
                width = Math.max(rand.nextFloat() * 1.5F, width);
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, WGConfig.widthMaxNormal);
                    width = Math.max(width, WGConfig.widthMinNormal);
                }
                if (rand.nextInt(largeCaveChance) == 0) {
                    width *= rand.nextFloat() * rand.nextFloat() * 4.0F + 1.0F;
                    if (largerLargeCaves) {
                        if (widthMultiplier < 1.0F) {
                            if (width > 7.5F) width *= widthMultiplier;
                        } else if (width < 7.5F) {
                            width *= widthMultiplier;
                        }
                    } else if (width > 8.5F) {
                        width = 8.5F;
                    }
                } else {
                    width *= widthMultiplier;
                }
                float direction = var23;
                if (var24 > 0)
                    direction = var23 + 6.2831855F * var24 / caves + (rand.nextFloat() - 0.5F) * 6.2831855F / caves;
                generateCave(rand.nextLong(), x, y, z, width, direction, (rand.nextFloat() - 0.5F) / 4.0F, 0, 0, curviness);
            }
        }
    }

    private static void generateColossalCaveSystem(int centerX, int centerZ) {
        if (rand.nextInt(100) + 1 <= WGConfig.caveColossalReductionPercentage) return;
        centerX *= 16;
        centerZ *= 16;
        int caveType = rand.nextInt(5);
        int caveCounter = rand.nextInt(200);
        if (caveType < 4) {
            int z = rand.nextInt(2);
            for (int x = 0; x < 8; x++) {
                int x2z2 = centerX;
                int offset = centerZ;
                switch (caveType) {
                    case 0:
                        offset = centerZ + x * 16 - 56;
                        break;
                    case 1:
                        x2z2 = centerX + x * 16 - 56;
                        break;
                    case 2:
                        x2z2 = centerX + x * 11 - 38;
                        offset = centerZ + x * 12 - 42;
                        break;
                    case 3:
                        x2z2 = centerX + 38 - x * 11;
                        offset = centerZ + x * 12 - 42;
                        break;
                }
                int size = (x + z & 0x1) + 12;
                if (caveType == 1) {
                    caveCounter = generateCCCaveSystem(size, x2z2, offset - 8, caveCounter);
                    caveCounter = generateCCCaveSystem(25 - size, x2z2, offset + 8, caveCounter);
                } else {
                    caveCounter = generateCCCaveSystem(size, x2z2 - 8, offset, caveCounter);
                    caveCounter = generateCCCaveSystem(25 - size, x2z2 + 8, offset, caveCounter);
                }
            }
        } else {
            for (int z = -2; z <= 2; z++) {
                for (int x = -2; x <= 2; x++) {
                    int x2z2 = x * x + z * z;
                    if (x2z2 > 0 && x2z2 <= 5) {
                        int offset = (x != 0 && z != 0) ? 16 : 20;
                        caveCounter = generateCCCaveSystem(10, centerX + x * offset, centerZ + z * offset, caveCounter);
                    }
                }
            }
        }
        centerX += 8;
        centerZ += 8;
        for (byte b = -32; b <= 32; b += 64) {
            for (int x = -32; x <= 32; x += 64)
                generateHorizontalLinkCave(centerX + x, rand.nextInt(15) + 15, centerZ + b, centerX, centerZ, 4);
        }
    }

    private static int generateCCCaveSystem(int size, int centerX, int centerZ, int caveCounter) {
        for (int i = 0; i < size; i++) {
            double x = (centerX + rand.nextInt(16));
            double z = (centerZ + rand.nextInt(16));
            int index12 = caveCounter % 12;
            double y = -7.0D;
            if (index12 < 9) {
                int width = Math.max(caveCounter % 3, caveCounter % 3 + WGConfig.widthAdditionColossal);
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, WGConfig.widthMaxColossal);
                    width = Math.max(width, WGConfig.widthMinColossal);
                }
                if (width < 2) {
                    y += (width * 10 + rand.nextInt(10));
                } else {
                    y += (index12 * 3 + 14 + rand.nextInt(9));
                }
            } else if (index12 == 9) {
                y += (47 + rand.nextInt(11));
            } else if (index12 == 10) {
                y += (58 + rand.nextInt(13));
            } else {
                y += (71 + rand.nextInt(20));
            }
            if (caveCounter % 7 == 0) generateCircularRoom(x, y, z, rand.nextFloat() * 5.0F + 2.0F);
            float var14 = rand.nextFloat() * 2.0F + rand.nextFloat();
            if (caveCounter % 19 == 0) {
                if (var14 < 1.5F) var14 += (var14 + 3.0F) / 3.0F;
                var14++;
            }
            generateCCCave(rand.nextLong(), x, y, z, var14, rand.nextFloat() * 6.2831855F, (rand.nextFloat() - 0.5F) / 4.0F, 0, (caveCounter % 5 == 0), (centerX + 8), (centerZ + 8));
            caveCounter++;
        }
        return caveCounter;
    }

    private static void generateCCCave(long seed, double x, double y, double z, float width, float directionXZ, float directionY, int pos, boolean isVerticalCave, double centerX, double centerZ) {
        caveRNG.setSeed(seed);
        float var23 = 0.0F;
        float var24 = 0.0F;
        for (int branchPoint = (pos == 0) ? 49 : -999; pos < 98; pos++) {
            if (pos == branchPoint) {
                seed = caveRNG.nextLong();
                width = caveRNG.nextFloat() * 0.5F + 0.5F;
                isVerticalCave = (caveRNG.nextInt(6) == 0);
                directionY /= 3.0F;
                generateCCCave(caveRNG.nextLong(), x, y, z, caveRNG.nextFloat() * 0.5F + 0.5F, directionXZ - 1.5707964F, directionY, pos, (caveRNG.nextInt(6) == 0), centerX, centerZ);
                generateCCCave(seed, x, y, z, width, directionXZ + 1.5707964F, directionY, pos, isVerticalCave, centerX, centerZ);
                return;
            }
            double radiusW = (1.5F + sine(pos * 0.0320571F) * width);
            double var35 = x - chunkCenterX;
            double var37 = z - chunkCenterZ;
            double var39 = (116 - pos) + radiusW;
            if (var35 * var35 + var37 * var37 > var39 * var39) return;
            if (caveRNG.nextInt(4) == 0) radiusW = radiusW / 5.0D + 0.75D;
            float var33 = cosine(directionY);
            x += (cosine(directionXZ) * var33);
            y += sine(directionY);
            z += (sine(directionXZ) * var33);
            if (isVerticalCave) {
                directionY *= 0.92F;
            } else {
                directionY *= 0.7F;
            }
            float devX = (float) (x - centerX);
            float devZ = (float) (z - centerZ);
            if (devX * devX + devZ * devZ > 576.0F) if (devZ >= 0.0F) {
                if (devX >= 0.0F) {
                    directionXZ = (directionXZ * 31.0F - 2.35619F) / 32.0F;
                } else {
                    directionXZ = (directionXZ * 31.0F - 0.7853982F) / 32.0F;
                }
            } else if (devX >= 0.0F) {
                directionXZ = (directionXZ * 31.0F + 2.35619F) / 32.0F;
            } else {
                directionXZ = (directionXZ * 31.0F + 0.7853982F) / 32.0F;
            }
            directionY += var24 * 0.1F;
            directionXZ += var23 * 0.1F;
            var24 *= 0.9F;
            var23 *= 0.75F;
            var24 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 2.0F;
            var23 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 4.0F;
            double radiusW_2 = radiusW + 9.0D;
            if (x >= chunkCenterX - radiusW_2 && x <= chunkCenterX + radiusW_2 && z >= chunkCenterZ - radiusW_2 && z <= chunkCenterZ + radiusW_2) {
                double noiseMultiplier = 0.275D / Math.max(radiusW - 1.0D, 0.916666D);
                generateCaveSegment(x, y, z, radiusW, radiusW, noiseMultiplier, 1);
            }
        }
    }

    private static void generateSpecialCaveSystems(int chunkX, int chunkZ, int type) {
        int centerX = chunkX * 16 + 8;
        int centerZ = chunkZ * 16 + 8;
        if (type == 2) {
            if ((chunkX + caveOffsetX - 15 & 0x20) == (chunkZ + caveOffsetZ - 15 & 0x20)) {
                if (rand.nextInt(100) + 1 <= WGConfig.caveCircularRoomReductionPercentage) return;
                generateCircularRoomCaveSystem(centerX, centerZ);
            } else {
                if (rand.nextInt(100) + 1 <= WGConfig.caveRavineCaveReductionPercentage) return;
                generateRavineCaveSystem(centerX, centerZ);
            }
        } else if (type == 6) {
            if (rand.nextInt(100) + 1 <= WGConfig.caveVerticalReductionPercentage) return;
            generateVerticalCaveSystem(centerX, centerZ);
        } else {
            if (rand.nextInt(100) + 1 <= WGConfig.caveMazeReductionPercentage) return;
            generateMazeCaveSystem(centerX, centerZ);
        }
    }

    private static void generateCircularRoomCaveSystem(int centerX, int centerZ) {
        int caveSize = rand.nextInt(15) + 35;
        double yInc = 39.0D / caveSize;
        double y = 0.0D;
        int offset = rand.nextInt(4);
        int centerCave = rand.nextInt(2);
        for (int i = 0; i < caveSize; i++) {
            int x, z, x2z2;
            do {
                x = rand.nextInt(33);
                z = rand.nextInt(33);
                x2z2 = x * x + z * z;
            } while (x2z2 < 101 || x2z2 > 1025);
            x *= getQuadrantX(i + offset);
            z *= getQuadrantZ(i + offset);
            x += centerX;
            y += (i / (caveSize - 1) + 1.0D) * yInc;
            z += centerZ;
            generateCircularRoom(x, y, z, rand.nextFloat() * rand.nextFloat() * 9.0F + 3.0F);
            generateDirectionalCave(x, (int) (y + 0.5D), z, centerX, centerZ, 16);
            if (i < 2) {
                x = centerX + (rand.nextInt(9) + 8) * (rand.nextInt(2) * 2 - 1);
                int y2 = rand.nextInt(8) + i * 16 + 16;
                z = centerZ + (rand.nextInt(9) + 8) * (rand.nextInt(2) * 2 - 1);
                generateVerticalCave(x, y2, 100, z);
                generateCircularRoom(x, y2, z, rand.nextFloat() * rand.nextFloat() * 9.0F + 3.0F);
                generateDirectionalCave(x, y2, z, centerX, centerZ, 999);
                generateHorizontalLinkCave(x, y2, z, centerX, centerZ, 0);
            }
            if ((i & 0x7) == centerCave) {
                x = centerX + (rand.nextInt(6) + 3) * (rand.nextInt(2) * 2 - 1);
                double var16 = y + (rand.nextInt(9) - 4);
                if (var16 < 0.0D) var16 += 4.0D;
                z = centerZ + (rand.nextInt(6) + 3) * (rand.nextInt(2) * 2 - 1);
                generateCircularRoom(x, y, z, rand.nextFloat() * rand.nextFloat() * 9.0F + 3.0F);
                if (i == centerCave) generateVerticalCave(x, 3, 32, z);
            }
        }
    }

    private static void generateRavineCaveSystem(int centerX, int centerZ) {
        int caveSize = rand.nextInt(10) + 30;
        double yInc = 39.0D / caveSize;
        double y = 0.0D;
        int offset = rand.nextInt(4);
        int vertCave1 = caveSize / 4 + rand.nextInt(3);
        int vertCave2 = caveSize / 3 + rand.nextInt(3);
        int centerCave = rand.nextInt(3);
        for (int i = 0; i < caveSize; i++) {
            int x, z, x2z2;
            do {
                x = rand.nextInt(33);
                z = rand.nextInt(33);
                x2z2 = x * x + z * z;
            } while (x2z2 < 145 || x2z2 > 1025);
            x *= getQuadrantX(i + offset);
            z *= getQuadrantZ(i + offset);
            x += centerX;
            y += (i / (caveSize - 1) + 1.0D) * yInc;
            z += centerZ;
            generateRavineCave(x, y, z, 2.0F);
            if (i == vertCave1 || i == vertCave2) {
                generateVerticalCave(x, (int) (y + 0.5D), 100, z);
                generateHorizontalLinkCave(x, (int) (y + 0.5D), z, centerX, centerZ, 0);
            }
            if (i % 7 == centerCave) {
                x = centerX + (rand.nextInt(6) + 3) * (rand.nextInt(2) * 2 - 1);
                double y2 = y + (rand.nextInt(5) - 2);
                if (y2 < 0.0D) y2 += 4.0D;
                z = centerZ + (rand.nextInt(6) + 3) * (rand.nextInt(2) * 2 - 1);
                int length = rand.nextInt(17) + 26;
                int segmentLength = length + rand.nextInt(2) * 8;
                float width = rand.nextFloat() * rand.nextFloat() + 1.0F;
                float height = rand.nextFloat() * 2.0F + 2.0F;
                float direction = rand.nextFloat() * 0.7853982F + i * 0.112199F;
                float directionY = (rand.nextFloat() - 0.5F) / 4.0F;
                float slope = (rand.nextFloat() * 0.75F + 0.25F) * 0.25F * (rand.nextInt(2) * 2 - 1);
                generateRavineCaveSegment(rand.nextLong(), x, y, z, width, direction, directionY, slope, segmentLength, height);
                segmentLength = length + rand.nextInt(2) * 8;
                generateRavineCaveSegment(rand.nextLong(), x, y, z, width, direction + 3.1415927F, -directionY, -slope, segmentLength, height);
            }
        }
    }

    private static void generateVerticalCaveSystem(int centerX, int centerZ) {
        int caveSize = rand.nextInt(15) + 45;
        double yInc = 39.0D / caveSize;
        double y = 0.0D;
        int offset = rand.nextInt(4);
        int horizCave = rand.nextInt(3);
        int deepHorizCave = 6 + rand.nextInt(3);
        int vertCave1 = caveSize / 3 + (rand.nextInt(5) - 2) * 2;
        int vertCave2 = caveSize / 3 + (rand.nextInt(5) - 2) * 2 + rand.nextInt(2) * 2 - 1;
        int largeCaveChance = (caveSize - 29) / 5;
        largeCaveChance += rand.nextInt(8 - largeCaveChance) + 5;
        int largeCaveOffset = rand.nextInt(largeCaveChance);
        for (int i = 0; i < caveSize; i++) {
            int x, z, x2z2;
            do {
                x = rand.nextInt(33);
                z = rand.nextInt(33);
                x2z2 = x * x + z * z;
            } while (x2z2 < 101 || x2z2 > 1025);
            x *= getQuadrantX(i + offset);
            z *= getQuadrantZ(i + offset);
            y += (i / (caveSize - 1) + 1.0D) * yInc;
            int y2 = (int) y;
            if (i < deepHorizCave)
                generateDirectionalCave(centerX - x - ((x >= 0) ? 8 : -8), 3, centerZ - z - ((z >= 0) ? 8 : -8), centerX, centerZ, 0);
            if (i % 3 == horizCave)
                generateDirectionalCave(centerX + x + ((x >= 0) ? 8 : -8), y2, centerZ + z + ((z >= 0) ? 8 : -8), centerX, centerZ, 0);
            if (i == vertCave1 || i == vertCave2)
                generateHorizontalLinkCave(centerX - x, y2, centerZ - z, centerX, centerZ, 2);
            y2 /= 3;
            int minY = y2 - rand.nextInt(5);
            int maxY = y2 + 32 + rand.nextInt(33 - y2);
            float width = Math.max(0.5F, rand.nextFloat() * rand.nextFloat() * 2.0F + WGConfig.caveVerticalReductionPercentage);
            if (i % largeCaveChance == largeCaveOffset)
                width = Math.min(8.0F, Math.max(width * 3.0F, width * (rand.nextFloat() * 3.0F + 1.0F) + 2.0F) + WGConfig.caveVerticalReductionPercentage);
            x += centerX;
            z += centerZ;
            if ((i + offset & 0x4) == 0) {
                generateVerticalCave(rand.nextLong(), x, minY, maxY, z, width, centerX, centerZ, 40);
            } else {
                generateVerticalCave(rand.nextLong(), x, maxY - minY, 0, z, width, centerX, centerZ, 40);
            }
            if (i == vertCave1 || i == vertCave2) {
                x = centerX + (rand.nextInt(6) + 3) * (rand.nextInt(2) * 2 - 1);
                z = centerZ + (rand.nextInt(6) + 3) * (rand.nextInt(2) * 2 - 1);
                if (i == vertCave1) {
                    generateVerticalCave(x, minY, 100, z);
                } else {
                    generateVerticalCave(x, 100, 0, z);
                }
            }
        }
    }

    private static void generateMazeCaveSystem(int centerX, int centerZ) {
        int vertCave2;
        boolean direction = false;
        int oldDirection = 0;
        boolean change = false;
        int yInc = 7 + rand.nextInt(2);
        byte minY = 3;
        byte maxY = 59;
        int quadrant = rand.nextInt(4);
        int oldQuadrant = quadrant;
        float height = 2.625F;
        int horizCave1 = rand.nextInt(4);
        int horizCave2 = rand.nextInt(4);
        do {
            horizCave2 = rand.nextInt(4);
        } while (horizCave1 == horizCave2);
        horizCave1 = minY + (horizCave1 + 1) * yInc + rand.nextInt(4);
        horizCave2 = minY + (horizCave2 + 1) * yInc + rand.nextInt(4);
        int vertCave1 = maxY + rand.nextInt(4);
        do {
            vertCave2 = maxY + rand.nextInt(4);
        } while (vertCave1 == vertCave2);
        int caveCount = rand.nextInt(2);
        int caveY;
        for (caveY = minY; caveY <= maxY; caveY += yInc) {
            int length, caveX = centerX + (rand.nextInt(7) + 4) * getQuadrantX(quadrant);
            int caveZ = centerZ + (rand.nextInt(7) + 4) * getQuadrantZ(quadrant);
            int var31 = rand.nextInt(2);
            if (change && var31 == oldDirection) var31 = 1 - var31;
            change = (var31 == oldDirection);
            oldDirection = var31;
            var31 += rand.nextInt(4) * 2;
            for (int d = 0; d < 4; d++) {
                var31 += 2;
                if ((var31 & 0x1) == 0) {
                    length = 28 + rand.nextInt(20);
                } else {
                    length = 20 + rand.nextInt(20);
                }
                generateMazeCaveSegment(caveX, caveY, caveZ, var31, length, height);
                int dirSwitch = rand.nextInt(2) * 4 + 2;
                int maxOffset = length * 3 / 4;
                int offset;
                for (offset = length / 5 + rand.nextInt(length / 4); offset < maxOffset; offset += length / 6 + rand.nextInt(length / 4) + 1) {
                    dirSwitch += 4;
                    int x = getOffsetX(caveX, var31, offset);
                    int z = getOffsetZ(caveZ, var31, offset);
                    int direction2 = var31 + dirSwitch;
                    int length2 = length / 3 + rand.nextInt(length / 3);
                    if ((direction2 & 0x1) == 1) length2 -= offset / 4;
                    generateMazeCaveSegment(x, caveY, z, direction2, length2, height);
                    int index = caveY + (direction2 / 2 & 0x3);
                    if (index != horizCave1 && index != horizCave2) {
                        if (index == vertCave1 || index == vertCave2) {
                            int offset2 = length2 / 4 + rand.nextInt(length2 / 2 + 1) + 1;
                            x = getOffsetX(x, direction2, offset2);
                            z = getOffsetZ(z, direction2, offset2);
                            if (index == vertCave1) {
                                vertCave1 = -999;
                                generateVerticalCave(rand.nextLong(), x, caveY, minY + yInc * 2, z, 0.0F, centerX, centerZ, 32);
                            } else {
                                vertCave2 = -999;
                                generateVerticalCave(rand.nextLong(), x, caveY, 100, z, 0.0F, 0.0D, 0.0D, 0);
                            }
                        }
                    } else {
                        if (index == horizCave1) {
                            horizCave1 = -999;
                        } else {
                            horizCave2 = -999;
                        }
                        x = getOffsetX(x, direction2, length2);
                        z = getOffsetZ(z, direction2, length2);
                        generateHorizontalLinkCave(x, caveY, z, centerX, centerZ, 0);
                    }
                    if (offset > length / 2) {
                        offset += rand.nextInt(length / 6) + 2;
                        x = getOffsetX(caveX, var31, offset);
                        z = getOffsetZ(caveZ, var31, offset);
                        direction2 += 4;
                        length2 = length / 3 + rand.nextInt(length / 3);
                        if ((direction2 & 0x1) == 1) length2 -= offset / 4;
                        generateMazeCaveSegment(x, caveY, z, direction2, length2, height);
                    }
                }
            }
            if (caveY == maxY) {
                length = 100;
            } else if (caveY == minY) {
                length = maxY - yInc * 2 + 1;
            } else {
                length = Math.min(maxY, caveY + yInc) + 1;
            }
            generateVerticalCave(rand.nextLong(), caveX, caveY, length, caveZ, 0.0F, caveX, caveZ, 8);
            do {
                quadrant = rand.nextInt(4);
            } while (quadrant == oldQuadrant);
            oldQuadrant = quadrant;
            height = 1.625F;
            caveCount++;
        }
    }

    private static void generateRegionalCaves(int chunkX, int chunkZ) {
        if (rand.nextInt(100) + 1 <= WGConfig.caveRegionalReductionPercentage) return;
        Random r = new Random();
        r.setSeed(worldObj.getSeed());
        if (isGiantCaveRegion(chunkX, chunkZ) || r.nextInt(100) < WGConfig.giantCaveChance) {
            int chunkOffX = isEdgeOfGiantCaveRegion(chunkX, chunkZ);
            if (chunkOffX > 0 || (chunkX & 0x1) == (chunkZ & 0x1)) {
                float var25;
                int chunkOffZ = chunkX;
                int div1;
                for (div1 = chunkZ; validRegionalCaveLocation(chunkOffZ - 1, div1, 4096); chunkOffZ--) ;
                while (validRegionalCaveLocation(chunkOffZ, div1 - 1, 4096)) div1--;
                int div2 = 0;
                float startY = 0.0F;
                int verticalCave = 0;
                int x;
                for (x = div1; x < div1 + 12; x++) {
                    for (int direction = chunkOffZ; direction < chunkOffZ + 12; direction++) {
                        if ((direction & 0x1) == (x & 0x1) || isEdgeOfGiantCaveRegion(direction, x) > 0) {
                            caveRNG.setSeed((direction * 341873128712L + x * 132897987541L) * seedMultiplier);
                            div2 += caveRNG.nextInt(65);
                            startY += caveRNG.nextFloat() * caveRNG.nextFloat();
                            verticalCave++;
                        }
                    }
                }
                div2 = Math.round(32.0F - div2 / verticalCave) + rand.nextInt(65) + 112;
                startY = 0.25F / startY / verticalCave * rand.nextFloat() * rand.nextFloat() * ((chunkOffX > 0) ? 12.0F : 11.3333F) + ((chunkOffX > 0) ? 3.0F : 3.66667F);
                x = rand.nextInt(div2 / 4) + div2 / 2;
                if (chunkOffX > 0) {
                    var25 = (chunkOffX - 1) * 0.7853982F;
                } else {
                    var25 = rand.nextFloat() * 6.2831855F;
                }
                chunkOffZ = chunkX * 16 + 8;
                div1 = chunkZ * 16 + 8;
                int y = rand.nextInt(11) + 25 * ((chunkOffX > 0) ? (chunkX + chunkZ & 0x1) : (chunkZ & 0x1)) + 10;
                generateLargeCave2(rand.nextLong(), chunkOffZ, y, div1, startY, var25, (rand.nextFloat() - 0.5F) / 4.0F, 0, div2, x, 0.1F, true, false);
                if (chunkOffX > 0 && (chunkOffX & 0x1) == 0) {
                    div2 = 64 + rand.nextInt(16);
                    generateHorizontalCave(rand.nextLong(), chunkOffZ, y, div1, rand.nextFloat() + 1.0F, var25 + 3.1415927F, (rand.nextFloat() - 0.5F) / 4.0F, div2, div2 + 32 + rand.nextInt(8), 1);
                }
                if (rand.nextInt((chunkOffX > 0) ? 12 : 6) == 0) generateVerticalCave(chunkOffZ, y, 100, div1);
            }
        } else {
            int chunkOffX = chunkX + caveOffsetX;
            int chunkOffZ = chunkZ + caveOffsetZ;
            byte var20 = 2;
            byte var21 = 3;
            if ((chunkOffX & 0x40) == (chunkOffZ & 0x40)) {
                var20 = 3;
                var21 = 2;
            }
            caveRNG.setSeed(((chunkOffX / var20) * 341873128712L + (chunkOffZ / var21) * 132897987541L) * seedMultiplier);
            if (chunkOffX % var20 == caveRNG.nextInt(var20) && chunkOffZ % var21 == caveRNG.nextInt(var21)) {
                int var22 = 10 + (chunkOffX / var21 + chunkOffZ / var20) % 3 * 20;
                boolean var23 = rand.nextBoolean();
                double var24 = (chunkX * 16 + rand.nextInt(16));
                double var26 = var22;
                double z = (chunkZ * 16 + rand.nextInt(16));
                float direction1 = rand.nextFloat() * 6.2831855F;
                int segments = rand.nextInt(3) + 2;
                float width = Math.max(1.0F, (!var23 && segments == 2) ? 1.0F : ((rand.nextInt(2) + 1) + WGConfig.widthAdditionRegional));
                width = rand.nextFloat() * rand.nextFloat() * width + 0.5F;
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, WGConfig.widthMaxRegional);
                    width = Math.max(width, WGConfig.widthMinRegional);
                }
                for (int i = 0; i < segments; i++) {
                    float segmentDirection = direction1;
                    direction1 += 6.2831855F / segments;
                    if (segments > 2) segmentDirection += (rand.nextFloat() - 0.5F) * 2.094395F / segments;
                    if (i > 0 && (var23 || segments > 2)) {
                        width = Math.max(0.6F, rand.nextFloat() * rand.nextFloat() * (rand.nextInt(2) + 1) + 0.5F + WGConfig.widthAdditionRegional);
                        if (WGConfig.hardLimitsEnabled) {
                            width = Math.min(width, WGConfig.widthMaxRegional);
                            width = Math.max(width, WGConfig.widthMinRegional);
                        }
                    }
                    generateHorizontalCave(rand.nextLong(), var24, var26, z, width, segmentDirection, (rand.nextFloat() - 0.5F) / 4.0F, 0, 112 + rand.nextInt(65), 2);
                }
                if (var23) {
                    generateVerticalCave(var24, var22, 100, z);
                    segments++;
                }
                if (segments > 2) generateCircularRoom(var24, var26, z, (rand.nextFloat() + 0.5F) * segments + 1.0F);
                if (!var23 && validCaveClusterLocation(chunkX, chunkZ, false, true) > 0)
                    generateCaveCluster(chunkX, chunkZ, 1);
            }
        }
    }

    private static void generateCave(long seed, double x, double y, double z, float width, float directionXZ, float directionY, int pos, int length, float curviness) {
        caveRNG.setSeed(seed);
        float var23 = 0.0F;
        float var24 = 0.0F;
        int branchPoint = -999;
        if (pos <= 0) {
            length = 112 - caveRNG.nextInt(28);
            if (width >= 1.0F) branchPoint = caveRNG.nextInt(length / 2) + length / 4;
        }
        for (boolean isVerticalCave = (caveRNG.nextInt(6) == 0); pos < length; pos++) {
            if (pos == branchPoint) {
                seed = caveRNG.nextLong();
                width = caveRNG.nextFloat() * 0.5F + 0.5F + WGConfig.widthAdditionBranchPoint;
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, WGConfig.widthMaxBranchPoint);
                    width = Math.max(width, WGConfig.widthMinBranchPoint);
                }
                directionY /= 3.0F;
                generateCave(caveRNG.nextLong(), x, y, z, caveRNG.nextFloat() * 0.5F + 0.5F, directionXZ - 1.5707964F, directionY, pos, length, curviness);
                generateCave(seed, x, y, z, width, directionXZ + 1.5707964F, directionY, pos, length, curviness);
                return;
            }
            double radiusW = (1.5F + sine(pos * 3.1415927F / length) * width);
            double var35 = x - chunkCenterX;
            double var37 = z - chunkCenterZ;
            double var39 = (length - pos + 18) + radiusW;
            if (var35 * var35 + var37 * var37 > var39 * var39) return;
            if (caveRNG.nextInt(4) == 0) radiusW = radiusW / 5.0D + 0.75D;
            float var33 = cosine(directionY);
            x += (cosine(directionXZ) * var33);
            y += sine(directionY);
            z += (sine(directionXZ) * var33);
            if (isVerticalCave) {
                directionY *= 0.92F;
            } else {
                directionY *= 0.7F;
            }
            directionY += var24 * 0.1F;
            directionXZ += var23 * curviness;
            var24 *= 0.9F;
            var23 *= 0.75F;
            var24 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 2.0F;
            var23 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 4.0F;
            double radiusW_2 = radiusW + 9.0D;
            if (x >= chunkCenterX - radiusW_2 && x <= chunkCenterX + radiusW_2 && z >= chunkCenterZ - radiusW_2 && z <= chunkCenterZ + radiusW_2) {
                double noiseMultiplier = 0.275D / Math.max(radiusW - 1.0D, 0.916666D);
                generateCaveSegment(x, y, z, radiusW, radiusW, noiseMultiplier, 1);
            }
        }
    }

    private static void generateCircularRoom(double x, double y, double z, float width) {
        long seed = rand.nextLong();
        width++;
        width = Math.max(1.0F, width + WGConfig.widthAdditionCircular);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, WGConfig.widthMaxCircular);
            width = Math.max(width, WGConfig.widthMinCircular);
        }
        double var35 = x - chunkCenterX;
        double var37 = z - chunkCenterZ;
        double var39 = (width + 18.0F);
        if (var35 * var35 + var37 * var37 <= var39 * var39) {
            caveRNG.setSeed(seed);
            x += (caveRNG.nextFloat() - 0.5F);
            y += (caveRNG.nextFloat() - 0.5F);
            z += (caveRNG.nextFloat() - 0.5F);
            double radiusW_2 = width + 9.0D;
            if (x >= chunkCenterX - radiusW_2 && x <= chunkCenterX + radiusW_2 && z >= chunkCenterZ - radiusW_2 && z <= chunkCenterZ + radiusW_2) {
                double noiseMultiplier = 0.33D / Math.max((width - 1.0F), 1.1D);
                generateCaveSegment(x, y, z, width, (width / 2.0F), noiseMultiplier, 1);
            }
        }
    }

    private static void generateSingleCave(int x, int y, int z, float curviness) {
        x += rand.nextInt(16);
        z += rand.nextInt(16);
        float width = rand.nextFloat() * rand.nextFloat() * rand.nextFloat() * 5.0F + 0.5F;
        generateCave(rand.nextLong(), x, y, z, width, rand.nextFloat() * 6.2831855F, (rand.nextFloat() - 0.5F) / 4.0F, 0, 0, curviness);
    }

    private static int generateLargeCave(int chunkX, int chunkZ, int type) {
        int length;
        float width;
        if (type == 0) {
            length = 224 + largeCaveRNG.nextInt(113);
            width = largeCaveRNG.nextFloat() * 8.0F + largeCaveRNG.nextFloat() * 6.0F + 10.0F + WGConfig.widthAdditionColossal;
            if (WGConfig.hardLimitsEnabled) {
                width = Math.min(width, WGConfig.widthMaxColossal);
                width = Math.max(width, WGConfig.widthMinColossal);
            }
            if (largeCaveRNG.nextBoolean()) width *= length / 224.0F;
        } else {
            length = Math.min(112 + largeCaveRNG.nextInt(largeCaveRNG.nextInt(336) + 1), 336);
            caveRNG.setSeed((((chunkX + caveOffsetX + 12) / 16) * 341873128712L + ((chunkZ + caveOffsetZ + 12) / 16) * 132897987541L) * seedMultiplier);
            width = Math.max(largeCaveRNG.nextFloat() * largeCaveRNG.nextFloat() * largeCaveRNG.nextFloat() * largeCaveRNG.nextFloat(), largeCaveRNG.nextFloat() * largeCaveRNG.nextFloat() * largeCaveRNG.nextFloat() + WGConfig.widthAdditionColossal);
            if (caveRNG.nextBoolean()) {
                width = width * 8.0F + 2.0F;
            } else {
                width = width * 2.66667F + 2.66667F;
            }
            if (largeCaveRNG.nextBoolean()) {
                float x = largeCaveRNG.nextFloat() * length / 96.0F + (672 - length) / 672.0F;
                if (x > 1.0F) width *= x;
            } else {
                float x = largeCaveRNG.nextFloat();
                width *= x * x * 3.0F + 1.0F;
            }
        }
        double x1 = (chunkX * 16 + 8);
        double y = (largeCaveRNG.nextInt(16) + 15);
        double z = (chunkZ * 16 + 8);
        if (y < 20.5D) y += ((width + 0.5F) / 4.0F);
        int branchPoint = largeCaveRNG.nextInt(length / 4) + length / 2;
        float direction = largeCaveRNG.nextFloat() * 6.2831855F;
        float curviness = length / 3360.0F + 0.05F;
        if (type == 0) {
            int i = length - branchPoint;
            if (WGConfig.hardLimitsEnabled) {
                width = Math.min(width, WGConfig.widthMaxColossal);
                width = Math.max(width, WGConfig.widthMinColossal);
            }
            generateLargeCave2(largeCaveRNG.nextLong(), x1, y, z, width, direction, (largeCaveRNG.nextFloat() - 0.5F) / 4.0F, i, length, 0, curviness, false, true);
            length += i;
            i = length / 2;
            branchPoint = i * 3 / 2 + largeCaveRNG.nextInt(i / 4);
            generateLargeCave2(largeCaveRNG.nextLong(), x1, y, z, (largeCaveRNG.nextFloat() * width + width * 2.0F) / 4.0F, direction + 1.5707964F, (largeCaveRNG.nextFloat() - 0.5F) / 4.0F, i, length, branchPoint, curviness, true, true);
            generateLargeCave2(largeCaveRNG.nextLong(), x1, y, z, (largeCaveRNG.nextFloat() * width + width * 2.0F) / 4.0F, direction - 1.5707964F, (largeCaveRNG.nextFloat() - 0.5F) / 4.0F, i, length, branchPoint, curviness, true, true);
        } else {
            generateLargeCave2(largeCaveRNG.nextLong(), x1, y, z, width, direction, (largeCaveRNG.nextFloat() - 0.5F) / 4.0F, 0, length, branchPoint, curviness, false, true);
        }
        int ret = (int) (y + 0.5D);
        if (type == 1) ret += (int) (direction * 1024.0F) * 256;
        return ret;
    }

    private static void generateLargeCave2(long seed, double x, double y, double z, float width, float directionXZ, float directionY, int pos, int length, int branchPoint, float curviness, boolean giantCaveBranch, boolean vertVar) {
        caveRNG.setSeed(seed);
        float var23 = 0.0F;
        float var24 = 0.0F;
        float minRadius = 1.75F + width / 53.3333F;
        for (boolean isVerticalCave = (vertVar && caveRNG.nextInt(6) == 0 && width < 20.0F); pos < length; pos++) {
            if (pos == branchPoint) {
                seed = caveRNG.nextLong();
                if (giantCaveBranch) width *= 1.5F;
                width = (caveRNG.nextFloat() * width + width) / 3.0F;
                directionY /= 3.0F;
                generateLargeCave2(caveRNG.nextLong(), x, y, z, (caveRNG.nextFloat() * width + width) / 3.0F, directionXZ - 1.5707964F, directionY, pos, length, 0, curviness, false, true);
                generateLargeCave2(seed, x, y, z, width, directionXZ + 1.5707964F, directionY, pos, length, 0, curviness, false, true);
                return;
            }
            double radiusW = (sine(pos * 3.1415927F / length) * width);
            double var35 = x - chunkCenterX;
            double var37 = z - chunkCenterZ;
            double var39 = (length - pos + 18) + radiusW;
            if (var35 * var35 + var37 * var37 > var39 * var39) return;
            double ratio = (1.0F - (float) radiusW / 100.0F);
            radiusW += minRadius;
            double radiusH = radiusW * ratio;
            if (caveRNG.nextInt(4) == 0) {
                radiusW = radiusW / 5.0D + 0.75D;
                radiusH = radiusH / 5.0D + 0.75D;
            }
            float var33 = cosine(directionY);
            x += (cosine(directionXZ) * var33);
            y += sine(directionY);
            z += (sine(directionXZ) * var33);
            if (isVerticalCave) {
                directionY *= 0.92F;
            } else {
                directionY *= 0.7F;
            }
            if (!vertVar) if (y > 45.0D) {
                var24 = -0.5F;
            } else if (y < 4.0D) {
                var24 = 0.5F;
            }
            directionY += var24 * 0.1F;
            directionXZ += var23 * curviness;
            var24 *= 0.9F;
            var23 *= 0.75F;
            var24 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 2.0F;
            var23 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 4.0F;
            double radiusW_2 = radiusW + 9.0D;
            if (x >= chunkCenterX - radiusW_2 && x <= chunkCenterX + radiusW_2 && z >= chunkCenterZ - radiusW_2 && z <= chunkCenterZ + radiusW_2) {
                double noiseMultiplier = 0.275D / Math.max(radiusW - 1.0D, 0.916666D) + 0.0033735D;
                generateCaveSegment(x, y, z, radiusW, radiusH, noiseMultiplier, 1);
            }
        }
    }

    private static void generateHorizontalCave(long seed, double x, double y, double z, float width, float directionXZ, float directionY, int pos, int length, int caveType) {
        float curviness;
        caveRNG.setSeed(seed);
        float var23 = 0.0F;
        float var24 = 0.0F;
        int branchPoint = -999;
        float startDir = directionXZ;
        double startY = y;
        byte branchFlag = -1;
        boolean flag = (caveType < 1 || caveType == 2);
        if (caveType < 2) {
            curviness = 0.1F;
        } else {
            curviness = (caveType != 3 && caveRNG.nextInt(4) != 0) ? 0.025F : 0.05F;
        }
        if (caveType == 1) if (pos == 0) {
            branchPoint = Math.min(length * 2 / 3 + caveRNG.nextInt(length / 11), 120);
        } else {
            branchPoint = pos;
            pos = 0;
            branchFlag = 0;
        }
        for (boolean isVerticalCave = (flag && caveRNG.nextInt(6) == 0); pos < length; pos++) {
            if (pos == branchPoint) {
                seed = caveRNG.nextLong();
                float var38 = Math.min(width * 0.75F, caveRNG.nextFloat() * width * 0.75F + width * 0.25F);
                width = Math.min(width * 0.75F, caveRNG.nextFloat() * width * 0.75F + width * 0.25F);
                directionY /= 3.0F;
                generateHorizontalCave(caveRNG.nextLong(), x, y, z, width, directionXZ - 1.5707964F, directionY, pos, length, branchFlag);
                generateHorizontalCave(seed, x, y, z, var38, directionXZ + 1.5707964F, directionY, pos, length, branchFlag);
                return;
            }
            double var35 = x - chunkCenterX;
            double var37 = z - chunkCenterZ;
            double var39 = (length - pos + 18) + width;
            if (var35 * var35 + var37 * var37 > var39 * var39) return;
            double radiusW = 1.25D;
            float var33 = cosine(directionY);
            x += (cosine(directionXZ) * var33);
            y += sine(directionY);
            z += (sine(directionXZ) * var33);
            if (isVerticalCave) {
                directionY *= 0.92F;
            } else {
                directionY *= 0.7F;
            }
            if (caveType < 2) {
                radiusW += (sine(pos * 3.1415927F / length) * width);
                if (caveType >= 0) {
                    float radiusW_2 = directionXZ - startDir;
                    if (radiusW_2 > 0.7853982F) {
                        var23 = -0.5F;
                    } else if (radiusW_2 < -0.7853982F) {
                        var23 = 0.5F;
                    }
                    radiusW_2 = (float) (y - startY);
                    if (radiusW_2 > 5.0F) {
                        var24 = -0.5F;
                    } else if (radiusW_2 < -5.0F) {
                        var24 = 0.5F;
                    }
                }
            } else {
                if (pos < length - 3) radiusW += 0.25D;
                if (pos < length - 6) radiusW += (width * caveRNG.nextFloat());
                if (caveType == 2) {
                    float radiusW_2 = (float) (y - startY);
                    if (radiusW_2 > 5.0F) {
                        var24 = -0.5F;
                    } else if (radiusW_2 < -5.0F) {
                        var24 = 0.5F;
                    }
                } else if (caveType == 3) {
                    float radiusW_2 = directionXZ - startDir;
                    if (radiusW_2 > 0.7853982F) {
                        var23 = -0.5F;
                    } else if (radiusW_2 < -0.7853982F) {
                        var23 = 0.5F;
                    }
                }
            }
            directionY += var24 * 0.1F;
            var24 *= 0.9F;
            var24 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 2.0F;
            if (caveRNG.nextInt(4) == 0) radiusW = 1.25D;
            directionXZ += var23 * curviness;
            var23 *= 0.75F;
            var23 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 4.0F;
            double var391 = radiusW + 9.0D;
            if (x >= chunkCenterX - var391 && x <= chunkCenterX + var391 && z >= chunkCenterZ - var391 && z <= chunkCenterZ + var391) {
                double noiseMultiplier = (radiusW < 1.916666D) ? 0.15D : (0.275D / (radiusW - 1.0D));
                generateCaveSegment(x, y, z, radiusW, radiusW, noiseMultiplier, 1);
            }
        }
    }

    private static void generateDirectionalCave(int x, int y, int z, int cx, int cz, int offset) {
        int var9;
        float direction = (rand.nextFloat() - 0.5F) * 0.7853982F;
        boolean length = false;
        cx = x - cx;
        cz = z - cz;
        if (cx > offset) {
            if (cz > offset) {
                direction -= 2.35619F;
            } else if (cz < -offset) {
                direction += 2.35619F;
            } else {
                direction += 3.1415927F;
            }
        } else if (cx < -offset) {
            if (cz > offset) {
                direction -= 0.7853982F;
            } else if (cz < -offset) {
                direction += 0.7853982F;
            }
        } else if (cz > offset) {
            direction--;
        } else if (cz < -offset) {
            direction++;
        } else {
            direction *= 8.0F;
            length = true;
        }
        if (!length) {
            var9 = rand.nextInt(16) + 8 + Math.round((float) Math.sqrt((cx * cx + cz * cz)));
        } else {
            var9 = rand.nextInt(8) + 24;
        }
        float width = Math.max(0.01F, rand.nextFloat() * 0.5F + WGConfig.widthAdditionDirectional / 10.0F);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, WGConfig.widthMaxDirectional);
            width = Math.max(width, WGConfig.widthMinDirectional);
        }
        generateHorizontalCave(rand.nextLong(), x, y, z, width, direction, (rand.nextFloat() - 0.5F) / 4.0F, 0, var9, 0);
    }

    private static void generateHorizontalLinkCave(int x, int y, int z, int cx, int cz, int type) {
        int length;
        byte caveType;
        float direction = (rand.nextFloat() - 0.5F) * 0.7853982F;
        cx = x - cx;
        cz = z - cz;
        if (cx == 0 && cz == 0) {
            direction *= 8.0F;
        } else if (cx >= 0) {
            if (cz >= 0) {
                direction += 0.7853982F;
            } else {
                direction -= 0.7853982F;
            }
        } else if (cz >= 0) {
            direction += 2.35619F;
        } else {
            direction -= 2.35619F;
        }
        float width = rand.nextFloat() * 0.75F + 0.25F;
        width += WGConfig.widthAdditionNormal;
        width = Math.max(0.275F, width);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, WGConfig.widthMaxNormal);
            width = Math.max(width, WGConfig.widthMinNormal);
        }
        if ((type & 0x1) == 0) {
            caveType = 1;
            length = ((type == 4) ? 144 : 128) + rand.nextInt(32);
        } else {
            caveType = 0;
            length = 80 + rand.nextInt(16);
        }
        generateHorizontalCave(rand.nextLong(), x, y, z, width, direction, (rand.nextFloat() - 0.5F) / 4.0F, 0, length, caveType);
        if (type >= 2) {
            length = 20 + rand.nextInt(6);
            generateHorizontalCave(rand.nextLong(), x, y, z, Math.min(width * 0.75F, rand.nextFloat() * width * 0.75F + width * 0.25F), direction - 2.0944F, (rand.nextFloat() - 0.5F) / 4.0F, 0, length, -1);
            generateHorizontalCave(rand.nextLong(), x, y, z, Math.min(width * 0.75F, rand.nextFloat() * width * 0.75F + width * 0.25F), direction + 2.0944F, (rand.nextFloat() - 0.5F) / 4.0F, 0, length, -1);
        }
    }

    private static void generateVerticalCave(double x, int y1, int y2, double z) {
        generateVerticalCave(rand.nextLong(), x, y1, y2, z, -1.0F, 0.0D, 0.0D, 0);
    }

    private static void generateVerticalCave(long seed, double x, int y1, int y2, double z, float width, double centerX, double centerZ, int maxDeviation) {
        caveRNG.setSeed(seed);
        float var23 = 0.0F;
        width += Math.max(0.0F, WGConfig.widthAdditionVertical);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, WGConfig.widthMaxVertical);
            width = Math.max(width, WGConfig.widthMinVertical);
        }
        float directionXZ = caveRNG.nextFloat() * 6.2831855F;
        boolean descending = false;
        maxDeviation *= maxDeviation;
        float horizVar = 1.0F;
        if (width != 0.0F) {
            horizVar = caveRNG.nextFloat();
            horizVar = 1.0F - horizVar * horizVar * 0.333333F;
        }
        if (y1 > y2) {
            int j = y1;
            y1 = y2;
            y2 = j;
            descending = true;
        }
        int length = y2 - y1;
        for (int i = 0; i < length; i++) {
            double radiusW, var35 = x - chunkCenterX;
            double var37 = z - chunkCenterZ;
            double var39 = (length - i + 18) + width;
            if (var35 * var35 + var37 * var37 > var39 * var39) return;
            if (width >= 0.0F) {
                radiusW = (1.5F + sine(i * 1.5707964F / length) * width);
            } else {
                radiusW = (1.5F + (caveRNG.nextFloat() + caveRNG.nextFloat()) * 0.5F);
            }
            x += (cosine(directionXZ) * horizVar);
            z += (sine(directionXZ) * horizVar);
            if (maxDeviation == 1) {
                float radiusW_2 = (float) (x - centerX);
                float devZ = (float) (z - centerZ);
                if (radiusW_2 > 1.0F) x--;
                if (radiusW_2 < -1.0F) x++;
                if (devZ > 1.0F) z--;
                if (devZ < -1.0F) z++;
            } else if (maxDeviation > 0) {
                float radiusW_2 = (float) (x - centerX);
                float devZ = (float) (z - centerZ);
                if (radiusW_2 * radiusW_2 + devZ * devZ > maxDeviation) if (devZ >= 0.0F) {
                    if (radiusW_2 >= 0.0F) {
                        directionXZ = (directionXZ * 3.0F - 2.35619F) / 4.0F;
                    } else {
                        directionXZ = (directionXZ * 3.0F - 0.7853982F) / 4.0F;
                    }
                } else if (radiusW_2 >= 0.0F) {
                    directionXZ = (directionXZ * 3.0F + 2.35619F) / 4.0F;
                } else {
                    directionXZ = (directionXZ * 3.0F + 0.7853982F) / 4.0F;
                }
            }
            directionXZ += var23 * 0.15F;
            var23 *= 0.75F;
            var23 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 4.0F;
            double var371 = radiusW + 9.0D;
            if (x >= chunkCenterX - var371 && x <= chunkCenterX + var371 && z >= chunkCenterZ - var371 && z <= chunkCenterZ + var371) {
                double y, noiseMultiplier = (radiusW < 1.916666D) ? 0.15D : (0.275D / (radiusW - 1.0D));
                double radiusH = 1.5D;
                if (descending) {
                    y = (y2 - i - 1);
                } else {
                    y = (y1 + i);
                }
                if (i == length - 1 && width >= 0.5F) radiusH = (width + 1.0F);
                generateCaveSegment(x, y, z, radiusW, radiusH, noiseMultiplier, 1);
            }
        }
    }

    private static void generateRavineCave(double x, double y, double z, float heightVariation) {
        int length = rand.nextInt(5) + 16;
        int segmentLength = length + rand.nextInt(4) * 2;
        float width = rand.nextFloat() * rand.nextFloat() + 1.0F + Math.max(-1.0F, WGConfig.widthAdditionRavineCave);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, WGConfig.widthMaxRavineCave);
            width = Math.max(width, WGConfig.widthMinRavineCave);
        }
        float height = rand.nextFloat() * heightVariation + 2.0F;
        float direction = rand.nextFloat() * 3.1415927F;
        float directionY = (rand.nextFloat() - 0.5F) / 4.0F;
        float slope = (rand.nextFloat() * 0.75F + 0.25F) * 0.25F * (rand.nextInt(2) * 2 - 1);
        generateRavineCaveSegment(rand.nextLong(), x, y, z, width, direction, directionY, slope, segmentLength, height);
        segmentLength = length + rand.nextInt(4) * 2;
        generateRavineCaveSegment(rand.nextLong(), x, y, z, width, direction + 3.1415927F, -directionY, -slope, segmentLength, height);
        if (rand.nextBoolean()) {
            length = rand.nextInt(5) + 16;
            segmentLength = length + rand.nextInt(4) * 2;
            width = rand.nextFloat() * rand.nextFloat() + 1.0F;
            height = rand.nextFloat() * heightVariation + 2.0F;
            direction += (rand.nextFloat() * 0.7853982F + 0.393699F) * (rand.nextInt(2) * 2 - 1);
            directionY = (rand.nextFloat() - 0.5F) / 4.0F;
            slope = (rand.nextFloat() * 0.75F + 0.25F) * 0.25F * (rand.nextInt(2) * 2 - 1) * (rand.nextInt(2) * 0.75F + 0.25F);
            generateRavineCaveSegment(rand.nextLong(), x, y, z, width, direction, directionY, slope, segmentLength, height);
            if (rand.nextBoolean()) {
                segmentLength = length + rand.nextInt(4) * 2;
                generateRavineCaveSegment(rand.nextLong(), x, y, z, width, direction + 3.1415927F, -directionY, -slope, segmentLength, height);
            }
        }
    }

    private static void generateRavineCaveSegment(long seed, double x, double y, double z, float width, float directionXZ, float directionY, float slope, int length, float heightRatio) {
        caveRNG.setSeed(seed);
        float var24 = 0.0F;
        float var25 = 0.0F;
        float startDir = directionXZ;
        int end = (width >= 1.666F) ? 3 : ((width >= 1.333F) ? 2 : 1);
        width /= 2.0F;
        for (int pos = 0; pos < length; pos++) {
            double var34 = x - chunkCenterX;
            double var36 = z - chunkCenterZ;
            double var38 = (length - pos + 18);
            if (var34 * var34 + var36 * var36 > var38 * var38) return;
            double radiusW = width;
            if (pos < length - end) radiusW += (caveRNG.nextFloat() * 0.5F);
            if (pos < length - end * 2) radiusW += width;
            double radiusH = radiusW * heightRatio;
            radiusW *= (caveRNG.nextFloat() * 0.25F + 0.75F);
            radiusH *= (caveRNG.nextFloat() * 0.25F + 0.75F);
            float var32 = cosine(directionY);
            x += (cosine(directionXZ) * var32);
            y += (sine(directionY) + slope);
            z += (sine(directionXZ) * var32);
            float dev = directionXZ - startDir;
            if (dev > 0.392699F) {
                var24 = -0.5F;
            } else if (dev < -0.392699F) {
                var24 = 0.5F;
            }
            directionY *= 0.7F;
            directionY += var25 * 0.1F;
            directionXZ += var24 * 0.1F;
            var25 *= 0.5F;
            var24 *= 0.75F;
            var25 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 2.0F;
            var24 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 4.0F;
            double radiusW_2 = radiusW + 9.0D;
            if (x >= chunkCenterX - radiusW_2 && x <= chunkCenterX + radiusW_2 && z >= chunkCenterZ - radiusW_2 && z <= chunkCenterZ + radiusW_2) {
                int var56 = MathHelper.floor_double(x - radiusW) - chunkX_16 - 1;
                int var35 = MathHelper.floor_double(x + radiusW) - chunkX_16 + 1;
                int var55 = (int) (y - radiusH) - 1;
                int var37 = (int) (y + radiusH) + 1;
                int var57 = MathHelper.floor_double(z - radiusW) - chunkZ_16 - 1;
                int var39 = MathHelper.floor_double(z + radiusW) - chunkZ_16 + 1;
                if (var56 < 0) var56 = 0;
                if (var35 > 16) var35 = 16;
                if (var55 < 0) var55 = 0;
                if (var37 > 200) var37 = 200;
                if (var57 < 0) var57 = 0;
                if (var39 > 16) var39 = 16;
                for (int var41 = var56; var41 < var35; var41++) {
                    double var59 = ((var41 + chunkX_16) + 0.5D - x) / radiusW;
                    var59 *= var59;
                    for (int var44 = var57; var44 < var39; var44++) {
                        double var45 = ((var44 + chunkZ_16) + 0.5D - z) / radiusW;
                        var45 = var45 * var45 + var59;
                        if (var45 < 1.0D) {
                            int var47 = var41 << 12 | var44 << 8 | var37;
                            int biome = (worldObj.getBiomeGenForCoordsBody(var41 + chunkX_16, var44 + chunkZ_16)).biomeID;
                            for (int var49 = var37 - 1; var49 >= var55; var49--) {
                                double var50 = (var49 + 0.5D - y) / radiusH;
                                if (var50 > -0.7D && var45 + var50 * var50 / 6.0D + (noiseGen.nextInt(3) - 1) * 0.3D < 1.0D)
                                    replaceBlock(var47, var41, var44, biome);
                                var47--;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void generateMazeCaveSegment(int x, int y, int z, int direction, int length, float height) {
        float width = Math.max(1.45F, ((direction & 0x1) == 1) ? 1.55F : (1.45F + WGConfig.widthAdditionMaze));
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, WGConfig.widthMaxMaze);
            width = Math.max(width, WGConfig.widthMinMaze);
        }
        for (int pos = 0; pos < length; pos++) {
            double var34 = x - chunkCenterX;
            double var36 = z - chunkCenterZ;
            double var38 = (length - pos + 18);
            if (var34 * var34 + var36 * var36 > var38 * var38) return;
            x = getOffsetX(x, direction, 1);
            z = getOffsetZ(z, direction, 1);
            double radiusW = width + 10.0D;
            if (x >= chunkCenterX - radiusW && x <= chunkCenterX + radiusW && z >= chunkCenterZ - radiusW && z <= chunkCenterZ + radiusW) {
                radiusW = (noiseGen.nextFloat() * 0.5F + width);
                double radiusH = (noiseGen.nextFloat() * 0.5F + height);
                double var10000 = (noiseGen.nextFloat() - 0.5F + x);
                var10000 = (noiseGen.nextFloat() - 0.5F + y);
                var10000 = (noiseGen.nextFloat() - 0.5F + z);
                generateCaveSegment(x, y, z, radiusW, radiusH, (width / 7.25F), 0);
            }
        }
    }

    private static void generateRavines(int chunkX, int chunkZ, boolean flag, int genCaves) {
        if (rand.nextInt(20) == 15) {
            if (genCaves == 3 && isGiantCaveRegion(chunkX, chunkZ)) return;
            byte bigRavine = -1;
            boolean notNearOrigin = true;
            if (notNearOrigin) {
                int x = chunkX + caveOffsetX + 4;
                int offsetZ = chunkZ + caveOffsetZ + 4;
                if ((x & 0x7) == 0 && (offsetZ & 0x7) == 0 && (x & 0x8) != (offsetZ & 0x8)) {
                    bigRavine = 2;
                } else if (rand.nextInt(25) < 19 && x % 3 == 0 && offsetZ % 3 == 0 && (x / 3 & 0x1) == (offsetZ / 3 & 0x1)) {
                    bigRavine = 1;
                }
            }
            if (bigRavine > 0 && genCaves == 5) bigRavine = 0;
            if (bigRavine >= 0 || (flag && (rand.nextInt(30) < 11 || (!notNearOrigin && rand.nextInt(20) == 0)))) {
                double var24 = (chunkX * 16 + 8);
                double y = (rand.nextInt(rand.nextInt(50) + 8) + 13);
                double z = (chunkZ * 16 + 8);
                float directionXZ = rand.nextFloat() * 3.1415927F;
                float directionY = (rand.nextFloat() - 0.5F) / 4.0F;
                float width = Math.max(rand.nextFloat() * 3.0F, rand.nextFloat() * 4.0F + rand.nextFloat() * 2.0F + WGConfig.widthAdditionRavineCave);
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, WGConfig.widthMaxRavineCave);
                    width = Math.max(width, WGConfig.widthMinRavineCave);
                }
                if (rand.nextInt(4) == 0) width += rand.nextFloat() * ((bigRavine != 1 && width < 2.0F) ? 0.0F : 2.0F);
                double heightRatio = 3.0D;
                int length = 112 - rand.nextInt(15) * 2;
                float curviness = 0.05F;
                if (rand.nextInt(3) == 0) if (rand.nextInt(3) == 0) {
                    curviness = 0.1F;
                } else {
                    curviness = 0.075F;
                }
                if (bigRavine <= 0) {
                    int data = biomeList[((worldObj.getBiomeGenForCoordsBody(chunkX_16 + 8, chunkZ_16 + 8)).biomeID > 255) ? 20 : (worldObj.getBiomeGenForCoordsBody(chunkX_16 + 8, chunkZ_16 + 8)).biomeID] >> 2 & 0x3;
                    if ((rand.nextBoolean() && data == 1) || data == 2) {
                        data = rand.nextInt(2) + 1;
                        if (y < 31.5D) y += (data * 8);
                        heightRatio += data;
                        if (width > (6 - data)) width /= 2.0F;
                    }
                } else {
                    length += rand.nextInt(64) * 2;
                    if (width < 2.0F) {
                        width++;
                        if (width < 2.0F) width++;
                    }
                    width *= rand.nextFloat() * rand.nextFloat() * 1.5F + 1.0F;
                    if (bigRavine == 2) {
                        length += 80 + rand.nextInt(40) * 2;
                        width += rand.nextFloat() * (length / 56) + 3.0F;
                    }
                    if (length > 336) length = 336;
                    if (width > 18.0F) width = 18.0F;
                    if (y < 23.5D) {
                        y += (width / 1.5F);
                    } else if (y > 52.5D) {
                        y -= (width * 1.5F);
                    } else if (y > 42.5D) {
                        y -= (width / 1.5F);
                    }
                    curviness = (curviness + length / 8960.0F + 0.0125F) / 1.5F;
                }
                float var25 = 1.0F;
                float ravineDataMultiplier = 1.1F - (width - 2.0F) * 0.07F;
                if (ravineDataMultiplier < 0.6F)
                    ravineDataMultiplier = 0.6F - (0.6F - ravineDataMultiplier) * 0.290322F;
                int skipCount = 999;
                for (int i = 0; i < 128; i++) {
                    skipCount++;
                    if (skipCount >= 2 && (skipCount >= 5 || rand.nextInt(3) == 0)) {
                        skipCount = 0;
                        var25 = (1.0F + rand.nextFloat() * rand.nextFloat() * ravineDataMultiplier) * (0.95F + rand.nextInt(2) * 0.1F);
                        var25 *= var25;
                    }
                    ravineData[i] = var25;
                }
                length /= 2;
                generateRavineHalf(rand.nextLong(), var24, y, z, width, directionXZ, directionY, heightRatio, length, curviness, (bigRavine > 0));
                generateRavineHalf(rand.nextLong(), var24, y, z, width, directionXZ + 3.1415927F, -directionY, heightRatio, length, curviness, (bigRavine > 0));
            }
        }
    }

    private static void generateRavineHalf(long seed, double x, double y, double z, float width, float directionXZ, float directionY, double heightRatio, int length, float curviness, boolean bigRavine) {
        caveRNG.setSeed(seed);
        float var24 = 0.0F;
        float var25 = 0.0F;
        float startDir = directionXZ;
        for (int pos = 0; pos < length; pos++) {
            double radiusW = (1.5F + cosine(pos * 1.5707964F / length) * width);
            double var34 = x - chunkCenterX;
            double var36 = z - chunkCenterZ;
            double var38 = (length - pos + 18) + radiusW;
            if (var34 * var34 + var36 * var36 > var38 * var38) return;
            double radiusH = radiusW * heightRatio;
            if (bigRavine) {
                if (width > 5.5F) {
                    radiusH *= ravineHeightLookup[(int) (radiusW * 10.025642D) - 15];
                } else {
                    radiusH *= ((ravineHeightLookup[(int) (radiusW * 10.025642D) - 15] + Math.max(1.875F - (float) radiusW / 4.0F, 1.0F) + 0.25F) / 2.25F);
                }
            } else if (width > 2.0F) {
                radiusH *= Math.max(1.875F - (float) radiusW / 4.0F, 1.0F);
            }
            radiusW *= (caveRNG.nextFloat() * 0.25F + 0.75F);
            radiusH *= (caveRNG.nextFloat() * 0.25F + 0.75F);
            if (caveRNG.nextInt(4) == 0) {
                radiusW = radiusW / 5.0D + 0.5D;
                radiusH = radiusH / 4.0D + 1.5D;
            }
            float var32 = cosine(directionY);
            x += (cosine(directionXZ) * var32);
            y += sine(directionY);
            z += (sine(directionXZ) * var32);
            float dev = directionXZ - startDir;
            if (dev > 0.7853982F) {
                var24 = -0.5F;
            } else if (dev < -0.7853982F) {
                var24 = 0.5F;
            }
            directionY *= 0.7F;
            directionY += var25 * 0.05F;
            directionXZ += var24 * curviness;
            var25 *= 0.8F;
            var24 *= 0.5F;
            var25 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 2.0F;
            var24 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 4.0F;
            double radiusW_2 = radiusW + 9.0D;
            if (x >= chunkCenterX - radiusW_2 && x <= chunkCenterX + radiusW_2 && z >= chunkCenterZ - radiusW_2 && z <= chunkCenterZ + radiusW_2) {
                int var56 = MathHelper.floor_double(x - radiusW) - chunkX_16 - 1;
                int var35 = MathHelper.floor_double(x + radiusW) - chunkX_16 + 1;
                int var55 = (int) (y - radiusH) - 1;
                int var37 = (int) (y + radiusH) + 1;
                int var57 = MathHelper.floor_double(z - radiusW) - chunkZ_16 - 1;
                int var39 = MathHelper.floor_double(z + radiusW) - chunkZ_16 + 1;
                if (var56 < 0) var56 = 0;
                if (var35 > 16) var35 = 16;
                if (var55 < 0) var55 = 0;
                if (var37 > 120) var37 = 120;
                if (var57 < 0) var57 = 0;
                if (var39 > 16) var39 = 16;
                double noiseMultiplier = 0.33333333D / Math.max(radiusW - 0.5D, 2.5D);
                for (int var41 = var56; var41 < var35; var41++) {
                    double var59 = ((var41 + chunkX_16) + 0.5D - x) / radiusW;
                    var59 *= var59;
                    for (int var44 = var57; var44 < var39; var44++) {
                        double var45 = ((var44 + chunkZ_16) + 0.5D - z) / radiusW;
                        var45 = var45 * var45 + var59;
                        if (var45 < 1.0D) {
                            int var47 = var41 << 12 | var44 << 8 | var37;
                            int biome = (worldObj.getBiomeGenForCoordsBody(var41 + chunkX_16, var44 + chunkZ_16)).biomeID;
                            for (int var49 = var37 - 1; var49 >= var55; var49--) {
                                double var50 = (var49 + 0.5D - y) / radiusH;
                                if (var45 * ravineData[var49] + var50 * var50 / 6.0D + (noiseGen.nextInt(3) - 1) * noiseMultiplier < 1.0D)
                                    replaceBlock(var47, var41, var44, biome);
                                var47--;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void generateCaveSegment(double x, double y, double z, double radiusW, double radiusH, double noiseMultiplier, int noiseOffset) {
        int var55 = MathHelper.floor_double(x - radiusW) - chunkX_16 - 1;
        int var36 = MathHelper.floor_double(x + radiusW) - chunkX_16 + 1;
        int var57 = (int) (y - radiusH) - 1;
        int var38 = (int) (y + radiusH) + 1;
        int var56 = MathHelper.floor_double(z - radiusW) - chunkZ_16 - 1;
        int var40 = MathHelper.floor_double(z + radiusW) - chunkZ_16 + 1;
        if (var55 < 0) var55 = 0;
        if (var36 > 16) var36 = 16;
        if (var57 < 0) var57 = 0;
        if (var38 > 200) var38 = 200;
        if (var56 < 0) var56 = 0;
        if (var40 > 16) var40 = 16;
        for (int var42 = var55; var42 < var36; var42++) {
            double var59 = ((var42 + chunkX_16) + 0.5D - x) / radiusW;
            var59 *= var59;
            for (int var45 = var56; var45 < var40; var45++) {
                double var46 = ((var45 + chunkZ_16) + 0.5D - z) / radiusW;
                var46 = var46 * var46 + var59;
                if (var46 < 1.0D) {
                    int var48 = var42 << 12 | var45 << 8 | var38;
                    int biome = (worldObj.getBiomeGenForCoordsBody(var42 + chunkX_16, var45 + chunkZ_16)).biomeID;
                    for (int var50 = var38 - 1; var50 >= var57; var50--) {
                        double var51 = (var50 + 0.5D - y) / radiusH;
                        if (var51 > -0.7D && var51 * var51 + var46 + (noiseGen.nextInt(3) - noiseOffset) * noiseMultiplier < 1.0D)
                            replaceBlock(var48, var42, var45, biome);
                        var48--;
                    }
                }
            }
        }
    }

    private static void replaceBlock(int index, int x, int z, int biome) {
        Block data = chunkData[index];
        if (data != null || data != Blocks.air) {
            int y = index & 0xFF;
            if (y >= 25 && y <= 62) {
                int minX = Math.max(x - 1, 0);
                int maxX = Math.min(x + 1, 15);
                int minZ = Math.max(z - 1, 0);
                int maxZ = Math.min(z + 1, 15);
                int x2;
                for (x2 = minX; x2 <= maxX; x2++) {
                    for (int i = minZ; i <= maxZ; i++) {
                        int xyz = x2 << 12 | i << 8 | y;
                        if (chunkData[xyz] == Blocks.water) return;
                        if (chunkData[xyz + 1] == Blocks.water) return;
                    }
                }
                for (x2 = minX; x2 <= maxX; x2++) {
                    int i = z - 2;
                    if (i >= 0 && chunkData[x2 << 12 | i << 8 | y] == Blocks.water) return;
                    i = z + 2;
                    if (i <= 15 && chunkData[x2 << 12 | i << 8 | y] == Blocks.water) return;
                }
                for (int z2 = minZ; z2 <= maxZ; z2++) {
                    x2 = x - 2;
                    if (x2 >= 0 && chunkData[x2 << 12 | z2 << 8 | y] == Blocks.water) return;
                    x2 = x + 2;
                    if (x2 <= 15 && chunkData[x2 << 12 | z2 << 8 | y] == Blocks.water) return;
                }
                if (chunkData[x << 12 | z << 8 | y + 2] == Blocks.water) return;
            }
            if (y >= 60 && y <= 64 && (biomeList[(biome > 255) ? 20 : biome] & 0x20) != 0) return;
            BiomeGenBase bm = null;
            try {
                bm = worldObj.getBiomeGenForCoordsBody(x + chunkX_16, z + chunkZ_16);
            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean flag1 = false;
            for (String str : WGConfig.secondYLevelList) {
                if (bm.biomeName.equalsIgnoreCase(str)) {
                    flag1 = true;
                    break;
                }
            }
            if (bm != null && y < oceanAvg && WGConfig.secondYLevel && flag1) {
                chunkData[index] = Blocks.water;
                if (CavesDecorator.shouldGenerateStone(worldObj, x + chunkX_16, z + chunkZ_16))
                    chunkData[index] = (newRand.nextFloat() > 0.5F) ? Blocks.stone : Blocks.cobblestone;
            } else if (y < WGConfig.floodLevel) {
                chunkData[index] = (WGConfig.floodMech == 1) ? Blocks.lava : Blocks.water;
            } else {
                if (y > 56 && (data == Blocks.grass || data == Blocks.mycelium) && chunkData[index - 1] == Blocks.dirt)
                    chunkData[index - 1] = data;
                chunkData[index] = null;
                if (y > 25) {
                    data = chunkData[index + 1];
                    if (data == Blocks.sand) {
                        if ((biome < 37 || biome > 39) && (biome < 165 || biome > 167)) {
                            chunkData[index + 1] = Blocks.sandstone;
                        } else {
                            chunkData[index + 1] = Blocks.stained_hardened_clay;
                        }
                    } else if (data == Blocks.gravel) {
                        chunkData[index + 1] = Blocks.stone;
                    }
                }
            }
        }
    }

    private static void initializeCaveData(int chunkX, int chunkZ) {
        boolean flag = (Math.abs(chunkX) < 82 && Math.abs(chunkZ) < 82);
        int distance = 6724;
        for (int z = -18; z <= 18; z++) {
            int zIndex = (z + 18) * 37 + 18;
            int cz = chunkZ + z;
            int z2 = z * z;
            for (int x = -18; x <= 18; x++) {
                int x2z2 = x * x + z2;
                if (x2z2 <= 329) {
                    int cx = chunkX + x;
                    if (flag) distance = cx * cx + cz * cz;
                    byte data = 0;
                    if (validColossalCaveLocation(cx, cz, distance)) {
                        data = -1;
                    } else if (validStrongholdLocation(cx, cz, distance)) {
                        data = 3;
                    } else if (x2z2 <= 287) {
                        if (validRegionalCaveLocation(cx, cz, distance)) {
                            data = 2;
                        } else if (x2z2 <= 262) {
                            int d = validSpecialCaveLocation(cx, cz, distance);
                            if (d > 0) data = (byte) d;
                        }
                    }
                    caveDataArray[zIndex + x] = data;
                }
            }
        }
    }

    private static int validCaveLocation(int cx, int cz) {
        byte flag = 1;
        for (int z = -6; z <= 6; z++) {
            int zIndex = (cz + z + 18) * 37 + cx + 18;
            int z2 = z * z;
            for (int x = -6; x <= 6; x++) {
                int x2z2 = x * x + z2;
                if (x2z2 <= 37) {
                    byte data = caveDataArray[zIndex + x];
                    if (data != 0) {
                        if (data == -1) {
                            if (x2z2 == 0) return -1;
                            return 0;
                        }
                        if (data == 1 && x2z2 <= 17) {
                            if (x2z2 == 0) return 2;
                            if (x2z2 <= 5) return 0;
                            flag = 5;
                        }
                        if (data == 4 && x2z2 <= 17) {
                            if (x2z2 == 0) return 6;
                            if (x2z2 <= 5) return 0;
                            flag = 5;
                        }
                        if (data == 5 && x2z2 <= 17) {
                            if (x2z2 == 0) return 7;
                            if (x2z2 <= 5) return 0;
                            flag = 5;
                        }
                        if (data == 2 && x2z2 <= 24) {
                            if (x2z2 == 0) return 3;
                            if (flag == 1) flag = 4;
                        }
                        if (data == 3) flag = 5;
                    }
                }
            }
        }
        return flag;
    }

    public static boolean validColossalCaveLocation(int chunkX, int chunkZ, int distance) {
        chunkX += caveOffsetX;
        chunkZ += caveOffsetZ;
        if ((chunkX & 0x40) == (chunkZ & 0x40)) return false;
        caveRNG.setSeed(((chunkX / 64) * 341873128712L + (chunkZ / 64) * 132897987541L) * colossalCaveSeedMultiplier);
        return ((chunkX & 0x3F) == caveRNG.nextInt(32) && (chunkZ & 0x3F) == caveRNG.nextInt(32));
    }

    public static int validSpecialCaveLocation(int chunkX, int chunkZ, int distance) {
        int offsetX = chunkX + caveOffsetX + 1;
        int offsetZ = chunkZ + caveOffsetZ + 1;
        if ((offsetX & 0x7) <= 2 && (offsetZ & 0x7) <= 2) {
            int d = validSpecialCaveLocation2(offsetX, offsetZ);
            if (d != 0) return d;
            offsetX -= 16;
            offsetZ -= 16;
            caveRNG.setSeed(((offsetX / 32) * 341873128712L + (offsetZ / 32) * 132897987541L) * seedMultiplier);
            if ((offsetX & 0x1F) == caveRNG.nextInt(4) * 8 + caveRNG.nextInt(3) && (offsetZ & 0x1F) == caveRNG.nextInt(4) * 8 + caveRNG.nextInt(3)) {
                boolean flag = (distance < 5041);
                for (int z = -7; z <= 7; z++) {
                    int cz = chunkZ + z;
                    for (int x = -7; x <= 7; x++) {
                        int x2z2 = x * x + z * z;
                        if (x2z2 <= 50) {
                            int cx = chunkX + x;
                            if (flag) distance = cx * cx + cz * cz;
                            if (validColossalCaveLocation(cx, cz, distance)) return 0;
                            if (x2z2 <= 37) {
                                if (validStrongholdLocation(cx, cz, distance)) return 0;
                                if (x2z2 <= 24) {
                                    if (validRegionalCaveLocation(cx, cz, distance)) return 0;
                                    if (x2z2 > 0 && x2z2 <= 17 && validSpecialCaveLocation2(cx + caveOffsetX + 1, cz + caveOffsetZ + 1) != 0)
                                        return 0;
                                }
                            }
                        }
                    }
                }
                return 1;
            }
        }
        return 0;
    }

    private static int validSpecialCaveLocation2(int offsetX, int offsetZ) {
        int x, z;
        caveRNG.setSeed(((offsetX / 64) * 341873128712L + (offsetZ / 64) * 132897987541L) * regionalCaveSeedMultiplier);
        if (caveRNG.nextBoolean()) {
            x = caveRNG.nextInt(4) * 8 + caveRNG.nextInt(3);
            z = caveRNG.nextInt(3) * 8 + caveRNG.nextInt(3) + 40;
        } else {
            x = caveRNG.nextInt(3) * 8 + caveRNG.nextInt(3) + 40;
            z = caveRNG.nextInt(4) * 8 + caveRNG.nextInt(3);
        }
        return ((offsetX & 0x3F) == x && (offsetZ & 0x3F) == z) ? 4 : (((offsetX & 0x3F) == caveRNG.nextInt(3) * 8 + caveRNG.nextInt(3) + 40 && (offsetZ & 0x3F) == caveRNG.nextInt(3) * 8 + caveRNG.nextInt(3) + 40) ? 5 : 0);
    }

    public static boolean validRegionalCaveLocation(int chunkX, int chunkZ, int distance) {
        int offsetX, offsetZ;
        chunkX += caveOffsetX;
        chunkZ += caveOffsetZ;
        caveRNG.setSeed(((chunkX / 64) * 341873128712L + (chunkZ / 64) * 132897987541L) * regionalCaveSeedMultiplier);
        chunkX &= 0x3F;
        chunkZ &= 0x3F;
        if (caveRNG.nextBoolean()) {
            offsetX = caveRNG.nextInt(9) + 38;
            offsetZ = caveRNG.nextInt(21);
        } else {
            offsetX = caveRNG.nextInt(21);
            offsetZ = caveRNG.nextInt(9) + 38;
        }
        return (chunkX >= offsetX && chunkX <= offsetX + 11 && chunkZ >= offsetZ && chunkZ <= offsetZ + 11);
    }

    public static boolean isGiantCaveRegion(int chunkX, int chunkZ) {
        chunkX = (chunkX + caveOffsetX) / 64;
        chunkZ = (chunkZ + caveOffsetZ) / 64;
        caveRNG.setSeed(((chunkX / 2) * 341873128712L + (chunkZ / 2) * 132897987541L) * regionalCaveSeedMultiplier);
        return ((chunkX & 0x1) == caveRNG.nextInt(2) && (chunkZ & 0x1) == caveRNG.nextInt(2));
    }

    private static int isEdgeOfGiantCaveRegion(int chunkX, int chunkZ) {
        int offsetX, offsetZ;
        chunkX += caveOffsetX;
        chunkZ += caveOffsetZ;
        caveRNG.setSeed(((chunkX / 64) * 341873128712L + (chunkZ / 64) * 132897987541L) * regionalCaveSeedMultiplier);
        chunkX &= 0x3F;
        chunkZ &= 0x3F;
        if (caveRNG.nextBoolean()) {
            offsetX = caveRNG.nextInt(9) + 38;
            offsetZ = caveRNG.nextInt(21);
        } else {
            offsetX = caveRNG.nextInt(21);
            offsetZ = caveRNG.nextInt(9) + 38;
        }
        return (chunkX == offsetX) ? ((chunkZ == offsetZ) ? 6 : ((chunkZ == offsetZ + 11) ? 4 : 5)) : ((chunkX == offsetX + 11) ? ((chunkZ == offsetZ) ? 8 : ((chunkZ == offsetZ + 11) ? 2 : 1)) : ((chunkZ == offsetZ) ? 7 : ((chunkZ == offsetZ + 11) ? 3 : 0)));
    }

    private static int getQuadrantX(int i) {
        return ((i + 1 & 0x3) < 2) ? -1 : 1;
    }

    private static int getQuadrantZ(int i) {
        return ((i & 0x3) < 2) ? -1 : 1;
    }

    private static int getOffsetX(int x, int direction, int offset) {
        switch (direction & 0x7) {
            case 0:
            case 1:
            case 7:
                return x + offset;
            default:
                return x;
            case 3:
            case 4:
            case 5:
                break;
        }
        return x - offset;
    }

    private static int getOffsetZ(int z, int direction, int offset) {
        switch (direction & 0x7) {
            case 1:
            case 2:
            case 3:
                return z + offset;
            default:
                return z;
            case 5:
            case 6:
            case 7:
                break;
        }
        return z - offset;
    }

    private static float sine(float f) {
        return SINE_TABLE[(int) (f * 162.97466F) & 0x3FF];
    }

    private static float cosine(float f) {
        return SINE_TABLE[(int) (f * 162.97466F) + 256 & 0x3FF];
    }

    private static boolean validStrongholdLocation(int chunkX, int chunkZ, int distance) {
        chunkX += caveOffsetX;
        chunkZ += caveOffsetZ;
        if ((chunkX & 0x40) != (chunkZ & 0x40)) return false;
        rand.setSeed(((chunkX / 64) * 341873128712L + (chunkZ / 64) * 132897987541L) * seedMultiplier);
        return ((chunkX & 0x3F) == rand.nextInt(32) && (chunkZ & 0x3F) == rand.nextInt(32) && distance >= 1600);
    }

    static {
        int i;
        for (i = 0; i < 1024; i++)
            SINE_TABLE[i] = (float) Math.sin(i * Math.PI * 2.0D / 1024.0D);
        biomeList[3] = 1;
        biomeList[20] = 1;
        biomeList[34] = 1;
        biomeList[131] = 1;
        biomeList[162] = 1;
        biomeList[163] = 1;
        biomeList[164] = 1;
        biomeList[36] = 2;
        biomeList[37] = 2;
        biomeList[38] = 2;
        biomeList[39] = 2;
        biomeList[165] = 2;
        biomeList[166] = 2;
        biomeList[167] = 2;
        biomeList[37] = (byte) (biomeList[37] + 4);
        biomeList[38] = (byte) (biomeList[38] + 4);
        biomeList[39] = (byte) (biomeList[39] + 4);
        biomeList[165] = (byte) (biomeList[165] + 4);
        biomeList[166] = (byte) (biomeList[166] + 4);
        biomeList[167] = (byte) (biomeList[167] + 4);
        biomeList[36] = (byte) (biomeList[36] + 4);
        biomeList[163] = (byte) (biomeList[163] + 8);
        biomeList[164] = (byte) (biomeList[164] + 8);
        biomeList[3] = (byte) (biomeList[3] + 16);
        biomeList[20] = (byte) (biomeList[20] + 16);
        biomeList[34] = (byte) (biomeList[34] + 16);
        biomeList[131] = (byte) (biomeList[131] + 16);
        biomeList[162] = (byte) (biomeList[162] + 16);
        biomeList[0] = (byte) (biomeList[0] + 32);
        biomeList[7] = (byte) (biomeList[7] + 32);
        biomeList[10] = (byte) (biomeList[10] + 32);
        biomeList[11] = (byte) (biomeList[11] + 32);
        biomeList[16] = (byte) (biomeList[16] + 32);
        biomeList[26] = (byte) (biomeList[26] + 32);
        for (i = 15; i <= 195; i++) {
            float heightMultiplier, radiusW = i / 10.0F;
            if (i < 35) {
                heightMultiplier = 2.42858F - radiusW / 3.5F;
            } else if (i < 70) {
                heightMultiplier = 1.85716F - radiusW / 8.1665F;
            } else if (i < 110) {
                heightMultiplier = 1.2F - radiusW / 25.0F;
            } else {
                heightMultiplier = 0.96706F - radiusW / 53.125F;
            }
            ravineHeightLookup[i - 15] = heightMultiplier;
        }
    }
}
