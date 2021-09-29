package com.adverity

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import java.text.SimpleDateFormat

class CampaignStatServiceSpec extends HibernateSpec implements ServiceUnitTest<CampaignStatService>{

    CampaignStatService testObj
    List<Class> getDomainClasses() { [CampaignStat, DataSource, Campaign] }

    def setup() {
        testObj = new CampaignStatService()
    }

    void "Get sum of clicks for a given datasource and time"() {
        given:
            DataSource twitter = new DataSource(name: 'Twitter').save()
            DataSource facebook = new DataSource(name: 'Facebook').save()

            Campaign campaign1 = new Campaign(name: 'GDN_Retargeting')
            Campaign campaign2 = new Campaign(name: 'GDN_Walking')
        and:
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/01/2020"), clicks: 20, impressions: 400).save()
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/02/2020"), clicks: 40, impressions: 200).save()
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/04/2020"), clicks: 100, impressions: 450).save()

            new CampaignStat(dataSource: facebook, campaign: campaign1, daily: getDate("12/01/2020"), clicks: 50, impressions: 400).save()
            new CampaignStat(dataSource: facebook, campaign: campaign1, daily: getDate("12/02/2020"), clicks: 60, impressions: 300).save()
            new CampaignStat(dataSource: facebook, campaign: campaign1, daily: getDate("12/04/2020"), clicks: 109, impressions: 100).save()

            new CampaignStat(dataSource: twitter, campaign: campaign2, daily: getDate("12/11/2020"), clicks: 12, impressions: 400).save()
            new CampaignStat(dataSource: twitter, campaign: campaign2, daily: getDate("12/12/2020"), clicks: 80, impressions: 300).save()
            new CampaignStat(dataSource: twitter, campaign: campaign2, daily: getDate("12/13/2020"), clicks: 50, impressions: 100).save()
        and:
            def query = new ProjectionRequest(projections: ["\$sum": ["clicks"] ], filters: ["dataSource": "Twitter", "\$between": ["from": "12/02/2020", "to": "12/04/2020"]] )
        when:
            List<Map<String,Object>> result = testObj.getProjectionsFor(query)
        then:
            result.size() == 1
            Map firstResult = result.first()
            firstResult['clicks_sum'] == 140

    }

    void "Get sum of clicks for a given datasource and time and grouped by datasource and campaign"() {
        given:
            DataSource twitter = new DataSource(name: 'Twitter').save()
            DataSource facebook = new DataSource(name: 'Facebook').save()

            Campaign campaign1 = new Campaign(name: 'GDN_Retargeting')
            Campaign campaign2 = new Campaign(name: 'GDN_Walking')
        and:
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/01/2020"), clicks: 20, impressions: 400).save()
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/02/2020"), clicks: 40, impressions: 200).save()
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/04/2020"), clicks: 100, impressions: 450).save()

            new CampaignStat(dataSource: facebook, campaign: campaign1, daily: getDate("12/01/2020"), clicks: 50, impressions: 400).save()
            new CampaignStat(dataSource: facebook, campaign: campaign1, daily: getDate("12/02/2020"), clicks: 60, impressions: 300).save()
            new CampaignStat(dataSource: facebook, campaign: campaign1, daily: getDate("12/04/2020"), clicks: 109, impressions: 100).save()

            new CampaignStat(dataSource: twitter, campaign: campaign2, daily: getDate("12/11/2020"), clicks: 12, impressions: 400).save()
            new CampaignStat(dataSource: twitter, campaign: campaign2, daily: getDate("12/12/2020"), clicks: 80, impressions: 300).save()
            new CampaignStat(dataSource: twitter, campaign: campaign2, daily: getDate("12/13/2020"), clicks: 50, impressions: 100).save()
        and:
            def query = new ProjectionRequest(projections: ["\$sum": ["clicks"] ], filters: ["dataSource": "Twitter", "\$between": ["from": "12/02/2020", "to": "12/04/2020"]], groupBy: ["\$groupBy": ["campaign", "dataSource"]] )
        when:
            List<Map<String,Object>> result = testObj.getProjectionsFor(query)
        then:
            result.size() == 1
            Map firstResult = result.first()
            firstResult['clicks_sum'] == 140
            firstResult['campaign'] == 'GDN_Retargeting'
            firstResult['dataSource'] == 'Twitter'
    }

    def 'Impressions over time for given campaign'() {
        given:
            DataSource twitter = new DataSource(name: 'Twitter').save()
            DataSource facebook = new DataSource(name: 'Facebook').save()

            Campaign campaign1 = new Campaign(name: 'GDN_Retargeting')
            Campaign campaign2 = new Campaign(name: 'GDN_Walking')
        and:
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("11/01/2020"), clicks: 20, impressions: 400).save()
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/01/2020"), clicks: 40, impressions: 200).save()
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("13/01/2020"), clicks: 100, impressions: 450).save()

            new CampaignStat(dataSource: facebook, campaign: campaign1, daily: getDate("14/01/2020"), clicks: 50, impressions: 400).save()
            new CampaignStat(dataSource: facebook, campaign: campaign1, daily: getDate("15/01/2020"), clicks: 60, impressions: 300).save()
            new CampaignStat(dataSource: facebook, campaign: campaign1, daily: getDate("17/01/2020"), clicks: 109, impressions: 100).save()

            new CampaignStat(dataSource: twitter, campaign: campaign2, daily: getDate("12/11/2020"), clicks: 12, impressions: 400).save()
            new CampaignStat(dataSource: twitter, campaign: campaign2, daily: getDate("12/12/2020"), clicks: 80, impressions: 300).save()
            new CampaignStat(dataSource: twitter, campaign: campaign2, daily: getDate("12/13/2020"), clicks: 50, impressions: 100).save()

        when:
            List<DailyImpressions> result = testObj.impressionsOverTime(campaign)

        then:
            result.size() == 6
            result[0].getDaily() == "11/01/2020"
            result[0].impressions == 400
            result[0].campaign == campaign
            result[0].datasource == 'Twitter'

            result[1].getDaily() == "12/01/2020"
            result[1].impressions == 200
            result[1].campaign == campaign
            result[1].datasource == 'Twitter'

            result[2].getDaily() == "13/01/2020"
            result[2].impressions == 450
            result[2].campaign == campaign
            result[2].datasource == 'Twitter'

            result[3].getDaily() == "14/01/2020"
            result[3].impressions == 400
            result[3].campaign == campaign
            result[3].datasource == 'Facebook'

            result[4].getDaily() == "15/01/2020"
            result[4].impressions == 300
            result[4].campaign == campaign
            result[4].datasource == 'Facebook'

            result[5].getDaily() == "17/01/2020"
            result[5].impressions == 100
            result[5].campaign == campaign
            result[5].datasource == 'Facebook'

        where:
            campaign = 'GDN_Retargeting'
    }
    private Date getDate(String date) {
        new SimpleDateFormat("dd/MM/yyyy").parse(date)
    }
}
