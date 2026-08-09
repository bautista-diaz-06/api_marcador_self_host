# GraalVM Native Image - Guía de Configuración

## ¿Qué hicimos?

### 1. **pom.xml** - Agregamos:
   - `spring-boot-starter-aot`: Dependencia para AOT (Ahead-of-Time) compilation
   - `native-maven-plugin`: Plugin GraalVM para compilar a imagen nativa
   - `process-aot` execution: Genera hints de reflexión automáticamente

### 2. **Dockerfile.native** - Nuevo archivo:
   - **Etapa 1 (BUILD)**: 
     - Base: `ghcr.io/graalvm/native-image:21-muslib`
     - Tiene GraalVM Native Image tools preinstalados
     - Compila con `mvn native:compile`
   - **Etapa 2 (RUNTIME)**:
     - Base: `debian:bookworm-slim` (muy ligero)
     - Contiene solo el ejecutable nativo

## Ventajas de Imagen Nativa
| Aspecto | JVM estándar | GraalVM Native |
|--------|-------------|-----------------|
| Tiempo arranque | 3-5 segundos | <100ms |
| Uso memoria | 300-500MB | 30-50MB |
| Tamaño imagen Docker | ~400MB | ~50-100MB |
| Performance | Gradual (JIT) | Inmediato (compilado) |

## Compilar Imagen Nativa

### Opción 1: Con Docker (recomendado)
```bash
# Compilar imagen nativa
docker build -f Dockerfile.native -t marcador-mapa:native .

# Ejecutar
docker run -p 8080:8080 marcador-mapa:native
```

### Opción 2: Localmente (requiere GraalVM)
```bash
# Necesitas GraalVM JDK 21 + native-image
# Descargar: https://www.graalvm.org/downloads/

export JAVA_HOME=/path/to/graalvm-jdk-21
$JAVA_HOME/bin/native-image --version

# Compilar
mvn clean native:compile -DskipTests

# Ejecutar
./target/marcador-mapa
```

## Solucionar Problemas

### Problema: "Class not found" o "Method not found"
**Causa**: GraalVM necesita hints sobre reflexión
**Solución**: 
1. Crear `src/main/resources/META-INF/native-image/reflect-config.json`
2. Agregar clases que usan reflexión

Ejemplo:
```json
[
  {
    "name": "com.proyecto.model.Marker",
    "allDeclaredMethods": true,
    "allDeclaredFields": true
  }
]
```

### Problema: "Compilation took too long"
**Causa**: Primera compilación nativa es lenta (5-15 minutos)
**Solución**: Aumentar RAM/CPU, usar caché de Docker

### Problema: PostgreSQL connection fails
**Causa**: Driver no está soportado completamente
**Solución**: Ya está agregado en dependencies como runtime

## Verificar que funciona

```bash
# Compilar
docker build -f Dockerfile.native -t marcador-app:native .

# Ejecutar con variables de entorno
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/marcador \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  marcador-app:native

# Probar en otra terminal
curl http://localhost:8080/health
```

## Próximos pasos opcionales

1. **Optimizar reflection hints**: Si hay errores, agregar clases manualmente
2. **Builder hints**: Para clases con builders no estándar
3. **Serialization hints**: Para clases que se serializan
4. **Proxy hints**: Para interfaces dinámicas

Ver: https://www.graalvm.org/latest/reference-manual/native-image/metadata/
