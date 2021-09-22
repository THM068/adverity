package com.adverity

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import java.text.SimpleDateFormat

@Integration
@Rollback
class CampaignStatServiceIntegrationSpec extends Specification {

    @Autowired
    CampaignStatService campaignStatService

    def 'Click-Through Rate (CTR) per Datasource and Campaign'() {
        given:
            DataSource twitter = new DataSource(name: 'Twitter').save()
            DataSource facebook = new DataSource(name: 'Facebook').save()

            Campaign campaign1 = new Campaign(name: 'GDN_Retargeting')
            Campaign campaign2 = new Campaign(name: 'GDN_Walking')
        and:
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/01/2020"), clicks: 50, impressions: 100).save()
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/02/2020"), clicks: 50, impressions: 100).save()
            new CampaignStat(dataSource: twitter, campaign: campaign1, daily: getDate("12/04/2020"), clicks: 50, impressions: 100).save()



            new CampaignStat(dataSource: facebook, campaign: campaign2, daily: getDate("12/11/2020"), clicks: 45, impressions: 90).save()
            new CampaignStat(dataSource: facebook, campaign: campaign2, daily: getDate("12/12/2020"), clicks: 45, impressions: 90).save()
            new CampaignStat(dataSource: facebook, campaign: campaign2, daily: getDate("12/13/2020"), clicks: 45, impressions: 90).save()
        when:
            def result = campaignStatService.getClickThroughRate()
        then:
            result.size() == 3

            ClickThroughRate first = result.first()


    }

    private Date getDate(String date) {
        new SimpleDateFormat("dd/MM/yyyy").parse(date)
    }
}
