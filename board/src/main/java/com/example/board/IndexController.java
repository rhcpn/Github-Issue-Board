package com.example.board;

import com.example.board.Service.ListService;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public JSONArray printList(Model model, String token, String urlString) throws Exception {

        // 라벨 리스트
        /* JSONArray labelArray = listService.getList(urlString + "/labels", token);
        model.addAttribute("labelList", labelArray); */

        return listService.getList(urlString, token);
    }

}