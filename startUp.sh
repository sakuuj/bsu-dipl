./gradlew clean
./gradlew bootJar
cp ./gradle.properties ./.env
docker compose build
docker compose up -d