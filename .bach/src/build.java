import com.github.sormuras.bach.Bach;
import com.github.sormuras.bach.ExternalModuleLocator;
import com.github.sormuras.bach.ToolCall;
import com.github.sormuras.bach.external.Maven;
import java.util.List;

class build {
  public static void main(String... args) {
    try (var bach = new Bach(args)) {
      var grabber = bach.grabber(new LWJGLModuleLookup("3.3.0"));

      var builder = bach.builder().conventional("com.github.sormuras.bach.lwjgl");

      builder.grab(
          grabber, "org.lwjgl.natives", "org.lwjgl.glfw.natives", "org.lwjgl.opengl.natives");

      builder.compile();

      if (!System.getenv().containsKey("CI")) {
        bach.logCaption("Launch LWJGL-based Application");
        // FXGL calls System.exit(), don't launch in-process: builder.runModule("com...bach.fxgl");
        var modulePaths = List.of(bach.path().workspace("modules"), bach.path().externalModules());
        var java =
            ToolCall.java()
                .with("--module-path", modulePaths)
                .with("--module", "com.github.sormuras.bach.lwjgl");
        bach.run(java);
      }

      bach.logCaption("Link modules into a custom runtime image");
      builder.link(jlink -> jlink.with("--launcher", "bach-lwjgl=com.github.sormuras.bach.lwjgl"));
    }
  }

  /** Maps well-known LWJGL module names to their Maven Central artifacts. */
  static class LWJGLModuleLookup implements ExternalModuleLocator {

    /** @return the classifier determined via the {@code os.name} system property */
    public static String classifier() {
      var os = System.getProperty("os.name").toLowerCase();
      var isWindows = os.contains("win");
      var isMac = os.contains("mac");
      var isLinux = !(isWindows || isMac);
      var arch = System.getProperty("os.arch").toLowerCase();
      var is64 = arch.contains("64");
      if (isLinux) {
        if (arch.startsWith("arm") || arch.startsWith("aarch64")) {
          var arm64 = is64 || arch.startsWith("armv8");
          return arm64 ? "natives-linux-arm64" : "natives-linux-arm32";
        }
        return "natives-linux";
      }
      if (isWindows) {
        return is64 ? "natives-windows" : "natives-windows-x86";
      }
      return "natives-macos";
    }

    private final String version;
    private final String classifier;

    /**
     * Constructs a new LWJGL module lookup with the given version.
     *
     * @param version the version
     */
    public LWJGLModuleLookup(String version) {
      this(version, classifier());
    }

    /**
     * Constructs a new LWJGL module lookup with the given version.
     *
     * @param version the version
     * @param classifier the classifier
     */
    public LWJGLModuleLookup(String version, String classifier) {
      this.version = version;
      this.classifier = classifier;
    }

    @Override
    public String locate(String module) {
      if (!module.startsWith("org.lwjgl")) return null;
      var natives = module.endsWith(".natives");
      var end = natives ? module.length() - 8 : module.length();
      var artifact = "lwjgl" + module.substring(9, end).replace('.', '-');
      return Maven.central("org.lwjgl", artifact, version, natives ? classifier : "");
    }

    @Override
    public String toString() {
      return "org.lwjgl[*] -> LWJGL " + version + '-' + classifier;
    }
  }
}
