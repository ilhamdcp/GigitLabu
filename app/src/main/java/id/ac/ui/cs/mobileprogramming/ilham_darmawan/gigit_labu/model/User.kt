package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(@PrimaryKey @ColumnInfo(name = "username") val username: String,
                @ColumnInfo(name = "personal_access_token") var pat: String,
                @ColumnInfo(name = "avatar") val avatar: String?
)