package music.com.olaplay.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import music.com.olaplay.R;

/**
 * Created by gpsdesk on 19/12/17.
 */

public class SongPlayService extends Service{
    private final String TAG = "SongPlayService";
    private SimpleExoPlayer player;
    private final IBinder mBinder = new LocalBinder();
    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;
    private NotificationManager mNotificationManager;
    private Handler mServiceHandler;
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = "started_from_notification";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"OnBind");
        stopForeground(true);

        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        stopForeground(true);

        super.onRebind(intent);

    }

    @Override
    public boolean onUnbind(Intent intent) {
//        return super.onUnbind(intent);
        startForeground(NOTIFICATION_ID, getNotification());
        return true;
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, SongPlayService.class);

        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AllSongsActivity.class), 0);
        return new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_launch, getString(R.string.launch_activity),
                        activityPendingIntent)
                .addAction(R.drawable.ic_cancel, getString(R.string.remove_location_updates),
                        servicePendingIntent)
                .setContentText("testing")
                .setContentTitle("From notification")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("text")
                .setWhen(System.currentTimeMillis()).build();
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate");

//        super.onCreate();
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"onStartCommand");

        return START_NOT_STICKY;
    }

    public void requestPlaySong(String url){
        startService(new Intent(getApplicationContext(),SongPlayService.class));
       /* player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                new DefaultLoadControl());


        playerView.setPlayer(player);
        player.setPlayWhenReady(true);

        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);*/
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                DataSourceFactoryHelper.createDataSourceFactory(this, "OlaPlay",null),
                new DefaultExtractorsFactory(),
                null, null);
    }

    public class LocalBinder extends Binder {
        SongPlayService getService() {
            return SongPlayService.this;
        }
    }
}
