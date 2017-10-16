SOURCE=./source
DEST=./bin

all: execute clean

genere_bin: $(SOURCE) $(DEST)
	arm-none-eabi-as -mcpu=arm926ej-s -g $(SOURCE)/startup.s -o startup.o
	arm-none-eabi-gcc -c -mcpu=arm926ej-s -g $(SOURCE)/test_int.c -o test.o

genere_elf: $(SOURCE) $(DEST) genere_bin
	arm-none-eabi-ld -T $(SOURCE)/test.ld test.o startup.o -o $(DEST)/test.elf

elf_a_binaire: $(DEST) genere_elf
	arm-none-eabi-objcopy -O binary $(DEST)/test.elf $(DEST)/test.bin

execute: $(DEST) elf_a_binaire
	qemu-system-arm -M versatilepb -m 128M -nographic -kernel $(DEST)/test.bin

.PHONY: clean
clean:
	rm ./*.o $(DEST)/*.elf $(DEST)/*.bin
