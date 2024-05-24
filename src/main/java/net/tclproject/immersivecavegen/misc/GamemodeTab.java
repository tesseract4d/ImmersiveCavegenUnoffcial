package net.tclproject.immersivecavegen.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.tclproject.immersivecavegen.items.ItemInit;

public final class GamemodeTab extends CreativeTabs {
  public static CreativeTabs tabCaves = new GamemodeTab("caves");
  
  public GamemodeTab(String lable) {
    super(lable);
  }
  
  public Item getTabIconItem() {
    return (new ItemStack(ItemInit.itemTabPlaceholder)).getItem();
  }
}
