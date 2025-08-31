def setup(Map config = [:]) {
    pipeline {
        agent any

        environment {
            RECIPIENTS = "${config.emailRecipients}"
        }

        stages {
            stage('Checkout') {
                steps {
                    echo "Checking out repo: ${config.repoUrl}, branch: ${config.branch ?: 'main'}"
                    checkout([$class: 'GitSCM',
                        branches: [[name: "*/${config.branch ?: 'main'}"]],
                        userRemoteConfigs: [[url: "${config.repoUrl}"]]
                    ])
                }
            }

            stage('Install Dependencies') {
                steps {
                    sh 'pip install --upgrade pip'
                    sh 'pip install -r requirements.txt'
                }
            }

            stage('Code Compilation') {
                steps {
                    echo "Running Python code compilation..."
                    sh '''
                        mkdir -p reports
                        python -m py_compile $(find . -name "*.py") > reports/code_compile_report.txt 2>&1
                    '''
                    archiveArtifacts artifacts: 'reports/code_compile_report.txt', followSymlinks: false
                }
            }
        }

        post {
            success {
                mail(
                    to: "${RECIPIENTS}",
                    subject: "Build SUCCESS: ${currentBuild.fullDisplayName}",
                    body: "Code compilation finished successfully.\nCheck report here: ${env.BUILD_URL}artifact/reports/code_compile_report.txt"
                )
            }
            failure {
                mail(
                    to: "${RECIPIENTS}",
                    subject: "Build FAILED: ${currentBuild.fullDisplayName}",
                    body: "Code compilation failed.\nCheck report here: ${env.BUILD_URL}artifact/reports/code_compile_report.txt"
                )
            }
        }
    }
}
