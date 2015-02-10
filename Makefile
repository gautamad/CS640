OBJS = Iperfer.class
TARGET = Iperfer

CC = javac

all: Iperfer

Iperfer: Iperfer.java
	$(CC) Iperfer.java

clean: 
	rm -f *.class
