TARGET=barber

$(TARGET): $(TARGET).c
	gcc -o barber barber.c

run: $(TARGET)
	./barber

test: $(TARGET)
	python3 test.py
