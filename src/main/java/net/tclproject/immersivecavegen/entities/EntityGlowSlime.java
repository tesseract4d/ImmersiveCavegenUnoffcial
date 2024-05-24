package net.tclproject.immersivecavegen.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tclproject.immersivecavegen.WGConfig;

public class EntityGlowSlime extends EntitySlime implements ICaveEntity {
  public EntityGlowSlime(World world) {
    super(world);
  }

  protected String getSlimeParticle() {
    return "water";
  }

  public boolean getCanSpawnHere() {
    return (getCanSpawnHereOriginal() && ((Entity)this).posY < 60.0D);
  }

  public boolean getCanSpawnHereOriginal() {
    return (((Entity)this).worldObj.checkNoEntityCollision(((Entity)this).boundingBox) && ((Entity)this).worldObj.getCollidingBoundingBoxes((Entity)this, ((Entity)this).boundingBox).isEmpty() && !((Entity)this).worldObj.isAnyLiquid(((Entity)this).boundingBox));
  }

  public void onDeath(DamageSource p_70645_1_) {
    super.onDeath(p_70645_1_);
    if (WGConfig.glowSlimesSpawnWater)
      if (((Entity)this).worldObj.isAirBlock((int)((Entity)this).posX, (int)((Entity)this).posY, (int)((Entity)this).posZ)) {
        ((Entity)this).worldObj.setBlock((int)((Entity)this).posX, (int)((Entity)this).posY, (int)((Entity)this).posZ, (Block)Blocks.flowing_water);
      } else if (((Entity)this).worldObj.isAirBlock((int)((Entity)this).posX - 1, (int)((Entity)this).posY, (int)((Entity)this).posZ)) {
        ((Entity)this).worldObj.setBlock((int)((Entity)this).posX, (int)((Entity)this).posY, (int)((Entity)this).posZ, (Block)Blocks.flowing_water);
      } else if (((Entity)this).worldObj.isAirBlock((int)((Entity)this).posX + 1, (int)((Entity)this).posY, (int)((Entity)this).posZ)) {
        ((Entity)this).worldObj.setBlock((int)((Entity)this).posX, (int)((Entity)this).posY, (int)((Entity)this).posZ, (Block)Blocks.flowing_water);
      } else if (((Entity)this).worldObj.isAirBlock((int)((Entity)this).posX, (int)((Entity)this).posY, (int)((Entity)this).posZ - 1)) {
        ((Entity)this).worldObj.setBlock((int)((Entity)this).posX, (int)((Entity)this).posY, (int)((Entity)this).posZ, (Block)Blocks.flowing_water);
      } else if (((Entity)this).worldObj.isAirBlock((int)((Entity)this).posX, (int)((Entity)this).posY, (int)((Entity)this).posZ + 1)) {
        ((Entity)this).worldObj.setBlock((int)((Entity)this).posX, (int)((Entity)this).posY, (int)((Entity)this).posZ, (Block)Blocks.flowing_water);
      }
  }

  public void setDead() {
    int i = getSlimeSize();
    if (!((Entity)this).worldObj.isRemote && i > 1 && getHealth() <= 0.0F) {
      int j = 2 + ((Entity)this).rand.nextInt(3);
      for (int k = 0; k < j; k++) {
        float f = ((k % 2) - 0.5F) * i / 4.0F;
        float f1 = ((k / 2) - 0.5F) * i / 4.0F;
        EntityGlowSlime entityslime = createInstance();
        entityslime.setSlimeSize(i / 2);
        entityslime.setLocationAndAngles(((Entity)this).posX + f, ((Entity)this).posY + 0.5D, ((Entity)this).posZ + f1, ((Entity)this).rand.nextFloat() * 360.0F, 0.0F);
        ((Entity)this).worldObj.spawnEntityInWorld((Entity)entityslime);
      }
    }
    super.setDead();
  }

  protected EntityGlowSlime createInstance() {
    return new EntityGlowSlime(((Entity)this).worldObj);
  }

  @SideOnly(Side.CLIENT)
  public int getBrightnessForRender(float p_70070_1_) {
    return 16;
  }

  protected int getJumpDelay() {
    return ((Entity)this).rand.nextInt(20);
  }

  public float getBrightness(float p_70013_1_) {
    return 16.0F;
  }
}
