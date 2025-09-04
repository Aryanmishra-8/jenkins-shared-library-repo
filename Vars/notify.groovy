def call(String status, String email, String slackChannel, String slackCredId) {
    def reportUrl = "${env.BUILD_URL}artifact/report.html"
    def color
    def subject
    def body

    if (status == "success") {
        color = 'good'
        subject = "Jenkins Job Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        body = "Credential scanning job completed successfully.\n\nCheck the HTML report here:\n${reportUrl}"
    } else {
        color = 'danger'
        subject = "Jenkins Job Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        body = "Credential scanning job has failed.\n\nCheck the HTML report here:\n${reportUrl}"
    }

    // Mail
    mail to: email,
         subject: subject,
         body: body

    // Slack
    slackSend(
        channel: slackChannel,
        color: color,
        message: "${status.toUpperCase()}: Job ${env.JOB_NAME} #${env.BUILD_NUMBER}\nReport: ${reportUrl}",
        tokenCredentialId: slackCredId
    )
}
