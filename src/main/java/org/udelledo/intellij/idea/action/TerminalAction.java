package org.udelledo.intellij.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.udelledo.intellij.idea.service.TerminalService;

import java.util.Objects;

public class TerminalAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
        super.update(e);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Objects.requireNonNull(e.getProject())
                .getService(TerminalService.class)
                .runCommand("echo evaluated");
    }


}
