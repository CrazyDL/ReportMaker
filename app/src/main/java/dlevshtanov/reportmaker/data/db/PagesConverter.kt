package dlevshtanov.reportmaker.data.db

import androidx.room.TypeConverter
import dlevshtanov.reportmaker.models.Pages


class PagesConverter {
    companion object {

        @JvmStatic
        @TypeConverter
        fun fromIntToPage(value: Int?): Pages {
            return if (value == null) Pages.UNKNOWN else Pages.getPageByPosition(value)
        }

        @JvmStatic
        @TypeConverter
        fun fromPageToInt(page: Pages): Int {
            return page.position
        }
    }
}