package project.facetunes.facetunes;

import android.content.Intent;
import android.content.SharedPreferences;
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

        //TESTING SHARED PREFERENCES
        final SharedPreferences likedItems = getSharedPreferences(
                LikedItems.LIKED_ITEMS_LIST_PREF, MODE_PRIVATE);
        final SharedPreferences.Editor likedItemsEdit = likedItems.edit();

        final SharedPreferences blockedItems = getSharedPreferences(
                BlacklistedItems.BLOCKED_ITEMS_LIST_PREF, MODE_PRIVATE);
        final SharedPreferences.Editor blockedItemsEdit = blockedItems.edit();

        final Button clearPrefBtn = (Button) findViewById(R.id.clearPrefs);

        clearPrefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likedItemsEdit.clear();
                likedItemsEdit.apply();
                blockedItemsEdit.clear();
                blockedItemsEdit.apply();
                count = 0;
                Toast.makeText(HomeActivity.this,
                        "Cleared Data", Toast.LENGTH_SHORT).show();
            }
        });
        //END TEST OF SHARED PREFERENCES

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
                Intent likedItemsActivity = new Intent(HomeActivity.this, LikedItems.class);
                count++;
                likedItemsActivity.putExtra(LikedItems.LIKE_ITEM, "Liked Song #" + count);
                startActivity(likedItemsActivity);
                return true;

            case R.id.menu_playlist:
                //Start playlist activity
                Toast.makeText(this,
                        "Start Playlist Activity", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.menu_select_mood:
                Intent moodActivity = new Intent(HomeActivity.this, SelectMood.class);
                startActivity(moodActivity);
                return true;

            case R.id.menu_settings:
                Intent blacklistedItems = new Intent(HomeActivity.this, BlacklistedItems.class);
                count++;
                blacklistedItems.putExtra(BlacklistedItems.BLOCK_ITEM, "Blocked Song #" + count);
                startActivity(blacklistedItems);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
