package com.g4share.pipeline.core

class Stages {
    static boolean isStageEnabled(String stageName, List<String> stages) {
        return stages
                ? stages.any { it == '*' || it.equalsIgnoreCase(stageName) }
                : true
    }
}