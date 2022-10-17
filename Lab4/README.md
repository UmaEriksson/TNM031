# This is a Reverse tcp connection attack implementation in Go for the course TNM031


## This is tested with netcat

## How to run

* This software is mainly tested with netcat and using two different terminals to simulate two different computers

* First run netcat in one terminal and specify a port
``nc -nlvp 8080``

* Then open a new terminal and run the program ``go run main.go`` if you haven't built the code, see this [Link](https://www.digitalocean.com/community/tutorials/how-to-build-and-install-go-programs) for building go programs, you can build to multiple operating systems.

* You can specify a port with the flag -p XXXX when running the go file, where XXXX is a port number. You can also specify the IP address with the -c flag

* Now you can run commands in the netcat terminal

* Try ``touch test.txt`` if victim pc is on linux, commands are OS specific


