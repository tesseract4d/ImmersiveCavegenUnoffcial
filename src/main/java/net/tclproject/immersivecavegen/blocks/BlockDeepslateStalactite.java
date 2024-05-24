package net.tclproject.immersivecavegen.blocks;

import cpw.mods.fml.common.Optional.Method;
import ganymedes01.etfuturum.ModBlocks;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockDeepslateStalactite extends BlockBaseStalactite {
  public BlockDeepslateStalactite() {
    super(Item.getItemFromBlock(Blocks.cobblestone));
    setBlockName("deepslateStalactiteBlock");
    setBlockTextureName(":deepslateDecoration");
    setHardness(1.5F);
    setResistance(3.0F);
  }

  @Method(modid = "etfuturum")
  public Item getItemDropped(int metadata, Random random, int par3) {
    return Item.getItemFromBlock(ModBlocks.deepslate);
  }

  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    if (entity.isEntityAlive() && !(entity instanceof net.tclproject.immersivecavegen.entities.ICaveEntity))
      entity.attackEntityFrom(DamageSource.cactus, 2.5F);
    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).capabilities.isCreativeMode || !((EntityPlayer)entity).capabilities.isFlying) {
      entity.motionX *= 0.7D;
      entity.motionZ *= 0.7D;
    }
  }
}
