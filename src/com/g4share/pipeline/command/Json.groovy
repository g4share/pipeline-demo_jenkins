package com.g4share.pipeline.command

import com.g4share.pipeline.core.TemplateProcessor
import groovy.json.JsonSlurperClassic

class Json extends Command {

    private TemplateProcessor templateProcessor = new TemplateProcessor()

    def config(String commandName, Map<String, String> vars) {
        def json = jsonParse('dependencies.json')
        loadResource(commandName, (String)json.name, vars)
    }

    def loadResource(String commandName, String projectName, Map<String, String> vars) {
        String json = context.libraryResource("projects/config/${projectName}.json");
        String fetchedJson = templateProcessor.fetchTemplate(json, vars)

        jsonSlurper(fetchedJson)
                .commands
                .find { it.name == commandName }
    }

    private jsonParse(String jsonFile) {
        String json = context.readFile(jsonFile)
        return jsonSlurper(json)
    }

    def jsonSlurper(String json) {
        return new JsonSlurperClassic().parseText(json)
    }
}
