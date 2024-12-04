#!/usr/bin/env groovy
def call(Map config) {
  def gitUrl = config.gitUrl ?: error("No url passed")            
   def branch = config.branch ?: error("No branch passed") 
   def gitToken = config.gitToken ?: ""  
    cleanWs()
   checkout scmGit(branches: [[name: config.branch]], userRemoteConfigs: [[credentialsId: config.gitToken, url: config.gitUrl]])
}