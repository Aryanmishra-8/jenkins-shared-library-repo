def sendEmail(String recipient, String status, String reportFile) {
    def subject = "Jenkins Job ${status}: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
    def body = """Hello,

Credential scanning job ${status.toLowerCase()}.

Check the HTML report here:
${env.BUILD_URL}artifact/${reportFile}

Thanks,
Jenkins
"""

    mail to: recipient, subject: subject, body: body
}
