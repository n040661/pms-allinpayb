package com.dominator.utils.system;


import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class comparatorUtil<T> implements Comparator<T> {

    Class<T> clazz;

    boolean is_asc ;

    public Field filed = null;

    public comparatorUtil(Class<T> clazz,boolean b,String Field) {
        this.clazz = clazz;
        is_asc = b;
        try {
           filed = clazz.getDeclaredField(Field);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compare(T o1, T o2) {
        int a = 0,b = 0;
        Date date1 = null,date2 = null;
        Class<?> fieldType = filed.getType();
        filed.setAccessible(true);
        if(fieldType.getName().equals("java.util.Date")){
            try {
                if (is_asc) {
                    date1 = (Date) filed.get(o1);
                    date2 = (Date) filed.get(o2);
                } else {
                    date1 = (Date) filed.get(o1);
                    date2 = (Date) filed.get(o2);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(date1!=null && date2!=null){

                if (date1.before(date2)) {
                    return 1;
                }
                else
                    return -1;
            }else{
                return  -1;
            }
        }else{
            try {
                if (is_asc) {
                    a = getintByString(filed.get(o1).toString());
                    b = getintByString(filed.get(o2).toString());
                } else {
                    a = getintByString(filed.get(o2).toString());
                    b = getintByString(filed.get(o1).toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (a > b) {
                return 1;
            } else if (a == b)
                return 0;
            else
                return -1;
        }


    }

    /**
     * 将字符串转成int
     * @param str
     * @return
     */
    public int getintByString(String str){
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(str);
        String result = m.replaceAll("");
        int i = Integer.parseInt(result);
        return i;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        /*T_repairPO t_repairPO  = new T_repairPO();
        Class clazz = t_repairPO.getClass();
        Field field = clazz.getDeclaredField("modify_time");
        Class<?> fieldType = field.getType();
        System.out.printf(fieldType.getName());*/
        String expect_time = "2017年10月25日";
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(expect_time);
        String result = m.replaceAll("");
        System.out.printf(result);
        int i = Integer.parseInt(result);
        System.out.printf(String.valueOf(i));
    }
}
