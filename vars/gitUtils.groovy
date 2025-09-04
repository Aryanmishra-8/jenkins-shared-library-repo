def checkoutRepo(String repoUrl, String branch, String credentialsId) {
    try {
        checkout([$class: 'GitSCM',
            branches: [[name: branch]],
            userRemoteConfigs: [[
                url: repoUrl,
                credentialsId: credentialsId
            ]]
        ])
        echo " Repository checkout successful: ${repoUrl} [${branch}]"
    } catch (Exception e) {
        error " Git Checkout Failed! Check if repo URL, branch, or credentials are correct.\nError: ${e.message}"
    }
}
