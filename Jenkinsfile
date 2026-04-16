pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK11'
    }
    
    environment {
        APP_NAME = 'jenkins-demo-app'
        APP_VERSION = '1.0.0'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Starting build process...'
                sh 'mvn clean compile'
                echo 'Build completed successfully!'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running automated tests...'
                sh 'mvn test'
                echo 'Tests completed successfully!'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    echo 'Test results published'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Creating application package...'
                sh 'mvn package -DskipTests'
                echo 'Package created successfully!'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    echo 'Artifacts archived successfully'
                }
            }
        }
        
        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                sh 'java -jar target/${APP_NAME}-${APP_VERSION}.jar'
                echo 'Application deployed successfully!'
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
            cleanWs()
        }
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline execution failed!'
        }
    }
}
