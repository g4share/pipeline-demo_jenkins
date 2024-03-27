package com.g4share.pipeline.core

class Binding {
    def randomSchema

    def init() {
        randomSchema = randomSchema(20);
    }

    private String randomSchema(length = MAX_USER_SUFFIX_LENGTH) {
        def alphabet = (('0'..'9') + ('A'..'Z')).join()
        generator(alphabet, length)
    }

    def MAX_USER_SUFFIX_LENGTH = 10

    def generator = { String alphabet, int n ->
        new Random().with {
            (1..n).collect {
                alphabet[nextInt(alphabet.length())]
            }.join()
        }
    }
}
