package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Projects")
@ForeignKey(
    entity = User::class,
    parentColumns = ["username"],
    childColumns = ["username"],
    onDelete = CASCADE
)
data class Project(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "is_selected") val is_selected: Boolean
)