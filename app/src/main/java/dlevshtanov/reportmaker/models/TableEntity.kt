package dlevshtanov.reportmaker.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["row_title", "column_title"])
data class TableEntity(
    @ColumnInfo(name = "row_title") var rowTitle: String,
    @ColumnInfo(name = "column_title") var columnTitle: String,
    @ColumnInfo(name = "value") var value: Int
)