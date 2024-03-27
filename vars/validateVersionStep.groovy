import com.g4share.pipeline.command.Repo
import com.g4share.pipeline.command.Sh

def call(Closure body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body.call()

    Repo repo = new Repo(context: this, sh: new Sh(context: this))
    repo.validateVersion((String)config.commit, (String)config.base)
}