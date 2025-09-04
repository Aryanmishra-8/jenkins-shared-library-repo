def call(String reportFile) {
    archiveArtifacts artifacts: reportFile, allowEmptyArchive: true
}
