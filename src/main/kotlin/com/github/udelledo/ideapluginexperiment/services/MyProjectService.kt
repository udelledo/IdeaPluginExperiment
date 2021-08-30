package com.github.udelledo.ideapluginexperiment.services

import com.github.udelledo.ideapluginexperiment.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
