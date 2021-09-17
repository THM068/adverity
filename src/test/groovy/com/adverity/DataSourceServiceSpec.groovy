package com.adverity

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest

class DataSourceServiceSpec extends  HibernateSpec implements ServiceUnitTest<DataSourceService>{

    DataSourceService testObj

    def setup() {
        DataSource d1 = new DataSource(name: "Facebook", id: 1L).save()
        DataSource d2 = new DataSource(name: "Twitter", id: 2L).save()
        DataSource d3 = new DataSource(name: "Instagram", id: 3L).save()

        testObj = new DataSourceService()
    }

    def cleanup() {
    }

    void "Returns all the Data sources if the request map is empty"() {
        expect:
            List<DataSource> result = testObj.getDataSources([:])
            result.size() == 3
            result*.name == ["Facebook", "Twitter", "Instagram"]
    }

    void "Return a datasource given a map with the datasource name"() {
        given:
            String name = "Twitter"
        when:
            List<DataSource> result = testObj.getDataSources(["name": name, id: 2L])
        then:
            result.size() == 1
            result[0].name == name
    }

    List<Class> getDomainClasses() { [DataSource] }
}
