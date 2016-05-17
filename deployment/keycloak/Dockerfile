FROM jboss/keycloak-postgres

ADD configure.cli $JBOSS_HOME/bin
RUN cd $JBOSS_HOME/standalone/configuration && \
keytool -keyalg RSA -keysize 2048 -genkey -noprompt -alias cross.build -dname "CN=cross.build, OU=cross.build, O=cross.build, L=none, S=none, C=SI" -keystore keycloak.jks -storepass changeit -keypass changeit
RUN $JBOSS_HOME/bin/jboss-cli.sh --file=$JBOSS_HOME/bin/configure.cli && rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/current && \
cat $JBOSS_HOME/standalone/configuration/standalone.xml && ls $JBOSS_HOME/standalone/configuration && echo 1
EXPOSE 8443
