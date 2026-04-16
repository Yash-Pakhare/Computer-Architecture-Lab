	.data
a:
	121
	.text
main:
	load %x0, $a, %x3
	addi %x0, 1, %x4
	addi %x3, 0, %x5
	addi %x0, 0, %x6
	addi %x0, 0, %x8
checklen:
	divi %x5, 10, %x5
	addi %x6, 1, %x6
	bne %x5, %x0, checklen
loop1:
	subi %x6, 1, %x6
	muli %x4, 10, %x4
	bne %x6, %x0, loop1
	addi %x3, 0, %x5
loop2:
	divi %x4, 10, %x4
	divi %x5, 10, %x5
	mul %x4, %31, %x7
	add %x7, %x8, %x8
	bne %x5, %x0, loop2
	beq %x8, %x3, palindrome
	subi %x0, 1, %x10
	end
palindrome:
	addi %x0, 1, %x10
	end
