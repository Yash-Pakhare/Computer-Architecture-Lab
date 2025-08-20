	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	addi %x0, 2, %x4
	divi %x3, 2, %x5
	addi %x5, 1, %x5
loop:
	div %x3, %x4, %x6
	beq %x31, %x0, notprime
	addi %x4, 1, %x4
	bne %x4, %x5, loop
	addi %x0, 1, %x10
	end
notprime:
	subi %x0, 1, %x10
	end
