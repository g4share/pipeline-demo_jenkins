package com.g4share.pipeline.command

class Repo extends Command {

    def sh

    RepoInfo checkout() {
        def scmInfo = context.checkout context.scm
        return new RepoInfo(scmInfo: scmInfo, repoInfo: repoInfo())
    }

    private def repoInfo() {
        def gitConfig = sh.exec('git config remote.origin.url')
        def url = gitConfig.substring(0, gitConfig.lastIndexOf('/') + 1)
        def name = gitConfig.substring(gitConfig.lastIndexOf('/') + 1) - '.git'

        return [url: url, name: name]
    }


    def validateVersion(String commit, String base) {
        //extract last 'base' commit - Jenkins doesn't fetch branch names
        String baseCommit = sh.exec("git ls-remote origin refs/heads/${base} | cut -f 1")

        if (commit == baseCommit) {
            return
        }
        String prevVersion = getVersion(baseCommit)
        String version = getVersion(commit)

        if (compareVersions(version, prevVersion) != 1) {
            context.error("The branch version \"$version\" must be greater than the base version \"$prevVersion\"")
        }
    }

    def getVersion(String commit) {
        sh.exec("git checkout $commit")

        def version = sh.exec('./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout')
        version
    }

    static int compareVersions(String version1, String version2) {
        List<Integer> v1Components = version1.tokenize('.').collect { it.toInteger()}
        List<Integer> v2Components = version2.tokenize('.').collect { it.toInteger()}

        int length = Math.max(v1Components.size(), v2Components.size())

        for (int i = 0; i < length; i++) {
            int v1 = i < v1Components.size() ? v1Components[i] : 0
            int v2 = i < v2Components.size() ? v2Components[i] : 0

            if (v1 > v2) {
                return 1
            }
            if (v1 < v2) {
                return -1
            }
        }
        return 0
    }
}