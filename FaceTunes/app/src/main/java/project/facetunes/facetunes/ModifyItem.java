package project.facetunes.facetunes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sahil on 11/6/2017.
 */

class ModifyItem {
    private SharedPreferences.Editor blockedItemsEdit;
    private SharedPreferences.Editor likedItemsEdit;


    @SuppressLint("CommitPrefEdits")
    ModifyItem(Context context) {
        SharedPreferences blockedItems = context.getSharedPreferences(
                BlacklistedItems.BLOCKED_ITEMS_LIST_PREF, Context.MODE_PRIVATE);
        blockedItemsEdit = blockedItems.edit();

        SharedPreferences likedItems = context.getSharedPreferences(
                LikedItems.LIKED_ITEMS_LIST_PREF, Context.MODE_PRIVATE);
        likedItemsEdit = likedItems.edit();
    }

    /**
     * Use this method with context to block a song.
     *
     * @param itemName name of item to block
     */
    void blockItem(String itemName) {
        blockedItemsEdit.putString(itemName, itemName);
        blockedItemsEdit.apply();
    }

    /**
     * Use this method in with context to like a song.
     *
     * @param itemName name of item to like
     */
    void likeItem(String itemName) {
        likedItemsEdit.putString(itemName, itemName);
        likedItemsEdit.apply();
    }

    void clearLikedItems() {
        likedItemsEdit.clear();
        likedItemsEdit.apply();
    }

    void clearBlockedItems() {
        blockedItemsEdit.clear();
        blockedItemsEdit.apply();
    }

}
