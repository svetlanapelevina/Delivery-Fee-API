curl -X POST --location "http://127.0.0.1:8080/delivery-fee" \
    -H "Content-Type: application/json" \
    -d '{
          "cart_value": 790,
          "delivery_distance": 2235,
          "number_of_items": 4,
          "time": "2024-01-15T13:00:00Z"
        }'