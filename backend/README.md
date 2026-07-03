# MiniAmigixV Backend

Backend para la aplicación MiniAmigixV - Chat IA y funcionalidades de correo.

## Características

- **Autenticación JWT** - Registro y login de usuarios
- **Chat IA** - Integración con OpenAI API (modo mock si no hay API key)
- **Historial de chat** - Almacenamiento de conversaciones por usuario
- **Envío de correos** - Funcionalidad para enviar emails vía SMTP

## Instalación

1. Instalar dependencias:
```bash
npm install
```

2. Configurar variables de entorno:
```bash
cp .env.example .env
```

3. Editar `.env` con tus configuraciones:
```
PORT=8000
JWT_SECRET=tu-secreto-jwt
OPENAI_API_KEY=tu-api-key-openai
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USER=tu-email@gmail.com
EMAIL_PASS=tu-app-password
EMAIL_FROM=MiniAmigixV <noreply@miniamigixv.com>
```

## Ejecutar

```bash
npm start
```

El servidor correrá en `http://localhost:8000`

## Endpoints

### Autenticación

- `POST /auth/register` - Registrar usuario
- `POST /auth/login` - Iniciar sesión
- `GET /auth/me` - Obtener información del usuario (requiere token)

### Chat

- `POST /api/chat/send/` - Enviar mensaje al chat (requiere token)
- `GET /api/chat/history/` - Obtener historial de chat (requiere token)
- `POST /chat` - Endpoint legacy para compatibilidad

### Email

- `POST /api/email/send` - Enviar email (requiere token)

## Configuración de Email (Gmail)

Para usar Gmail con nodemailer:

1. Activa 2FA en tu cuenta Google
2. Genera una "App Password" en:
   - Cuenta de Google > Seguridad > 2FA > App Passwords
3. Usa esa app password en `EMAIL_PASS`

## Modo Mock

Si no configuras `OPENAI_API_KEY`, el backend usará respuestas predefinidas en modo mock.

## Notas

- Los datos se almacenan en memoria (reiniciar el servidor borra todo)
- Para producción, usar base de datos real (PostgreSQL, MongoDB, etc.)
- Configurar HTTPS para producción
