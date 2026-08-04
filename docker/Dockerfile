# =============================================================================
# Dockerfile multi-stage para ClinicaOdontologica
# =============================================================================
# Build: eclipse-temurin:25-jdk + Maven
# Run:   eclipse-temurin:25-jre (imagen minima)
# =============================================================================

# ---- Stage 1: Build ----
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copiar solo pom.xml primero (capa cacheable)
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copiar fuente y construir
COPY src src
RUN mvn package -DskipTests -B

# ---- Stage 2: Run ----
FROM eclipse-temurin:25-jre AS runner
WORKDIR /app

RUN groupadd -r app && useradd -r -g app app

COPY --from=builder /app/target/*.jar app.jar

USER app
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
