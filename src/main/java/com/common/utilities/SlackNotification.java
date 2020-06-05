package com.common.utilities;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ui.utilities.DriverManager;

public class SlackNotification {
    
    
	public static void sendMessage(String message) {
		SlackMessage slackMessage = new SlackMessage();
		slackMessage.setUsername("Slack User");
		// slackMessage.setIcon_emoji(":twice:");
		slackMessage.setText(message);
		// slackMessage.setChannel("test-ci");
		
		sendMessage(slackMessage);
		
	}
	
    public static void sendMessage(SlackMessage message) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(DriverManager.getSlackHook());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(message);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            client.execute(httpPost);
            client.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
