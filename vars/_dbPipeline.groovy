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
        agent any

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

            stage('Migrate') {
                when { expression { isStageEnabled("${STAGE_NAME}", enabledStages) }}

                steps {
                    script {
                        def cmd = commandStep {
                            commandName = "migrate"
                            bind = binding.properties
                        }

                        //TODO: https://github.com/g4share/pipeline-demo_jenkins/issues/1
                        echo "${sh.exec(cmd)}"
                    }
                }
            }
        }

        post {
            always {
                script {
                    if (isStageEnabled("clean", enabledStages)) {
                        def cmd = commandStep {
                            commandName = "clean"
                            bind = binding.properties
                        }
                        echo "${sh.exec(cmd)}"
                    }
                }
            }
        }
    }
}