package ru.javaops.topjava.web;

import static ru.javaops.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static int id = 1;

    public static int authUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        SecurityUtil.id = id;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}