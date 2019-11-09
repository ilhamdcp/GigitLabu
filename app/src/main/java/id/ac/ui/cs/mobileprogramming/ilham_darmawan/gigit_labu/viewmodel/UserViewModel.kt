package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var mRepository =
        UserRepository(
            application
        )

    private var mAllUsers = mRepository.getAllUsers()

    fun insert(user: User) {
        mRepository.insert(user)
    }

    fun getAllUsers() : LiveData<List<User>> {
        return mAllUsers
    }

    fun deleteAllUsers() {
        return mRepository.deleteAllUsers()
    }

    fun updateAvatar(user: User, avatar: String) {
        mRepository.updateAvatar(user, avatar)
    }

}