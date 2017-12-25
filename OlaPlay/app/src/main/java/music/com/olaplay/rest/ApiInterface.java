package music.com.olaplay.rest;





import java.util.List;

import music.com.olaplay.model.mainApiResponse.AllSongsAndInfo;
import music.com.olaplay.model.mainApiResponse.SongsAndInfo;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by gpsdesk5 on 19/4/17.
 */

public interface ApiInterface {


    @GET("studio")
    Call<List<SongsAndInfo>> getAllSongs();

}
