package com.adverity

import groovy.transform.ToString

@ToString
class CampaignStat {

    Campaign campaign
    DataSource dataSource
    Integer clicks
    Integer impressions
    Date daily

    static constraints = {

    }
}
