package com.adverity

import com.adverity.exceptions.DbAccessException
import grails.gorm.transactions.Transactional
import org.hibernate.criterion.CriteriaSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate

import static com.adverity.constants.SQL.CLTR_SQL

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
        try {
            return jdbcTemplate.query(CLTR_SQL, new ClickThroughRateRowMapper())
        }
        catch (DataAccessException ex ) {
            throw new DbAccessException(ex.message)
        }
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

    List<DailyImpressions> impressionsOverTime(final String campaign) {
       def query = CampaignStat.where {
           campaign == Campaign.findByName(campaign)
       }

       query.list(sort: 'daily').collect {
           DailyImpressions dailyImpressions = new DailyImpressions()
           dailyImpressions.campaign = it.campaign.name
           dailyImpressions.datasource = it.dataSource.name
           dailyImpressions.impressions = it.impressions
           dailyImpressions.setDaily(it.daily)
           dailyImpressions
       }
    }


}
