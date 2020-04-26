package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.model.GithubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokensDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        String json = JSON.toJSONString(accessTokensDTO);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String str = response.body().string();
            return str.split("=")[1].split("&")[0];
        } catch (IOException e) {
            log.error("get getAccessToken error, {}", accessTokensDTO);
        }
        return null;


    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String strJson = response.body().string();
            log.info(strJson);
            GithubUser githubUser=JSON.parseObject(strJson,GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            log.error("getUser error");
            e.printStackTrace();
        }
        return null;
    }
}
