# 🚀 Orders App -- CI/CD con GitHub Actions + Docker + AWS ECR

Este proyecto utiliza GitHub Actions, Docker y Amazon ECR para
automatizar el build, test y despliegue de una aplicación Java.

------------------------------------------------------------------------

## 📦 Arquitectura del flujo

El pipeline está dividido en dos workflows principales:

### 1. 🚀 Deploy to ECR

📄 `.github/workflows/deploy.yml`

Se ejecuta cuando hay un push a main o manualmente.

### 2. 🧪 CI de validación

📄 `.github/workflows/ci.yml`

Se ejecuta en Pull Requests hacia main.

------------------------------------------------------------------------

## 🚀 Workflow: Deploy to ECR

Compila la app, construye imagen Docker y la sube a AWS ECR.

### Trigger

on: push: branches: \["main"\] workflow_dispatch:

### Pasos

-   Checkout repo
-   Setup Java 21
-   mvn clean package -DskipTests
-   AWS OIDC login
-   Docker login ECR
-   docker build
-   tag + push a ECR

------------------------------------------------------------------------

## 🧪 Workflow: CI

Se ejecuta en PRs.

### Trigger

on: pull_request: branches: \["main"\]

### Pasos

-   Build Maven
-   Run tests
-   Build Docker multistage
-   Mostrar tamaños de imagen

------------------------------------------------------------------------

## 🐳 Dockerfile (Multistage)

### Build stage

FROM maven:3.9-eclipse-temurin-21 AS build WORKDIR /app COPY . . RUN mvn
clean package -DskipTests

### Runtime stage

FROM eclipse-temurin:21-jre WORKDIR /app COPY --from=build
/app/target/\*.jar app.jar ENTRYPOINT \["java","-jar","app.jar"\]

------------------------------------------------------------------------

## 🔐 Seguridad

-   AWS OIDC (sin secrets)
-   IAM Role: rnez-role
-   Deploy solo desde main

------------------------------------------------------------------------

## 📌 Flujo

PR → tests + docker build → validación\
main → build → docker → push ECR → deploy


