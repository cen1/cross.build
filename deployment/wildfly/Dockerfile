FROM jboss/wildfly:10.0.0.Final

USER root
RUN yum install -y git && \
yum install -y wget && \
yum install -y unzip && \
wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo && \
sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo && \
yum install -y apache-maven

USER jboss
ENV KEYCLOAK_VERSION 1.9.3.Final

RUN git clone https://github.com/TFaga/lynx.git && \
cd lynx && mvn install && cd .. && \
git clone https://github.com/cen1/cross.build.git && \
cd cross.build/core && \
mvn package && \
cp ear/target/cross-build-ear-0.0.1-SNAPSHOT.ear $JBOSS_HOME/standalone/deployments && \
wget -P $JBOSS_HOME http://downloads.jboss.org/keycloak/1.9.4.Final/adapters/keycloak-oidc/keycloak-wildfly-adapter-dist-1.9.4.Final.zip && \
unzip $JBOSS_HOME/keycloak-wildfly-adapter-dist-1.9.4.Final.zip -d $JBOSS_HOME/ && \
mkdir -p /opt/jboss/wildfly/modules/org/postgresql/main

ADD https://jdbc.postgresql.org/download/postgresql-9.4-1206-jdbc42.jar /opt/jboss/wildfly/modules/org/postgresql/main
ADD module.xml $JBOSS_HOME/modules/org/postgresql/main
ADD configure.cli $JBOSS_HOME/bin
RUN $JBOSS_HOME/bin/jboss-cli.sh --file=$JBOSS_HOME/bin/configure.cli

USER root
RUN chown -R jboss:jboss /opt/jboss/wildfly/modules && ls -la /opt/jboss/wildfly/modules/org/postgresql/main && rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/current
USER jboss

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]








