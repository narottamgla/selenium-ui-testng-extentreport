package com.common.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpRequest {

	private static HttpURLConnection httpConn;
	static final String COOKIES_HEADER = "Set-Cookie";
	static java.net.CookieManager cookieJar = new java.net.CookieManager();
	
	/**
     * Used for attaching a cookie from a previous response to
     * a new request.
     */
	private static void setCookiesForConnection() {
		if (cookieJar.getCookieStore().getCookies().size() > 0) {
			httpConn.setRequestProperty("Cookie", cookieJar.getCookieStore().getCookies().get(0).toString());    
		}
	}
	
	/**
     * Used for getting the cookies from a response to be used in
     * a future request.
     */
	private static void getCookiesFromConnection() {
	    Map<String, List<String>> headerFields = httpConn.getHeaderFields();
	    List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
	      
	    if (cookiesHeader != null) {
	        for (String cookie : cookiesHeader) {
	            cookieJar.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
	        }               
	    }
	}
	
	/**
	 * Sends a Get request to the specified URL with additional parameters
	 * 
	 * @param requestURL
	 * @param additional parameters
	 * @param store Cookie in CookieJar
	 * @param use Cookie from CookieJar
	 * @return
	 * @throws IOException
	 */
	public static HttpURLConnection sendGetRequest(String requestURL, String parametersUrl, Boolean storeCookie, Boolean setCookie) throws IOException {
        URL url = new URL(requestURL + parametersUrl);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        if (setCookie) {
        	setCookiesForConnection();
        } else if (storeCookie) {
        	getCookiesFromConnection();
        }
        
        return httpConn;
	}
	
	/**
	 * Sends a Post request to the specified URL with a body.
     * 
	 * @param requestURL
	 * @param payload
	 * @param store Cookie in CookieJar
	 * @param use Cookie from CookieJar
	 * @return the connection
	 * @throws IOException
     *             thrown if any I/O error occurred
	 */
	public static HttpURLConnection sendPostRequest(String requestURL, String payload, Boolean storeCookie, Boolean setCookie) throws IOException {
		URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoInput(true); // true indicates the server returns response
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "application/JSON");
        
        httpConn.setDoOutput(true); // true indicates POST request
        if (setCookie) {
        	setCookiesForConnection();
        }

        // sends POST data
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write(payload.toString());
        writer.flush();

        if (storeCookie) {
        	getCookiesFromConnection();
        }
        
        return httpConn;
    }
	
	/**
     * Returns only one line from the server's response. This method should be
     * used if the server returns only a single line of String.
     *
     * @return a String of the server's response
     * @throws IOException
     *             thrown if any I/O error occurred
     */
    public static String readSingleLineResponse() throws IOException {
        InputStream inputStream = null;
        if (httpConn != null) {
            inputStream = httpConn.getInputStream();
        } else {
            throw new IOException("Connection is not established.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
 
        String response = reader.readLine();
        reader.close();
        
        return response;
    }
  
    /**
     * Returns an array of lines from the server's response. This method should
     * be used if the server returns multiple lines of String.
     *
     * @return an array of Strings of the server's response
     * @throws IOException
     *             thrown if any I/O error occurred
     */
    public static String[] readMultipleLinesResponse() throws IOException {
        InputStream inputStream = null;
        if (httpConn != null) {
            inputStream = httpConn.getInputStream();
        } else {
            throw new IOException("Connection is not established.");
        }
 
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        List<String> response = new ArrayList<String>();
 
        String line = "";
        while ((line = reader.readLine()) != null) {
        	System.out.println(line);
            response.add(line);
        }
        reader.close();
 
        return (String[]) response.toArray(new String[0]);
    }

    /**
     * Closes the connection if opened
     */
    public static void disconnect() {
        if (httpConn != null) {
            httpConn.disconnect();
        }
    }
}
