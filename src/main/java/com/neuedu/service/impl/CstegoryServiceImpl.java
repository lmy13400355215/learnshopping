package com.neuedu.service.impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CstegoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    /**
     *获取品类子节点(平级)
     */
    @Override
    public ServerResponse get_category(Integer categoryId) {
        //step1:非空校验
        if (categoryId==null){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAM_EMPTY .getStatus(),ResponseCode.PARAM_EMPTY.getMsg());
        }
        //step2:根据categoryid查询类别
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return ServerResponse.createServerResponseByError("查询的类别不存在");
        }
        //step3:查询子类别
        List<Category> categoryList=categoryMapper.findChildCategory(categoryId);
        //step4:返回结果
        return ServerResponse.createServerResponseBySucess(null,categoryList);
    }

    /**
     * 增加节点
     */
    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
        //step1:参数非空校验
        if (categoryName==null||categoryName.equals("")){
            return ServerResponse.createServerResponseByError("类别名称不能为空");
        }
        //step2:添加节点
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(1);
        int result=categoryMapper.insert(category);

        //step3:返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess();
        }

        return ServerResponse.createServerResponseByError("添加失败");
    }

    /**
     *修改节点
     */
    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
        //step1:参数非空校验
        if (categoryId==null||categoryId.equals("")){
            return ServerResponse.createServerResponseByError("类别id不能为空");
        }
        if (categoryName==null||categoryName.equals("")){
            return ServerResponse.createServerResponseByError("类别名称不能为空");
        }
        //step2:根据categoryId查询类别
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return ServerResponse.createServerResponseByError("要修改的类别不存在");
        }
        //step3:修改类别
        category.setName(categoryName);
        int result=categoryMapper.updateByPrimaryKey(category);
        //step4:返回结果

        if (result>0){
            //修改成功
            return ServerResponse.createServerResponseBySucess();
        }
        return ServerResponse.createServerResponseByError("修改失败");
    }

    /**
     *增加节点
     */
}
