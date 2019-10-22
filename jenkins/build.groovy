def label = UUID.randomUUID().toString()
def projectName = "SSRTBPriceCrypter"
podTemplate(
  inheritFrom: 'default',
  label: label,
  containers: [
    containerTemplate(
      name: 'maven',
      image: 'maven:3-jdk-8-slim',
      ttyEnabled: true,
      command: '/bin/cat',
      resourceRequestCpu: '4',
      resourceRequestMemory: '2G',
      resourceLimitCpu: '4',
      resourceLimitMemory: '3.5G'
    )
]){
  node(label) {
    stage ('checkout') {
      git(
        url: "${gitSshUrl}",
        credentialsId: "${gitCredentialsId}",
        branch: "${gitBranch}"
      )
    }
    stage ('build/test/publish') {
      container(name: 'maven', shell: '/bin/sh') {
        sh """#!/bin/sh
		mvn -B -DskipTests clean package
		mvn -B test
		mvn -B deploy
        """
      }
    }
  }
}
