package net.tclproject.immersivecavegen.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GlowSoup extends ItemSoup {
  public GlowSoup(int p_i45330_1_) {
    super(p_i45330_1_);
  }
  
  public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
    p_77654_3_.addPotionEffect(new PotionEffect(Potion.regeneration.id, 150, 1));
    super.onEaten(p_77654_1_, p_77654_2_, p_77654_3_);
    return new ItemStack(Items.bowl);
  }
}
