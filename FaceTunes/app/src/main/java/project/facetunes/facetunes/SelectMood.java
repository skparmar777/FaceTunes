package project.facetunes.facetunes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Sahil on 10/10/2017.
 */

public class SelectMood extends AppCompatActivity {

    public static int[] emojiClickedVal = new int[2];
    public static final int EMOJIS_PER_CATEGORY = 4;
    public static final String MOOD_VAL = "Key Value for activity launch on click";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_mood);

        TableLayout emojiTable = (TableLayout) findViewById(R.id.emoji_mood_table);
        TableRow happyEmojis = (TableRow) findViewById(R.id.happy_emojis);

        //creates 2D button Array with emoji tables
        final Button[][] emojiSet =
                new Button[emojiTable.getChildCount()][EMOJIS_PER_CATEGORY];

        //TODO: gather images and populate this array
        final Drawable emojisImages[][] = new Drawable[emojiSet.length][emojiSet[0].length];

        for(int row = 0; row < emojiSet.length; row++){
            for(int col = 0; col < emojiSet[row].length; col++){
                final int currentRow = row;
                final int currentCol = col;
                TableRow currentTableRow = (TableRow)(emojiTable.getChildAt(row));

                Button currentButton = new Button(this);
                //sets image
                currentButton.setBackground(emojisImages[row][col]);

                //sets onClick listener to row, column into an int array
                currentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emojiClickedVal[0] = currentRow;
                        emojiClickedVal[1] = currentCol;

                        //TODO CHANGE TO NEW ACTIVITY WHEN MOOD GETS SELECTED
                        Intent myIntent = new Intent(SelectMood.this, HomeActivity.class);
                        myIntent.putExtra(MOOD_VAL, emojiClickedVal);
                        startActivity(myIntent);

                    }
                });
                emojiSet[row][col] = currentButton;
                currentTableRow.addView(currentButton);
            }
        }


    }
}
