package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.GigitLabuDatabase
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.dao.UserDao

class UserRepository(application: Application) {
    private val userDao: UserDao
    private val listLiveData: LiveData<List<User>>

    init {
        val gigitLabuDatabase: GigitLabuDatabase = GigitLabuDatabase.getInstance(
            application.applicationContext
        )!!
        userDao = gigitLabuDatabase.userDao()
        listLiveData = userDao.getAllUsers()
    }

    fun getAllUsers(): LiveData<List<User>> {
        return listLiveData
    }

    fun insert(user: User) {
        insertAsyncTask(
            userDao
        ).execute(user)
    }

    fun deleteAllUsers() {
        deleteAllAsyncTask(
            userDao
        ).execute()
    }

    fun updateAvatar(user: User, avatar: String) {
        updateAvatarAsyncTask(
            userDao,
            avatar
        ).execute(user)

    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: UserDao) : AsyncTask<User, Void, Void>() {

        override fun doInBackground(vararg params: User): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class deleteAllAsyncTask internal constructor(private val mAsyncTaskDao: UserDao) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }

    private class updateAvatarAsyncTask internal constructor(private val mAsyncTaskDao: UserDao, private val avatar: String) : AsyncTask<User, Void, Void>() {

        override fun doInBackground(vararg params: User): Void? {
            mAsyncTaskDao.updateAvatar(params[0].username, avatar)
            return null
        }
    }

}