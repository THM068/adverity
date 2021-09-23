package com.adverity

class DailyImpressions {
    String campaign
    String datasource
    String daily
    Integer impressions

    void setDaily(Date date) {
        daily = Util.convertDateToString(date)
    }

}
