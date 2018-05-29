package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TPropertyExt {
    List<TProperty> getGardenToManager(String user_id);

    List<TProperty> listGardens(String user_id);

    List<Dto> listManagers(Dto dto);

    List<Dto> listGardenWechats(String garden_id);

    String getPropertyIdByAppId(String appId);
    /**
     * 根据appid获取到物业信息
     * @param appId
     * @return
     */
    TProperty getPropertyByAppId(String appId);
    /**
     * 根据园区id获取物业信息
     */
    Dto getPropertyByGardenId(String garden_id);

    List<Dto> listProperties(Dto dto);

    Dto getProperty(Dto dto);

    Dto getPropertyUser(Dto dto);

}
