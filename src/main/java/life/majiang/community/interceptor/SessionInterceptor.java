package life.majiang.community.interceptor;

import life.majiang.community.mapper.UserExtMapper;
import life.majiang.community.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserExtMapper userExtMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.getServletContext().setAttribute("clientId", clientId);
        request.getServletContext().setAttribute("clientSecret", clientSecret);
        request.getServletContext().setAttribute("redirectUri", redirectUri);

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    User user= userExtMapper.selectByToken(token);
                    if(user!=null){
                        HttpSession session=request.getSession();
                        session.setAttribute("user",user);
                        log.info("{} 已经登录",user.getName());
                        break;
                    }

                }

            }
        }
        log.info("拦截器 SessionTntercept 已经执行");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
