package dlevshtanov.reportmaker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TitleEntity(
    @PrimaryKey @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "order_id") var orderId: Int = 0,
    @ColumnInfo(name = "type") var type: Pages
)