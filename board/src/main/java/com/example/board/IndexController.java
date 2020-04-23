package com.example.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.example.board.Service.ListService;
import com.example.board.dto.ListDto;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

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
    public ArrayList<Object> printChart(String urlString, String token) throws Exception {
        return listService.getChart(urlString, token);
    }

    // csv 파일 다운로드
    @GetMapping("/download.csv")
    public void downloadCsv(HttpServletResponse response, String urlString, String token) {

        response.setContentType("text/csv; charset=MS949");
        response.setCharacterEncoding("MS949");
        response.setHeader("Content-Disposition", "attachment; file=issuelist.csv");
        ICsvBeanWriter csvWriter = null;

        try {
            csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

            String header[] = { "number", "title", "state", "user", "createAt" };

            for (ListDto list : listService.download(urlString, token)) {
                csvWriter.write(list, header);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (csvWriter != null) {
                    csvWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}