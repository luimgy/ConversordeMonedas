# Conversor de Monedas Interactivo Oracle Next Education

![Captura de pantalla de la aplicación](https://i.imgur.com/LTki2k0.png)

## 📄 Descripción

Este es un conversor de monedas de escritorio desarrollado en **Java**, como parte del Challenge de programación del programa **Oracle Next Education (ONE) en colaboración con Alura Latam**. La aplicación permite a los usuarios realizar conversiones de divisas en tiempo real, utilizando una interfaz gráfica amigable e intuitiva construida con Java Swing.

El sistema se conecta a la API [ExchangeRate-API](https://www.exchangerate-api.com/) para obtener las tasas de cambio más recientes y soporta más de 160 monedas de todo el mundo. Además, guarda un registro persistente de todas las conversiones en un archivo local.

---

## ✨ Características Principales

* **Interfaz Gráfica de Usuario (GUI):** Desarrollada con **Java Swing** para una experiencia de usuario sencilla y directa.
* **Conversión en Tiempo Real:** Utiliza una API para obtener tasas de cambio actualizadas al momento.
* **Menú Dinámico de Divisas:** La lista de monedas disponibles se carga directamente desde la API, asegurando que todas las divisas soportadas estén siempre disponibles.
* **Historial de Conversiones:**
    * Muestra un historial de las transacciones realizadas durante la sesión.
    * Guarda un registro persistente de todas las conversiones en un archivo `historial_conversiones.txt` para futuras referencias.
* **Validación de Entradas:** La interfaz maneja errores de entrada, como cantidades no numéricas, para evitar fallos en la aplicación.
* **API HTTP Moderna:** Utiliza el cliente `HttpClient` nativo de Java 11+ para una comunicación de red eficiente.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 11+
* **Interfaz Gráfica:** Java Swing
* **Gestión de Dependencias:** Maven
* **Librerías Externas:**
    * **Gson:** Para analizar eficientemente las respuestas JSON de la API.
* **API:** ExchangeRate-API para las tasas de cambio.
* **IDE de Desarrollo:** IntelliJ IDEA

---

## 🚀 Cómo Ejecutar el Proyecto

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/luimgy/ConversordeMonedas.git)
    ```
2.  **Abrir en tu IDE:** Abre el proyecto clonado en tu IDE de preferencia (se recomienda IntelliJ IDEA).

3.  **Instalar Dependencias:** El proyecto está configurado con Maven. El IDE debería detectar el archivo `pom.xml` y descargar la dependencia de Gson automáticamente.

4.  **Ejecutar:** Localiza la clase `Main.java` dentro de la ruta `src/main/java/` y ejecútala. Esto lanzará la interfaz gráfica de la aplicación.

---

## 👨‍💻 Autor

* **[Luis Miguel turizo jiemenez]**
