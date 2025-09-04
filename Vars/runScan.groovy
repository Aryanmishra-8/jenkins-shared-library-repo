def call(String reportFile) {
    sh "./gitleaks detect --source . --report-format html --report-path ${reportFile}"
}
