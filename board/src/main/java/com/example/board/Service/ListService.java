package com.example.board.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        JSONArray array = new JSONArray();
        int i = 1;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 오늘
        Calendar ca = Calendar.getInstance();
        String today = dateFormat.format(ca.getTime());
        // 1일전
        ca.add(Calendar.DATE, -1);
        String before1Day = dateFormat.format(ca.getTime());
        // 2일전
        ca.add(Calendar.DATE, -1);
        String before2Day = dateFormat.format(ca.getTime());
        // 3일전
        ca.add(Calendar.DATE, -1);
        String before3Day = dateFormat.format(ca.getTime());
        // 4일전
        ca.add(Calendar.DATE, -1);
        String before4Day = dateFormat.format(ca.getTime());

        int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0;
        int count6 = 0, count7 = 0, count8 = 0, count9 = 0, count10 = 0;
        int closedCount = 0;
        int openCount = 0;

        JSONObject list1 = new JSONObject();
        JSONObject list2 = new JSONObject();
        JSONObject list3 = new JSONObject();
        JSONObject list4 = new JSONObject();
        JSONObject list5 = new JSONObject();

        while (true) {
            URL url = new URL(urlString + "&page=" + i);
            JSONArray temp = urlRequest(url, token);

            for (int j = 0; j < temp.size(); j++) {
                JSONObject ob = (JSONObject) temp.get(j);
                String state = (String) ob.get("closed_at");

                if (state == null) {
                    openCount++;
                    state = (String) ob.get("created_at");

                    Date date = dateFormat.parse(state);
                    String issueDate = dateFormat.format(date);

                    if (issueDate.equals(today)) {
                        count1++;
                        list1.put("created_at", count1);
                    } else if (issueDate.equals(before1Day)) {
                        count2++;
                        list2.put("created_at", count2);
                    } else if (issueDate.equals(before2Day)) {
                        count3++;
                        list3.put("created_at", count3);
                    } else if (issueDate.equals(before3Day)) {
                        count4++;
                        list4.put("created_at", count4);
                    } else if (issueDate.equals(before4Day)) {
                        count5++;
                        list5.put("created_at", count5);
                    }
                }

            }

            if (temp.isEmpty() || temp == null) {
                break;
            }
            i++;
        }
        list1.put("openCount", openCount);
        // list1.put("closedCount", closedCount);
        System.out.println("총 이슈 :" + openCount);
        array.add(list1);
        array.add(list2);
        array.add(list3);
        array.add(list4);
        array.add(list5);
        return array;
    }

}