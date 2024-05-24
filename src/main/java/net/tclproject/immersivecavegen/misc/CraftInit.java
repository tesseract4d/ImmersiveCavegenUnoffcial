package net.tclproject.immersivecavegen.misc;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.tclproject.immersivecavegen.blocks.BlockInit;
import net.tclproject.immersivecavegen.items.ItemInit;

public final class CraftInit {
  public static final void init() {
    GameRegistry.addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(Blocks.web, 4), new Object[] { "S S", " B ", "S S", Character.valueOf('S'), Items.string, Character.valueOf('B'), Items.slime_ball }));
    GameRegistry.addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(ItemInit.itemStalactiteDagger), new Object[] { "S", "W", Character.valueOf('S'), BlockInit.stoneStalactiteBlock, Character.valueOf('W'), Items.stick }));
    GameRegistry.addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack(Item.getItemFromBlock(BlockInit.ceilingVine)), new Object[] { new ItemStack(Blocks.vine), Items.stick }));
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        GameRegistry.addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack(ItemInit.glowSoup, 1, 0), new Object[] { new ItemStack(BlockInit.cavePlantBlock, 2, 2 + i), new ItemStack(BlockInit.cavePlantBlock, 2, 2 + j), Items.bowl }));
      } 
    } 
  }
}
