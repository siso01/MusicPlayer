
package music.com.olaplay.model.mainApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AllSongsAndInfo {

    public List<SongsAndInfo> getSongsAndInfo() {
        return songsAndInfo;
    }

    public void setSongsAndInfo(List<SongsAndInfo> songsAndInfo) {
        this.songsAndInfo = songsAndInfo;
    }

    @SerializedName("songsAndInfo")
    @Expose
    private List<SongsAndInfo> songsAndInfo = null;


}
