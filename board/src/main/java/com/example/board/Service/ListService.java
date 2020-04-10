package com.example.board.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;


@Service
public class ListService {

    public JSONArray getList(String urlString, String token) throws Exception {

        String line;
        StringBuilder sb = new StringBuilder();
        JSONArray array = new JSONArray();
        int i=1;
        
        while(true) {

            URL url = new URL(urlString + "?page=" + i);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.addRequestProperty("token", token);

            InputStreamReader in = new InputStreamReader(con.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(in);

            while((line = br.readLine() ) != null) {
                sb.append(line);
            }

            br.close();
        
            String lines = sb.toString();

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(lines);
            JSONArray temp = (JSONArray)obj;

            array.addAll(temp);

            i++;
        }

        return array;

    }
}