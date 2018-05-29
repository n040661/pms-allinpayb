package com.dominator.utils.excel;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/***
 * @author gsh
 */
public class ExcelExportor {
    /**
     * modified by gsh
     *
     * @param fileName    EXCEL文件名称
     *                    EXCEL文件第一行列标题集合
     * @param listContent EXCEL文件正文数据集合
     * @return demo: String[] title=new
     * String[]{"项目名称","现金","商币","会员名","付款单号","外部单号","备注","支付方式","支付时间",
     * "是否支付"}; ExcelExportor.exportExcel("后台管理员充值记录.xls", title, dat);
     */
    public final static String exportExcelByCfg(String fileName, List<ColumonCfg> cfg,
                                                List<Map<String, Object>> listContent) {
        String result = "系统提示：Excel文件导出成功！";
        // 以下开始输出到EXCEL
        try {
            // 定义输出流，以便打开保存对话框______________________begin
            // modified by gsh.begin
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attrs.getResponse();
            // modified by gsh.end

            // HttpServletResponse response =
            // ServletActionContext.getResponse();
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            // 设定输出文件头
            response.setContentType("application/msexcel");// 定义输出类型
            // 定义输出流，以便打开保存对话框_______________________end

            /** **********创建工作簿************ */
            WritableWorkbook workbook = Workbook.createWorkbook(os);

            /** **********创建工作表************ */

            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            /** **********设置纵横打印（默认为纵打）、打印纸***************** */
            jxl.SheetSettings sheetset = sheet.getSettings();
            sheetset.setProtected(false);
            /** ************设置单元格字体************** */
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 12);
            WritableFont BoldFont = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD);

            /** ************以下设置三种单元格样式，灵活备用************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(false); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
            wcf_left.setWrap(false); // 文字是否换行

            /** ***************以下是EXCEL开头大标题，暂时省略********************* */
            // sheet.mergeCells(0, 0, colWidth, 0);
            // sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
            /** ***************以下是EXCEL第一行列标题********************* */
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cfg.size(); i++) {
                sb.append("," + cfg.get(i).getFieldName() + ",");

                CellView navCellView = new CellView();
                //navCellView.setAutosize(true); // 设置自动大小
                navCellView.setSize(cfg.get(i).getWidth() * 40);
                sheet.setColumnView(i, navCellView);

                WritableCell cell = new Label(i, 0, cfg.get(i).getTitle(), wcf_center);
                sheet.addCell(cell);
            }
            String fieldStr = sb.toString();
            /** ***************以下是EXCEL正文数据********************* */
            // Field[] fields = null;
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int i = 1;
            for (Map<String, Object> row : listContent) {
                int j = 0;
                for (ColumonCfg v : cfg) {
                    if (!fieldStr.contains("," + v.getFieldName() + ","))
                        continue;
                    Object val = row.get(v.getFieldName());
                    //处理Date格式
                    if (val instanceof Date) {
                        val = sdf.format((Date) val);
                    }
                    sheet.addCell(new Label(j, i, String.format("%s", val == null ? "" : val), wcf_left));
                    j++;
                }
                i++;
            }
            /** **********将以上缓存中的内容写到EXCEL文件中******** */
            workbook.write();
            /** *********关闭文件************* */
            workbook.close();

        } catch (Exception e) {
            result = "系统提示：Excel文件导出失败，原因：" + e.toString();
            System.out.println(result);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param fileName    EXCEL文件名称
     *                    EXCEL文件第一行列标题集合
     * @param listContent EXCEL文件正文数据集合
     * @return demo: String[] title=new
     * String[]{"项目名称","现金","商币","会员名","付款单号","外部单号","备注","支付方式","支付时间",
     * "是否支付"}; ExcelExportor.exportExcel("后台管理员充值记录.xls", title, dat);
     */
    public final static String exportExcel(String fileName, String[] Title, List<Object> listContent){
        //无数据时，返回 0（int）
        if (listContent == null || listContent.size() == 0) {
            return "0";
        }
        String result = "系统提示：Excel文件导出成功！";
        // 以下开始输出到EXCEL
        try {
            // 定义输出流，以便打开保存对话框______________________begin
            // modified by gsh.begin
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attrs.getResponse();
            // modified by gsh.end

            // HttpServletResponse response =
            // ServletActionContext.getResponse();
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            // 设定输出文件头
            response.setContentType("application/msexcel");// 定义输出类型
            // 定义输出流，以便打开保存对话框_______________________end

            /** **********创建工作簿************ */
            WritableWorkbook workbook = Workbook.createWorkbook(os);

            /** **********创建工作表************ */

            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            /** **********设置纵横打印（默认为纵打）、打印纸***************** */
            jxl.SheetSettings sheetset = sheet.getSettings();
            sheetset.setProtected(false);

            /** ************设置单元格字体************** */
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

            /** ************以下设置三种单元格样式，灵活备用************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(false); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
            wcf_left.setWrap(false); // 文字是否换行

            /** ***************以下是EXCEL开头大标题，暂时省略********************* */
            // sheet.mergeCells(0, 0, colWidth, 0);
            // sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
            /** ***************以下是EXCEL第一行列标题********************* */
            // modified by gsh.begin
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Title.length; i++) {
                sb.append("," + Title[i] + ",");
                sheet.addCell(new Label(i, 0, Title[i], wcf_center));
            }
            String titleStr = sb.toString();
            // modified by gsh.end
            /** ***************以下是EXCEL正文数据********************* */
            Field[] fields = null;
            int i = 1;
            for (Object obj : listContent) {
                if (obj instanceof Map) {
                    int j = 0;
                    for (String v : Title) {
                        if (!titleStr.contains("," + v + ","))
                            continue;
                        Object val = ((Map) obj).get(v);
                        sheet.addCell(new Label(j, i, String.format("%s", val == null ? "" : val), wcf_left));
                        j++;
                    }
                } else {
                    fields = obj.getClass().getDeclaredFields();// 取得类的所有变量成员，obj可以实体对象或是Map
                    int j = 0;
                    for (Field v : fields) {
                        v.setAccessible(true);
                        if (!titleStr.contains("," + v.getName() + ","))
                            continue;
                        Object va = v.get(obj);// 取变量的值
                        if (va == null) {
                            va = "";
                        }
                        sheet.addCell(new Label(j, i, va.toString(), wcf_left));
                        j++;
                    }
                }
                i++;
            }
            /** **********将以上缓存中的内容写到EXCEL文件中******** */
            workbook.write();
            /** *********关闭文件************* */
            workbook.close();

        } catch (Exception e) {
            result = "系统提示：Excel文件导出失败，原因：" + e.toString();
            System.out.println(result);
            e.printStackTrace();
        }
        return result;
    }
}
