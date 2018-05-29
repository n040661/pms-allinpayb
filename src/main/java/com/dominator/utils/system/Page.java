package com.dominator.utils.system;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页对象
 * @author gsh
 *
 */
public class Page {

    public static void startPage(Integer pageNum, Integer pageSize){
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        PageHelper.startPage(pageNum, pageSize);
    }

	public static void startPage(Dto dto){
        Integer pageNum = dto.getInteger("pageNum");
        Integer pageSize = dto.getInteger("pageSize");
        Page.startPage(pageNum, pageSize);
    }

    public static Dto resultPage(List list){

        Dto dto = new HashDto();
        PageInfo pageInfo = new PageInfo(list);
        dto.put("pageSize",pageInfo.getPageSize());
        dto.put("pageNum",pageInfo.getPageNum());
        dto.put("total",pageInfo.getTotal());
        dto.put("list",pageInfo.getList());
        return dto;
    }
}
