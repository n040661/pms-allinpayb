package com.dominator.mapper;

import com.dominator.entity.TblWxopenComponentVerifyTicket;
import com.dominator.entity.TblWxopenComponentVerifyTicketExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblWxopenComponentVerifyTicketMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int countByExample(TblWxopenComponentVerifyTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int deleteByExample(TblWxopenComponentVerifyTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insert(TblWxopenComponentVerifyTicket record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int insertSelective(TblWxopenComponentVerifyTicket record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    List<TblWxopenComponentVerifyTicket> selectByExample(TblWxopenComponentVerifyTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    TblWxopenComponentVerifyTicket selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExampleSelective(@Param("record") TblWxopenComponentVerifyTicket record, @Param("example") TblWxopenComponentVerifyTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByExample(@Param("record") TblWxopenComponentVerifyTicket record, @Param("example") TblWxopenComponentVerifyTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByPrimaryKeySelective(TblWxopenComponentVerifyTicket record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_wxopen_component_verify_ticket
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    int updateByPrimaryKey(TblWxopenComponentVerifyTicket record);
}