import com.github.sormuras.bach.ProjectInfo;
import com.github.sormuras.bach.ProjectInfo.Library;
import com.github.sormuras.bach.ProjectInfo.Library.Link;
import com.github.sormuras.bach.ProjectInfo.Main;
import com.github.sormuras.bach.ProjectInfo.Test;
import com.github.sormuras.bach.ProjectInfo.Tweak;

@ProjectInfo(
    name = "bach-lwjgl",
    version = "1",
    library =
        @Library(
            links = {
              @Link(
                  module = "org.lwjgl",
                  target = "org.lwjgl:lwjgl:3.2.3"),
              @Link(
                  module = "org.lwjgl.natives",
                  target = "org.lwjgl:lwjgl:3.2.3:${LWJGL-NATIVES}"),
              @Link(
                  module = "org.lwjgl.glfw",
                  target = "org.lwjgl:lwjgl-glfw:3.2.3"),
              @Link(
                  module = "org.lwjgl.glfw.natives",
                  target = "org.lwjgl:lwjgl-glfw:3.2.3:${LWJGL-NATIVES}"),
              @Link(
                  module = "org.lwjgl.opengl",
                  target = "org.lwjgl:lwjgl-opengl:3.2.3"),
              @Link(
                  module = "org.lwjgl.opengl.natives",
                  target = "org.lwjgl:lwjgl-opengl:3.2.3:${LWJGL-NATIVES}")
            }),
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
