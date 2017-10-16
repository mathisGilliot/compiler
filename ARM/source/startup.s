	.global _start
_start:
	LDR R2, =y
	LDR R2, [R2]
	MOV R2, #0
	STR R2, y
	B E_Cond1
debLoop1:
	LDR R2, =y
	LDR R2, [R2]
	LDR R2, =y
	LDR R2, [R2]
	add R2, R2, #1
	STR R2, y
E_Cond1:
	LDR R2, =y
	LDR R2, [R2]
	cmp R2, #10
	BLT debLoop1
	LDR SP, =stack_top
	LDR R0, =y
	LDR R0, [R0]
	BL print_int_uart0
	B .
y:
	.byte 0
