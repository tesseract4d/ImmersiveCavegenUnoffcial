package net.tclproject.immersivecavegen.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockStoneStalactite extends BlockBaseStalactite {
  public BlockStoneStalactite() {
    super(Item.getItemFromBlock(Blocks.cobblestone));
    setBlockName("stoneStalactiteBlock");
    setBlockTextureName(":stoneDecoration");
  }

  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, int x, int y, int z, Random random) {
    if (world.getBlockMetadata(x, y, z) < 4) {
      boolean isWatered = world.getBlock(x, y + 2, z).getMaterial().isLiquid();
      for (int h = y; world.getBlock(x, h, z) == this; h--) {
        if (random.nextInt(5 + (isWatered ? 0 : 10)) == 0) {
          double d0 = (x + random.nextFloat());
          double d2 = (z + random.nextFloat());
          double d1 = h + 0.05D + (d0 - x) * (d2 - z);
          world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
      }
    }
  }
}
