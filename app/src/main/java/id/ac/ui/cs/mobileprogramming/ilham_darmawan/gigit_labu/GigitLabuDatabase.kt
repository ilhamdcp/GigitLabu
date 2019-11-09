package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.dao.CommitDao
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.dao.ProjectDao
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.dao.UserDao
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Commit
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Project
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User


@Database(entities = [User::class, Project::class, Commit::class], version = 1)
abstract class GigitLabuDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun commitDao(): CommitDao

    companion object {
        private var INSTANCE: GigitLabuDatabase? = null

        fun getInstance(context: Context): GigitLabuDatabase? {
            if (INSTANCE == null) {
                synchronized(RoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        GigitLabuDatabase::class.java, "user.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}