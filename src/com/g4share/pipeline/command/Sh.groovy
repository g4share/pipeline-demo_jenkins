package com.g4share.pipeline.command

class Sh extends Command {

    def exec(script, returnStdout = true, enabled = true) {
        if (!enabled) {
            context.echo ">>>> sh: ${script}"
            return
        }

        def output = context.sh (returnStdout: returnStdout, script: script).trim()
        return output
    }

    def setX(String file) {
        exec("chmod +x \"$file\"")
    }

    def pwd() {
        exec("pwd", true, true)
    }

    def ls() {
        exec("ls -al")
    }
}
