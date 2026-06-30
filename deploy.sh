#!/bin/bash

# =========================================================================
#  Spring Boot Nginx 무중단 배포 스크립트 (PORT_A <-> PORT_B)
# =========================================================================

echo "========================================="
echo " [Nginx Port-Switching Deploy] Start"
echo "========================================="

# 외부에서 환경변수로 주입받은 포트를 사용하고, 없을 경우에만 기본값 할당
PORT_A=${PORT_A}
PORT_B=${PORT_B}

CURRENT_PORT=$PORT_A
TARGET_PORT=$PORT_B

# PORT_A가 활성화(LISTEN) 상태인지 확인
if lsof -i :$PORT_A > /dev/null; then
    CURRENT_PORT=$PORT_A
    TARGET_PORT=$PORT_B
else
    # PORT_A가 닫혀 있으면 PORT_B가 돌고 있거나 최초 기동임
    if lsof -i :$PORT_B > /dev/null; then
        CURRENT_PORT=$PORT_B
        TARGET_PORT=$PORT_A
    else
        # 둘 다 안 열려 있는 최초 기동일 경우 기본 타겟 포트를 PORT_A로 잡음
        CURRENT_PORT=$PORT_B
        TARGET_PORT=$PORT_A
    fi
fi

echo ">> 현재 구동 중인 포트: $CURRENT_PORT"
echo ">> 새로 배포할 타겟 포트: $TARGET_PORT"

# 타겟 포트에 기존 프로세스가 돌고 있다면 강제 종료 (포트 점유 방지)
TARGET_PID=$(lsof -t -i :$TARGET_PORT)
if [ ! -z "$TARGET_PID" ]; then
    echo ">> 타겟 포트($TARGET_PORT)에 이미 실행 중인 기존 프로세스($TARGET_PID) 종료"
    kill -9 $TARGET_PID
    sleep 2
fi

# 타겟 포트로 새 JAR 파일 구동
echo ">> 타겟 포트($TARGET_PORT)로 신규 JAR 기동 시작..."
# deploy.yml 등에서 넘겨받은 기존 환경변수들을 그대로 사용하여 백그라운드 구동
nohup java -jar -Dserver.port=$TARGET_PORT -Dspring.profiles.active=prod /home/ubuntu/rest-api/restApi.jar > /home/ubuntu/rest-api/nohup_$TARGET_PORT.out 2>&1 &

# 새 서버 헬스체크 (정상 구동될 때까지 최대 60초 대기)
echo ">> 신규 서버 헬스체크 시작 (http://localhost:$TARGET_PORT)..."
HEALTH_SUCCESS=false

for i in {1..60}; do
    # 헬스체크 응답 코드 확인
    STATUS_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:$TARGET_PORT/api/getUrl || true)
    
    if [ "$STATUS_CODE" = "200" ]; then
        echo ">> 신규 서버 기동 완료! (응답 코드: 200, 소요시간: ${i}초)"
        HEALTH_SUCCESS=true
        break
    fi
    
    echo ">> 헬스체크 대기 중... (${i}/60)"
    sleep 1
done

if [ "$HEALTH_SUCCESS" = false ]; then
    echo ">> [ERROR] 신규 서버 기동 실패 (60초 초과). 배포를 중단합니다."
    exit 1
fi

# Nginx 설정 스위칭 (PORT_A -> PORT_B 또는 PORT_B -> PORT_A)
echo ">> Nginx 라우팅 포트 스위칭 ($CURRENT_PORT -> $TARGET_PORT)"

# Nginx 설정 파일에서 기존 포트를 타겟 포트로 일괄 치환
# (경로는 Lightsail 서버 Nginx 설정 기본 경로인 /etc/nginx/sites-available/default 사용)
sudo sed -i "s/localhost:$CURRENT_PORT/localhost:$TARGET_PORT/g" /etc/nginx/sites-available/default

# Nginx 무중단 설정 리로드
sudo systemctl reload nginx
echo ">> Nginx 설정 리로드 완료. 라우팅 전환 유예를 위해 15초간 대기합니다..."
sleep 15

# 이전 버전 포트 프로세스 안전하게 종료
echo ">> 이전 버전 프로세스($CURRENT_PORT) 순차 종료 중..."
CURRENT_PID=$(lsof -t -i :$CURRENT_PORT)
if [ ! -z "$CURRENT_PID" ]; then
    # Graceful Shutdown 유도를 위해 SIGTERM(15) 전송 후 대기
    kill -15 $CURRENT_PID
    echo ">> Graceful Shutdown 진행 중... 10초 대기"
    sleep 10
    
    # 여전히 안 꺼졌다면 강제 종료(SIGKILL)
    REMAIN_PID=$(lsof -t -i :$CURRENT_PORT)
    if [ ! -z "$REMAIN_PID" ]; then
        echo ">> 이전 프로세스가 아직 살아있어 강제 종료합니다: $REMAIN_PID"
        kill -9 $REMAIN_PID
    fi
fi

echo "========================================="
echo " [Nginx Port-Switching Deploy] Success!"
echo "========================================="
