package com.example.board;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.board.Service.ListService;

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

        String urlString = "https://api.github.com/repos/" + repo;

        ListService listService = new ListService();
        JSONArray array = listService.getList(urlString + "/issues?state=open", token);

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


        String urlString = "https://api.github.com/repos/" + repo;

        ListService listService = new ListService();
        JSONArray array = listService.getList(urlString + "/issues?state=closed", token);

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

    @GetMapping("/labels")
    public String getLabel(Model model, String repo, String token, String label) throws Exception {

        String urlString = "https://api.github.com/repos/" + repo;

        ListService listService = new ListService();
        JSONArray array = listService.getList(urlString + "/issues/" + label, token);

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