package build;

import com.github.sormuras.bach.project.ModuleLookup;
import com.github.sormuras.bach.project.ModuleLookups;
import java.util.Optional;

public class ExternalModules implements ModuleLookup {

  private final ModuleLookup lwjgl = new ModuleLookups.LWJGL_3_2_3();

  public ExternalModules() {}

  @Override
  public Optional<String> lookup(String module) {
    return lwjgl.lookup(module);
  }
}
