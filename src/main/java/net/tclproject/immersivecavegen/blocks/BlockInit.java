package net.tclproject.immersivecavegen.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.tclproject.immersivecavegen.items.ItemStalactite;
import net.tclproject.immersivecavegen.items.MultiItemBlock;
import net.tclproject.immersivecavegen.misc.GamemodeTab;

public class BlockInit {
  public static Block cavePlantBlock;

  public static Block iceStalactiteBlock;

  public static Block stoneStalactiteBlock;

  public static Block netherStalactiteBlock;

  public static Block deepslateStalactiteBlock;

  public static Block scorchedStone;

  public static Block scorchedLavaStone;

  public static Block sandStalactiteBlock;

  public static Block glowLily;

  public static Block glowLilyBlue;

  public static Block ceilingVine;

  public static Block mushroomBlockBlue;

  public static Block mushroomBlockGreen;

  public static void init() {
    glowLily = GameRegistry.registerBlock(new BlockGlowlily(), "GlowLily").setBlockTextureName("immersivecavegen:glowlily");
    glowLilyBlue = GameRegistry.registerBlock(new BlockGlowlily(), "GlowLilyBlue").setBlockTextureName("immersivecavegen:glowlily2");
    ceilingVine = GameRegistry.registerBlock(new BlockCeilingVine(), "CeilingVine").setBlockName("ceilingVine").setHardness(0.2F).setStepSound(Block.soundTypeGrass).setBlockTextureName("immersivecavegen:greenvine");
    scorchedStone = GameRegistry.registerBlock(new BlockStone(), "ScorchedStone").setBlockName("scorchedStoneBlock").setCreativeTab(GamemodeTab.tabCaves).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setBlockTextureName("immersivecavegen:scorched_stone");
    scorchedLavaStone = GameRegistry.registerBlock(new ScorchedLavaStone(), "ScorchedLavaStone");
    stoneStalactiteBlock = GameRegistry.registerBlock(new BlockStoneStalactite(), ItemStalactite.class, "StoneStalactite");
    netherStalactiteBlock = GameRegistry.registerBlock(new BlockNetherStalactite(), ItemStalactite.class, "NetherStalactite");
    deepslateStalactiteBlock = GameRegistry.registerBlock(new BlockDeepslateStalactite(), ItemStalactite.class, "DeepslateStalactite");
    sandStalactiteBlock = GameRegistry.registerBlock((new BlockBaseStalactite(Item.getItemFromBlock(Blocks.sandstone))).setBlockName("sandStalactiteBlock").setBlockTextureName(":sandDecoration"), ItemStalactite.class, "SandstoneSalactite");
    iceStalactiteBlock = GameRegistry.registerBlock(new BlockIceStalactite(), MultiItemBlock.class, "iceStalactite");
    cavePlantBlock = GameRegistry.registerBlock((new BlockCavePlant()).setLightLevel(6.0F), MultiItemBlock.class, "cavePlant");
    mushroomBlockBlue = GameRegistry.registerBlock(new BlockHugeGlowingMushroom(Material.wood, 0), "glowMushroomBlue").setCreativeTab(GamemodeTab.tabCaves);
    mushroomBlockGreen = GameRegistry.registerBlock(new BlockHugeGlowingMushroom2(Material.wood, 1), "glowMushroomGreen").setCreativeTab(GamemodeTab.tabCaves);
  }
}
