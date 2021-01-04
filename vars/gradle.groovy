def call(){
    def stages = ["Build & Test", "Sonar", "Run", "Rest", "Nexus"] as String[]
    def userStages

    if(params.stage != ''){
        userStages = stage.split(';')
    }
     
    if(userStages.every { stages.contains(it) }){
    for(stg in userStages){
        if(stg.contains("Build & Test")){
            stage('Build & Test'){ 
                env.TAREA = Build & Test
                sh "gradle clean build"
    }
        }
        if(stg.contains("Sonar")){
            stage('Sonar'){
               env.TAREA = Sonar
               def scannerHome = tool 'sonar-scanner';
               withSonarQubeEnv('Sonar'){ 
                   sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        }  
    }
        }
        if(stg.contains("Run")){
            stage('Run'){
                env.TAREA = Run
                sh 'nohup bash gradle bootRun &'
                sleep 5
    }
        }
        if(stg.contains("Rest")){
            stage('Rest'){
                env.TAREA = Rest
                sh 'curl -X GET http://localhost:8081/rest/mscovid/test?msg=testing'
    }
        }
        if(stg.contains("Nexus")){
            stage('Nexus'){
                env.TAREA = Nexus
                nexusPublisher nexusInstanceId: 'Nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]    
    }
        }

}  
}
}
return this;
