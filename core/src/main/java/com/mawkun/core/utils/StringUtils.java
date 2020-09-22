package com.mawkun.core.utils;

import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.RandomUtils;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import static com.xiaoleilu.hutool.util.StrUtil.isEmpty;
import static org.thymeleaf.util.StringUtils.replace;

public final class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final String enReg = "^\\w+$";

    public StringUtils() {
    }

    public static String getFileExtend(String fn) {
        if (isEmpty(fn)) {
            return null;
        } else {
            int idx = fn.lastIndexOf(46) + 1;
            return idx != 0 && idx < fn.length() ? fn.substring(idx) : null;
        }
    }

    public static String checkStr(String str) {
        return str != null && str.length() != 0 && !str.equals("null") ? str.trim() : "";
    }

    public static boolean checkObj(Object obj) {
        return obj == null || obj.equals("null");
    }

    public static String formatNotHtml(String str) {
        if (str == null) {
            return "";
        } else {
            String randomStr = String.valueOf(System.currentTimeMillis());
            String html = replace(str, "&nbsp;", randomStr);
            html = replace(html, "\"", "&quot;");
            html = replace(html, "\t", "&nbsp;&nbsp;");
            html = replace(html, "<", "&lt;");
            html = replace(html, ">", "&gt;");
            return replace(html, randomStr, "&nbsp;").trim();
        }
    }

    public static String formatHtml(String str) {
        if (str == null) {
            return "";
        } else {
            String randomStr = String.valueOf(System.currentTimeMillis());
            String html = replace(str, "&nbsp;", randomStr);
            html = replace(html, "&amp;", "&");
            html = replace(html, "&apos;", "'");
            html = replace(html, "&quot;", "\"");
            html = replace(html, "&nbsp;&nbsp;", "\t");
            html = replace(html, "&nbsp;", " ");
            html = replace(html, "&lt;", "<");
            html = replace(html, "&gt;", ">");
            html = replace(html, "&#34;", "\"");
            html = replace(html, "&#60;", "<");
            html = replace(html, "&#62;", ">");
            return replace(html, randomStr, "&nbsp;").trim();
        }
    }

    public static String delBlankLine(String text) {
        if (text != null && text.length() != 0) {
            text = replace(replace(text, "\r", ""), "\n", "");
            return text;
        } else {
            return "";
        }
    }

    public static boolean arrayItemIsEmpty(String[] str) {
        if (str == null) {
            return true;
        } else {
            for(int i = 0; i < str.length; ++i) {
                if (isEmpty(str[i])) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean arrayItemHaveValue(String[] str) {
        if (str == null) {
            return false;
        } else {
            for(int i = 0; i < str.length; ++i) {
                if (!isEmpty(str[i])) {
                    return true;
                }
            }

            return false;
        }
    }

    public static String obj2Str(Object obj) {
        return obj != null && obj instanceof String ? obj.toString().trim() : "";
    }

    public static String toHex(String str) {
        if (isEmpty(str)) {
            return "";
        } else {
            String msg = "";
            char[] chars = str.toCharArray();

            for(int i = 0; i < chars.length; ++i) {
                msg = msg + "\\u" + Integer.toHexString(chars[i]);
            }

            return msg;
        }
    }

    public static String formatString(String str) {
        str = str + "";
        if (str.indexOf(".") > 0) {
            str = str.substring(0, str.indexOf("."));
        }

        return str;
    }

    public static String numberSplit(String str, String split) {
        if (str == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < str.length(); ++i) {
                sb.append(str.charAt(i));
                if (i < str.length() - 1) {
                    sb.append(split);
                }
            }

            return sb.toString();
        }
    }

    public static String[] number2Arr(String str) {
        if (str == null) {
            return new String[0];
        } else {
            String[] arr = new String[str.length()];

            for(int i = 0; i < str.length(); ++i) {
                arr[i] = String.valueOf(str.charAt(i));
            }

            return arr;
        }
    }

    public static String[] str2ArrSplit(String str) {
        String[] str_arr = str.split("[^\\d]+");
        return str_arr;
    }

    public static String strArray2Str(String[] data, String fenge) {
        if (data == null) {
            return "";
        } else {
            if (fenge == null || fenge.equals("")) {
                fenge = ",";
            }

            StringBuffer sb = new StringBuffer("");

            for(int i = 0; i < data.length; ++i) {
                sb.append(data[i]).append(fenge);
            }

            return data.length > 0 ? sb.substring(0, sb.length() - 1) : sb.toString();
        }
    }

    public static String intArrayToStr(int[] data) {
        StringBuffer sb = new StringBuffer("");

        for(int i = 0; i < data.length; ++i) {
            sb.append(data[i]);
        }

        return sb.toString();
    }

    public static String intArrayToStr(int[] data, String fenge) {
        if (data == null) {
            return "";
        } else {
            if (fenge == null || fenge.equals("")) {
                fenge = ",";
            }

            StringBuffer sb = new StringBuffer("");

            for(int i = 0; i < data.length; ++i) {
                sb.append(data[i]).append(fenge);
            }

            return data.length > 0 ? sb.substring(0, sb.length() - 1) : sb.toString();
        }
    }

    public static String array2Str(Object[] array, String fenge) {
        if (array == null) {
            return "";
        } else {
            if (fenge == null || fenge.equals("")) {
                fenge = ",";
            }

            StringBuffer sb = new StringBuffer("");

            for(int i = 0; i < array.length; ++i) {
                sb.append(array[i]).append(fenge);
            }

            return array.length > 0 ? sb.substring(0, sb.length() - 1) : sb.toString();
        }
    }

    public static String join(Set<String> set, String split) {
        StringBuffer sb = new StringBuffer();
        Iterator var3 = set.iterator();

        while(var3.hasNext()) {
            String str = (String)var3.next();
            sb.append(str).append(split);
        }

        return sb.length() - split.length() < 0 ? sb.toString() : sb.substring(0, sb.length() - split.length());
    }

    public static int strLen(String str) {
        if (str != null) {
            try {
                int num = str.getBytes("gbk").length;
                return num / 2 + num % 2;
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }
        }

        return 0;
    }

    public static String cutStr(String str, int len) {
        if (str != null && !str.equals("")) {
            if (len > str.length()) {
                return str;
            } else {
                len *= 2;
                int cutNum = 0;

                for(int i = 0; i < str.length(); ++i) {
                    char c = str.charAt(i);
                    if (c > 256) {
                        cutNum += 2;
                    } else {
                        ++cutNum;
                    }

                    if (cutNum > len) {
                        return str.substring(0, i).toString().trim();
                    }
                }

                return str;
            }
        } else {
            return "";
        }
    }

    public static boolean isEnStr(String str) {
        return str.matches("^\\w+$");
    }

    public static boolean isChinese(char a) {
        return a >= 19968 && a <= 171941;
    }

    public static boolean containsChinese(String s) {
        if (null != s && !"".equals(s.trim())) {
            for(int i = 0; i < s.length(); ++i) {
                if (isChinese(s.charAt(i))) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        } else {
            email = email.trim();
            if (email.indexOf(32) != -1) {
                return false;
            } else {
                int idx = email.indexOf(64);
                if (idx != -1 && idx != 0 && idx + 1 != email.length()) {
                    if (email.indexOf(64, idx + 1) != -1) {
                        return false;
                    } else {
                        return email.indexOf(46) != -1;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public static boolean isPhone(String phone) {
        if (phone != null) {
            phone = phone.trim();
            String regex = "^0\\d{2,3}-*\\d{7,8}-*\\d*$";
            return phone.matches(regex);
        } else {
            return false;
        }
    }

    public static boolean isMobile(String mobile) {
        if (isEmpty(mobile)) {
            return false;
        } else if (mobile.length() < 11) {
            return false;
        } else {
            if (mobile.startsWith("+86")) {
                mobile = mobile.substring(3);
            }

            if (mobile.length() == 11 && mobile.startsWith("1")) {
                if (!mobile.startsWith("12") && !mobile.startsWith("16")) {
                    return mobile.matches("1(2|3|4|5|6|7|8|9)[0-9]{9}");
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static boolean stringInArray(String[] strs, String str) {
        if (!isEmpty(str) && strs != null) {
            for(int i = 0; i < strs.length; ++i) {
                if (!isEmpty(strs[i]) && strs[i].equalsIgnoreCase(str)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean isIPAddr(String addr) {
        if (isEmpty(addr)) {
            return false;
        } else {
            String[] ips = split(addr, ".");
            if (ips.length != 4) {
                return false;
            } else {
                try {
                    int ipa = Integer.parseInt(ips[0]);
                    int ipb = Integer.parseInt(ips[1]);
                    int ipc = Integer.parseInt(ips[2]);
                    int ipd = Integer.parseInt(ips[3]);
                    return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0 && ipc <= 255 && ipd >= 0 && ipd <= 255;
                } catch (Exception var6) {
                    return false;
                }
            }
        }
    }

    public static boolean isGBK(String str) {
        try {
            byte[] bytes = str.replace('?', 'a').getBytes("8859_1");

            for(int i = 0; i < bytes.length; ++i) {
                if (bytes[i] == 63) {
                    return true;
                }
            }

            return false;
        } catch (UnsupportedEncodingException var3) {
            return false;
        }
    }

    public static String replaceIgnoreCase(String str, String src, String obj) {
        String l_str = str.toLowerCase();
        String l_src = src.toLowerCase();
        int fromIdx = 0;
        StringBuffer result = new StringBuffer();

        while(true) {
            int idx = l_str.indexOf(l_src, fromIdx);
            if (idx == -1) {
                result.append(str.substring(fromIdx));
                return result.toString();
            }

            result.append(str.substring(fromIdx, idx));
            result.append(obj);
            fromIdx = idx + src.length();
        }
    }

    public static boolean isLegalUsername(String username) {
        for(int i = 0; i < username.length(); ++i) {
            char ch = username.charAt(i);
            if (!isAscii(ch) && ch != '.' && ch != '_' && ch != '-' && ch != '+' && ch != '(' && ch != ')' && ch != '*' && ch != '^' && ch != '@' && ch != '%' && ch != '$' && ch != '#' && ch != '~' && ch != '-') {
                return false;
            }
        }

        return true;
    }

    public static boolean isAsciiOrDigit(String name) {
        for(int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);
            if (!isAscii(ch)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAscii(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9';
    }

    public static boolean containsIgnoreCase(String source, String matchStr) {
        if (source != null && matchStr != null) {
            return source.contains(matchStr) || source.toLowerCase().contains(matchStr.toLowerCase());
        } else {
            return false;
        }
    }

    public static String[] createArray(String[]... arrays) {
        int len = 0;

        for(int i = 0; i < arrays.length; ++i) {
            len += arrays[i].length;
        }

        String[] newArray = new String[len];
        int totalLen = 0;

        for(int i = 0; i < arrays.length; ++i) {
            String[] array = arrays[i];
            System.arraycopy(array, 0, newArray, totalLen, array.length);
            totalLen += array.length;
        }

        return newArray;
    }

    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return "";
        } else {
            str = str.replaceAll("[^一-龥　-〿\uff00-\uffef\u0000-\u007f“-”]", "").replaceAll("\\s", "");
            return str;
        }
    }

    /**
     * 创建指定长度的随机字符串
     * @param length
     * @return
     */
    public static String createRandomStr(Integer length) {
        Random random = new Random();
        String str = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < length; i ++) {
            int number = random.nextInt(str.length());
            builder.append(str.charAt(number));
        }
        return builder.toString();
    }

    /**
     * 创建订单号
     * @return
     */
    public static String createOrderFormNo(String orderKey){
        String currentDate = DateUtils.getCurrDate("yyyyMMddhhmm");
        String result = currentDate + orderKey;
        return result;
    }

    public static void main(String[] args) {
        String date = DateUtils.getCurrDate("yyyyMMddhhmmss");
        System.out.println(date);
    }
}
