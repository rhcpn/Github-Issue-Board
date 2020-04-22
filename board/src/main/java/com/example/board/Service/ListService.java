package com.example.board.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.google.gson.JsonArray;

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
    public ArrayList getChart(String urlString, String token) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
        // open, close array
        ArrayList<Map<String, Integer>> array = new ArrayList<>();
        // 날짜 array
        ArrayList<String> date = new ArrayList<>();

        int i = 1;

        while (true) {
            URL url = new URL(urlString + "&page=" + i);
            JSONArray temp = urlRequest(url, token);

            if (temp.isEmpty() || temp == null) {
                break;
            }

            // map 에 open / closed 날짜 put
            for (int j = 0; j < temp.size(); j++) {
                JSONObject ob = (JSONObject) temp.get(j);

                // 날짜 포맷
                String closeAt = (String) ob.get("closed_at");
                if (closeAt != null) {
                    closeAt = format.format(format.parse(closeAt));
                }
                String createAt = (String) ob.get("created_at");
                if (createAt != null) {
                    createAt = format.format(format.parse(createAt));
                }

                Map<String, Integer> createItem = result.get(createAt);
                Map<String, Integer> closeItem = result.get(closeAt);

                // close 이슈의 close 날짜
                if (closeAt != null) {

                    // 키가 없으면
                    if (closeItem == null) {

                        closeItem = new HashMap<String, Integer>();
                        closeItem.put("open", 0);
                        closeItem.put("close", 0);
                        result.put(closeAt, closeItem);

                        // open, closed , 날짜 array 저장
                        array.add(closeItem);
                        date.add(closeAt);
                    }
                    createItem = result.get(createAt);
                    closeItem.put("close", closeItem.get("close") + 1);
                }
                // open, close 이슈의 open 날짜
                if (createItem == null) {

                    createItem = new HashMap<String, Integer>();
                    createItem.put("open", 0);
                    createItem.put("close", 0);
                    result.put(createAt, createItem);

                    // open, closed , 날짜 array 저장
                    array.add(createItem);
                    date.add(createAt);
                }
                if (createItem.get("open") != null) {
                    createItem.put("open", createItem.get("open") + 1);

                }
            }

            i++;
        }

        ArrayList<Object> open = new ArrayList<>();
        ArrayList<Object> close = new ArrayList<>();

        // open 수 , close 수 각각 리스트에 담기
        for (int j = 0; j < array.size(); j++) {
            Map<String, Integer> m = (Map<String, Integer>) array.get(j);
            open.add(m.get("open"));
            close.add(m.get("close"));
        }

        ArrayList<Object> results = new ArrayList<>();

        Collections.reverse(date);
        Collections.reverse(open);
        Collections.reverse(close);

        results.add(date);
        results.add(open);
        results.add(close);

        return results;
    }

}