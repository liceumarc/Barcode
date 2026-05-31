# Librería de Codificación & Decodificación: Code 11 & Code 93

Este proyecto es una librería desarrollada en Java para la generación y lectura de códigos de barras bajo los estándares **Code 11** y **Code 93**. Cuenta con un motor de escaneo avanzado capaz de leer imágenes con ruido, manchas y rotaciones arbitrarias.

## 🛠 Codificación Code 11
Para la implementación de Code 11, se han seguido las siguientes reglas técnicas:

### 1. Composición del Símbolo
- **Start/Stop:** Patrón fijo `00110` (representado por el carácter `*`).
- **Datos:** Dígitos 0-9 y el carácter `-`.
- **Alternancia:** Los patrones siempre siguen el orden Barra-Espacio-Barra-Espacio-Barra.

### 2. Especificaciones de Dibujo
- **Grosor 0 (Estrecho):** 1 unidad.
- **Grosor 1 (Ancho):** 2 unidades (X-dimension).
- **Inter-character Gap:** 1 unidad de espacio estrecho entre caracteres.

### 📋 Diccionario de Patrones Code 11

| Carácter | Valor | Patrón (Bits) | Estructura Detallada |
| :--- | :---: | :---: | :--- |
| **0** | 0 | `00001` | Barra estrecha, Espacio estrecho, Barra estrecha, Espacio estrecho, **Barra ancha** |
| **1** | 1 | `10001` | **Barra ancha**, Espacio estrecho, Barra estrecha, Espacio estrecho, **Barra ancha** |
| **2** | 2 | `01001` | Barra estrecha, **Espacio ancho**, Barra estrecha, Espacio estrecho, **Barra ancha** |
| **3** | 3 | `11000` | **Barra ancha**, **Espacio ancho**, Barra estrecha, Espacio estrecho, Barra estrecha |
| **4** | 4 | `00101` | Barra estrecha, Espacio estrecho, **Barra ancha**, Espacio estrecho, **Barra ancha** |
| **5** | 5 | `10100` | **Barra ancha**, Espacio estrecho, **Barra ancha**, Espacio estrecho, Barra estrecha |
| **6** | 6 | `01100` | Barra estrecha, **Espacio ancho**, **Barra ancha**, Espacio estrecho, Barra estrecha |
| **7** | 7 | `00011` | Barra estrecha, Espacio estrecho, Barra estrecha, **Espacio ancho**, **Barra ancha** |
| **8** | 8 | `10010` | **Barra ancha**, Espacio estrecho, Barra estrecha, **Espacio ancho**, Barra estrecha |
| **9** | 9 | `10000` | **Barra ancha**, Espacio estrecho, Barra estrecha, Espacio estrecho, Barra estrecha |
| **-** | 10 | `00100` | Barra estrecha, Espacio estrecho, **Barra ancha**, Espacio estrecho, Barra estrecha |
| **Start/Stop** | - | `00110` | Barra estrecha, Espacio estrecho, **Barra ancha**, **Espacio ancho**, Barra estrecha |

---

## 🏗️ Decodificación Code 11 (Motor de Escaneo)

### 1. Extracción y Arquitectura Multidireccional
- **Matriz Binaria:** La imagen PPM se parsea a una matriz 2D de píxeles lógicos (`0` para barras, `1` para espacios en blanco).
- **Líneas de Escaneo (Scanlines):** En lugar de evaluar una única fila central, el motor "dispara" múltiples líneas de escaneo sobre la matriz en diferentes ángulos:
    - **Horizontales (0º):** Para códigos en posición estándar.
    - **Verticales (90º):** Para códigos rotados verticalmente.
    - **Diagonales (45º y 135º):** Para interceptar códigos inclinados evitando cortes prematuros.

### 2. Procesamiento de la Línea y Limpieza
- **Recorte de Márgenes (Quiet Zones):** Se identifican la primera y última transición a píxel negro, descartando los amplios márgenes blancos exteriores de la imagen.
- **Agrupación de Grosores:** Se cuentan los píxeles consecutivos del mismo color, generando un array con las anchuras físicas de todas las barras y espacios interceptados por la línea de escaneo.

### 3. Cálculo Dinámico del Umbral (Clustering)
Dado que las rotaciones y los artefactos de compresión deforman el grosor de los píxeles (efecto "dientes de sierra" o engrosamiento), el algoritmo calcula el umbral de decisión dinámicamente en cada línea basándose en percentiles estructurales del estándar Code 11:
- **Mediana (Percentil 50):** Como más del 60% de los elementos de un código de barras son estrechos, la mediana representa matemáticamente una **barra estrecha** 100% segura.
- **Percentil 85:** Se selecciona como representante de una **barra ancha**, esquivando tanto los elementos estrechos engordados por ruido (outliers bajos) como las manchas masivas de tinta (outliers altos).
- **Umbral:** Se establece en el punto medio exacto entre el valor del percentil 50 y el percentil 85.

### 4. Binarización y Búsqueda Deslizante
- **Conversión Lógica:** Cada anchura física se compara con el umbral. Si es `<=` al umbral, se convierte en un bit `0` (estrecho). Si es mayor, en un `1` (ancho).
- **Escaneo Bidireccional:** Para soportar imágenes rotadas 180º, se intenta la lectura tanto en el orden original de la cadena de bits como invirtiendo el string (`reverse()`).
- **Sliding Window:** El sistema busca el patrón de anclaje `*` (`00110`). Una vez encontrado, procesa la cadena en saltos de 6 bits (5 de datos + 1 gap ignorado) y traduce usando el diccionario. Si un patrón falla, se descarta ese fragmento pero el sistema sigue buscando en la línea.

### 5. Selección de la Lectura Óptima (Best Match)
El motor de escaneo no se detiene en la primera coincidencia que contenga el inicio y fin (`*...*`). Debido a que las manchas físicas pueden "comerse" partes del código generando falsos positivos más cortos, el algoritmo evalúa **todas las líneas de escaneo** y almacena en memoria la lectura más larga y consistente, retornando el resultado más fiable de toda la imagen.