# Node.js 이미지 사용
FROM node:14

# 작업 디렉토리 설정
WORKDIR /usr/src/app

# 의존성 설치
COPY package*.json ./
RUN npm install

# 애플리케이션 코드 복사
COPY . .

# 포트 열기
EXPOSE 8080

# 애플리케이션 실행
CMD [ "node", "app.js" ]