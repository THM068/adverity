package adverity.api

import com.adverity.CampaignStatService
import com.adverity.JsonParserUtility
import com.adverity.ProjectionRequest
import com.adverity.exceptions.DateParsingException
import com.adverity.exceptions.MissingParameterException

class CampaignStatController implements  JsonParserUtility{
	static responseFormats = ['json', 'xml']
    CampaignStatService campaignStatService

    def index() {
        ProjectionRequest projectionRequest = getProjectionRequest(params)
        def result = campaignStatService.getProjectionsFor(projectionRequest)
        [result: result, projectionRequest: projectionRequest]
    }

    def clickthroughrate() {
        [clickThroughRateList: campaignStatService.getClickThroughRate()]
    }

    def impressionsOverTime() {
        [impressionsOverTime: campaignStatService.impressionsOverTime(getCampaignName(params))]
    }

    private ProjectionRequest getProjectionRequest(params) {
        ProjectionRequest projectionRequest = new ProjectionRequest()
        if(!params.p) {
            throw new MissingParameterException("Missing projection parameter p in request")
        }
        projectionRequest.projections = parseRequest(params.p)

        if(params.h) {
            projectionRequest.groupBy = parseRequest(params.h)
        }
        if(params.q) {
            projectionRequest.filters = parseRequest(params.q)
        }

        return projectionRequest
    }

    private String getCampaignName(params) {
        if(!params.q) {
            throw new MissingParameterException("Missing projection parameter q in request")
        }
        Map<String, String> campaignNameMap = parseRequest(params.q)
        campaignNameMap.getOrDefault("campaign", "")
    }

    def handleMissingParameterException(MissingParameterException e) {
        render(status: 422, message: e.message)
    }

    def handleDateParsingException(DateParsingException e) {
        render(status: 422, message: e.message)
    }
}
