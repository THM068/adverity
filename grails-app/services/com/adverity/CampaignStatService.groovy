package com.adverity
import grails.gorm.transactions.Transactional
import org.hibernate.criterion.CriteriaSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate

import java.text.SimpleDateFormat

@Transactional
class CampaignStatService {
    @Autowired
    JdbcTemplate jdbcTemplate

    List<String> supportedProjections = ['sum', 'avg', 'max', 'min']
    List<Map<String, Object>> getProjectionsFor(ProjectionRequest request) {
        List<Map<String, Object>> result = CampaignStat.createCriteria().list(projectionCriteria(request))
        return result
    }

    List<ClickThroughRate> getClickThroughRate() {
        List<Map> result = jdbcTemplate.query(CLTR_SQL, new ClickThroughRateRowMapper())
    }

    Closure projectionCriteria(ProjectionRequest request) {
        def criteriaClosure = {
            //fetchMode 'campaign', FetchMode.JOIN
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

    private final String CLTR_SQL = """
select campaign.name as campaign,data_source.name as datasource, sum(clicks)/SUM(impressions) * 100 as clickThroughRate
 from campaign_stat 
 INNER JOIN campaign  on campaign.id = campaign_stat.campaign_id
 INNER JOIN data_source on data_source.id = campaign_stat.data_source_id
GROUP BY campaign_id, data_source_id
"""
}
