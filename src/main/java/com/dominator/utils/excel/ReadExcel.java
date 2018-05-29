package com.dominator.utils.excel;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.enums.BillEnums;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadExcel {

    private static final Logger log = LoggerFactory.getLogger(ReadExcel.class);
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;

    //构造方法
    public ReadExcel() {
    }

    //获取总行数
    public int getTotalRows() {
        return totalRows;
    }

    //获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    //获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }

    public Map<String,List<Dto>> parseExcel(MultipartFile mFile){
        Map<String,List<Dto>> map = new HashMap<>();
        String fileName = mFile.getOriginalFilename();
        System.out.println("【EXcel表格的名称："+fileName+"】");
        try {
            if (!validateExcel(fileName)) {
                return null;
            }
            boolean isExcel2003 = true;
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }

            POIFSFileSystem fs=new POIFSFileSystem(mFile.getInputStream());
            Workbook wb = null;
            if (isExcel2003) { //excel2003
                wb = new HSSFWorkbook(fs);
            } else {        //excel2007
                wb = new XSSFWorkbook(mFile.getInputStream());
            }

            HSSFSheet sheet=null;
            String sheetName=null;
            int numberOfSheets = wb.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                sheet = (HSSFSheet) wb.getSheetAt(i);
                sheetName = sheet.getSheetName();
                if(BillEnums.PMSSAAS_WATER_BILL.getMsg().equals(sheetName)){
                    map.put("water_bill",this.getPhysicalNumberOfCells2(sheet));
                }else if (BillEnums.PMSSAAS_ELECTRIC_CHARGE_BILL.getMsg().equals(sheetName)){
                    map.put("electric_charge_bill",this.getPhysicalNumberOfCells2(sheet));
                }else if (BillEnums.PMSSAAS_GAS_BILL.getMsg().equals(sheetName)){
                    map.put("gas_bill",this.getPhysicalNumberOfCells2(sheet));
                }else if(BillEnums.PMSSAAS_RENT_BILL.getMsg().equals(sheetName)){
                    map.put("rent_bill",this.getPhysicalNumberOfCells2(sheet));
                }else if (BillEnums.PMSSAAS_PROPERTY_BILL.getMsg().equals(sheetName)){
                    map.put("property_bill",this.getPhysicalNumberOfCells2(sheet));
                }else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public  List<List<String>> getPhysicalNumberOfCells(HSSFSheet sheet){
        if (sheet==null){
            return null;
        }
        List<List<String>> lists=new ArrayList<>();
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            if(i==0||i==2||i==1) continue;
            HSSFRow row=sheet.getRow(i);
            List<String> list=new ArrayList<>();
            if(row!=null&&row.getCell(0)!=null){
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    HSSFCell cell = row.getCell(j);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell!=null){
                        list.add(cell.getStringCellValue());
                    }else {
                      list.add("");
                    }
                }
                lists.add(list);
            }else {
                return lists;
            }
        }
        return lists;
    }

    public List<Dto> getPhysicalNumberOfCells2( HSSFSheet sheet){
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<Dto> billDtos = new ArrayList<>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                break;
            }
            Cell cell1 = row.getCell(0);
            if (null == cell1) {
                break;
            }
            if (cell1.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                if (SystemUtils.isEmpty(String.valueOf(cell1.getNumericCellValue()))) {
                    break;
                }
            } else {
                if (SystemUtils.isEmpty(cell1.getStringCellValue())) {
                    break;
                }
            }
            List list = new ArrayList();
            // 循环Excel的列
            for (int c = 1; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    //如果是纯数字,比如你写的是25,cell.getNumericCellValue()获得是25.0,通过截取字符串去掉.0获得25
                    if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        String name = "";
                        System.out.println("====");
                        if (cell.getNumericCellValue() > 10000000000d && (double)((long)cell.getNumericCellValue())==cell.getNumericCellValue())
                            name = String.valueOf((long) cell.getNumericCellValue()).trim();
                        else
                            name = String.valueOf(cell.getNumericCellValue()).trim();
                        list.add(name);//名称
                    } else {
                        list.add(cell.getStringCellValue().trim());//名称
                    }
                }
            }
            Dto dto = new HashDto();
            dto.put("list", list);
            // 添加到list
            billDtos.add(dto);
        }
        if (billDtos.size() > 1) {
            billDtos.remove(1);
        }
        return billDtos;
    }
    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param
     * @return
     */
    public List<Dto> getExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();//获取文件名
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            List<Dto> userList = createExcel(mFile.getInputStream(), isExcel2003);
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<Dto> createExcel(InputStream is, boolean isExcel2003) {
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            List<Dto> userList = readExcelValue(wb);// 读取Excel里面客户的信息
            return userList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    private List<Dto> readExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<Dto> userList = new ArrayList<>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                break;
            }
            Cell cell1 = row.getCell(0);
            if (null == cell1) {
                break;
            }
            if (cell1.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                if (SystemUtils.isEmpty(String.valueOf(cell1.getNumericCellValue()))) {
                    break;
                }
            } else {
                if (SystemUtils.isEmpty(cell1.getStringCellValue())) {
                    break;
                }
            }

            List list = new ArrayList();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    //如果是纯数字,比如你写的是25,cell.getNumericCellValue()获得是25.0,通过截取字符串去掉.0获得25
                    if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        String name = "";
                        if (cell.getNumericCellValue() > 10000000000d && (double)((long)cell.getNumericCellValue())==cell.getNumericCellValue())
                            name = String.valueOf((long) cell.getNumericCellValue()).trim();
                        else
                            name = String.valueOf(cell.getNumericCellValue()).trim();
                        list.add(name);//名称
                    } else {
                        list.add(cell.getStringCellValue().trim());//名称
                    }
                }
            }
            log.info("=========循环List========="+list);
            Dto dto = new HashDto();
            dto.put("list", list);
            // 添加到list
            userList.add(dto);
        }
        if (userList.size() > 1) {
            userList.remove(1);
        }
//        log.info("userList:{}", userList.toString());
        return userList;
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }


}
