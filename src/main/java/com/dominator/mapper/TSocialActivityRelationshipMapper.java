package com.dominator.mapper;

import com.dominator.entity.TSocialActivityRelationship;
import com.dominator.entity.TSocialActivityRelationshipExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface TSocialActivityRelationshipMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    int countByExample(TSocialActivityRelationshipExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    int deleteByExample(TSocialActivityRelationshipExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    int insert(TSocialActivityRelationship record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    int insertSelective(TSocialActivityRelationship record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    List<TSocialActivityRelationship> selectByExample(TSocialActivityRelationshipExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    TSocialActivityRelationship selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    int updateByExampleSelective(@Param("record") TSocialActivityRelationship record, @Param("example") TSocialActivityRelationshipExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    int updateByExample(@Param("record") TSocialActivityRelationship record, @Param("example") TSocialActivityRelationshipExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    int updateByPrimaryKeySelective(TSocialActivityRelationship record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_social_activity_relationship
     *
<<<<<<< Updated upstream
     * @mbggenerated Thu May 03 11:37:14 CST 2018
=======
     * @mbggenerated Fri Apr 27 18:41:37 CST 2018
>>>>>>> Stashed changes
     */
    int updateByPrimaryKey(TSocialActivityRelationship record);
}