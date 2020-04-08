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
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/list")
    public String sendRequest(Model model, String token, String repo) throws Exception {

        String urlString = "https://api.github.com/repos/"+ repo+ "/issues?page=";

        String line;
        StringBuilder sb = new StringBuilder();

        //for(int i=1; i<=3; i++) {

        URL url = new URL(urlString + 1 + "&per_page=100");
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

        return "index";
    }
}