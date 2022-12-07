package com.xiaxinyu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.xiaxinyu.mall.common.ApiRestResponse;
import com.xiaxinyu.mall.common.Constant;
import com.xiaxinyu.mall.exception.ExceptionEnum;
import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.pojo.User;
import com.xiaxinyu.mall.model.request.AddCategoryReq;
import com.xiaxinyu.mall.model.request.UpdateCategoryReq;
import com.xiaxinyu.mall.model.vo.CategoryVO;
import com.xiaxinyu.mall.service.CategoryService;
import com.xiaxinyu.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月04日 18:41
 * @Copyright:
 * @version: 1.0.0
 */

@RestController
public class CategoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("后台添加目录")
    @PostMapping("admin/category/add")
    public ApiRestResponse addCategory(@Valid @RequestBody AddCategoryReq addCategoryReq, HttpSession session){
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
    }


    @ApiOperation("后台更新目录")
    @PostMapping("admin/category/update")
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq, HttpSession session){
            categoryService.update(updateCategoryReq);
            return ApiRestResponse.success();
    }

    @ApiOperation("后台删除目录")
    @PostMapping("admin/category/delete")
    public ApiRestResponse deleteCategory(@RequestParam Integer id){
        categoryService.delete(id);
        return ApiRestResponse.success();
    }


    @ApiOperation("后台目录列表")
    @PostMapping("admin/category/list")
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("后台目录列表")
    @PostMapping("category/list")
    public ApiRestResponse listCategoryForCustoms(){
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer();
        return ApiRestResponse.success(categoryVOS);
    }
}