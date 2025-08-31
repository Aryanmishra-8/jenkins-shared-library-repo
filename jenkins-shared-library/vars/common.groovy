def setup(Map config = [:]) {
    echo "Checking out repo: ${config.repoUrl} branch: ${config.branch ?: 'main'}"
    checkout([$class: 'GitSCM',
        branches: [[name: "*/${config.branch ?: 'main'}"]],
        userRemoteConfigs: [[url: config.repoUrl]]
    ])
}

def gitleaksScan() {
    echo "Installing and running Gitleaks..."
    sh '''
        if ! command -v gitleaks >/dev/null 2>&1; then
            LATEST=$(curl -s https://api.github.com/repos/gitleaks/gitleaks/releases/latest | grep tag_name | cut -d '"' -f4)
            wget -q https://github.com/gitleaks/gitleaks/releases/download/$LATEST/gitleaks_${LATEST}_linux_x64.tar.gz -O gitleaks.tar.gz
            tar -xzf gitleaks.tar.gz gitleaks
            chmod +x gitleaks
            mkdir -p /var/lib/jenkins/bin
            mv gitleaks /var/lib/jenkins/bin/
            rm -f gitleaks.tar.gz
        fi
        gitleaks version
        gitleaks detect --source . --report-format json --report-path gitleaks-report.json
    '''
    archiveArtifacts artifacts: 'gitleaks-report.json', allowEmptyArchive: true
}

def notify(String status, String recipients) {
    mail(
        from: 'jenikins-unit-testing',
        to: recipients,
        subject: "Build ${status}: ${currentBuild.fullDisplayName}",
        body: "Hey Aryan, your build ${status}. Check reports: ${env.BUILD_URL}artifact/gitleaks-report.json"
    )
}
