package net.tclproject.immersivecavegen.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBrownSpiderSmall extends RenderSpider {
  private static final ResourceLocation brownSpiderTextures = new ResourceLocation("immersivecavegen:textures/entity/brown_spider.png");
  
  private boolean shadowSizeChanged = false;
  
  protected void preRenderCallback(EntityBrownSpiderSmall p_77041_1_, float p_77041_2_) {
    GL11.glScalef(EntityBrownSpider.sizes[0][0] / 1.4F, EntityBrownSpider.sizes[1][0] / 0.9F, EntityBrownSpider.sizes[0][0] / 1.4F);
    if (!this.shadowSizeChanged) {
      ((Render)this).shadowSize *= EntityBrownSpider.sizes[0][0];
      this.shadowSizeChanged = true;
    } 
  }
  
  protected ResourceLocation getEntityTexture(EntityBrownSpiderSmall p_110775_1_) {
    return brownSpiderTextures;
  }
  
  protected ResourceLocation getEntityTexture(EntitySpider p_110775_1_) {
    return getEntityTexture((EntityBrownSpiderSmall)p_110775_1_);
  }
  
  protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
    preRenderCallback((EntityBrownSpiderSmall)p_77041_1_, p_77041_2_);
  }
  
  protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
    return getEntityTexture((EntityBrownSpiderSmall)p_110775_1_);
  }
}
