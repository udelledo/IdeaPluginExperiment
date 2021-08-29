package org.udelledo.intellij.idea.service

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.wm.ToolWindowManager
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.jetbrains.plugins.terminal.ShellTerminalWidget
import org.jetbrains.plugins.terminal.TerminalView
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Paths

@ExtendWith(MockKExtension::class)
internal class TerminalServiceTest {

    @MockK
    lateinit var project: Project

    @MockK
    lateinit var shell: ShellTerminalWidget

    @BeforeEach
    internal fun setUp() {
        with(shell) {
            justRun { executeCommand(any()) }
        }

    }

    @Test
    fun `when terminal is available it is reused to run a command`() {
        mockkStatic(com.intellij.openapi.util.Key::class) {
            every { Key.findKeyByName(any()) } returns mockk {}
            every { project.getService(ToolWindowManager::class.java) } returns mockk {
                every { getToolWindow("Terminal") } returns mockk {
                    every { contentManager } returns mockk {
                        every { contents } returns arrayOf(mockk {
                            every { tabName } returns "NSHints"
                            every { getUserData<ShellTerminalWidget>(any()) } returns shell
                        })
                    }
                }
            }
            val testSubject = TerminalService(project)
            testSubject.runCommand("test command")
            verify { shell.executeCommand(any()) }

        }
    }

    @Test
    fun `when terminal is not available it is initialized to run a command`() {
        every { project.basePath } returns Paths.get(".").toString()
        every { project.getService(ToolWindowManager::class.java) } returns mockk {
            every { getToolWindow("Terminal") } returns mockk {
                every { contentManager } returns mockk {
                    every { contents } returns arrayOf(mockk {
                        every { tabName } returns "Local"
                        every { getUserData<ShellTerminalWidget>(any()) } returns shell
                    })
                    every { project.getService(TerminalView::class.java) } returns mockk {
                        every { createLocalShellWidget(any(), any()) } returns shell
                    }
                }
            }
        }
        val testSubject = TerminalService(project)
        testSubject.runCommand("test command")
        verify { shell.executeCommand(any()) }

    }
}
