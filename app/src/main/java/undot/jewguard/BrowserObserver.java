package undot.jewguard;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Browser;
import android.util.Log;

public class BrowserObserver extends ContentObserver {
    private static String lastVisitedURL = "";
    private static String lastVisitedWebsite = "";
    Context mContext;
    //Query values:
    final String[] projection = new String[] { Browser.BookmarkColumns.URL };  // URLs
    final String selection = Browser.BookmarkColumns.BOOKMARK + " = 0";  // history item
    final String sortOrder = Browser.BookmarkColumns.DATE;  // the date the item was last visited


    public BrowserObserver(Handler handler,Context context) {
        super(handler);
        mContext=context;

    }


    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }


    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange);

        //Retrieve all the visited URLs:
        final Cursor cursor = mContext.getContentResolver().query(Uri.parse("content://com.android.chrome.browser/bookmarks"), projection, selection, null, sortOrder);

        //Retrieve the last URL:
        cursor.moveToLast();
        final String url = cursor.getString(cursor.getColumnIndex(projection[0]));

        //Close the cursor:
        cursor.close();
        Log.d("History", "URL Visited: " + url + "\n");

        if ( !url.equals(lastVisitedURL) ) {  // to avoid information retrieval and/or refreshing...
            lastVisitedURL = url;

            //Debug:
        }
    }
}