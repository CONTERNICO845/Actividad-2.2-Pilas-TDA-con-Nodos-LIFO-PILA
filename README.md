# Actividad-2.2-Pilas-TDA-con-Nodos-LIFO-PILA

## Actividad 2.2: Pilas — TDA con Nodos (LIFO)

Este repositorio contiene el código fuente de la **Práctica 2.2: Pilas TDA con Nodos LIFO** de la materia de **Estructura de Datos (2026B_IH060_ED)**. El proyecto implementa el Tipo de Dato Abstracto (TDA) Pila mediante una estructura dinámica de nodos enlazados bajo el principio **LIFO (Last In, First Out)**, integrado con una interfaz gráfica desarrollada en **Java Swing** para la administración de una pila de libros.

---

## 👥 Integrantes del Equipo 10 (Libros)

- Leyva Perez Victor Manuel
- Espinoza López Manuel Ángel
- Carmona Barbosa Geovani Gael

---


## 🏗️ Arquitectura

El proyecto sigue una arquitectura de **tres capas desacopladas**:

1. **Capa de Dominio** (`Libro.java`) — Modelo del objeto a almacenar, con atributos encapsulados y métodos de acceso.
2. **Capa del TDA** (`Nodo.java` + `Pila.java`) — Lógica LIFO pura, sin dependencias de la GUI.
3. **Capa de Vista** (`Ventana.java`) — Interfaz Swing que invoca las operaciones del TDA y refleja el estado de la pila en tiempo real.

---


### 1. Clonar el repositorio

```bash
git clone https://github.com/CONTERNICO845/Actividad-2.2-Pilas-TDA-con-Nodos-LIFO-PILA.git
```


## ✅ Validaciones implementadas

- **Pila vacía:** Intentar desapilar o consultar el tope cuando no hay elementos muestra un `JOptionPane` de error sin lanzar `NullPointerException`.
- **Campos vacíos:** El formulario valida que todos los campos estén llenos antes de crear un libro.
- **Año inválido:** El campo Año acepta únicamente números enteros positivos; cualquier otro valor genera un aviso con foco devuelto al campo.
- **Confirmación de vaciado:** Vaciar la pila solicita confirmación mediante un diálogo antes de ejecutar la operación.
