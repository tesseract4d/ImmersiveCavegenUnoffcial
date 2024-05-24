package net.tclproject.immersivecavegen.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tclproject.immersivecavegen.misc.GamemodeTab;

public class BlockBaseStalactite extends Block {
  private final Item droppedItem;

  private IIcon[] icons = new IIcon[26];

  public BlockBaseStalactite(Item drop) {
    super(Material.rock);
    this.droppedItem = drop;
    setHardness(0.8F);
    setStepSound(Block.soundTypeStone);
    setCreativeTab(GamemodeTab.tabCaves);
  }

  public Item getItemDropped(int metadata, Random random, int par3) {
    return this.droppedItem;
  }

  public int quantityDropped(Random rand) {
    return rand.nextInt(3) - 1;
  }

  public boolean canBlockStay(World world, int x, int y, int z) {
    boolean result = true;
    int metaAbove = world.getBlockMetadata(x, y + 1, z);
    int metaUnder = world.getBlockMetadata(x, y - 1, z);
    int metadata = world.getBlockMetadata(x, y, z);
    Block above = world.getBlock(x, y + 1, z);
    Block below = world.getBlock(x, y - 1, z);
    if (!(above instanceof BlockBaseStalactite))
      metaAbove = 0;
    if (!(below instanceof BlockBaseStalactite))
      metaUnder = 0;
    if (metadata >= 0 && metadata <= 3 &&
      !above.isNormalCube())
      result = false;
    if ((metadata == 0 || metadata == 8 || metadata == 9 || metadata == 10) && !below.isNormalCube())
      result = false;
    if ((metadata == 4 || metadata == 5) &&
      !above.isNormalCube() && metaAbove != 4 && metaAbove != 5 && metaAbove != 6 && metaAbove != 12 && metaAbove != 3 && !below.isNormalCube() && metaUnder != 4 && metaUnder != 5 && metaUnder != 11 && metaUnder != 8 && metaUnder != 7)
      result = false;
    if ((metadata == 6 || metadata == 12) &&
      !below.isNormalCube() && metaUnder != 4 && metaUnder != 5 && metaUnder != 8)
      result = false;
    if ((metadata == 7 || metadata == 11) &&
      !above.isNormalCube() && metaAbove != 4 && metaAbove != 5 && metaAbove != 3)
      result = false;
    return result;
  }

  protected boolean canSilkHarvest() {
    return true;
  }

  public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    return null;
  }

  public int getDamageValue(World world, int x, int y, int z) {
    return world.getBlockMetadata(x, y, z);
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int metadata) {
    if (metadata > 25)
      metadata = 0;
    return this.icons[metadata];
  }

  public int getRenderType() {
    return 1;
  }

  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    for (int i = 0; i < 13; i++)
      par3List.add(new ItemStack(par1, 1, i));
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
    updateTick(world, x, y, z, (Random)null);
  }

  public void updateTick(World world, int x, int y, int z, Random random) {
    if (!canBlockStay(world, x, y, z)) {
      dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
      world.setBlockToAir(x, y, z);
    }
  }

  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    if (entity.isEntityAlive() && !(entity instanceof net.tclproject.immersivecavegen.entities.ICaveEntity))
      entity.attackEntityFrom(DamageSource.cactus, 1.0F);
    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).capabilities.isCreativeMode || !((EntityPlayer)entity).capabilities.isFlying) {
      entity.motionX *= 0.7D;
      entity.motionZ *= 0.7D;
    }
  }

  public void onFallenUpon(World world, int par2, int par3, int par4, Entity entity, float par6) {
    if (entity.isEntityAlive())
      entity.attackEntityFrom(DamageSource.generic, 5.0F);
  }

  public void onNeighborBlockChange(World world, int x, int y, int z, Block blockID) {
    if (!world.isRemote && !canBlockStay(world, x, y, z))
      world.func_147480_a(x, y, z, true);
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister iconRegister) {
    for (int i = 0; i < 13; i++)
      this.icons[i] = iconRegister.registerIcon("immersivecavegen" + getTextureName() + i);
  }

  public boolean isOpaqueCube() {
    return false;
  }

  public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
    int metadata = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
    switch (metadata) {
      case 1:
        setBlockBounds(0.25F, 0.2F, 0.25F, 0.75F, 1.0F, 0.75F);
        return;
      case 2:
        setBlockBounds(0.25F, 0.5F, 0.25F, 0.75F, 1.0F, 0.75F);
        return;
      default:
        setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
        return;
      case 9:
        setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.8F, 0.75F);
        return;
      case 10:
        break;
    }
    setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.4F, 0.75F);
  }
}
