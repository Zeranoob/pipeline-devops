def call(){
  
pipeline {
        agent any

        parameters { choice(name: 'herramienta', choices: ['gradle','maven'], description: '') }

        stages {
                stage('Pipelines') {
                        steps {
                                script {

                                        params.herramienta // -> gradle o maven

                                        if(params.herramienta == 'gradle'){
                        def ejecucion = load 'gradle.groovy'
                        ejecucion.call()
                    }else{
                        def ejecucion = load 'maven.groovy'
                        ejecucion.call()
                    }
                                }
                        }
                }
        }
  }

}

return this;
