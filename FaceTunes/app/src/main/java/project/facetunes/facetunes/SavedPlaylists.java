package project.facetunes.facetunes;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SavedPlaylists extends AppCompatActivity {

    ListView listView;
    List<String> plist;
    ListAdapter adapter;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_playlists);

        listView = (ListView) findViewById(R.id.playlistLV);
        plist = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            plist.add(field.getName());
        }

//        plist.remove(0);
//        plist.remove(0);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, plist);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }

                int resID = getResources().getIdentifier(plist.get(position), "raw", getPackageName());
                mediaPlayer = MediaPlayer.create(SavedPlaylists.this, resID);
                mediaPlayer.start();
            }
        });

    }
}
