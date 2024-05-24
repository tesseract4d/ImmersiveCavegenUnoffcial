package net.tclproject.immersivecavegen.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.tclproject.immersivecavegen.misc.GamemodeTab;

public class BlockCavePlant extends BlockBush implements IGrowable {
  private IIcon[] icons = new IIcon[8];

  public BlockCavePlant() {
    super(Material.plants);
    setCreativeTab(GamemodeTab.tabCaves);
    setLightOpacity(0);
    setStepSound(Block.soundTypeGrass);
    setResistance(0.6F);
    setBlockName("caveplant");
  }

  public boolean canBlockStay(World world, int x, int y, int z) {
    if (world.isBlockNormalCubeDefault(x, y - 1, z, false))
      return true;
    Block bellowId = world.getBlock(x, y - 1, z);
    return (bellowId.getMaterial().getMaterialMapColor() == MapColor.stoneColor || (bellowId == this && world.getBlockMetadata(x, y - 1, z) == 4));
  }

  public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
    return true;
  }

  public int damageDropped(int meta) {
    return 0;
  }

  public int getDamageValue(World world, int x, int y, int z) {
    return world.getBlockMetadata(x, y, z);
  }

  public boolean canSilkHarvest() {
    return true;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int metadata) {
    if (metadata > 7)
      metadata = 0;
    return this.icons[metadata];
  }

  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    for (int i = 0; i < this.icons.length; i++)
      par3List.add(new ItemStack(par1, 1, i));
  }

  public Item getItemDropped(int par1, Random par2Random, int par3) {
    return Items.redstone;
  }

  public void func_149855_e(World world, int x, int y, int z) {
    if (!canBlockStay(world, x, y, z))
      world.setBlockToAir(x, y, z);
  }

  public void onNeighborBlockChange(World world, int x, int y, int z, Block blockID) {
    func_149855_e(world, x, y, z);
  }

  public int quantityDropped(Random rand) {
    return 1;
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister iconRegister) {
    for (int i = 0; i < this.icons.length; i++)
      this.icons[i] = iconRegister.registerIcon("immersivecavegen:caveplant" + i);
  }

  public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
    int metadata = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
    switch (metadata) {
      case 1:
        setBlockBounds(0.25F, 0.0F, 0.25F, 0.6F, 0.75F, 0.75F);
        return;
      case 2:
      case 4:
        setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.4F, 0.75F);
        return;
    }
    setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
  }

  public static void growMushroom(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_, int l) {
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
              } else {
                flag = true;
              }
            }
          }
        }
        if (!flag)
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
                p_76484_1_.setBlock(l2, k1, i2, (l == 0) ? BlockInit.mushroomBlockBlue : BlockInit.mushroomBlockGreen, j2, 2);
              continue;
            }
          }
        }
        for (k1 = 0; k1 < i1; k1++) {
          Block block2 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + k1, p_76484_5_);
          if (block2.canBeReplacedByLeaves((IBlockAccess)p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_))
            p_76484_1_.setBlock(p_76484_3_, p_76484_4_ + k1, p_76484_5_, (l == 0) ? BlockInit.mushroomBlockBlue : BlockInit.mushroomBlockGreen, 10, 2);
        }
        return;
      }
    } else {
      return;
    }
  }

  protected boolean func_149854_a(Block par1) {
    return true;
  }

  public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
    return !(direction == ForgeDirection.UP);
  }

  public boolean func_149851_a(World world, int x, int y, int z, boolean whatisthis) {
    int meta = world.getBlockMetadata(x, y, z);
    return (meta == 2 || meta == 3 || meta == 4 || meta == 5);
  }

  public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_) {
    return (p_149852_1_.rand.nextFloat() < 0.45D);
  }

  public void func_149853_b(World world, Random rand, int x, int y, int z) {
    int meta = world.getBlockMetadata(x, y, z);
    if (meta == 2 || meta == 3) {
      growMushroom(world, rand, x, y, z, 1);
    } else if (meta == 4 || meta == 5) {
      growMushroom(world, rand, x, y, z, 0);
    }
  }
}
