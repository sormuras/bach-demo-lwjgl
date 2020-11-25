import com.github.sormuras.bach.ProjectInfo;
import com.github.sormuras.bach.ProjectInfo.Library;
import com.github.sormuras.bach.ProjectInfo.Library.Link;
import com.github.sormuras.bach.ProjectInfo.Library.Searcher;
import com.github.sormuras.bach.ProjectInfo.Main;
import com.github.sormuras.bach.ProjectInfo.Test;
import com.github.sormuras.bach.ProjectInfo.Tweak;
import com.github.sormuras.bach.module.ModuleSearcher.LWJGLSearcher;

@ProjectInfo(
    name = "bach-lwjgl",
    version = "1",
    library = @Library(searchers = {@Searcher(with = LWJGLSearcher.class, version = "3.2.3")}),
    main =
        @Main(
            generateCustomRuntimeImage = true,
            tweaks = {
              @Tweak(
                  tool = "jar(com.github.sormuras.bach.javafx)",
                  args = {"--main-class", "com.github.sormuras.bach.javafx.Main"}),
              @Tweak(
                  tool = "jlink",
                  args = {
                    "--launcher",
                    "bach-lwjgl=com.github.sormuras.bach.lwjgl/com.github.sormuras.bach.lwjgl.Main",
                    "--no-header-files",
                    "--no-man-pages",
                    "--strip-debug"
                  }),
            }))
module build {
  requires com.github.sormuras.bach;
}
