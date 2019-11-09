package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("SELECT * FROM users ORDER BY username ASC" )
    fun getAllUsers() : LiveData<List<User>>

    @Query("UPDATE users SET personal_access_token = :pat WHERE username = :username")
    fun changePat(username: String, pat: String)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    fun getUser(username: String) : LiveData<User>

    @Query("UPDATE users SET avatar = :avatar WHERE username = :username")
    fun updateAvatar(username: String, avatar: String)
}