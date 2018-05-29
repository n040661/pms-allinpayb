package com.dominator.utils.system;

/**
 * 页面工具
 * @author Administrator
 *
 */
public class PageTools {
	
   /**
    * 提示后2秒后关闭	
    */
   public static String alert(String title,String content,Integer timeout){
	   StringBuilder sb=new StringBuilder();
	   sb.append("<script>");
	   sb.append(String.format("msgTools.alert(\"%s\", \"%s\",%d);",title,content,timeout));
	   sb.append("</script>");
	   return sb.toString();
   }
   
   /**
    * 提示后2秒后关闭，回调	
    */
   public static String alert(String title,String content,Integer timeout,String callBack){
	   StringBuilder sb=new StringBuilder();
	   sb.append("<script>");
	   sb.append(String.format("msgTools.alert(\"%s\", \"%s\",%d,%s);",title,content,timeout,callBack));
	   sb.append("</script>");
	   return sb.toString();
   }
   
   /**
    * 不提示直接关闭当前iframe弹出层	
    */
   public static String closeFra(Integer timeout){
	   StringBuilder sb=new StringBuilder();
	   sb.append("<script>");
	   sb.append(String.format("msgTools.closeFra(%d);",timeout));
	   sb.append("</script>");
	   return sb.toString();
   }
   
   /**
    * 不提示直接关闭当前iframe弹出层，回调	
    */
   public static String closeFra(Integer timeout,String callBack){
	   StringBuilder sb=new StringBuilder();
	   sb.append("<script>");
	   sb.append(String.format("msgTools.closeFra(%d,%s);",timeout,callBack));
	   sb.append("</script>");
	   return sb.toString();
   }  
   
   /**
    * 提示2秒后关闭当前iframe弹出层	
    */
   public static String alertCloseFra(String title,String content,Integer timeout){
	   StringBuilder sb=new StringBuilder();
	   sb.append("<script>");
	   sb.append(String.format("msgTools.alertCloseFra(\"%s\", \"%s\",%d);",title,content,timeout));
	   sb.append("</script>");
	   return sb.toString();
   }
   
   /**
    * 提示2秒后关闭当前iframe弹出层，回调	
    */
   public static String alertCloseFra(String title,String content,Integer timeout,String callBack){
	   StringBuilder sb=new StringBuilder();
	   sb.append("<script>");
	   sb.append(String.format("msgTools.alertCloseFra(\"%s\",\"%s\",%d,%s);",title,content,timeout,callBack));
	   sb.append("</script>");
	   return sb.toString();
   }
   
   /**
    * 提示2秒后关闭当前iframe弹出层，并刷新内容主框架	
    */
   public static String alertCloseFraRefresh(String title,String content,Integer timeout){
	   StringBuilder sb=new StringBuilder();
	   sb.append("<script>");
	   sb.append(String.format("msgTools.alertCloseFraRefresh(\"%s\", \"%s\",%d,true);",title,content,timeout));
	   sb.append("</script>");
	   return sb.toString();
   }
   
   /**
    * 提示2秒后关闭当前iframe弹出层，并刷新内容主框架，回调
    */
   public static String alertCloseFraRefresh(String title,String content,Integer timeout,String callBack){
	   StringBuilder sb=new StringBuilder();
	   sb.append("<script>");
	   sb.append(String.format("msgTools.alertCloseFraRefresh(\"%s\", \"%s\",%d,true,%s);",title,content,timeout,callBack));
	   sb.append("</script>");
	   return sb.toString();
   }  
}
