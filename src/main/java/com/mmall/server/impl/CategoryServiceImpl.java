package com.mmall.server.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.server.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/13.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName,Integer parentId){
        if(parentId==null||org.apache.commons.lang3.StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//设置分类可用

        int rowCount= categoryMapper.insert(category);
        if(rowCount>0){
            return  ServerResponse.createBySuccess("添加品类成功");
        }
        return  ServerResponse.createByErrorMessage("添加品类失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId==null||org.apache.commons.lang3.StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return  ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }


    /**
     * 递归查询本节点的Id和孩子节点的Id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet= Sets.newHashSet();
        findChildCotegory(categorySet,categoryId);

        List<Integer> categoryList= Lists.newArrayList();
        if(categoryId!=null){
            for(Category categoryItem:categorySet){
                categoryList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryList);
    }
    //递归算出子节点
    private Set<Category> findChildCotegory(Set<Category> categorySet, Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        //查找子节点，递归退出条件
        List<Category> categoryList= categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categryItem:categoryList){
            findChildCotegory(categorySet,categryItem.getId());
        }
        return categorySet;
    }

}
