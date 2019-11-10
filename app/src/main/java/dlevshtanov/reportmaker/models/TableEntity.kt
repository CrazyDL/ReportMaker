package dlevshtanov.reportmaker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TableEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "row_title") var rowTitle: String,
    @ColumnInfo(name = "column_title") var columnTitle: String,
    @ColumnInfo(name = "value") var value: Int
)