package com.adverity

import java.text.DateFormat
import java.text.SimpleDateFormat

class Util {

    static String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.format(date);
    }
}
