import com.github.sormuras.bach.Bach;
import com.github.sormuras.bach.ToolCall;

class build {
  public static void main(String... args) {
    var bach = Bach.ofSystem(args);
    bach.run("build");

    var main = bach.project().spaces().main();
    var paths = bach.configuration().paths();
    var modulePaths = bach.project().spaces().test().toModulePath(paths).orElseThrow();

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
}
