package com.dominator.serviceImpl.api;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominator.entity.TGarden;
import com.dominator.entity.TGardenExample;
import com.dominator.mapper.TGardenMapper;
import com.dominator.mapper.ext.TPropertyExt;
import com.dominator.service.api.ApiGardenService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.system.Constants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiGardenServiceImpl implements ApiGardenService{

    @Autowired
    private TPropertyExt tPropertyExt;

    @Autowired
    private TGardenMapper tGardenMapper;

    @Override
    public ApiMessage getgardensByProId(Dto dto) {

        String appId = dto.getString("app_id");

        String property_id = tPropertyExt.getPropertyIdByAppId(appId);
        dto.put("property_id", property_id);

        ApiMessage msg = new ApiMessage(Constants.REQ_SUCCESS, Constants.MSG_SUCCESS);
        Dto resDto = Dtos.newDto();

        dto.put("is_valid", "1");
        TGardenExample tGardenExample = new TGardenExample();
        tGardenExample.or().andPropertyIdEqualTo(property_id).andIsValidEqualTo("1");
        List<TGarden> list = tGardenMapper.selectByExample(tGardenExample);
        tGardenExample.clear();
        //List<TGarden> list = t_gardenDao.like(dto);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                JSONObject json = new JSONObject();
                json.accumulate("garden_id", list.get(i).getId());
                json.accumulate("garden_name", list.get(i).getGardenName());
                json.accumulate("province", list.get(i).getProvince());
                json.accumulate("city", list.get(i).getCity());
                json.accumulate("area", list.get(i).getArea());
                json.accumulate("street", list.get(i).getStreet());
                jsonArray.add(json);
            }

        }
        System.out.printf(String.valueOf(jsonArray));
        resDto.put("garden", jsonArray);
        //返回园区信息包括id和name
        msg.setData(Des3Utils.encResponse(resDto));

        return msg;
    }
}
