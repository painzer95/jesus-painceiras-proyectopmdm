# CineApp - Gestión de Películas 🎬

Práctica de desarrollo Android utilizando Jetpack Compose (Material Design 3).

## 🚀 Prueba la aplicación
Puedes probar la aplicación directamente desde tu navegador sin necesidad de instalar nada gracias a Appetize.io:
👉 **[Enlace a la demo en Appetize.io](https://appetize.io/app/b_zzvc4dbkrv766tllpthvctocim)**

## 🛠️ Mejoras implementadas y Justificación Técnica

A continuación, se detallan las decisiones técnicas tomadas para cumplir y mejorar los requisitos de la entrega:

### 1. Migración e Integración de Material Design 3 (M3)
* **Coherencia Semántica:** Se corrigieron las tarjetas de la `ListaPeliculasScreen` modificando `CardDefaults.cardColors` para que utilicen la familia semántica `surface` y `onSurface` del `MaterialTheme.colorScheme`.
* **Componentes Modernos:** Se integró el botón de navegación hacia atrás directamente en el `TopAppBar` (nativamente en M3 mediante `navigationIcon`) y se incluyó el logo de la aplicación en el login utilizando la misma paleta M3 (`primary`).
* **Optimización de Iconos (Core vs Extended):** Para los iconos universales (Añadir, Borrar, Atrás) se utilizó la librería nativa de Compose (`Icons.Filled`). Sin embargo, para los iconos específicos (Logo, Email, Contraseña), se optó por descargarlos como **Vector Assets** (`res/drawable`) en formato XML. Esto evita importar la pesada dependencia `material-icons-extended`, reduciendo drásticamente el tamaño del APK y mejorando el tiempo de compilación.

### 2. Refactorización y Componentes Reutilizables
* Se creó el componente `CineInput`, que actúa como envoltorio de un `OutlinedTextField`.
* Esto ha permitido eliminar decenas de líneas redundantes en `LoginScreen`, `RegistroScreen` y `FormularioPeliculaScreen`. El componente soporta inyección dinámica de `label`, transformaciones visuales (para contraseñas), gestión de errores y `leadingIcon` opcional.

### 3. Modelo de Datos y UUID
* Se añadió un identificador único automático a los modelos `Pelicula` y `Usuario` utilizando `UUID.randomUUID().toString()`.
* Para adaptar la lógica existente sin romper la creación de objetos, se instanciaron los datos en el `MockData` utilizando parámetros nombrados explícitos de Kotlin (ej. `titulo = "..."`), permitiendo que el ID se genere implícitamente por defecto.
* Se actualizó la navegación (`AppNavigation`) y las rutas (`PantallaFormulario`) para transferir el ID de la película (String) en lugar de su índice (Int) en la lista, garantizando la persistencia e inmutabilidad de la referencia.

### 4. Lógica de Negocio y Validaciones
* Se implementó la función `.isBlank()` en los formularios de registro y login para evitar la creación de cuentas "vacías" o compuestas únicamente por espacios en blanco.
* Se añadió una validación estricta en la edición y creación de películas para asegurar que la calificación (nota) esté forzosamente contenida entre `0.0` y `10.0`.

### 5. Internacionalización y Multi-Preview
* **Cadenas de texto (Strings):** Todo el código fue limpiado de textos *hardcodeados*. Todos los textos (botones, descripciones, notificaciones del *SnackBar* y etiquetas dinámicas) han sido extraídos al archivo `res/values/strings.xml` e invocados mediante `stringResource()`, preparando la aplicación para traducciones.
* **Soporte multipantalla:** Se añadió la anotación `@androidx.compose.ui.tooling.preview.PreviewScreenSizes` a las pantallas principales para garantizar mediante *Preview* que la UI se adapta a diferentes densidades y formatos (móviles y tablets).

---
**Autor:** Jesús Painceiras  
**Versión:** 1.0