package com.thecoolguy.rumaan.fileio.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.thecoolguy.rumaan.fileio.data.db.DatabaseContract

/**
 * File Entity object which will be saved in the DB.
 * This FileEntity object will have the upload url from the server as well as all the attributes
 * of the local file.
 *
 *
 * Use this after the File Upload is successful and link is obtained.
 */
@Entity(tableName = DatabaseContract.TABLE_NAME)
class FileEntity {

    @ColumnInfo(name = DatabaseContract.COLUMN_DATE_UPLOAD)
    lateinit var date: String

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = DatabaseContract.COLUMN_DATE_FILE_NAME)
    lateinit var name: String

    @ColumnInfo(name = DatabaseContract.COLUMN_UPLOAD_URL)
    lateinit var url: String

    /* Default Days the link will expire is 14 Days */
    @ColumnInfo(name = DatabaseContract.COLUMN_DAYS_TO_EXPIRE)
    var daysToExpire = 14

    @Ignore
    constructor()

    constructor(name: String,
                url: String, date: String, daysToExpire: Int) {
        this.name = name
        this.url = url
        this.daysToExpire = daysToExpire
        this.date = date
    }

    override fun toString(): String {
        return "FileEntity{" +
                "date='" + date + '\''.toString() +
                ", id=" + id +
                ", name='" + name + '\''.toString() +
                ", url='" + url + '\''.toString() +
                ", daysToExpire=" + daysToExpire +
                '}'.toString()
    }
}
