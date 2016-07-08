#include <stdio.h>
#include <camkes/dataport.h>
#include <platsupport/serial.h>

#define UART_MDR1_MODE_SELECT_UART16X (0x0u)
#define UART16x_OPER_MODE (UART_MDR1_MODE_SELECT_UART16X)



void UART_rx (unsigned int baseAdd, unsigned char* buf, unsigned int size);
unsigned int UART_tx(unsigned int baseAdd, unsigned char *txbuf, unsigned int nobytes);
volatile Buf * serial_mem;
void UARTsetup(unsigned int baseAdd,
		unsigned int baud_rate,
		unsigned int UARTparms,
		unsigned int UARTmode);
void UartFIFOConfigure(unsigned int baseAdd);

int init = 0;

void driver (int* datain, int* dataout)
{
	if (init == 0)
	{
		unsigned int UARTparms = 0;
		UartFIFOConfigure(serial_mem);
		UARTparms = 0x3; // p4033 - 8 data bits, 1 stop bit, no parity
		UARTsetup(serial_mem, 115200, UARTparms, UART16x_OPER_MODE);
		init = 1;
	}

        //              user_ping_spg ((*data_sink) );
        //              sprintf(mybuf, "hello %d\n", (*data_sink % 10));
        //              UART_tx(devmem, mybuf, 7);
        //              UART_tx(devmem, "hello", 5);
        //UART_rx (devmem, mybuf, 10);
        //              printf ("UART= %s\n", mybuf);

        UART_rx (serial_mem, (unsigned char*)dataout, 1);
        UART_tx (serial_mem, (unsigned char*)datain, 1);
        printf ("[DRIVER] Sending %d\n", *dataout);
        printf ("[DRIVER] Receiving %d\n", *datain);
}
