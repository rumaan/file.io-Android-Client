package com.thecoolguy.rumaan.fileio.utils;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.thecoolguy.rumaan.fileio.data.models.LocalFile;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Bunch of helper methods here.
 */
public final class Utils {


  private static final String TAG = Utils.class.getSimpleName();

  public static InputStream getInputStream(final Context context, final Uri uri)
      throws FileNotFoundException {
    return context.getContentResolver().openInputStream(uri);
  }


  /**
   * Get the file details -> name, size from the Android Provider Database
   *
   * @param fileUri Requested file URI
   * @param context Context of the requesting activity
   * @return File Details of the given URI and wrap it into a POJO with file stream
   */
  public static LocalFile getLocalFile(@NonNull final Context context, @NonNull final Uri fileUri
  ) throws CursorIndexOutOfBoundsException, FileNotFoundException {
    Cursor cursor = null;
    try {
      cursor = context.getContentResolver()
          .query(fileUri, null, null, null, null);

      if (cursor != null) {
        LocalFile localFile = null;
        while (cursor.moveToNext()) {
          int nameIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
          int sizeIndex = cursor.getColumnIndexOrThrow(OpenableColumns.SIZE);

          String fileName = cursor.getString(nameIndex);
          long fileSize = cursor.getLong(sizeIndex);
          InputStream inputStream = getInputStream(context, fileUri);
          localFile = new LocalFile(fileName, fileSize, fileUri, inputStream);
        }
        return localFile;
      } else {
        throw new CursorIndexOutOfBoundsException("Invalid Cursor position!");
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
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
  public static FileInputStream getFileInputStream(final Uri fileUri, final Context context) {
    ParcelFileDescriptor parcelFileDescriptor;
    try {
      parcelFileDescriptor = context.getContentResolver()
          .openFileDescriptor(fileUri, "r");
      if (parcelFileDescriptor != null) {
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        if (fileDescriptor.valid()) {
          return new FileInputStream(fileDescriptor);
        } else {
          throw new FileNotFoundException();
        }
      } else {
        throw new FileNotFoundException();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      Log.e(TAG, "File Not Found!", e);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Wrapper class for all Android related helper methods.
   */
  public static final class Android {

    public static Intent getChooseFileIntent() {
      return Utils
          .Android.newIntent(Intent.ACTION_OPEN_DOCUMENT, Intent.CATEGORY_OPENABLE, "*/*");
    }

    /* Returns the state of current network info */
    public static boolean isConnectedToNetwork(Context context) {
      ConnectivityManager connectivityManager = (ConnectivityManager) context
          .getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connectivityManager != null) {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
      } else {
        return false;
      }
    }

    /* Returns new intent for the given action and category */
    public static Intent newIntent(String action, String category, String type) {
      Intent intent = new Intent();
      intent.setAction(action);
      intent.setType(type);
      intent.addCategory(category);
      return intent;
    }

    /* Show the fragment with the provided fragment manager */
    public static void showDialogFragment(Fragment fragment, FragmentManager fragmentManager,
        String tag) {
      ((DialogFragment) fragment).show(fragmentManager, tag);
    }

    /* Dismisses the given dialog */
    public static void dismissDialog(Dialog dialog) {
      if (dialog == null) {
        return;
      }
      if (dialog.isShowing()) {
        dialog.dismiss();
      }
    }

    /* Opens App info screen in settings */
    public static void showAppDetailsSettings(Context context) {
      try {
        Intent intent;
        intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
      } catch (ActivityNotFoundException e) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
        context.startActivity(intent);
        e.printStackTrace();
      }
    }

    /* Copy the given text into clipboard */
    public static void copyTextToClipboard(final Context context, final String label,
        final String text) {
      ClipboardManager clipboardManager = (ClipboardManager) context
          .getSystemService(Context.CLIPBOARD_SERVICE);
      ClipData clipData = ClipData.newPlainText(label, text);
      if (clipboardManager != null) {
        clipboardManager.setPrimaryClip(clipData);
      }
    }
  }

  /**
   * Wrapper class for all methods that parse or handle file.io URLS.
   */
  public static final class URLParser {

    public static String getExpireUrl(String daysToExpire) {
      return Constants.BASE_URL + Constants.QUERY + Constants.EXPIRE_PARAM + daysToExpire;
    }

    /* Removes the '/dwnld' from the URL */
    public static String parseEncryptUrl(final String s) {
      int index = s.lastIndexOf(Constants.POSTFIX);
      return s.substring(0, index);
    }

  }

  /**
   * Wrapper class for all methods that parse the JSON.
   */
  public static final class JSONParser {

    /**
     * Parse the JSON received.
     *
     * @param response JSON String.
     * @return Pair object of Received Link and Expiry Days.
     * @deprecated The Response object has been modified into a POJO.
     */
    @Deprecated
    public static ContentValues parseResults(String response) {
      if (response != null) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          String link = jsonObject.getString("link");

          // append POSTFIX key to link
          link += Constants.POSTFIX;

          String expiry = jsonObject.getString("expiry");
          Integer days = getDaysFromExpireString(expiry);
          ContentValues contentValues = new ContentValues();
          contentValues.put("link", link);
          contentValues.put("days", days);
          return contentValues;
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
      return null;
    }

    /**
     * Returns Days from the string.
     * ex: "242 Days" ->  returns 242
     */
    public static Integer getDaysFromExpireString(String expiry) {
      // FIXME: look out for months and years, if you seek to implement them in the future.
      // Split on spaces
      if (expiry == null) {
        throw new RuntimeException("Expiry is null");
      }
      return Integer.parseInt(expiry.split(" ")[0]);
    }
  }

  /**
   * Date functions helper class
   */
  public static final class Date {

    public static final String TIME_STAMP_FORMAT = "dd MMMM, yyyy";

    private static String getTimeStamp() {
      return new SimpleDateFormat(TIME_STAMP_FORMAT, Locale.US).format(new java.util.Date());
    }

    /**
     * @return Current Date as specified in the Date.TIME_STAMP_FORMAT
     */
    public static String getCurrentDate() {
      return Date.getTimeStamp();
    }
  }
}
