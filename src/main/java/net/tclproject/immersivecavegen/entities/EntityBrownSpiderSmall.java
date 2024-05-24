package net.tclproject.immersivecavegen.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.World;

public class EntityBrownSpiderSmall extends EntitySpider implements ICaveEntity {
  public static float[][] sizes = new float[][] { { 0.5F, 1.4F, 2.3F }, { 0.32F, 0.9F, 1.48F } };
  
  public EntityBrownSpiderSmall(World world) {
    super(world);
    setSize(sizes[0][0], sizes[1][0]);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D * sizes[0][0]);
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
  }
  
  public boolean getCanSpawnHere() {
    return (super.getCanSpawnHere() && ((Entity)this).posY < 60.0D);
  }
  
  public boolean attackEntityAsMob(Entity p_70652_1_) {
    if (super.attackEntityAsMob(p_70652_1_))
      return true; 
    return false;
  }
}
