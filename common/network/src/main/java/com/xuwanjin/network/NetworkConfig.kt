package com.xuwanjin.network

/**
 *  the Config for network: timeout, baseurl etc.
 */
object NetworkConfig {
    const val HTTP_WRITE_TIMEOUT: Long = 30
    const val HTTP_CONNECT_TIMEOUT: Long = 30
    const val HTTP_READ_TIMEOUT: Long = 30
    const val BASE_HOST_URL: String = "https://openexchangerates.org/api/"
}
