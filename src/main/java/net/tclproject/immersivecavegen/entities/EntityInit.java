package net.tclproject.immersivecavegen.entities;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.world.biome.BiomeGenBase;
import net.tclproject.immersivecavegen.ImmersiveCavegen;
import net.tclproject.immersivecavegen.WGConfig;

public class EntityInit {
  public static void init() {
    int spiderId = (WGConfig.entityBrSpID == -1) ? EntityRegistry.findGlobalUniqueEntityId() : WGConfig.entityBrSpID;
    EntityRegistry.registerGlobalEntityID(EntityBrownSpider.class, "BrownSpider", spiderId);
    EntityRegistry.registerModEntity(EntityBrownSpider.class, "BrownSpider", spiderId, ImmersiveCavegen.instance, 64, 1, true);
    int spiderId2 = (WGConfig.entitySmBrSpID == -1) ? EntityRegistry.findGlobalUniqueEntityId() : WGConfig.entitySmBrSpID;
    EntityRegistry.registerGlobalEntityID(EntityBrownSpiderSmall.class, "BrownSpiderSmall", spiderId2);
    EntityRegistry.registerModEntity(EntityBrownSpiderSmall.class, "BrownSpiderSmall", spiderId2, ImmersiveCavegen.instance, 64, 1, true);
    int spiderId3 = (WGConfig.entityLBrSpID == -1) ? EntityRegistry.findGlobalUniqueEntityId() : WGConfig.entityLBrSpID;
    EntityRegistry.registerGlobalEntityID(EntityBrownSpiderLarge.class, "BrownSpiderLarge", spiderId3);
    EntityRegistry.registerModEntity(EntityBrownSpiderLarge.class, "BrownSpiderLarge", spiderId3, ImmersiveCavegen.instance, 64, 1, true);
    int glowSlimeID = (WGConfig.entityGlSlID == -1) ? EntityRegistry.findGlobalUniqueEntityId() : WGConfig.entityGlSlID;
    EntityRegistry.registerGlobalEntityID(EntityGlowSlime.class, "GlowSlime", glowSlimeID);
    EntityRegistry.registerModEntity(EntityGlowSlime.class, "GlowSlime", glowSlimeID, ImmersiveCavegen.instance, 64, 1, true);
  }

  public static BiomeGenBase[] getNotNullBiomeGenArray() {
    BiomeGenBase[] arrayWithNullValues = BiomeGenBase.getBiomeGenArray();
    List<BiomeGenBase> list = new ArrayList<>();
    for (BiomeGenBase s : arrayWithNullValues) {
      if (s != null)
        list.add(s);
    }
    BiomeGenBase[] returnedArray = list.<BiomeGenBase>toArray(new BiomeGenBase[list.size()]);
    return returnedArray;
  }

  @SideOnly(Side.CLIENT)
  public static void clientInit() {
    RenderingRegistry.registerEntityRenderingHandler(EntityBrownSpider.class, (Render)new RenderBrownSpider());
    RenderingRegistry.registerEntityRenderingHandler(EntityBrownSpiderSmall.class, (Render)new RenderBrownSpiderSmall());
    RenderingRegistry.registerEntityRenderingHandler(EntityBrownSpiderLarge.class, (Render)new RenderBrownSpiderLarge());
    RenderingRegistry.registerEntityRenderingHandler(EntityGlowSlime.class, (Render)new RenderGlowSlime((ModelBase)new ModelSlime(16), (ModelBase)new ModelSlime(0), 0.25F));
  }
}
