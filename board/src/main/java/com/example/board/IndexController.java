package com.example.board;

import com.example.board.Service.ListService;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final ListService listService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/list")
    public JSONArray printList(String urlString, String token) throws Exception {

        return listService.getList(urlString, token);
    }

    @ResponseBody
    @GetMapping("/label")
    public JSONArray printLabel(String urlString, String token) throws Exception {

        return listService.getLabel(urlString, token);
    }

    @ResponseBody
    @GetMapping("/chart")
    public JSONArray printChart(String urlString, String token) throws Exception {
        return listService.getChart(urlString, token);
    }

}