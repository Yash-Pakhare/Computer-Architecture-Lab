	.data
n:
	10
	.text
main:
	load %x0, $n, %x3
	addi %x0, 0, %x4
	addi %x0, 1, %x5
	addi %x0, 65535, %x6
loop:
	subi %x3, 1, %x3
	store %x4, 0, %x6
	subi %x6, 1, %x6
	add %x0, %x4, %x7
	add %x0, %x5, %x4
	add %x7, %x5, %x5
	bne %x3, %x0, loop
	end
