
This is a spring boot application to parse carbbank.txt file and generata/save the data in a postgres database.

Postgres database is expected to run at localhost:5436

The application creates the tables in "postgres" database with user "glygen". 
The password for the glygen user is the same as the GlyTableMaker-backend application. 

In order to run the application, VM argument -Djasypt.encryptor.password=xxxxxx needs to be provided.
The jasypt secret is also the same as GlyTableMaker-backend application.

Program arguments should contain --file=/path/to/carbbank/file

Columns in the CarbbankRecord table
- id
- AU
- BA
- BA2
- CC
- CT
- DA
- FC
- SB
- SI
- TI
- structure

List of BS columns:
- bs
- c
- cell line
- cn
- disease
- domain
- f
- gs
- gt
- k
- ls
- nt
- o
- ot
- p/d
- star

There are separate tables with a foreign key to the main table for the following keys:
- AG
- AM
- AN
- BS
- DB
- MT
- NC
- NT
- PA
- PM
- SC
  - ST
  - TN
  - VR
