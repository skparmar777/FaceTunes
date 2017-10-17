import java.net.URI;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class APIRequest {

//    public static String requestFromAPI() {
//        Gson gson = new Gson();
//
//        // builds the URL from the given parameters.
//        final String URL = "https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize";
//
//        try {
//
//
//            final HttpResponse<Entity> stringHttpResponse = Unirest.post(URL)
//                    .header("Ocp-Apim-Subscription-Key", "38d815f4864544ebbd38420c4f45fbef").
//                            body("{ \"url\": \"http://dreamatico.com/data_images/people/people-2.jpg\" }").getEntity();
//            final String URL_OUTPUT = stringHttpResponse.getBody();
//
//            // parses the JSON if the output is valid.
//            if (stringHttpResponse.getStatus() == 200) {
////                Face[] resultFaces = gson.fromJson(URL_OUTPUT, Face[].class);
////                return Face[];
//                return URL_OUTPUT;
//            } else {
//                return URL_OUTPUT + stringHttpResponse.getStatus();
//            }
//
//        } catch (UnirestException e) {
//            return "ay";
//        }
//    }
//
//    public static void main(String[] args) {
//        System.out.println(requestFromAPI());
//    }

    public static Face[] requestFromAPI() {
        HttpClient httpClient = new DefaultHttpClient();
        Gson gson = new Gson();

        try {

            URIBuilder uriBuilder = new URIBuilder("https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize");

            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", "38d815f4864544ebbd38420c4f45fbef");

            StringEntity reqEntity = new StringEntity("{ \"url\": \"http://dreamatico.com/data_images/people/people-2.jpg\" }");
            request.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String json = EntityUtils.toString(entity);
                Face[] faces = gson.fromJson(json, Face[].class);
                return faces;
            }
            return null;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static void main(String[] args) {
        System.out.println(requestFromAPI()[0].getScores().getAnger());
    }
}
