package net.tclproject.immersivecavegen.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.tclproject.immersivecavegen.misc.GamemodeTab;

public class ScorchedLavaStone extends Block {
  public ScorchedLavaStone() {
    super(Material.rock);
    setCreativeTab(GamemodeTab.tabCaves).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setBlockName("scorchedLavaStoneBlock").setBlockTextureName("immersivecavegen:scorched_lava_stone");
  }

  public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
    return Item.getItemFromBlock(Blocks.cobblestone);
  }

  public void breakBlock(World p_149749_1_, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
    super.breakBlock(p_149749_1_, x, y, z, p_149749_5_, p_149749_6_);
    p_149749_1_.setBlock(x, y, z, (Block)Blocks.flowing_lava, 0, 3);
  }

  public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
    return null;
  }

  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    if (entity instanceof net.minecraft.entity.EntityLivingBase)
      breakBlock(world, x, y, z, this, 0);
    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).capabilities.isCreativeMode || !((EntityPlayer)entity).capabilities.isFlying) {
      entity.motionX *= 0.7D;
      entity.motionZ *= 0.7D;
    }
  }
}
