package com.example.board;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListController {

    @GetMapping("/open")
    public String oepnList(Model model, String repo, String token) throws Exception {


        String urlString = "https://api.github.com/repos/"+ repo+ "/issues?page=";

        String line;
        StringBuilder sb = new StringBuilder();

        //for(int i=1; i<=3; i++) {

        URL url = new URL(urlString + 1 + "&per_page=100&state=open");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.addRequestProperty("token", token);

        InputStreamReader in = new InputStreamReader(con.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader(in);

        line = br.readLine();

        sb.append(line).append("\n");

        //}

        con.disconnect();

       /*  while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close(); */

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(sb.toString());
        JSONArray array = (JSONArray)obj;
        JSONArray labels = null;

        for(int i=0; i<array.size(); i++) {
            JSONObject issue = (JSONObject)array.get(i);
            // 라벨 
            labels = (JSONArray)issue.get("labels");

        }
        model.addAttribute("issue", array);
        model.addAttribute("labels", labels);
        model.addAttribute("repo",repo);
        model.addAttribute("token", token);


        return "index";
    }

    @GetMapping("/closed")
    public String closedList(Model model, String repo, String token) throws Exception {


        String urlString = "https://api.github.com/repos/"+ repo+ "/issues?page=";

        String line;
        StringBuilder sb = new StringBuilder();

        //for(int i=1; i<=3; i++) {

        URL url = new URL(urlString + 1 + "&per_page=100&state=closed");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.addRequestProperty("token", token);

        InputStreamReader in = new InputStreamReader(con.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader(in);

        line = br.readLine();

        sb.append(line).append("\n");

        //}

        con.disconnect();

       /*  while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close(); */

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(sb.toString());
        JSONArray array = (JSONArray)obj;
        JSONArray labels = null;

        for(int i=0; i<array.size(); i++) {
            JSONObject issue = (JSONObject)array.get(i);
            // 라벨 
            labels = (JSONArray)issue.get("labels");

        }
        model.addAttribute("issue", array);
        model.addAttribute("labels", labels);
        model.addAttribute("repo",repo);
        model.addAttribute("token", token);

        return "index";
    }

}