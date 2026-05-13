# Librería de Codificación & Decodificación: Code 11 & Code 93

Este proyecto es una librería desarrollada en Java para la generación y lectura de códigos de barras bajo los estándares **Code 11** y **Code 93**.

## 🛠 Codificación Code 11 
Para la implementación de Code 11, se han seguido las siguientes reglas técnicas:

### 1. Composición del Símbolo
- **Start/Stop:** Patrón fijo `00110`.
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

## 🏗️ Decodificación Code 11 

### Implementación de la decodificación

### 1. Limpieza y Normalización
- **Trim Inicial:** Se eliminan los espacios en blanco laterales para evitar errores en el cálculo del ancho base.
- **Detección de Caracteres Inválidos:** Si la cadena contiene caracteres diferentes a `█` o ` `, la decodificación se interrumpe retornando `null`.

### 2. Cálculo del Ancho Base y Umbral
- **Ancho Base:** Se mide la longitud del primer bloque de barras (`█`). Al ser el inicio del patrón `*` (`00110`), este primer bloque representa obligatoriamente una barra estrecha (bit `0`).
- **Umbral de Decisión:** Se establece un margen de x1.5 sobre el ancho base para clasificar los bits:
    - **Bit 0 (Estrecho):** Bloque $\leq$ $(anchoBase \times 1.5)$.
    - **Bit 1 (Ancho):** Bloque $>$ $(anchoBase \times 1.5)$.

### 3. Procesamiento de la Secuencia de Bits
- **Conversión:** Se recorre la imagen de barras y espacios, transformando cada bloque físico en un bit lógico (`0` o `1`) según el umbral calculado.
- **Segmentación:** La cadena de bits resultante se divide en grupos de 6 elementos:
    - **Bits 1-5:** Representan el valor del carácter según el diccionario.
    - **Bit 6 (Gap):** Espacio de separación inter-característico que se ignora durante la traducción.

### 4. Validación y Salida
- **Traducción:** Se busca la coincidencia de cada grupo de 5 bits en el diccionario. Si un patrón de bits no existe, se considera una lectura fallida.
- **Integridad:** El mensaje final solo es válido si comienza y finaliza con el carácter delimitador `*`.
- **Retorno:** Si todas las validaciones son correctas, se devuelve la cadena de texto (incluyendo los delimitadores); de lo contrario, se devuelve `null`.

