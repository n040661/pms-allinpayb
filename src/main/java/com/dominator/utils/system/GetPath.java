package com.dominator.utils.system;

public class GetPath {
    /**
     * 获取webinf 的绝对路径
     * @return
     */
    public static String getXmlPath(String xmlpath)
    {
        String path=Thread.currentThread().getContextClassLoader().getResource("").toString();
        path=path.replace("file:", ""); //去掉file:
        path=path.replace("classes/", ""); //去掉class\
        path=path + xmlpath;
        return path;
    }
}
