package com.dominator.mapper.ext;

import com.dominFramework.core.typewrap.Dto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TPublicExt {

    List<Dto> getOrderList(Dto dto);

}
