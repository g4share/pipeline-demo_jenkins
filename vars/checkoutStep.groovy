import com.g4share.pipeline.command.Repo
import com.g4share.pipeline.command.Sh

def call() {
    Repo repo = new Repo(context: this, sh: new Sh(context: this))
    return repo.checkout()
}