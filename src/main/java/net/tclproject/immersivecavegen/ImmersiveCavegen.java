package net.tclproject.immersivecavegen;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.tclproject.immersivecavegen.blocks.BlockInit;
import net.tclproject.immersivecavegen.entities.EntityBrownSpider;
import net.tclproject.immersivecavegen.entities.EntityBrownSpiderLarge;
import net.tclproject.immersivecavegen.entities.EntityBrownSpiderSmall;
import net.tclproject.immersivecavegen.entities.EntityGlowSlime;
import net.tclproject.immersivecavegen.entities.EntityInit;
import net.tclproject.immersivecavegen.items.ItemInit;
import net.tclproject.immersivecavegen.misc.CraftInit;
import net.tclproject.immersivecavegen.world.CavesDecorator;
import net.tclproject.immersivecavegen.world.GenerateStoneStalactite;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

@Mod(modid = "immersivecavegen", version = "1.2g-hotfix4", name = "Immersive Cavegen",dependencies = "required-after:MysteriumLib")
public class ImmersiveCavegen {
  public static final String MODID = "immersivecavegen";

  public static final String VERSION = "1.2g-hotfix4";

  public static Random rand = new Random();

  public static int giganticCaveChance;

  public Logger logger;

  @Instance("immersivecavegen")
  public static ImmersiveCavegen instance;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    this.logger = event.getModLog();
    WGConfig.init(event.getModConfigurationDirectory().toString(), event);
    if (!WGConfig.serveronly) {
      BlockInit.init();
      ItemInit.init();
      if (WGConfig.enableEntities) {
        EntityInit.init();
        if (event.getSide().isClient())
          EntityInit.clientInit();
      }
    }
    instance = this;
    MinecraftForge.EVENT_BUS.register(this);
    FMLCommonHandler.instance().bus().register(this);
    MinecraftForge.TERRAIN_GEN_BUS.register(this);
    MinecraftForge.ORE_GEN_BUS.register(this);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    if (!WGConfig.serveronly) {
      CraftInit.init();
    }
    String[] list = "netherrack,stone,grass,dirt,cobblestone,gravel,gold_ore,iron_ore,coal_ore,lapis_ore,sandstone,diamond_ore,redstone_ore,lit_redstone_ore,ice,snow,clay,monster_egg,emerald_ore".split(",");
    String[] arr$ = list;
    int len = list.length;
    for (int i$ = 0; i$ < len; i$++) {
      String txt = arr$[i$];
      try {
        Block block = (Block)GameData.getBlockRegistry().getObject(txt.trim());
        if (block != null && block.getMaterial() != Material.air)
          GenerateStoneStalactite.blockWhiteList.add(block);
      } catch (Exception ignored) {}
    }
    giganticCaveChance = WGConfig.giantCaveChance;
    CavesDecorator decor = new CavesDecorator();
    if (!WGConfig.serveronly && WGConfig.caveDecorations)
      MinecraftForge.EVENT_BUS.register(decor);
  }


  ResourceLocation rc = new ResourceLocation("immersivecavegen:textures/custom.png");

  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onRenderEvent(RenderPlayerEvent.Specials.Pre event) {
    try {
      EntityPlayer player = event.entityPlayer;
      if ("Nlghtwing".equalsIgnoreCase(player.getDisplayName()))
        render(player);
    } catch (Throwable var4) {
      var4.printStackTrace();
    }
  }

  private void render(EntityPlayer player) {
    if (!player.isInvisible()) {
      Minecraft.getMinecraft().getTextureManager().bindTexture(this.rc);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.0F, 0.125F);
      double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * 0.0625D - ((Entity)player).prevPosX + (((Entity)player).posX - ((Entity)player).prevPosX) * 0.0625D;
      double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * 0.0625D - ((Entity)player).prevPosY + (((Entity)player).posY - ((Entity)player).prevPosY) * 0.0625D;
      double d0 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * 0.0625D - ((Entity)player).prevPosZ + (((Entity)player).posZ - ((Entity)player).prevPosZ) * 0.0625D;
      float f4 = ((EntityLivingBase)player).prevRenderYawOffset + (((EntityLivingBase)player).renderYawOffset - ((EntityLivingBase)player).prevRenderYawOffset) * 0.0625F;
      double d1 = MathHelper.sin(f4 * 3.1415927F / 180.0F);
      double d2 = -MathHelper.cos(f4 * 3.1415927F / 180.0F);
      float f5 = (float)d4 * 10.0F;
      if (f5 < -6.0F)
        f5 = -6.0F;
      if (f5 > 32.0F)
        f5 = 32.0F;
      float f6 = (float)(d3 * d1 + d0 * d2) * 100.0F;
      float f7 = (float)(d3 * d2 - d0 * d1) * 100.0F;
      if (f6 < 0.0F)
        f6 = 0.0F;
      float f8 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * 0.0625F;
      f5 += MathHelper.sin((((Entity)player).prevDistanceWalkedModified + (((Entity)player).distanceWalkedModified - ((Entity)player).prevDistanceWalkedModified) * 0.0625F) * 6.0F) * 32.0F * f8;
      if (player.isSneaking())
        f5 += 25.0F;
      GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(0.0F, 0.0F, 0.125F);
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      (getCapeModel()).cape.render(0.0625F);
      GL11.glPopMatrix();
    }
  }

  @SideOnly(Side.CLIENT)
  private CapeModel getCapeModel() {
    return new CapeModel();
  }

  @SideOnly(Side.CLIENT)
  public class CapeModel extends ModelBiped {
    public ModelRenderer cape;

    public CapeModel() {
      ((ModelBase)this).textureWidth = 64;
      ((ModelBase)this).textureHeight = 32;
      this.cape = new ModelRenderer((ModelBase)this, 0, 0);
      this.cape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1);
      this.cape.setRotationPoint(0.0F, 0.0F, 2.0F);
      this.cape.setTextureSize(64, 32);
      setRotation(this.cape, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.cape.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void playerBlind(TickEvent.PlayerTickEvent event) {
    if (event.player.posY < 35 && WGConfig.jumpscare && rand.nextFloat() > 0.99993F && !((Entity)event.player).worldObj.isRemote && ((Entity)event.player).worldObj.provider.terrainType != WorldType.FLAT &&
      !event.player.isPotionActive(Potion.blindness)) {
      event.player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0, false));
      event.player.playSound("random.fizz", 0.75F, 1.0F);
    }
  }

  @SubscribeEvent
  public void onOreGen(OreGenEvent.GenerateMinable event) {
    if (event.type == OreGenEvent.GenerateMinable.EventType.DIRT && WGConfig.turnOffVanillaUndergroundDirt)
      event.setResult(Event.Result.DENY);
  }

  @SubscribeEvent
  public void onWorldLoad(WorldEvent.Load event) {
    WGConfig.loadConfiguration();
  }

  @SubscribeEvent
  public void onWorldUnload(WorldEvent.Unload event) {
    WGConfig.loadConfiguration();
  }

  @SubscribeEvent
  public void onMapInit(InitMapGenEvent event) {
    WGConfig.loadConfiguration();
  }

  @SubscribeEvent
  public void onNoiseGenInit(InitNoiseGensEvent event) {
    WGConfig.loadConfiguration();
  }

  @SubscribeEvent
  public void onFMLNetworkJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
    WGConfig.loadConfiguration();
  }

  @SubscribeEvent
  public void onFMLNetworkLeave(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
    WGConfig.loadConfiguration();
  }

  @SubscribeEvent
  public void entitySpawnEvent(LivingSpawnEvent.CheckSpawn event) {
    //this;
    if (!WGConfig.serveronly && event.world.provider.terrainType != WorldType.FLAT && event.y < 34.0F && WGConfig.enableEntities && event.getResult() != Event.Result.DENY && !(event.entity instanceof net.tclproject.immersivecavegen.entities.ICaveEntity) && rand.nextInt(WGConfig.mobSpawnChance) == 0 && event.world.getBlockLightValue((int)event.x, (int)event.y, (int)event.z) <= rand.nextInt(8)) {
      BiomeGenBase biome = event.world.getBiomeGenForCoords((int)event.x, (int)event.z);
      if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MUSHROOM) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.DENSE)) {
        EntityGlowSlime entityToSpawn = new EntityGlowSlime(event.world);
        entityToSpawn.setLocationAndAngles(event.x, event.y, event.z, rand.nextFloat() * 360.0F, 0.0F);
        event.world.spawnEntityInWorld((Entity)entityToSpawn);
      } else if (!BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.NETHER) && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.END)) {
        float randomValue = rand.nextFloat();
        if (randomValue < 0.5D) {
          EntityBrownSpiderSmall entityToSpawn = new EntityBrownSpiderSmall(event.world);
          entityToSpawn.setLocationAndAngles(event.x, event.y, event.z, rand.nextFloat() * 360.0F, 0.0F);
          event.world.spawnEntityInWorld((Entity)entityToSpawn);
        } else if (randomValue < 0.83D) {
          EntityBrownSpider entityToSpawn = new EntityBrownSpider(event.world);
          entityToSpawn.setLocationAndAngles(event.x, event.y, event.z, rand.nextFloat() * 360.0F, 0.0F);
          event.world.spawnEntityInWorld((Entity)entityToSpawn);
        } else {
          EntityBrownSpiderLarge entityToSpawn = new EntityBrownSpiderLarge(event.world);
          entityToSpawn.setLocationAndAngles(event.x, event.y, event.z, rand.nextFloat() * 360.0F, 0.0F);
          event.world.spawnEntityInWorld((Entity)entityToSpawn);
        }
      }
    }
  }
}
