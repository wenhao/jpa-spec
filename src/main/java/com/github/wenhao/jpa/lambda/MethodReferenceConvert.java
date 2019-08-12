/*
 * Copyright © 2019, Wen Hao <wenhao@126.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.wenhao.jpa.lambda;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodReferenceConvert {

    private static final Map<Class, String> cache = new ConcurrentHashMap<>();

    public static <T> String convertToColumn(MethodReferenceColumn<T> column) {
        return convertToColumn((Object) column);
    }

    public static <T> String convertToColumn(StaticMethodReferenceColumn<T> column) {
        return convertToColumn((Object) column);
    }

    public static String convertToColumn(Object column) {
        return cache.computeIfAbsent(column.getClass(), t -> {
            SerializedLambda lambda = SerializedLambda.of(column);
            String methodName = lambda.getMethodName();
            if (methodName.startsWith("get")) {
                return toLowerCaseFirstOne(methodName.substring(3));
            } else if (methodName.startsWith("is")) {
                return toLowerCaseFirstOne(methodName.substring(2));
            }
            return methodName;
        });
    }

    private static String toLowerCaseFirstOne(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

}
