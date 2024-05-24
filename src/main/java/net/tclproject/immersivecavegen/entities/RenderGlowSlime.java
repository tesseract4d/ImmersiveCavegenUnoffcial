package net.tclproject.immersivecavegen.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderGlowSlime extends RenderSlime {
  private static final ResourceLocation slimeTextures = new ResourceLocation("immersivecavegen:textures/entity/glowslime.png");
  
  private ModelBase scaleAmount;
  
  public RenderGlowSlime(ModelBase p_i1267_1_, ModelBase p_i1267_2_, float p_i1267_3_) {
    super(p_i1267_1_, p_i1267_2_, p_i1267_3_);
    this.scaleAmount = p_i1267_2_;
  }
  
  protected ResourceLocation getEntityTexture(EntitySlime p_110775_1_) {
    return slimeTextures;
  }
  
  protected int shouldRenderPass(EntitySlime p_77032_1_, int p_77032_2_, float p_77032_3_) {
    if (p_77032_1_.isInvisible())
      return 0; 
    if (p_77032_2_ == 0) {
      setRenderPassModel(this.scaleAmount);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 100.0F, 100.0F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      return 1;
    } 
    if (p_77032_2_ == 1) {
      GL11.glDisable(3042);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    } 
    return -1;
  }
}
