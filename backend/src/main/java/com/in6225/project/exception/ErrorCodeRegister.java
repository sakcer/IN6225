package com.in6225.project.exception;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.antlr.v4.runtime.misc.Pair;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ErrorCodeRegister {
    public static void init() {
        register(SystemErrorCode.class, 10000, 20000);
    }

    static final int MIN_ERROR_CODE = 10000; // 错误码的最小值

    // 每类错误码的范围
    static final Map<Class<? extends ErrorCode>, Pair<Integer, Integer>> ERROR_CODE_RANGES = new ConcurrentHashMap<>();


    static void register(Class<? extends ErrorCode> clazz, Integer start, Integer end) {
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException(
                    String.format("[RegisterErrorCodeError]%s is not Enum class", clazz.getSimpleName()));
        }

        if (ERROR_CODE_RANGES.containsKey(clazz)) {
            throw new IllegalArgumentException(
                    String.format("[RegisterErrorCodeError]Enum %s already exist", clazz.getSimpleName()));
        }

        ERROR_CODE_RANGES.put(clazz, new Pair<>(start, end));
    }
}