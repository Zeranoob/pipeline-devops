def call(){
  
pipeline {
        agent any
        parameters { 
                choice(name: 'herramienta', choices: ['gradle','maven'], description: 'Elección de herramienta de construcción para aplicación covid') 
                string(name: 'stage', defaultValue: '', description: '')
        }
        stages {
                stage('Pipelines') {
                        environment {
                            LAST_STAGE_NAME = ''
                            
                        }
                        steps {
                                script {

                       if (env.GIT_BRANCH == "develop" || env.GIT_BRANCH == "feature"){
                                gradle_ci.call();
                        } else if (env.GIT_BRANCH.contains("release")){  
                                gradle_cd.call();                 
                                } else {
                                echo " La rama ${env.GIT_BRANCH} no se proceso" 
                                        }
                                }
                        }
                }
        }
         post {
                success {
                        slackSend color: 'good', message: "Diego Perez][${env.JOB_NAME}][${params.herramienta}] Ejecución exitosa."
                }
                failure {
                        slackSend color: 'danger', message: "[Diego Perez][${env.JOB_NAME}][${params.herramienta}] Ejecución fallida en stage [${env.LAST_STAGE_NAME}]."
                                }
                }
        }
 }

return this;
