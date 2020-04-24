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

    public String urlString(String url, String repo, String state, String label) {

        // 이슈 리스트
        if (url.equals("/list")) {
            return "https://api.github.com/repos/" + repo + "/issues?per_page=100" + label + state;
        }
        // 라벨 리스트
        else if (url.equals("/label")) {
            return "https://api.github.com/repos/mobigen/IRIS-BigData-Platform/labels";
        }
        // 차트, 다운로드
        else {
            return "https://api.github.com/repos/mobigen/IRIS-BigData-Platform/labels";
        }

    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/list")
    public JSONArray printList(String token, String url, String repo, String state, String label) throws Exception {

        String urlString = urlString(url, repo, state, label);
        return listService.getList(urlString, token);
    }

    @ResponseBody
    @GetMapping("/label")
    public JSONArray printLabel(String token, String url, String repo, String state, String label) throws Exception {

        String urlString = urlString(url, repo, state, label);
        return listService.getLabel(urlString, token);
    }

    @ResponseBody
    @GetMapping("/chart")
    public ArrayList<Object> printChart(String token, String url, String repo, String state, String label)
            throws Exception {
        String urlString = urlString(url, repo, state, label);
        return listService.getChart(urlString, token);
    }

    // csv 파일 다운로드
    @GetMapping("/download.csv")
    public void downloadCsv(HttpServletResponse response, String token, String url, String repo) {

        response.setContentType("text/csv; charset=MS949");
        response.setCharacterEncoding("MS949");
        response.setHeader("Content-Disposition", "attachment; file=issuelist.csv");
        ICsvBeanWriter csvWriter = null;

        try {
            csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

            String header[] = { "number", "title", "state", "user", "createAt" };
            String urlString = urlString(url, repo, null, null);
            System.out.println(urlString);

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