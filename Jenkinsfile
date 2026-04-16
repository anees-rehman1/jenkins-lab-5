
pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK17'
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
                    echo 'Test results and coverage report published'
                }
            }
        }
        
        stage('Code Quality Check') {
            steps {
                echo 'Performing code quality checks...'
                sh '''
                    echo "Checking for compilation warnings..."
                    mvn compile 2>&1 | grep -i warning || echo "No compilation warnings found"
                    
                    echo "Checking test coverage threshold..."
                    COVERAGE=$(grep -o 'Total.*[0-9]*%' target/site/jacoco/index.html | grep -o '[0-9]*%' | head -1 | sed 's/%//')
                    echo "Code coverage: ${COVERAGE}%"
                    if [ "$COVERAGE" -lt 50 ]; then
                        echo "Warning: Code coverage is below 50%"
                    else
                        echo "Code coverage meets minimum threshold"
                    fi
                '''
                echo 'Code quality check completed!'
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
                    java -jar target/${APP_NAME}-${APP_VERSION}.jar > app.log 2>&1 &
                    APP_PID=$!
                    sleep 2
                    
                    echo "Running integration test scenarios..."
                    echo "Test 1: Application startup verification"
                    if grep -q "Hello Jenkins CI/CD!" app.log; then
                        echo "✓ Application started successfully"
                    else
                        echo "✗ Application startup failed"
                        kill $APP_PID 2>/dev/null || true
                        exit 1
                    fi
                    
                    echo "Test 2: Application version check"
                    if grep -q "Application version: 1.0.0" app.log; then
                        echo "✓ Version information correct"
                    else
                        echo "✗ Version information missing or incorrect"
                        kill $APP_PID 2>/dev/null || true
                        exit 1
                    fi
                    
                    echo "Stopping application..."
                    kill $APP_PID 2>/dev/null || true
                    sleep 1
                '''
                echo 'Integration tests completed successfully!'
            }
        }
        
        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                sh '''
                    echo "Deployment simulation - copying JAR to deployment directory"
                    mkdir -p deployment
                    cp target/${APP_NAME}-${APP_VERSION}.jar deployment/
                    echo "Application deployed to deployment directory"
                    
                    echo "Verifying deployment..."
                    if [ -f "deployment/${APP_NAME}-${APP_VERSION}.jar" ]; then
                        echo "✓ Deployment verification successful"
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
            echo 'All stages completed: Checkout → Build → Test → Quality Check → Package → Integration Test → Deploy'
        }
        failure {
            echo '❌ Pipeline execution failed!'
            echo 'Check the logs above to identify the failing stage'
        }
    }
}

