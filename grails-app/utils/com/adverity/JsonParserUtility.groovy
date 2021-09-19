package com.adverity

import groovy.json.JsonException
import groovy.json.JsonParserType
import groovy.json.JsonSlurper

trait JsonParserUtility {
    JsonSlurper slurper = new JsonSlurper(type: JsonParserType.INDEX_OVERLAY)

    def parseRequest(String request) {
        slurper.parseText(request)
    }

    def handleJsonException(JsonException e) {
        render(status: 400, message: e.message)
    }

    def handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        render(status: 400, message: "Please specify a query e.g url?q={}")
    }
}