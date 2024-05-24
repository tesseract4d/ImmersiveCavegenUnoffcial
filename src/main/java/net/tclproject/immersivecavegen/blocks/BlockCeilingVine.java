package net.tclproject.immersivecavegen.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.world.World;
import net.tclproject.immersivecavegen.misc.GamemodeTab;

public class BlockCeilingVine extends BlockVine {
  public BlockCeilingVine() {
    setCreativeTab(GamemodeTab.tabCaves);
  }

  public boolean canPlaceBlockOnSide(World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_, int p_149707_5_) {
    switch (p_149707_5_) {
      case 1:
        return func_150093_a(p_149707_1_.getBlock(p_149707_2_, p_149707_3_ + 1, p_149707_4_));
      case 2:
        return func_150093_a(p_149707_1_.getBlock(p_149707_2_, p_149707_3_, p_149707_4_ + 1));
      case 3:
        return func_150093_a(p_149707_1_.getBlock(p_149707_2_, p_149707_3_, p_149707_4_ - 1));
      case 4:
        return func_150093_a(p_149707_1_.getBlock(p_149707_2_ + 1, p_149707_3_, p_149707_4_));
      case 5:
        return func_150093_a(p_149707_1_.getBlock(p_149707_2_ - 1, p_149707_3_, p_149707_4_));
      case 0:
        return true;
    }
    return false;
  }

  public boolean func_150093_a(Block p_150093_1_) {
    return (p_150093_1_.renderAsNormalBlock() && p_150093_1_.getMaterial().blocksMovement());
  }
}
