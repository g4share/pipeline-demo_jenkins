import com.g4share.pipeline.command.RepoInfo
import com.g4share.pipeline.command.Sh
import com.g4share.pipeline.core.Binding

import static com.g4share.pipeline.core.Stages.isStageEnabled

def call(Map params = [:]) {

    String baseBranch = params.get('baseBranch', 'main')
    List<String> enabledStages = params.get('stages', [])

    Binding binding = new Binding()
    binding.init()

    RepoInfo repoInfo
    Sh sh = new Sh(context: this)

    pipeline {
        //agent any
        agent { label 'java21' }

        stages {
            stage('Checkout') {
                when { expression { isStageEnabled("${STAGE_NAME}", enabledStages) }}

                steps {
                    script {
                        repoInfo = checkoutStep()
                        validateVersionStep {
                            commit = repoInfo.scmInfo.GIT_COMMIT
                            base = baseBranch
                        }
                    }
                }
            }

            stage('Compile') {
                when { expression { isStageEnabled("${STAGE_NAME}", enabledStages) }}

                steps {
                    script {
                        def cmd = commandStep {
                            commandName = "compile"
                            bind = binding.properties
                        }
                        echo "${sh.exec(cmd)}"
                    }
                }
            }

            stage('Test') {
                when { expression { isStageEnabled("${STAGE_NAME}", enabledStages) }}

                steps {
                    script {
                        def cmd = commandStep {
                            commandName = "unit-test"
                            bind = binding.properties
                        }
                        echo "${sh.exec(cmd)}"
                    }
                }
            }

            stage('Integration Tests') {
                when { expression { isStageEnabled("${STAGE_NAME}", enabledStages) }}

                steps {
                    script {
                        def cmd = commandStep {
                            commandName = "integration-test"
                            bind = binding.properties
                        }
                        echo "${sh.exec(cmd)}"
                    }
                }
            }
        }
    }
}