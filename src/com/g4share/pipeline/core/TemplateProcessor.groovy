package com.g4share.pipeline.core

import groovy.text.SimpleTemplateEngine

class TemplateProcessor implements Serializable {

    String fetchTemplate(String template, Map<String, String> vars) {
        String result = new SimpleTemplateEngine()
                .createTemplate(template)
                .make(vars)
         result
    }
}
