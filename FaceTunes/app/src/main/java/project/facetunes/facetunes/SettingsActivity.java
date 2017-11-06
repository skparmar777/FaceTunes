package project.facetunes.facetunes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final ListView settingsList = (ListView) findViewById(R.id.settings_list_view);

        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        break;
                    case 1:
                        Intent blItems = new Intent(SettingsActivity.this,
                                BlacklistedItems.class);
                        startActivity(blItems);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
