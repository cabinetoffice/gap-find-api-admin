#!/bin/bash

# Get the list of API keys, filter by names starting with "Org", and extract IDs
ids=($(aws apigateway get-api-keys | jq -r '.items[] | select(.name | contains("Cypress")) | .id'))
sleep 10
echo "Found ${#ids[@]} keys"
echo keys ids: ${ids[@]}
# Iterate over the IDs and delete each API key
for id in "${ids[@]}"; do
echo deleting id ${id}
    aws apigateway delete-api-key --api-key "$id"
done
echo finished