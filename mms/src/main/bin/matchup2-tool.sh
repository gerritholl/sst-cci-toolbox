#! /bin/sh

. $MMS_INST/mymms
. $MMS_HOME/bin/mms-env.sh

java \
    -Dmms.home="$MMS_HOME" \
    -Xmx4096M $MMS_OPTIONS \
    -javaagent:"$MMS_HOME/lib/openjpa-all-${openjpa.version}.jar" \
    -classpath "$MMS_HOME/lib/*" \
    org.esa.cci.sst.tools.MatchupTool "$@"