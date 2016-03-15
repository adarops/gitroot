package com.ilincar.drivingrecorder2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.ilincar.utils.ExceptionChecker;

/**
 * Created by zoufeng on 3/3/16.
 */
public class ExceptionCheckService extends Service implements ExceptionChecker.OnCollisionListener {

    private ExceptionChecker mChecker;
    private Context mContext;
    private boolean isCollision = false;

    public static final String ACTION_ILINCAR_COLLISION_EXCEPTION = "action.ilincar.collision.exception";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCollision() {
        isCollision = true;
        this.sendBroadcast(new Intent(ACTION_ILINCAR_COLLISION_EXCEPTION));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        mChecker = new ExceptionChecker(mContext);
        mChecker.setOnCollisionListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
