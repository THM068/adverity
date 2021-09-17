package adverity.api

import grails.core.GrailsApplication
import grails.plugins.*

class ApplicationController implements PluginManagerAware {

    GrailsApplication grailsApplication
    GrailsPluginManager pluginManager

    def index() {
        println(params.name)
        [grailsApplication: grailsApplication, pluginManager: pluginManager]
    }
}
