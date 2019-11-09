package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Commit

@Dao
interface CommitDao {
    @Insert
    fun insert(commit: Commit)

    @Insert(onConflict = IGNORE)
    fun insertAll(commit: List<Commit>)

    @Query("SELECT * FROM commits ORDER BY commits.created_at DESC")
    fun getAllCommits(): LiveData<List<Commit>>

    @Query("DELETE FROM commits")
    fun deleteAll()
}