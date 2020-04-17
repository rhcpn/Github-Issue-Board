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

    public JSONArray urlRequest(URL url, String token) throws Exception {

        BufferedReader br = null;
        JSONArray temp = null;

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.addRequestProperty("token", token);

            InputStreamReader in = new InputStreamReader(con.getInputStream(), "utf-8");
            br = new BufferedReader(in);

            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            System.out.println("url : " + url);

            String lines = sb.toString();
            if (lines == null) {
                return null;
            }
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(lines);
            if (obj == null) {
                return null;
            }
            temp = (JSONArray) obj;

        } finally {
            br.close();
        }

        return temp;
    }

    // 이슈 리스트
    public JSONArray getList(String urlString, String token) throws Exception {

        JSONArray array = new JSONArray();
        int i = 1;

        while (true) {
            URL url = new URL(urlString + "&page=" + i);

            JSONArray temp = urlRequest(url, token);

            if (temp.isEmpty() || temp == null) {
                break;
            }
            array.addAll(temp);
            i++;
        }

        return array;

    }

    // 라벨 리스트
    public JSONArray getLabel(String urlString, String token) throws Exception {

        URL url = new URL(urlString);
        JSONArray temp = urlRequest(url, token);

        return temp;
    }

}