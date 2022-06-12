import com.github.sormuras.bach.Bach;
import com.github.sormuras.bach.ToolCall;
import com.github.sormuras.bach.project.ExternalModuleLocator;
import java.util.StringJoiner;

class build {
  public static void main(String... args) {
    var initial = Bach.ofSystem(args);
    var project = initial.project().with(new LWJGLModuleLookup("3.3.1"));
    var bach = new Bach(initial.configuration(), project);
    bach.run("build");

    var main = bach.project().spaces().main();
    var paths = bach.configuration().paths();
    var modulePaths = project.spaces().test().toModulePath(paths).orElseThrow();

    if (!System.getenv().containsKey("CI")) {
      bach.run(ToolCall.of("java")
          .with("--module-path", modulePaths)
          .with("--module", main.modules().list().get(0).name()));
    }

    var image = paths.out(main.name(), "image");
    bach.run("tree", "--mode", "CLEAN", image);
    bach.run(
        ToolCall.of("jlink")
            .with("--output", image)
            .with("--launcher", "bach-lwjgl=com.github.sormuras.bach.lwjgl")
            .with("--add-modules", main.modules().names(","))
            .with("--module-path", modulePaths));
  }

  /** Maps well-known LWJGL module names to their Maven Central artifacts. */
  static class LWJGLModuleLookup implements ExternalModuleLocator {

    /**
     * @return the classifier determined via the {@code os.name} system property
     */
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
      var location = new StringJoiner("/");
      location.add("https://repo.maven.apache.org/maven2");
      location.add("org/lwjgl");
      location.add(artifact);
      location.add(version);
      location.add(artifact + "-" + version + (natives ? "-" + classifier : "") + ".jar#SIZE=ANY");
      return location.toString();
    }

    @Override
    public String toString() {
      return "org.lwjgl[*] -> LWJGL " + version + '-' + classifier;
    }
  }
}
