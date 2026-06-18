# MiniAmigixV Mobile

Aplicación móvil Android desarrollada con Kotlin y Jetpack Compose que ofrece múltiples funcionalidades interactivas.

## 🚀 Características

- **Autenticación**: Inicio de sesión con correo/contraseña y Google Sign-In
- **Chat IA**: Asistente virtual inteligente con respuestas simuladas
- **Música**: Reproductor de música integrado
- **Clima**: Información meteorológica actual
- **Juegos**: Colección de juegos interactivos
- **Estudio**: Herramientas educativas
- **Blog**: Plataforma para leer y escribir contenido
- **Perfil**: Gestión de perfil de usuario

## 🛠️ Tecnologías

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose
- **Arquitectura**: MVVM con ViewModel
- **Networking**: Retrofit + OkHttp
- **Autenticación**: Firebase Auth + Google Sign-In
- **Backend**: Node.js + Express (opcional)
- **Build**: Gradle con Kotlin DSL

## 📋 Requisitos Previos

- Android Studio Hedgehog o superior
- SDK Android 34 (API Level 34)
- JDK 11 o superior
- Node.js 18+ (para backend opcional)

## 🔧 Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/maria45889/MiniAmigixV-Mobile.git
cd MiniAmigixV-Mobile
```

### 2. Configurar Firebase
1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Agrega una app Android con el paquete `com.miniamigixv.miniamigixv_app`
3. Descarga `google-services.json` y colócalo en `app/`
4. Habilita Google Sign-In en Firebase Console

### 3. Configurar OAuth Client (Recomendado)
Para flujos OAuth seguros:
1. Ve a [Google Cloud Console](https://console.cloud.google.com/apis/credentials)
2. Crea un cliente OAuth 2.0 tipo "Web application"
3. Configura la pantalla de consentimiento OAuth
4. Actualiza `WEB_CLIENT_ID` en `AuthViewModel.kt`

### 4. Modo Desarrollo (Mock)
El proyecto está configurado con modo mock activado por defecto para desarrollo sin backend:

**Credenciales de prueba:**
- Email: `test@test.com`
- Contraseña: `123456`

**Para desactivar modo mock:**
- En `AuthRepository.kt`: `useMockApi = false`
- En `ChatApiConfig.kt`: `useMockApi = false`

## 🏗️ Estructura del Proyecto

```
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

## 📝 Notas de Desarrollo

- El proyecto usa Material Design 3 con tema personalizado
- La navegación se implementa con Jetpack Navigation Compose
- El chat usa respuestas simuladas en modo mock
- El backend usa almacenamiento en memoria (no persistente)

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-feature`)
3. Commit tus cambios (`git commit -m 'Añadir nueva feature'`)
4. Push a la rama (`git push origin feature/nueva-feature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 👨‍💻 Autor

**María José** - [GitHub](https://github.com/maria45889)

## 🙏 Agradecimientos

- Firebase por las herramientas de autenticación
- Google por las APIs de Android
- La comunidad de código abierto
