def checkoutRepo(String repoUrl, String branch, String credentialsId) {
    checkout([$class: 'GitSCM',
        branches: [[name: branch]],
        userRemoteConfigs: [[
            url: repoUrl,
            credentialsId: credentialsId
        ]]
    ])
}
