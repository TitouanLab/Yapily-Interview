package com.springboot.yapily.productservice.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ListToStringConverterTest {

    @Test
    public void convertToString_withValidList_assertString() {
        String result = ListToStringConverter.convertToString(Arrays.asList("food", "limited"));
        assertEquals(result, "food,limited");
    }

    @Test
    public void convertToString_withNullList_assertStringEmpty() {
        String result = ListToStringConverter.convertToString(null);
        assertEquals(result, "");
    }

    @Test
    public void convertToString_withEmptyList_assertStringEmpty() {
        String result = ListToStringConverter.convertToString(new ArrayList<>());
        assertEquals(result, "");
    }

    @Test
    public void convertToList_withValidString_assertList() {
        List<String> result = ListToStringConverter.convertToList("food,limited");
        assertEquals(result, Arrays.asList("food", "limited"));
    }

    @Test
    public void convertToList_withNullString_assertEmptyList() {
        List<String> result = ListToStringConverter.convertToList(null);
        assertEquals(result, new ArrayList<>());
    }

    @Test
    public void convertToList_withEmptySpaceString_assertEmptyList() {
        List<String> result = ListToStringConverter.convertToList("    ");
        assertEquals(result, new ArrayList<>());
    }
}
