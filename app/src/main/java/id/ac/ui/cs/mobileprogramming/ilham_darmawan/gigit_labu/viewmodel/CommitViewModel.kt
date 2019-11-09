package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Commit
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Project
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.repository.CommitRepository
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.repository.ProjectRepository
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.repository.UserRepository

class CommitViewModel(application: Application): AndroidViewModel(application) {
    private var commitRepository =
        CommitRepository(
            application
        )
    private var projectRepository =
        ProjectRepository(
            application
        )
    private var userRepository =
        UserRepository(
            application
        )

    private var mAllCommits = commitRepository.getAllCommits()
    private var mAllProjects = projectRepository.getAllProjects()
    private var mAllUsers = userRepository.getAllUsers()

    fun insertAll(listCommit: List<Commit>) {
        commitRepository.insertAll(listCommit)
    }

    fun getAllCommits(): LiveData<List<Commit>> {
        return mAllCommits
    }

    fun getSelectedProject(): LiveData<List<Project>> {
        return projectRepository.getSelectedProject()
    }

    fun getAllUsers(): LiveData<List<User>> {
        return mAllUsers
    }
    fun deleteAllCommits() {
        commitRepository.deleteAllCommits()
    }


}