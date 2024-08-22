echo "--------------- 서버 배포 시작 -----------------"
cd ~
cd hello-world-server
docker pull 533267276735.dkr.ecr.ap-northeast-2.amazonaws.com/hello-world/hello-world-server:latest
docker compose down || true
docker compose up -d --build
echo "--------------- 서버 배포 끝 -----------------"
