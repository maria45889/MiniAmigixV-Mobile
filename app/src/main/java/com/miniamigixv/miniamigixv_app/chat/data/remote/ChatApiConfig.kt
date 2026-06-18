package com.miniamigixv.miniamigixv_app.chat.data.remote

object ChatApiConfig {
    /** Base URL of your backend (e.g. "https://api.tudominio.com/" or "http://10.0.2.2:3000/" for local dev) */
    const val BASE_URL = "http://192.168.100.174:3000/"

    /** Timeout in seconds for each request */
    const val TIMEOUT_SECONDS = 30L

    /** Whether to use the real API or mock data */
    var useMockApi: Boolean = true
}
