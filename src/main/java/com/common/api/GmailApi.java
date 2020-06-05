package com.common.api;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.MessagePart;
import com.ui.utilities.DriverManager;
import com.google.api.services.gmail.model.Message;
import com.google.api.client.util.Base64;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


public class GmailApi {
    public GmailApi() throws IOException, GeneralSecurityException {
    }

    private String user = DriverManager.getGmailAccount();
    private GmailApiHelper serviceObj = new GmailApiHelper();
    private final Gmail service = serviceObj.gmailAPIService();

    public String gmailAPIReponse(String query) throws IOException {
        List<Message> emails = getAllEmails(query);
        String emailMsg = emailContent(emails);
        return emailMsg;
    }

    private List<Message> getAllEmails(String query) throws IOException {
        ListMessagesResponse msgResponse = service.users().messages().list(user).setQ(query).execute();
        return msgResponse.getMessages();
    }

    private String emailContent(List<Message> messageList) throws IOException {
        String emailText = "" ;
        if (messageList.size() > 0 && messageList.size() <=1) {
            for (Message message : messageList) {
                Message aMsg = service.users().messages().get(user, message.getId()).setFormat("full").execute();
                List<MessagePart> messageParts = aMsg.getPayload().getParts();
                byte[] emailData = Base64.decodeBase64(messageParts.get(0).getBody().getData());   //MIME Message Text
                emailText = new String(emailData);
            }
        }
        return emailText;
    }





}
