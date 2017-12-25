package music.com.olaplay.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

import music.com.olaplay.R;
import music.com.olaplay.model.mainApiResponse.SongsAndInfo;

/**
 * Created by gpsdesk on 17/12/17.
 */

public class SongRowAdaptor extends RecyclerView.Adapter<SongRowAdaptor.MySongViewHolder>{
    private ArrayList<SongsAndInfo> songsAndInfoList;
    public static final String SONG_URL_KEY = "songUrl";
    private Context context;
    SongPlayService songPlayService = null;
    SimpleExoPlayerView playerView = null;
    public SongRowAdaptor(Context context, ArrayList<SongsAndInfo> songsAndInfoList, SongPlayService songPlayService) {
        this.songsAndInfoList = songsAndInfoList;
        this.context = context;
        this.songPlayService = songPlayService;
      /* context.bindService(new Intent(context, SongPlayService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);*/
    }
    /*private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SongPlayService.LocalBinder binder = (SongPlayService.LocalBinder) iBinder;
            songPlayService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            songPlayService = null;
        }
    };*/
    @Override
    public MySongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_row_adaptor,parent,false);
        return new MySongViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MySongViewHolder holder, final int position) {
        holder.tvSongName.setText(songsAndInfoList.get(position).getSong());
        holder.tvArtistsName.setText(songsAndInfoList.get(position).getArtists());
        holder.imgViewPlaySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent playSongIntent = new Intent(context,PlaySongActivity.class);
                playSongIntent.putExtra(SONG_URL_KEY,songsAndInfoList.get(position).getUrl());
                context.startActivity(playSongIntent);*/
//                songPlayService.requestPlaySong(playerView,songsAndInfoList.get(position).getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsAndInfoList.size();
    }

    class  MySongViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSongName;
        private TextView tvArtistsName;
        private ImageView imgViewPlaySong;
        public MySongViewHolder(View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.textView);
            tvArtistsName = itemView.findViewById(R.id.tv_artistsName);
            imgViewPlaySong = itemView.findViewById(R.id.imgViewPlay);
        }
    }
}
