package org.udelledo.intellij.idea.service;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class TerminalService {

    private final Project project;

    public TerminalService(Project project) {
        this.project = project;
    }

    @NotNull
    private ShellTerminalWidget getTerminal() {
        return Optional.ofNullable(project.getService(ToolWindowManager.class).getToolWindow("Terminal"))
                .flatMap(toolWindow -> Arrays
                        .stream(toolWindow.getContentManager().getContents())
                        .filter(content -> "NSHints".equals(content.getTabName()))
                        .findFirst())
                .map(tab -> (ShellTerminalWidget) tab.getUserData(Objects.requireNonNull(Key.findKeyByName("TerminalWidget"))))
                .orElseGet(() ->
                        project.getService(TerminalView.class)
                                .createLocalShellWidget(project.getBasePath(), "NSHints"));
    }

    public void runCommand(String command) {
        try {
            getTerminal().executeCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
