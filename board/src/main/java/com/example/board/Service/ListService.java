package com.example.board.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.board.dto.ListDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Service
public class ListService {

    public JSONArray urlRequest(URL url, String token, String userId) throws Exception {

        BufferedReader br = null;
        JSONArray temp = null;

        token = userId + ":" + token;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(token.getBytes()));
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", basicAuth);

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
    public JSONArray getList(String urlString, String token, String userId) throws Exception {

        JSONArray array = new JSONArray();
        int pageNum = 1;

        while (true) {
            URL url = new URL(urlString + "&page=" + pageNum++);

            JSONArray temp = urlRequest(url, token, userId);

            if (temp.isEmpty() || temp == null) {
                break;
            }
            array.addAll(temp);
        }

        return array;

    }

    // 라벨 리스트
    public JSONArray getLabel(String urlString, String token, String userId) throws Exception {

        URL url = new URL(urlString);
        JSONArray temp = urlRequest(url, token, userId);

        return temp;
    }

    // 날짜 포맷
    public String formatDate(String date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (date != null) {
                date = format.format(format.parse(date));
            }

        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    // 맵에 open, close
    public Map<String, Integer> putMap() {

        Map<String, Integer> item = new HashMap<String, Integer>();
        item.put("open", 0);
        item.put("close", 0);

        return item;
    }

    // 차트
    public ArrayList<Object> getChart(String urlString, String token, String userId) throws Exception {

        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
        // open, close 리스트
        ArrayList<Map<String, Integer>> array = new ArrayList<>();
        // 날짜 리스트
        ArrayList<String> date = new ArrayList<>();

        int pageNum = 1;

        while (true) {
            URL url = new URL(urlString + "&page=" + pageNum++);
            JSONArray temp = urlRequest(url, token, userId);

            if (temp.isEmpty() || temp == null) {
                break;
            }

            // map 에 open / closed 날짜 put
            for (int j = 0; j < temp.size(); j++) {
                JSONObject ob = (JSONObject) temp.get(j);

                if (ob == null) {
                    continue;
                }

                // 날짜 포맷
                String closeAt = (String) ob.get("closed_at");
                String createAt = (String) ob.get("created_at");
                closeAt = formatDate(closeAt);
                createAt = formatDate(createAt);

                Map<String, Integer> createItem = result.get(createAt);
                Map<String, Integer> closeItem = result.get(closeAt);

                // close 이슈의 close 날짜
                if (closeAt != null) {

                    // 키가 없으면
                    if (closeItem == null) {

                        closeItem = putMap();
                        result.put(closeAt, closeItem);

                        // open, closed , 날짜 array 저장
                        array.add(closeItem);
                        date.add(closeAt);
                    }
                    closeItem.put("close", closeItem.get("close") + 1);
                }
                // open, close 이슈의 open 날짜
                if (createItem == null) {

                    createItem = putMap();
                    result.put(createAt, createItem);

                    // open, closed , 날짜 array 저장
                    array.add(createItem);
                    date.add(createAt);
                }
                if (createItem.get("open") != null) {
                    createItem.put("open", createItem.get("open") + 1);
                }
            }

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

    // 리스트 다운로드
    public ArrayList<ListDto> download(String urlString, String token, String userId) throws Exception {

        ArrayList<ListDto> array = new ArrayList<>();
        int pageNum = 1;

        while (true) {
            URL url = new URL(urlString + "&page=" + pageNum++);
            JSONArray temp = urlRequest(url, token, userId);

            if (temp.isEmpty() || temp == null) {
                break;
            }

            for (int j = 0; j < temp.size(); j++) {
                JSONObject ob = (JSONObject) temp.get(j);

                Long number = (Long) ob.get("number");
                String title = (String) ob.get("title");
                String state = (String) ob.get("state");

                JSONObject userOb = (JSONObject) ob.get("user");
                String user = (String) userOb.get("login");

                String createAt = (String) ob.get("created_at");
                createAt = formatDate(createAt);

                ListDto listDto = new ListDto(number, title, state, user, createAt);

                array.add(listDto);

            }
        }
        return array;

    }

}