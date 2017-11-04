package project.facetunes.facetunes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_liked_songs:
                //Start liked songs activity
                Toast.makeText(this,
                        "Start Liked Songs Activity", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_playlist:
                //Start playlist activity
//                Toast.makeText(this,
//                        "Start Playlist Activity", Toast.LENGTH_SHORT).show();
                Intent blacklistedItems = new Intent(HomeActivity.this, BlacklistedItems.class);
                count++;
                blacklistedItems.putExtra(BlacklistedItems.BLOCK_SONG, "SONG TO BLOCK " + count);
                startActivity(blacklistedItems);
                return true;

            case R.id.menu_select_mood:
                Intent moodActivity = new Intent(HomeActivity.this, SelectMood.class);
                startActivity(moodActivity);
                return true;

            case R.id.menu_settings:
                Intent settingsActivty =
                        new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(settingsActivty);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
