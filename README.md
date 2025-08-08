## 실행 명령

docker compose up -d --build


## 구성 컨테이너

1. **app** (Spring Boot 애플리케이션)
    - 이미지: `openjdk:17-jdk`
    - 포트: `8080`
    - Dockerfile은 컨테이너 내부에서 빌드

2. **mysql**
    - 이미지: `mysql:8.0`
    - 포트: `3311`
    - 초기 데이터베이스 및 계정 생성
    - 데이터 영속성을 위해 volume 마운트
