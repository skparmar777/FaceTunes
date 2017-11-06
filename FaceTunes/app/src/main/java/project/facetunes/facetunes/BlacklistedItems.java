package project.facetunes.facetunes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BlacklistedItems extends AppCompatActivity {

    public final static String BLOCKED_ITEMS_LIST_PREF = "KEY FOR BLOCKED ITEMS PREFERENCES";

    SharedPreferences blItemsPref;
    SharedPreferences.Editor blItemsPrefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklisted_items);

        final ListView blockedItemsListView = (ListView) findViewById(R.id.blocked_items_list);

        blItemsPref = getSharedPreferences(BLOCKED_ITEMS_LIST_PREF, MODE_PRIVATE);
        blItemsPrefEdit = blItemsPref.edit();

        //FOR TESTING
//        for (String item : blockedItems) {
//            Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
//        }
//        clearSharedPref();

        populateListView(blockedItemsListView);


        blockedItemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BlacklistedItems.this);
                builder.setTitle(R.string.remove_item_title)
                        .setPositiveButton(R.string.remove_item_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String blockedItemName =
                                        blockedItemsListView.getItemAtPosition(position).toString();
                                blItemsPrefEdit.remove(blockedItemName);
                                blItemsPrefEdit.apply();
                                populateListView(blockedItemsListView);
                            }
                        })
                        .show();

                return false;
            }
        });

    }

    //FOR TESTING PURPOSES
    private void clearSharedPref() {
        blItemsPrefEdit.clear();
        blItemsPrefEdit.commit();
    }

    /**
     * Populate the list view. TODO update to recycler view later on.
     *
     * @param lv listview
     */
    private void populateListView(ListView lv) {
        ArrayList<String> aList = new ArrayList<>();

        for (String key : blItemsPref.getAll().keySet()) {
            //Populates the array list with all values besides the unique key count
            aList.add(blItemsPref.getString(key, ""));
        }

        //Alphabetical order sort
        Collections.sort(aList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                BlacklistedItems.this,
                android.R.layout.simple_list_item_1,
                aList
        );
        lv.setAdapter(arrayAdapter);

    }
}
