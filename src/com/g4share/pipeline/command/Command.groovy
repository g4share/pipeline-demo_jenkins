package com.g4share.pipeline.command

class Command implements Serializable {

    def context

    protected Sh sh;

    Command(context) {
        this.context = context;
        sh = new Sh(context: context)
    }
}