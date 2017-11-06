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

public class LikedItems extends AppCompatActivity {

    public final static String LIKED_ITEMS_LIST_PREF = "KEY FOR LIKED ITEMS PREFERENCES";

    SharedPreferences likedItemsPref;
    SharedPreferences.Editor likedItemsPrefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_items);

        final ListView likedItemsListView = (ListView) findViewById(R.id.liked_items_list);

        likedItemsPref = getSharedPreferences(LIKED_ITEMS_LIST_PREF, MODE_PRIVATE);
        likedItemsPrefEdit = likedItemsPref.edit();

        //FOR TESTING
//        for (String item : blockedItems) {
//            Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
//        }
//        clearSharedPref();

        populateListView(likedItemsListView);


        likedItemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LikedItems.this);
                builder.setTitle(R.string.remove_item_title)
                        .setPositiveButton(R.string.remove_item_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String blockedItemName =
                                        likedItemsListView.getItemAtPosition(position).toString();
                                likedItemsPrefEdit.remove(blockedItemName);
                                likedItemsPrefEdit.apply();
                                populateListView(likedItemsListView);
                            }
                        })
                        .show();

                return false;
            }
        });

    }

    //FOR TESTING PURPOSES
    private void clearSharedPref() {
        likedItemsPrefEdit.clear();
        likedItemsPrefEdit.commit();
    }

    /**
     * Populate the list view. TODO update to recycler view later on.
     *
     * @param lv listview
     */
    private void populateListView(ListView lv) {
        ArrayList<String> aList = new ArrayList<>();

        for (String key : likedItemsPref.getAll().keySet()) {
            //Populates the array list with all values besides the unique key count
            aList.add(likedItemsPref.getString(key, ""));
        }

        //Alphabetical order sort
        Collections.sort(aList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                LikedItems.this,
                android.R.layout.simple_list_item_1,
                aList
        );
        lv.setAdapter(arrayAdapter);

    }
}
