package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExtMapper {

    @Select("select * from user where id=#{id}")
    User selectById(Integer id);

    void update(User user);

    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified,bio,avatar_url) values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{bio},#{avatarUrl})")
    void add(User user);

    @Select("select * from user where token=#{token}")
    User selectByToken(String token);
}
