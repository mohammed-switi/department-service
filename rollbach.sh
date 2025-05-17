#!/usr/bin/env bash
set -euo pipefail

# rollback.sh
#
# Usage:
#   ./rollback.sh <environment> [revision]
#
#  - environment: dev | staging | prod
#  - revision   : optional specific revision number (e.g. 3). If omitted, the script picks the previous revision.

# Configuration
REGION="us-east-1"
ACCOUNT_ID="398453103114"
FAMILY="department-task"

# Read args
ENV="$1"
REVISION_OVERRIDE="${2:-}"

CLUSTER="department-cluster-${ENV}"
SERVICE="department-service-${ENV}"

echo "🔄 Rolling back ECS service '$SERVICE' in cluster '$CLUSTER'..."

# 1) Determine the target task definition ARN
if [ -n "$REVISION_OVERRIDE" ]; then
  TARGET_TD_ARN="arn:aws:ecs:${REGION}:${ACCOUNT_ID}:task-definition/${FAMILY}:${REVISION_OVERRIDE}"
else
  # fetch the two most recent task definition ARNs
  ARNS=( $(aws ecs \
      list-task-definitions \
      --family-prefix $FAMILY \
      --sort DESC \
      --max-items 2 \
      --region $REGION \
      --query 'taskDefinitionArns[]' \
      --output text) )
  # ARNS[0] is current, ARNS[1] is previous
  TARGET_TD_ARN="${ARNS[1]}"
fi

if [ -z "$TARGET_TD_ARN" ]; then
  echo "❌ Could not determine a previous task definition. Specify a revision manually."
  exit 1
fi

echo "   • Rolling back to task definition: $TARGET_TD_ARN"

# 2) Update the service to the old revision
aws ecs update-service \
  --cluster "$CLUSTER" \
  --service "$SERVICE" \
  --task-definition "$TARGET_TD_ARN" \
  --force-new-deployment \
  --region "$REGION"

echo "✅ Rollback initiated successfully."

