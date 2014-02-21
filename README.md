crontab-parser
======================================================

Scans the specified users' crontabs and persists the entries of interest.


-------------------------
| application structure |
-------------------------

bin:  contains the main executable (crontab_parser.sh)

config: contains database connection pooling configurations (c3p0.properties), log4j configurations (log4j.properties), and the main application configurations (config.properties)

lib:  contains the libraries required to compile and run this application

log:  contains the logs produced by the application

src:  contains the IDE-independant source code of the application

src/META-INF: contains Hibernate specific configuration files
