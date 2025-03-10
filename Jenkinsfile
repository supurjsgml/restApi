pipeline {
    agent any

    environment {
        BRANCH = 'master'
        DEPLOY_SERVER = 'ec2-user@54.205.85.125'
        APP_DIR = '/home/ec2-user/restApi'
        GIT_CREDENTIALS_ID = 'github-access-token'  // Jenkins Credentials에서 등록한 ID
    }

    stages {
        // 1️⃣ Build Stage
        stage('Build JAR') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: GIT_CREDENTIALS_ID, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh '''
                            ssh -i /home/ec2-user/.ssh/id_rsa ${DEPLOY_SERVER} "bash -s" <<EOF
                                echo "[1] 기존 프로젝트 유지한 채 소스 최신화"
                                cd ${APP_DIR}
                                git reset --hard origin/${BRANCH}
                                git pull origin ${BRANCH}

                                echo "[2] 기존 JAR 파일을 이전 버전으로 백업"
                                if [ -f ${APP_DIR}/build/libs/restApi.jar ]; then
                                    mv ${APP_DIR}/build/libs/restApi.jar ${APP_DIR}/build/libs/restApi_prev.jar
                                fi

                                echo "[3] 새 JAR 파일 빌드 (기존 JAR 유지)"
                                cd ${APP_DIR}
                                chmod +x gradlew
                                
                                # ✅ nohup으로 Gradle 빌드 실행
                                ./gradlew clean build -x test > ${APP_DIR}/gradle_build.log 2>&1
                            EOF
                        '''
                    }
                }
            }
        }

        // 2️⃣ Deploy Stage
        stage('Deploy to EC2') {
            steps {
                script {
                    sh '''
                            ssh -i /home/ec2-user/.ssh/id_rsa ${DEPLOY_SERVER} "bash -s" <<EOF
                            echo "[4] 기존 실행 중인 스프링 부트 서버 종료 시도"
                            pgrep -f 'build/libs/restApi.jar' | xargs kill -9 || true

                            echo "[5] 새로운 JAR 실행"
                            nohup java -jar ${APP_DIR}/build/libs/restApi.jar --server.port=8081 --spring.profiles.active=prod > ${APP_DIR}/app.log 2>&1 & disown
                            
                            echo "[6] 실행된 프로세스 확인"
                            ps aux | grep java
                            
                            echo "[7] nohup 로그 확인"
                            tail -n 20 ${APP_DIR}/app.log
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
