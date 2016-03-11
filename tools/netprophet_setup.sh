# You can use this shell script ONLY in a new environment.
# This script DO change some libararies that is usually used.

# Check the privilege
if [ $UID -ne 0 ]; then
    echo "Superuser privileges are required to run this script."
    echo "e.g. \"sudo $0\""
    exit 1
fi

# Install basic tools
apt-get update
apt-get install software-properties-common
apt-get install openssl

# Check Java installation

# Install Java
add-apt-repository ppa:webupd8team/java
apt-get update
apt-get install oracle-java8-installer
apt-get install oracle-java8-set-default

# Set and get Java path
JAVA_HOME=/usr/lib/jvm/java-8-oracle
JRE_HOME=${JAVA_HOME}/jre

# Check tomcat installation

# Install tomcat
wget http://apache.fayea.com/tomcat/tomcat-7/v7.0.68/bin/apache-tomcat-7.0.68.tar.gz
tar zxvf apache-tomcat-7.0.68.tar.gz
mv apache-tomcat-7.0.68 /usr/bin/

# Tomcat directory
TOMCAT_HOME=/usr/bin/apache-tomcat-7.0.68

# Add environment variables into ${TOMCAT_HOME}/bin/startup.sh and ${TOMCAT_HOME}/bin/shutdown.sh
sed -i "27a JAVA_HOME=${JAVA_HOME}" ${TOMCAT_HOME}/bin/startup.sh ${TOMCAT_HOME}/bin/shutdown.sh
sed -i '28a JRE_HOME=${JAVA_HOME}/jre' ${TOMCAT_HOME}/bin/startup.sh ${TOMCAT_HOME}/bin/shutdown.sh
sed -i '29a PATH=${JAVA_HOME}/bin:${JRE_HOME}:$PATH' ${TOMCAT_HOME}/bin/startup.sh ${TOMCAT_HOME}/bin/shutdown.sh
sed -i '30a CLASSPATH=.:${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar' ${TOMCAT_HOME}/bin/startup.sh ${TOMCAT_HOME}/bin/shutdown.sh
sed -i "31a TOMCAT_HOME=${TOMCAT_HOME}" ${TOMCAT_HOME}/bin/startup.sh ${TOMCAT_HOME}/bin/shutdown.sh

# Auto-start tomcat
# if [ -z "$(grep -x "exit 0" /etc/rc.local)" ]; then
#   sed -i '$a /usr/bin/apache-tomcat-7.0.68/bin/startup.sh\n' /etc/rc.local
#   sed -i '$a exit\ 0' /etc/rc.local
# else
#   line_number=$(nl -b a /etc/rc.local | tail -n 1 | xargs | cut -d ' ' -f 1)
#   sed -i "${line_number}i /usr/bin/apache-tomcat-7.0.68/bin/startup.sh\n" /etc/rc.local
# fi

# Add web-admin
tomcat_user_name=admin
tomcat_password=password
sed -i "$(nl -b a /${TOMCAT_HOME}/conf/tomcat-users.xml | tail -n 1 | xargs |cut -d ' ' -f 1)i <role rolename=\"admin-gui\"/>\n<user username=\"${tomcat_user_name}\" password=\"${tomcat_password}\" roles=\"admin-gui\"/>" ${TOMCAT_HOME}/conf/tomcat-users.xml

# Start tomcat
sh ${TOMCAT_HOME}/bin/startup.sh

# Remove installation package
rm -f apache-tomcat-7.0.68.tar.gz

# Install mysql server and client
apt-get install --reinstall mysql-server mysql-client

# Exit
exit 0




