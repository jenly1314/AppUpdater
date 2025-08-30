package com.king.app.updater.exception

/**
 * Http异常
 *
 * @param statusCode HTTP状态码
 * @param message 错误信息
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class HttpException(
    statusCode: Int, message: String
) : RuntimeException("Http $statusCode: $message")
