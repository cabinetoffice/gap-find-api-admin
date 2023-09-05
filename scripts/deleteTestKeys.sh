#!/bin/bash
USAGE_PLAN_ID="$1"


if [ $# -eq 0 ]; then
  echo "Error: No USAGE_PLAN_ID provided as an argument."
  exit 1
fi

# Get the list of API keys, from the usagePlan and extract ids
ids=($(aws apigateway get-usage-plan-keys --usage-plan-id $USAGE_PLAN_ID | jq -r '.items[] | .id'))
total_keys="${#ids[@]}"
current_key=0

sleep 10
echo "Found ${#ids[@]} keys"
echo keys ids: ${ids[@]}
# Iterate over the IDs and delete each API key
for id in "${ids[@]}"; do
  current_key=$((current_key + 1))
  echo deleting id ${id}
    aws apigateway delete-api-key --api-key "$id"
    keys_left=$((total_keys - current_key))
    echo "Keys left to delete: $keys_left"
done
echo finished