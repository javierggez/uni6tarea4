# Gestión de Usuarios Latino (uni6tarea4)

Una aplicación Android moderna que funciona como un directorio de contactos, integrando persistencia local, consumo de APIs REST y una arquitectura robusta basada en las mejores prácticas de Google.

## 🚀 Características Principales

- **Arquitectura MVVM:** Separación clara de responsabilidades entre la UI, los modelos de datos y la lógica de negocio.
- **Transformación de Datos Local:** Implementa una lógica personalizada en el `UserRepository` que intercepta los nombres en inglés de la API y los reemplaza por **nombres latinos** (Juan Pérez, María García, etc.) para una experiencia de usuario localizada.
- **Persistencia con Room:** Los datos se guardan localmente para permitir el acceso sin conexión (Offline-first).
- **Consumo de API con Retrofit:** Integración con JSONPlaceholder para obtener datos de usuarios en tiempo real.
- **Diseño Material Design:** Uso de `CardView`, `RecyclerView`, `FloatingActionButton` y temas de Material Components.
- **Formulario de Contacto Validado:** Incluye validaciones de campos obligatorios y formato de correo electrónico mediante expresiones regulares.
- **Firebase Integrado:** Configuración lista para el reporte de errores con **Crashlytics** y seguimiento de eventos con **Analytics**.
- **Navegación Avanzada:** Uso de Actividades y Fragmentos para mostrar detalles profundos del usuario.

## 🛠️ Stack Tecnológico

- **Lenguaje:** [Kotlin](https://kotlinlang.org/)
- **UI:** XML con [View Binding](https://developer.android.com/topic/libraries/view-binding)
- **Base de Datos:** [Room](https://developer.android.com/training/data-storage/room)
- **Networking:** [Retrofit 2](https://square.github.io/retrofit/) + [Gson](https://github.com/google/gson)
- **Concurrencia:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- **Componentes de Arquitectura:** ViewModel, LiveData, Lifecycle.
- **Servicios:** Firebase (BOM), Crashlytics, Analytics.
- **Testing:** JUnit 4, Mockito, Espresso.

## 📦 Instalación y Configuración

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/TU_USUARIO/uni6tarea4.git
   ```

2. **Firebase:**
   - Registra el paquete `com.example.uni6tarea4` en tu consola de Firebase.
   - Descarga el archivo `google-services.json` y colócalo en la carpeta `/app`.

3. **Compilación:**
   - Abre el proyecto en **Android Studio**.
   - Sincroniza los archivos de Gradle.
   - Ejecuta la aplicación en un emulador o dispositivo físico.

## 📸 Estructura del Proyecto

- `MainActivity`: Lista principal de usuarios con RecyclerView.
- `DetailActivity`: Contenedor para el detalle del usuario.
- `UserDetailFragment`: Fragmento encargado de renderizar la información específica del contacto.
- `FormActivity`: Pantalla de contacto con validación de entradas.
- `UserRepository`: Lógica de sincronización API-Base de Datos y mapeo de nombres.

---
Desarrollado como parte de la práctica de gestión de datos y arquitecturas modernas en Android.
