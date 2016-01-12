package undot.jewguard;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by mymac on 12/01/16.
 */
class PInfo {
  public String appname = "";
    public String pname = "";
    public String versionName = "";
    public int versionCode = 0;
    public Drawable icon;
    public void prettyPrint() {
        Log.v("packageinfo",appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
    }
}
