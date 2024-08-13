package com.springboot.yapily.productservice.util;

import org.apache.tomcat.util.buf.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListToStringConverter {

    public static String convertToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return StringUtils.join(list, ',');
    }

    public static List<String> convertToList(String string){
        if (string == null || string.trim().isEmpty()){
            return new ArrayList<>();
        }
        String[] listData = string.split(",");
        return Arrays.asList(listData);
    }
}
