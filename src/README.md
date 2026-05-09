# Librería de Codificación: Code 11 & Code 93

Este proyecto es una librería desarrollada en Java para la generación y lectura de códigos de barras bajo los estándares **Code 11** y **Code 93**.

## 🛠 Estructura de Code 11
Para la implementación de Code 11, se han seguido las siguientes reglas técnicas:

### 1. Composición del Símbolo
- **Start/Stop:** Patrón fijo `00110`.
- **Datos:** Dígitos 0-9 y el carácter `-`.
- **Alternancia:** Los patrones siempre siguen el orden Barra-Espacio-Barra-Espacio-Barra.

### 2. Algoritmo de Checksum (Módulo 11)
- **C-Check:** Obligatorio para todos los mensajes.
- **K-Check:** Obligatorio si el mensaje tiene 10 o más caracteres.
- **Pesos:**
    - Para C: 1 a 10.
    - Para K: 1 a 9.

### 3. Especificaciones de Dibujo
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
**Nota técnica para la implementación:**
1. Los bits `0` representan ancho **estrecho** (1 unidad).
2. Los bits `1` representan ancho **ancho** (2 unidades).
3. Cada patrón debe ir seguido de un **espacio estrecho (gap)** de 1 unidad que no está incluido en la tabla.