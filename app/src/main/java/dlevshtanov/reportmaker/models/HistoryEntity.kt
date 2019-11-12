package dlevshtanov.reportmaker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey var date: Long,
    @ColumnInfo(name = "row_title") var rowTitle: String,
    @ColumnInfo(name = "column_title") var columnTitle: String,
    @ColumnInfo(name = "changed_value") var changedValue: Int,
    @ColumnInfo(name = "was_value") var wasValue: Int
)