package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TBanner;
import com.dominator.entity.TBannerExample;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TBannerMapper;
import com.dominator.service.BannerService;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.PrimaryGenerater;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private TBannerMapper tBannerMapper;

    @Override
    @Transactional
    public void postBanner(Dto dto) throws ApiException{
        String propertyId = dto.getString("propertyId");
        String url = dto.getString("url");
        int sort = dto.getInteger("sort");
        String type = dto.getString("type");
        int right = 0;
        int count;

        if (type.equals("1")) {
            right = 2;
        } else if (type.equals("3")) {
            right = 1;
        }
        try {
            TBanner banner = new TBanner();
            banner.setId(PrimaryGenerater.getInstance().uuid());
            banner.setPropertyId(propertyId);
            banner.setUrl(url);
            banner.setSort(sort);
            banner.setPriority(right);
            banner.setCreateTime(new Date());
            banner.setIsValid("1");
            count = tBannerMapper.insertSelective(banner);
            if (count != 1)
                throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);

            TBannerExample bannerExample = new TBannerExample();
            bannerExample.or().andPropertyIdEqualTo(propertyId)
                    .andSortEqualTo(sort)
                    .andPriorityGreaterThan(right)
                    .andIsValidEqualTo("1");
            banner = new TBanner();
            banner.setIsValid("0");
            tBannerMapper.updateByExampleSelective(banner, bannerExample);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR);
        }
    }

    @Override
    public List<TBanner> getBanner(Dto dto) {
        String[] sorts = dto.getString("sorts").split(",");
        String propertyId = dto.getString("propertyId");
        List<Integer> list = new ArrayList<>();
        for(String str : sorts)
            list.add(Integer.valueOf(str));

        TBannerExample bannerExample = new TBannerExample();
        bannerExample.or().andPropertyIdEqualTo(propertyId)
                .andSortIn(list)
                .andIsValidEqualTo("1");
        return tBannerMapper.selectByExample(bannerExample);
    }
}
