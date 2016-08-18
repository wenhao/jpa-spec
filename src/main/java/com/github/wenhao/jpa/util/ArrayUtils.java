package com.github.wenhao.jpa.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ArrayUtils {
    public static Object[] removeDuplicate(Object[] values) {
        Set<Object> objects = new HashSet<>();
        Collections.addAll(objects, values);
        return objects.toArray();
    }
}
