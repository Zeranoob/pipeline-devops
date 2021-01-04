def call(){
    def stages = ["build & test", "sonar", "run", "rest", "nexus"] as String[]
    def userStages

    if(params.stage != ''){
        userStages = stage.split(';')
    }
     
    if(userStages.every { stages.contains(it) }){
    for(stg in userStages){
        if(stg.contains("build & test")){
            stage('build & test'){ 
                env.TAREA = env.STAGE_NAME
                sh "./gradle clean build"
    }
        }
        if(stg.contains("sonar")){
            stage('sonar'){
               env.TAREA = env.STAGE_NAME
               def scannerHome = tool 'sonar-scanner';
               withSonarQubeEnv('Sonar'){ 
                   sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        }  
    }
        }
        if(stg.contains("run")){
            stage('run'){
                env.TAREA = env.STAGE_NAME
                sh 'nohup bash gradle bootRun &'
                sleep 15
    }
        }
        if(stg.contains("rest")){
            stage('rest'){
                env.TAREA = env.STAGE_NAME
                sh 'curl -X GET http://localhost:8081/rest/mscovid/test?msg=testing'
    }
        }
        if(stg.contains("nexus")){
            stage('nexus'){
                env.TAREA = env.STAGE_NAME
                nexusPublisher nexusInstanceId: 'Nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]    
    }
        }

}  
}
}
return this;
