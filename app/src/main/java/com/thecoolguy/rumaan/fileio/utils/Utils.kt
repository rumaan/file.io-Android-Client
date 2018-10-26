package com.thecoolguy.rumaan.fileio.utils

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.net.ConnectivityManager
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.util.Log
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import org.json.JSONException
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Bunch of helper methods here.
 */
object Utils {

  /* TODO: Do manual kotlin conversion in this class */

  private val TAG = Utils::class.java.simpleName

  @Throws(FileNotFoundException::class)
  private fun getInputStream(
    context: Context,
    uri: Uri
  ): InputStream? {
    return context.contentResolver.openInputStream(uri)
  }

  /**
   * Get the file details -> name, size from the Android Provider Database
   *
   * @param fileUri Requested file URI
   * @param context Context of the requesting activity
   * @return File Details of the given URI and wrap it into a POJO with file stream
   */
  @Throws(CursorIndexOutOfBoundsException::class, FileNotFoundException::class)
  fun getLocalFile(
    context: Context,
    fileUri: Uri
  ): LocalFile? {
    var cursor: Cursor? = null
    try {
      cursor = context.contentResolver
          .query(fileUri, null, null, null, null)

      if (cursor != null) {
        var localFile: LocalFile? = null
        while (cursor.moveToNext()) {
          val nameIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
          val sizeIndex = cursor.getColumnIndexOrThrow(OpenableColumns.SIZE)

          val fileName = cursor.getString(nameIndex)
          val fileSize = cursor.getLong(sizeIndex)
          val inputStream = getInputStream(context, fileUri)
          localFile = LocalFile(fileName, fileSize, fileUri, inputStream!!)
        }
        return localFile
      } else {
        throw CursorIndexOutOfBoundsException("Invalid Cursor position!")
      }
    } finally {
      cursor?.close()
    }
  }

  /**
   * Open the file from the storage in read mode and return the
   * InputStream.
   *
   * @param context Context of the calling activity
   * @param fileUri Uri of the requested file
   * @return Return the Stream of the requested file
   */
  fun getFileInputStream(
    fileUri: Uri,
    context: Context
  ): FileInputStream? {
    val parcelFileDescriptor: ParcelFileDescriptor?
    try {
      parcelFileDescriptor = context.contentResolver
          .openFileDescriptor(fileUri, "r")
      if (parcelFileDescriptor != null) {
        val fileDescriptor = parcelFileDescriptor.fileDescriptor
        return if (fileDescriptor.valid()) {
          FileInputStream(fileDescriptor)
        } else {
          throw FileNotFoundException()
        }
      } else {
        throw FileNotFoundException()
      }
    } catch (e: FileNotFoundException) {
      e.printStackTrace()
      Log.e(TAG, "File Not Found!", e)
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return null
  }

  /**
   * Wrapper class for all Android related helper methods.
   */
  object Android {

    val chooseFileIntent: Intent
      get() = Utils
          .Android.newIntent(Intent.ACTION_OPEN_DOCUMENT, Intent.CATEGORY_OPENABLE, "*/*")

    /* Returns the state of current network info */
    fun isConnectedToNetwork(context: Context): Boolean {
      val connectivityManager = context
          .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
      return if (connectivityManager != null) {
        val networkInfo = connectivityManager.activeNetworkInfo
        networkInfo != null && networkInfo.isConnected
      } else {
        false
      }
    }

    /* Returns new intent for the given action and category */
    private fun newIntent(
      action: String,
      category: String,
      type: String
    ): Intent {
      val intent = Intent()
      intent.action = action
      intent.type = type
      intent.addCategory(category)
      return intent
    }

    /* Show the fragment with the provided fragment manager */
    fun showDialogFragment(
            fragment: androidx.fragment.app.Fragment,
            fragmentManager: androidx.fragment.app.FragmentManager,
            tag: String
    ) {
      (fragment as androidx.fragment.app.DialogFragment).show(fragmentManager, tag)
    }

    /* Dismisses the given dialog */
    fun dismissDialog(dialog: Dialog?) {
      dialog?.takeIf {
        it.isShowing
      }
          ?.apply {
            dismiss()
          }
    }

    /* Opens App info screen in settings */
    fun showAppDetailsSettings(context: Context) {
      try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivity(intent)
      } catch (e: ActivityNotFoundException) {
        val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
        context.startActivity(intent)
        e.printStackTrace()
      }

    }

    /* Copy the given text into clipboard */
    fun copyTextToClipboard(
      context: Context?,
      label: String,
      text: String
    ) {
      val clipboardManager = context
          ?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
      val clipData = ClipData.newPlainText(label, text)
      clipboardManager?.primaryClip = clipData
    }
  }

  /**
   * Wrapper class for all methods that parse or handle file.io URLS.
   */
  object URLParser {

    fun getExpireUrl(daysToExpire: String): String {
      return BASE_URL + QUERY + EXPIRE_PARAM + daysToExpire
    }

    /* Removes the '/dwnld' from the URL */
    fun parseEncryptUrl(s: String): String {
      val index = s.lastIndexOf(POSTFIX)
      return s.substring(0, index)
    }

  }

  /**
   * Wrapper class for all methods that parse the JSON.
   */
  object JSONParser {

    /**
     * Parse the JSON received.
     *
     * @param response JSON String.
     * @return Pair object of Received Link and Expiry Days.
     */
    @Deprecated("The Response object has been modified into a POJO.")
    fun parseResults(response: String?): ContentValues? {
      if (response != null) {
        try {
          val jsonObject = JSONObject(response)
          var link = jsonObject.getString("link")

          // append POSTFIX key to link
          link += POSTFIX

          val expiry = jsonObject.getString("expiry")
          val days = getDaysFromExpireString(expiry)
          val contentValues = ContentValues()
          contentValues.put("link", link)
          contentValues.put("days", days)
          return contentValues
        } catch (e: JSONException) {
          e.printStackTrace()
        }

      }
      return null
    }

    /**
     * Returns Days from the string.
     * ex: "242 Days" ->  returns 242
     */
    fun getDaysFromExpireString(expiry: String?): Int {
      // FIXME: look out for months and years, if you seek to implement them in the future.
      // Split on spaces
      if (expiry == null) {
        throw RuntimeException("Expiry is null")
      }
      return Integer.parseInt(
          expiry.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
      )
    }
  }

  /**
   * Date functions helper class
   */
  object Date {

    const val TIME_STAMP_FORMAT = "dd MMMM, yyyy"

    private val timeStamp: String
      get() = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.US).format(java.util.Date())

    /**
     * @return Current Date as specified in the Date.TIME_STAMP_FORMAT
     */
    val currentDate: String
      get() = Date.timeStamp
  }
}
