package undot.jewguard;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Browser;
import android.util.Log;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    ActivityManager mActivityManager;
    RecentUseComparator mRecentComp;
    BrowserObserver hObserver;
    MyTimerTask myTimerTask;
    Timer timer;
    List<String> blockedapps;
    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        // Code to execute when the service is first created
     //  mActivityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        mRecentComp = new RecentUseComparator();
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(myTimerTask, 0, 1000);
        TinyDB tinyDB = new TinyDB(getBaseContext());
        blockedapps=tinyDB.getListString("chk");

            hObserver = new BrowserObserver(new Handler(), getBaseContext());
            getApplicationContext().getContentResolver().registerContentObserver(Uri.parse("content://com.android.chrome.browser/bookmarks"), true, hObserver);

    }
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
          /*  List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);

            ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
            String activityOnTop=ar.topActivity.getClassName();
            Log.d("activity",activityOnTop);*/
            String top = getTopPackage();
            Log.d("activity", top);
            if(blockedapps!=null){
            if(blockedapps.contains(top))
            {
                Intent lockIntent = new Intent(getBaseContext(), LockScreen.class);
                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(lockIntent);
            }
            }
            else
            {
                TinyDB tinyDB = new TinyDB(getBaseContext());
                blockedapps=tinyDB.getListString("chk");
            }
        }

    }
    String getTopPackage(){
        long ts = System.currentTimeMillis();
        UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService("usagestats");
        List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts-1000, ts);
        if (usageStats == null || usageStats.size() == 0) {
            return "no package";
        }
        Collections.sort(usageStats,mRecentComp);
        return usageStats.get(0).getPackageName();
    }
    static class RecentUseComparator implements Comparator<UsageStats> {

        @Override
        public int compare(UsageStats lhs, UsageStats rhs) {
            return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        getApplicationContext().getContentResolver().unregisterContentObserver(hObserver);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
