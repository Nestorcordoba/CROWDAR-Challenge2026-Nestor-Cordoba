# Framework de Automatización — SauceDemo

Pruebas automatizadas para https://www.saucedemo.com  
Challenge Automation 2026 — Néstor German Cordoba

---

## Tecnologías utilizadas

- Java 11
- Selenium WebDriver 4.18
- TestNG 7.9
- ExtentReports 5.1
- WebDriverManager 5.7 (descarga el driver automáticamente)
- Rest Assured 5.4 (pruebas de API)
- Maven (gestor de dependencias)

---

## Requisitos previos

1. Tener instalado **Java JDK 11 o superior**
   - Verificar con: `java -version`

2. Tener instalado **Maven 3.8 o superior** // yo instalé Maven apache-maven-4.0.0-rc-5
   - Verificar con: `mvn -version`


## Instalacióm de Maven por si no saben como hacerlo

Descargá Maven desde https://maven.apache.org/download.cgi
Extraé el zip en una carpeta, por ejemplo C:\apache-maven-4.0.0-rc-5
Agregá al PATH:

# En PowerShell como Administrador:
[Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\apache-maven-4.0.0-rc-5", "Machine")
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\apache-maven-4.0.0-rc-5\bin", "Machine")

## Instalación

1. Clonar o descomprimir el proyecto alojado en GitHub https://github.com/Nestorcordoba/CROWDAR-Challenge2026-Nestor-Cordoba
2. Abrir una terminal en la carpeta raíz del proyecto (donde está el `pom.xml`)
3. Descargar las dependencias:

```bash
mvn dependency:resolve
```

---

## Ejecución de las pruebas

### Ejecutar todas las pruebas
```bash
mvn test
```

### Ejecutar solo las pruebas de Login
```bash
mvn test -Dtest=LoginTest
```

### Ejecutar solo las pruebas del Carrito
```bash
mvn test -Dtest=CarritoTest
```

### Ejecutar solo la prueba de API (Mercado Libre)
```bash
mvn test -Dtest=MercadoLibreApiTest
```


