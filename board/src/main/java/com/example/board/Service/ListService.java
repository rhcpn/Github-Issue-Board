package com.example.board.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
            if (br != null) {
                br.close();
            }

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

    // 차트
    public JSONArray getChart(String urlString, String token) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        JSONArray array = new JSONArray();
        Map<String, Integer> openMap = new LinkedHashMap<>();
        Map<String, Integer> closedMap = new LinkedHashMap<>();
        int i = 1;
        int openCount = 1;
        int closedCount = 1;
        int openIssue = 0;
        int closedIssue = 0;

        while (true) {
            URL url = new URL(urlString + "&page=" + i);
            JSONArray temp = urlRequest(url, token);

            if (temp.isEmpty() || temp == null) {
                break;
            }

            // close날짜, open 날짜 비교
            for (int j = 0; j < temp.size(); j++) {
                JSONObject ob = (JSONObject) temp.get(j);
                String state = (String) ob.get("closed_at");

                if (state == null) {
                    openIssue++;
                    state = (String) ob.get("created_at");
                    String date = format.format(format.parse(state));

                    if (openMap.containsKey(date)) {
                        openMap.put(date, ++openCount);
                        continue;
                    }

                    openMap.put(date, openCount);

                } else {
                    closedIssue++;
                    String date = format.format(format.parse(state));

                    if (closedMap.containsKey(date)) {
                        closedMap.put(date, ++closedCount);
                        continue;
                    }
                    closedMap.put(date, closedCount);
                }

            }

            i++;
        }
        openMap.put("openIssue", openIssue);
        closedMap.put("closedIssue", closedIssue);
        array.add(openMap);
        array.add(closedMap);

        return array;
    }

}