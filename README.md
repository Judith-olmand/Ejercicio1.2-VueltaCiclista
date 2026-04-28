# 🚴 Sistema de Gestión de Carreras Ciclistas

Sistema de gestión de etapas y participaciones para competiciones ciclistas con transacciones ACID, desarrollado en Java con Oracle Database.

## 📋 Descripción

Aplicación que permite registrar etapas de carreras ciclistas, gestionar la participación de ciclistas con posiciones aleatorias, asignar puntos automáticamente según la clasificación, y mostrar resúmenes detallados. Implementa control transaccional completo con commit/rollback.

## 🎯 Funcionalidades Principales

### Gestión de Etapas 🏁
- ✅ Crear nuevas etapas con validación de duplicados
- ✅ Registro de origen, destino, distancia y fecha
- ✅ Validación de datos de entrada
- ✅ Control de errores con rollback automático

### Gestión de Participaciones 🏆
- ✅ Asignación aleatoria de posiciones (sin repetir)
- ✅ Registro automático de todos los ciclistas
- ✅ Asignación de puntos según posición (top 5)
- ✅ Sistema de puntuación: 1º→100, 2º→90, 3º→80, 4º→70, 5º→60

### Control Transaccional 🔒
- ✅ Transacciones explícitas con `setAutoCommit(false)`
- ✅ Commit al completar todas las operaciones
- ✅ Rollback automático en caso de error
- ✅ Integridad de datos garantizada

## 🏗️ Arquitectura del Proyecto

```
Sistema de Gestión Ciclista/
│
├── org.example/
│   ├── Main.java                      # Punto de entrada con control transaccional
│   ├── DBConfig.java                  # Configuración de base de datos
│   │
│   ├── InsertarEtapa.java            # Creación de nuevas etapas
│   ├── InsertarParticipacion.java    # Registro de participaciones
│   ├── MostrarResumen.java           # Visualización de resultados
│   ├── ActualizarPuntos.java         # Actualización masiva de puntos
│   └── Comprobador.java              # Validaciones y verificaciones
│
└── pom.xml / build.gradle            # Dependencias del proyecto
```

## 🗄️ Modelo de Base de Datos

### Diagrama ER

```
┌─────────────────┐
│    CICLISTA     │
├─────────────────┤
│ ID_CICLISTA(PK) │
│ NOMBRE          │
│ APELLIDOS       │
│ NACIONALIDAD    │
│ FECHA_NACIMIENTO│
└────────┬────────┘
         │
         │ 1:N
         │
┌────────▼────────┐         ┌─────────────────┐
│ PARTICIPACION   │    N:1  │     ETAPA       │
├─────────────────┤◄────────├─────────────────┤
│ ID_CICLISTA(FK) │         │ NUMERO (PK)     │
│ NUMERO_ETAPA(FK)│         │ ORIGEN          │
│ POSICION        │         │ DESTINO         │
│ PUNTOS          │         │ DISTANCIA_KM    │
└─────────────────┘         │ FECHA           │
                            └─────────────────┘
```

### Script de Creación de Tablas

```sql
-- Tabla CICLISTA
CREATE TABLE CICLISTA (
    ID_CICLISTA NUMBER PRIMARY KEY,
    NOMBRE VARCHAR2(50) NOT NULL,
    APELLIDOS VARCHAR2(100),
    NACIONALIDAD VARCHAR2(50),
    FECHA_NACIMIENTO DATE
);

-- Tabla ETAPA
CREATE TABLE ETAPA (
    NUMERO NUMBER PRIMARY KEY,
    ORIGEN VARCHAR2(100) NOT NULL,
    DESTINO VARCHAR2(100) NOT NULL,
    DISTANCIA_KM NUMBER(6,2) NOT NULL,
    FECHA DATE NOT NULL
);

-- Tabla PARTICIPACION
CREATE TABLE PARTICIPACION (
    ID_CICLISTA NUMBER NOT NULL,
    NUMERO_ETAPA NUMBER NOT NULL,
    POSICION NUMBER NOT NULL,
    PUNTOS NUMBER DEFAULT 0,
    CONSTRAINT PK_PARTICIPACION PRIMARY KEY (ID_CICLISTA, NUMERO_ETAPA),
    CONSTRAINT FK_PART_CICLISTA FOREIGN KEY (ID_CICLISTA) 
        REFERENCES CICLISTA(ID_CICLISTA),
    CONSTRAINT FK_PART_ETAPA FOREIGN KEY (NUMERO_ETAPA) 
        REFERENCES ETAPA(NUMERO),
    CONSTRAINT CHK_POSICION CHECK (POSICION > 0)
);

-- Índices para mejorar el rendimiento
CREATE INDEX IDX_PART_CICLISTA ON PARTICIPACION(ID_CICLISTA);
CREATE INDEX IDX_PART_ETAPA ON PARTICIPACION(NUMERO_ETAPA);
CREATE INDEX IDX_PART_POSICION ON PARTICIPACION(POSICION);

-- Datos de ejemplo: Ciclistas
INSERT INTO CICLISTA VALUES (1, 'Tadej', 'Pogačar', 'Eslovenia', TO_DATE('1998-09-21', 'YYYY-MM-DD'));
INSERT INTO CICLISTA VALUES (2, 'Jonas', 'Vingegaard', 'Dinamarca', TO_DATE('1996-12-10', 'YYYY-MM-DD'));
INSERT INTO CICLISTA VALUES (3, 'Primož', 'Roglič', 'Eslovenia', TO_DATE('1989-10-29', 'YYYY-MM-DD'));
INSERT INTO CICLISTA VALUES (4, 'Remco', 'Evenepoel', 'Bélgica', TO_DATE('2000-01-25', 'YYYY-MM-DD'));
INSERT INTO CICLISTA VALUES (5, 'Egan', 'Bernal', 'Colombia', TO_DATE('1997-01-13', 'YYYY-MM-DD'));
INSERT INTO CICLISTA VALUES (6, 'Richard', 'Carapaz', 'Ecuador', TO_DATE('1993-05-29', 'YYYY-MM-DD'));
INSERT INTO CICLISTA VALUES (7, 'Enric', 'Mas', 'España', TO_DATE('1995-01-07', 'YYYY-MM-DD'));
INSERT INTO CICLISTA VALUES (8, 'Juan', 'Ayuso', 'España', TO_DATE('2002-09-16', 'YYYY-MM-DD'));
COMMIT;
```

## 🚀 Configuración y Ejecución

### Requisitos Previos

- ☕ **Java JDK 8 o superior**
- 🗄️ **Oracle Database** (11g o superior)
- 📦 **Maven** o **Gradle**
- 🔌 **Oracle JDBC Driver** (ojdbc8.jar)

### Configuración de la Base de Datos

#### DBConfig.java

```java
package org.example;

public class DBConfig {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "tu_usuario";
    private static final String PASSWORD = "tu_password";

    public static String getUrl() {
        return URL;
    }

    public static String getUser() {
        return USER;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
```

### Dependencias Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.oracle.database.jdbc</groupId>
        <artifactId>ojdbc8</artifactId>
        <version>21.9.0.0</version>
    </dependency>
</dependencies>
```

### Compilación y Ejecución

```bash
# Compilar
mvn clean compile

# Ejecutar
mvn exec:java -Dexec.mainClass="org.example.Main"
```

## 🎮 Uso del Sistema

### Flujo de Ejecución

```
1. Conexión a BD con autoCommit(false)
2. Solicitar datos de la nueva etapa
3. Insertar etapa en BD
4. Generar participaciones aleatorias para todos los ciclistas
5. Asignar puntos a los top 5
6. Mostrar resumen de la etapa
7. COMMIT si todo OK / ROLLBACK si hay error
```

### Ejemplo de Ejecución Exitosa

```
Introduce el número de etapa.
> 10

Número de etapa disponible

Introduce el origen.
> Madrid

Introduce el destino.
> Toledo

Introduce la distancia en km.
> 73.5

Introduce la fecha (YYYY-MM-DD).
> 2026-07-15

Etapa añadida correctamente.
Participaciones añadidas

ETAPA AÑADIDA: 
Nº etapa   Origen               Destino              Km    Fecha     
-----------------------------------------------------------------------------------------------
10         Madrid               Toledo               73    2026-07-15

--------------------------------------------------------------------------------
ID del Ciclista    Número de etapa    Posición     Puntos    
---------------------------------------------------------------------------
3                  10                 1            100       
7                  10                 2            90        
1                  10                 3            80        
5                  10                 4            70        
2                  10                 5            60        

Todo correcto. Haciendo commit.
```

### Ejemplo de Ejecución con Error

```
Introduce el número de etapa.
> 10

Número de etapa disponible

Introduce el origen.
> Barcelona

[Error en la inserción - por ejemplo, constraint violation]

Etapa cancelada por error. No se guardaron los datos.
```

## 🎯 Lógica de Asignación de Posiciones

### Algoritmo de Posiciones Aleatorias

```java
// Pseudo-código del proceso
Set<Integer> posicionesUsadas = new HashSet<>();

para cada ciclista:
    do {
        posicion = random.nextInt(totalCiclistas) + 1;
    } while (posicionesUsadas.contains(posicion));
    
    posicionesUsadas.add(posicion);
    
    if (posicion <= 5) {
        puntos = asignarPuntos(posicion);
        insertarParticipacion(ciclista, etapa, posicion, puntos);
    }
```

**Características:**
- ✅ Sin posiciones duplicadas (uso de `HashSet`)
- ✅ Solo se registran top 5 en la tabla PARTICIPACION
- ✅ Aleatorización completa en cada etapa
- ✅ Rango: 1 hasta número total de ciclistas

## 🏆 Sistema de Puntuación

| Posición | Puntos | Descripción |
|----------|--------|-------------|
| 🥇 1º    | 100    | Ganador de la etapa |
| 🥈 2º    | 90     | Segundo clasificado |
| 🥉 3º    | 80     | Tercer clasificado |
| 4º       | 70     | Cuarto clasificado |
| 5º       | 60     | Quinto clasificado |
| 6º+      | 0      | Sin puntos (no se registra) |

## 🔧 Componentes del Sistema

### 1. Main.java - Control Transaccional

```java
Características:
✅ setAutoCommit(false) - Transacciones manuales
✅ try-catch con rollback automático
✅ commit() solo si todo funciona
✅ try-with-resources para auto-close de conexión
```

**Flujo de Control:**
```
┌─────────────────┐
│  Conectar a BD  │
│ autoCommit=false│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  try {          │
│   InsertarEtapa │
│   Commit        │
│ }               │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  catch {        │
│   Rollback      │
│   Mensaje error │
│ }               │
└─────────────────┘
```

### 2. InsertarEtapa.java - Creación de Etapas

**Validaciones:**
- ❌ Número de etapa duplicado
- ✅ Formato de fecha (YYYY-MM-DD)
- ✅ Distancia numérica

**Proceso:**
1. Verificar que el número de etapa no exista
2. Solicitar datos: origen, destino, km, fecha
3. Insertar en tabla ETAPA
4. Llamar a InsertarParticipacion
5. Llamar a MostrarResumen

### 3. InsertarParticipacion.java - Participaciones

**Características:**
- 🎲 Generación aleatoria de posiciones
- 🚫 Sin duplicados (HashSet)
- 📊 Solo registra top 5
- 💯 Asignación automática de puntos

**Algoritmo:**
```java
// Para cada ciclista en la BD
for (Ciclista c : todosLosCiclistas) {
    posicion = aleatorioSinRepetir();
    
    if (posicion <= 5) {
        puntos = calcularPuntos(posicion);
        INSERT INTO PARTICIPACION VALUES (...);
    }
}
```

### 4. MostrarResumen.java - Visualización

**Muestra:**
1. Datos de la etapa creada
2. Top 5 de la clasificación con puntos
3. Formato tabular profesional

### 5. Comprobador.java - Validaciones

**Métodos:**
- `numeroEtapa()`: Verifica si existe un número de etapa
- `numeroCiclistas()`: Cuenta total de ciclistas en BD

### 6. ActualizarPuntos.java - Actualización Masiva

**Uso:** Para recalcular puntos de todas las participaciones

```sql
UPDATE PARTICIPACION 
SET puntos = CASE posicion 
    WHEN 1 THEN 100 
    WHEN 2 THEN 90 
    WHEN 3 THEN 80 
    WHEN 4 THEN 70 
    WHEN 5 THEN 60 
    ELSE 0 
END
```

## 📊 Ejemplos de Consultas Útiles

### Ver Clasificación General (suma de puntos)

```sql
SELECT 
    c.NOMBRE || ' ' || c.APELLIDOS AS CICLISTA,
    c.NACIONALIDAD,
    SUM(p.PUNTOS) AS TOTAL_PUNTOS,
    COUNT(p.NUMERO_ETAPA) AS ETAPAS_TOP5
FROM CICLISTA c
LEFT JOIN PARTICIPACION p ON c.ID_CICLISTA = p.ID_CICLISTA
GROUP BY c.ID_CICLISTA, c.NOMBRE, c.APELLIDOS, c.NACIONALIDAD
ORDER BY TOTAL_PUNTOS DESC NULLS LAST;
```

### Ver Etapas Ganadas por Ciclista

```sql
SELECT 
    c.NOMBRE || ' ' || c.APELLIDOS AS CICLISTA,
    COUNT(*) AS VICTORIAS
FROM PARTICIPACION p
JOIN CICLISTA c ON p.ID_CICLISTA = c.ID_CICLISTA
WHERE p.POSICION = 1
GROUP BY c.ID_CICLISTA, c.NOMBRE, c.APELLIDOS
ORDER BY VICTORIAS DESC;
```

### Ver Histórico de una Etapa

```sql
SELECT 
    e.NUMERO,
    e.ORIGEN || ' - ' || e.DESTINO AS RECORRIDO,
    e.DISTANCIA_KM || ' km' AS DISTANCIA,
    TO_CHAR(e.FECHA, 'DD/MM/YYYY') AS FECHA,
    c.NOMBRE || ' ' || c.APELLIDOS AS GANADOR
FROM ETAPA e
JOIN PARTICIPACION p ON e.NUMERO = p.NUMERO_ETAPA
JOIN CICLISTA c ON p.ID_CICLISTA = c.ID_CICLISTA
WHERE p.POSICION = 1
ORDER BY e.NUMERO;
```

## 🔒 Control Transaccional

### ¿Por qué Transacciones?

Una etapa implica múltiples operaciones:
1. INSERT en ETAPA (1 fila)
2. INSERT en PARTICIPACION (5+ filas)

**Sin transacciones:**
- ❌ Si falla la participación 3, quedan 2 huérfanas
- ❌ Datos inconsistentes en la BD
- ❌ Etapa "a medias"

**Con transacciones:**
- ✅ Todo o nada (atomicidad)
- ✅ Si falla algo, se deshace todo (rollback)
- ✅ Solo commit si todas las operaciones funcionan

### Propiedades ACID Implementadas

- **A**tomicidad: Todo o nada - rollback si hay error
- **C**onsistencia: Constraints y validaciones
- **I**solation: Nivel READ COMMITTED (default Oracle)
- **D**urabilidad: COMMIT persiste en disco

## ⚠️ Manejo de Errores

### Errores Comunes

#### 1. Etapa Duplicada
```
El número de etapa ya existe
```
**Solución:** Usar un número diferente

#### 2. Error de Fecha
```
DateTimeParseException
```
**Solución:** Usar formato YYYY-MM-DD (ej: 2026-07-15)

#### 3. Sin Ciclistas en BD
```
Error al obtener el id del ciclista
```
**Solución:** Ejecutar script de INSERT de ciclistas

#### 4. Error de Conexión
```
Error al conectar: Listener refused the connection
```
**Solución:** 
- Verificar que Oracle esté corriendo
- Comprobar credenciales en DBConfig

## 🎓 Conceptos Aplicados

### Transacciones JDBC

```java
// Desactivar auto-commit
conn.setAutoCommit(false);

try {
    // Operaciones múltiples
    operacion1();
    operacion2();
    operacion3();
    
    // Todo OK → guardar
    conn.commit();
    
} catch (Exception e) {
    // Algo falló → deshacer
    conn.rollback();
}
```

### Try-with-Resources

```java
// Auto-close de recursos
try (Connection conn = DriverManager.getConnection(...)) {
    // Uso de la conexión
} // conn.close() automático
```

### PreparedStatement vs Statement

| Aspecto | PreparedStatement | Statement |
|---------|------------------|-----------|
| SQL Injection | ✅ Protegido | ❌ Vulnerable |
| Performance | ✅ Compilado una vez | ❌ Compila cada vez |
| Parámetros | ✅ Con placeholders | ❌ Concatenación |
| Uso | ✅ Cuando hay variables | ⚠️ Solo consultas fijas |

### Uso de Set para Evitar Duplicados

```java
Set<Integer> posicionesUsadas = new HashSet<>();

// HashSet garantiza unicidad
posicionesUsadas.add(3);
posicionesUsadas.add(3); // No se añade de nuevo
posicionesUsadas.contains(3); // true
```

## 📈 Mejoras Futuras

- [ ] Interfaz gráfica (JavaFX)
- [ ] Importar etapas desde CSV
- [ ] Estadísticas avanzadas (media km, etapas ganadas)
- [ ] Sistema de equipos ciclistas
- [ ] Clasificación por equipos
- [ ] Maillots especiales (montaña, regularidad)
- [ ] Histórico de ediciones
- [ ] API REST para consultas
- [ ] Exportar resultados a PDF
- [ ] Dashboard con gráficos

## 🎯 Ejercicios Propuestos

1. **Básico**: Añadir método para listar todas las etapas
2. **Intermedio**: Implementar búsqueda de ciclistas por nacionalidad
3. **Avanzado**: Crear sistema de clasificación general acumulada
4. **Experto**: Implementar sistema de equipos con clasificación conjunta

## 🐛 Troubleshooting

### No se registran participaciones
**Causa:** No hay ciclistas en la tabla CICLISTA  
**Solución:** Ejecutar INSERT de ciclistas de ejemplo

### Todas las posiciones son las mismas
**Causa:** Error en el algoritmo de aleatorización  
**Solución:** Verificar que el Set se está usando correctamente

### Error de commit
**Causa:** Violación de constraint o error SQL  
**Solución:** Revisar logs, verificar estructura de tablas

## 📚 Recursos Adicionales

- [Oracle JDBC Transaction Management](https://docs.oracle.com/en/database/oracle/oracle-database/21/jjdbc/transaction-management.html)
- [Java Random Class](https://docs.oracle.com/javase/8/docs/api/java/util/Random.html)
- [PreparedStatement Best Practices](https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html)

## 💡 Notas del Desarrollador

### Decisiones de Diseño

1. **Solo registrar top 5**: Reduce tamaño de tabla PARTICIPACION
   - Alternativa: Registrar todos con puntos=0 para 6º+

2. **Asignación aleatoria**: Simula variabilidad de carreras reales
   - Alternativa: Permitir entrada manual de posiciones

3. **Transacciones explícitas**: Garantiza integridad
   - Ventaja: Control total sobre commit/rollback
   - Desventaja: Requiere manejo manual

4. **Switch para puntos**: Legible y mantenible
   - Alternativa: Fórmula matemática o consulta UPDATE

## 📄 Licencia

Proyecto educativo para aprendizaje de transacciones JDBC y gestión de datos relacionales.

## 👤 Autor

Sistema de gestión desarrollado como proyecto de aprendizaje de transacciones ACID, JDBC y bases de datos Oracle.

---

## 🚴 ¡Gestiona tus Carreras Ciclistas Profesionalmente!

Un sistema completo con control transaccional para organizar etapas y clasificaciones de manera robusta y confiable. 🏁🏆
