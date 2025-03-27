# AWS Event Bridge Publisher
## Overview
AWS Event Bridge Publisher is a Spring Boot application that sends events to the EventBridge service in AWS.

## Features
- Sends events to AWS EventBridge service
- Handles exceptions and provides structured error responses

## Prerequisites
Ensure you have the following installed and configured:
- Java 17 or higher
- Maven 3.9.0 or higher
- Docker 25.0.8 or higher
- AWS CLI configured with your IAM credentials (for local testing)

## Installation & Setup
Clone the repository:
```commandline
git clone https://github.com/danijeldragicevic/aws-eventbridge-publisher.git 
cd aws-eventbridge-publisher
```
Build the project:
```commandline
mvn clean install
```
Run the application using Maven:
```commandline
mvn spring-boot:run -Dspring-boot.run.arguments="--AWS_REGION=your-region-here --AWS_EVENT_BUS_NAME=your-event-bus-name-here"
```
Run the application using Docker:
```commandline
docker build -t aws-eventbridge-publisher:latest .
docker run -d --name aws-publisher-app -p 8081:8081 \
                -e AWS_REGION=your-region-here \
                -e AWS_EVENT_BUS_NAME=your-event-bus-name-here \
                -v ~/.aws:/root/.aws:ro \
                aws-eventbridge-publisher:latest
```
The `-v ~/.aws:/root/.aws:ro` option mounts your local AWS credentials into the Docker container. This is necessary for local testing to allow the application to authenticate with AWS services using your local AWS credentials. <p>

When the application is deployed on an EC2 instance, this option is not needed because the EC2 instance can be configured with an IAM role that provides the necessary permissions to access AWS services. The application running on the EC2 instance will automatically use the credentials provided by the IAM role.

## GitHub Actions
This project uses GitHub Actions for continuous integration and deployment. The workflow is defined in `.github/workflows/deploy.yaml.`

Workflow Steps:
- Build Docker Image - The workflow builds the Docker image and saves it as an artifact.
- Push to ECR - The Docker image is pushed to Amazon ECR.
- Run on EC2 - The Docker container is pulled and run on an EC2 instance.

## API Usage
### Create order
Endpoint: `POST /orders` <br>
Example Request:
```commandline
curl --request POST \
  --url http://localhost:8081/orders \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/10.3.1' \
  --data '{
  "source": "com.productdock.orders",
  "detailType": "OrderPlaced",
  "orderId": "test12",
  "orderAmount": 112.1,
  "customer": {
    "address": "123 Main Street",
    "city": "AnyCity",
    "state": "WA",
    "countryCode": "US"
  }
}'
```
Example Response:
```commandline
{
	"message": "Order created successfully"
}
```
## Error Handling
The application provides meaningful error responses.
Example of a 404 Not Found response:
```commandline
{
	"timestamp": "2025-03-27T08:28:17.190+00:00",
	"status": 404,
	"error": "Not Found",
	"path": "/invalid-endpoint"
}
```
## Running Tests
The project includes JUnit tests for repositories, services, controllers, and exception handling.
Run tests with:
```commandline
mvn test
```
## Contributing
Contributions are welcome! Feel free to submit a pull request or open an issue.

## License
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
