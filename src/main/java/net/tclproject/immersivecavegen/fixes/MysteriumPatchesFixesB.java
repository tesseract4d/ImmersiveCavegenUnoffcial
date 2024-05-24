package net.tclproject.immersivecavegen.fixes;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;

public class MysteriumPatchesFixesB {
  private static final Random caveRNG = new Random();

  private static final Random largeCaveRNG = new Random();

  private static final Random spawnRNG = new Random();

  private static long seedMultiplier;

  private static long regionalCaveSeedMultiplier;

  private static long colossalCaveSeedMultiplier;

  private static int caveOffsetX;

  private static int caveOffsetZ;

  private static int mineshaftOffsetX;

  private static Random rand;

  private static World worldObj;

  private static int mineshaftOffsetZ;

  private static double field_82673_e = 0.004D;

  private static final int caveLimit = 18;

  private static boolean isInitialized;

  private static void initialize() {
    isInitialized = true;
    rand.setSeed(worldObj.getSeed());
    seedMultiplier = rand.nextLong() / 2L * 2L + 1L;
    caveOffsetX = rand.nextInt(128) + 2000000;
    caveOffsetZ = rand.nextInt(128) + 2000000;
    mineshaftOffsetX = rand.nextInt(7) + 2000000;
    mineshaftOffsetZ = rand.nextInt(7) + 2000000;
    byte range = 66;
    int i;
    label36: for (i = 0; i < 100; i += 2) {
      colossalCaveSeedMultiplier = seedMultiplier + i;
      for (int z = -range; z <= range; z++) {
        for (int x = -range; x <= range; x++) {
          if (validColossalCaveLocation(x, z, x * x + z * z))
            break label36;
        }
      }
    }
    for (i = 0; i < 100; i += 2) {
      regionalCaveSeedMultiplier = seedMultiplier + i;
      for (int z = -range; z <= range; z += 12) {
        for (int x = -range; x <= range; x += 12) {
          if (validRegionalCaveLocation(x, z, x * x + z * z) && isGiantCaveRegion(x, z))
            return;
        }
      }
    }
  }

  @Fix(returnSetting = EnumReturnSetting.ALWAYS)
  public static boolean canSpawnStructureAtCoords(MapGenMineshaft m, int chunkX, int chunkZ) {
    rand = ((MapGenBase)m).rand;
    worldObj = ((MapGenBase)m).worldObj;
    if (!isInitialized)
      initialize();
    if (chunkX == 0 && chunkZ == 0)
      return false;
    int chunkOffX = chunkX + mineshaftOffsetX;
    int chunkOffZ = chunkZ + mineshaftOffsetZ;
    if ((chunkOffX / 7 & 0x1) == (chunkOffZ / 7 & 0x1))
      return false;
    int chunkModX = chunkOffX % 7;
    int chunkModZ = chunkOffZ % 7;
    if (chunkModX <= 2 && chunkModZ <= 2) {
      spawnRNG.setSeed(((chunkOffX / 7) * 341873128712L + (chunkOffZ / 7) * 132897987541L) * seedMultiplier);
      if (chunkModX == spawnRNG.nextInt(3) && chunkModZ == spawnRNG.nextInt(3)) {
        rand.setSeed((chunkX * 341873128712L + chunkZ * 132897987541L) * seedMultiplier);
        int distance = (Math.abs(chunkX) < 64 && Math.abs(chunkZ) < 64) ? (chunkX * chunkX + chunkZ * chunkZ) : 4096;
        return caveCheck(m, chunkX, chunkZ, distance);
      }
    }
    return false;
  }

  private static boolean caveCheck(MapGenMineshaft m, int chunkX, int chunkZ, int distance) {
    byte radius = 6;
    boolean colossalCaveR2 = true;
    byte caveR2 = 17;
    byte centerR2 = 4;
    int caveCount1 = 0;
    int caveCount2 = 0;
    int caveCount3 = 0;
    int caveCount4 = 0;
    int caveCount5 = 0;
    float totalWidth = 0.0F;
    boolean flag = (distance < 4096);
    for (int z = -radius; z <= radius; z++) {
      int cz = chunkZ + z;
      for (int x = -radius; x <= radius; x++) {
        int x2z2 = x * x + z * z;
          if (x2z2 <= 37) {
          int cx = chunkX + x;
          if (flag)
            distance = cx * cx + cz * cz;
          if (validColossalCaveLocation(cx, cz, distance))
            return false;
          int chunkOffX = cx + caveOffsetX + 4;
          int chunkOffZ = cz + caveOffsetZ + 4;
          if (distance >= 1024) {
            spawnRNG.setSeed((cx * 341873128712L + cz * 132897987541L) * seedMultiplier);
            if (spawnRNG.nextInt(20) == 15) {
              byte caveSize = 0;
              if ((chunkOffX & 0x7) == 0 && (chunkOffZ & 0x7) == 0 && (chunkOffX & 0x8) != (chunkOffZ & 0x8)) {
                caveSize = 2;
              } else if (spawnRNG.nextInt(25) < 19 && chunkOffX % 3 == 0 && chunkOffZ % 3 == 0 && (chunkOffX / 3 & 0x1) == (chunkOffZ / 3 & 0x1)) {
                caveSize = 1;
              }
              if (caveSize > 0) {
                spawnRNG.nextInt(spawnRNG.nextInt(50) + 8);
                spawnRNG.nextFloat();
                spawnRNG.nextFloat();
                float applyCaveVariation = spawnRNG.nextFloat() * 4.0F + spawnRNG.nextFloat() * 2.0F;
                if (spawnRNG.nextInt(4) == 0)
                  applyCaveVariation += spawnRNG.nextFloat() * ((caveSize != 1 && applyCaveVariation < 2.0F) ? 0.0F : 2.0F);
                int largeCaveChance = 112 - spawnRNG.nextInt(15) * 2;
                if (spawnRNG.nextInt(3) == 0)
                  spawnRNG.nextInt(3);
                largeCaveChance += spawnRNG.nextInt(64) * 2;
                if (applyCaveVariation < 2.0F) {
                  applyCaveVariation++;
                  if (applyCaveVariation < 2.0F)
                    applyCaveVariation++;
                }
                applyCaveVariation *= spawnRNG.nextFloat() * spawnRNG.nextFloat() * 1.5F + 1.0F;
                if (caveSize == 2) {
                  largeCaveChance += 80 + spawnRNG.nextInt(40) * 2;
                  applyCaveVariation += spawnRNG.nextFloat() * (largeCaveChance / 56) + 3.0F;
                }
                if (largeCaveChance > 160 || x2z2 <= caveR2) {
                  totalWidth += applyCaveVariation;
                  if (applyCaveVariation > 10.0F || totalWidth > 30.0F)
                    return false;
                }
              }
            }
            if ((chunkOffX & 0x7) == 0 && (chunkOffZ & 0x7) == 0 && (chunkOffX & 0x8) == (chunkOffZ & 0x8)) {
              spawnRNG.setSeed((cx * 341873128712L + cz * 132897987541L) * seedMultiplier);
              if (spawnRNG.nextInt(15) == 0 && spawnRNG.nextInt(spawnRNG.nextInt(spawnRNG.nextInt(40) + 1) + 1) > 0) {
                largeCaveRNG.setSeed(spawnRNG.nextLong());
                float var38 = getLargeCaveWidth(cx, cz, 0);
                totalWidth += var38;
                if (var38 > 18.0F || totalWidth > 30.0F)
                  return false;
              }
            }
          }
          if (x2z2 <= caveR2) {
            if (validStrongholdLocation(cx, cz, distance))
              return false;
            if (validSpecialCaveLocation(cx, cz, distance) > 0)
              return false;
            if (isGiantCaveRegion(cx, cz) && validRegionalCaveLocation(cx, cz, distance))
              return false;
            spawnRNG.setSeed((cx * 341873128712L + cz * 132897987541L) * seedMultiplier);
            if (spawnRNG.nextInt(15) == 0) {
              int var39 = spawnRNG.nextInt(spawnRNG.nextInt(spawnRNG.nextInt(40) + 1) + 1);
              if (var39 > 0) {
                boolean var40 = false;
                int largeCaveChance = -1;
                long regionSeed = 0L;
                if (distance >= 1024) {
                  caveRNG.setSeed(((chunkOffX / 16) * 341873128712L + (chunkOffZ / 16) * 132897987541L) * seedMultiplier);
                  largeCaveRNG.setSeed(spawnRNG.nextLong());
                  if (caveRNG.nextInt(4) != 0) {
                    largeCaveChance = (1 << caveRNG.nextInt(3)) - 1;
                    var40 = true;
                    regionSeed = caveRNG.nextLong();
                  }
                  if ((chunkOffX & 0x7) == 0 && (chunkOffZ & 0x7) == 0 && (chunkOffX & 0x8) == (chunkOffZ & 0x8))
                    return false;
                  if (var39 <= 3 && largeCaveRNG.nextInt(4) <= largeCaveChance && validLargeCaveLocation(cx, cz, var39) > 0) {
                    int largerCircularRooms = 1;
                    if (largeCaveRNG.nextInt(10) == 0)
                      largerCircularRooms += 1 + largeCaveRNG.nextInt(3);
                    for (int j = 0; j < largerCircularRooms; j++) {
                      float f = getLargeCaveWidth(cx, cz, 1);
                      totalWidth += f;
                      if (f > 10.0F || totalWidth > 30.0F)
                        return false;
                    }
                  }
                }
                boolean var41 = false;
                int circularRoomChance = 4;
                largeCaveChance = 10;
                float widthMultiplier = 1.0F;
                byte multiplier = 1;
                if (var40) {
                  caveRNG.setSeed(regionSeed);
                  if (var39 < 20) {
                    var41 = caveRNG.nextBoolean();
                    circularRoomChance = 2 << caveRNG.nextInt(2) + caveRNG.nextInt(2);
                    caveRNG.nextBoolean();
                    largeCaveChance = 5 << caveRNG.nextInt(2) + caveRNG.nextInt(2);
                  }
                  if (caveRNG.nextBoolean()) {
                    widthMultiplier += caveRNG.nextFloat();
                    if (caveRNG.nextBoolean())
                      widthMultiplier /= 2.0F;
                  }
                  if (caveRNG.nextBoolean()) {
                    caveRNG.nextBoolean();
                    if (spawnRNG.nextBoolean())
                      spawnRNG.nextFloat();
                  }
                  if (var39 >= 20) {
                    float caves = (var39 / 10);
                    widthMultiplier = (widthMultiplier + caves - 1.0F) / caves;
                  }
                  if (var39 >= 10)
                    if (widthMultiplier > 1.5F) {
                      var39 = var39 / 2 + 1;
                      multiplier = 3;
                    } else if (widthMultiplier > 1.25F) {
                      var39 = var39 * 3 / 4 + 1;
                      multiplier = 2;
                    }
                }
                int var42 = 0;
                for (int i = 0; i < var39; i++) {
                  spawnRNG.nextInt(16);
                  int y = spawnRNG.nextInt(spawnRNG.nextInt(120) + 8);
                  spawnRNG.nextInt(16);
                  int caves2 = 1;
                  if (spawnRNG.nextInt(circularRoomChance) == 0) {
                    int k = spawnRNG.nextInt(4);
                    caves2 += k;
                    float width = spawnRNG.nextFloat() * 6.0F + 1.0F;
                    if (var41 && spawnRNG.nextInt(16 / circularRoomChance) == 0) {
                      width = width * (spawnRNG.nextFloat() * spawnRNG.nextFloat() + 1.0F) + 3.0F;
                      if (width > 8.5F)
                        caves2 += 2;
                    }
                    if (widthMultiplier >= 1.0F) {
                      width *= widthMultiplier;
                      if (k == 0 && width > 10.0F && width < 17.0F) {
                        float f = (width - 10.0F) / 7.0F;
                        width *= spawnRNG.nextFloat() * (1.0F - f) + 1.0F + f;
                      }
                    }
                    if (width > 15.5F) {
                      totalWidth += width / 2.0F;
                      if (width > 23.5F || totalWidth > 30.0F)
                        return false;
                    }
                    spawnRNG.nextLong();
                  }
                  spawnRNG.nextFloat();
                  if (y > 10 && y < 60)
                    var42 += caves2 * multiplier;
                  for (int j = 0; j < caves2; j++) {
                    spawnRNG.nextFloat();
                    spawnRNG.nextFloat();
                    if (spawnRNG.nextInt(largeCaveChance) == 0) {
                      spawnRNG.nextFloat();
                      spawnRNG.nextFloat();
                    }
                    if (j > 0)
                      spawnRNG.nextFloat();
                    spawnRNG.nextLong();
                    spawnRNG.nextFloat();
                  }
                }
                if (x < 0) {
                  caveCount1 += var42;
                  m.getClass();
                  if (caveCount1 > 18)
                    return false;
                } else if (x > 0) {
                  caveCount2 += var42;
                  m.getClass();
                  if (caveCount2 > 18)
                    return false;
                }
                if (z < 0) {
                  caveCount3 += var42;
                  m.getClass();
                  if (caveCount3 > 18)
                    return false;
                } else if (z > 0) {
                  caveCount4 += var42;
                  m.getClass();
                  if (caveCount4 > 18)
                    return false;
                }
                if (x2z2 <= centerR2) {
                  caveCount5 += var42;
                  m.getClass();
                  if (caveCount5 > 12)
                    return false;
                }
              }
            }
          }
        }
      }
    }
    return true;
  }

  private static float getLargeCaveWidth(int chunkX, int chunkZ, int type) {
    if (type == 0) {
      int i = 224 + largeCaveRNG.nextInt(113);
      float f = largeCaveRNG.nextFloat() * 8.0F + largeCaveRNG.nextFloat() * 6.0F + 10.0F;
      if (largeCaveRNG.nextBoolean())
        f *= i / 224.0F;
      return f * i / 336.0F;
    }
    int length = Math.min(112 + largeCaveRNG.nextInt(largeCaveRNG.nextInt(336) + 1), 336);
    caveRNG.setSeed((((chunkX + caveOffsetX + 12) / 16) * 341873128712L + ((chunkZ + caveOffsetZ + 12) / 16) * 132897987541L) * seedMultiplier);
    float width = largeCaveRNG.nextFloat() * largeCaveRNG.nextFloat() * largeCaveRNG.nextFloat();
    if (caveRNG.nextBoolean()) {
      width = width * 8.0F + 2.0F;
    } else {
      width = width * 2.66667F + 2.66667F;
    }
    if (largeCaveRNG.nextBoolean()) {
      float multiplier = largeCaveRNG.nextFloat() * length / 96.0F + (672 - length) / 672.0F;
      if (multiplier > 1.0F)
        width *= multiplier;
    } else {
      float multiplier = largeCaveRNG.nextFloat();
      width *= multiplier * multiplier * 3.0F + 1.0F;
    }
    largeCaveRNG.nextInt(16);
    largeCaveRNG.nextInt(length / 4);
    largeCaveRNG.nextFloat();
    largeCaveRNG.nextLong();
    largeCaveRNG.nextFloat();
    return width;
  }

  private static boolean validStrongholdLocation(int chunkX, int chunkZ, int distance) {
    chunkX += caveOffsetX;
    chunkZ += caveOffsetZ;
    if ((chunkX & 0x40) != (chunkZ & 0x40))
      return false;
    caveRNG.setSeed(((chunkX / 64) * 341873128712L + (chunkZ / 64) * 132897987541L) * seedMultiplier);
    return ((chunkX & 0x3F) == caveRNG.nextInt(32) && (chunkZ & 0x3F) == caveRNG.nextInt(32) && distance >= 1600);
  }

  private static boolean validColossalCaveLocation(int chunkX, int chunkZ, int distance) {
    chunkX += caveOffsetX;
    chunkZ += caveOffsetZ;
    if ((chunkX & 0x40) == (chunkZ & 0x40))
      return false;
    caveRNG.setSeed(((chunkX / 64) * 341873128712L + (chunkZ / 64) * 132897987541L) * colossalCaveSeedMultiplier);
    return ((chunkX & 0x3F) == caveRNG.nextInt(32) && (chunkZ & 0x3F) == caveRNG.nextInt(32) && distance >= 1600);
  }

  private static int validSpecialCaveLocation(int chunkX, int chunkZ, int distance) {
    if (distance >= 1024) {
      int offsetX = chunkX + caveOffsetX + 1;
      int offsetZ = chunkZ + caveOffsetZ + 1;
      if ((offsetX & 0x7) <= 2 && (offsetZ & 0x7) <= 2) {
        int d = validSpecialCaveLocation2(offsetX, offsetZ);
        if (d != 0)
          return d;
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
                if (flag)
                  distance = cx * cx + cz * cz;
                if (validColossalCaveLocation(cx, cz, distance))
                  return 0;
                if (x2z2 <= 37) {
                  if (validStrongholdLocation(cx, cz, distance))
                    return 0;
                  if (x2z2 <= 24) {
                    if (validRegionalCaveLocation(cx, cz, distance))
                      return 0;
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

  private static boolean validRegionalCaveLocation(int chunkX, int chunkZ, int distance) {
    int offsetX, offsetZ, x1 = chunkX;
    int z1 = chunkZ;
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
    if (chunkX >= offsetX && chunkX <= offsetX + 11 && chunkZ >= offsetZ && chunkZ <= offsetZ + 11) {
      if (distance < 4096) {
        x1 -= chunkX - offsetX;
        z1 -= chunkZ - offsetZ;
        int x2 = x1 + 11;
        int z2 = z1 + 11;
        x1 *= x1;
        z1 *= z1;
        x2 *= x2;
        z2 *= z2;
        if (x1 + z1 < 1024 || x2 + z1 < 1024 || x1 + z2 < 1024 || x2 + z2 < 1024)
          return false;
      }
      return true;
    }
    return false;
  }

  private static boolean isGiantCaveRegion(int chunkX, int chunkZ) {
    chunkX = (chunkX + caveOffsetX) / 64;
    chunkZ = (chunkZ + caveOffsetZ) / 64;
    caveRNG.setSeed(((chunkX / 2) * 341873128712L + (chunkZ / 2) * 132897987541L) * regionalCaveSeedMultiplier);
    return ((chunkX & 0x1) == caveRNG.nextInt(2) && (chunkZ & 0x1) == caveRNG.nextInt(2));
  }

  private static int validLargeCaveLocation(int chunkX, int chunkZ, int caves) {
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
              if (caves > 12)
                return 0;
            }
            caves2 += c;
            if (caves2 > 6)
              flag = (caves2 > 12) ? 1 : 2;
          }
        }
      }
    }
    return flag;
  }
}
