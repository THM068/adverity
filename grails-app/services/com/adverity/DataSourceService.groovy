package com.adverity

import grails.gorm.transactions.Transactional

@Transactional
class DataSourceService {


     List<DataSource> getDataSources(Map<String, String> request) {
        List<DataSource> sources = []
        if(request == null || request?.isEmpty()) {
            sources = DataSource.findAll()
        }
        else {
            sources = DataSource.createCriteria().list(getDatasourceByCriteria(request))
        }

        return sources
    }

    Closure  getDatasourceByCriteria(Map<String, String> request) {
        def closure = {
            and {
                request.each {entry ->
                    eq entry.key, entry.key == 'id' ? entry.value.toLong() : entry.value
                }
            }
        }
    }



}
