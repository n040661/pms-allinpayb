//package com.dominator.utils.excel;
//
//import com.dominator.dto.StudentVo;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/excel")
//public class ExcelTest {
//
//
//    /*@RequestMapping（"／import"）
//    public String importexcel(){
//
//        return ""；
//    }*/
//    public static void main(String[] args) {
//        List<StudentVo> list = new ArrayList<StudentVo>();
//        StudentVo sy = new StudentVo();
//        sy.setId(1);
//        sy.setName("李坤");
//        sy.setAge(26);
//        sy.setClazz("五期提高班");
//        sy.setCompany("天融信");
//        list.add(sy);
//        StudentVo syq = new StudentVo();
//        syq.setId(2);
//        syq.setName("曹贵生");
//        syq.setClazz("五期提高班");
//        syq.setCompany("中银");
//        list.add(syq);
//
//        OutputStream out = null;
//        try {
//            out = new FileOutputStream(new File("test.xls"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        ExcelUtilAll<StudentVo> util = new ExcelUtilAll<StudentVo>(StudentVo.class);// 创建工具类.
//        try {
//            // exportExcel(List<T> list, OutputStream output, String templatePath, int startrow, Map<String, String> map, boolean ishead)
//            String[] sheetNames = new String[]{"学生信息"};
//            Map map = new HashMap();
////              public boolean exportExcel(List<T> list, String sheetName, OutputStream output, int startrow, Map<String, String> map, boolean isneeshead) {
//            util.exportExcel(list, "hahh.xlsx", out, 1, map, true);// 导出
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//}
