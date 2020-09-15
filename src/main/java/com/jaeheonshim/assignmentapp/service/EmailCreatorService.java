package com.jaeheonshim.assignmentapp.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class EmailCreatorService {
    public String createRawEmail(InputStream emailTemplate, Map<String, String> parameters) {
        StringBuilder body = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(emailTemplate))) {
            String line;

            while((line = reader.readLine()) != null) {
                body.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String bodyString = body.toString();

        for(Map.Entry<String, String> entry : parameters.entrySet()) {
            bodyString = bodyString.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return bodyString;
    }
}
