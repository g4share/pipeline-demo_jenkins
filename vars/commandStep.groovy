import com.g4share.pipeline.command.Json

def call(Closure body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body.call()

    Json json = new Json(context: this)

    def command = json.config((String)config.commandName, (Map<String, String>)config.bind)

    def cmd = command.script + " " + command.parameters
            .collect { key, value -> "$key=$value" }
            .join(" ")
    cmd
}