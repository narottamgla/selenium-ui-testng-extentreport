package com.common.utilities;


import java.io.IOException;
import java.security.GeneralSecurityException;

import com.common.api.GmailApi;

public class GmailEmailMessage {

    public String emailMsg(String query) throws IOException, GeneralSecurityException {
        GmailApi apiCall = new GmailApi();
        return apiCall.gmailAPIReponse(query);
    }


}
