pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/supurjsgml/restApi.git'
        BRANCH = 'master'
        DEPLOY_SERVER = 'ec2-user@50.17.47.14'
        APP_DIR = '/home/ec2-user/restApi'
    }

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    sh 'rm -rf restApi && git clone -b ${BRANCH} ${REPO_URL}'
                }
            }
        }

        stage('Build JAR') {
            steps {
                script {
                    sh '''
                    cd restApi
                    ./gradlew clean build -x test
                    '''
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                script {
                    sh '''
                    scp -i /home/ec2-user/.ssh/id_rsa restApi/build/libs/*.jar ${DEPLOY_SERVER}:${APP_DIR}
                    ssh -i /home/ec2-user/.ssh/id_rsa ${DEPLOY_SERVER} "bash -s" <<EOF
                    cd ${APP_DIR}
                    pkill -f 'java -jar' || true
                    nohup java -jar *.jar > app.log 2>&1 &
                    EOF
                    '''
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deployment Successful'
        }
        failure {
            echo '❌ Deployment Failed'
        }
    }
}
