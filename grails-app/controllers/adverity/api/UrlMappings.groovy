package adverity.api

class UrlMappings {

    static mappings = {
        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")

        get "/adverity/api/reports/projections"(controller: 'campaignStat', action: 'index')
        get "/adverity/api/reports/click-through-rate"(controller: 'campaignStat', action: 'clickthroughrate')
        get "/adverity/api/reports/impressions-over-time"(controller: 'campaignStat', action: 'impressionsOverTime')

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
