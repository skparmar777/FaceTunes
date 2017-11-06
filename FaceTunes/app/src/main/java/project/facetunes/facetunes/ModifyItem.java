package project.facetunes.facetunes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sahil on 11/6/2017.
 */

class ModifyItem {
    private static SharedPreferences.Editor blockedItemsEdit;
    private static SharedPreferences.Editor likedItemsEdit;

    /**
     * Use this method with context to block a song.
     *
     * @param itemName name of item to block
     */
    static void blockItem(Context context, String itemName) {
        setContext(context);
        blockedItemsEdit.putString(itemName, itemName);
        blockedItemsEdit.apply();
    }

    /**
     * Use this method in with context to like a song.
     *
     * @param itemName name of item to like
     */
    static void likeItem(Context context, String itemName) {
        setContext(context);
        likedItemsEdit.putString(itemName, itemName);
        likedItemsEdit.apply();
    }

    static void clearLikedItems(Context context) {
        setContext(context);
        likedItemsEdit.clear();
        likedItemsEdit.apply();
    }

    static void clearBlockedItems(Context context) {
        setContext(context);
        blockedItemsEdit.clear();
        blockedItemsEdit.apply();
    }

    @SuppressLint("CommitPrefEdits")
    private static void setContext(Context context) {
        SharedPreferences blockedItems = context.getSharedPreferences(
                BlacklistedItems.BLOCKED_ITEMS_LIST_PREF, Context.MODE_PRIVATE);
        blockedItemsEdit = blockedItems.edit();

        SharedPreferences likedItems = context.getSharedPreferences(
                LikedItems.LIKED_ITEMS_LIST_PREF, Context.MODE_PRIVATE);
        likedItemsEdit = likedItems.edit();
    }


}
