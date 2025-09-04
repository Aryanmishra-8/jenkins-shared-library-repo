def call(Map config = [:]) {
    pipeline {
        agent any

        environment {
            RECIPIENT      = config.get('recipient', 'default@mail.com')
            REPO_URL       = config.get('repoUrl', '')
            BRANCH         = config.get('branch', 'main')
            CREDENTIALS_ID = config.get('credentialsId', '')
            GITLEAKS_VER   = config.get('gitleaksVersion', '8.18.2')
            REPORT_FILE    = config.get('reportFile', 'report.html')
        }

        stages {
            stage('Checkout Code') {
                steps {
                    script {
                        gitUtils.checkoutRepo(env.REPO_URL, env.BRANCH, env.CREDENTIALS_ID)
                    }
                }
            }

            stage('Install Gitleaks') {
                steps {
                    script {
                        gitleaksUtils.install(env.GITLEAKS_VER)
                    }
                }
            }

            stage('Credential Scanning') {
                steps {
                    script {
                        gitleaksUtils.scan(env.REPORT_FILE)
                    }
                }
            }

            stage('Archive Scan Report') {
                steps {
                    archiveArtifacts artifacts: "${env.REPORT_FILE}", allowEmptyArchive: true
                }
            }
        }

        post {
            success {
                script {
                    notifyUtils.sendEmail(env.RECIPIENT, "Success", env.REPORT_FILE)
                }
            }
            failure {
                script {
                    notifyUtils.sendEmail(env.RECIPIENT, "Failed", env.REPORT_FILE)
                }
            }
            always {
                echo "Pipeline finished. Check status above."
            }
        }
    }
}
