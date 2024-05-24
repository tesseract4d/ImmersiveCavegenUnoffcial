package net.tclproject.immersivecavegen.fixes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import net.tclproject.mysteriumlib.asm.common.CustomLoadingPlugin;
import net.tclproject.mysteriumlib.asm.common.FirstClassTransformer;

public class MysteriumPatchesFixLoaderB extends CustomLoadingPlugin {
  public String[] getASMTransformerClass() {
      System.out.println(114514);
    return new String[] { FirstClassTransformer.class.getName() };
  }

  public void registerFixes() {
    ArrayList<String> lines2 = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(new File("."), "config/immersivecavegen.cfg")));
      String line = br.readLine();
      while (line != null) {
        lines2.add(line);
        line = br.readLine();
      }
      br.close();
    } catch (Exception exception) {}
    boolean oldGen = false, mineshafts = true, sand = true, caverns = true, popChanges = true;
    for (String str : lines2) {
      if (str.contains("\"Old Cave Gen\"=true"))
        oldGen = true;
      if (str.contains("\"Enable Better Mineshafts\"=false"))
        mineshafts = false;
      if (str.contains("\"Enable Better Sand Generation\"=false"))
        sand = false;
      if (str.contains("\"Enable Caverns Replacer\"=false"))
        caverns = false;
      if (str.contains("\"Enable World Population Changes\"=false"))
        popChanges = false;
    }
    if (!oldGen) {
      if (mineshafts)
        registerClassWithFixes("net.tclproject.immersivecavegen.fixes.MysteriumPatchesFixesB");
      if (sand)
        registerClassWithFixes("net.tclproject.immersivecavegen.fixes.MysteriumPatchesFixesSand");
      if (caverns)
        registerClassWithFixes("net.tclproject.immersivecavegen.fixes.MysteriumPatchesFixesRavine");
      if (popChanges)
        registerClassWithFixes("net.tclproject.immersivecavegen.fixes.MysteriumPatchesFixesPop");
      registerClassWithFixes("net.tclproject.immersivecavegen.fixes.MysteriumPatchesFixesCave");
    } else if (oldGen) {
      registerClassWithFixes("net.tclproject.immersivecavegen.fixes.MysteriumPatchesFixesOld");
    }
  }
}
