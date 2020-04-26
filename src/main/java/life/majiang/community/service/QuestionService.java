package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import life.majiang.community.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.server.InactiveGroupException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserService userService;


    public PaginationDTO list(Integer page, Integer size, String search, String tag, String sort) {
        PaginationDTO pagination = new PaginationDTO();

        //TODO
        List<Question> questions = null;
        QuestionExample questionExample = new QuestionExample();
        List<Question> questions1 = questionMapper.selectByExample(questionExample);
        List<QuestionDTO> questionDTOS=new ArrayList<QuestionDTO>();
        for (Question question:questions1){
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setId(question.getId());
            questionDTO.setCommentCount(question.getCommentCount());
            questionDTO.setGmtCreate(question.getGmtCreate());
            questionDTO.setGmtModified(question.getGmtModified());
            questionDTO.setLikeCount(question.getLikeCount());
            questionDTO.setTitle(question.getTitle());
            questionDTO.setTag(question.getTag());
            questionDTO.setViewCount(question.getViewCount());

            User user = userService.getUserById(question.getCreator());
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pagination.setData(questionDTOS);
        long count = questionMapper.countByExample(questionExample);
        int totalpage = (int) count / size;
        pagination.setPagination(totalpage, page);
        return pagination;
    }

    public boolean CreateOrUpdate(Question question) {

        if (question.getId() == null) {
            //新增
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            log.info("{}问题已保存之数据库", question.getTitle());
            questionMapper.insert(question);
        } else {
            //更新
            Question dbQuestion = questionMapper.selectByPrimaryKey(question.getId());
            Question updateQuestion = new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setGmtModified(System.currentTimeMillis());

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int flag = questionMapper.updateByExample(updateQuestion, questionExample);
            if (flag == 1) {
                log.info("{}问题已更新", question.getId());
                return true;
            } else {
                return false;
            }

        }
        return false;
    }

    public QuestionDTO getById(String id) {
        Question question = questionMapper.selectByPrimaryKey(Integer.parseInt(id));
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user= userService.getUserById(question.getCreator());
        questionDTO.setUser(user);

      return questionDTO;
    }

    public List<QuestionDTO> selectRelated(String id) {
        return null;
    }

    public void increaseViewCount(String id) {
    }
}
