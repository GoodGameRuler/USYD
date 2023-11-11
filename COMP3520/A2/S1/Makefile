TARGET := dispatch
DEPENDENCIES := src/pcb.c
SRC := src
OUT := out
LIB := -I lib
RESOURCES := resources
DEBUG_DIR := debug
FLAGS := -fsanitize=address -g
PROCESS := process

.PHONY: all debug batch_debug

all: $(TARGET)

run: $(TARGET) $(PROCESS) 
	./dispatch $(RESOURCES)/rand.txt

$(PROCESS): $(SRC)/sigtrap.c
	gcc -o $(PROCESS) $^

rand: random
	./random rand.txt

random: random.c
	gcc -o $(OUT)/$@ $^ -lm $(LIB)

$(TARGET): $(SRC)/$(TARGET).c $(DEPENDENCIES)
	gcc $(FLAGS) -o $@ $(SRC)/pcb.c $(SRC)/$@.c $(LIB)

debug: $(TARGET)
	gdb -x $(DEBUG_DIR)/basic.txt --args $(TARGET) $(RESOURCES)/rand.txt

batch_debug: $(TARGET)
	gdb -x $(DEBUG_DIR)/basic.txt --batch --args $(TARGET) $(RESOURCES)/rand.txt



