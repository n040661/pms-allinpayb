package com.dominator.serviceImpl;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.Dtos;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.entity.*;
import com.dominator.enums.ReqEnums;
import com.dominator.mapper.TCompanyBillMapper;
import com.dominator.mapper.TGardenMapper;
import com.dominator.mapper.ext.TCompanyBillExt;
import com.dominator.mapper.ext.TCompanyExt;
import com.dominator.service.BillService;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.api.ApiMessageUtil;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.dateutil.DateUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import com.dominator.utils.system.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static com.dominator.utils.dateutil.DateUtil.DatetoStringFormat;
import static com.dominator.utils.dateutil.DateUtil.getMonthBetween;
import static com.dominator.utils.dateutil.DateUtil.timeStampToString;

@Service
@Slf4j
public class BillServiceImpl implements BillService{

    @Autowired
    private TCompanyBillMapper tCompanyBillMapper;

    @Autowired
    private TGardenMapper tGardenMapper;

    @Autowired
    private TCompanyBillExt tCompanyBillExt;

    private RedisUtil ru = RedisUtil.getRu();

    @Override
    public List<TCompanyBill> listCompanyBill(Dto dto) {
        String companyId = dto.getString("companyId");
        String isPaid = dto.getString("isPaid");
        TCompanyBillExample companyBillExample = new TCompanyBillExample();
        companyBillExample.or().andCompanyIdEqualTo(companyId)
                .andIsPaidEqualTo(isPaid)
                .andIsValidEqualTo("1");
        return tCompanyBillMapper.selectByExample(companyBillExample);
    }

    @Override
    public ApiMessage overview(Dto dto) {
        String companyId = dto.getString("companyId");
        TCompanyBillExample tCompanyBillExample = new TCompanyBillExample();
        TCompanyBillExample.Criteria criteria = tCompanyBillExample.createCriteria();
        criteria.andCompanyIdEqualTo(companyId).andIsValidEqualTo("1").andIsPaidEqualTo("0");
        List<TCompanyBill> tCompanyBills = tCompanyBillMapper.selectByExample(tCompanyBillExample);
        BigDecimal totalFee = new BigDecimal(0);
        int monthCount = 0;
        for (TCompanyBill tCompanyBill : tCompanyBills){
            String billYearMonth = tCompanyBill.getBillYearMonth();
            String s = DateUtil.formatLocalDateTimeToString(LocalDateTime.now(), "yyyy-MM");
            if (s.equals(billYearMonth)){
                monthCount++;
            }
            totalFee = totalFee.add(tCompanyBill.getTotalFee()) ;
        }
        dto.clear();
        dto.put("monthCount",monthCount);
        dto.put("totalCount",tCompanyBills.size());
        dto.put("totalFee",totalFee);
        return ApiMessageUtil.success(dto);
    }

    @Override
    public Dto payOverview(Dto dto) {
        String gardenId = dto.getString("gardenId");
        Date startTime = dto.getDate("startTime");
        Date endTime = dto.getDate("endTime");
        String isPaid = dto.getString("isPaid");
        List<Dto> list = new ArrayList<>();
        BigDecimal amount;

        Calendar start = Calendar.getInstance();
        start.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        log.info("start:{}", start);
        log.info("end:{}", end);
        while (true) {
            Dto dto1 = new HashDto();
            dto1.put("start", startTime);
            dto1.put("gardenId", gardenId);
            dto1.put("isPaid", isPaid);
            amount = tCompanyBillExt.payOverview(dto1);

            dto1.clear();
            dto1.put("year", start.get(Calendar.YEAR));
            dto1.put("month", start.get(Calendar.MONTH) + 1);
            dto1.put("day", start.get(Calendar.DAY_OF_MONTH));
            dto1.put("amount", amount);

            list.add(dto1);
            start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH) + 1);
            log.info("amount:{}", amount);
            if (start.after(end)) {
                break;
            }
        }
        Dto resDto = new HashDto();
        resDto.put("payOverview", list);
        return resDto;
    }

    @Override
    public ApiMessage listBillsByPage(Dto dto) {
        String type = dto.getString("type");
        PageInfo<Dto> pageInfo = null;
        Page.startPage(dto.getInteger("pageNum"),dto.getInteger("pageSize"));
//        PagerUtil.getPager(dto, dto.getInteger("pageNum"), dto.getInteger("pageSize"));
//        List<Dto> bills = tCompanyBillDao.listBillsByPage(dto);
        List<Dto> bills = tCompanyBillExt.listBillsByPage(dto);
        pageInfo = new PageInfo<>(bills);
        Dto resDto = new HashDto();
//        resDto.put("bills", bills);
//        resDto.put("pageSize", dto.getString("pageSize"));
//        resDto.put("pageNum", dto.getString("pageNum"));
//        resDto.put("total", dto.getPageTotal());
        resDto.put("bills",pageInfo.getList());
        resDto.put("total",pageInfo.getTotal());
        resDto.put("pageSize",pageInfo.getPageSize());
        resDto.put("pageNum",pageInfo.getPageNum());
        return ApiMessageUtil.success(resDto);
    }

    /**
     * 根据公司id分页获取账单信息
     * @param dto
     * @return
     */
    @Override
    public ApiMessage getBillAll(Dto dto) {
        //页码
        Integer pageNum = dto.getInteger("pageNum");
        //每页多少数
        Integer pageSize = dto.getInteger("pageSize");

        /*String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);*/
        //用户名称
        //String userName = JwtToken.getString(token, "userName");
        //String userNameame = tokenDto.getString("userName");
        //获取公司id
        //o.gdto.put("companyId",tokenDtetString("companyId"));
        //dto.put("userName",userNameame);

        //默认是第一页
        if(pageNum == null){
            pageNum = 1;
        }
        //默认显示六个月的账单信息
        if(pageSize==null){
            pageSize = 6;
        }

        int startpage = 0;

        dto.put("startpage",startpage+(pageNum-1)*pageSize);
        dto.put("pageSize",pageSize);

        Dto billdto = new HashDto();
        List<Dto> list =  tCompanyBillExt.listpageByorder(dto);
        log.info("get bill by page size"+list.size());
        //JSONArray jsonArray = new JSONArray();
        Dto  dto1 = new HashDto();
        if(list!=null &&list.size()>0) {
            for (int j = 0; j < list.size(); j++) {
                String month = list.get(j).getString("bill_year_month");
                month = month.substring(0, 4) + "年" + month.substring(5, 7) + "月";
                String s = dto1.getString(month);
                if (StringUtils.isEmpty(s)) {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject json = getbilljson(list.get(j));
                    jsonArray.add(json);
                    dto1.put(month, jsonArray);

                } else {
                    JSONObject json = getbilljson(list.get(j));
                    JSONArray jsonArray = JSONArray.fromObject(dto1.getString(month));
                    jsonArray.add(json);
                    dto1.put(month, jsonArray);
                }

            }
        }
        JSONArray jsonArray = new JSONArray();
        for(String key : dto1.keySet()){
            JSONObject json = new JSONObject();
            json.accumulate("month",key);
            json.accumulate("array",dto1.get(key));
            jsonArray.add(json);
        }

        dto.clear();
        dto.put("pageSize",pageSize);
        dto.put("pageNum",pageNum);
        dto.put("total",jsonArray.size());
        dto.put("list",jsonArray);
        return ApiMessageUtil.success(dto);
    }

    /**
     * 根据月份条件查询账单信息
     * @param dto
     * @return
     * @throws ApiException
     */

    @Override
    public ApiMessage billbymonth(Dto dto) throws  ApiException {
//        List<Dto> list = t_company_billDao.billbymonth(dto);
        /*String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);

        String companyId = tokenDto.getString("companyId");
        String phoneNum = tokenDto.getString("userName");
        dto.put("companyId",companyId);
        dto.put("phoneNum",phoneNum);*/
        String month = dto.getString("billYearMonth");
        String billYearMonth = timeStampToString(Long.parseLong(month),"yyyy-MM");
        dto.put("billYearMonth",billYearMonth);
        List<Dto> list = tCompanyBillExt.billbymonth(dto);
        if(list == null || list.size() == 0){
            throw new ApiException(ReqEnums.REQ_MONTH_BILL_ERROR.getCode(), ReqEnums.REQ_MONTH_BILL_ERROR.getMsg());
        }
        Dto resDto = Dtos.newDto();
        resDto.put("month",timeStampToString(Long.parseLong(month),"yyyy年MM月"));
        JSONArray jsonArray = new JSONArray();
        for (Dto bill : list){
            // String type = bill.getFee_type();
            if(bill != null ){
                JSONObject json = getbilljson(bill);
                jsonArray.add(json);
            }
        }
        resDto.put("array",jsonArray);
        return ApiMessageUtil.success(resDto);
    }

    /**
     * 获取账单详情
     * @param dto
     * @return
     * @throws ApiException
     */
    @Override
    public ApiMessage billdetail(Dto dto) throws  ApiException{
       /* String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        log.info("billdetail");
        String garden_id = tokenDto.getString("gardenId");*/
       String gardenId = dto.getString("gardenId");
        String  billId = dto.getString("billId");
//        Dto dto1 =  tCompanyBillDao.getBillDetail(bill_id);
         Dto dto1 =  tCompanyBillExt.getBillDetail(billId);

//        T_gardenPO gardenPO = t_gardenDao.selectByKey(garden_id);

        TGardenExample tGardenExample = new TGardenExample();
        tGardenExample.or().andIdEqualTo(gardenId);
        List<TGarden>  gardenList =  tGardenMapper.selectByExample(tGardenExample);
        if(dto1==null || gardenList == null || gardenList.isEmpty()){
            throw new ApiException(ReqEnums.REQ_RESULT_NULL.getCode(), ReqEnums.REQ_RESULT_NULL.getMsg());
        }
        TGarden garden = gardenList.get(0);

        Dto resDto = Dtos.newDto();
        String month = dto1.getString("bill_year_month");
        month = month.substring(0, 4) + "年" + month.substring(5, 7) + "月";
        resDto.put("billYearMonth",month);
        Date date = dto1.getDate("push_time");
        String push_time = String.valueOf(date.getTime());
        resDto.put("expiryTime",push_time);//推送时间
        resDto.put("billName",dto1.getString("billName"));
        resDto.put("companyName",dto1.getString("company_name"));
        resDto.put("isPaid",dto1.getString("is_paid"));
        resDto.put("totalFee",dto1.getString("total_fee")==null?"0":dto1.getString("total_fee"));
        resDto.put("mainFee",dto1.getString("main_fee")==null?"0":dto1.getString("main_fee"));
        resDto.put("otherFee",dto1.getString("other_fee")==null?"0":dto1.getString("other_fee"));
        //只有电费和燃气费才有单价和度数
        if(dto1.getString("fee_type").equals("1")||dto1.getString("fee_type").equals("2")){
            resDto.put("feeUnit",dto1.getString("fee_unit")==null?"0":dto1.getString("fee_unit"));//单位费用
            resDto.put("feeDgree",dto1.getString("fee_dgree")==null?"0":dto1.getString("fee_dgree"));//度数
        }
        resDto.put("feeType",dto1.getString("fee_type"));//费用类型
        resDto.put("billNum",dto1.getString("bill_num")); //账单号
        resDto.put("phoneNum",garden.getPhoneNum());//园区联系方式
        resDto.put("bankName",garden.getBankName());//收款银行
        resDto.put("collectionUnit",garden.getCollectionUnit());//收款单位
        resDto.put("bankNum",garden.getBankNum());//银行账户号


        return ApiMessageUtil.success(resDto);
    }

    /**]
     * 根据公司id获取所有的月份是否欠费信息
     * @param dto
     * @return
     * @throws ApiException
     */
    @Override
    public ApiMessage billallbycompanyid(Dto dto) throws  ApiException{
        String companyId = dto.getString("companyId");
        //根据公司id查询该公司所有的月份的账单信息
        TCompanyBillExample tCompanyBillExample = new TCompanyBillExample();
        tCompanyBillExample.or().andCompanyIdEqualTo(companyId).andIsValidEqualTo("1");
        List<TCompanyBill> billList = tCompanyBillMapper.selectByExample(tCompanyBillExample);
        String startDate = "2017-08";
        Date nowDate = new Date();
        String endDate = DatetoStringFormat(nowDate,"yyyy-MM");
        //获取所有月份
        List<String> monthlist = getMonthBetween(startDate, endDate);
        //有账单月份
        List<String> monthbill = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray();
        if (billList != null && billList.size() > 0) {
            for (TCompanyBill bill : billList) {
                if (bill != null) {
                    monthbill.add(bill.getBillYearMonth());
                }
            }
        }
        for (String month : monthlist) {
            JSONObject json = new JSONObject();
            if (monthbill.contains(month)) {
                json.accumulate("month", month);
                json.accumulate("isHaveBill", "1");//该月是否有账单（1有账单，0无账单）
            } else {
                json.accumulate("month", month);
                json.accumulate("isHaveBill", "0");
            }
            jsonArray.add(json);
        }

        return ApiMessageUtil.success(jsonArray);
    }

    public JSONObject getbilljson(Dto dto){
        JSONObject json  = new JSONObject();
        json.accumulate("billId",dto.getString("id"));
        json.accumulate("billName",dto.getString("billName"));
        json.accumulate("totalFee",dto.getString("total_fee")==null?"0":dto.getString("total_fee"));
        json.accumulate("isPaid",dto.getString("is_paid"));
        Date push_time = dto.getDate("push_time");
        String sb = String.valueOf(push_time.getTime());
        json.accumulate("expiryTime",sb);//推送时间
        return json;
    }

}

