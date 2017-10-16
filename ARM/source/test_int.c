volatile unsigned int * const UART0DR = (unsigned int *)0x101f1000;

void putc_uart0(char c) {
 *UART0DR = (unsigned int)c;
}
void print_uart0(const char *s) {
 while(*s != '\0') { /* Loop until end of string */
 putc_uart0(*s); /* Transmit char */
 s++; /* Next char */
 }
}

void print_int_uart0(int num){
    int a = num;

    if (num < 0)
    {
       putc_uart0('-');
       num = -num;
    }

    if (num > 9) print_int_uart0(num/10);

    putc_uart0('0'+ (a%10));
 }

void c_entry() {
 print_int_uart0(4);
}

void k_entry() {
  print_uart0("Hello!!");
}
