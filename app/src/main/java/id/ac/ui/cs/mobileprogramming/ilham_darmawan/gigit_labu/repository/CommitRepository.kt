package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Commit
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.GigitLabuDatabase
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.dao.CommitDao

class CommitRepository(application: Application) {
    private val commitDao: CommitDao
    private val listLiveData: LiveData<List<Commit>>

    init {
        val gigitLabuDatabase =
            GigitLabuDatabase.getInstance(
                application.applicationContext
            )!!
        commitDao = gigitLabuDatabase.commitDao()
        listLiveData = commitDao.getAllCommits()
    }

    fun insertAll(listCommit: List<Commit>) {
        insertAllAsyncTask(
            commitDao,
            listCommit
        ).execute()
    }

    fun getAllCommits(): LiveData<List<Commit>> {
        return listLiveData
    }

    fun deleteAllCommits() {
        deleteAllAsyncTask(
            commitDao
        ).execute()

    }

    // handle insertAll and deleteAll in asynctask to prevent UI blocking
    private class insertAllAsyncTask internal constructor(private val mAsyncTaskDao: CommitDao, private val listCommit: List<Commit>) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            mAsyncTaskDao.insertAll(listCommit)
            return null
        }
    }

    private class deleteAllAsyncTask internal constructor(private val mAsyncTaskDao: CommitDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }
}