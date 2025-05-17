#!/usr/bin/env bash
set -euo pipefail

# Usage: ./rollback.sh <environment> [revision]
#   environment: dev | staging | prod
#   revision:    optional specific revision number (e.g. 3)

REGION="us-east-1"
ACCOUNT_ID="398453103114"
FAMILY="department-task"
ENV="$1"
REV_OVERRIDE="${2:-}"

CLUSTER="department-cluster-${ENV}"
SERVICE="department-task-service-${ENV}"

echo "🔄 Rolling back '$SERVICE' in '$CLUSTER'..."

if [ -n "$REV_OVERRIDE" ]; then
  TARGET_ARN="arn:aws:ecs:${REGION}:${ACCOUNT_ID}:task-definition/${FAMILY}:${REV_OVERRIDE}"
else
  ARNS=( $(aws ecs list-task-definitions \
    --family-prefix $FAMILY \
    --sort DESC \
    --max-items 2 \
    --region $REGION \
    --query 'taskDefinitionArns[]' \
    --output text) )
    TARGET_ARN="${ARNS[1]}"
fi

if [ -z "$TARGET_ARN" ]; then
  echo "❌ No previous task definition found. Specify a revision."
  exit 1
fi

echo "   • Using task definition: $TARGET_ARN"
aws ecs update-service \
  --cluster "$CLUSTER" \
  --service "$SERVICE" \
  --task-definition "$TARGET_ARN" \
  --force-new-deployment \
  --region "$REGION"

echo "✅ Rollback initiated."
