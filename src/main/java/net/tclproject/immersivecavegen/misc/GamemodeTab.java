package net.tclproject.immersivecavegen.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public final class GamemodeTab extends CreativeTabs {
  public static CreativeTabs tabCaves = new GamemodeTab("caves");

  public GamemodeTab(String lable) {
    super(lable);
  }

  public Item getTabIconItem() {
    return Item.getItemFromBlock(Blocks.stone);
  }
}
