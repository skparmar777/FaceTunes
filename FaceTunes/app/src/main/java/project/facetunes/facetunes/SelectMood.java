package project.facetunes.facetunes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Sahil on 10/10/2017.
 */

public class SelectMood extends AppCompatActivity {

    public int[] emojiClickedVal = new int[2];
    public final int EMOJIS_PER_CATEGORY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_mood);

        TableLayout emojiTable = (TableLayout) findViewById(R.id.emoji_mood_table);
        TableRow happyEmojis = (TableRow) findViewById(R.id.happy_emojis);

        //creates 2D button Array with emoji tables
        final Button[][] emojiSet =
                new Button[emojiTable.getChildCount()][EMOJIS_PER_CATEGORY];

        for(int row = 0; row < emojiSet.length; row++){
            for(int col = 0; col < emojiSet[row].length; col++){
                Button currentButton = new Button(this);
                TableRow currentTableRow = (TableRow)(emojiTable.getChildAt(row));
                final int currentRow = row;
                final int currentCol = col;
                currentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emojiClickedVal[0] = currentRow;
                        emojiClickedVal[1] = currentCol;
                    }
                });
                emojiSet[row][col] = currentButton;
                currentTableRow.addView(currentButton);
            }
        }


    }
}
