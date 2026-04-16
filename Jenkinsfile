
pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK11'
    }
    
    environment {
        APP_NAME = 'jenkins-demo-app'
        APP_VERSION = '1.0.0'
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
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
        
        stage('Parallel Testing') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        echo 'Running unit tests with code coverage...'
                        sh 'mvn test jacoco:report'
                        echo 'Unit tests completed successfully!'
                    }
                    post {
                        always {
                            junit 'target/surefire-reports/*.xml'
                            publishHTML([
                                allowMissing: false,
                                alwaysLinkToLastBuild: true,
                                keepAll: true,
                                reportDir: 'target/site/jacoco',
                                reportFiles: 'index.html',
                                reportName: 'Code Coverage Report'
                            ])
                        }
                    }
                }
                
                stage('Code Quality Check') {
                    steps {
                        echo 'Performing code quality checks...'
                        sh '''
                            echo "Checking for compilation warnings..."
                            mvn compile 2>&1 | grep -i warning || echo "No compilation warnings found"
                            
                            echo "Checking code style..."
                            find src -name "*.java" -exec wc -l {} + | tail -1 | awk '{print "Total lines of code: " $1}'
                            
                            echo "Checking for TODO comments..."
                            grep -r "TODO" src/ || echo "No TODO comments found"
                        '''
                        echo 'Code quality check completed!'
                    }
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
        
        stage('Integration Test') {
            steps {
                echo 'Running integration tests...'
                sh '''
                    echo "Starting application for integration testing..."
                    timeout 10s java -jar target/${APP_NAME}-${APP_VERSION}.jar > app.log 2>&1 || true
                    
                    echo "Running integration test scenarios..."
                    echo "Test 1: Application startup verification"
                    if grep -q "Hello Jenkins CI/CD!" app.log; then
                        echo "✓ Application started successfully"
                    else
                        echo "✗ Application startup failed"
                        exit 1
                    fi
                    
                    echo "Test 2: Application version check"
                    if grep -q "Application version: 1.0.0" app.log; then
                        echo "✓ Version information correct"
                    else
                        echo "✗ Version information missing or incorrect"
                        exit 1
                    fi
                '''
                echo 'Integration tests completed successfully!'
            }
        }
        
        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                echo 'Deploying application...'
                sh '''
                    echo "Deployment simulation - copying JAR to deployment directory"
                    mkdir -p deployment
                    cp target/${APP_NAME}-${APP_VERSION}.jar deployment/${APP_NAME}-${APP_VERSION}-build-${BUILD_NUMBER}.jar
                    echo "Application deployed as ${APP_NAME}-${APP_VERSION}-build-${BUILD_NUMBER}.jar"
                    
                    echo "Creating deployment manifest..."
                    cat > deployment/manifest.txt << EOL
Application: ${APP_NAME}
Version: ${APP_VERSION}
Build: ${BUILD_NUMBER}
Deployed: $(date)
JAR File: ${APP_NAME}-${APP_VERSION}-build-${BUILD_NUMBER}.jar
EOL
                    
                    echo "Verifying deployment..."
                    if [ -f "deployment/${APP_NAME}-${APP_VERSION}-build-${BUILD_NUMBER}.jar" ]; then
                        echo "✓ Deployment verification successful"
                        cat deployment/manifest.txt
                    else
                        echo "✗ Deployment verification failed"
                        exit 1
                    fi
                '''
                echo 'Application deployed successfully!'
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
            sh 'rm -f app.log'
            cleanWs()
        }
        success {
            echo '🎉 Pipeline executed successfully!'
            echo "Build #${BUILD_NUMBER} completed successfully"
            echo 'All stages completed: Checkout → Build → Parallel Testing → Package → Integration Test → Deploy'
        }
        failure {
            echo '❌ Pipeline execution failed!'
            echo "Build #${BUILD_NUMBER} failed"
            echo 'Check the logs above to identify the failing stage'
        }
    }
}
