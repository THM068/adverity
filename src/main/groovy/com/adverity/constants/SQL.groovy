package com.adverity.constants

class SQL {

    static final String CLTR_SQL = """
select campaign.name as campaign,data_source.name as datasource, sum(clicks)/SUM(impressions) * 100 as clickThroughRate
 from campaign_stat 
 INNER JOIN campaign  on campaign.id = campaign_stat.campaign_id
 INNER JOIN data_source on data_source.id = campaign_stat.data_source_id
GROUP BY campaign_id, data_source_id
"""

    static final String IMPRESSIONS_OVER_TIME = """
select campaign.name as campaign,data_source.name as datasource, daily, impressions
 from campaign_stat 
 INNER JOIN campaign  on campaign.id = campaign_stat.campaign_id
 INNER JOIN data_source on data_source.id = campaign_stat.data_source_id
 AND campaign.name = :campaign ORDER BY daily ASC;"""
}
