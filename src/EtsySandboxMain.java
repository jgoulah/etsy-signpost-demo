import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

public class EtsySandboxMain {

    public static void main(String[] args) throws Exception {

        String consumer_key = System.getenv("OAUTH_CONSUMER_KEY");
        String consumer_secret = System.getenv("OAUTH_CONSUMER_SECRET");

        OAuthConsumer consumer = new DefaultOAuthConsumer(
                consumer_key, consumer_secret
                );

        OAuthProvider provider = new DefaultOAuthProvider(
                "http://openapi.etsy.com/v2/sandbox/oauth/request_token",
                "http://openapi.etsy.com/v2/sandbox/oauth/access_token",
                "http://www.etsy.com/oauth/signin");

        System.out.println("Fetching request token from Etsy...");

        // we do not support callbacks, thus pass OOB
        String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);

        System.out.println("Request token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        System.out.println("Now visit:\n" + authUrl
                + "\n... and grant this app authorization");
        System.out.println("Enter the PIN code and hit ENTER when you're done:");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String pin = br.readLine();

        System.out.println("Fetching access token from Etsy...");

        provider.retrieveAccessToken(consumer, pin);

        System.out.println("Access token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        URL url = new URL("http://openapi.etsy.com/v2/sandbox/private/users/__SELF__");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        consumer.sign(request);

        System.out.println("Sending request to Etsy...");
        request.connect();

        System.out.println("Response: " + request.getResponseCode() + " "
                + request.getResponseMessage());
        
        System.out.println("Payload:");
        InputStream stream = request.getInputStream();
		String stringbuff = "";
        byte[] buffer = new byte[4096];
        
        while (stream.read(buffer) > 0) {
        	for (byte b: buffer) {
        		stringbuff += (char)b;
        	}
        }

        System.out.print(stringbuff);
        stream.close();

        url = new URL("http://openapi.etsy.com/v2/sandbox/private/users/__SELF__");
        request = (HttpURLConnection) url.openConnection();

        consumer.sign(request);

        System.out.println("Sending request to Etsy...");
        request.connect();

        System.out.println("Response: " + request.getResponseCode() + " "
                + request.getResponseMessage());
        
        System.out.println("Payload:");
        stream = request.getInputStream();
		stringbuff = "";
        buffer = new byte[4096];
        
        while (stream.read(buffer) > 0) {
        	for (byte b: buffer) {
        		stringbuff += (char)b;
        	}
        }

        System.out.print(stringbuff);
        stream.close();
    }
}
