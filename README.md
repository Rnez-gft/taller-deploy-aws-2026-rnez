# 🚀 Orders App — CI/CD con GitHub Actions, Docker, AWS ECR y ECS

Este proyecto implementa un pipeline CI/CD completo usando GitHub Actions, Docker, Amazon ECR y Amazon ECS para automatizar el build, test y despliegue de una aplicación Java.

---

## 📦 Arquitectura del flujo

El sistema se compone de **3 workflows principales**:

### 1. 🐳 Deploy a ECR

* Compila la aplicación
* Construye imagen Docker
* Sube la imagen a Amazon ECR

### 2. 🧪 CI de validación (Docker Comparison CI)

* Se ejecuta en Pull Requests
* Ejecuta tests y build
* Genera imagen Docker multistage
* Muestra comparación de tamaños

### 3. 🚀 Deploy a ECS

* Actualiza task definition
* Reemplaza imagen en ECS
* Despliega nueva revisión del servicio

---

# 🚀 Workflow: Deploy to ECR

📄 `.github/workflows/deploy_image.yml`

Se ejecuta en push a `main` o manualmente.

## 🔐 Permisos

* AWS OIDC (sin secrets)
* IAM Role: `taller-deploy-aws-2026-rol-<correoAbreviado>`

## ⚙️ Variables

```yaml
AWS_REGION: eu-west-1
ECR_REPOSITORY: taller-deploy-aws-2026-repository-<correoAbreviado>
ACCOUNT_ID: 822414985516
```

## 🧩 Pasos del workflow

1. Checkout repository
2. Setup Java 21
3. Build JAR con Maven
4. Configurar credenciales AWS (OIDC)
5. Login en Amazon ECR
6. Build Docker image
7. Tag image
8. Push a ECR
9. Debug de imágenes

---

# 🧪 Workflow: Docker Comparison CI

📄 `.github/workflows/build.yml`

Se ejecuta en Pull Requests hacia `main`.

## ⚙️ Pasos

1. Checkout repo
2. Setup Java 21
3. Cache Maven dependencies
4. Build aplicación (JAR)
5. Ejecutar tests
6. Build Docker multistage image
7. Mostrar tamaños de imagen
8. Resumen de imágenes

---

# 🚀 Workflow: Deploy to ECS

📄 `.github/workflows/deploy_ECS.yml`

Se ejecuta manualmente (`workflow_dispatch`).

## ⚙️ Variables

```yaml
AWS_REGION: eu-west-1
ACCOUNT_ID: 822414985516
ECR_REPOSITORY: taller-deploy-aws-2026-repository-<correoAbreviado>
ECS_CLUSTER: taller-deploy-aws-2026-cluster-<correoAbreviado>
ECS_SERVICE: Modificar por el nombre del servicio en ECS
TASK_DEFINITION: taller-deploy-aws-2026-task-<correoAbreviado>
```

## 🧩 Flujo de despliegue

### 1. Checkout repo

### 2. 🔐 AWS OIDC

Autenticación sin secrets:

```yaml
role-to-assume: arn:aws:iam::822414985516:role/taller-deploy-aws-2026-rol-<correoAbreviado>
```

### 3. 📄 Obtener task definition actual

```bash
aws ecs describe-task-definition
```

### 4. 🧹 Limpiar JSON

Se eliminan campos no válidos:

* taskDefinitionArn
* revision
* status
* requiresAttributes
* compatibilities
* registeredAt
* registeredBy

### 5. ✏️ Actualizar imagen Docker

Se reemplaza la imagen por:

```
ACCOUNT_ID.dkr.ecr.AWS_REGION.amazonaws.com/ECR_REPOSITORY:latest
```

### 6. 🚀 Registrar nueva task definition

```bash
aws ecs register-task-definition
```

### 7. 🔄 Update ECS service

```bash
aws ecs update-service
--force-new-deployment
```

---

# 🐳 Dockerfile (Multistage)

## 🏗️ Build stage

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
```

## 🚀 Runtime stage

```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
```

---

# 🔐 Seguridad

* AWS OIDC (sin secrets en GitHub)
* IAM Role dedicado
* Deploy controlado por rama `main`
* ECS deployment con nueva task revision

---

# 📌 Flujo completo

### Pull Request

```
PR → CI tests + Docker build → validación
```

### Main branch

```
Push → build → Docker → push ECR
```

### Deploy ECS

```
Manual → update task definition → new revision → deploy service
```

---

# 📊 Resumen

✔ CI automático en PRs
✔ Build y push a ECR en main
✔ Deploy controlado a ECS
✔ OIDC sin secretos
✔ Docker multistage optimizado
