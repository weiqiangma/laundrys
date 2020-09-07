package com.mawkun.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.*;

public final class JsonUtils {
    public static final SerializeConfig SERIALIZE_CONFIG = new SerializeConfig();

    public JsonUtils() {
    }

    public static void main(String[] args) {
        System.out.println(parse("{root:{id2875:'color_canyd',id2876:'color_canyd',id2877:'color_canyd',id2878:'color_canyd',id2879:'color_canyd',id2880:'color_canyd'}}"));
    }

    public static Object parse(String string) {
        return JSON.parse(string);
    }

    public static JSONObject toJSON(Object javaObject) {
        return (JSONObject)JSON.toJSON(javaObject);
    }

    public static JSONObject parseString(String jsonString) {
        return jsonString == null ? null : JSON.parseObject(jsonString);
    }

    public static JSONObject asJSONObject(String jsonString) {
        return JSON.parseObject(jsonString);
    }

    public static JSONObject asJSONObject(String jsonString, JSONObject def) {
        try {
            return jsonString != null && jsonString.length() != 0 && jsonString.startsWith("{") ? JSON.parseObject(jsonString) : def;
        } catch (Exception var3) {
            return def;
        }
    }

    public static JSONArray asJSONArray(String jsonString) {
        return JSON.parseArray(jsonString);
    }

    public static JSONArray toJSONArray(List<?> list) {
        return (JSONArray)JSON.toJSON(list);
    }

    public static <T> T toJavaObject(JSON json, Class<T> clazz) {
        return JSON.toJavaObject(json, clazz);
    }

    public static <T> T toJavaObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    public static Map<String, String> fromJsonToMap(String json) throws Exception {
        Map<String, Object> map = JSON.parseObject(json);
        Map<String, String> retMap = new HashMap();
        Iterator var3 = map.keySet().iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            retMap.put(key, String.valueOf(map.get(key)));
        }

        return retMap;
    }

    public static <T> List<T> getJavaCollection(String jsons, Class<T> clazz) {
        JSONArray array = JSON.parseArray(jsons);
        return jsonArrayToList(array, clazz);
    }

    public static <T> List<T> jsonArrayToList(JSONArray array, Class<T> clazz) {
        List<T> objs = null;
        if (array != null) {
            objs = new ArrayList();

            for(int i = 0; i < array.size(); ++i) {
                JSONObject jsonObject = array.getJSONObject(i);
                T obj = JSON.toJavaObject(jsonObject, clazz);
                objs.add(obj);
            }
        }

        return objs;
    }

    public static <T> List<T> toJavaList(JSONArray array, Class<T> clazz) {
        return jsonArrayToList(array, clazz);
    }

    public static String toString(Object object) {
        return toString(object, false);
    }

    public static String toString(Object object, boolean format) {
        return format ? JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.IgnoreNonFieldGetter, SerializerFeature.PrettyFormat}) : JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.IgnoreNonFieldGetter});
    }

    public static String toStringNoEx(Object object) {
        return toStringNoEx(object, false);
    }

    public static String toStringNoEx(Object object, boolean format) {
        try {
            return format ? JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.IgnoreNonFieldGetter, SerializerFeature.PrettyFormat}) : JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.IgnoreNonFieldGetter});
        } catch (Exception var3) {
            return "format json error";
        }
    }

    public static boolean isBadJson(String json) {
        return !isJson(json);
    }

    public static boolean isJson(String json) {
        if (StringUtils.isEmpty(json)) {
            return false;
        } else {
            try {
                JSON.parse(json);
                return true;
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static boolean isJsonSimple(String json) {
        if (StringUtils.isEmpty(json)) {
            return false;
        } else {
            return json.startsWith("{") && json.endsWith("}");
        }
    }

    public static JSONObject create(String[] keys, Serializable[] values) {
        JSONObject model = new JSONObject();
        if (keys != null && values != null && keys.length == values.length) {
            for(int i = 0; i < keys.length; ++i) {
                model.put(keys[i], values[i]);
            }

            return model;
        } else {
            return model;
        }
    }
}
