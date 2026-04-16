	.data
a:
	10
	.text
main:
	load %x0, $a, %x4
	andi %x4, 1, %x5
	addi %x0, 1, %x3
	beq %x5, %x3, odd
	beq %x5, %x0, even
odd:
	addi %x0, 1, %x10
	end
even:
	subi %x0, 1, %x10
	end
