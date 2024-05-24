package net.tclproject.immersivecavegen.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityBrownSpider extends EntitySpider implements ICaveEntity {
  public static float[][] sizes = new float[][] { { 0.5F, 1.4F, 2.3F }, { 0.32F, 0.9F, 1.48F } };
  
  public EntityBrownSpider(World world) {
    super(world);
    setSize(sizes[0][1], sizes[1][1]);
  }
  
  public boolean getCanSpawnHere() {
    return (super.getCanSpawnHere() && ((Entity)this).posY < 60.0D);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D * sizes[0][1]);
  }
  
  public boolean attackEntityAsMob(Entity p_70652_1_) {
    if (super.attackEntityAsMob(p_70652_1_)) {
      if (p_70652_1_ instanceof EntityLivingBase) {
        byte b0 = 0;
        if (((Entity)this).worldObj.difficultySetting == EnumDifficulty.NORMAL) {
          b0 = 7;
        } else if (((Entity)this).worldObj.difficultySetting == EnumDifficulty.HARD) {
          b0 = 15;
        } 
        if (b0 > 0) {
          ((EntityLivingBase)p_70652_1_).addPotionEffect(new PotionEffect(Potion.poison.id, b0 * 20, 0));
          if (((Entity)this).rand.nextInt(21 - b0 - 2) == 0) {
            ((EntityLivingBase)p_70652_1_).addPotionEffect(new PotionEffect(Potion.blindness.id, b0 * 5, 0));
            ((EntityLivingBase)p_70652_1_).addPotionEffect(new PotionEffect(Potion.confusion.id, b0 * 10, 0));
          } 
        } 
      } 
      return true;
    } 
    return false;
  }
}
