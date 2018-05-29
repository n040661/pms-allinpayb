package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TGarden;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {

    /**
     * 上传图片
     *
     * @param multipartFile
     * @return
     * @throws ApiException
     */
    ApiMessage uploadPicture(MultipartFile multipartFile) throws ApiException;


    ApiMessage uploadExcelRead(MultipartFile file) throws ApiException;

    /**
     * 将上传Excel信息保存到数据库
     *
     * @param dto 1.name=companyuser 传参如下
     *            company_id String 必传 公司id
     *            modify_id String 必传 当前登录用户id
     *            name String 必传 导入内容名称
     *            data String 必传 预览excel时，返回的json数据
     *            <p>
     *            2.name=gardenuser 传参如下 3.name=company 传参如下
     *            garden_id String 必传 园区id
     *            modify_id String 必传 当前登录用户id
     *            name String 必传 导入内容名称
     *            data String 必传 预览excel时，返回的json数据
     *            <p>
     *            4.name=gas/power/water/rent传参如下
     *            company_id String 必传 企业id
     *            modify_id String 必传 当前登录用户id
     *            push_time 必传 推送时间
     *            name String 必传 导入内容名称
     *            data String 必传 预览excel时，返回的json数据
     * @return
     */
    ApiMessage saveExcel2Database(Dto dto) throws ApiException;

    /**
     * 导出excel
     *
     * @param dto 1.企业员工列表
     *            type = companyuser
     *            company_id String 必传 企业id
     *            status String 必传 状态：0离职，1在职，2全部
     *            2.园区员工列表
     *            type = gardenuser
     *            garden_id String 必传 园区id
     *            status String 必传 状态：0离职，1在职，2全部
     *            3.园区端/企业端--账单列表
     *            type = unpaidbills/paidbills
     *            garden_id String 园区id (园区端必传）
     *            company_id String 企业id (企业端必传）
     *            fee_type String 非必传 费用类型 0水 1电费 2煤气 3房租物业
     *            startTime String 非必传 开始时间（应缴日期）
     *            endTime String 非必传 结束时间（应缴日期）
     *            4.访客列表
     *            type = gardenvisitors/companyvisitors
     *            garden_id／company_id 必传
     *            status String 非必传 访客类型 0待访问 1已访问 2已过期
     *            startTime String 非必传 开始时间
     *            endTime String 非必传 结束时间
     *            5.某园区的企业列表
     *            type=company
     *            garden_id 必传
     * @return
     */
    ApiMessage export(Dto dto);

    /**
     * 获取菜单列表
     *
     * @param dto 1.企业端
     *            parent_id String 必传 父菜单id, 一级菜单传 root
     *            modules_type String 必传 模块类型 0 h5, 1园区garden管理后台,2企业company管理后台，3其他
     * @return
     */
    ApiMessage listModules(Dto dto);

    /**
     * 注册企业管理员 company_id String 必传
     * nick_name String 必传
     * phone_num String 必传
     *
     * @param dto
     */
    ApiMessage registerCompanyManager(Dto dto) throws ApiException;

    /**
     * @param dto garden_name 非必传
     *            company_name 非必传
     *            bill_num 非必传
     * @return
     */
    ApiMessage pushBill(Dto dto) throws ApiException;
}
