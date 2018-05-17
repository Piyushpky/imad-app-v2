AVATAR REG
==========

CTF TEST CASES
==============

ISSUE
=====

In running the CTF test cases, it was looking for 4 databases. But HSQL does not support linking of more than one database at a single instance of time.
So CTF was not able to connect to the databases.

APPROACH
========

We got a approach of connecting the CTF framework with only one database instead of pointing it to all 4 databases by creating all the table entries in
a single database. So created all the table entries required for the CTF in only single database i.e Pod_Device.
and hence connected CTF to one single database through HSQL.