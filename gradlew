#!/bin/sh
APP_HOME="$(cd "$(dirname "$0")" && pwd)"
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Resolve java: JAVA_HOME > /usr/libexec/java_home > PATH
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
elif [ -x "/usr/libexec/java_home" ] && /usr/libexec/java_home > /dev/null 2>&1; then
    JAVACMD="$(/usr/libexec/java_home)/bin/java"
elif command -v java > /dev/null 2>&1; then
    JAVACMD="java"
else
    echo "ERROR: Java not found. Install Java 21+." >&2
    exit 1
fi

exec "$JAVACMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
