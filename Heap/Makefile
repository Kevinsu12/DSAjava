# This is the Makefile to use for Homework Assignment #10
# To use, at the prompt, type:
#
#       make		# This will make everything
# or
#       make clean	# This will safely remove old stuff
# or
#	make new	# This will remove all old stuff and
#			# will compile fresh

.PHONY: clean new 
.SUFFIXES: .java .class
.java.class:
	javac -g $<


# run: clean all
# 	clear
# 	./Calc 2>&1 | tee ../__outputs__/java.txt

all:	Driver 


Driver:	Base.java Driver.java MyLib.java \
	SymTab.java Heap.java Tracker.java Size.java \
	Driver.java
	javac Driver.java
	echo 'java Driver $$*' > Driver
	chmod ug+rx Driver

clean:
	rm -f *.class Driver Calc
	# cp ./class_files/*.class .
	cp $(HOME)/hw10/class_files/*.class .

public:
	rm -f *.class Driver Calc

new:
	make clean
	make

