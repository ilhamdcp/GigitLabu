package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Project

@Dao
interface ProjectDao {
    @Insert
    fun insert(project: Project)

    @Insert(onConflict = IGNORE)
    fun insertAll(project: List<Project>)

    @Query("DELETE FROM projects")
    fun deleteAll()

    @Query("SELECT * FROM projects ORDER BY is_selected DESC" )
    fun getAllProjects() : LiveData<List<Project>>

    @Transaction
    fun changeSelectedProject(id: Int) {
        toggleCurrentSelectedProject()
        updateSelectedProject(id)
    }

    @Query("SELECT * FROM projects WHERE is_selected")
    fun getSelectedProject(): LiveData<List<Project>>

    @Query("UPDATE projects SET is_selected = 1 WHERE id = :id")
    fun updateSelectedProject(id: Int)

    @Query("UPDATE projects SET is_selected = 0 WHERE is_selected")
    fun toggleCurrentSelectedProject()

}