#!/bin/sh
case "`uname`" in
    CYGWIN*)
        CFILE = `cygpath "$0"`
        RESOLVED_NAME=`readlink -f "$CFILE"`
        ;;
    Darwin*)
        RESOLVED_NAME=`readlink "$0"`
        ;;
    FreeBSD)
        RESOLVED_NAME=`readlink -f "$0"`
        ;;
    OpenBSD)
        RESOLVED_NAME=`readlink -f "$0"`
        JAVA_HOME=`/usr/local/bin/javaPathHelper -h keycloak`
        ;;
    Linux)
        RESOLVED_NAME=`readlink -f "$0"`
        ;;
esac

if [ "x$RESOLVED_NAME" = "x" ]; then
    RESOLVED_NAME="$0"
fi

if [ "x$JAVA" = "x" ]; then
    if [ "x$JAVA_HOME" != "x" ]; then
        JAVA="$JAVA_HOME/bin/java"
    else
        JAVA="java"
    fi
fi

DIRNAME=`dirname "$RESOLVED_NAME"`
"$JAVA" $KC_OPTS -cp $DIRNAME/client/keycloak-client-registration-cli-22.0.5.jar --add-opens=java.base/java.security=ALL-UNNAMED -Dkc.lib.dir=$DIRNAME/client/lib org.keycloak.client.registration.cli.KcRegMain "$@"
