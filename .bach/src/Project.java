import run.bach.workflow.Builder;
import run.bach.workflow.Structure;
import run.bach.workflow.Workflow;

record Project(Workflow workflow) implements Builder {
  static Project ofCurrentWorkingDirectory() {
    return new Project(
        Workflow.ofCurrentWorkingDirectory()
            .withMainSpace(
                main ->
                    main.withModule("demo", "demo/module-info.java")
                        .withLauncher("demo=demo/demo.Main")
                        .with(Structure.Space.Flag.COMPILE_RUNTIME_IMAGE)));
  }

  @Override
  public boolean builderDoesCleanAtTheBeginning() {
    return true;
  }
}
