import com.g4share.pipeline.command.Json
import com.g4share.pipeline.command.Repo
import com.g4share.pipeline.command.RepoInfo
import com.g4share.pipeline.core.TemplateProcessor

def call() {

    Repo repo = new Repo(this)
    Json json = new Json(this)

    TemplateProcessor templateProcessor = new TemplateProcessor()

    Map<String, String> params = [:]

    RepoInfo repoInfo

    pipeline {
        agent any

        stages {
            stage('Checkout') {
                steps {
                    script {
                        repoInfo = repo.checkout()
                        repo.validateVersion((String)repoInfo.scmInfo.GIT_COMMIT, "main")
                    }
                }
            }
            stage('Prepare') {
                steps {
                    script {
                        String resource = json.rawResource()
                        echo templateProcessor.fetchTemplate(resource)
                    }
                }
            }

            stage('Deploy') {
                steps {
                    script {
                        json.config()
                    }
                }
            }
        }
    }
}