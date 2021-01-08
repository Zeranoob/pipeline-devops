def call(String stageParam) {
    stages = ['DownloadNexus', 'RunDownloadedJar', 'Rest', 'nexusCD']

    // Si stage es vacio se consideran todos los stages
    _stage = stageParam ? stageParam.split(';') : stages
    // Se valida stage ingresado
    _stage.each { el ->
        if (!stages.contains(el)) {
            throw new Exception("Stage: $el no es una opción válida.")
        }
    }

    if(_stage.contains('DownloadNexus')) {
        stage('DownloadNexus') {
            env.LAST_STAGE_NAME = env.STAGE_NAME
            sh "curl -X GET -U admin:admin http://localhost:8081/repository/test-repo/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O"
            
        }
    }
    
    if(_stage.contains('RunDownloadedJar')) {
        stage('RunDownloadedJar') {
            env.LAST_STAGE_NAME = env.STAGE_NAME
            sh "nohup java -jar DevOpsUsach2020-0.0.1.jar --server.port=8089 &"
            sleep 10
        }
    }

    if(_stage.contains('Rest')) {
        stage('Rest') {
            env.LAST_STAGE_NAME = env.STAGE_NAME
            sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
        }
    }

    if(_stage.contains('NexusCD')) {
        stage('NexusCD') {
            env.LAST_STAGE_NAME = env.STAGE_NAME
            nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: 'v1-0-0']]]
        }
    }
}

return this