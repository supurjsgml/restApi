pipeline {
    agent any

    environment {
        BRANCH = 'master'
        DEPLOY_SERVER = 'ec2-user@34.235.143.19'
        APP_DIR = '/home/ec2-user/restApi'
        GIT_CREDENTIALS_ID = 'github-access-token'  // Jenkins Credentials에서 등록한 ID
    }

    stages {
        stage('Deploy to EC2 and Build') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: GIT_CREDENTIALS_ID, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh '''
                        ssh -i /home/ec2-user/.ssh/id_rsa ${DEPLOY_SERVER} "bash -s" <<EOF
                        # 1️⃣ 기존 프로젝트 삭제 후 새롭게 클론
                        pgrep -f 'build/libs/restApi.jar' | xargs kill -9 || true
                        rm -rf ${APP_DIR}
                        git clone -b ${BRANCH} https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/supurjsgml/restApi.git ${APP_DIR}

                        # 2️⃣ 빌드 실행
                        cd ${APP_DIR}
                        chmod +x gradlew
                        ./gradlew clean build -x test

                        # 3️⃣ 기존 실행 중인 애플리케이션 종료 후 새 JAR 실행
                        # pkill -f 'java -jar' || true
                        cd ${APP_DIR}
                        nohup java -jar build/libs/restApi.jar --server.port=8081 --spring.profiles.active=prod > app.log 2>&1 &
                        ps aux | grep java
                        EOF
                        '''
                    }
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
