def call(){

    figlet 'Gradle'
    figlet 'Despliegue Continuo'

    stages = ["DownloadNexus", "RunDownloadedJar", "Rest", "NexusCD"] as String[]
    
    // Si stage es vacio se ejecutan todas las stages
    _stage = params.stage ? params.stage.split(';') : stages
    // Validacion de stage ingresada
    _stage.each { el ->
        if (!stages.contains(el)) {
            throw new Exception("Stage: $el no es una opción válida.")
        }
    }

       if(_stage.contains('DownloadNexus')) {
            stage('DownloadNexus') {
              env.LAST_STAGE_NAME = env.STAGE_NAME
                sh 'curl -X GET -u admin:Pch1axli3003 http://localhost:8082/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O' 
            }
         }   

        if(_stage.contains("RunDownloadedJar")){
            stage('RunDownloadedJar'){
               env.LAST_STAGE_NAME = env.STAGE_NAME
               sh "nohup java -jar DevOpsUsach2020-0.0.1.jar &"
               sleep 10

            }
        }

        if(_stage.contains("Rest")){
            stage('Rest'){
                env.LAST_STAGE_NAME = env.STAGE_NAME
                sh 'curl -X GET http://localhost:8081/rest/mscovid/test?msg=testing'
            }
        }

        if(_stage.contains("NexusCD")){
            stage('Nexus'){
                env.LAST_STAGE_NAME = env.STAGE_NAME
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]    
             }
        }

    }  

return this;
