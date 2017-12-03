package project.facetunes.facetunes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private static int count = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static Bitmap cameraImage; //current image
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkPermissions();

        if(!deleteImages()){
            Toast.makeText(this, "Failed to delete images.", Toast.LENGTH_SHORT).show();
        }

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
}
