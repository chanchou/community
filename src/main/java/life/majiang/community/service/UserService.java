package life.majiang.community.service;

import life.majiang.community.mapper.UserExtMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserExtMapper userExtMapper;
    @Autowired
    private UserMapper userMapper;

    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public void createOrUpdate(User user) {

        if (userExtMapper.selectById(user.getId()) == null) {
            log.info("{} user is insert...", user.getAccountId());
            userExtMapper.add(user);
        } else {
            log.info("{}用户已经存在", user.getName());
            //userMapper.update(user);

        }

    }
}
