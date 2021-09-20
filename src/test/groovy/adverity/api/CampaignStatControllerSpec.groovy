package adverity.api

import com.adverity.CampaignStatService
import com.adverity.exceptions.DateParsingException
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

import java.text.SimpleDateFormat

class CampaignStatControllerSpec extends Specification implements ControllerUnitTest<CampaignStatController> {
    CampaignStatService campaignStatServiceMock = Mock()

    def setup() {
        controller.campaignStatService = campaignStatServiceMock
    }


    void "Can create a sum of clicks given a datasource and date range"() {
        given:
            request.method = 'GET'
        and:
            params.p = "{\"\$sum\": [\"clicks\"]}"
            params.q = "{\"dataSource\": \"twitter\", \"\$between\": {\"from\": \"12/01/2020\", \"to\": \"17/01/2020\"}}"
        when:
            controller.index()
        then:
            1 * campaignStatServiceMock.getProjectionsFor( {
                it.projections == ["\$sum": ["clicks"]] &&
                it.filters["dataSource"] == "twitter" &&
                it.dateRange.get().from == getDate("12/01/2020") &&
                it.dateRange.get().to == getDate("17/01/2020")
            })
    }

    void "displays 422 error if projection parameter is not specified"() {
        given:
            request.method = 'GET'
        and:
            params.q = "{\"dataSource\": \"twitter\", \"\$between\": {\"from\": \"12/01/2020\", \"to\": \"17/01/2020\"}}"
        when:
            controller.index()
        then:
            response.status == 422
            response.errorMessage == "Missing projection parameter p in request"
    }

    void "displays 422 error if date is of incorrect format"() {
        given:
            request.method = 'GET'
        and:
            params.p = "{\"\$sum\": [\"clicks\"]}"
            params.q = "{\"dataSource\": \"twitter\", \"\$between\": {\"from\": \"12/dd/2020\", \"to\": \"17/01/2020\"}}"

            campaignStatServiceMock.getProjectionsFor(_) >> { throw new DateParsingException("Please enter a valid date of format dd/MM/yyyy") }
        when:
            controller.index()
        then:
            response.status == 422
            response.errorMessage == "Please enter a valid date of format dd/MM/yyyy"
    }

    private Date getDate(String date) {
        new SimpleDateFormat("dd/MM/yyyy").parse(date)
    }
}
