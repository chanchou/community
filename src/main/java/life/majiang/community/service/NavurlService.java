package life.majiang.community.service;

import life.majiang.community.dto.NavurlDTO;
import life.majiang.community.mapper.NavurlMapper;
import life.majiang.community.model.Navurl;
import life.majiang.community.model.NavurlExample;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NavurlService {
    @Autowired
    private NavurlMapper navurlMapper;

    public Map<String, List<NavurlDTO>> getNavurlDTOMap() {
        Map<String, List<NavurlDTO>> map = new HashMap<String, List<NavurlDTO>>();
        NavurlExample example = new NavurlExample();
        example.or().andTopIdEqualTo(0);
        example.setOrderByClause("'sort_number' asc");
        List<Navurl> navurls = navurlMapper.selectByExample(example);
        for (Navurl navurl : navurls) {
            List<NavurlDTO> list = new ArrayList<>();
            NavurlExample exampleSub = new NavurlExample();
            exampleSub.or().andTopIdEqualTo(navurl.getId());
            exampleSub.setOrderByClause("'sort_number' desc");
            List<Navurl> navurlSubs = navurlMapper.selectByExample(exampleSub);
            for (Navurl navurlSub : navurlSubs) {
                NavurlDTO navurlDTO=new NavurlDTO();
                navurlDTO.setName(navurlSub.getDecription());
                navurlDTO.setUrl(navurlSub.getUrl());
                list.add(navurlDTO);
            }

            map.put(navurl.getDecription(), list);
        }

        return map;
    }
}
