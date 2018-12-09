package com.neuedu.controller.manage;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category/")
public class CategoryManageController {
    @Autowired
    ICategoryService categoryService;

    /**
     * 获取品类子节点(平级)
     */
    @RequestMapping(value = "get_category.do")
    public ServerResponse get_category(HttpSession session,Integer categoryId){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.USER_ROLE_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_PRIVILEGE.getStatus(),ResponseCode.USER_NOT_PRIVILEGE.getMsg());
        }

        return categoryService.get_category(categoryId);
    }

    /**
     *增加节点
     */
    @RequestMapping(value = "add_category.do")
    public ServerResponse add_category(HttpSession session,
                                       @RequestParam(required = false,defaultValue = "0") Integer parentId,
                                       String categoryName){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.USER_ROLE_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_PRIVILEGE.getStatus(),ResponseCode.USER_NOT_PRIVILEGE.getMsg());
        }

        return categoryService.add_category(parentId,categoryName);
    }

    /**
     * 修改品类名字
     */
    @RequestMapping(value = "set_category_name.do")
    public ServerResponse set_category_name(HttpSession session,Integer categoryId,String categoryName){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.USER_ROLE_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_PRIVILEGE.getStatus(),ResponseCode.USER_NOT_PRIVILEGE.getMsg());
        }

        return categoryService.set_category_name(categoryId,categoryName);
    }

}
