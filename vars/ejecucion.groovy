def call(){
  
pipeline {
        agent any
        parameters { choice(name: 'herramienta', choices: ['gradle','maven'], description: 'Elección de herramienta de construcción para aplicación covid') }
        stages {
                stage('Pipelines') {
                        steps {
                                script {
                                        def STAGE_NAME = ''
                                        params.herramienta // -> gradle o maven
                                        if(params.herramienta == 'gradle'){
                        
                        gradle.call()
                    }else{
                      
                        maven.call()
                    }
                }
                        }
                }
        }
         post {
                success {
                        slackSend teamDomain: 'devops-usach-2020', tokenCredentialId: 'token-slack', color: 'good', message: "Diego Perez][Pipeline-maven-gradle][${params.herramienta}] Ejecución exitosa."
                }
                failure {
                        slackSend teamDomain: 'devops-usach-2020', tokenCredentialId: 'token-slack', color: 'danger', message: "[Diego Perez][Pipeline-maven-gradle][${params.herramienta}] Ejecución fallida en stage ${STAGE_NAME}."
                                }
            }
        }
 }

return this;