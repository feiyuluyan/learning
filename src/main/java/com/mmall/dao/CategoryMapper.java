package com.mmall.dao;

import com.mmall.pojo.Category;

import java.util.List;

/**
 * Created by Administrator on 2018/5/13.
 */
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category>  selectCategoryChildrenByParentId(Integer parent_id);
}
