package com.dominator.entity;

import java.util.ArrayList;
import java.util.List;

public class TblYeeUnlockRecordExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public TblYeeUnlockRecordExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUnlockAtIsNull() {
            addCriterion("unlock_at is null");
            return (Criteria) this;
        }

        public Criteria andUnlockAtIsNotNull() {
            addCriterion("unlock_at is not null");
            return (Criteria) this;
        }

        public Criteria andUnlockAtEqualTo(String value) {
            addCriterion("unlock_at =", value, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtNotEqualTo(String value) {
            addCriterion("unlock_at <>", value, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtGreaterThan(String value) {
            addCriterion("unlock_at >", value, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtGreaterThanOrEqualTo(String value) {
            addCriterion("unlock_at >=", value, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtLessThan(String value) {
            addCriterion("unlock_at <", value, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtLessThanOrEqualTo(String value) {
            addCriterion("unlock_at <=", value, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtLike(String value) {
            addCriterion("unlock_at like", value, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtNotLike(String value) {
            addCriterion("unlock_at not like", value, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtIn(List<String> values) {
            addCriterion("unlock_at in", values, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtNotIn(List<String> values) {
            addCriterion("unlock_at not in", values, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtBetween(String value1, String value2) {
            addCriterion("unlock_at between", value1, value2, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUnlockAtNotBetween(String value1, String value2) {
            addCriterion("unlock_at not between", value1, value2, "unlockAt");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andLockTypeIsNull() {
            addCriterion("lock_type is null");
            return (Criteria) this;
        }

        public Criteria andLockTypeIsNotNull() {
            addCriterion("lock_type is not null");
            return (Criteria) this;
        }

        public Criteria andLockTypeEqualTo(String value) {
            addCriterion("lock_type =", value, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeNotEqualTo(String value) {
            addCriterion("lock_type <>", value, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeGreaterThan(String value) {
            addCriterion("lock_type >", value, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeGreaterThanOrEqualTo(String value) {
            addCriterion("lock_type >=", value, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeLessThan(String value) {
            addCriterion("lock_type <", value, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeLessThanOrEqualTo(String value) {
            addCriterion("lock_type <=", value, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeLike(String value) {
            addCriterion("lock_type like", value, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeNotLike(String value) {
            addCriterion("lock_type not like", value, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeIn(List<String> values) {
            addCriterion("lock_type in", values, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeNotIn(List<String> values) {
            addCriterion("lock_type not in", values, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeBetween(String value1, String value2) {
            addCriterion("lock_type between", value1, value2, "lockType");
            return (Criteria) this;
        }

        public Criteria andLockTypeNotBetween(String value1, String value2) {
            addCriterion("lock_type not between", value1, value2, "lockType");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameIsNull() {
            addCriterion("grant_user_name is null");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameIsNotNull() {
            addCriterion("grant_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameEqualTo(String value) {
            addCriterion("grant_user_name =", value, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameNotEqualTo(String value) {
            addCriterion("grant_user_name <>", value, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameGreaterThan(String value) {
            addCriterion("grant_user_name >", value, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("grant_user_name >=", value, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameLessThan(String value) {
            addCriterion("grant_user_name <", value, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameLessThanOrEqualTo(String value) {
            addCriterion("grant_user_name <=", value, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameLike(String value) {
            addCriterion("grant_user_name like", value, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameNotLike(String value) {
            addCriterion("grant_user_name not like", value, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameIn(List<String> values) {
            addCriterion("grant_user_name in", values, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameNotIn(List<String> values) {
            addCriterion("grant_user_name not in", values, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameBetween(String value1, String value2) {
            addCriterion("grant_user_name between", value1, value2, "grantUserName");
            return (Criteria) this;
        }

        public Criteria andGrantUserNameNotBetween(String value1, String value2) {
            addCriterion("grant_user_name not between", value1, value2, "grantUserName");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated do_not_delete_during_merge Sun Apr 22 22:34:38 CST 2018
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table tbl_yee_unlock_record
     *
     * @mbggenerated Sun Apr 22 22:34:38 CST 2018
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}