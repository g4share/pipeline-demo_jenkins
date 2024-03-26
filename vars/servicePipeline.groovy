import com.g4share.pipeline.command.Repo
import com.g4share.pipeline.command.RepoInfo

def call() {

    Repo repo = new Repo(this)

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
        }
    }
}