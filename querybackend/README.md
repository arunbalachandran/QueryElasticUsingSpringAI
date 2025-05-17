# Query Elasticsearch using Spring AI

This project demonstrates how to use Spring AI to convert natural language queries into Elasticsearch queries, with data flowing from PostgreSQL to Elasticsearch via Kafka Connect.

## Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 21
- OpenAI API Key

### Setup & Run

1. **Clone and Start Infrastructure**
   ```bash
   git clone <repository-url>
   cd querybackend
   docker-compose up -d postgres kafka zookeeper akhq elasticsearch kafka-connect
   ```

2. **Configure Environment**
   ```bash
   export OPENAI_API_KEY=your-api-key-here
   # or if using Intellij you can set it in your build configurations
   ```

3. **Initialize Elasticsearch (if you haven't already)**
   ```bash
   # Create the Order index with the required mapping:
   curl -X PUT "http://localhost:9200/order" \
   -H "Content-Type: application/json" \
   -d '{
     "mappings": {
       "properties": {
         "id": {
           "type": "keyword"
         },
         "productName": {
           "type": "text",
           "fields": {
             "keyword": {
               "type": "keyword",
               "ignore_above": 256
             }
           }
         },
         "productQty": {
           "type": "integer"
         },
         "productPrice": {
           "type": "double"
         },
         "productDescription": {
           "type": "text"
         },
         "createdTime": {
           "type": "date"
         },
         "updatedTime": {
           "type": "date"
         }
       }
     }
   }'

   # verify
   curl "http://localhost:9200/order/_mapping" -H "Content-Type: application/json"
   # returns mapping
   ```

4. **Start the Application**
   ```bash
   ./gradlew bootRun
   ```

## Data Flow Setup

### 1. Setup Kafka Connect
Initialize the PostgreSQL to Elasticsearch connectors:
```bash

curl -X POST http://localhost:8084/connectors \
-H "Content-Type: application/json" \
-d '{
  "name": "postgres-to-kafka-connector",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url": "jdbc:postgresql://localhost:5432/postgres",
    "topic": "orders",
    "table.whitelist": "public.",
    "key.converter": "org.apache.kafka.connect.json.JsonConverter",
    "value.converter": "org.apache.kafka.connect.json.JsonConverter",
    "schema.ignore": "true",
    "auto.create": "false",
    "auto.evolve": "false",
    "max.batch.size": "100",
    "poll.interval.ms": "1000"
  }
}'

curl -X POST http://localhost:8084/connectors \
-H "Content-Type: application/json" \
-d '{
  "name": "kafka-to-elastic",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "tasks.max": "1",
    "topics": "order",
    "connection.url": "http://elasticsearch:9200",
    "type.name": "_doc",
    "key.ignore": "true",
    "schema.ignore": "true"
  }
}'
```

## Testing the Setup
Use the following commands to verify that the setup is working. I've also added a postman collection that can be used for convenience.

### 1. Create Test Data
Add an order using the REST API:
```bash
curl -X POST http://localhost:8080/api/v1/orders \
-H "Content-Type: application/json" \
-d '{
  "productName": "Test Product",
  "productQty": 1,
  "productPrice": 99.99,
  "productDescription": "A test product"
}'

# Response
{
    "id": "ebc44c8d-c5e6-4873-9baa-c94e2f2d8a48",
    "productName": "Test Product",
    "productQty": 1,
    "productPrice": 99.99,
    "productDescription": "A test product",
    "createdTime": "2025-05-15T21:35:00.299667",
    "updatedTime": "2025-05-15T21:35:00.29975"
}

curl 'http://localhost:8080/api/v1/orders'

# Response
[
    {
        "id": "ebc44c8d-c5e6-4873-9baa-c94e2f2d8a48",
        "productName": "Test Product",
        "productQty": 1,
        "productPrice": 99.99,
        "productDescription": "A test product",
        "createdTime": "2025-05-15T21:35:00.299667",
        "updatedTime": "2025-05-15T21:35:00.29975"
    }
]
```

### 2. Verify Data Flow
- Check PostgreSQL: Use DBeaver or your favorite database tool to connect to `localhost:5432` (user: postgres, password: password)
- Check Elasticsearch: Use Kibana at `http://localhost:5601` to view the data

### 3. Test Natural Language Queries
Try the natural language query API:
```bash
curl -X POST http://localhost:8080/api/v1/elastic/query \
-H "Content-Type: application/json" \
-d '"Show me all orders with price greater than 50"'
```

## Troubleshooting

- **Elasticsearch Issues**: Check status at `http://localhost:9200`
- **Kafka Connect**: Monitor at `http://localhost:8084`
- **Kafka UI**: Access AKHQ at `http://localhost:8081`
- **Reset Elasticsearch Index**:
  ```bash
  curl -X DELETE "http://localhost:9200/order"
  ```

## Additional Resources
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/index.html)
- [Elasticsearch Documentation](https://www.elastic.co/guide/index.html)
- [Kafka Connect Documentation](https://docs.confluent.io/platform/current/connect/index.html)
- [Debezium Connector Reference](https://debezium.io/documentation/reference/stable/connectors/postgresql.html)
- [Elasticsearch Connector Reference](https://docs.confluent.io/kafka-connectors/elasticsearch/current/overview.html)