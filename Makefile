JC = javac
JCFLAGS = -d bin
JD = javadoc
JDFLAGS = -d doc


COMPILE_SRC = src/*.java src/gui/*.java src/objects/*.java src/raytracer/*.java 
DOCS_SRC = src/*.java src/gui/*.java src/objects/*.java src/raytracer/*.java 

all: compile docs

# generate class files
compile:
	mkdir -p bin
	$(JC) $(JCFLAGS) $(COMPILE_SRC)

# generate java docs
docs:
	mkdir -p doc
	$(JD) $(JDFLAGS) $(DOCS_SRC)

# run the terminal app
run:
	java -cp bin Main

# clean the bin a doc directory
clean:
	rm -rf bin/* doc/*