To run queries, you can use the inject_query.sh script in the "project/bash_application/bin" folder.
When using the script, it must be called like so: inject_query <username> <password> <database> <query> (without brackets)

-BEFORE RUNNING ANY QUERIES-
Run inject_query.sh with the db_init.txt file first then you can use other query files.
This should load in the data files for the tables located in "~/project/bash/application/database_data".
You can find all queries for the project under "/project/bash/application/db_queries"

To make the inject_query.sh executable by only referencing its name, do the following:
1. append the directory containing the inject_query.sh to your PATH variable
	example: PATH=$PATH:HOME/project/bash_application/bin"
2. make sure you have permission to execute the script using chmod +x inject_query.sh
You should now be able to call it from any folder in your directory.
