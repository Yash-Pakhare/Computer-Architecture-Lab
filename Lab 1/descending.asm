	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x3
	addi %x0, 0, %x4
	addi %x0, 0, %x8
	addi %x0, 1, %x9
loop:
	load %x4, $a, %x6
	addi %x4, 1, %x5
	beq %x5, %x3, check
	load %x5, $a, %x7
	beq %x6, %x7, reloop
	bgt %x6, %x7, reloop
	store %x7, 0, %x4
	store %x6, 0, %x5
	addi %x0, 1, %x8
	bne %x5, %x3, reloop
check:
	addi %x0, 0, %x4
	subi %x3, 1, %x3
	beq %x8, %x9, assign
	end
assign:
	addi %x0, 0, %x8
	jmp loop
reloop:
	addi %x4, 1, %x4
	jmp loop
