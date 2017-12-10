package project.facetunes.facetunes;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * ASYNCHRONUS TASK
 */

public class FaceAsyncTask extends AsyncTask<String, Integer, String> {

    public static final String MICROSOFT_API_URL = "https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize";

    public FaceAsyncTask() {
        super();
    }

    @Override
    protected void onPostExecute(String strongestEmotion) {
        super.onPostExecute(strongestEmotion);
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
