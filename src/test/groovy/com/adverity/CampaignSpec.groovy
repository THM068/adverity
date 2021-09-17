package com.adverity

import grails.test.hibernate.HibernateSpec
import spock.lang.Unroll

class CampaignSpec  extends HibernateSpec {
    List<Class> getDomainClasses() { [Campaign] }

    def setup() {

    }

    def cleanup() {
    }

    @Unroll("Campaign follows validation rules #name #expected")
    void "Campaign follows validation rules"() {
        expect:
            new Campaign(name: name).validate(['name']) == expected
        where:
            name    | expected
            ""      |  false
            null    |  false
            "Pepsi" |  true
    }

    void "Campaigns must be unique"() {
        given:
            Campaign campaign = new Campaign(name: name).save()
        expect:
            new Campaign(name: name)
        where:
            name = "Pepsi"
    }
}
