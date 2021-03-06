package com.dominator.mapper;

import com.dominator.entity.TAppointPublicRecord;
import com.dominator.entity.TAppointPublicRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface TAppointPublicRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    int countByExample(TAppointPublicRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    int deleteByExample(TAppointPublicRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    int insert(TAppointPublicRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    int insertSelective(TAppointPublicRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    List<TAppointPublicRecord> selectByExample(TAppointPublicRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    TAppointPublicRecord selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    int updateByExampleSelective(@Param("record") TAppointPublicRecord record, @Param("example") TAppointPublicRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    int updateByExample(@Param("record") TAppointPublicRecord record, @Param("example") TAppointPublicRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    int updateByPrimaryKeySelective(TAppointPublicRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_appoint_public_record
     *
     * @mbggenerated Tue May 22 09:42:45 CST 2018
     */
    int updateByPrimaryKey(TAppointPublicRecord record);
}