package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TAddressMapper;
import com.dominator.mapper.TProjectReleaseMapper;
import com.dominator.service.ProjectService;
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
import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private TProjectReleaseMapper tProjectReleaseMapper;

    @Autowired
    private TAddressMapper tAddressMapper;

    /**
     * 增加项目
     * @param dto       token
     *                 name 必传 String 项目名称
     *                 subtitle 必传 String 副标题
     *                 detail 必传 String 项目介绍
     *                 province 非必传 String 省
     *                 city    非必传 String 市
     *                 area    非必传 String 县
     *                 street  非必传 String 街道
     *                 chargePerson 非必传 String 负责人
     *                 contactPhone 非必传 String 联系电话
     *                 pics 非必传 String 图片
     * @return
     */
    @Override
    public ApiMessage addProject(Dto dto) {
        try {
            TProjectRelease tProjectRelease = new TProjectRelease();
            TAddress tAddress = new TAddress();
            String projectId = PrimaryGenerater.getInstance().uuid();
            tProjectRelease.setId(projectId);           //id
            tProjectRelease.setGardenId(dto.getString("gardenId"));             //园区id
            tProjectRelease.setPjName(dto.getString("name"));     //项目名称
            tProjectRelease.setSubtitle(dto.getString("subtitle")); //副标题
            tProjectRelease.setChargePerson(dto.getString("chargePerson"));     //负责人
            tProjectRelease.setContactPhone(dto.getString("contactPhone"));     //联系电话
            tProjectRelease.setAddress(dto.getString("address"));   //地址
            tProjectRelease.setProjectPics(dto.getString("pics"));           //图片

            tProjectRelease.setModifyTime(new Date());                             //修改时间
            tProjectRelease.setCreateTime(new Date());                             //创建时间
            tProjectRelease.settOperator(dto.getString("userName"));               //操作人
            tProjectRelease.settRelease("1");                                 //是否启用, 0:启用, 1:停用
            tProjectRelease.setProjectDescribe(dto.getString("detail"));       //项目介绍
            tProjectRelease.setIsValid("1");                                    //当前记录是否有效，0：无效，1：有效


            int count = 0;
            count = tProjectReleaseMapper.insertSelective(tProjectRelease);
            if (count != 1) {
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
            }
            tAddress.setId(PrimaryGenerater.getInstance().uuid());
            tAddress.setOwnerId(projectId);
            tAddress.setProvince(dto.getString("province"));
            tAddress.setCity(dto.getString("city"));
            tAddress.setArea(dto.getString("area"));
            tAddress.setStreet(dto.getString("street"));
            tAddress.setIsValid("1");
            tAddress.setType("2");

            count = tAddressMapper.insertSelective(tAddress);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
        return ApiMessageUtil.success();
    }

    /**
     * 项目列表
     *
     * @param dto token
     *            pageNum int 非必传 第几页
     *            pageSize int 非必传 每页几条
     *            condition 非必传 查询条件
     */
    @Override
    public ApiMessage listProject(Dto dto) {
        String condition = dto.getString("condition");
        TProjectReleaseExample tProjectReleaseExample = new TProjectReleaseExample();
        TProjectReleaseExample.Criteria criteria = tProjectReleaseExample.createCriteria();
        criteria.andIsValidEqualTo("1");
        if(condition!=null && !condition.isEmpty()){
            criteria.andPjNameLike("%"+condition+"%");
        }
        Page.startPage(dto);
        List<TProjectRelease> projects = tProjectReleaseMapper.selectByExample(tProjectReleaseExample);
        PageInfo<TProjectRelease> pageInfo = null;
        pageInfo = new PageInfo<>(projects);
        Dto resDto = new HashDto();
        JSONArray jsonArray = new JSONArray();
        if (projects != null && projects.size() > 0) {
            for (TProjectRelease tProject : projects) {
                if (tProject != null) {
                    Dto project = new HashDto();
                    project.put("id", tProject.getId());
                    project.put("detail", tProject.getProjectDescribe());
                    project.put("operator",tProject.gettOperator());
                    project.put("name", tProject.getPjName());
                    project.put("subtitle", tProject.getSubtitle());
                    project.put("pics", tProject.getProjectPics());
                    project.put("modifyTime",tProject.getModifyTime());
                    project.put("tRelease",tProject.gettRelease());
                    String projectId = tProject.getId();
                    TAddressExample tAddressExample = new TAddressExample();
                    tAddressExample.or().andOwnerIdEqualTo(projectId).andIsValidEqualTo("1").andTypeEqualTo("2");
                    List<TAddress> addressList = tAddressMapper.selectByExample(tAddressExample);
                    if (addressList != null && !addressList.isEmpty()) {
                        //project.put("projectAddress", addressList.get(0));
                        TAddress tAddress = addressList.get(0);
                        if (tAddress != null) {
                            project.put("province", addressList.get(0).getProvince());
                            project.put("city", addressList.get(0).getCity());
                            project.put("area", addressList.get(0).getArea());
                            project.put("street", addressList.get(0).getStreet());
                        }


                    }
                    jsonArray.add(project);
                }
            }
            resDto.put("jsonArray", jsonArray);
            resDto.put("pageSize", pageInfo.getPageSize());
            resDto.put("pageNum", pageInfo.getPageNum());
            resDto.put("total", pageInfo.getTotal());
        }
        return ApiMessageUtil.success(resDto);
    }
        /**
         * 项目详情
         * @param dto token
         *                 id   必传 String 项目id
         *                 pageNum int 非必传 第几页
         *                 pageSize int 非必传 每页几条
         *
         */
        @Override
        public ApiMessage getPjDetails (Dto dto){
            String projectId = dto.getString("id");
            TProjectRelease tProjects = tProjectReleaseMapper.selectByPrimaryKey(dto.getString("id"));
            Dto project = new HashDto();
            project.put("name",tProjects.getPjName());
            project.put("subtitle",tProjects.getSubtitle());
            project.put("contactPhone",tProjects.getContactPhone());
            project.put("id",tProjects.getId());
            project.put("chargePerson",tProjects.getChargePerson());
            project.put("gardenId",tProjects.getGardenId());
            project.put("pics",tProjects.getProjectPics());
            project.put("detail",tProjects.getProjectDescribe());
            //Dto projectDetail = JsonUtils.toDto(JsonUtils.toJson(tProjects));
            TAddressExample tAddressExample = new TAddressExample();
            tAddressExample.or().andOwnerIdEqualTo(projectId).andIsValidEqualTo("1").andTypeEqualTo("2");
            List<TAddress> addressList = tAddressMapper.selectByExample(tAddressExample);
            if (addressList != null && !addressList.isEmpty()) {
                //project.put("projectAddress", addressList.get(0));
                TAddress tAddress = addressList.get(0);
                if (tAddress != null) {
                    project.put("province", addressList.get(0).getProvince());
                    project.put("city", addressList.get(0).getCity());
                    project.put("area", addressList.get(0).getArea());
                    project.put("street", addressList.get(0).getStreet());
                }
            }
            return ApiMessageUtil.success(project);

        }

        /**
         * 项目编辑  token
         * @param   dto     id 必传 String 项目id
         *                  name 必传 String 项目名称
         *                  subtitle 必传 String 副标题
         *                  detail 必传 String 项目介绍
         *                  province 非必传 String 省
         *                  city    非必传 String 市
         *                  area    非必传 String 县
         *                  street  非必传 String 街道
         *                  chargePerson 非必传 String 负责人
         *                  contactPhone 非必传 String 联系电话
         *                  pics 非必传 String 图片
         */
        @Override
        public ApiMessage editProject (Dto dto){
            try {
                TProjectRelease tproject = tProjectReleaseMapper.selectByPrimaryKey(dto.getString("id"));
                String projectId = dto.getString("id");
                //TProjectRelease tProjectRelease = new TProjectRelease();
                //tproject.setId(projectId);
                tproject.setGardenId(dto.getString("gardenId"));             //园区id
                tproject.setPjName(dto.getString("name"));     //项目名称
                tproject.setSubtitle(dto.getString("subtitle")); //副标题
                tproject.setChargePerson(dto.getString("chargePerson"));     //负责人
                tproject.setContactPhone(dto.getString("contactPhone"));     //联系电话
                tproject.setAddress(dto.getString("address"));   //地址
                tproject.setProjectPics(dto.getString("pics"));           //图片
                tproject.setModifyTime(new Date());                             //修改时间
                tproject.setModifyId(dto.getString("userName"));               //修改人id
                tproject.setProjectDescribe(dto.getString("detail"));       //项目介绍
                int count = 0;
                count = tProjectReleaseMapper.updateByPrimaryKeySelective(tproject);
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
         * @param   dto id 必传 String 项目id
         *              tRelease 必传 String 是否启用, 0:启用, 1:停用
         */
        @Override
        public ApiMessage releaseProject(Dto dto){
            try {
                TProjectRelease tproject = tProjectReleaseMapper.selectByPrimaryKey(dto.getString("id"));
                if (dto.getString("tRelease").equals("1")) {
                    tproject.settRelease("0");                                 //是否启用, 0:启用, 1:停用
                } else if (dto.getString("tRelease").equals("0")) {
                    tproject.settRelease("1");
                }

                int count = 0;
                count = tProjectReleaseMapper.updateByPrimaryKeySelective(tproject);
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
    public ApiMessage deleteProject(Dto dto) {
            try {
                TProjectRelease tProject = tProjectReleaseMapper.selectByPrimaryKey(dto.getString("id"));
                tProject.setIsValid("0");
                int count = 0;
                count = tProjectReleaseMapper.updateByPrimaryKeySelective(tProject);
                if (count != 1) {
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
        TProjectReleaseExample tProjectReleaseExample = new TProjectReleaseExample();
        tProjectReleaseExample.or().andTReleaseEqualTo("0").andIsValidEqualTo("1");
        Dto resDto = new HashDto();
        int count = tProjectReleaseMapper.countByExample(tProjectReleaseExample);
        resDto.put("reNum",count);
        return ApiMessageUtil.success(resDto);
    }


}
