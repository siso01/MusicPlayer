package music.com.olaplay.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.List;

import music.com.olaplay.R;
import music.com.olaplay.model.mainApiResponse.AllSongsAndInfo;
import music.com.olaplay.model.mainApiResponse.SongsAndInfo;
import music.com.olaplay.rest.ApiClient;
import music.com.olaplay.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSongsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSongs;
    private SongRowAdaptor songRowAdaptor;
    private ApiInterface apiInterface;
    public SongPlayService songPlayService = null;
    private ArrayList<SongsAndInfo> songsAndInfoList;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);
        recyclerViewSongs = findViewById(R.id.rc_view_songs);
        // init...
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        songsAndInfoList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        recyclerViewSongs.setLayoutManager(mLayoutManger);

        callAllSongsApi();
//        startService(new Intent(this,SongPlayService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("AllSongsActivity","onStart()");
        bindService(new Intent(this, SongPlayService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
        songRowAdaptor = new SongRowAdaptor(this,songsAndInfoList,songPlayService);
        recyclerViewSongs.setAdapter(songRowAdaptor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("AllSongsActivity","onResume()");
        songPlayService.requestPlaySong("");
    }


    private void callAllSongsApi() {
        Call<List<SongsAndInfo>> call = apiInterface.getAllSongs();
        call.enqueue(new Callback<List<SongsAndInfo>>() {
            @Override
            public void onResponse(Call<List<SongsAndInfo>> call, Response<List<SongsAndInfo>> response) {
                if (response.code() == 200){
                    int size = response.body().size();
                    for (int i = 0 ; i < size ; i++){
                        songsAndInfoList.add(response.body().get(i));
                    }
                    songRowAdaptor.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<SongsAndInfo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SongPlayService.LocalBinder binder = (SongPlayService.LocalBinder) iBinder;
            songPlayService = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            songPlayService = null;
            mBound = false;

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
    }
}
