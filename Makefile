#!/bin/bash
all:
	javac -classpath lib -sourcepath src -d bin src/Main.java
run:
	java -classpath "bin:lib" Main
runwin:
	java -classpath "bin;lib" Main
clean:
	rm -rf bin/*