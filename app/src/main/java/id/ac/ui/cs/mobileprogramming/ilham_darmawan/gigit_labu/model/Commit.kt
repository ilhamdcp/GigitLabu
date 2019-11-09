package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Commits")
@ForeignKey(
    entity = Project::class,
    parentColumns = ["id"],
    childColumns = ["project_id"],
    onDelete = CASCADE
)
data class Commit(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "project_id") val projectId: Int,
    @ColumnInfo(name = "created_by") val createdBy: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "created_at") val createdAt: String
)