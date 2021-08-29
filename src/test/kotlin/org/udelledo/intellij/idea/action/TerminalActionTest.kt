package org.udelledo.intellij.idea.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.udelledo.intellij.idea.service.TerminalService

@ExtendWith(MockKExtension::class)
internal class TerminalActionTest {

    @MockK
    lateinit var project: Project
    private val anActionEvent = mockk<AnActionEvent>()

    @BeforeEach
    internal fun setUp() {
        every { anActionEvent.project } returns project
    }

    @Test
    fun update() {
        every { anActionEvent.presentation } returns mockk {
            justRun { isEnabled = any() }
        }
        val testSubject = TerminalAction()
        testSubject.update(anActionEvent)
    }

    @Test
    fun actionPerformed() {
        every { project.getService(TerminalService::class.java) } returns mockk{
            justRun { runCommand(any()) }
        }
        val testSubject = TerminalAction()
        testSubject.actionPerformed(anActionEvent)
    }
}