def setup(Map config = [:]) {
    echo "Checking out repo: ${config.repoUrl} branch: ${config.branch ?: 'main'}"
    checkout([$class: 'GitSCM',
        branches: [[name: "*/${config.branch ?: 'main'}"]],
        userRemoteConfigs: [[url: config.repoUrl]]
    ])
}

def installPython(String version = '3.11', String requirementsFile = 'requirements.txt') {
    echo "Installing Python ${version} and dependencies..."
    sh """
        python${version} --version
        python${version} -m pip install --upgrade pip
        python${version} -m pip install -r ${requirementsFile}
    """
}

def build(String buildCommand = 'python -m compileall .') {
    echo "Building project..."
    sh buildCommand
}

def test(String testCommand = 'pytest --junitxml=reports/results.xml --html=reports/report.html', String reportDir = 'reports') {
    echo "Running tests..."
    sh """
        mkdir -p ${reportDir}
        ${testCommand}
    """
    archiveArtifacts artifacts: "${reportDir}/**", allowEmptyArchive: true
}

def notify(String status, String recipients, String reportPath = 'reports/report.html') {
    mail(
        from: 'jenkins-unit-testing',
        to: recipients,
        subject: "Python CI Build ${status}: ${currentBuild.fullDisplayName}",
        body: "Hey, your build ${status}. Check reports: ${env.BUILD_URL}artifact/${reportPath}"
    )
}
