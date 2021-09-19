package com.adverity

import grails.gorm.transactions.Transactional
import org.hibernate.FetchMode
import org.hibernate.criterion.CriteriaSpecification

import java.text.SimpleDateFormat

@Transactional
class CampaignStatService {

    List<String> supportedProjections = ['sum', 'avg', 'max', 'min']
    List<Map<String, Object>> getProjectionsFor(ProjectionRequest request) {
   def result = CampaignStat.createCriteria().list(projectionCriteria(request))
      return result
    }

    Closure projectionCriteria(ProjectionRequest request) {
        def criteriaClosure = {
            fetchMode 'campaign', FetchMode.JOIN
            createAlias("dataSource", "dataSource")
            createAlias("campaign", "campaign")
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                request.projections.each {entry ->
                    String method = entry.key - "\$"
                    if(entry.key.contains("\$") && supportedProjections.any { it.equals(method)}) {
                        entry.value.each {target ->
                            "$method"(target, "${target}_${method}")
                        }
                    }
                }
                if(!request.groupBy.isEmpty()) {
                    request.groupBy.each { entry ->
                       entry.value.each { val->
                           groupProperty "${val}.name", val
                       }
                    }
                }
               // groupProperty 'dataSource.name', 'data_source'
               // groupProperty 'campaign.name', 'campaign'
            }
            and {
                request.filters.each {entry ->
                    if(!entry.key.contains("\$")) {
                        eq "${entry.key}.name", entry.value
                    }
                }
                if(request.dateRange.isPresent()) {
                    DateRange dateRange = request.dateRange.get()
                    between 'daily', dateRange.from, dateRange.to
                }
            }
        }
    }
}

//def result = CampaignStat.createCriteria().list {
//    fetchMode 'campaign', FetchMode.JOIN
//    createAlias("dataSource", "source")
//    createAlias("campaign", "camp")
//    resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
//    projections {
//        "$sum"('clicks', 'clicks')
//        groupProperty 'source.name', 'data_source'
//        groupProperty 'camp.name', 'campaign'
//        min 'daily', 'from'
//        max 'daily', 'to'
//    }
//    and {
//        //eq 'source.name', 'Twitter'
//    }
//}
