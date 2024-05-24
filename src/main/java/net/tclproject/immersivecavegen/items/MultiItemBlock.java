package net.tclproject.immersivecavegen.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class MultiItemBlock extends ItemBlock {
  private final Block block;
  
  public MultiItemBlock(Block block) {
    super(block);
    this.block = block;
    setHasSubtypes(true);
  }
  
  public MultiItemBlock(Block block, ArrayList names) {
    super(block);
    this.block = block;
    setHasSubtypes(true);
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int damage) {
    if (damage > 26)
      damage = 0; 
    return this.block.getIcon(0, damage);
  }
  
  public int getMetadata(int damage) {
    if (damage > 26)
      damage = 0; 
    return damage;
  }
  
  public String getUnlocalizedName(ItemStack itemstack) {
    return getUnlocalizedName() + itemstack.getItemDamage();
  }
}
