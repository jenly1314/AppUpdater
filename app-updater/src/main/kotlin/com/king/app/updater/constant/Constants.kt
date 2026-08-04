package com.king.app.updater.constant

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class Constants private constructor() {

    companion object {

        const val DEFAULT_NOTIFICATION_ID = 0x66

        const val DEFAULT_NOTIFICATION_CHANNEL_ID = "0x66"

        const val DEFAULT_NOTIFICATION_CHANNEL_NAME = "AppUpdater"

        const val EXTRA_ACTION = "app_updater_extra_action"

        const val ACTION_START_DOWNLOAD = "app_updater_action_start_download"

        const val ACTION_CANCEL_DOWNLOAD = "app_updater_action_cancel_download"

        const val ACTION_RETRY_DOWNLOAD = "app_updater_action_retry_download"

        const val ACTION_CLEAR_LISTENER = "app_updater_action_clear_listener"

        const val NONE = -1

        const val DEFAULT_FILE_PROVIDER = ".AppUpdaterFileProvider"

        const val DEFAULT_DIR = "apk"

        const val PROGRESS_UPDATE_INTERVAL = 50L

        const val TIME_OUT_MILLIS = 20000

        internal const val HTTP_TEMP_REDIRECT = 307
        internal const val HTTP_PERM_REDIRECT = 308
    }
}
