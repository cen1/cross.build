# cross.build
Cross-platform CI.

1. Setting up WildFly 10
WildFly root directory is referred to as $JBOSS_HOME

1.1 Properly PostgreSQL JDBC driver to $JBOSS_HOME/modules
1.2 Fill correct values in cross.build.properties, copy it to $JBOSS_HOME/bin
1.3 Set <resolve-parameter-values> in $JBOSS_HOME/bin/jboss-cli.xml to true
1.4 Copy setup.cli $JBOSS_HOME/bin and run ./jboss-cli --file=setup.cli --properties=cross.build.properties
1.5 Extract Keycloak 1.9.1 overlay in $JBOSS_HOME
1.6 Copy keycloak-install.cli to $JBOSS_HOME/bin and run ./jboss-cli.sh --file=keycloak-install.cli

2. Keycloak setup
2.1 Open http://ip:8080/auth, create initial admin account and create "crossbuild" realm
2.2 Follow Keycloak documentation for adding GitHub identity provider: 
http://keycloak.github.io/docs/userguide/keycloak-server/html/identity-broker.html#d4e1870
No scopes are required.
2.3 Add realm role "user"
2.4 Set the role "user" to be added as default for new users
2.5 Create client "crossbuild": openid-connect, public, with proper URLs for your web ui (ex: http://localhost:3002/* for redirect)
2.6 Copy "crossbuild" client installation keycloak.json to backend and frontend
2.7 Extract Keycloak adapter to $JBOSS_HOME
2.8 Run ./jboss-cli.sh -c --file=adapter-install.cli