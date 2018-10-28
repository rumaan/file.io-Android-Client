package com.thecoolguy.rumaan.fileio.data.db

/**
 * Database Details Class
 */

object DatabaseContract {
    const val TABLE_NAME = "upload_history"
    const val COLUMN_DATE_UPLOAD = "upload_date"
    const val COLUMN_DATE_FILE_NAME = "file_name"
    const val COLUMN_UPLOAD_URL = "url"
    const val COLUMN_DAYS_TO_EXPIRE = "days_to_expire"
}
