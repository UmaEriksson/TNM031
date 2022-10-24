# This is a Reverse tcp connection attack implementation in Go for the course TNM031, Network programming and Security

## This is tested with netcat

## How to run

* This software is mainly tested with netcat and using two different terminals to simulate two different computers

* Download netcat [here](https://nmap.org/ncat/) and install it (netcat is preinstalled on most linux distros)

* First run netcat in one terminal and specify a port, this will create a tcp server on the attack pc
``nc -nlvp 8080``

* Then open a new terminal and run the program ``go run main.go`` if you haven't built the code, see this [Link](https://www.digitalocean.com/community/tutorials/how-to-build-and-install-go-programs) for building go programs, you can build to multiple operating systems.

* You can specify a port with the flag -p XXXX when running the go file, where XXXX is a port number. You can also specify the IP address with the -c flag

* Now you can run commands in the terminal (pc) where you ran netcat and the command will be executed on the victim (pc)

* Try ``touch test.txt`` if victim pc is on linux or ``type nul > filename.txt`` on if the victim pc is on windows, some shell commands are OS specific.

* Now you can write shell commands on the attack pc which will execute on the victim pc


