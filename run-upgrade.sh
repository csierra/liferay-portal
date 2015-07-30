#!/usr/local/bin/fish

set CLASSPATH "/tmp/properties:portal-impl/portal-impl.jar:portal-service/portal-service.jar:util-java/util-java.jar:util-taglib/util-taglib.jar:tools/sdk/dist/com.liferay.portal.upgrade-1.0.0.jar:tools/sdk/dist/com.liferay.registry.api-1.0.0.jar:tools/sdk/dist/com.liferay.registry.impl-1.0.0.jar"

set LIBS (ls lib/portal)
for lib in $LIBS;set CLASSPATH "$CLASSPATH:lib/portal/$lib"; end

set LIBS (ls lib/global)
for lib in $LIBS;set CLASSPATH "$CLASSPATH:lib/global/$lib"; end

set LIBS (ls lib/development)
for lib in $LIBS;set CLASSPATH "$CLASSPATH:lib/development/$lib"; end

set DEBUG_OPTS "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=9000"

mysql -uliferay -pliferay lportal_6_2 < /tmp/portal-upgrade/liferay-6.2.sql

# Run the upgrade process
java "-Xmx1024M" "-XX:MaxPermSize=256M" -cp "$CLASSPATH" "$DEBUG_OPTS" com.liferay.portal.tools.DBUpgrader
