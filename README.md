Duckling VMT
============

Virtual orgnization Management Tool (组织通讯录)

Build
-----
The localdeps of vmt is not complete so that please install localdeps
of 'ddl' as well.

'mvn package' and get 'vmt.war'.

Setup
-----
1) Have tomcat ready.

2) In WEB-INF/classes/conf, copy 'vmt-TEMPLATE.properties' to
'vmt.properties' and edit the latter.

3) Create database in mysql and create tables with
'WEB-INF/classes/conf/vmt_schema-xxx.sql'.

4) Setup LDAP db according to 'WEB-INF/classes/conf/ldap/README-LDAP.

5) Setup memcached. (download & make & make install, '$memcached -d')

6) Setup rabbitmq. (apt-get rabbitmq-server, no more config if using
default account guest/guest)

7) 'vmt.properties' mentions ddl/clb, which can be set later. Of
course, umt must be ready.

8) Start tomcat and enjoy.