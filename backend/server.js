const express = require("express");
const cors = require("cors");
const jwt = require("jsonwebtoken");
const crypto = require("crypto");
const nodemailer = require("nodemailer");

const app = express();
const PORT = process.env.PORT || 8000;
const JWT_SECRET = process.env.JWT_SECRET || "miniamigixv-secret-key-cambiar-en-prod";

// === CONFIG ===
const OPENAI_API_KEY = process.env.OPENAI_API_KEY || "tu-api-key-aqui";
const USE_OPENAI = OPENAI_API_KEY !== "tu-api-key-aqui";

// Email configuration
const EMAIL_HOST = process.env.EMAIL_HOST || "smtp.gmail.com";
const EMAIL_PORT = process.env.EMAIL_PORT || 587;
const EMAIL_USER = process.env.EMAIL_USER || "tu-email@gmail.com";
const EMAIL_PASS = process.env.EMAIL_PASS || "tu-app-password";
const EMAIL_FROM = process.env.EMAIL_FROM || "MiniAmigixV <noreply@miniamigixv.com>";

// Chat history storage (in-memory)
const chatHistory = [];

app.use(cors());
app.use(express.json());

// === AUTH STORE (in-memory; reemplazar con DB en producción) ===
const users = [];

// === MIDDLEWARE ===
function authMiddleware(req, res, next) {
  const header = req.headers.authorization;
  if (!header || !header.startsWith("Bearer ")) {
    return res.status(401).json({ error: "Token requerido" });
  }
  try {
    const decoded = jwt.verify(header.split(" ")[1], JWT_SECRET);
    req.user = decoded;
    next();
  } catch {
    return res.status(401).json({ error: "Token inválido o expirado" });
  }
}

// === AUTH ENDPOINTS ===

app.post("/auth/register", async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ error: "Email y contraseña requeridos" });
  }
  if (password.length < 6) {
    return res.status(400).json({ error: "La contraseña debe tener al menos 6 caracteres" });
  }

  const exists = users.find((u) => u.email === email);
  if (exists) {
    return res.status(409).json({ error: "El email ya está registrado" });
  }

  const salt = crypto.randomBytes(16).toString("hex");
  const hashedPassword = salt + ":" + crypto.pbkdf2Sync(password, salt, 10000, 64, "sha512").toString("hex");
  const user = { id: users.length + 1, email, password: hashedPassword };
  users.push(user);

  const token = jwt.sign({ id: user.id, email: user.email }, JWT_SECRET, { expiresIn: "7d" });
  res.status(201).json({ token, user: { id: user.id, email: user.email } });
});

app.post("/auth/login", async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ error: "Email y contraseña requeridos" });
  }

  const user = users.find((u) => u.email === email);
  if (!user) {
    return res.status(401).json({ error: "Credenciales inválidas" });
  }

  const [salt, storedHash] = user.password.split(":");
  const valid = storedHash === crypto.pbkdf2Sync(password, salt, 10000, 64, "sha512").toString("hex");
  if (!valid) {
    return res.status(401).json({ error: "Credenciales inválidas" });
  }

  const token = jwt.sign({ id: user.id, email: user.email }, JWT_SECRET, { expiresIn: "7d" });
  res.json({ token, user: { id: user.id, email: user.email } });
});

app.get("/auth/me", authMiddleware, (req, res) => {
  res.json({ user: req.user });
});

// Mock responses when no OpenAI key is configured
const mockResponses = [
  "¡Hola! ¿En qué puedo ayudarte hoy?",
  "Entiendo. Cuéntame más sobre eso.",
  "Eso suena interesante. ¿Qué más puedes decirme?",
  "Claro, déjame pensar... Es una buena pregunta.",
  "¡Claro! Puedo ayudarte con eso.",
  "MiniAmigixV está aquí para asistirte.",
  "¿Hay algo más en lo que pueda ayudarte?",
];

function getMockReply() {
  return mockResponses[Math.floor(Math.random() * mockResponses.length)];
}

// === ENDPOINTS ===

// Health check
app.get("/", (_req, res) => {
  res.json({ status: "ok", service: "MiniAmigixV Chat API" });
});

// Chat endpoint - updated to match app API
app.post("/api/chat/send/", authMiddleware, async (req, res) => {
  const { message, history = [] } = req.body;

  if (!message || message.trim().length === 0) {
    return res.status(400).json({ error: "Message is required" });
  }

  try {
    let reply;

    if (USE_OPENAI) {
      const OpenAI = require("openai");
      const openai = new OpenAI({ apiKey: OPENAI_API_KEY });

      const messages = [
        {
          role: "system",
          content:
            "Eres MiniAmigixV, un asistente virtual amigable y servicial. " +
            "Respondes en español de forma clara, concisa y cálida. " +
            "Ayudas con información general, clima, música, y conversación.",
        },
        ...history.map((h) => ({ role: h.role, content: h.content })),
        { role: "user", content: message },
      ];

      const completion = await openai.chat.completions.create({
        model: "gpt-4o-mini",
        messages,
        max_tokens: 500,
        temperature: 0.7,
      });

      reply = completion.choices[0]?.message?.content || "Lo siento, no pude procesar tu mensaje.";
    } else {
      // Mock mode - simulate delay
      await new Promise((resolve) => setTimeout(resolve, 1000));
      reply = getMockReply();
    }

    // Save to history
    const userId = req.user.id;
    const timestamp = new Date().toISOString();
    chatHistory.push({ userId, role: "user", content: message, created_at: timestamp });
    chatHistory.push({ userId, role: "assistant", content: reply, created_at: timestamp });

    res.json({ response: reply, timestamp: Date.now() });
  } catch (error) {
    console.error("Chat error:", error.message);
    res.status(500).json({ error: "Error processing message" });
  }
});

// Chat history endpoint
app.get("/api/chat/history/", authMiddleware, (req, res) => {
  const userId = req.user.id;
  const userHistory = chatHistory.filter(h => h.userId === userId);
  res.json(userHistory);
});

// Legacy endpoint for compatibility
app.post("/chat", authMiddleware, async (req, res) => {
  const { message, history = [] } = req.body;

  if (!message || message.trim().length === 0) {
    return res.status(400).json({ error: "Message is required" });
  }

  try {
    let reply;

    if (USE_OPENAI) {
      const OpenAI = require("openai");
      const openai = new OpenAI({ apiKey: OPENAI_API_KEY });

      const messages = [
        {
          role: "system",
          content:
            "Eres MiniAmigixV, un asistente virtual amigable y servicial. " +
            "Respondes en español de forma clara, concisa y cálida. " +
            "Ayudas con información general, clima, música, y conversación.",
        },
        ...history.map((h) => ({ role: h.role, content: h.content })),
        { role: "user", content: message },
      ];

      const completion = await openai.chat.completions.create({
        model: "gpt-4o-mini",
        messages,
        max_tokens: 500,
        temperature: 0.7,
      });

      reply = completion.choices[0]?.message?.content || "Lo siento, no pude procesar tu mensaje.";
    } else {
      // Mock mode - simulate delay
      await new Promise((resolve) => setTimeout(resolve, 1000));
      reply = getMockReply();
    }

    res.json({ reply, timestamp: Date.now() });
  } catch (error) {
    console.error("Chat error:", error.message);
    res.status(500).json({ error: "Error processing message" });
  }
});

// Email endpoint
app.post("/api/email/send", authMiddleware, async (req, res) => {
  const { to, subject, body } = req.body;

  if (!to || !subject || !body) {
    return res.status(400).json({ error: "To, subject, and body are required" });
  }

  try {
    const transporter = nodemailer.createTransport({
      host: EMAIL_HOST,
      port: EMAIL_PORT,
      secure: false,
      auth: {
        user: EMAIL_USER,
        pass: EMAIL_PASS,
      },
    });

    const mailOptions = {
      from: EMAIL_FROM,
      to: to,
      subject: subject,
      html: body,
    };

    await transporter.sendMail(mailOptions);
    res.json({ success: true, message: "Email sent successfully" });
  } catch (error) {
    console.error("Email error:", error.message);
    res.status(500).json({ error: "Error sending email" });
  }
});

app.listen(PORT, "0.0.0.0", () => {
  console.log(`MiniAmigixV Backend running on http://0.0.0.0:${PORT}`);
  console.log(`Mode: ${USE_OPENAI ? "OpenAI" : "Mock"} (set OPENAI_API_KEY for real AI)`);
  console.log(`Email: ${EMAIL_USER} (set EMAIL_HOST, EMAIL_USER, EMAIL_PASS for email)`);
});
