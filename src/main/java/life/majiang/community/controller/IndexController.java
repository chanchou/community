package life.majiang.community.controller;

import life.majiang.community.dto.NavurlDTO;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.NavurlService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NavurlService navurlService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {

        PaginationDTO pagination = questionService.list(page, size, search, tag, sort);
        model.addAttribute("pagination", pagination);
        model.addAttribute("sort", sort);
        model.addAttribute("tag", tag);
        Map<String, List<NavurlDTO>> navurlDTOMap = navurlService.getNavurlDTOMap();
        model.addAttribute("navurls", navurlDTOMap);
        return "index";
    }
}
