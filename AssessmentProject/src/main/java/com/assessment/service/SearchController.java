package com.assessment.service;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class SearchController {
    @GetMapping("/")
    public String index() {
        return "search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("directoryPath") String directoryPath,
                         @RequestParam("searchString") String searchString,
                         Model model) throws IOException {

        SearchTab searchTab = new SearchTab();
        String result = searchTab.search(directoryPath, searchString);
        model.addAttribute("result", result);
        return "result";
    }
}
