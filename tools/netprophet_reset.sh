USER=root
PASSWD=23621169qqo
DB=netprophet
mysql -u${USER} -p${PASSWD} ${DB} -N < tables_to_drop.sql > drop.sql
mysql -u${USER} -p${PASSWD} ${DB} -N < drop.sql
mysql -u${USER} -p${PASSWD} ${DB} -e "truncate table applications_map;" 
mysql -u${USER} -p${PASSWD} ${DB} -e "alter table applications_map AUTO_INCREMENT=1;"
sh tomcat_restart.sh
