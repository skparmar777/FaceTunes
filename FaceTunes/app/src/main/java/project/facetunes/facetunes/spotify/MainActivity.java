package project.facetunes.facetunes.spotify;

/**
 * Created by Sahil on 12/9/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import project.facetunes.facetunes.R;


// TutorialApp
// Created by Spotify on 25/02/14.
// Copyright (c) 2014 Spotify. All rights reserved.
public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    private final static String CLIENT_ID = "4a2d313ffde44aa9a1da7c115ef7aec5";
    private final static String REDIRECT_URI = "facetunes://callback";
    public static String EMOTION = "fear";
    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;
    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_spotify);
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addNotificationCallback(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        if (EMOTION == "happiness") {
            mPlayer.playUri(null, "spotify:user:spotify:playlist:37i9dQZF1DXcF6B6QPhFDv", 0, 0);
        } else if (EMOTION == "anger") {
            mPlayer.playUri(null, "spotify:user:lenalynn2222:playlist:6SO62h8wlSqf7YS5tIIabJ", 0, 0);
        } else if (EMOTION == "contempt") {
            mPlayer.playUri(null, "spotify:user:john3286j:playlist:1QbteDFGxMayGO98aCCjA9", 0, 0);
        } else if (EMOTION == "sadness") {
            mPlayer.playUri(null, "spotify:user:ellegmyers:playlist:7oWnAGM64Bty4TRGmUsw79", 0, 0);
        } else if (EMOTION == "neutral") {
            mPlayer.playUri(null, "spotify:user:spotify:playlist:37i9dQZF1DX4sWSpwq3LiO", 0, 0);
        } else if (EMOTION == "surprise") {
            mPlayer.playUri(null, "spotify:user:bgreen2885:playlist:5WliFKFTqhNdQupuTK6zMi", 0, 0);
        } else if (EMOTION == "fear") {
            mPlayer.playUri(null, "spotify:user:antyx:playlist:7EUMnD3zM6X8f2uDQabom8", 0, 0);
        } else {
            mPlayer.playUri(null, "spotify:user:b_bolt:playlist:16MZDiZxUSnwcAk9fxw929", 0, 0);
        }
        //mPlayer.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 1, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }
}
