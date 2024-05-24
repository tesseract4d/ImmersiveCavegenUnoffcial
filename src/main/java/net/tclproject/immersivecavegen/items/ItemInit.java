package net.tclproject.immersivecavegen.items;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.tclproject.immersivecavegen.misc.GamemodeTab;

public final class ItemInit {
    public static Item itemTabPlaceholder;

    public static Item glowSoup;

    public static Item itemStalactiteDagger;

    public static Item itemStalagmiteDagger;

    public static Item itemSpawnSpSm;

    public static Item itemSpawnSp;

    public static Item itemSpawnSpL;

    public static Item itemSpawnGlSl;

    public static Item.ToolMaterial stalactiteMat = EnumHelper.addToolMaterial("Stalactite", 0, 89, 4.0F, 1.3F, 16);

    public static Item.ToolMaterial stalagmiteMat = EnumHelper.addToolMaterial("Stalagmite", 0, 112, 4.0F, 1.5F, 10);

    public static void init() {
        itemTabPlaceholder = (new ItemTabPlaceholder()).setUnlocalizedName("ItemTabPlaceholder").setTextureName("immersivecavegen:tab_icon");
        GameRegistry.registerItem(itemTabPlaceholder, "ItemTabPlaceholder");
        itemStalactiteDagger = (new ItemStalactiteDagger(stalactiteMat)).setUnlocalizedName("ItemStalactiteDagger").setTextureName("immersivecavegen:stalactite_dagger").setCreativeTab(GamemodeTab.tabCaves);
        GameRegistry.registerItem(itemStalactiteDagger, "ItemStalactiteDagger");
        glowSoup = (new GlowSoup(8)).setUnlocalizedName("glowSoup").setTextureName("immersivecavegen:glow_soup").setCreativeTab(GamemodeTab.tabCaves);
        GameRegistry.registerItem(glowSoup, "ItemGlowSoup");
        itemSpawnSpSm = (new SpawnEggBrownSpiderSmall()).setUnlocalizedName("SpawnSmallBrownSpider").setTextureName("immersivecavegen:spawn_egg_brownspidersmall").setCreativeTab(GamemodeTab.tabCaves);
        GameRegistry.registerItem(itemSpawnSpSm, "SpawnSmallBrownSpider");
        itemSpawnSp = (new SpawnEggBrownSpider()).setUnlocalizedName("SpawnBrownSpider").setTextureName("immersivecavegen:spawn_egg_brownspider").setCreativeTab(GamemodeTab.tabCaves);
        GameRegistry.registerItem(itemSpawnSp, "SpawnBrownSpider");
        itemSpawnSpL = (new SpawnEggBrownSpiderLarge()).setUnlocalizedName("SpawnLargeBrownSpider").setTextureName("immersivecavegen:spawn_egg_brownspiderlarge").setCreativeTab(GamemodeTab.tabCaves);
        GameRegistry.registerItem(itemSpawnSpL, "SpawnLargeBrownSpider");
        itemSpawnGlSl = (new SpawnEggGlowSlime()).setUnlocalizedName("SpawnGlowSlime").setTextureName("immersivecavegen:spawn_egg_glowslime").setCreativeTab(GamemodeTab.tabCaves);
        GameRegistry.registerItem(itemSpawnGlSl, "SpawnGlowSlime");
    }
}
