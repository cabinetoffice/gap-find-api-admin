#!/bin/bash

# Get the list of API keys, filter by names starting with "Org", and extract IDs
ids=($(aws apigateway get-api-keys | jq -r '.items[] | select(.name | contains("Cypress")) | .id'))
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