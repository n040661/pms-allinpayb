package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TAddressMapper;
import com.dominator.mapper.TSpaceReleaseMapper;
import com.dominator.service.SpaceService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Page;
import com.dominator.utils.system.PrimaryGenerater;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SpaceServiceImpl implements SpaceService {

    @Autowired
    private TSpaceReleaseMapper tSpaceReleaseMapper;

    @Autowired
    private TAddressMapper tAddressMapper;
    /**
     * 增加空间
     * @param dto      token
     *                 gardenId 必传 String 园区id
     *                 name 必传 String 空间名称
     *                 subtitle 必传 String 副标题
     *                 chargePerson 必传 String 负责人
     *                 contactPhone 必传 String 联系电话
     *                 excellent 必传 String 是否为优质,0:非优质 , 1:优质
     *                 tRelease 必传 Stirng 是否启用, 0:启用, 1:停用
     *                 province 必传 String 省
     *                 city    必传 String 市
     *                 area    必传 String 县
     *                 street  必传 String 街道
     *                 detail 必传 String 项目介绍
     *                 pics 必传 String 图片
     *
     * @return
     */
    @Override
    public ApiMessage addSpace(Dto dto) {
        try{
            TSpaceRelease tSpaceRelease = new TSpaceRelease();
            String spaceId = PrimaryGenerater.getInstance().uuid();
            tSpaceRelease.setId(spaceId);           //空间id
            tSpaceRelease.setGardenId(dto.getString("gardenId"));             //园区id
            tSpaceRelease.setSpaceName(dto.getString("name"));     //项目名称
            tSpaceRelease.setSubtitle(dto.getString("subtitle")); //副标题
            tSpaceRelease.setChargePerson(dto.getString("chargePerson"));     //负责人
            tSpaceRelease.setContactPhone(dto.getString("contactPhone"));     //联系电话
            tSpaceRelease.setSpacePics(dto.getString("pics"));           //图片
            tSpaceRelease.setExcellent(dto.getString("excellent"));
            tSpaceRelease.settOperator(dto.getString("userName"));      //操作人

            tSpaceRelease.setModifyTime(new Date());                             //修改时间
            tSpaceRelease.setCreateTime(new Date());                             //创建时间
            tSpaceRelease.setModifyId(dto.getString("userId"));               //修改人id
            tSpaceRelease.setSpaceDescribe(dto.getString("detail"));   //空间介绍
            tSpaceRelease.settRelease(dto.getString("tRelease"));     //是否启用, 0:启用, 1:停用
            tSpaceRelease.setExcellent(dto.getString("excellent")); //是否为优质,0:非优质 , 1:优质
            tSpaceRelease.setIsValid("1");                                    //当前记录是否有效，0：无效，1：有效

            int count = 0;
            count = tSpaceReleaseMapper.insertSelective(tSpaceRelease);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
            TAddress tAddress = new TAddress();
            tAddress.setId(PrimaryGenerater.getInstance().uuid());
            tAddress.setOwnerId(spaceId);
            tAddress.setProvince(dto.getString("province"));
            tAddress.setCity(dto.getString("city"));
            tAddress.setArea(dto.getString("area"));
            tAddress.setStreet(dto.getString("street"));
            tAddress.setIsValid("1");
            tAddress.setType("2");
            count = tAddressMapper.insertSelective(tAddress);
            if (count != 1){
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        return ApiMessageUtil.success();
    }

    /**
     * 空间列表
     * @param dto      token
     *                 pageNum int 非必传 第几页
     *                 pageSize int 非必传 每页几条
     *                 condition String 非必传 查询条件
     */
    @Override
    public ApiMessage listSpace(Dto dto) {
        String condition = dto.getString("condition");
        TSpaceReleaseExample tSpaceReleaseExample = new TSpaceReleaseExample();
        TSpaceReleaseExample.Criteria criteria = tSpaceReleaseExample.createCriteria();
        criteria.andIsValidEqualTo("1");
        if(condition!=null && !condition.isEmpty()){
            criteria.andSpaceNameLike("%"+condition+"%");
        }
        tSpaceReleaseExample.setOrderByClause("t_release,excellent desc");
        Page.startPage(dto);
        List<TSpaceRelease> spaceList = tSpaceReleaseMapper.selectByExample(tSpaceReleaseExample);
        PageInfo<TSpaceRelease> pageInfo = null;
        pageInfo = new PageInfo<>(spaceList);
        Dto resDto = new HashDto();
        JSONArray jsonArray = new JSONArray();
        if(spaceList != null && spaceList.size() > 0){
            for (TSpaceRelease tSpaceRelease : spaceList){
                if(tSpaceRelease!=null){
                    Dto space = new HashDto();
                    space.put("id",tSpaceRelease.getId());
                    space.put("detail",tSpaceRelease.getSpaceDescribe());
                    space.put("name",tSpaceRelease.getSpaceName());
                    space.put("subtitle",tSpaceRelease.getSubtitle());
                    space.put("pics",tSpaceRelease.getSpacePics());
                    space.put("excellent",tSpaceRelease.getExcellent());
                    space.put("tRelease",tSpaceRelease.gettRelease());
                    space.put("operator",tSpaceRelease.gettOperator());
                    space.put("modifyTime",tSpaceRelease.getModifyTime());

                    String spaceId = tSpaceRelease.getId();
                    TAddressExample tAddressExample = new TAddressExample();
                    tAddressExample.or().andOwnerIdEqualTo(spaceId).andIsValidEqualTo("1").andTypeEqualTo("2");
                    List<TAddress> addressList = tAddressMapper.selectByExample(tAddressExample);
                    if (addressList != null && !addressList.isEmpty()){
                        TAddress tAddress = addressList.get(0);
                        if (tAddress!=null){
                            space.put("province",tAddress.getProvince());
                            space.put("city",tAddress.getCity());
                            space.put("area",tAddress.getArea());
                            space.put("street",tAddress.getStreet());
                        }
                    }
                    jsonArray.add(space);
                }

            }
        }


        resDto.put("spaces",jsonArray);
        resDto.put("pageSize", pageInfo.getPageSize());
        resDto.put("pageNum", pageInfo.getPageNum());
        resDto.put("total", pageInfo.getTotal());
        return ApiMessageUtil.success(resDto);

    }
    /**
     * 空间详情
     * @param dto token
     *                 id   必传 String 项目id
     *                 pageNum int 非必传 第几页
     *                 pageSize int 非必传 每页几条
     *
     */
    @Override
    public ApiMessage getSpDetails(Dto dto) {
        String spaceId = dto.getString("id");
        TSpaceRelease tSpaceRelease = tSpaceReleaseMapper.selectByPrimaryKey(dto.getString("id"));
        Dto space = new HashDto();
        space.put("name",tSpaceRelease.getSpaceName());
        space.put("subtitle",tSpaceRelease.getSubtitle());
        space.put("contactPhone",tSpaceRelease.getContactPhone());
        space.put("id",tSpaceRelease.getId());
        space.put("chargePerson",tSpaceRelease.getChargePerson());
        space.put("gardenId",tSpaceRelease.getGardenId());
        space.put("pics",tSpaceRelease.getSpacePics());
        space.put("detail",tSpaceRelease.getSpaceDescribe());
        space.put("tRelease",tSpaceRelease.gettRelease());      //是否启用, 0:启用, 1:停用
        space.put("excellent",tSpaceRelease.getExcellent()); //是否为优质,0:非优质 , 1:优质

        //Dto projectDetail = JsonUtils.toDto(JsonUtils.toJson(tProjects));
        TAddressExample tAddressExample = new TAddressExample();
        tAddressExample.or().andOwnerIdEqualTo(spaceId).andIsValidEqualTo("1").andTypeEqualTo("2");
        List<TAddress> addressList = tAddressMapper.selectByExample(tAddressExample);
        if (addressList != null && !addressList.isEmpty()) {
            //project.put("projectAddress", addressList.get(0));
            TAddress tAddress = addressList.get(0);
            if (tAddress != null) {
                space.put("province", addressList.get(0).getProvince());
                space.put("city", addressList.get(0).getCity());
                space.put("area", addressList.get(0).getArea());
                space.put("street", addressList.get(0).getStreet());
            }
        }
        return ApiMessageUtil.success(space);

    }
    /**
     * 空间编辑  token
     * @param   dto      id 必传 String 项目id
     *                   name 必传 String 项目名称
     *                   subtitle 必传 String 副标题
     *                   tRelease 必传 String 是否启用, 0:启用, 1:停用
     *                   excellent 必传 String 是否为优质,0:非优质 , 1:优质
     *                   chargePerson 必传 String 负责人
     *                   contactPhone 必传 String 联系电话
     *                   detail 必传 String 项目介绍
     *                   pics 必传 String 图片
     *                   province 必传 String 省
     *                   city    必传 String 市
     *                   area    必传 String 县
     *                   street  必传 String 街道
     */
    @Override
    public ApiMessage editSpace (Dto dto){
        try {
            TSpaceRelease tspace = tSpaceReleaseMapper.selectByPrimaryKey(dto.getString("id"));
            String projectId = dto.getString("id");
            //TProjectRelease tProjectRelease = new TProjectRelease();
            //tproject.setId(projectId);
            tspace.setGardenId(dto.getString("gardenId"));             //园区id
            tspace.setSpaceName(dto.getString("name"));     //空间名称
            tspace.setSubtitle(dto.getString("subtitle")); //副标题
            tspace.setChargePerson(dto.getString("chargePerson"));     //负责人
            tspace.setContactPhone(dto.getString("contactPhone"));     //联系电话
            tspace.setAddress(dto.getString("address"));   //地址
            tspace.setSpacePics(dto.getString("pics"));           //图片
            tspace.setModifyTime(new Date());                             //修改时间
            tspace.setModifyId(dto.getString("userId"));               //修改人id
            tspace.settRelease(dto.getString("tRelease"));                                 //是否启用, 0:启用, 1:停用
            tspace.setSpaceDescribe(dto.getString("detail"));       //空间介绍
            tspace.setExcellent(dto.getString("excellent"));        //是否为优质,0:非优质 , 1:优质
            int count = 0;
            count = tSpaceReleaseMapper.updateByPrimaryKeySelective(tspace);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
            TAddressExample addressExample = new TAddressExample();
            addressExample.or().andOwnerIdEqualTo(projectId).andIsValidEqualTo("1").andTypeEqualTo("2");
            TAddress tAddress = new TAddress();
            tAddress.setProvince(dto.getString("province"));
            tAddress.setCity(dto.getString("city"));
            tAddress.setArea(dto.getString("area"));
            tAddress.setStreet(dto.getString("street"));
            count = tAddressMapper.updateByExampleSelective(tAddress,addressExample);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        return ApiMessageUtil.success();
    }

    /**
     * 停用启用      token
     * @param   dto      id 必传 String 项目id
     *                   tRelease 必传 String 是否启用, 0:启用, 1:停用
     */
    @Override
    public ApiMessage releaseSpace(Dto dto){
        try {
            TSpaceRelease tSpaceRelease = tSpaceReleaseMapper.selectByPrimaryKey(dto.getString("id"));
            if (dto.getString("tRelease").equals("1")) {
                tSpaceRelease.settRelease("0");                                 //是否启用, 0:启用, 1:停用
            } else if (dto.getString("tRelease").equals("0")) {
                tSpaceRelease.settRelease("1");
            }

            int count = 0;
            count = tSpaceReleaseMapper.updateByPrimaryKeySelective(tSpaceRelease);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }

        return ApiMessageUtil.success();
    }
    @Override
    public ApiMessage deleteSpace(Dto dto) {
        try{
            TSpaceRelease tspace = tSpaceReleaseMapper.selectByPrimaryKey(dto.getString("id"));
            tspace.setIsValid("0");
            int count = 0;
            count = tSpaceReleaseMapper.updateByPrimaryKeySelective(tspace);
            if(count !=1){
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
        }catch(Exception e){
            e.printStackTrace();
                                                                                                                                                                                                                                             throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        return ApiMessageUtil.success();
    }

    @Override
    public ApiMessage exNum(Dto dto) {
            TSpaceReleaseExample tSpaceReleaseExample = new TSpaceReleaseExample();
            tSpaceReleaseExample.or().andExcellentEqualTo("1").andIsValidEqualTo("1");
            int count = tSpaceReleaseMapper.countByExample(tSpaceReleaseExample);
            Dto resDto = new HashDto();
            resDto.put("exNum",count);
            tSpaceReleaseExample.clear();
            tSpaceReleaseExample.or().andTReleaseEqualTo("0").andIsValidEqualTo("1");
            int count1 = tSpaceReleaseMapper.countByExample(tSpaceReleaseExample);
            resDto.put("reNum",count1);
            return ApiMessageUtil.success(resDto);
    }

}
