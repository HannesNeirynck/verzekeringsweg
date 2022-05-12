package com.aginsurance.verzekeringsweg.Util;

import java.util.List;

public class Checker {

    public static Boolean String(String toCheck){
        if (toCheck != null && !toCheck.trim().isEmpty()) return true;
        else return false;
    }

    public static Boolean NullCheck(Object o){
        if (o == null) return false;
        else return true;
    }

    public static Boolean ListChecker(List list){
        if (list == null || list.isEmpty()) return false;
        else return true;
    }
}
