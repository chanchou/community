package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.model.GithubUser;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import life.majiang.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(HttpServletRequest request, HttpServletResponse response, @RequestParam("state") String state, @RequestParam("code") String code) {
        AccessTokenDTO accessTokensDTO = new AccessTokenDTO();
        accessTokensDTO.setClient_id(request.getServletContext().getAttribute("clientId").toString());
        accessTokensDTO.setClient_secret(request.getServletContext().getAttribute("clientSecret").toString());
        accessTokensDTO.setRedirect_uri(request.getServletContext().getAttribute("redirectUri").toString());
        accessTokensDTO.setCode(code);
        accessTokensDTO.setState(state);

        String accessToken = githubProvider.getAccessToken(accessTokensDTO);
        log.info("github get accessToken is {}",accessToken);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null) {
            String token = UUID.randomUUID().toString();
            User user = new User();
            user.setToken(token);
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setBio(githubUser.getBio());
            user.setName(githubUser.getLogin());

            userService.createOrUpdate(user);

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);

        } else {
            log.error("callback get github error, {}", githubUser);
        }
        log.info("跳转回首页");
        return "redirect:/";
    }

}
