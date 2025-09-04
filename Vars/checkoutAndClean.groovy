def call(String repoUrl, String branch, String credId) {
    cleanWs()
    checkout([
        $class: 'GitSCM',
        branches: [[name: branch]],
        userRemoteConfigs: [[url: repoUrl, credentialsId: credId]]
    ])
}
