#!/usr/bin/env groovy
def call(Map config) {
    
    def webhookUrl = config.webhookUrl ?: error("Webhook URL is required.")
    def message = config.message ?: 'Unstable'
    def color = config.color ?: 'good'  
    def title = config.title ?: 'Build Notification'
    def footer = config.footer ?: 'Jenkins'
    def footerIcon = config.footerIcon ?: 'https://jenkins.io/images/logos/jenkins/256.png'  
    def pipelineStarted = config.pipelineStarted ?: true
    
    
    if (pipelineStarted) {
        message = "Pipeline Started: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}"
        title = "Pipeline Started"
        color = 'warning'  
    }

    
    def finalMessage
    def finalColor
    if (currentBuild.result == 'SUCCESS') {
       finalMessage = "Pipeline run successful: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}"
        finalColor = 'good' 
    } else if (currentBuild.result == 'FAILURE') {
        finalMessage = "Pipeline failed: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}"
        finalColor = 'danger'  
    } else {
        finalMessage = message
        finalColor = color  
    }

    def slackMessage = [
        "attachments": [
            [
                "color"       : finalColor,
                "pretext"     : "Notification from Jenkins",
                "title"       : title,
                "text"        : finalMessage,
                "footer"      : footer,
                "footer_icon" : footerIcon
            ]
        ]
    ]
    
    withCredentials([string(credentialsId: webhookUrl, variable: 'WEBHOOK_URL')]) {
        def jsonMessage = groovy.json.JsonOutput.toJson(slackMessage)
        
        sh """
        curl -X POST -H 'Content-type: application/json' --data '${jsonMessage}' ${WEBHOOK_URL}
        """
    }
}
