package dlevshtanov.reportmaker.models


enum class Pages(val position: Int) {
    ROW(0),
    COLUMN(1),
    TABLE(2),
    UNKNOWN(-1);

    companion object {

        @JvmStatic
        fun getPageByPosition(position: Int): Pages {
            for (value in values()) {
                if (value.position == position) {
                    return value
                }
            }
            return UNKNOWN
        }
    }
}