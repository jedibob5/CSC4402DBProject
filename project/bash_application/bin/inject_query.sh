#!/bin/bash
if [ $# -lt 4 ]; then
	echo "Not enough arguments. Supply username,password,database_name,query_file in that order"
	exit 1
elif [ $# -gt 4 ]; then
	echo "Too many arguments. Supply username,password,database_name,query_file in that order"
	exit 1
fi
username=$1
password=$2
database=$3
query=$4
authentication='--local-infile -u$username' '-p$password' '$database
mysql $authentication <$query