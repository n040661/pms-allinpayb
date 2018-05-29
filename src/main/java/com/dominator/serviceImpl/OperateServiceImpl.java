package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.entity.TModules;
import com.dominator.service.OperateService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OperateServiceImpl implements OperateService{
    private static RedisUtil ru = RedisUtil.getRu();

    private static String LISTMODULES = "LIST_MODULES";
    @Override
    public ApiMessage listModules(Dto dto) {
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        String propertyId = tokenDto.getString("property_id");
        //获取该物业未包含的子应用
        List<Dto> list = new ArrayList<Dto>();
        List<String> modulesList = ru.lrange(LISTMODULES+propertyId,0,-1);
        //列表中为空  Redis中未再该物业下添加所有子应用
        if(modulesList == null){
            //获取子应用列表
            Dto moduledto = new HashDto();
            moduledto.put("classify", "0,0");
            //表示h5子应用
            moduledto.put("sign","page");
            moduledto.putAll(tokenDto);
//            List<T_modulesPO>  h5modules = H5modules(moduledto);

            //todo
//            List<TModules>  h5modules = H5modules(moduledto);
            List<TModules>  h5modules = new ArrayList<>();


//            加入一个冗余数据，用来标识该物业是否是已经添加过子应用
            ru.lpush(LISTMODULES+propertyId,propertyId);
            for(TModules t_modulesPO : h5modules){
                //将获取到的所有子应用加入到Redis中
                ru.lpush(LISTMODULES+propertyId,t_modulesPO.getId()+","+t_modulesPO.getModulesName());
                Dto modulesDto = new HashDto();
                modulesDto.put("id",t_modulesPO.getId());
                modulesDto.put("name",t_modulesPO.getModulesName());
                list.add(modulesDto);
            }
        }
        else if(modulesList!=null && modulesList.size()>0){
            for(String str :modulesList){
                Dto modulesDto = new HashDto();
                String[] strs = str.split(",");
                if(strs.length==2){
                    modulesDto.put("id",strs[0]);
                    modulesDto.put("name",strs[1]);
                }
                list.add(modulesDto);
            }
        }
        return ApiMessageUtil.success(list);
    }

    @Override
    public ApiMessage addPackage(Dto dto) {

        return null;
    }
}
