# department\_service Microservice

This repository contains the **department\_service** Spring Boot REST API microservice, with a fully automated GitHub Actions CI/CD pipeline supporting **dev**, **staging**, and **prod** environments.

---

## Branching Strategy (GitFlow)

* **main**: Production-ready code (deployed to **prod**)
* **staging**: Pre-production staging (deployed to **staging**)
* **develop**: Ongoing development (deployed to **dev**)
* **feature/**\*: Feature branches off `develop`
* **release/**\*: Release preparation off `develop`
* **hotfix/**\*: Production fixes off `main`

---

## Environments

Each branch triggers deployment to its corresponding AWS ECS cluster and service:

| Environment | Branch  | Cluster                      | Service                      | URL                        |
| ----------- | ------- | ---------------------------- | ---------------------------- | -------------------------- |
| Development | develop | `department-cluster-dev`     | `department-service-dev`     | `http://<dev-ip>:8080`     |
| Staging     | staging | `department-cluster-staging` | `department-service-staging` | `http://<staging-ip>:8080` |
| Production  | main    | `department-cluster-prod`    | `department-service-prod`    | `http://<prod-ip>:8080`    |

> Replace `<dev-ip>`, `<staging-ip>`, and `<prod-ip>` with the actual public IPs or ALB DNS names after deployment.

---

## CI/CD Pipeline

Defined in `.github/workflows/ci-cd.yml`, the pipeline runs on pushes and PRs to **develop**, **staging**, and **main**:

1. **Build & Test**

   * Checkout code
   * Set up JDK 21
   * Cache Maven dependencies
   * Run `mvn clean package`, `mvn test`, `mvn checkstyle:check`

2. **Docker Build & Push**

   * Configure AWS credentials
   * Login to Amazon ECR
   * Build Docker image
   * Tag and push both `${{ github.sha }}` and `latest` to `398453103114.dkr.ecr.us-east-1.amazonaws.com/department_service`

3. **Deploy to ECS**

   * Determine environment (`dev`, `staging`, `prod`) from the branch name
   * Update ECS service with `aws ecs update-service --force-new-deployment`

4. **Email Notifications**

   * Success and failure alerts sent via Gmail SMTP through GitHub Actions

---

## API Endpoints

Base URL: `http://<environment-url>:8080`

| Method | Path                | Description                   |
| ------ | ------------------- | ----------------------------- |
| GET    | `/departments`      | Retrieve all departments      |
| POST   | `/departments`      | Create a new department       |
| GET    | `/departments/{id}` | Retrieve a department by ID   |
| PUT    | `/departments/{id}` | Update an existing department |
| DELETE | `/departments/{id}` | Delete a department           |

### Swagger UI

Explore the interactive API docs:

```
http://<environment-url>:8080/swagger-ui.html
```

---

## Rollback Procedure

To roll back to the previous working version in any environment, run:

```bash
./rollback.sh <environment>
```

Example:

```bash
./rollback.sh dev
```

---

## Infrastructure-as-Code

All AWS resources (VPC, subnets, security groups, ECR, ECS clusters, task definitions, services) are defined in the `infra/` directory using Terraform.

To provision:

```bash
cd infra
terraform init
terraform apply
```

Adjust variables in `variables.tf` as needed for your account and environment.

---

*End of README*

