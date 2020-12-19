def call(){

	stage('Build & Test'){
		sh "gradle clean build"
	}	

	stage('Sonar'){
		def scannerHome = tool 'sonar-scanner';
		//nombre del servidor de sonar en jenkins
		withSonarQubeEnv('Sonar') {
			sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
		}
	}

	stage('Run'){
		sh "nohup gradle bootRun &"
		sleep 20
	}

	stage('Rest'){
		sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
	}

	stage('Nexus'){
		nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
	}
}

return this;
