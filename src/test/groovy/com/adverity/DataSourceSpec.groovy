package com.adverity

import grails.test.hibernate.HibernateSpec
import spock.lang.Unroll

class DataSourceSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [DataSource] }

    def setup() {
    }

    def cleanup() {
    }

    @Unroll("DataSource follows validation rules #name #expected")
    void "DataSource follows validation rules"() {
        expect:
            new DataSource(name: name).validate(['name']) == expected
        where:
            name      | expected
            ""        |  false
            null      |  false
            "Twitter" |  true
    }

    void "Campaigns must be unique"() {
        given:
            DataSource campaign = new DataSource(name: name).save()
        expect:
            new DataSource(name: name)
        where:
            name = "Twitter"
    }
}
