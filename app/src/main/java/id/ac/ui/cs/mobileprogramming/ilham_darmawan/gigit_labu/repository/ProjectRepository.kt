package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.GigitLabuDatabase
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Project
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.dao.ProjectDao

class ProjectRepository(application: Application) {
    private val projectDao: ProjectDao
    private val listLiveData: LiveData<List<Project>>

    init {
        val gigitLabuDatabase =
            GigitLabuDatabase.getInstance(
                application.applicationContext
            )!!
        projectDao = gigitLabuDatabase.projectDao()
        listLiveData = projectDao.getAllProjects()
    }

    fun getAllProjects(): LiveData<List<Project>> {
        return listLiveData
    }

    fun getSelectedProject(): LiveData<List<Project>> {
        return projectDao.getSelectedProject()
    }

    fun insert(project: Project) {
        insertAsyncTask(
            projectDao
        ).execute(project)
    }

    fun insertAll(projectList: List<Project>) {
        insertAllAsyncTask(
            projectDao,
            projectList
        ).execute()
    }

    fun changeSelectedProject(project: Project) {
        updateSelectedProjectAsyncTask(
            projectDao
        ).execute(project)
    }

    fun deleteAllProjects() {
        deleteAllAsyncTask(
            projectDao
        ).execute()
    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: ProjectDao) :
        AsyncTask<Project, Void, Void>() {

        override fun doInBackground(vararg params: Project): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class insertAllAsyncTask internal constructor(
        private val mAsyncTaskDao: ProjectDao,
        private val listProject: List<Project>
    ) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            mAsyncTaskDao.insertAll(listProject)
            return null
        }
    }

    private class updateSelectedProjectAsyncTask internal constructor(private val mAsyncTaskDao: ProjectDao) :
        AsyncTask<Project, Void, Void>() {

        override fun doInBackground(vararg params: Project): Void? {
            mAsyncTaskDao.changeSelectedProject(params[0].id)
            return null
        }
    }

    private class deleteAllAsyncTask internal constructor(private val mAsyncTaskDao: ProjectDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }
}