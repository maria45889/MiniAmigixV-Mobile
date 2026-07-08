<div align="center">

# 🚀 MiniAmigixV Mobile

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5-blue.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active-success.svg)]()

**Aplicación móvil Android moderna que replica la funcionalidad completa de MiniAmigixV Web con diseño glassmorphism y múltiples herramientas inteligentes integradas.**

[Demo](#) • [Reportar Bug](#) • [Solicitar Feature](#)

</div>

---

## 📖 Sobre MiniAmigixV Mobile

MiniAmigixV Mobile es la versión nativa Android de la plataforma web MiniAmigixV, desarrollada con **Kotlin** y **Jetpack Compose**. Ofrece una experiencia visual excepcional con diseño **glassmorphism**, interfaz completamente responsiva y todas las funcionalidades potenciadas por Inteligencia Artificial de la versión web.

### 🎯 Visión

Proporcionar la misma experiencia completa de MiniAmigixV en dispositivos móviles, permitiendo a los usuarios gestionar su productividad, entretenimiento y aprendizaje desde cualquier lugar con una interfaz nativa optimizada.

---

## ✨ Características Principales

### 🤖 Chat IA
- Asistente inteligente integrado con múltiples proveedores (OpenAI, Groq, Ollama)
- Respuestas en tiempo real con contexto personalizado
- Interfaz moderna tipo chat con historial de conversaciones
- Soporte para imágenes y análisis visual
- Respuestas empáticas y sentimentales

### 🎵 Música
- Reproductor musical interactivo con YouTube
- Sistema de playlists personalizadas
- Favoritos y gestión de biblioteca
- Búsqueda de letras y sincronización
- Estadísticas de reproducción

### 🎮 Juegos
- Módulo de entretenimiento con minijuegos educativos
- Sistema de puntuaciones y logros
- Interfaz dinámica y visual
- Desafíos diarios y rankings

### 🌦️ Clima
- Consulta del clima en tiempo real
- Información de temperatura, humedad y condiciones meteorológicas
- Diseño con animaciones fluidas
- Geolocalización automática
- Pronóstico extendido

### 🌍 Traductor
- Traducción entre múltiples idiomas
- Interfaz rápida y sencilla
- Detección automática de idioma
- Historial de traducciones

### 🎬 Entretenimiento
- Sección de contenido multimedia
- Tendencias y categorías interactivas
- Recomendaciones personalizadas
- Películas, series, anime y más

### 📝 Blog / Noticias Globales
- Publicaciones personales de usuarios
- Noticias oficiales creadas por administradores
- Sistema de anuncios globales
- Publicaciones fijadas y destacadas
- Categorías y comentarios

### 📅 Eventos
- Gestión y visualización de eventos
- Organización de actividades
- Sistema de recordatorios automáticos
- Calendario interactivo
- Sincronización con notificaciones
- Categorías con emojis (Evento, Tarea, Cumpleaños, Reunión, Recordatorio)
- Validación de fechas futuras

### 🔔 Notificaciones
- Centro de notificaciones moderno con diseño tipo app
- Agrupación por fecha (Hoy, Ayer, Esta semana, Este mes)
- Sección de notificaciones destacadas para prioridad alta
- Buscador en tiempo real
- Filtros por categoría (Chat IA, Música, Estudio, Eventos, Soporte, Sistema)
- Acciones rápidas: marcar leída, fijar, eliminar
- Estadísticas visuales por categoría
- Colores e iconos específicos por tipo de notificación
- Prioridades: Alta (🔥), Normal, Baja (📌)
- Sistema de fijación para notificaciones importantes

### 👤 Perfil de Usuario
- Gestión de perfil completo
- Personalización de cuenta
- Configuración de tema (claro/oscuro)
- Foto de perfil
- Estadísticas de uso

### 🛠 Soporte Técnico
- Sistema de tickets avanzado
- Comunicación entre usuarios y administradores
- Seguimiento de incidencias
- Respuesta por correo electrónico
- Categorías: Consulta general, Reportar error, Problema con cuenta, Sugerencia, Otro
- Prioridades automáticas según categoría
- Estadísticas de tiempos de respuesta

### 💡 Sugerencias
- Envío de sugerencias por parte de usuarios
- Revisión y respuesta administrativa
- Categorías: Mejora de funcionalidad, Reporte de error, Nueva característica, Diseño/UI, Otro
- Sistema de prioridades (Baja, Media, Alta)
- Estado de implementación

---

## 🏗 Tecnologías Utilizadas

### Mobile
<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple.svg)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5-blue.svg)
![Android](https://img.shields.io/badge/Android-14-green.svg)
![Material Design 3](https://img.shields.io/badge/Material%20Design-3-blue.svg)

</div>

- **Kotlin 1.9** - Lenguaje principal
- **Jetpack Compose 1.5** - UI declarativa moderna
- **Material Design 3** - Sistema de diseño
- **Android 14** - Versión objetivo
- **MVVM Architecture** - Patrón de arquitectura
- **ViewModel + LiveData** - Gestión de estado
- **Navigation Compose** - Navegación entre pantallas

### Backend Integration
<div align="center">

![Retrofit](https://img.shields.io/badge/Retrofit-2.9-green.svg)
![OkHttp](https://img.shields.io/badge/OkHttp-4.12-orange.svg)
![Firebase Auth](https://img.shields.io/badge/Firebase%20Auth-Latest-yellow.svg)

</div>

- **Retrofit 2.9** - Cliente HTTP
- **OkHttp 4.12** - Cliente HTTP subyacente
- **Firebase Auth** - Autenticación
- **Google Sign-In** - Autenticación social
- **Coroutines** - Programación asíncrona

### APIs y Servicios
<div align="center">

![OpenAI](https://img.shields.io/badge/OpenAI-GPT--4o-black.svg)
![Groq](https://img.shields.io/badge/Groq-Llama--3-orange.svg)
![OpenWeather](https://img.shields.io/badge/OpenWeather-API-blue.svg)
![DeepTranslator](https://img.shields.io/badge/DeepTranslator-API-green.svg)

</div>

- **OpenAI API** - GPT-4o para Chat IA
- **Groq API** - Llama 3.3 para respuestas rápidas
- **Open-Meteo API** - Datos del clima
- **Deep Translator** - Traducción multilenguaje

---

## � Estructura del Proyecto

```bash
MiniAmigixV-Mobile/
├── app/
│   ├── src/main/
│   │   ├── java/com/miniamigixv/miniamigixv_app/
│   │   │   ├── auth/                 # Autenticación (Login, Register, Google Sign-In)
│   │   │   ├── chat/                 # Chat IA con múltiples proveedores
│   │   │   ├── music/                # Reproductor de música y playlists
│   │   │   ├── screens/              # Pantallas UI principales
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   ├── ChatScreen.kt
│   │   │   │   ├── EventsScreen.kt
│   │   │   │   ├── NotificationsScreen.kt
│   │   │   │   ├── SupportScreen.kt
│   │   │   │   ├── SuggestionsBottomSheet.kt
│   │   │   │   └── ...
│   │   │   ├── data/                 # Modelos de datos y repositorios
│   │   │   ├── ui/                   # Componentes UI y tema
│   │   │   │   ├── theme/            # Tema Material Design 3
│   │   │   │   └── components/       # Componentes reutilizables
│   │   │   └── MainActivity.kt       # Actividad principal
│   │   └── google-services.json     # Configuración Firebase
│   └── build.gradle.kts              # Configuración Gradle del módulo
├── backend/                          # Backend opcional (Node.js)
│   ├── server.js                     # Servidor Express
│   └── package.json                  # Dependencias Node.js
├── build.gradle.kts                  # Configuración Gradle raíz
├── settings.gradle.kts               # Configuración de settings
├── gradle.properties                 # Propiedades de Gradle
├── local.properties                  # Configuración local
└── README.md                         # Este archivo
```

---

## ⚙ Instalación

### Requisitos Previos
- Android Studio Hedgehog o superior
- SDK Android 34 (API Level 34)
- JDK 11 o superior
- Gradle 8.0+

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/maria45889/MiniAmigixV-Mobile.git
cd MiniAmigixV-Mobile
```

### Paso 2: Configurar Firebase

1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Agrega una app Android con el paquete `com.miniamigixv.miniamigixv_app`
3. Descarga `google-services.json` y colócalo en `app/`
4. Habilita Google Sign-In en Firebase Console

### Paso 3: Configurar OAuth Client (Recomendado)

Para flujos OAuth seguros:
1. Ve a [Google Cloud Console](https://console.cloud.google.com/apis/credentials)
2. Crea un cliente OAuth 2.0 tipo "Web application"
3. Configura la pantalla de consentimiento OAuth
4. Actualiza `WEB_CLIENT_ID` en `AuthViewModel.kt`

### Paso 4: Abrir en Android Studio

1. Abre Android Studio
2. Selecciona "Open an Existing Project"
3. Navega al directorio del proyecto y selecciónalo
4. Espera a que Gradle sincronice el proyecto

### Paso 5: Configurar Variables de Entorno

Opcionalmente, puedes configurar las siguientes variables en `local.properties`:

```properties
OPENAI_API_KEY=tu_api_key_de_openai
GROQ_API_KEY=tu_api_key_de_groq
```

### Paso 6: Modo Desarrollo (Mock)

El proyecto está configurado con modo mock activado por defecto para desarrollo sin backend:

**Credenciales de prueba:**
- Email: `test@test.com`
- Contraseña: `123456`

**Para desactivar modo mock:**
- En `AuthRepository.kt`: `useMockApi = false`
- En `ChatApiConfig.kt`: `useMockApi = false`

---

## 🏗️ Estructura del Proyecto

```bash
MiniAmigixV-Mobile/
├── app/
│   ├── src/main/
│   │   ├── java/com/miniamigixv/miniamigixv_app/
│   │   │   ├── auth/                 # Autenticación
│   │   │   ├── chat/                 # Chat IA
│   │   │   ├── music/                # Música
│   │   │   ├── screens/              # Pantallas UI
│   │   │   └── MainActivity.kt       # Actividad principal
│   │   └── google-services.json     # Configuración Firebase
│   └── build.gradle.kts              # Configuración Gradle
├── backend/                          # Backend opcional (Node.js)
│   ├── server.js                     # Servidor Express
│   └── package.json                  # Dependencias Node.js
├── build.gradle.kts                  # Configuración Gradle raíz
└── README.md                         # Este archivo
```

---

## 📦 Construir el Proyecto

### Usando Gradle Wrapper
```bash
# Limpiar proyecto
./gradlew clean

# Construir APK debug
./gradlew assembleDebug

# Construir APK release
./gradlew assembleRelease

# Ejecutar pruebas
./gradlew test
```

### Usando Android Studio
1. Abre el proyecto en Android Studio
2. Sincroniza el proyecto con Gradle
3. Selecciona "Build > Build Bundle(s) / APK(s) > Build APK(s)"

---

## 🚀 Ejecutar la Aplicación

### En Emulador
```bash
# Listar emuladores disponibles
emulator -list-avds

# Iniciar emulador específico
emulator -avd <nombre_emulador>

# Instalar APK
adb install app/build/outputs/apk/debug/app-debug.apk
```

### En Dispositivo Físico
1. Habilita "Depuración USB" en tu dispositivo
2. Conecta el dispositivo al PC
3. Ejecuta:
```bash
adb devices
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔐 Configuración de Seguridad

### OAuth Seguro
El proyecto está configurado con OAuth seguro usando:
- Cliente OAuth Web (client_type: 1) para flujos de autorización seguros
- Cliente OAuth Android (client_type: 3) para compatibilidad
- Firebase Auth para gestión de sesiones

### Variables de Configuración
- `AuthApiClient.BASE_URL`: URL del backend de autenticación
- `ChatApiConfig.BASE_URL`: URL del backend de chat
- `AuthRepository.useMockApi`: Activar/desactivar modo mock
- `ChatApiConfig.useMockApi`: Activar/desactivar modo mock

---

## 🎨 Diseño UI/UX

MiniAmigixV Mobile utiliza una interfaz visual moderna basada en:

<div align="center">

**Glassmorphism** • **Degradados Neon** • **Animaciones Suaves** • **Material Design 3** • **Jetpack Compose**

</div>

### Inspiración Visual
- 🎬 Netflix - Interfaz de contenido multimedia
- 🎵 Spotify - Reproductor de música y playlists
- 📊 Dashboards futuristas - Visualización de datos
- 🌟 Apple Design - Minimalismo y elegancia

### Características de Diseño
- Efectos de vidrio esmerilado (glassmorphism)
- Gradientes vibrantes y modernos
- Transiciones fluidas y naturales
- Tipografía clara y legible
- Iconos intuitivos y consistentes
- Paleta de colores cohesiva
- Modo oscuro optimizado
- Diseño responsivo para diferentes tamaños de pantalla

---

## 🐛 Solución de Problemas

### Error: "No data is available for this project"
- Verifica que `google-services.json` esté correctamente configurado
- Asegúrate de que el proyecto de Firebase esté activo

### Error: "Cannot access 'state': it is private"
- Este error fue corregido agregando métodos públicos en `AuthViewModel`
- Reconstruye el proyecto después de los cambios

### Error de conexión con backend
- Verifica que el backend esté corriendo en el puerto 3000
- Para emuladores usa `10.0.2.2:3000` en lugar de `localhost:3000`
- Activa el modo mock para desarrollo sin backend

---

## 📝 Notas de Desarrollo

- El proyecto usa Material Design 3 con tema personalizado
- La navegación se implementa con Jetpack Navigation Compose
- El chat usa respuestas simuladas en modo mock
- El backend usa almacenamiento en memoria (no persistente)
- Las notificaciones usan agrupación por fecha como la versión web
- Los eventos incluyen recordatorios automáticos
- El soporte técnico usa sistema de tickets con categorías
- Las sugerencias incluyen estados y prioridades

---

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-feature`)
3. Commit tus cambios (`git commit -m 'Añadir nueva feature'`)
4. Push a la rama (`git push origin feature/nueva-feature`)
5. Abre un Pull Request

### Guía de Contribución
- Respeta el estilo de código existente
- Escribe tests para nuevas features
- Actualiza la documentación
- Sé respetuoso y constructivo

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## 👨‍💻 Autor

<div align="center">

### María José Taco

[![GitHub](https://img.shields.io/badge/Github-maria45889-black.svg)](https://github.com/maria45889)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-María--José--Taco-blue.svg)](https://linkedin.com/in/maria-jose-taco)

**Desarrolladora Mobile Full Stack**

Especialista en Kotlin, Jetpack Compose y desarrollo de interfaces móviles modernas. Apasionada por crear experiencias digitales únicas y funcionales en Android.

</div>

---

## 🙏 Agradecimientos

- A Firebase por las herramientas de autenticación
- A Google por las APIs de Android y Jetpack Compose
- A la comunidad de código abierto
- Al equipo de MiniAmigixV Web por la inspiración

---

## 📞 Soporte

¿Tienes preguntas o sugerencias?

- 📧 Email: miniamigixv@gmail.com
- 🐛 Issues: [GitHub Issues](https://github.com/maria45889/MiniAmigixV-Mobile/issues)
- 💬 Discord: [Únete a nuestro servidor](#)

---

<div align="center">

**⭐ Si te gusta este proyecto, ¡dale una estrella en GitHub! ⭐**

