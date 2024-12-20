# -----------------------------------------
# Makefile for project TP2
#
# Help and advice provided by ChatGPT (OpenAI)
# for the structure and improvements of the Makefile.
# -----------------------------------------

# Global variables
MAINCLASS=Main
TESTCLASS=Tests.Utilities.TestRunner
INSTALLDIR=../out/production/tp2
MAINDIR=$(dir $(subst .,/,$(MAINCLASS)))  # Directory containing Main.java
CLASSES=$(shell find $(INSTALLDIR)/$(MAINDIR) -name \*.class)

# Compilation target
compile: $(subst .,/,$(MAINCLASS)).class

# Compile all unit tests
compile_tests: $(subst .,/,$(TESTCLASS)).class

# Compile .java files into .class files
%.class: %.java
	javac -g -d $(INSTALLDIR) $(shell find . -name "*.java")

# Install necessary files (currently empty)
install:

# Clean the compiled class files
cleanInstall:
	rm -f $(subst $$,\$$,$(CLASSES))

# General cleaning
clean:
	make cleanInstall
	rm -f $(INSTALLDIR)/*.class  # Remove generated class files