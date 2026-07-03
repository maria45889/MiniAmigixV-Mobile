package com.miniamigixv.miniamigixv_app.chat.data.remote

object ChatApiConfig {
    /** Base URL of your backend (e.g. "https://api.tudominio.com/" or "http://10.0.2.2:8000/" for local dev) */
    const val BASE_URL = "http://10.19.45.68:8000/"

    /** Timeout in seconds for each request */
    const val TIMEOUT_SECONDS = 30L

    /** Whether to use the real API or mock data */
    var useMockApi: Boolean = true // Enable mock API for testing
}
