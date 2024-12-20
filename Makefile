# -----------------------------------------
# Makefile for project TP2
#
# Help and advice provided by ChatGPT (OpenAI)
# for the structure and improvements of the Makefile.
# -----------------------------------------

# Global variables
MAINCLASS=Main
TESTCLASS=Tests.Utilities.TestRunner
INSTALLDIR=out/production/tp2
JARFILE=TP2_RANDOM_TREES

# Default target: compile, install, jar, and execute
all: compile install jar exec

# Compile the source files
compile:
	cd src ; make compile

# Install necessary files (if applicable)
install:
	cd src ; make install

# Create the .jar file (requires compiled classes)
jar: compile
	cd $(INSTALLDIR); \
	echo Main-Class: $(subst /,.,$(MAINCLASS)) > manifest.txt ; \
	jar cvfm $(JARFILE).jar manifest.txt ./
	mv $(INSTALLDIR)/$(JARFILE).jar ./

# Execute the .jar file with command-line arguments -> make ARGS="1" / make exec ARGS="1"
exec: $(JARFILE).jar
	@if [ -z "$(ARGS)" ]; then \
		java -jar $(JARFILE).jar; \
	else \
		java -jar $(JARFILE).jar $(ARGS); \
	fi

# Clean generated files (classes, jars, zips, etc.)
clean:
	cd src ; make clean ; make cleanInstall
	rm -f *.zip *.jar manifest.*

# Run all unit tests
run_tests: compile_tests exec_tests

# Compile all unit tests
compile_tests:
	cd src ; make compile_tests

# Execute all unit tests
exec_tests:
	java -classpath $(INSTALLDIR) $(TESTCLASS)

# Create a zip of the project files
zip:
	moi=$$(whoami) ; zip -r $${moi}_renduTP2.zip *

# Verify the contents of the zip
zipVerify:
	moi=$$(whoami) ; unzip -l $${moi}_renduTP2.zip
