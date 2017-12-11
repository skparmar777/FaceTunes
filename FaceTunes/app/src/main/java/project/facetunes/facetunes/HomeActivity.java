package project.facetunes.facetunes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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
import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.util.List;

import project.facetunes.facetunes.microsoft.helper.ImageHelper;
import project.facetunes.facetunes.spotify.MainActivity;

public class HomeActivity extends AppCompatActivity {

    private static int count = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private String current_mood;
    private Bitmap mBitmap;
    private EmotionServiceClient client;
    private Uri current_image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkPermissions();

        if (!deleteImages()) {
            Toast.makeText(this, "Failed to delete images.", Toast.LENGTH_SHORT).show();
        }

        if (client == null) {
            client = new EmotionServiceRestClient(getString(R.string.face_subscription_key));
        }

        Intent getMood = getIntent();
        if (getMood.hasExtra(SelectMood.MOOD_VAL)) {
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


            switch (emoji_row) {
                case 0:
                    current_mood = "happiness";
                    break;
                case 1:
                    if (emoji_unicode == 0x1F623 || emoji_unicode == 0x1F616) {
                        current_mood = "fear";
                    } else {
                        current_mood = "sadness";
                    }
                    break;
                case 2:
                    if (emoji_unicode == 0x1F612) {
                        current_mood = "disgust";
                    } else if (emoji_unicode == 0x1F644) {
                        current_mood = "surprise";
                    } else {
                        current_mood = "contempt";
                    }
                    break;
                case 3:
                    current_mood = "anger";
                    break;
                case 4:
                    if (emoji_unicode == 0x1F636) {
                        current_mood = "neutral";
                    } else {
                        current_mood = "neutral";
                    }
                    break;
                default:
                    break;
            }
            Toast.makeText(this, "Mood: " + current_mood, Toast.LENGTH_SHORT).show();

            startSpotify(current_mood);
//            MainActivity.EMOTION = current_mood;
//            Intent startSpotify = new Intent(HomeActivity.this, MainActivity.class);
//            startActivity(startSpotify);
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
                        "project.facetunes.facetunes.fileprovider", photoFile);
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
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    // If image is selected successfully, set the image URI and bitmap.

                    mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                            current_image_uri, getContentResolver());

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    //imageView.setImageURI(current_image_uri);

                    if (mBitmap != null) {

                        // Show the image on screen.
                        //imageView.setImageBitmap(mBitmap);

                        // Add detection log.
                        Log.d("RecognizeActivity", "Image: " + current_image_uri + " resized to " + mBitmap.getWidth()
                                + "x" + mBitmap.getHeight());

                        doRecognize();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public void doRecognize() {

        // Do emotion detection using auto-detected faces.
        try {
            new doRequest(false).execute();
        } catch (Exception e) {
            //mEditText.append("Error encountered. Exception is: " + e.toString());
            Log.d("Exception", e.toString());
        }

        //String faceSubscriptionKey = getString(R.string.faceSubscription_key);
//        if (faceSubscriptionKey.equalsIgnoreCase("Please_add_the_face_subscription_key_here")) {
//            Log.d("Face Key", "No key");
//        } else {
//            // Do emotion detection using face rectangles provided by Face API.
//            try {
//                new doRequest(true).execute();
//            } catch (Exception e) {
//                Log.d("Exception", e.toString());
//            }
//        }
    }

    private class doRequest extends AsyncTask<String, String, List<RecognizeResult>> {
        // Store error message
        private Exception e = null;
        private boolean useFaceRectangles = false;

        public doRequest(boolean useFaceRectangles) {
            this.useFaceRectangles = useFaceRectangles;
        }

        @Override
        protected List<RecognizeResult> doInBackground(String... args) {
            if (this.useFaceRectangles == false) {
                try {
                    return processWithAutoFaceDetection();
                } catch (Exception e) {
                    this.e = e;    // Store error
                }
            } else {
                try {
                    //return processWithFaceRectangles();
                } catch (Exception e) {
                    this.e = e;    // Store error
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<RecognizeResult> result) {
            super.onPostExecute(result);
            // Display based on error existence
            HashMap<String, Double> map = new HashMap<>();
            map.put("anger", 0.0);
            map.put("contempt", 0.0);
            map.put("disgust", 0.0);
            map.put("fear", 0.0);
            map.put("happiness", 0.0);
            map.put("neutral", 0.0);
            map.put("sadness", 0.0);
            map.put("surprise", 0.0);

            for (RecognizeResult r : result) {
                map.put("anger", map.get("anger") + r.scores.anger);
                map.put("contempt", map.get("contempt") + r.scores.contempt);
                map.put("disgust", map.get("disgust") + r.scores.disgust);
                map.put("fear", map.get("fear") + r.scores.fear);
                map.put("happiness", map.get("happiness") + r.scores.happiness);
                map.put("neutral", map.get("neutral") + r.scores.happiness);
                map.put("sadness", map.get("sadness") + r.scores.sadness);
                map.put("surprise", map.get("surprise") + r.scores.surprise);
            }
            double max = 0;
            String strongestEmotion = "";
            for (String key : map.keySet()) {
                if (map.get(key) > max) {
                    max = map.get(key);
                    strongestEmotion = key;
                }
            }




            if (this.useFaceRectangles == false) {
                //mEditText.append("\n\nRecognizing emotions with auto-detected face rectangles...\n");
            } else {
                // mEditText.append("\n\nRecognizing emotions with existing face rectangles from Face API...\n");
            }
            if (e != null) {
                Log.d("Exception", e.getMessage());
                this.e = null;
            } else {
                if (result.size() == 0) {
                    Toast.makeText(HomeActivity.this, "No Emotion Detected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Strongest Emotion: " + strongestEmotion, Toast.LENGTH_SHORT).show();
                    startSpotify(strongestEmotion);
                }
            }
        }
    }

    private List<RecognizeResult> processWithAutoFaceDetection() throws EmotionServiceException, IOException {
        Log.d("emotion", "Start emotion detection with auto-face detection");

        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        long startTime = System.currentTimeMillis();
        // -----------------------------------------------------------------------
        // KEY SAMPLE CODE STARTS HERE
        // -----------------------------------------------------------------------

        List<RecognizeResult> result = null;
        //
        // Detect emotion by auto-detecting faces in the image.
        //
        result = this.client.recognizeImage(inputStream);

        String json = gson.toJson(result);
        Log.d("result", json);

        Log.d("emotion", String.format("Detection done. Elapsed time: %d ms", (System.currentTimeMillis() - startTime)));
        // -----------------------------------------------------------------------
        // KEY SAMPLE CODE ENDS HERE
        // -----------------------------------------------------------------------
        return result;
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

    private boolean deleteImages() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] imageFiles = new File[0];
        if (dir != null) {
            imageFiles = dir.listFiles();
        }
        if (imageFiles.length > 5) {
            for (File file : imageFiles) {
                if (!file.delete()) {
                    Toast.makeText(this, "Cant delete File", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 42);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 43);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
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

    private void startSpotify(String emotion) {
        MainActivity.EMOTION = emotion;
        Intent startSpotify = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(startSpotify);
    }
}
