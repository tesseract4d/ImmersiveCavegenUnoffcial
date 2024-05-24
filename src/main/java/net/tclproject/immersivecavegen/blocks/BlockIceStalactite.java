package net.tclproject.immersivecavegen.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tclproject.immersivecavegen.misc.GamemodeTab;

public class BlockIceStalactite extends Block {
  private IIcon[] icons = new IIcon[3];

  public BlockIceStalactite() {
    super(Material.ice);
    setCreativeTab(GamemodeTab.tabCaves);
    setResistance(0.6F);
    setBlockName("icestalactite");
    setStepSound(Block.soundTypeGlass);
  }

  public int getRenderBlockPass() {
    return 1;
  }

  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    if (entity.isEntityAlive())
      entity.attackEntityFrom(DamageSource.cactus, 1.0F);
  }

  public boolean canBlockStay(World world, int x, int y, int z) {
    return (world.getBlock(x, y + 1, z).isNormalCube((IBlockAccess)world, x, y, z) || world.getBlock(x, y + 1, z) instanceof BlockIceStalactite || world.getBlock(x, y + 1, z).getMaterial().getMaterialMapColor() == MapColor.stoneColor);
  }

  public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    return (canBlockStay(world, x, y, z) && super.canPlaceBlockAt(world, x, y, z));
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
    if (metadata > 2)
      metadata = 0;
    return this.icons[metadata];
  }

  public int getRenderType() {
    return 1;
  }

  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    for (int i = 0; i < this.icons.length; i++)
      par3List.add(new ItemStack(par1, 1, i));
  }

  public Item getItemDropped(int metadata, Random random, int par3) {
    return Item.getItemFromBlock(Blocks.ice);
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    if (!canBlockStay(world, x, y, z)) {
      onBlockEventReceived(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
      world.setBlockToAir(x, y, z);
    }
  }

  public int quantityDropped(Random rand) {
    return rand.nextInt(3) - 1;
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister iconRegister) {
    for (int i = 0; i < this.icons.length; i++)
      this.icons[i] = iconRegister.registerIcon("immersivecavegen:icestalactite" + i);
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
      case 9:
        setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.8F, 0.75F);
        return;
      case 10:
        setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.4F, 0.75F);
        return;
    }
    setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
  }
}
