package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    //    是否显示前一页
    private boolean showPrevious;
    //    是否显示后一页
    private boolean showNext;
    //    是否显示第一页
    private boolean showFirstPage;
    //    是否显示最后一页
    private boolean showEndPage;
    //    交互数据
    private List<T> data;
    //    当前页码
    private Integer page;
    //    总页码
    private Integer totalPage;
    //    需要显示的页码
    private List<Integer> pages=new ArrayList<Integer>();

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        this.page = page;
        pages.add(page);

        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0,page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }
//        是否显示最后一页
        if(page==totalPage){
            showNext=false;
        }else {
            showNext=true;
        }
        if(page==1){
            showPrevious=false;
        }else {
            showPrevious=true;
        }
        // 是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }

    }

}
