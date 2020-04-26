package life.majiang.community.controller;

import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model,
                          HttpServletRequest request,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "description", required = false) String description,
                          @RequestParam(value = "tag", required = false) String tag,
                          @RequestParam(value = "id", required = false) String id) {

        log.info("接收到publish的get请求");
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(Model model,
                            HttpServletRequest request,
                            @RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "tag", required = false) String tag,
                            @RequestParam(value = "id", required = false) String id) {
        log.info("接收到publish的post请求");
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("id", id);
        if (StringUtils.isEmptyOrWhitespace(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (StringUtils.isEmptyOrWhitespace(description)) {
            model.addAttribute("error", "问题描述不能为空");
            return "publish";
        }
        if (StringUtils.isEmptyOrWhitespace(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        //判断用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        questionService.CreateOrUpdate(question);

        return "redirect:/";
    }
}
