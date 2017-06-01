#!/usr/bin/env bash

sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'

apt-get install wget ca-certificates
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
apt-get update

echo  "--- Install PostgreSQL ---"
apt-get install -y --force-yes postgresql-9.4

echo "host all all 10.0.2.0/24 md5" >> /etc/postgresql/9.4/main/pg_hba.conf
sed -i -e "s/#listen_addresses = 'localhost'/listen_addresses = '*'/g" /etc/postgresql/9.4/main/postgresql.conf

/etc/init.d/postgresql restart

#echo "--- Switching user to 'postgres' ---"
#sudo su - postgres

echo "--- Dropping DB if existing ---"
sudo su postgres -c "dropdb 'wms_db'"

echo "--- Creating user for DB ---"
sudo su postgres -c "psql -c \"CREATE USER wms PASSWORD 'somepassword';\""

echo "--- Create database 'wms_db' ---"
sudo su postgres -c "createdb wms_db -O wms"