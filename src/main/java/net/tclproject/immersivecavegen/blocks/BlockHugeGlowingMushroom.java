package net.tclproject.immersivecavegen.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHugeGlowingMushroom extends BlockHugeMushroom {
  private static final String[] field_149793_a = new String[] { "skin_blue2", "skin_green2" };

  private final int field_149792_b;

  @SideOnly(Side.CLIENT)
  private IIcon[] field_149794_M;

  @SideOnly(Side.CLIENT)
  private IIcon field_149795_N;

  @SideOnly(Side.CLIENT)
  private IIcon field_149796_O;

  Random rand = new Random();

  public BlockHugeGlowingMushroom(Material p_i45412_1_, int p_i45412_2_) {
    super(p_i45412_1_, p_i45412_2_);
    this.field_149792_b = p_i45412_2_;
    setHardness(0.2F).setStepSound(Block.soundTypeWood).setLightOpacity(3).setBlockName("mushroom").setBlockTextureName("immersivecavegen:mushroom_block").setLightLevel(0.4F);
  }

  public boolean isOpaqueCube() {
    return false;
  }

  public int getDamageValue(World world, int x, int y, int z) {
    return world.getBlockMetadata(x, y, z);
  }

  @SideOnly(Side.CLIENT)
  public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
    return Item.getItemById(Block.getIdFromBlock(BlockInit.mushroomBlockBlue));
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

  @SideOnly(Side.CLIENT)
  public static int func_149997_b(int p_149997_0_) {
    return (p_149997_0_ ^ 0xFFFFFFFF) & 0xF;
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
    Block block = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_);
    if (block != p_149646_1_.getBlock(p_149646_2_ - Facing.offsetsXForSide[p_149646_5_], p_149646_3_ - Facing.offsetsYForSide[p_149646_5_], p_149646_4_ - Facing.offsetsZForSide[p_149646_5_]))
      return true;
    if (block == this)
      return false;
    return (block == this) ? false : super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
  }

  public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
    return Item.getItemFromBlock(BlockInit.cavePlantBlock);
  }

  public int damageDropped(int metadata) {
    return 4 + this.rand.nextInt(2);
  }

  @SideOnly(Side.CLIENT)
  public int getRenderBlockPass() {
    return 1;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
    return (p_149691_2_ == 10 && p_149691_1_ > 1) ? this.field_149795_N : ((p_149691_2_ >= 1 && p_149691_2_ <= 9 && p_149691_1_ == 1) ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ >= 1 && p_149691_2_ <= 3 && p_149691_1_ == 2) ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ >= 7 && p_149691_2_ <= 9 && p_149691_1_ == 3) ? this.field_149794_M[this.field_149792_b] : (((p_149691_2_ == 1 || p_149691_2_ == 4 || p_149691_2_ == 7) && p_149691_1_ == 4) ? this.field_149794_M[this.field_149792_b] : (((p_149691_2_ == 3 || p_149691_2_ == 6 || p_149691_2_ == 9) && p_149691_1_ == 5) ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ == 14) ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ == 15) ? this.field_149795_N : this.field_149796_O)))))));
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister p_149651_1_) {
    this.field_149794_M = new IIcon[field_149793_a.length];
    for (int i = 0; i < this.field_149794_M.length; i++)
      this.field_149794_M[i] = p_149651_1_.registerIcon(getTextureName() + "_" + field_149793_a[i]);
    this.field_149796_O = p_149651_1_.registerIcon(getTextureName() + "_inside");
    this.field_149795_N = p_149651_1_.registerIcon(getTextureName() + "_skin_stem");
  }
}
