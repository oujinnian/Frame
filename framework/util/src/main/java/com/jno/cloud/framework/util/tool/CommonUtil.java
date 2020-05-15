package com.jno.cloud.framework.util.tool;

import com.alibaba.fastjson.JSONObject;
import com.jno.cloud.framework.util.base.TreeBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 基本工具包
 * 字符串处理，日期处理，IP获取，GUID生成，等等
 */
public class CommonUtil {

    private final static String CODES = "0123456789abcdefghijklmnopqrstuvwxyzABCDZFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 判断字符串是否为空
     *
     * @param str String
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return null == str || str.isEmpty();
    }

    /**
     * 判断字符串是否非空
     *
     * @param str String
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 根据自定义日期格式，String转Date
     *
     * @param str    日期字符串
     * @param format 自定义日期格式
     * @return Date
     */
    public static Date stringToDate(String str, String format) {
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * LocalDateTime转换为指定格式的时间字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String localDateTimeToString(LocalDateTime date, String pattern) {
        if (date == null) {
            return null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return formatter.format(date);
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss日期字符串转Timestamp
     *
     * @param str 日期字符串
     * @return Timestamp
     */
    public static Timestamp stringToDate(String str) {
        return Timestamp.valueOf(str);
    }

    /**
     * 日期字符串转Timestamp
     *
     * @param str    日期字符串
     * @param format SimpleDateFormat
     * @return Timestamp
     */
    public static Timestamp stringToTimestamp(String str, SimpleDateFormat format) {
        try {
            return new Timestamp(format.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 自定义日期格式，日期字符串转Timestamp
     *
     * @param str    日期字符串
     * @param format 自定义日期格式
     * @return Timestamp
     */
    public static Timestamp stringToTimestamp(String str, String format) {
        try {
            return new Timestamp(new SimpleDateFormat(format).parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 随机字符串
     *
     * @param length 随机字符串长度
     * @return String
     */
    public static String randomString(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rand = (int) (Math.random() * CODES.length());
            result.append(CODES.charAt(rand));
        }
        return result.toString();
    }

    /**
     * 翻页工具：结束行
     *
     * @param page  页码
     * @param limit 行数
     * @return int
     */
    public static int formatPageEnd(int page, int limit) {
        return limit;
    }

    /**
     * 返回非空
     *
     * @param obj 字符串Object
     * @return String
     */
    public static String trimToEmity(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * Object 转 BigDecimal
     *
     * @param value 数据源
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass()
                        + " into a BigDecimal.");
            }
        }
        return ret;
    }

    /**
     * 返回自定义GUID
     *
     * @return String
     */
    public static String GUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getPostInputStream(ServletRequest request) {
        String str = "";

        try {
            request.setCharacterEncoding("UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            str = sb.toString();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return str;
    }

    /**
     * 特殊字符过滤
     *
     * @param str 待过滤字符串
     * @return String
     */
    public static String stringFilter(String str) {
        return str.replace("&quot;", "\"").replace("TID:\\nEPC:", "").replace("TID:", "").replace("\\n", "");
    }

    /**
     * 获取IP地址
     *
     * @param request 请求信息
     * @return String
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 返回字符串
     *
     * @param obj 数据源
     * @return String
     */
    public static String returnValueToString(Object obj) {
        return obj == null ? "" : obj.toString().trim();
    }

    /**
     * 列表树转换成父子结构树
     *
     * @param list
     * @return
     */
    public static List<TreeBean> setTreeListToChildren(List<TreeBean> list) {
        List<TreeBean> rootBeans = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            // 找出根级菜单，（约定所有根级菜单的parentId属性为-1）
            TreeBean treeBean = list.get(i);
            if ("-1".equals(treeBean.getParentId())) {
                TreeBean bean = new TreeBean(treeBean.getId(), treeBean.getText(), treeBean.getParentId());
                rootBeans.add(bean);
            }
        }
        // 循环根级菜单，为每个根级菜单下的子菜单list赋值。
        for (TreeBean rootBean : rootBeans) {
            rootBean.setChildren(getTreeChildren(rootBean.getId(), list));
        }
        return rootBeans;
    }

    /**
     * @param id
     * @param list
     * @return
     */
    private static List<TreeBean> getTreeChildren(String id, List<TreeBean> list) {
        List<TreeBean> result = new ArrayList<>();
        for (TreeBean treeBean : list) {
            //判读是否是有直属单位
            if (CommonUtil.isNotEmpty(treeBean.getParentId())) {
                if (id.equals(treeBean.getParentId())) {
                    result.add(treeBean);
                }
            }
        }
        //递归出口
        if (result.size() == 0) {
            return null;
        }
        // 递归获取子单位或部门
        for (TreeBean treeBean : result) {
            treeBean.setChildren(getTreeChildren(treeBean.getId(), list));
        }
        return result;
    }


    /**
     * 获取入参
     * @return JSONObject
     */
    public static JSONObject getRequestParams() {
        return getRequestParams(getRequest());
    }

    /**
     * 获取入参
     * @return JSONObject
     */
    public static JSONObject getRequestParams(HttpServletRequest request) {
        JSONObject paramsJSON = new JSONObject();
        if(StringUtils.isNotBlank(request.getHeader("content-type"))){
            if("GET".equals(request.getMethod().toUpperCase()) || !request.getHeader("content-type").contains("application/json")) {
                Map<String, String[]> params = request.getParameterMap();
                if(params.size() > 0) {
                    for(String key : params.keySet()) {
                        paramsJSON.put(key, params.get(key));
                    }
                }
            }else if(request.getHeader("content-type").contains("application/json")) {
                paramsJSON = JSONObject.parseObject(CommonUtil.stringFilter(CommonUtil.getPostInputStream(request)));
            }
        }
        return paramsJSON;
    }

    /**
     * 获取Ip
     * @return String
     */
    public static String getIp() {
        return CommonUtil.getRequestIp(getRequest());
    }

    /**
     * 获取request
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
    }

}
