package com.dominator.service;

import com.dominFramework.core.typewrap.Dto;
import com.dominator.entity.TblWxopenAuthorizerAccountInfo;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;

import java.util.List;

public interface PropertyService {


    int checkPropertyName(String propertyName, String propertyId);

    void postProperty(Dto dto) throws ApiException;

    void putProperty(Dto dto) throws ApiException;

    void delProperty(Dto dto) throws ApiException;

    ApiMessage addGardenToManager(Dto dto) throws ApiException;

    ApiMessage getGardenToManager(Dto dto);

    /**
     * 园区端--获取园区基本信息，微信公众号列表，管理员
     *
     * @param dto garden_id String 必传 园区id
     *            flag String 必传 列表状态：0只传ID，1详细信息
     * @return
     */
    ApiMessage getGardenBaseInfo(Dto dto);

    /**
     * 园区端--获取园区管理员列表
     *
     * @param dto property_id String 必传 园区id
     * @return
     */
    ApiMessage listManagers(Dto dto);

    ApiMessage getManager(Dto dto);

    /**
     * 园区端--园区端--添加/修改园区管理员
     *
     * @param dto opt=add/update String 必传 执行添加或更新操作
     *            1.add
     *            property_id String 必传 物业id
     *            nick_name String 必传 管理员名称
     *            user_name String 必传 管理员账号
     *            password String 必传 密码
     *            phone_num String 必传 手机号
     *            2.update
     *            property_id String 必传 物业id
     *            user_id String 必传 待修改用户id
     *            modify_id 必传 当前登录用户id
     *            nick_name String 必传 管理员名称
     *            password String 必传 密码
     *            phone_num String 必传 手机号
     * @return
     * @throws ApiException
     */
    ApiMessage addManager(Dto dto) throws ApiException;

    /**
     * 删除管理员信息
     *
     * @param dto property_user_id String 必传 物业用户关系表id
     *            modify_id 必传 当前登录用户id
     * @return
     */
    ApiMessage delManager(Dto dto);

    /**
     * 保存添加园区信息
     *
     * @param dto property_id String 必传 物业id
     *            garden_name String 必传 园区名
     *            province String 必传 省
     *            city String 必传 市
     *            area String 必传 区县
     *            street String 必传 街道详细地址
     *            telephone String 必传 座机号
     *            collection_unit String 必传 收款单位
     *            bank_name String 必传 开户银行
     *            bank_num String 必传 银行账号
     *            wechat_ids List 必传 关联园区的微信id列表
     *            user_id String 非必传 管理员id
     * @return
     * @throws ApiException
     */
    ApiMessage saveGardenInfo(Dto dto) throws ApiException;

    /**
     * 修改园区信息
     *
     * @param dto property_id String 必传 物业id
     *            garden_id String 必传 园区id
     *            modify_id String 必传 当前登录用户id
     *            garden_name String 必传 园区名
     *            province String 必传 省
     *            city String 必传 市
     *            area String 必传 区县
     *            street String 必传 街道详细地址
     *            telephone String 必传 座机号
     *            collection_unit String 必传 收款单位
     *            bank_name String 必传 开户银行
     *            bank_num String 必传 银行账号
     *            wechat_ids List 必传 关联园区的微信id列表
     *            user_id String 非必传 管理员id
     * @return
     */
    ApiMessage updateGardenInfo(Dto dto) throws ApiException;

    /**
     * 园区端--删除园区
     *
     * @param dto garden_id String 必传
     *            modify_id String 必传 当前登录用户id
     * @return
     */
    ApiMessage delGarden(Dto dto) throws ApiException;

    /**
     * 园区端--恢复园区
     *
     * @param dto garden_id String 必传
     *            modify_id String 必传 当前登录用户id
     * @return
     */
    ApiMessage recoverGarden(Dto dto) throws ApiException;

    ApiMessage listProperties(Dto dto);

    Dto getProperty(Dto dto);

    /**
     * 获取物业公司的支付方式
     * @param dto   property_id 物业公司ID
     * @return
     */
    ApiMessage getPayType(Dto dto);
}
