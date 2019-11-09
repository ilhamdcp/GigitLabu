package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Project
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.repository.ProjectRepository
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.repository.UserRepository

class ProjectViewModel(application: Application): AndroidViewModel(application) {
    private var projectRepository =
        ProjectRepository(
            application
        )
    private var userRepository =
        UserRepository(
            application
        )

    private var mAllProjects = projectRepository.getAllProjects()
    private var mAllUsers = userRepository.getAllUsers()

    fun getAllUsers() : LiveData<List<User>> {
        return mAllUsers
    }

    fun insert(project: Project) {
        projectRepository.insert(project)
    }

    fun getAllProjects() : LiveData<List<Project>> {
        return mAllProjects
    }

    fun deleteAllUsers() {
        return projectRepository.deleteAllProjects()
    }

    fun changeSelectedProject(project: Project) {
        projectRepository.changeSelectedProject(project)
    }

    fun getSelectedProject(): LiveData<List<Project>> {
        return projectRepository.getSelectedProject()
    }

    fun insertAll(listProject: List<Project>) {
        projectRepository.insertAll(listProject)

    }
}