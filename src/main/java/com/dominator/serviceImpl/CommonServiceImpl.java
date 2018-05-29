package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.entity.*;
import com.dominator.enums.MessageEnums;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.*;
import com.dominator.mapper.ext.TCommonExt;
import com.dominator.service.CommonService;
import com.dominator.service.CompanyService;
import com.dominator.service.UserService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.encode.Des3Utils;
import com.dominator.utils.excel.ExcelExportor;
import com.dominator.utils.excel.ReadExcel;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.qingyun.QCloudUploadFile;
import com.dominator.utils.system.Constants;
import com.dominator.utils.system.PrimaryGenerater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dominator.utils.dateutil.DateUtil.DatetoStringFormat;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    private static RedisUtil ru = RedisUtil.getRu();

    @Autowired
    private TCompanyMapper tCompanyMapper;

    @Autowired
    private TCompanyBillMapper tCompanyBillMapper;

    @Autowired
    private TGardenMapper tGardenMapper;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TCommonExt tCommonExt;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Override
    public ApiMessage uploadPicture(MultipartFile multipartFile) {
        ApiMessage msg = new ApiMessage(Constants.REQ_SUCCESS, Constants.MSG_SUCCESS);
        String result = QCloudUploadFile.uploadFile(multipartFile);
        Dto resDto = Dtos.newDto();
        resDto.put("url", result);
        msg.setData(Des3Utils.encResponse(resDto));
        return msg;
    }

    /**
     * 上传文件预览
     *
     * @param file
     * @return
     * @throws ApiException
     */
    @Override
    public ApiMessage uploadExcelRead(MultipartFile file) throws ApiException {
        try {
            //创建处理EXCEL的类
            ReadExcel readExcel = new ReadExcel();
            String fileName = file.getOriginalFilename();
            if (!"Bills.xls".equals(fileName)) {
                return ApiMessageUtil.success(readExcel.getExcelInfo(file));
            }
            // String originalFilename = file.getOriginalFilename();
            return ApiMessageUtil.success(readExcel.parseExcel(file));
            /**
             //解析excel，获取上传的事件单
             List<Dto> useList = readExcel.getExcelInfo(file);
             log.info("useList:{}", useList.toString());
             if (useList == null || useList.size() <= 1) {
             throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), MessageEnums.EXCEL_NULL.getMsg());
             }
             //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,
             //和你具体业务有关,这里不做具体的示范
             return ApiMessageUtil.success(useList);
             **/
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), ReqEnums.REQ_PARAM_ERROR.getMsg());
        }
    }

    /**
     * 将上传Excel信息保存到数据库
     *
     * @param dto 1.name=companyuser 传参如下
     *            company_id String 必传 公司id
     *            modify_id String 必传 当前登录用户id
     *            name String 必传 导入内容名称
     *            data String 必传 预览excel时，返回的json数据
     *            <p>
     *            <p>
     *            2.name=gardenuser 传参如下 3.name=company 传参如下
     *            garden_id String 必传 园区id
     *            modify_id String 必传 当前登录用户id
     *            name String 必传 导入内容名称
     *            data String 必传 预览excel时，返回的json数据
     *            <p>
     *            4.name=gas/power/water/rent传参如下
     *            garden_id String  园区id （园区端必传）
     *            company_id String 企业id  (企业端必传）
     *            company_name String 必传 企业名称
     *            modify_id String 必传 当前登录用户id
     *            push_time 必传 推送时间
     *            name String 必传 导入内容名称
     *            data String 必传 预览excel时，返回的json数据
     * @return
     */

    @Override
    @Transactional
    public ApiMessage saveExcel2Database(Dto dto) throws ApiException {
        String name = dto.getString("name").toLowerCase();
        List<Dto> list = new ArrayList<>();
        if (!"bills".equals(name)) {
            list = JsonUtils.fromJson(dto.getString("data"));
        }
        try {
            if ("companyuser".equals(name)) {
                saveCompanyUser(dto, list);
            }
            if ("gardenuser".equals(name)) {
                saveGardenUser2(dto, list);
            }
            if ("company".equals(name)) {
                saveCompany(dto, list);
            }
            if ("power".equals(name) || "gas".equals(name) ||
                    "rent".equals(name) || "water".equals(name) || "property".equals(name)) {
                saveBill(dto, name, list);
            }
            if ("bills".equals(name)) {
                this.saveBills(dto);
            }
            return ApiMessageUtil.success();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), MessageEnums.WRONG_DATEFORMAT.getMsg());
        } catch (ApiException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void saveBills(Dto dto) throws ParseException {
        Dto billDto = JsonUtils.toDto(dto.getString("data"));
        //物业费
        String propertyBillStr = billDto.getString("property_bill");
        List<Dto> propertyBills = JsonUtils.fromJson(propertyBillStr);
        if (!CollectionUtils.isEmpty(propertyBills)) this.saveBill(dto, "property", propertyBills);
        //房租费
        String rentBillStr = billDto.getString("rent_bill");
        System.out.println(rentBillStr);
        List<Dto> rentBills = JsonUtils.fromJson(rentBillStr);
        if (!CollectionUtils.isEmpty(rentBills)) this.saveBill(dto, "rent", rentBills);

        //电费
        String powerBillStr = billDto.getString("electric_charge_bill");
        List<Dto> powerBills = JsonUtils.fromJson(powerBillStr);
        if (!CollectionUtils.isEmpty(powerBills)) this.saveBill(dto, "power", powerBills);

        //水费
        String waterBillStr = billDto.getString("water_bill");
        List<Dto> waterBills = JsonUtils.fromJson(waterBillStr);
        if (!CollectionUtils.isEmpty(waterBills)) this.saveBill(dto, "water", waterBills);

        //燃气费
        String gasBillStr = billDto.getString("gas_bill");
        //gasBillStr="[{list=[*缴费单位(必填), *账单月份(必填), *用量(度), *单价(元,度必填), 其他(选填), *账单金额(必填), *应缴日期(必填)]}, {list=[上海众屏, 2017-12, 100, 1, 200, 300, 2018-01-15]}]";
        List<Dto> gasBills = JsonUtils.fromJson(gasBillStr);
        if (!CollectionUtils.isEmpty(gasBills)) this.saveBill(dto, "gas", gasBills);
    }

    public void saveBill(Dto dto, String name, List<Dto> list) throws ParseException {
        String fee_type = "";

        //*缴费单位, *账单月份, *用量（度）, *单价（元/度）, 其他, *账单金额, *应缴日期（list1元素顺序）
        if ("power".equals(name)) fee_type = "1";
        if ("gas".equals(name)) fee_type = "2";

        //*缴费单位, *账单月份, *房租/水费, 其他, *账单金额, *应缴日期（list1元素顺序）
        if ("water".equals(name)) fee_type = "0";
        if ("rent".equals(name)) fee_type = "3";
        if ("property".equals(name)) fee_type = "4";

        TCompanyExample companyExample = new TCompanyExample();
        companyExample.or().andGardenIdEqualTo(dto.getString("garden_id"))
                .andIsValidEqualTo("1");
        List<TCompany> companyList = tCompanyMapper.selectByExample(companyExample);
        List<String> compIds = new ArrayList<>();
        if (companyList != null && !companyList.isEmpty()) {
            for (TCompany company : companyList)
                compIds.add(company.getId());
        }
        //导入公司不在当前园区中
        List<String> errorList = new ArrayList<>();

        for (int i = 1; i < list.size(); i++) {
            boolean exist = false; //该账单是否存在
            //获取导入list本行的 所有元素list1
            List list1 = list.get(i).getList("list");
            String company_id = "";
            String company_name = String.valueOf(list1.get(0));
            String bill_year_month = "";
            String company_bill_id = "";
            if (list1.get(1) != null) {
                bill_year_month = String.valueOf(list1.get(1));
            }
            log.info("bill_year_month:{}", bill_year_month);

            companyExample.clear();
            companyExample.or().andCompanyNameEqualTo(company_name)
                    .andIsValidEqualTo("1");
            List<TCompany> companyList1 = tCompanyMapper.selectByExample(companyExample);
            if (companyList1 != null && !companyList1.isEmpty()) {
                TCompany company = companyList1.get(0);
                boolean contains = compIds.contains(company.getId());
                if (!contains) {
                    errorList.add(company_name + ReqEnums.REQ_COMPANY_NOT_EXIST_IN_GARDEN.getMsg());
                    continue;
                    // throw new ApiException(ReqEnums.REQ_COMPANY_NOT_EXIST_IN_GARDEN.getCode(),company_name+ReqEnums.REQ_COMPANY_NOT_EXIST_IN_GARDEN.getMsg());
                }
                company_id = company.getId();
            } else {
                errorList.add(company_name + MessageEnums.COMPANY_NOT_EXIST.getMsg());
                continue;
            }

            TCompanyBillExample companyBillExample = new TCompanyBillExample();
            TCompanyBillExample.Criteria criteria = companyBillExample.createCriteria();
            criteria.andCompanyIdEqualTo(company_id);
            ;
            criteria.andBillYearMonthEqualTo(bill_year_month);
            if (!SystemUtils.isEmpty(fee_type))
                criteria.andFeeTypeEqualTo(fee_type);
            companyBillExample.or(criteria);
            List<TCompanyBill> companyBillList = tCompanyBillMapper.selectByExample(companyBillExample);
            TCompanyBill company_bill = new TCompanyBill();
            if (companyBillList != null && !companyBillList.isEmpty()) {
                company_bill = companyBillList.get(0);
                if (company_bill.getIsPushed().equals("1")) {
                    errorList.add(MessageEnums.BILL_PUSHED.getMsg() + "(" + list1.toString() + ")");
                    continue;
                    //throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), MessageEnums.BILL_PUSHED.getMsg() + "(" + list1.toString() + ")");
                }
                exist = true;
                company_bill_id = company_bill.getId();
            }

            BigDecimal total_fee = null;
            BigDecimal other_fee = null;
            BigDecimal main_fee = null;
            BigDecimal fee_unit = null;
            BigDecimal fee_dgree = null;
            if ("power".equals(name) || "gas".equals(name)) {
                total_fee = new BigDecimal(String.valueOf(list1.get(5)));
                other_fee = new BigDecimal(0);
                if (list1.get(4) != null) {
                    other_fee = new BigDecimal(String.valueOf(list1.get(4)));
                }
                main_fee = total_fee.min(other_fee);
                fee_unit = new BigDecimal(String.valueOf(list1.get(3)));
                fee_dgree = new BigDecimal(String.valueOf(list1.get(2)));
            }
            if ("water".equals(name) || "rent".equals(name) || "property".equals(name)) {
                total_fee = new BigDecimal(String.valueOf(list1.get(4)));
                other_fee = new BigDecimal(0);
                if (list1.get(4) != null) {
                    other_fee = new BigDecimal(String.valueOf(list1.get(3)));
                }
                main_fee = new BigDecimal(String.valueOf(list1.get(2)));
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            company_bill = new TCompanyBill();
            company_bill.setTotalFee(total_fee);
            company_bill.setMainFee(main_fee);
            company_bill.setOtherFee(other_fee);
            company_bill.setIsValid("1");
            if (fee_type.equals("1") || fee_type.equals("2")) {
                company_bill.setFeeUnit(fee_unit);
                company_bill.setFeeDgree(fee_dgree);
                company_bill.setExpiryTime(sdf.parse(String.valueOf(list1.get(6))));
            } else {
                company_bill.setExpiryTime(sdf.parse(String.valueOf(list1.get(5))));
            }

            if (exist) {
                String token = dto.getString("token");
                Dto tokenDto = (Dto) ru.getObject(token);
                String modify_id = tokenDto.getString("user_id");
                company_bill.setId(company_bill_id);
                company_bill.setPushTime(dto.getDate("push_time"));
                company_bill.setModifyTime(new Date());
                company_bill.setModifyId(modify_id);
                tCompanyBillMapper.updateByPrimaryKey(company_bill);
            } else {
                company_bill.setId(PrimaryGenerater.getInstance().uuid());
                company_bill.setCompanyId(company_id);
                company_bill.setBillNum(PrimaryGenerater.getInstance().next());
                company_bill.setBillYearMonth(bill_year_month);
                company_bill.setFeeType(fee_type);
                company_bill.setIsPushed("0");
                company_bill.setPushTime(dto.getDate("push_time"));
                company_bill.setIsPaid("0");
                company_bill.setCreateTime(new Date());
                tCompanyBillMapper.insertSelective(company_bill);
            }
        }
        if (errorList != null && errorList.size() > 0) {
            throw new ApiException(ReqEnums.REQ_ERORR_MSG_LIST.getCode(), ReqEnums.REQ_ERORR_MSG_LIST.getMsg() + errorList);
        }
    }

    private void saveCompany(Dto dto, List<Dto> list) throws ApiException {
        //﻿*企业名称, *租赁地址, *企业联系人, *联系人手机号,
        // 注册地址, 注册电话, 开户银行, 银行账号, 统一社会信用代码, 法定代表人（list1元素顺序）
        int count = 0;
        String garden_id = dto.getString("garden_id");
        String contact_province = "";
        String contact_city = "";
        String contact_area = "";

        TGarden garden = tGardenMapper.selectByPrimaryKey(garden_id);
        if (garden != null) {
            contact_province = garden.getProvince();
            contact_city = garden.getCity();
            contact_area = garden.getArea();
        }
        List<String> company_name_list = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            //获取导入list本行的 所有元素list1
            List list1 = list.get(i).getList("list");
            String company_name = String.valueOf(list1.get(0));
            String contact_street = String.valueOf(list1.get(1));
            String contact_name = String.valueOf(list1.get(2));
            String contact_phone = String.valueOf(list1.get(3));
            Dto dto1 = new HashDto();
            dto1.put("garden_id", garden_id);
            dto1.put("company_name", company_name);
            dto1.put("contact_name", contact_name);
            dto1.put("contact_phone", contact_phone);
            dto1.put("contact_province", contact_province);
            dto1.put("contact_city", contact_city);
            dto1.put("contact_area", contact_area);
            dto1.put("contact_street", contact_street);
            try {
                companyService.postCompany(dto1);
            } catch (ApiException e) {
                e.printStackTrace();
                company_name_list.add(company_name);
            }
        }
        if (company_name_list.size() > 0)
            throw new ApiException(ReqEnums.REQ_IMPORT_ERROR.getCode(), ReqEnums.REQ_IMPORT_ERROR + company_name_list.toString());
    }

    @Transactional
    public void saveGardenUser2(Dto dto, List<Dto> list) {
        for (int i = 1; i < list.size(); i++) {
            Dto newDto = new HashDto();
            List list1 = list.get(i).getList("list");
            newDto.put("nick_name", list1.get(0));
            newDto.put("phone_num", list1.get(1));
            newDto.put("id_card", list1.get(2));
            newDto.put("hire_date", list1.get(3));
            //newDto.put("status",split[4]);
            newDto.put("work_num", list1.get(4));
            newDto.put("department_name", list1.get(5));
            newDto.put("position", list1.get(6));
            newDto.put("level", list1.get(7));
            newDto.put("email", list1.get(7));
            newDto.put("garden_id", dto.getString("garden_id").trim());

            TRoleExample roleExample = new TRoleExample();
            roleExample.or().andRoleNameEqualTo("普通员工")
                    .andTypeEqualTo("2")
                    .andIsValidEqualTo("1");
            List<TRole> roleList = tRoleMapper.selectByExample(roleExample);
            if (roleList == null || roleList.isEmpty())
                throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), MessageEnums.WRONG_ROLE_NAME.getMsg());
            TRole role = roleList.get(0);
            newDto.put("role_id", role.getId());
            newDto.put("type", "1");
            //userService.postUser(newDto);
        }
    }

//    private void saveGardenUser(Dto dto, List<Dto> list) throws ParseException {
//        //员工姓名(必填), 手机号码(必填), 身份证（必填）, 入职时间（必填）, 在职状态（必填）,角色权限（必填）
//        //工号（选填）, 部门（选填）, 岗位（选填）, 职级（选填）, 工作邮箱（选填）（list1元素顺序）
//        for (int i = 1; i < list.size(); i++) {
//            boolean exist = false;
//            //获取导入list本行的 所有元素list1
//            List list1 = list.get(i).getList("list");
//            String id_card = String.valueOf(list1.get(2));
//            String garden_id = dto.getString("garden_id");
//            String user_id = "";
//            String garden_user_id = "";
//            Dto dto2 = new HashDto();
//            dto2.put("id_card", id_card);
//            T_userPO user1 = t_userDao.selectOne(dto2);
//            if (user1 != null) {
//                //该身份证号的user存在
//                Dto dto3 = new HashDto();
//                dto3.put("garden_id", garden_id);
//                dto3.put("user_id", user1.getId());
//                T_garden_userPO garden_user = t_garden_userDao.selectOne(dto3);
//                if (garden_user != null) {
//                    //该园区的此员工信息存在
//                    exist = true;
//                    user_id = user1.getId();
//                    garden_user_id = garden_user.getId();
//                } else {
//                    //其他园区存在该员工信息
//                    throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), id_card + MessageEnums.REPEAT_ID_CARD.getMsg());
//                }
//            }
//            if (exist) {
//                gardenUserExist(dto, list1, id_card, user_id, garden_user_id, garden_id);
//            } else {
//                gardenUserNotExist(list1, id_card, garden_id);
//            }
//        }
//    }

    private void saveCompanyUser(Dto dto, List<Dto> list) throws ParseException {
        //员工姓名(必填), 手机号码(必填),  入职时间（必填）,
        //身份证（必填）,工号（选填）, 部门（选填）, 岗位（选填）, 职级（选填）, 工作邮箱（选填）（list1元素顺序）
        List<String> user_names = new ArrayList<>();
        String company_id = dto.getString("company_id");
        for (int i = 1; i < list.size(); i++) {
            //获取导入list本行的 所有元素list1
            List list1 = list.get(i).getList("list");
            String nick_name = String.valueOf(list1.get(0));
            String phone_num = String.valueOf(list1.get(1));
            String hire_date = String.valueOf(list1.get(2));
            String id_card = String.valueOf(list1.get(3));
            String work_num = String.valueOf(list1.get(4));
            String department_name = String.valueOf(list1.get(5));
            String position = String.valueOf(list1.get(6));
            String level = String.valueOf(list1.get(7));
            String email = String.valueOf(list1.get(8));

            Dto dto1 = new HashDto();
            dto1.put("company_id", company_id);
            dto1.put("nick_name", nick_name);
            dto1.put("phone_num", phone_num);
            dto1.put("hire_date", hire_date);
            dto1.put("id_card", id_card);
            dto1.put("work_num", work_num);
            dto1.put("department_name", department_name);
            dto1.put("position", position);
            dto1.put("level", level);
            dto1.put("email", email);
            dto1.put("type", "2");
            userService.addUser(dto1);
            if (!SystemUtils.isEmpty(dto.getString("fail"))) {
                user_names.add(phone_num);
            }
        }
        if (user_names.size() > 0) {
            throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), user_names.toString() + "用户已在其他企业添加，添加失败");
        }
    }

//    private void gardenUserNotExist(List list1, String id_card, String garden_id) throws ParseException {
//        //员工姓名(必填), 手机号码(必填), 身份证（必填）, 入职时间（必填）, 在职状态（必填）,角色权限（必填）
//        //工号（选填）, 部门（选填）, 岗位（选填）, 职级（选填）, 工作邮箱（选填）（list1元素顺序）
//        String user_id;
//        log.info("list1:{}", list1);
//        user_id = PrimaryGenerater.getInstance().uuid();
//        T_userPO user = new T_userPO();
//        user.setId(user_id);
//        user.setUser_name(String.valueOf(list1.get(1)));
//        user.setNick_name(String.valueOf(list1.get(0)));
//
//        //根据身份证号判断性别， 第17位偶数女， 奇数男
//        String gender = "";
//        if (id_card != null && id_card.length() == 18) {
//            gender = Integer.valueOf(id_card.charAt(16)) % 2 + "";
//        }
//        user.setGender(gender);
//        user.setPhone_num(String.valueOf(list1.get(1)));
//        user.setId_card(id_card);
//        user.setEmail(String.valueOf(list1.get(9)));
//        user.setCreate_time(new Date());
//        user.setIs_valid("1");
//
//        T_garden_userPO garden_user = new T_garden_userPO();
//        garden_user.setId(PrimaryGenerater.getInstance().uuid());
//        garden_user.setGarden_id(garden_id);
//        garden_user.setUser_id(user_id);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        garden_user.setHire_date(sdf.parse(String.valueOf(list1.get(3))));
//        garden_user.setWork_num(String.valueOf(list1.get(5)));
//        garden_user.setDepartment_name(String.valueOf(list1.get(6)));
//        garden_user.setPosition(String.valueOf(list1.get(7)));
//        garden_user.setLevel(String.valueOf(list1.get(8)));
//        garden_user.setEmail(String.valueOf(list1.get(9)));
//        garden_user.setCreate_time(new Date());
//        String status = String.valueOf(list1.get(4));
//        garden_user.setStatus("离职".equals(status) ? "0" : "1");
//        garden_user.setIs_valid("1");
//
//        String role_name = "普通员工";
//        String role_id = "";
//        /**String role_name = String.valueOf(list1.get(5));
//         String role_id = "";
//         if (role_name == null || role_name.equals("")) {
//         role_name = "无";
//         }
//         **/
//        Dto dto1 = new HashDto();
//        dto1.put("role_name", role_name);
//        dto1.put("type", "2");
//        dto1.put("is_valid", "1");
//
//        T_rolePO role = t_roleDao.selectOne(dto1);
//        if (role == null) {
//            throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), MessageEnums.WRONG_ROLE_NAME.getMsg());
//        }
//        role_id = role.getId();
//
//        T_garden_user_rolePO garden_user_role = new T_garden_user_rolePO();
//        garden_user_role.setId(PrimaryGenerater.getInstance().uuid());
//        garden_user_role.setGarden_id(garden_id);
//        garden_user_role.setRole_id(role_id);
//        garden_user_role.setUser_id(user_id);
//        garden_user_role.setCreate_time(new Date());
//        garden_user_role.setIs_valid("1");
//        int count = 0;
//        count = t_garden_user_roleDao.insert(garden_user_role);
//        if (count != 1) {
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "t_garden_user_roleDao");
//        }
//        count = t_userDao.insert(user);
//        if (count != 1) {
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "t_userDao");
//        }
//        count = t_garden_userDao.insert(garden_user);
//        if (count != 1) {
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "t_garden_userDao");
//        }
//    }

//    private void gardenUserExist(Dto dto, List list1, String id_card, String user_id, String garden_user_id, String garden_id) throws ParseException {
//        T_userPO user = new T_userPO();
//        user.setId(user_id);
//        user.setUser_name(String.valueOf(list1.get(1)));
//        user.setNick_name(String.valueOf(list1.get(0)));
//        user.setPhone_num(String.valueOf(list1.get(1)));
//        user.setId_card(id_card);
//        user.setEmail(String.valueOf(list1.get(9)));
//        user.setModify_time(new Date());
//        user.setModify_id(dto.getString("modify_id"));
//        user.setIs_valid("1");
//
//        T_garden_userPO garden_user = new T_garden_userPO();
//        garden_user.setId(garden_user_id);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        garden_user.setHire_date(sdf.parse(String.valueOf(list1.get(3))));
//        garden_user.setWork_num(String.valueOf(list1.get(5)));
//        garden_user.setDepartment_name(String.valueOf(list1.get(6)));
//        garden_user.setPosition(String.valueOf(list1.get(7)));
//        garden_user.setLevel(String.valueOf(list1.get(8)));
//        garden_user.setEmail(String.valueOf(list1.get(9)));
//        garden_user.setModify_time(new Date());
//        garden_user.setModify_id(dto.getString("modify_id"));
//        String status = String.valueOf(list1.get(4));
//        garden_user.setStatus("离职".equals(status) ? "0" : "1");
//        garden_user.setIs_valid("1");
//
//        String role_name = String.valueOf(list1.get(5));
//        if (role_name == null || role_name.equals("")) {
//            role_name = "无";
//        }
//
//        String role_id = "";
//        Dto dto5 = new HashDto();
//        dto5.put("role_name", role_name);
//        dto5.put("is_valid", "1");
//        T_rolePO role = t_roleDao.selectOne(dto5);
//        if (role == null) {
//            throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), MessageEnums.WRONG_ROLE_NAME.getMsg());
//        }
//        role_id = role.getId();
//
//        Dto dto4 = new HashDto();
//        dto4.put("garden_id", garden_id);
//        dto4.put("user_id", user_id);
//        dto4.put("is_valid", "1");
//        T_garden_user_rolePO garden_user_role = t_garden_user_roleDao.selectOne(dto4);
//        //设置过权限，修改信息
//        garden_user_role.setRole_id(role_id);
//        garden_user_role.setModify_id(dto.getString("modify_id"));
//        garden_user_role.setModify_time(new Date());
//        garden_user_role.setIs_valid("1");
//
//        int count = 0;
//        count = t_garden_user_roleDao.updateByKey(garden_user_role);
//        if (count != 1) {
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "t_garden_user_roleDao");
//        }
//        count = t_userDao.updateByKey(user);
//        if (count != 1) {
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "t_userDao");
//        }
//        count = t_garden_userDao.updateByKey(garden_user);
//        if (count != 1) {
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "t_garden_userDao");
//        }
//    }

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
    @Override
    public ApiMessage export(Dto dto) {
        String str = "";
        String type = dto.getString("type").toLowerCase();
        if ("companyuser".equals(type)) {
//            if ("2".equals(dto.getString("status"))) {
//                dto.put("status", "");
//            }
//            dto.put("fileName", "companyuser");
//            try {
//                dto.put("title", new String(SysConfig.companyuser.getBytes("ISO-8859-1"), "utf-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            str = exportCompanyuser(dto);
        }
        if ("gardenuser".equals(type)) {
//            if ("2".equals(dto.getString("status"))) {
//                dto.put("status", "");
//            }
//            dto.put("fileName", "gardenuser");
//            try {
//                dto.put("title", new String(SysConfig.gardenuser.getBytes("ISO-8859-1"), "utf-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            str = exportGardenuser(dto);
        }
        if ("unpaidbills".equals(type)) {
            dto.put("fileName", "unpaidbills");
            dto.put("is_paid", "0");
            try {
                dto.put("title", new String(SysConfig.unpaidbills.getBytes("ISO-8859-1"), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            str = exportBills(dto);
        }
        if ("paidbills".equals(type)) {
            dto.put("fileName", "paidbills");
            dto.put("is_paid", "1");
            try {
                dto.put("title", new String(SysConfig.paidbills.getBytes("ISO-8859-1"), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            str = exportBills(dto);
        }
        if ("gardenvisitors".equals(type)) {
            dto.put("fileName", "gardenvisitors");
            try {
                dto.put("title", new String(SysConfig.gardenvisitors.getBytes("ISO-8859-1"), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            str = exportVisitors(dto);
        }
        if ("companyvisitors".equals(type)) {
            dto.put("fileName", "companyvisitors");
            try {
                dto.put("title", new String(SysConfig.companyvisitors.getBytes("ISO-8859-1"), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            str = exportVisitors(dto);
        }
        if ("company".equals(type)) {
            dto.put("fileName", "company");
            try {
                dto.put("title", new String(SysConfig.company.getBytes("ISO-8859-1"), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            str = exportCompany(dto);
        }
        //无数据时，返回 0（int）
        if (str.equals("0")) {
            return ApiMessageUtil.success(0);
        }
        return ApiMessageUtil.success(str);
    }

    private String exportCompany(Dto dto) {
        Dto dto1 = new HashDto();
        dto1.put("garden_id", dto.getString("garden_id"));
        dto1.put("company_id", dto.getString("company_id"));
        dto1.put("fee_type", dto.getString("fee_type"));
        dto1.put("is_paid", dto.getString("is_paid"));
        dto1.put("is_valid", "1");
        dto1.put("startTime", dto.getDate("startTime"));
        dto1.put("endTime", dto.getDate("endTime"));
        List<Dto> list = tCommonExt.list(dto1);
        if (list != null && list.size() > 0) {
            for (Dto dto2 : list) {
                //企业名称,企业联系人,手机号,租赁地址,待缴费金额
                dto2.put("企业名称", dto2.getString("bill_num"));
                dto2.put("企业联系人", dto2.getString("bill_name"));
                dto2.put("手机号", dto2.getString("total_fee"));
                dto2.put("租赁地址", dto2.getString("company_name"));
                dto2.put("待缴费金额", dto2.getDate("create_time"));
            }
        }

        String fileName = dto.getString("fileName");
        String title = dto.getString("title");

        return ExcelExportor.exportExcel(fileName, title.split(","), (List) list);
    }

    //gardenvisitors companyvisitors公用方法
    private String exportVisitors(Dto dto) {
        Dto dto1 = new HashDto();
        if ("gardenvisitors".equals(dto.getString("type").toLowerCase())) {
            dto1.put("garden_id", dto.getString("garden_id"));
        }
        if ("companyvisitors".equals(dto.getString("type").toLowerCase())) {
            dto1.put("company_id", dto.getString("company_id"));
        }
        dto1.put("status", dto.getString("status"));
        dto1.put("is_valid", "1");
        if (SystemUtils.isEmpty(dto.getString("startTime")) && SystemUtils.isEmpty(dto.getString("endTime"))) {
            Date now = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.MONTH, -1);
            Date create_time = c.getTime();
            String Now = DatetoStringFormat(now, "yyyy年MM月dd日");
            String Creatime = DatetoStringFormat(create_time, "yyyy年MM月dd日");
            dto1.put("end_time", Now);
            dto1.put("create_time", Creatime);
        } else {
            dto.put("end_time", DatetoStringFormat(dto.getDate("endTime"), "yyyy年MM月dd日"));
            dto.put("create_time", DatetoStringFormat(dto.getDate("startTime"), "yyyy年MM月dd日"));
        }
        List<Dto> list = tCommonExt.listBackVisitor(dto1);
        if (list.size() > 0 && list != null) {
            for (Dto visitorDto : list) {
                //通行码,访客姓名,电话,车牌号,预约到访时间,实际到访时间,状态
                visitorDto.put("通行码", visitorDto.getString("visitor_pass_code"));
                visitorDto.put("访客姓名", visitorDto.getString("visitor_name"));
                visitorDto.put("电话", visitorDto.getString("visitor_phone"));
                visitorDto.put("车牌号", visitorDto.getString("visitor_car_num"));
                visitorDto.put("拜访单位", visitorDto.getString("company_name") + " " + visitorDto.getString("company_address"));
                visitorDto.put("预约到访时间", visitorDto.getString("expect_time"));
                visitorDto.put("实际到访时间", visitorDto.getString("fact_time"));
                visitorDto.put("状态", visitorDto.getString("status"));
            }
        }
        String fileName = dto.getString("fileName");
        String title = dto.getString("title");

        return ExcelExportor.exportExcel(fileName, title.split(","), (List) list);

    }


    //paidbills unpaidbills公用方法
    private String exportBills(Dto dto) {
        Dto dto1 = new HashDto();
        dto1.put("garden_id", dto.getString("garden_id"));
        dto1.put("company_id", dto.getString("company_id"));
        dto1.put("fee_type", dto.getString("fee_type"));
        dto1.put("is_paid", dto.getString("is_paid"));
        dto1.put("is_valid", "1");
        dto1.put("startTime", dto.getDate("startTime"));
        dto1.put("endTime", dto.getDate("endTime"));
        List<Dto> list = tCommonExt.listBills(dto1);
        if (list != null && list.size() > 0) {
            for (Dto dto2 : list) {
                //账单号,应缴日期,账单名称,账单金额,缴费单位,创建时间,推送状态(实缴日期)
                dto2.put("账单号", dto2.getString("bill_num"));
                dto2.put("应缴日期", dto2.getDate("expiry_time"));
                dto2.put("账单名称", dto2.getString("bill_name"));
                dto2.put("账单金额", dto2.getString("total_fee"));
                dto2.put("缴费单位", dto2.getString("company_name"));
                dto2.put("创建时间", dto2.getDate("create_time"));
                dto2.put("推送状态", dto2.getString("is_pushed").equals("0") ? "未推送" : "已推送");
                dto2.put("实缴日期", dto2.getDate("pay_time"));
            }
        }

        String fileName = dto.getString("fileName");
        String title = dto.getString("title");

        return ExcelExportor.exportExcel(fileName, title.split(","), (List) list);

    }

//    private String exportCompanyuser(Dto dto) {
//        log.info("dto:{}", dto.toString());
//        Dto dto1 = new HashDto();
//        dto1.put("company_id", dto.getString("company_id"));
//        dto1.put("status", dto.getString("status"));
//        dto1.put("company_admin_id", propertiesLoader.getProperty("company_admin_id"));
//        dto1.put("is_valid", "1");
//        List<Dto> list = tUserDaoExt.listCompanyUsers(dto1);
//        if (list != null && list.size() > 0) {
//            for (Dto dto2 : list) {
//                //员工姓名,手机号码,身份证,入职时间,在职状态,工号,部门,岗位,职级,工作邮箱
//                dto2.put("员工姓名", dto2.getString("nick_name"));
//                dto2.put("手机号码", dto2.getString("phone_num"));
//                dto2.put("身份证", dto2.getString("id_card"));
//                dto2.put("入职时间", dto2.getString("hire_date"));
//                dto2.put("在职状态", "1".equals(dto2.getString("status")) ? "在职" : "离职");
//                dto2.put("工号", dto2.getString("work_num"));
//                dto2.put("部门", dto2.getString("department_name"));
//                dto2.put("岗位", dto2.getString("position"));
//                dto2.put("职级", dto2.getString("level"));
//                dto2.put("工作邮箱", dto2.getString("email"));
//            }
//        }
//
//        String fileName = dto.getString("fileName");
//        String title = dto.getString("title");
//
//        return ExcelExportor.exportExcel(fileName, title.split(","), (List) list);
//    }

//    private String exportGardenuser(Dto dto) {
//        log.info("dto:{}", dto.toString());
//        Dto dto1 = new HashDto();
//        dto1.put("garden_id", dto.getString("garden_id"));
//        dto1.put("status", dto.getString("status"));
//        dto1.put("super_admin_id", propertiesLoader.getProperty("super_admin_id"));
//        dto1.put("admin_id", propertiesLoader.getProperty("admin_id"));
//        dto1.put("is_valid", "1");
//        List<Dto> list = tUserDaoExt.listGardenUsers(dto1);
//        if (list != null && list.size() > 0) {
//            for (Dto dto2 : list) {
//                //员工姓名,手机号码,身份证,入职时间,在职状态,角色权限,工号,部门,岗位,职级,工作邮箱
//                dto2.put("员工姓名", dto2.getString("nick_name"));
//                dto2.put("手机号码", dto2.getString("phone_num"));
//                dto2.put("身份证", dto2.getString("id_card"));
//                dto2.put("入职时间", dto2.getString("hire_date"));
//                //dto2.put("在职状态", "1".equals(dto2.getString("status")) ? "在职" : "离职");
//                dto2.put("角色权限", dto2.getString("role_name"));
//                dto2.put("工号", dto2.getString("work_num"));
//                dto2.put("部门", dto2.getString("department_name"));
//                dto2.put("岗位", dto2.getString("position"));
//                dto2.put("职级", dto2.getString("level"));
//                dto2.put("工作邮箱", dto2.getString("email"));
//            }
//        }
//
//        String fileName = dto.getString("fileName");
//        String title = dto.getString("title");
//
//        return ExcelExportor.exportExcel(fileName, title.split(","), (List) list);
//    }

    /**
     * 获取菜单列表
     *
     * @param dto 1.企业端
     *            modules_type String 必传 模块类型 0 h5, 1园区garden管理后台,2企业company管理后台，3其他
     *            parent_id String 非必传 父菜单id, 一级菜单传 root
     * @return
     */
    @Override
    public ApiMessage listModules(Dto dto) {
//        dto.put("is_valid", "1");
//        List<T_modulesPO> modules = tCommonDaoExt.listModules(dto);
//        List<T_modulesPO> mainModules = new ArrayList<>();
//        List<T_modulesPO> subModules = new ArrayList<>();
//        for (T_modulesPO module : modules) {
//            if (module.getParent_id().equals("root")) {
//                mainModules.add(module);
//            } else {
//                subModules.add(module);
//            }
//        }
//        Dto resDto = new HashDto();
//        resDto.put("mainModules", mainModules);
//        resDto.put("subModules", subModules);
//        return ApiMessageUtil.success(resDto);
        return null;
    }

    /**
     * 注册企业管理员
     *
     * @param dto company_id String 必传
     *            nick_name String 必传
     *            phone_num String 必传
     */
    @Override
    @Transactional
    public ApiMessage registerCompanyManager(Dto dto) throws ApiException {
//        try {
//            Dto dto1 = new HashDto();
//            dto1.put("user_name", dto.getString("phone_num"));
//            dto1.put("is_valid", "1");
//            T_userPO user1 = t_userDao.selectOne(dto1);
//            if (user1 != null) {
//                user1.setModify_time(new Date());
//                t_userDao.deleteByKey(user1.getId());
//
//                Dto dto2 = new HashDto();
//                dto2.put("user_id", user1.getId());
//                dto2.put("company_id", dto.getString("company_id"));
//                dto2.put("is_valid", "1");
//                T_company_userPO company_user = t_company_userDao.selectOne(dto2);
//                if (company_user != null) {
//                    t_company_userDao.deleteByKey(company_user.getId());
//                }
//
//                T_company_user_rolePO company_user_role = t_company_user_roleDao.selectOne(dto2);
//                if (company_user_role != null) {
//                    t_company_user_roleDao.deleteByKey(company_user_role.getId());
//                }
//            }
//
//            //1.存user表
//            String user_id = PrimaryGenerater.getInstance().uuid();
//            T_userPO user = new T_userPO();
//            user.copyProperties(dto);
//            user.setId(user_id);
//            user.setUser_name(dto.getString("phone_num"));
//            user.setCreate_time(new Date());
//            user.setIs_valid("1");
//            //2.存company_user表
//            T_company_userPO company_user = new T_company_userPO();
//            company_user.setId(PrimaryGenerater.getInstance().uuid());
//            company_user.setUser_id(user_id);
//            company_user.setCompany_id(dto.getString("company_id"));
//            company_user.setHire_date(new Date());
//            company_user.setIs_binding("0");
//            company_user.setCreate_time(new Date());
//            company_user.setStatus("1");
//            company_user.setIs_valid("1");
//            //3.存company_user_role表
//            T_company_user_rolePO company_user_role = new T_company_user_rolePO();
//            company_user_role.setId(PrimaryGenerater.getInstance().uuid());
//            company_user_role.setCompany_id(dto.getString("company_id"));
//            company_user_role.setRole_id(propertiesLoader.getProperty("company_admin_id"));
//            company_user_role.setUser_id(user_id);
//            company_user_role.setCreate_time(new Date());
//            company_user_role.setIs_valid("1");
//
//            int count = 0;
//            count = t_userDao.insert(user);
//            if (count != 1) {
//                throw new ApiException(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg() + "t_userDao");
//            }
//            count = t_company_userDao.insert(company_user);
//            if (count != 1) {
//                throw new ApiException(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg() + "t_company_userDao");
//            }
//            count = t_company_user_roleDao.insert(company_user_role);
//            if (count != 1) {
//                throw new ApiException(ReqEnums.SYS_ERROR.getCode(), ReqEnums.SYS_ERROR.getMsg() + "t_company_user_roleDao");
//            }
//            return ApiMessageUtil.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
//        }
        return null;
    }

    /**
     * @param dto garden_name 非必传
     *            company_name 非必传
     *            bill_num 非必传
     * @return
     */
    @Override
    @Transactional
    public ApiMessage pushBill(Dto dto) throws ApiException {
        try {
            dto.put("is_valid", "1");
            dto.put("is_pushed", "0");
            int count = 0;
            if (!SystemUtils.isEmpty(dto.getString("bill_num"))) {
                TCompanyBillExample companyBillExample = new TCompanyBillExample();
                companyBillExample.or().andIsPushedEqualTo("0")
                        .andIsValidEqualTo("1");
                List<TCompanyBill> companyBillList = tCompanyBillMapper.selectByExample(companyBillExample);
                if(companyBillList==null||companyBillList.isEmpty())
                    throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg() + "账单不存在");
                TCompanyBill companyBill = companyBillList.get(0);
                companyBill.setIsPushed("1");
                count = tCompanyBillMapper.updateByPrimaryKeySelective(companyBill);
            }
            String res = "更新成功数量" + count;
            return ApiMessageUtil.success(res);
        } catch (ApiException e) {
            e.printStackTrace();
            throw new ApiException(ReqEnums.REQ_UPDATE_ERROR.getCode(), ReqEnums.REQ_UPDATE_ERROR.getMsg());
        }
    }
}
