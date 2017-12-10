package project.facetunes.facetunes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import project.facetunes.facetunes.spotify.MainActivity;

public class HomeActivity extends AppCompatActivity {

    private static int count = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private String current_mood;
    private Uri current_image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkPermissions();

        if(!deleteImages()){
            Toast.makeText(this, "Failed to delete images.", Toast.LENGTH_SHORT).show();
        }

        Intent getMood = getIntent();
        if(getMood.hasExtra(SelectMood.MOOD_VAL)){
            int emoji_unicode = getMood.getIntExtra(SelectMood.MOOD_VAL, 0);
            int emoji_row = getMood.getIntExtra(SelectMood.ROW_VAL, 0);
            /* TODO Convert unicode to mood string */

            double anger;
            double contempt;
            double disgust;
            double fear;
            double happiness;
            double neutral;
            double sadness;
            double surprise;


            switch(emoji_row){
                case 0:
                    current_mood = "happiness";
                    break;
                case 1:
                    if(emoji_unicode == 0x1F623 ){
                        current_mood = "fear";
                    }
                    else {
                        current_mood = "sadness";
                    }
                    break;
                case 2:
                    if(emoji_unicode == 0x1F612){
                        current_mood = "disgust";
                    } else if (emoji_unicode == 0x1F644){
                        current_mood = "surprise";
                    }
                    else {
                        current_mood = "contempt";
                    }
                    break;
                case 3:
                    current_mood = "anger";
                    break;
                case 4:
                    if(emoji_unicode == 0x1F636){
                        current_mood = "neutral";
                    } else {
                        current_mood = "neutral";
                    }
                    break;
                default:
                    break;
            }
            Toast.makeText(this, "Mood: " + current_mood, Toast.LENGTH_SHORT).show();

            MainActivity.EMOTION = current_mood;
            Intent startSpotify = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(startSpotify);
        } //End intent

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
                Intent likedItemsActivity = new Intent(HomeActivity.this, LikedItems.class);
                count++;
                final String temp_liked_item = "Liked Song #" + count;
                ModifyItem.likeItem(this, temp_liked_item);
                startActivity(likedItemsActivity);
                return true;

            case R.id.menu_playlist:
                Intent savedPlaylistsActivity = new Intent(HomeActivity.this, SavedPlaylists.class);
                startActivity(savedPlaylistsActivity);
                return true;

            case R.id.menu_select_mood:
                Intent moodActivity = new Intent(HomeActivity.this, SelectMood.class);
                startActivity(moodActivity);
                return true;

            case R.id.menu_settings:
                Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the user touches the button
     * Launches the camera API
     */
    public void launchCamera(View view) {
        // Do something in response to button click
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) { //makes sure camera is available
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show();
            }
            //Continue only if file was created
            if (photoFile != null) {

                Uri currentPhotoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider", photoFile);
                current_image_uri = currentPhotoURI;
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoURI);
                Log.d("Location", "URI: " + currentPhotoURI.toString());
                Log.d("Location", "PATH: " + mCurrentPhotoPath);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE); //start camera
            }
        }
        //clearData(view); //TODO Comment out for final release
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //constructor



        if (resultCode == RESULT_OK) {
            Bitmap cameraImage = null;
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras(); // get the image from camera activity
                    if (extras != null) {
                        cameraImage = (Bitmap) extras.get("data"); //image has been set
                    }

                    ImageView myImageView = (ImageView) findViewById(R.id.imageView);
                    myImageView.setImageBitmap(cameraImage);
                    break;

                default:
                    break;
            }
        }
    }

    private File createImageFile() throws IOException {
        //Create an image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean deleteImages(){
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] imageFiles = new File[0];
        if (dir != null) {
            imageFiles = dir.listFiles();
        }
        if(imageFiles.length > 5) {
            for (File file : imageFiles) {
                if (!file.delete()) {
                    Toast.makeText(this, "Cant delete File", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    private void checkPermissions(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 42);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 43);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 44);
        }
    }

    //Clears Shared Preferences; delete for final release
    public void clearData(View view) {
        ModifyItem.clearBlockedItems(HomeActivity.this);
        ModifyItem.clearLikedItems(HomeActivity.this);
        count = 0;
        Toast.makeText(HomeActivity.this,
                "Cleared Data", Toast.LENGTH_SHORT).show();
    }

    public class FaceAsyncTask extends AsyncTask<String, Integer, String> {

        public static final String MICROSOFT_API_URL = "https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize";

        public FaceAsyncTask() {
            super();
        }

        @Override
        protected void onPostExecute(String strongestEmotion) {
            super.onPostExecute(strongestEmotion);
            /* TODO Spotify code goes here */
            current_mood = strongestEmotion;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(MICROSOFT_API_URL);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                String postParameter = "{ \"url\": \"http://dreamatico.com/data_images/people/people-2.jpg\" }";

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept",
                        "application/json");
                urlConnection.setRequestProperty("Content-Type",
                        "application/json");
                urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", "992f402fbf054cf6aec06c617b0ae712");
                // setting your headers its a json in my case set your appropriate header

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();// setting your connection

                OutputStream os = null;
                os = new BufferedOutputStream(
                        urlConnection.getOutputStream());
                os.write(postParameter.getBytes());
                os.flush();// writing your data which you post
                System.out.println(connection.getErrorStream());

                InputStream is = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                String line = null;

                while ((line = br.readLine()) != null)
                    buffer.append(line + "\r\n");

                InputStreamReader inStreamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
                Gson gson = new Gson();
                Face[] objects = gson.fromJson(buffer.toString(), Face[].class);

                is.close();
                urlConnection.disconnect();

                HashMap<String, Double> map = new HashMap<>();
                map.put("anger", 0.0);
                map.put("contempt", 0.0);
                map.put("disgust", 0.0);
                map.put("fear", 0.0);
                map.put("happiness", 0.0);
                map.put("neutral", 0.0);
                map.put("sadness", 0.0);
                map.put("surprise", 0.0);

                for (Face f : objects) {
                    Scores s = f.getScores();
                    map.put("anger", map.get("anger") + s.getAnger());
                    map.put("contempt", map.get("contempt") + s.getContempt());
                    map.put("disgust", map.get("disgust") + s.getDisgust());
                    map.put("fear", map.get("fear") + s.getFear());
                    map.put("happiness", map.get("happiness") + s.getHappiness());
                    map.put("neutral", map.get("neutral") + s.getNeutral());
                    map.put("sadness", map.get("sadness") + s.getSadness());
                    map.put("surprise", map.get("surprise") + s.getSurprise());
                }
                double max = 0;
                String strongestEmotion = "";
                for (String key : map.keySet()) {
                    if (map.get(key) > max) {
                        max = map.get(key);
                        strongestEmotion = key;
                    }
                }
                return strongestEmotion;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
