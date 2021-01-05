def call(){
  
pipeline {
        agent any
        parameters { 
                choice(name: 'herramienta', choices: ['gradle','maven'], description: 'Elección de herramienta de construcción para aplicación covid') 
                string(name: 'stage', defaultValue: '', description: '')
        }
        stages {
                stage('Pipelines') {
                        enviroment {
                            LAST_STAGE_NAME = ''
                        }
                        steps {
                                script {
<<<<<<< HEAD
                                    println 'herramienta: ' + params.herramienta
                                    println 'stage: ' + params.stage
                                        
                                        if(params.herramienta == 'gradle'){ 
=======
                                        env.TAREA = ''
                                        params.stage 
                                        if(params.stage == 'gradle'){ 
>>>>>>> 41aa036a05f78f44e6e235a80d7f53dc63a459fd
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
                        slackSend color: 'good', message: "Diego Perez][${env.JOB_NAME}][${params.herramienta}] Ejecución exitosa."
                }
                failure {
                        slackSend color: 'danger', message: "[Diego Perez][${env.JOB_NAME}][${params.herramienta}] Ejecución fallida en stage [${env.LAST_STAGE_NAME}]."
                                }
                }
        }
 }

return this;
