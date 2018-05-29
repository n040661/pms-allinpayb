package com.dominator.app.service.impl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.app.service.SpaceService;
import com.dominator.entity.*;
import com.dominator.mapper.TAddressMapper;
import com.dominator.mapper.TSpaceReleaseMapper;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.system.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("app.SpaceServiceImpl")
public class SpaceServiceImpl implements SpaceService {

    @Autowired
    private TSpaceReleaseMapper tSpaceReleaseMapper;

    @Autowired
    private TAddressMapper tAddressMapper;

    @Override
    public ApiMessage spaceDetail(Dto dto) {
        String spaceId = dto.getString("spaceId");
        TSpaceRelease space = tSpaceReleaseMapper.selectByPrimaryKey(spaceId);
        Dto spacetDetail  = new HashDto();
        if(space!=null){
            spacetDetail.put("name",space.getSpaceName());  //空间名称
            spacetDetail.put("subtitle",space.getSubtitle()); //副标题
            spacetDetail.put("chargePerson",space.getChargePerson()); //负责人
            spacetDetail.put("contactPhone",space.getContactPhone()); //联系电话
            spacetDetail.put("excellent",space.getExcellent()); //是否为优质,0:非优质 , 1:优质
//            spacetDetail.put("address",space.getAddress()); //地址
            spacetDetail.put("pics",space.getSpacePics()); //图片
            TAddressExample tAddressExample = new TAddressExample();
            tAddressExample.or().andOwnerIdEqualTo(spaceId).andIsValidEqualTo("1");
            List<TAddress> addressList = tAddressMapper.selectByExample(tAddressExample);
            if (addressList != null && !addressList.isEmpty()){
                //project.put("projectAddress", addressList.get(0));
                TAddress tAddress = addressList.get(0);
                if (tAddress!= null){
                    spacetDetail.put("province",addressList.get(0).getProvince());
                    spacetDetail.put("city",addressList.get(0).getCity());
                    spacetDetail.put("area",addressList.get(0).getArea());
                    spacetDetail.put("street",addressList.get(0).getStreet());
                }
            }
            spacetDetail.put("detail",space.getSpaceDescribe()); //空间详情
        }
        return ApiMessageUtil.success(spacetDetail);
    }

    @Override
    public ApiMessage listSpace(Dto dto) {
        String sort = dto.getString("sort");
        String field = dto.getString("field");
        String excellent = dto.getString("excellent");

        TSpaceReleaseExample tSpaceReleaseExample = new TSpaceReleaseExample();
        if(excellent.equals("1")){
            tSpaceReleaseExample.or().andExcellentEqualTo("1").andTReleaseEqualTo("0").andIsValidEqualTo("1");
        }else{
            tSpaceReleaseExample.or().andExcellentEqualTo("0").andTReleaseEqualTo("0").andIsValidEqualTo("1");
        }
        //tSpaceReleaseExample.setOrderByClause(field +" "+ sort);
        Page.startPage(dto);
        List<TSpaceRelease> spaces = tSpaceReleaseMapper.selectByExample(tSpaceReleaseExample);
        PageInfo<TSpaceRelease> pageInfo;
        pageInfo = new PageInfo<>(spaces);
        Dto resDto = new HashDto();
        JSONArray jsonArray = new JSONArray();
        if(spaces != null && spaces.size()>0){
            for(TSpaceRelease tspace : spaces){
                if(tspace!=null){
                    Dto space = new HashDto();
                    String spacetId = tspace.getId();
                    space.put("id",spacetId);
                    space.put("detail",tspace.getSpaceDescribe());

                    space.put("name",tspace.getSpaceName());
                    space.put("subtitle",tspace.getSubtitle());
                    space.put("pics",tspace.getSpacePics());
                    TAddressExample tAddressExample = new TAddressExample();
                    tAddressExample.or().andOwnerIdEqualTo(spacetId).andIsValidEqualTo("1");
                    List<TAddress> addressList = tAddressMapper.selectByExample(tAddressExample);
                    if (addressList != null && !addressList.isEmpty()){
                        //project.put("projectAddress", addressList.get(0));
                        TAddress tAddress = addressList.get(0);
                        if (tAddress!= null){
                            space.put("province",addressList.get(0).getProvince());
                            space.put("city",addressList.get(0).getCity());
                            space.put("area",addressList.get(0).getArea());
                            space.put("street",addressList.get(0).getStreet());
                        }


                    }
                    jsonArray.add(space);
                }
            }
            resDto.put("jsonArray",jsonArray);
            resDto.put("pageSize", pageInfo.getPageSize());
            resDto.put("pageNum", pageInfo.getPageNum());
            resDto.put("total", pageInfo.getTotal());
        }
        return ApiMessageUtil.success(resDto);
    }

}
