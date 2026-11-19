package app.aaps.shared.impl.logging

import app.aaps.core.interfaces.logging.AAPSLogger
import app.aaps.core.interfaces.logging.LTag

/**
 * A logger that writes nothing. Every call returns at once.
 *
 * Used instead of `AAPSLoggerProduction` to save battery: that logger builds a stack trace for
 * every call and writes each line to a file and to logcat. With this logger there are no logs at
 * all, also no errors.
 */
class AAPSLoggerNone : AAPSLogger {

    override fun debug(message: String) {}
    override fun debug(enable: Boolean, tag: LTag, message: String) {}
    override fun debug(tag: LTag, message: String) {}
    override fun debug(tag: LTag, accessor: () -> String) {}
    override fun debug(tag: LTag, format: String, vararg arguments: Any?) {}
    override fun warn(tag: LTag, message: String) {}
    override fun warn(tag: LTag, format: String, vararg arguments: Any?) {}
    override fun info(tag: LTag, message: String) {}
    override fun info(tag: LTag, format: String, vararg arguments: Any?) {}
    override fun error(tag: LTag, message: String) {}
    override fun error(tag: LTag, message: String, throwable: Throwable) {}
    override fun error(tag: LTag, format: String, vararg arguments: Any?) {}
    override fun error(message: String) {}
    override fun error(message: String, throwable: Throwable) {}
    override fun error(format: String, vararg arguments: Any?) {}
    override fun debug(className: String, methodName: String, lineNumber: Int, tag: LTag, message: String) {}
    override fun info(className: String, methodName: String, lineNumber: Int, tag: LTag, message: String) {}
    override fun warn(className: String, methodName: String, lineNumber: Int, tag: LTag, message: String) {}
    override fun error(className: String, methodName: String, lineNumber: Int, tag: LTag, message: String) {}
}
