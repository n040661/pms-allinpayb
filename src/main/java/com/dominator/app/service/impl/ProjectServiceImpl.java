package com.dominator.app.service.impl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.app.service.ProjectService;
import com.dominator.entity.*;
import com.dominator.mapper.TAddressMapper;
import com.dominator.mapper.TProjectReleaseMapper;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.system.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dominator.utils.dateutil.DateUtil.DatetoStringFormat;

@Service("app.ProjectServiceImpl")
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private TProjectReleaseMapper tProjectReleaseMapper;

    @Autowired
    private TAddressMapper tAddressMapper;

    @Override
    public ApiMessage projectDetail(Dto dto) {
        String projectId = dto.getString("projectId");
        TProjectRelease project = tProjectReleaseMapper.selectByPrimaryKey(projectId);
        Dto projectDetail  = new HashDto();
        if(project!=null){
            projectDetail.put("name",project.getPjName());  //项目名称
            projectDetail.put("subtitle",project.getSubtitle()); //副标题
            projectDetail.put("chargePerson",project.getChargePerson()); //负责人
            projectDetail.put("contactPhone",project.getContactPhone()); //联系电话
            //projectDetail.put("address",project.getAddress()); //地址

            TAddressExample tAddressExample = new TAddressExample();
            tAddressExample.or().andOwnerIdEqualTo(projectId).andIsValidEqualTo("1");
            List<TAddress> addressList = tAddressMapper.selectByExample(tAddressExample);
            if (addressList != null && !addressList.isEmpty()){
                //project.put("projectAddress", addressList.get(0));
                TAddress tAddress = addressList.get(0);
                if (tAddress!= null){
                    projectDetail.put("province",addressList.get(0).getProvince());
                    projectDetail.put("city",addressList.get(0).getCity());
                    projectDetail.put("area",addressList.get(0).getArea());
                    projectDetail.put("street",addressList.get(0).getStreet());
                }
            }
            projectDetail.put("detail",project.getProjectDescribe()); //项目详情
            String pics = project.getProjectPics();
            projectDetail.put("pics",pics);
        }
        return ApiMessageUtil.success(projectDetail);
    }

    @Override
    public ApiMessage listProject(Dto dto) {
        String sort = dto.getString("sort");
        String field = dto.getString("field");
        TProjectReleaseExample tProjectReleaseExample = new TProjectReleaseExample();
        tProjectReleaseExample.or().andTReleaseEqualTo("0").andIsValidEqualTo("1");
        if (StringUtils.isNotEmpty(sort) && StringUtils.isNotEmpty(field))
            tProjectReleaseExample.setOrderByClause(field +" "+ sort);
        Page.startPage(dto);
        List<TProjectRelease> projects = tProjectReleaseMapper.selectByExample(tProjectReleaseExample);
        PageInfo<TProjectRelease> pageInfo = null;
        pageInfo = new PageInfo<>(projects);
        Dto resDto = new HashDto();
        JSONArray jsonArray = new JSONArray();
        if(projects != null && projects.size()>0){
            for(TProjectRelease tProject : projects){
                if(tProject!=null){
                    Dto project = new HashDto();
                    project.put("id",tProject.getId());
                    project.put("detail",tProject.getProjectDescribe());

                    project.put("name",tProject.getPjName());
                    project.put("subtitle",tProject.getSubtitle());
                    project.put("pics",tProject.getProjectPics());
                    String projectId = tProject.getId();
                    TAddressExample tAddressExample = new TAddressExample();
                    tAddressExample.or().andOwnerIdEqualTo(projectId).andIsValidEqualTo("1");
                    List<TAddress> addressList = tAddressMapper.selectByExample(tAddressExample);
                    if (addressList != null && !addressList.isEmpty()){
                        //project.put("projectAddress", addressList.get(0));
                        TAddress tAddress = addressList.get(0);
                        if (tAddress!= null){
                            project.put("province",addressList.get(0).getProvince());
                            project.put("city",addressList.get(0).getCity());
                            project.put("area",addressList.get(0).getArea());
                            project.put("street",addressList.get(0).getStreet());
                        }


                    }
                    jsonArray.add(project);
                }
            }
            resDto.put("jsonArray",jsonArray);
            resDto.put("pageSize",pageInfo.getPageSize());
            resDto.put("pageNum",pageInfo.getPageNum());
            resDto.put("total",pageInfo.getTotal());
            /*resDto.put("pageSize", pageInfo.getPageSize());
            resDto.put("pageNum", pageInfo.getPageNum());
            resDto.put("total", pageInfo.getTotal());*/
        }
        return ApiMessageUtil.success(resDto);
    }
}
