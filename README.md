# CROSS.BUILD

Cross.Build is an open source project dedicated for building and testing of cross platform code written in compiled languages. It was design as a public service and with scalability in mind. You can try out the demo and read more about the project at http://cross.build.

## Installation instructions
Docker files and deployment scripts are preliminary and will be improved over time.Especially some build time parameters need to be moved out of Dockerfiles to runtime parameters instead.
All deployment Dockerfiles and scripts are in ./deployments folder which is also the working directory for the following instructions.

1. POSTGRES
   * Go to postgres, modify extend.sh, run build.sh and then run.sh. This will create and run a postgres container named crossbuild-postgres.

2. KEYCLOAK
   * Go to keycloak, modify Dockerfile and change certificate CN to your
domain. Modify configure.cli with correct key alias too.
   * Run build.sh then run.sh. This will create a new container called crossbuild-keycloak. Open keycloak console at: https://hostname:8443.
   * Add new realm named "crossbuild"
   * Add an identity provider, for example GitHub:
http://keycloak.github.io/docs/userguide/keycloak-server/html/identity-broker.html#d4e1945
Be extra careful when copying client ID and secret, copy them from a
regular text editor to be sure there are no spaces involved.
   * Add new client for frontend called "crossbuild"
   * Choose protocol "openid-connect" and set your frontend base url
(http://hostname:port). For valid redirect field add /* on the end.Leave everything else default.
   * Add realm role "user" and add it to default roles.

3. JENKINS
   * Go to Jenkins folder and execute run.sh. This will create a new container called crossbuild-jenkins.
   * Go to http://hostname:8081
   * Execute: docker exec -it <CONTAINERID> cat
/var/jenkins_home/secrets/initialAdminPassword
to get the initial password. Go to http://hostname:8081 and input the
initial password.
   * Choose option to install plugins on your own. Deselect all plugins
except:
*Timestamper, Git, SSH Slaves*
   * Create admin account
   * Go to Manage Jenkins->Configure Global Security and remove:
"Prevent Cross Site Request Forgery exploits" and "Enable Slave → Master
Access Control"

4. WILDFLY (core)
   * Accept FreeBSD licence agreement on AWS marketplace (no way to do this programatically): https://aws.amazon.com/marketplace/pp/B00KSS55FY/
   * Go to wildfly folder and modify configure.cli in regard to db datasource and env vars.
   * Run build.sh and run.sh. You will get a new container named crossbuild-wildfly.

5. WEB
   * Go to web folder and edit Dockerfile with correct Keycloak and API (core) url.Run build and run.sh. This will create container crossbuild-web.


If WILDFLY (core) backend fails to start for whatever reason you should clean the following data in order for initial run to succeed:  
1. Truncate or re-import whole database  
2. Remove all global credentials from Jenkins  
3. Remove Keypairs, Security groups and any newly created virtual machines on Ec2.  

After these three things are reset to initial state you can try to re-run the backend again.
