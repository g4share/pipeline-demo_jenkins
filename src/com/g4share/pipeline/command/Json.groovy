package com.g4share.pipeline.command

import com.g4share.pipeline.core.TemplateProcessor
import groovy.json.JsonSlurperClassic

class Json extends Command {

    private TemplateProcessor templateProcessor = new TemplateProcessor()

    Json(context) {
        super(context)
    }

    def config() {
        def json = jsonParse('dependencies.json')
        context.echo "============= ${json.name}"
        loadResource((String)json.name)
        json
    }

    def loadResource(String projectName) {
        String x = context.libraryResource("projects/config/${projectName}.json");
        context.echo "============= ${x}"
        Map<String, String> variables = [randomSchema: "${ransomSchema()}"]
        context.echo "============= ${templateProcessor.fetchTemplate(x, variables)}"
    }

    def rawResource() {
        context.libraryResource("projects/common-vars.json");
    }

    def ransomSchema() {
        return "abc"
    }

    private jsonParse(String jsonFile) {
        String json = context.readFile(jsonFile)
        return new JsonSlurperClassic().parseText(json)
    }
}
