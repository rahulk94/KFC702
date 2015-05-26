package com.getterekt.kentucy_fried_cecurity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by devon on 26/05/15.
 */
public class Logcat {
    private static final long CAT_DELAY = 1;

    private Level mLevel = null;
    private String mFilter = null;
    private Pattern mFilterPattern = null;
    private boolean mRunning = false;
    private BufferedReader mReader = null;
    private boolean mIsFilterPattern;
    private Handler mHandler;
    private Buffer mBuffer;
    private Process logcatProc;
    private Context mContext;
    private ArrayList<String> mLogCache = new ArrayList<String>();
    private boolean mPlay = true;
    private long lastCat = -1;
    private Runnable catRunner = new Runnable() {

        @Override
        public void run() {
            if (!mPlay) {
                return;
            }
            long now = System.currentTimeMillis();
            if (now < lastCat + CAT_DELAY) {
                return;
            }
            lastCat = now;
            cat();
        }
    };
    private ScheduledExecutorService EX;

    Format mFormat;

    public Logcat(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;

        Prefs prefs = new Prefs(mContext);

        mLevel = prefs.getLevel();
        mIsFilterPattern = prefs.isFilterPattern();
        mFilter = prefs.getFilter();
        mFilterPattern = prefs.getFilterPattern();
        mFormat = prefs.getFormat();
        mBuffer = prefs.getBuffer();
    }

    public void start() {
         Log.v("CS702", "Devon Ahmu started log cat");
        stop();

        mRunning = true;

        EX = Executors.newScheduledThreadPool(1);
        EX.scheduleAtFixedRate(catRunner, CAT_DELAY, CAT_DELAY,
                TimeUnit.SECONDS);

        try {
            // RAHUL ALERT -- dont know what we are doing. help! changed int to get rid of error
            Log.v("CS702", "Before message obtain");
            Message m = Message.obtain(mHandler, 2);
            mHandler.sendMessage(m);
            Log.v("CS702", "After message obtain");
            List<String> progs = new ArrayList<String>();

            progs.add("logcat");
            progs.add("-v");
            progs.add(mFormat.getValue());
            if (mBuffer != Buffer.MAIN) {
                progs.add("-b");
                progs.add(mBuffer.getValue());
            }
            progs.add("*:" + mLevel);

            logcatProc = Runtime.getRuntime()
                    .exec(progs.toArray(new String[0]));

            mReader = new BufferedReader(new InputStreamReader(
                    logcatProc.getInputStream()), 1024);

            String line;

            while (mRunning && (line = mReader.readLine()) != null) {
                Log.v("CS702","line ="+line);
                if (!mRunning) {
                    break;
                }
                if (line.length() == 0) {
                    Log.v("CS702", "1");
                    continue;
                }
                if (mIsFilterPattern) {
                    if (mFilterPattern != null
                            && !mFilterPattern.matcher(line).find()) {

                        Log.v("CS702", "2");
                        continue;
                    }
                } else {
                    if (mFilter != null
                            && !line.toLowerCase().contains(
                            mFilter.toLowerCase())) {

                        Log.v("CS702", "3");
                        continue;
                    }
                }
                synchronized (mLogCache) {

//                    Log.v("CS702", "4");
                    mLogCache.add(line);
                }
            }
            Log.v("CS702", "Out of the while loop");
        } catch (IOException e) {
            Log.e("alogcat", "error reading log", e);
            return;
        } finally {
            // Log.d("alogcat", "stopped");

            if (logcatProc != null) {
                logcatProc.destroy();
                logcatProc = null;
            }
            if (mReader != null) {
                try {
                    mReader.close();
                    mReader = null;
                } catch (IOException e) {
                    Log.e("alogcat", "error closing stream", e);
                }
            }
        }
        Log.v("CS702", "Log start() finished");
    }

    private void cat() {
        Message m;

        if (mLogCache.size() > 0) {
            synchronized (mLogCache) {
                if (mLogCache.size() > 0) {
                    // RAHUL ALERT -- dont know what we are doing. help! changed int to get rid of error
                    m = Message.obtain(mHandler, 1);
                    m.obj = mLogCache.clone();
                    mLogCache.clear();
                    mHandler.sendMessage(m);
                }
            }
        }
    }

    public void stop() {
         Log.v("CS702", "Devon stopping the logcat...");
        mRunning = false;

        if (EX != null && !EX.isShutdown()) {
            EX.shutdown();
            EX = null;
        }
        Log.v("CS702", "Devons stopped the logcat now");
    }

    public boolean isRunning() {
        return mRunning;
    }

    public boolean isPlay() {
        return mPlay;
    }

    public void setPlay(boolean play) {
        mPlay = play;
    }

}