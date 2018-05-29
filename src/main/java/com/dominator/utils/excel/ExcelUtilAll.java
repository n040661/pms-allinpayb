//package com.dominator.utils.excel;
//
//import org.apache.commons.lang3.time.DateFormatUtils;
//import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.util.CellRangeAddressList;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.*;
//import java.lang.reflect.Field;
//import java.text.DecimalFormat;
//import java.util.*;
//
///*
// * ExcelUtil工具类实现功能:
// * 导出时传入list<T>,即可实现导出为一个excel,其中每个对象Ｔ为Excel中的一条记录.
// * 导入时读取excel,得到的结果是一个list<T>.T是自己定义的对象.
// * 需要导出的实体对象只需简单配置注解就能实现灵活导出,通过注解您可以方便实现下面功能:
// * 1.实体属性配置了注解就能导出到excel中,每个属性都对应一列.
// * 2.列名称可以通过注解配置.
// * 3.导出到哪一列可以通过注解配置.
// * 4.鼠标移动到该列时提示信息可以通过注解配置.
// * 5.用注解设置只能下拉选择不能随意填写功能.
// * 6.用注解设置是否只导出标题而不导出内容,这在导出内容作为模板以供用户填写时比较实用.
// */
//public class ExcelUtilAll<T> {
//
//    Class<T> clazz;
//
//    public ExcelUtilAll(Class<T> clazz) {
//        this.clazz = clazz;
//    }
//
//    /**
//     * @param sheetName sheet 名 不填默认第一个
//     * @param input     输入流
//     * @param startrow  第几行开始读取数字 0表示第一行 ；
//     **/
//    public List<T> importExcel(String sheetName, InputStream input, int startrow, String name) throws Exception {
//        int maxCol = 0;
//        List<T> list = new ArrayList<T>();
//        try {
//            Workbook workbook  = null;
//            if (name.endsWith("xls")) {
//                workbook  = new HSSFWorkbook(input);
//            } else if (name.endsWith("xlsx")) {
//                workbook = new XSSFWorkbook(input);
//            }
//            Sheet sheet = workbook.getSheet(sheetName);
//            if (!sheetName.trim().equals("")) {
//                sheet = workbook.getSheet(sheetName);// 如果指定sheet名,则取指定sheet中的内容.
//            }
//            if (sheet == null) {
//                sheet = workbook.getSheetAt(0); // 如果传入的sheet名不存在则默认指向第1个sheet.
//            }
//            int rows = sheet.getLastRowNum();
//            if (rows > 0) {// 有数据时才处理
//                // Field[] allFields = clazz.getDeclaredFields();// 得到类的所有field.
//                List<Field> allFields = getMappedFiled(clazz, null);
//                Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();// 定义一个map用于存放列的序号和field.
//                for (Field field : allFields) {
//                    // 将有注解的field存放到map中.
//                    if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
//                        ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
//                        int col = getExcelCol(attr.column());// 获得列号
//                        maxCol = Math.max(col, maxCol);
//                        field.setAccessible(true);// 设置类的私有字段属性可访问.
//                        fieldsMap.put(col, field);
//                    }
//                }
//                for (int i = startrow; i <= rows; i++) {// 从第2行开始取数据,默认第一行是表头.
//                    Row row = sheet.getRow(i);
//                    int cellNum = maxCol;
//                    T entity = null;
//                    for (int j = 0; j <= cellNum; j++) {
//                        if (row == null) {
//                            row = sheet.createRow(i);
//                        }
//                        Cell cell = row.getCell(j);
//                        if (cell == null) {
//                            continue;
//                        }
//                        Field field = fieldsMap.get(j);// 从map中得到对应列的field.
//                        if (field == null) {
//                            continue;
//                        }
//                        Class<?> fieldType = field.getType();
//                        int cellType = cell.getCellType();
//                        String c = "";
//                        if (cellType == XSSFCell.CELL_TYPE_NUMERIC) {
//                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
//                                Date date = cell.getDateCellValue();
//                                c = DateFormatUtils.format(date, "yyyy-MM-dd");
//                            } else {
//                                c = String.valueOf(cell.getNumericCellValue());
//                                if (String.class == fieldType) {//
//                                    DecimalFormat df = new DecimalFormat("#");
//                                    c = df.format(cell.getNumericCellValue());
//                                }
//                            }
//                        } else if (cellType == XSSFCell.CELL_TYPE_BOOLEAN) {
//                            c = String.valueOf(cell.getBooleanCellValue());
//                        } else if (cellType == XSSFCell.CELL_TYPE_FORMULA) {
//                            c = String.valueOf(cell.getNumericCellValue());
//                        } else {
//                            c = cell.getStringCellValue();
//                        }
//                        if (c == null || c.equals("")) {
//                            continue;
//                        }
//                        entity = (entity == null ? clazz.newInstance() : entity);// 如果不存在实例则新建.
//                        // 取得类型,并根据对象类型设置值.
//                        if (String.class == fieldType) {
//                            field.set(entity, String.valueOf(c));
//                        } else if (Integer.TYPE == fieldType || Integer.class == fieldType) {
//                            if (String.class == fieldType) {//
//                                DecimalFormat df = new DecimalFormat("#");
//                                c = df.format(cell.getNumericCellValue());
//                            }
//                            field.set(entity, Double.valueOf(c).intValue());
//                        } else if (Long.TYPE == fieldType || Long.class == fieldType) {
//                            field.set(entity, Long.valueOf(c));
//                        } else if (Float.TYPE == fieldType || Float.class == fieldType) {
//                            field.set(entity, Float.valueOf(c));
//                        } else if (Short.TYPE == fieldType || Short.class == fieldType) {
//                            field.set(entity, Short.valueOf(c));
//                        } else if (Double.TYPE == fieldType || Double.class == fieldType) {
//                            field.set(entity, Double.valueOf(c));
//                        } else if (Character.TYPE == fieldType && c != null && c.length() > 0) {
//                            field.set(entity, Character.valueOf(c.charAt(0)));
//                        }
//                    }
//                    if (entity != null) {
//                        list.add(entity);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    /**
//     * 对list数据源将其里面的数据导入到excel表单
//     *
//     * @param
//     * @param output            java输出流
//     * @param startrow          数据开始的行数 ， 0表示第一行 最少是1
//     * @param map               自定义修改列名 键值对满足 map.put("A","学生姓名") 类行 ，第一个是列的编号,第二个是列的名称
//     * @param ismodifytablehead 是否修改表头名称 ，false 不修改 ，map参数也视为无效
//     */
//    private boolean exportExcel(List<T> lists[], String sheetNames[], OutputStream output, int startrow, Map<String, String> map, boolean ismodifytablehead) {
//        if (lists.length != sheetNames.length) {
//            return false;
//        }
//        HSSFWorkbook workbook = new HSSFWorkbook();// 产生工作薄对象
//        for (int ii = 0; ii < lists.length; ii++) {
//            List<T> list = lists[ii];
//            String sheetName = sheetNames[ii];
//            HSSFSheet sheet = workbook.createSheet();// 产生工作表对象
//            workbook.setSheetName(ii, sheetName);
//            getworkbook(workbook, startrow,  map,ismodifytablehead, list, sheet);
//        }
//        try {
//            output.flush();
//            workbook.write(output);
//            output.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//  /*  *//**
//     * 对list数据源将其里面的数据导入到excel表单
//     *
//     * @param
//     * @param output            java输出流
//     * @param templatePath      文件路径
//     * @param startrow          数据开始的行数 0表示第一行 至少为1 * @param map 自定义修改列名 键值对满足 map.put("A","学生姓名") 类行 ，第一个是列的编号,第二个是列的名称
//     * @param ismodifytablehead 是否修改表头名称 ，false 不修改 ，map参数也视为无效
//     * @throws IOException
//     * @throws FileNotFoundException
//     */
//    private boolean exportExcel(List<T> lists[], OutputStream output, String templatePath, int startrow, Map<String, String> map, boolean ismodifytablehead) throws FileNotFoundException, IOException {
//        FileInputStream fis = null;
//        fis = new FileInputStream(templatePath);
//        HSSFWorkbook workbook = new HSSFWorkbook(fis);
//        List<T> list = lists[0];
//        //List<Field> fields = getMappedFiled(clazz, null);
//        HSSFSheet sheet = workbook.getSheetAt(0);// 产生工作表对象
//        // workbook.setSheetName(ii, sheetName);
//        getworkbook(workbook, startrow,  map,ismodifytablehead, list,sheet);
//
//        try {
//            output.flush();
//            workbook.write(output);
//            output.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean exportExcel(List<T> list, String sheetName, OutputStream output, int startrow, Map<String, String> map, boolean isneeshead) {
//        // 此处 对类型进行转换
//        List<T> ilist = new ArrayList<T>();
//        for (T t : list) {
//            ilist.add(t);
//        }
//        List<T>[] lists = new ArrayList[1];
//        lists[0] = ilist;
//        String[] sheetNames = new String[1];
//        if (sheetName == null || sheetName.trim().length() == 0) {
//            sheetName = "Sheet1";
//        }
//        sheetNames[0] = sheetName;
//        return exportExcel(lists, sheetNames, output, startrow, map, isneeshead);
//    }
//
//    /**
//     * 对list数据源将其里面的数据导入到excel表单
//     *
//     * @param
//     * @param
//     * @param templatePath 模版文件路径
//     * @param output       java输出流
//     * @param
//     * @throws IOException
//     * @throws FileNotFoundException
//     */
//    public boolean exportExcel(List<T> list, OutputStream output, String templatePath, int startrow, Map<String, String> map, boolean ishead) throws FileNotFoundException, IOException {
//        // 此处 对类型进行转换
//        List<T> ilist = new ArrayList<T>();
//        for (T t : list) {
//            ilist.add(t);
//        }
//        List<T>[] lists = new ArrayList[1];
//        lists[0] = ilist;
//        return exportExcel(lists, output, templatePath, startrow, map, ishead);
//    }
//
//    /**
//     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
//     *
//     * @param col
//     */
//    private static short getExcelCol(String col) {
//        col = col.toUpperCase();
//        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
//        short count = -1;
//        char[] cs = col.toCharArray();
//        for (int i = 0; i < cs.length; i++) {
//            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
//        }
//        return count;
//    }
//
//    /**
//     * 设置单元格上提示
//     *
//     * @param sheet         要设置的sheet.
//     * @param promptTitle   标题
//     * @param promptContent 内容
//     * @param firstRow      开始行
//     * @param endRow        结束行
//     * @param firstCol      开始列
//     * @param endCol        结束列
//     * @return 设置好的sheet.
//     */
//    private static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle, String promptContent, int firstRow, int endRow, int firstCol, int endCol) {
//        // 构造constraint对象
//        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");
//        // 四个参数分别是：起始行、终止行、起始列、终止列
//        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
//        // 数据有效性对象
//        HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
//        data_validation_view.createPromptBox(promptTitle, promptContent);
//        sheet.addValidationData(data_validation_view);
//        return sheet;
//    }
//
//    /**
//     * 设置某些列的值只能输入预制的数据,显示下拉框.
//     *
//     * @param sheet    要设置的sheet.
//     * @param textlist 下拉框显示的内容
//     * @param firstRow 开始行
//     * @param endRow   结束行
//     * @param firstCol 开始列
//     * @param endCol   结束列
//     * @return 设置好的sheet.
//     */
//    private static HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
//        // 加载下拉列表内容
//        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
//        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
//        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
//        // 数据有效性对象
//        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
//        sheet.addValidationData(data_validation_list);
//        return sheet;
//    }
//
//    /**
//     * 得到实体类所有通过注解映射了数据表的字段
//     *
//     * @param
//     * @return
//     */
//    @SuppressWarnings("rawtypes")
//    private List<Field> getMappedFiled(Class clazz, List<Field> fields) {
//        if (fields == null) {
//            fields = new ArrayList<Field>();
//        }
//        Field[] allFields = clazz.getDeclaredFields();// 得到所有定义字段
//        // 得到所有field并存放到一个list中.
//        for (Field field : allFields) {
//            if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
//                fields.add(field);
//            }
//        }
//        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
//            getMappedFiled(clazz.getSuperclass(), fields);
//        }
//        return fields;
//    }
//
//    /*
//        * 列头单元格样式
//        */
//    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
//
//        // 设置字体
//        HSSFFont font = workbook.createFont();
//        //设置字体大小
//        font.setFontHeightInPoints((short)11);
//        //字体加粗
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        //设置字体名字
//        font.setFontName("Courier New");
//        //设置样式;
//        HSSFCellStyle style = workbook.createCellStyle();
//        //设置底边框;
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        //设置底边框颜色;
//        style.setBottomBorderColor(HSSFColor.BLACK.index);
//        //设置左边框;
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        //设置左边框颜色;
//        style.setLeftBorderColor(HSSFColor.BLACK.index);
//        //设置右边框;
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        //设置右边框颜色;
//        style.setRightBorderColor(HSSFColor.BLACK.index);
//        //设置顶边框;
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        //设置顶边框颜色;
//        style.setTopBorderColor(HSSFColor.BLACK.index);
//        //在样式用应用设置的字体;
//        style.setFont(font);
//        //设置自动换行;
//        style.setWrapText(false);
//        //设置水平对齐的样式为居中对齐;
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        //设置垂直对齐的样式为居中对齐;
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//
//        return style;
//
//    }
//
//    /*
//     * 列数据信息单元格样式
//     */
//    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
//        // 设置字体
//        HSSFFont font = workbook.createFont();
//        //设置字体大小
//        //font.setFontHeightInPoints((short)10);
//        //字体加粗
//        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        //设置字体名字
//        font.setFontName("Courier New");
//        //设置样式;
//        HSSFCellStyle style = workbook.createCellStyle();
//        //设置底边框;
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        //设置底边框颜色;
//        style.setBottomBorderColor(HSSFColor.BLACK.index);
//        //设置左边框;
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        //设置左边框颜色;
//        style.setLeftBorderColor(HSSFColor.BLACK.index);
//        //设置右边框;
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        //设置右边框颜色;
//        style.setRightBorderColor(HSSFColor.BLACK.index);
//        //设置顶边框;
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        //设置顶边框颜色;
//        style.setTopBorderColor(HSSFColor.BLACK.index);
//        //在样式用应用设置的字体;
//        style.setFont(font);
//        //设置自动换行;
//        style.setWrapText(false);
//        //设置水平对齐的样式为居中对齐;
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        //设置垂直对齐的样式为居中对齐;
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//
//        return style;
//
//    }
//
//    /**
//     * 数据放入workbook对象中
//     * @param workbook
//     * @param startrow
//     * @param map
//     * @param ismodifytablehead
//     * @param list
//     * @param sheet
//     */
//
//    public void getworkbook(HSSFWorkbook workbook,int startrow, Map<String, String> map, boolean ismodifytablehead,List<T> list,HSSFSheet sheet){
//        HSSFRow row;
//        HSSFCell cell;// 产生单元格
//        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
//        HSSFCellStyle style = this.getStyle(workbook);                  //单元格样式对象
//        List<Field> fields = getMappedFiled(clazz, null);    //反射获取字段
//        int columnnum = 0;//设置列数
//        // HSSFCellStyle style = workbook.createCellStyle();
//        //style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//        //style.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
//        // 写入各个字段的列头名称
//        if (ismodifytablehead) {
//            row = sheet.createRow(startrow - 1);// 产生一行
//            for (int i = 0; i < fields.size(); i++) {
//                Field field = fields.get(i);
//                columnnum = fields.size();
//                ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
//                short col = getExcelCol(attr.column());// 获得列号
//                cell = row.createCell(col);// 创建列
//                cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置列中写入内容为String类型
//                cell.setCellValue(attr.name());// 写入列名
//                // 如果设置了提示信息则鼠标放上去提示.
//                if (!attr.prompt().trim().equals("")) {
//                    setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);// 这里默认设了2-101列提示.
//                }
//                // 如果设置了combo属性则本列只能选择不能输入
//                if (attr.combo().length > 0) {
//                    setHSSFValidation(sheet, attr.combo(), 1, 100, col, col);// 这里默认设了2-101列只能选择不能输入.
//                }
//                cell.setCellStyle(columnTopStyle);
//            }
//            if (map != null&&map.size()>0) {
//                row = sheet.createRow(startrow - 1);// 产生一行
//                Set<String> keyset = map.keySet();
//                for (String key : keyset) {
//                    short col = getExcelCol(key);// 获得列号
//                    cell = row.createCell(col);// 创建列
//                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置列中写入内容为String类型
//                    cell.setCellValue(map.get(key));// 写入列名
//                    cell.setCellStyle(columnTopStyle);
//                    columnnum = map.size();
//                }
//            }
//        }
//        int startNo = 0;
//        int endNo = list.size();
//        // 写入各条记录,每条记录对应excel表中的一行
//        for (int i = startNo; i < endNo; i++) {
//            row = sheet.createRow(i + startrow - startNo);
//            T vo = (T) list.get(i); // 得到导出对象.
//            for (int j = 0; j < fields.size(); j++) {
//                Field field = fields.get(j);// 获得field.
//                field.setAccessible(true);// 设置实体类私有属性可访问
//                ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
//                try {
//                    // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
//                    if (attr.isExport()) {
//                        cell = row.createCell(getExcelCol(attr.column()));// 创建cell
//                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//                        cell.setCellValue(field.get(vo) == null ? "" : String.valueOf(field.get(vo)));// 如果数据存在就填入,不存在填入空格.
//                        cell.setCellStyle(style);
//                    }
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        //让列宽随着导出的列长自动适应
//        for (int colNum = 0; colNum < columnnum; colNum++) {
//            int columnWidth = sheet.getColumnWidth(colNum) / 256;
//            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
//                HSSFRow currentRow;
//                //当前行未被使用过
//                if (sheet.getRow(rowNum) == null) {
//                    currentRow = sheet.createRow(rowNum);
//                } else {
//                    currentRow = sheet.getRow(rowNum);
//                }
//                if (currentRow.getCell(colNum) != null) {
//                    HSSFCell currentCell = currentRow.getCell(colNum);
//                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//                        int length = currentCell.getStringCellValue().getBytes().length;
//                        if (columnWidth < length) {
//                            columnWidth = length;
//                        }
//                    }
//                }
//            }
//            if(colNum == 0){
//                sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
//            }else{
//                sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
//            }
//        }
//    }
//
//}