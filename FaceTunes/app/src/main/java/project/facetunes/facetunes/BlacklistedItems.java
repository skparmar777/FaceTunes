package project.facetunes.facetunes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BlacklistedItems extends AppCompatActivity {

    public final static String BLOCKED_ITEMS_LIST_PREF = "KEY FOR BLOCKED ITEMS PREFERENCES";
    private final static String KEY_GEN_CODE = "KEY GENERATOR PREF";

    /**
     * Type BlacklistedItems.BLOCK_SONG in intent.putStringExtra() to block the song.
     */
    public final static String BLOCK_SONG = "STRING TO BLOCK A SONG";

    SharedPreferences blItemsPref;
    SharedPreferences.Editor blItemsPrefEdit;

    SharedPreferences keyPref;
    SharedPreferences.Editor keyPrefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklisted_items);

        ArrayList<String> blockedItems = new ArrayList<>();

        blItemsPref = getSharedPreferences(BLOCKED_ITEMS_LIST_PREF, MODE_PRIVATE);
        blItemsPrefEdit = blItemsPref.edit();

        keyPref = getSharedPreferences(KEY_GEN_CODE, MODE_PRIVATE);
        keyPrefEdit = keyPref.edit();

        Intent intent = getIntent();

        if (intent.hasExtra(BLOCK_SONG)) {
            //Generate new key;
            int key = keyPref.getInt(KEY_GEN_CODE, 0) + 1;
            String uniqueKey = "Blocked Item #" + key;
            keyPrefEdit.putInt(KEY_GEN_CODE, key);
            keyPrefEdit.apply();

            String blockedItem = intent.getStringExtra(BLOCK_SONG);
            blItemsPrefEdit.putString(uniqueKey, blockedItem);
            blItemsPrefEdit.apply();
        }

        //Fill ArrayList
        for(String key : blItemsPref.getAll().keySet()){
            blockedItems.add(blItemsPref.getString(key, ""));
        }

        for(String item : blockedItems){
            Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
        }

        //clearSharedPref();

    }

    //FOR TESTING PURPOSES
    private void clearSharedPref(){
        blItemsPrefEdit.clear();
        blItemsPrefEdit.commit();

        keyPrefEdit.clear();
        keyPrefEdit.apply();
    }
}
