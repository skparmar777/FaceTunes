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

    public static final int EMOJIS_PER_CATEGORY = 5;
    public static final String MOOD_VAL = "Key Value for specific mood";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_mood);

        TableLayout emojiTable = (TableLayout) findViewById(R.id.emoji_mood_table);

        //creates 2D button Array with emoji tables
        final Button[][] emojiSet =
                new Button[emojiTable.getChildCount()][EMOJIS_PER_CATEGORY];

        //Unicode Values of Emoji Set
        final int emojisUnicode[][] = {
                {0x1F642, 0x1F603, 0x1F600, 0x1F604, 0x1F606,},
                {0x2639, 0x1F614, 0x1F623, 0x1F616, 0x1F622 },
                {0x1F615, 0x1F644, 0x1F612, 0x1F926, 0x1F611 },
                {0x1F610, 0x1F620, 0x1F624, 0x1F621, 0x1F47F },
                {0x1F636, 0x1F625, 0x1F613, 0x1F634, 0x1F62A },
        };

        for(int row = 0; row < emojiSet.length; row++){
            for(int col = 0; col < emojiSet[row].length; col++){
                final int currentEmoji = emojisUnicode[row][col];
                TableRow currentTableRow = (TableRow)(emojiTable.getChildAt(row));

                Button currentButton = new Button(this);
                currentButton.setText(getEmojiByUnicode(currentEmoji));

                //returns the unicode value of selected emoji
                currentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //TODO CHANGE TO NEW ACTIVITY WHEN MOOD GETS SELECTED
                        Intent myIntent = new Intent(SelectMood.this, HomeActivity.class);
                        myIntent.putExtra(MOOD_VAL, currentEmoji);
                        startActivity(myIntent);

                    }
                });
                emojiSet[row][col] = currentButton;
                currentTableRow.addView(currentButton);
            }
        }
    }

    public String getEmojiByUnicode(int unicodeVal){
        return new String(Character.toChars(unicodeVal));
    }
}
