pipeline {
    agent any

    environment {
        BRANCH = 'master'
        DEPLOY_SERVER = 'ec2-user@54.234.44.252'
        APP_DIR = '/home/ec2-user/restApi'
        GIT_CREDENTIALS_ID = 'github-access-token'
        BUILD_DIR = 'build/libs' // JAR 빌드 경로
    }

    stages {
        stage('Checkout & Build JAR') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: GIT_CREDENTIALS_ID, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh '''
                            echo "[1] 최신 코드 Pull"
                            git clone -b ${BRANCH} https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/supurjsgml/restApi.git || (cd restApi && git pull origin ${BRANCH})

                            echo "[2] Gradle Build 실행"
                            cd restApi
                            chmod +x gradlew
                            ./gradlew clean build -x test

                            echo "[3] 빌드 완료: JAR 파일 목록"
                            ls -lh ${BUILD_DIR}
                        '''
                    }
                }
            }
        }

        stage('Deploy JAR to EC2') {
            steps {
                script {
                    sh '''
                        JAR_NAME="restApi_$(date +'%Y%m%d%H%M%S').jar"
                        echo "[4] 생성된 JAR 이름: $JAR_NAME"

                        echo "[5] 기존 JAR 파일 백업 및 새 JAR 전송"
                        scp -o StrictHostKeyChecking=no -i /home/ec2-user/.ssh/id_rsa restApi/${BUILD_DIR}/restApi.jar ${DEPLOY_SERVER}:${APP_DIR}/build/libs/$JAR_NAME

                        echo "[6] 서버에서 기존 애플리케이션 종료 후 새 JAR 실행"
                        ssh -o StrictHostKeyChecking=no -i /home/ec2-user/.ssh/id_rsa ${DEPLOY_SERVER} "bash -s" <<EOF
                        echo "[7] 기존 실행 중인 스프링 서버 종료 시도"
                        pgrep -f 'build/libs/restApi.jar' | xargs kill -9 || true

                        echo "[8] 새 JAR 실행"
                        nohup java -jar ${APP_DIR}/$JAR_NAME --server.port=8081 --spring.profiles.active=prod > ${APP_DIR}/app.log 2>&1 & disown

                        echo "[9] 실행된 프로세스 확인"
                        ps aux | grep java
EOF
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment Successful'
        }
        failure {
            echo 'Deployment Failed'
        }
    }
}
