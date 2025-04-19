#!/usr/bin/env bash
set -euo pipefail

IMAGE=kivislime/org/tennis-scoreboard:$(git rev-parse --short HEAD)

docker build -t "$IMAGE" .

if docker ps -a --format '{{.Names}}' | grep -q "^tennis-scoreboard\$"; then
  docker stop tennis-scoreboard
  docker rm tennis-scoreboard
fi

docker run -d \
  --name tennis-scoreboard \
  -p 8080:8080 \
  --restart unless-stopped \
  "$IMAGE"

echo "Deployed $IMAGE"
