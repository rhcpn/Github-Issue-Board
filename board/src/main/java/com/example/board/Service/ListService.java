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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 오늘
        Calendar ca = Calendar.getInstance();
        String today = dateFormat.format(ca.getTime());
        // 1일전
        ca.add(Calendar.DATE, -1);
        String today_1 = dateFormat.format(ca.getTime());
        // 2일전
        ca.add(Calendar.DATE, -1);
        String today_2 = dateFormat.format(ca.getTime());
        // 3일전
        ca.add(Calendar.DATE, -1);
        String today_3 = dateFormat.format(ca.getTime());
        // 4일전
        ca.add(Calendar.DATE, -1);
        String today_4 = dateFormat.format(ca.getTime());

        // 해당 날짜 open, closed 이슈 count
        int openCount = 0;
        int closedCount = 0;
        // open, closed 총 이슈 count
        int closed = 0;
        int open = 0;

        JSONObject list1 = new JSONObject();
        JSONObject list2 = new JSONObject();
        JSONObject list3 = new JSONObject();
        JSONObject list4 = new JSONObject();
        JSONObject list5 = new JSONObject();

        JSONArray array = new JSONArray();
        int i = 1;

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

                // closed_at == null , open 이슈
                if (state == null) {
                    open++;
                    state = (String) ob.get("created_at");

                    Date date = dateFormat.parse(state);
                    String issueDate = dateFormat.format(date);

                    if (issueDate.equals(today)) {
                        openCount++;
                        list1.put("created_at", openCount);
                    } else if (issueDate.equals(today_1)) {
                        openCount++;
                        list2.put("created_at", openCount);
                    } else if (issueDate.equals(today_2)) {
                        openCount++;
                        list3.put("created_at", openCount);
                    } else if (issueDate.equals(today_3)) {
                        openCount++;
                        list4.put("created_at", openCount);
                    } else if (issueDate.equals(today_4)) {
                        openCount++;
                        list5.put("created_at", openCount);
                    }
                }
                // closed 이슈
                else {
                    closed++;

                    Date date = dateFormat.parse(state);
                    String issueDate = dateFormat.format(date);

                    if (issueDate.equals(today)) {
                        closedCount++;
                        list1.put("closed_at", closedCount);
                    } else if (issueDate.equals(today_1)) {
                        closedCount++;
                        list2.put("closed_at", closedCount);
                    } else if (issueDate.equals(today_2)) {
                        closedCount++;
                        list3.put("closed_at", closedCount);
                    } else if (issueDate.equals(today_3)) {
                        closedCount++;
                        list4.put("closed_at", closedCount);
                    } else if (issueDate.equals(today_4)) {
                        closedCount++;
                        list5.put("closed_at", closedCount);
                    }
                }

            }

            i++;
        }
        list1.put("openCount", open);
        list1.put("closedCount", closed);
        System.out.println("총 이슈 :" + openCount);
        System.out.println("닫힌 이슈 :" + closedCount);
        array.add(list1);
        array.add(list2);
        array.add(list3);
        array.add(list4);
        array.add(list5);
        return array;
    }

}