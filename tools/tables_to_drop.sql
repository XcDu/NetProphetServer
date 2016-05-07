select concat('drop table ', table_name, ';')
from information_schema.tables
where table_name like 'app%_%info';
