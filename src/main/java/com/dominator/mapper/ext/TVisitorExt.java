package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TVisitorExt {

    /**
     * 获取后台访客列表分页
     * @param dto
     * @return
     */
    List<Dto> listBackVisitorPage(Dto dto);

    /**
     * 获取后台今日访客数量
     * @param dto
     * @return
     */
    String selectVisitorNum(Dto dto);

    /**
     * 获取后台访客总量
     * @param dto
     * @return
     */
    String selectVisitorAll(Dto dto);

    /**
     * 获取设备列表
     * @param dto
     * @return
     */
    List<Dto> deviceList(Dto dto);
}
