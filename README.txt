Copyright (c) 2010 Roman Naumann

	  This file is part of SR.
	  SR is free software: you can redistribute it and/or modify
	  it under the terms of the GNU General Public License as published by
	  the Free Software Foundation, either version 3 of the License, or
	  (at your option) any later version.

	  SR is distributed in the hope that it will be useful,
	  but WITHOUT ANY WARRANTY; without even the implied warranty of
	  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	  GNU General Public License for more details.

	  You should have received a copy of the GNU General Public License
	  along with SR.  If not, see <http://www.gnu.org/licenses/ >.

SR is supposed to become a video game in mid-future. Currently, though, it's just an 
incomplete bunch of isolated components we're implementing.
The following documentation is meant for developers:

You'll need maven, sbt and mysql (and of course git) to compile and test SR.

Akka has to be trunk for the remote actors to work properly:

# git clone git://github.com/jboner/akka.git
# cd akka
# sbt update publish-local publish

Now install mysql and configure it, so that it listens on Socket 3306

Next, clone SR:

# git clone git://github.com/rumpelstilzchen/SR.git
# cd SR
# mvn clean compile package

You can run the scripts now.
To create a database, run:
# ./create_database root

To create the tables, run:
# ./dbcmd <mysql-user> <mysql-pw> create_tables
# ./dbcmd <mysql-user> <mysql-pw> <cmd>

Finished. Happy hacking.
