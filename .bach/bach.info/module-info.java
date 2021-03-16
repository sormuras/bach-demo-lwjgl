import com.github.sormuras.bach.ProjectInfo;
import com.github.sormuras.bach.ProjectInfo.Externals;
import com.github.sormuras.bach.ProjectInfo.Externals.Name;

@ProjectInfo(lookupExternals = @Externals(name = Name.LWJGL, version = "3.2.3"))
module bach.info {
  requires com.github.sormuras.bach;
}
