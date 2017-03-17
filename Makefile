all:
	javac -classpath lib -sourcepath src -d bin src/Main.java
run:
	java -classpath "bin;lib" Main
clean:
	rm -rf bin/*