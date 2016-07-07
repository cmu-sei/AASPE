#include <camkes.h>

#include <platsupport/serial.h>


#include <generatedtypes.h>
#include <stdio.h>
#include <string.h>

#define RHR         0x00
#define THR         0x00
#define IER         0x04
#define LSR         0x14
#define RHR_MASK    MASK(8)
#define IER_RHRIT   BIT(0)
#define LSR_TXFIFOE BIT(5)
#define LSR_RXFIFOE BIT(0)

#define UTHR 0x00 /* UART Transmit Holding Register */
#define ULSR 0x14 /* UART Line Status Register */
#define ULSR_THRE 0x20 /* Transmit Holding Register Empty */


unsigned int devmem;

#define UART_REG(x)     ((volatile uint32_t *)((devmem) + (x)))


#define UART_SYSC (0x54)
#define UART_SYSC_SOFTRESET (0x00000002u)
#define UART_SYSS (0x58)
#define UART_SYSS_RESETDONE (0x00000001u)
#define UART_DLL (0x0)
#define UART_RHR (0x0)
#define UART_THR (0x0)
#define UART_DLH (0x4)
#define UART_IER (0x4)
#define UART_EFR (0x8)
#define UART_FCR (0x8)
#define UART_IIR (0x8)
#define UART_LCR (0xC)
#define UART_MCR (0x10)
#define UART_LSR (0x14)
#define UART_TLR (0x1C)
#define UART_MDR1 (0x20)
#define UART_SCR (0x40)
#define UART_REG_CONFIG_MODE_A (0x0080)
#define UART_REG_CONFIG_MODE_B (0x00BF)
#define UART_REG_OPERATIONAL_MODE (0x007F)
#define UART_EFR_ENHANCED_EN (0x00000010u)
#define UART_MCR_TCR_TLR (0x00000040u)
#define UART_FCR_FIFO_EN (0x00000001u)
#define UART_TRIG_LVL_GRANULARITY_1 (0x0001)
#define UART_SCR_RX_TRIG_GRANU1 (0x00000080u)
#define UART_TLR_RX_FIFO_TRIG_DMA (0x000000F0u)
#define UART_TLR_RX_FIFO_TRIG_DMA_SHIFT (0x00000004u)
#define UART_TLR_TX_FIFO_TRIG_DMA (0x0000000Fu)
#define UART_TLR_TX_FIFO_TRIG_DMA_SHIFT (0x00000000u)
#define UART_FCR_RX_FIFO_TRIG (0x000000C0u)
#define UART_FCR_RX_FIFO_TRIG_SHIFT (0x00000006u)
#define UART_FCR_RX_FIFO_TRIG_16CHAR (0x1u)
#define UART_FCR_RX_FIFO_TRIG_56CHAR (0x2u)
#define UART_FCR_RX_FIFO_TRIG_60CHAR (0x3u)
#define UART_FCR_RX_FIFO_TRIG_8CHAR (0x0u)
#define UART_FCR_RX_TRIG_LVL_8 (UART_FCR_RX_FIFO_TRIG_8CHAR << \
		UART_FCR_RX_FIFO_TRIG_SHIFT)
#define UART_FCR_RX_TRIG_LVL_16 (UART_FCR_RX_FIFO_TRIG_16CHAR << \
		UART_FCR_RX_FIFO_TRIG_SHIFT)
#define UART_FCR_RX_TRIG_LVL_56 (UART_FCR_RX_FIFO_TRIG_56CHAR << \
		UART_FCR_RX_FIFO_TRIG_SHIFT)
#define UART_FCR_RX_TRIG_LVL_60 (UART_FCR_RX_FIFO_TRIG_60CHAR << \
		UART_FCR_RX_FIFO_TRIG_SHIFT)
#define UART_FCR_TX_TRIG_LVL_8 (UART_FCR_TX_FIFO_TRIG_8SPACES << \
		UART_FCR_TX_FIFO_TRIG_SHIFT)
#define UART_FCR_TX_TRIG_LVL_16 (UART_FCR_TX_FIFO_TRIG_16SPACES << \
		UART_FCR_TX_FIFO_TRIG_SHIFT)
#define UART_FCR_TX_TRIG_LVL_32 (UART_FCR_TX_FIFO_TRIG_32SPACES << \
		UART_FCR_TX_FIFO_TRIG_SHIFT)
#define UART_FCR_TX_TRIG_LVL_56 (UART_FCR_TX_FIFO_TRIG_56SPACES << \
		UART_FCR_TX_FIFO_TRIG_SHIFT)
#define UART_FCR_TX_FIFO_TRIG (0x00000030u)
#define UART_FCR_TX_FIFO_TRIG_SHIFT (0x00000004u)
#define UART_FCR_TX_FIFO_TRIG_8SPACES (0x0u)
#define UART_FCR_TX_FIFO_TRIG_16SPACES (0x1u)
#define UART_FCR_TX_FIFO_TRIG_32SPACES (0x2u)
#define UART_FCR_TX_FIFO_TRIG_56SPACES (0x3u)
#define UART_DMA_EN_PATH_FCR (UART_SCR_DMA_MODE_CTL_FCR)
#define UART_FCR_DMA_MODE (0x00000008u)
#define UART_FCR_DMA_MODE_SHIFT (0x00000003u)
#define UART_SCR_DMA_MODE_CTL (0x00000001u)
#define UART_SCR_DMA_MODE_CTL_FCR (0x0u)
#define UART_SCR_DMA_MODE_2 (0x00000006u)
#define UART_SCR_DMA_MODE_2_SHIFT (0x00000001u)
#define UART_SCR_TX_TRIG_GRANU1 (0x00000040u)
#define UART_FCR_RX_FIFO_CLEAR_SHIFT (0x00000001u)
#define UART_FCR_TX_FIFO_CLEAR_SHIFT (0x00000002u)
#define UART_MDR1_MODE_SELECT (0x00000007u)
#define UART_MDR1_MODE_SELECT_UART13X (0x3u)
#define UART_MDR1_MODE_SELECT_UART16X (0x0u)
#define UART16x_OPER_MODE (UART_MDR1_MODE_SELECT_UART16X)
#define UART13x_OPER_MODE (UART_MDR1_MODE_SELECT_UART13X)
#define UART_MDR1_MODE_SELECT_DISABLED (0x7u)
#define UART_IER_SLEEP_MODE_IT (0x00000010u)
#define UART_IER_THR_IT (0x00000002u)
#define UART_IIR_IT_TYPE (0x0000003Eu)
#define UART_IIR_IT_TYPE_SHIFT (0x00000001u)
#define UART_IIR_IT_TYPE_RHRINT (0x2u)
#define UART_IIR_IT_TYPE_THRINT (0x1u)
#define UART_INTID_TX_THRES_REACH (UART_IIR_IT_TYPE_THRINT << UART_IIR_IT_TYPE_SHIFT)
#define UART_INTID_RX_THRES_REACH (UART_IIR_IT_TYPE_RHRINT << UART_IIR_IT_TYPE_SHIFT)
#define UART_INT_LINE_STAT (UART_IER_LINE_STS_IT)
#define UART_INT_THR (UART_IER_THR_IT)
#define UART_INT_RHR_CTI (UART_IER_RHR_IT)
#define UART_LSR_RX_FIFO_E (0x00000001u)
#define UART_LSR_TX_FIFO_E (0x00000020u)
#define UART_LSR_TX_SR_E (0x00000040u)
#define UART_IER_LINE_STS_IT (0x00000004u)
#define UART_IER_RHR_IT (0x00000001u)

//#include "hw_types.h"
#define HWREG(x) (*((volatile unsigned int *)(x)))
#define TRUE 1
#define FALSE 0
#define REG(x)(*((volatile uint32_t *)(x)))
#define UART_MODULE_INPUT_CLK (48000000)


unsigned int UART_tx(unsigned int baseAdd, unsigned char *txbuf, unsigned int nobytes);
unsigned int UART_tx_big(unsigned int baseAdd, unsigned char *TxBuf, unsigned int TxBufSize);


void UartFIFOConfigure(unsigned int baseAdd) {
	unsigned int txGra = 0x0; // tx trigger gran = 4
	unsigned int rxGra = 0x1; // rx trigger gran = 1
	unsigned int txTrig = 0x30; // tx trig lev = 8 (fifo size 64 - fifo space 56)
	unsigned int rxTrig = 0x1; // rx trig lev = 1
	unsigned int txClr = 0x1; // clear tx fifo
	unsigned int rxClr = 0x4; // clear rx fifo
	unsigned int dmaEnPath = 0x1; // dma enab thru scr
	unsigned int dmaMode = 0x0; // dma mode = 0 (dma disabled)
	unsigned int lcrRegValue = 0;
	unsigned int enhanFnBitVal = 0;
	unsigned int tcrTlrBitVal = 0;
	unsigned int tlrValue = 0;
	unsigned int fcrValue = 0;
	// see TRM - UART Programming Quick Start Procedure
	// retain mode value
	lcrRegValue = HWREG(baseAdd + UART_LCR);
	// switch to Register Configuration Mode B
	HWREG(baseAdd + UART_LCR) = (UART_REG_CONFIG_MODE_B);
	// retain ENHANCEDEN bit value
	enhanFnBitVal = (HWREG(baseAdd + UART_EFR) & UART_EFR_ENHANCED_EN);
	// set ENHANCEDEN bit - EFR[4] to 1
	HWREG(baseAdd + UART_EFR) |= UART_EFR_ENHANCED_EN;
	// switch to Register Configuration Mode A
	HWREG(baseAdd + UART_LCR) = (UART_REG_CONFIG_MODE_A);
	// retain TCR_TLR bit value - MCR[6]
	tcrTlrBitVal = (HWREG(baseAdd + UART_MCR) & UART_MCR_TCR_TLR);
	// set TCRTLR bit to 1
	HWREG(baseAdd + UART_MCR) |= (UART_MCR_TCR_TLR);
	// enable FIFO: fcr[0] = 1
	fcrValue |= UART_FCR_FIFO_EN;
	// set Receiver FIFO trigger level
	if(UART_TRIG_LVL_GRANULARITY_1 != rxGra) {
		// clear RXTRIGGRANU1 bit in SCR
		HWREG(baseAdd + UART_SCR) &= ~(UART_SCR_RX_TRIG_GRANU1);
		// clear RX_FIFO_TRIG_DMA field of TLR register
		HWREG(baseAdd + UART_TLR) &= ~(UART_TLR_RX_FIFO_TRIG_DMA);
		fcrValue &= ~(UART_FCR_RX_FIFO_TRIG);
		// check if 'rxTrig' matches with the RX Trigger level values in FCR
		if((UART_FCR_RX_TRIG_LVL_8 == rxTrig) ||
				(UART_FCR_RX_TRIG_LVL_16 == rxTrig) ||
				(UART_FCR_RX_TRIG_LVL_56 == rxTrig) ||
				(UART_FCR_RX_TRIG_LVL_60 == rxTrig)) {
			fcrValue |= (rxTrig & UART_FCR_RX_FIFO_TRIG);
		} else {
			// RX Trig level multiple of 4, set RX_FIFO_TRIG_DMA of TLR
			HWREG(baseAdd + UART_TLR) |= ((rxTrig << UART_TLR_RX_FIFO_TRIG_DMA_SHIFT) &
					UART_TLR_RX_FIFO_TRIG_DMA);
		}
	} else { // yes: rxGra = 0x1
		// 'rxTrig' contains the 6-bit RX Trigger level value
		rxTrig &= 0x003F;
		// collect bits rxTrig[5:2]
		tlrValue = (rxTrig & 0x003C) >> 2;
		// collect bits rxTrig[1:0] and write to 'fcrValue'
		fcrValue |= (rxTrig & 0x0003) << UART_FCR_RX_FIFO_TRIG_SHIFT;
		// set RX_TRIG_GRANU1 bit of SCR register
		HWREG(baseAdd + UART_SCR) |= UART_SCR_RX_TRIG_GRANU1;
		// program RX_FIFO_TRIG_DMA field of TLR register
		HWREG(baseAdd + UART_TLR) |= (tlrValue << UART_TLR_RX_FIFO_TRIG_DMA_SHIFT);
	}
	// set Tx FIFO trigger level
	if(UART_TRIG_LVL_GRANULARITY_1 != txGra) { // yes txgra = 0x0
		// clear TX_TRIG_GRANU1 bit in SCR
		HWREG(baseAdd + UART_SCR) &= ~(UART_SCR_TX_TRIG_GRANU1);
		// clear TX_FIFO_TRIG_DMA field of TLR register
		HWREG(baseAdd + UART_TLR) &= ~(UART_TLR_TX_FIFO_TRIG_DMA);
		fcrValue &= ~(UART_FCR_TX_FIFO_TRIG);
		// check if 'txTrig' matches with the TX Trigger level values in FCR
		if((UART_FCR_TX_TRIG_LVL_8 == (txTrig)) ||
				(UART_FCR_TX_TRIG_LVL_16 == (txTrig)) ||
				(UART_FCR_TX_TRIG_LVL_32 == (txTrig)) ||
				(UART_FCR_TX_TRIG_LVL_56 == (txTrig))) {
			fcrValue |= (txTrig & UART_FCR_TX_FIFO_TRIG);
		} else {
			// TX Trig level a multiple of 4, set TX_FIFO_TRIG_DMA of TLR
			HWREG(baseAdd + UART_TLR) |= ((txTrig << UART_TLR_TX_FIFO_TRIG_DMA_SHIFT) &
					UART_TLR_TX_FIFO_TRIG_DMA);
		}
	} else {
		// 'txTrig' has the 6-bit TX Trigger level value
		txTrig &= 0x003F;
		// collect bits txTrig[5:2]
		tlrValue = (txTrig & 0x003C) >> 2;
		// collect bits txTrig[1:0] and write to 'fcrValue'
		fcrValue |= (txTrig & 0x0003) << UART_FCR_TX_FIFO_TRIG_SHIFT;
		// Setting the TXTRIGGRANU1 bit of SCR register
		HWREG(baseAdd + UART_SCR) |= UART_SCR_TX_TRIG_GRANU1;
		// program TX_FIFO_TRIG_DMA field of TLR register
		HWREG(baseAdd + UART_TLR) |= (tlrValue << UART_TLR_TX_FIFO_TRIG_DMA_SHIFT);
	}
	if(UART_DMA_EN_PATH_FCR == dmaEnPath) {
		// Configuring the UART DMA Mode through FCR register
		HWREG(baseAdd + UART_SCR) &= ~(UART_SCR_DMA_MODE_CTL);
		dmaMode &= 0x1;
		// clear bit corresponding to the DMA_MODE in 'fcrValue'
		fcrValue &= ~(UART_FCR_DMA_MODE);
		// set DMA Mode of operation
		fcrValue |= (dmaMode << UART_FCR_DMA_MODE_SHIFT);
	} else {
		dmaMode &= 0x3;
		// configure UART DMA Mode through SCR register
		HWREG(baseAdd + UART_SCR) |= UART_SCR_DMA_MODE_CTL;
		// clear DMAMODE2 field in SCR
		HWREG(baseAdd + UART_SCR) &= ~(UART_SCR_DMA_MODE_2);
		// program DMAMODE2 field in SCR
		HWREG(baseAdd + UART_SCR) |= (dmaMode << UART_SCR_DMA_MODE_2_SHIFT);
	}
	// program bits which clear the RX and TX FIFOs
	fcrValue |= (rxClr << UART_FCR_RX_FIFO_CLEAR_SHIFT);
	fcrValue |= (txClr << UART_FCR_TX_FIFO_CLEAR_SHIFT);
	// write fcrValue to FCR register
	HWREG(baseAdd + UART_FCR) = fcrValue;
	// switch to Register Configuration Mode B
	HWREG(baseAdd + UART_LCR) = (UART_REG_CONFIG_MODE_B);
	// restore ENHANCEDEN bit - EFR[4] to original value
	HWREG(baseAdd + UART_EFR) &= ~(UART_EFR_ENHANCED_EN);
	HWREG(baseAdd + UART_EFR) |= (enhanFnBitVal & UART_EFR_ENHANCED_EN);
	// switch to Register Configuration Mode A
	HWREG(baseAdd + UART_LCR) = (UART_REG_CONFIG_MODE_A);
	// restore original value of TCRTLR bit in MCR[6]
	HWREG(baseAdd + UART_MCR) &= ~(UART_MCR_TCR_TLR);
	HWREG(baseAdd + UART_MCR) |= (tcrTlrBitVal & UART_MCR_TCR_TLR);
	// restore LCR to original value
	HWREG(baseAdd + UART_LCR) = lcrRegValue;
}

void UARTsetup(unsigned int baseAdd,
		unsigned int baud_rate,
		unsigned int UARTparms,
		unsigned int UARTmode) {
	unsigned int divisorValue = 0;
	unsigned int enhanFnBitVal = 0;
	unsigned int sleepMdBitVal = 0;
	// compute Divisor Value
	UARTmode &= UART_MDR1_MODE_SELECT;
	switch(UARTmode) { // see TRM spruh73j p3983
		case UART16x_OPER_MODE:
			divisorValue = (UART_MODULE_INPUT_CLK)/(16 * baud_rate);
			break;
		case UART13x_OPER_MODE:
			divisorValue = (UART_MODULE_INPUT_CLK)/(13 * baud_rate);
			break;
		default:
			divisorValue = (UART_MODULE_INPUT_CLK)/(16 * baud_rate);
			break;
	}
	// disable UART...
	// clear MODESELECT field in MDR1
	HWREG(baseAdd + UART_MDR1) &= ~(UART_MDR1_MODE_SELECT);
	// set MODESELECT field in MDR1 to DISABLED mode = 0x7 & 0x7
	HWREG(baseAdd + UART_MDR1) |= (UART_MDR1_MODE_SELECT_DISABLED & UART_MDR1_MODE_SELECT);
	// switch to Register Configuration Mode B = 0xBF
	HWREG(baseAdd + UART_LCR) = (UART_REG_CONFIG_MODE_B);
	// retain ENHANCEDEN bit value
	enhanFnBitVal = (HWREG(baseAdd + UART_EFR) & UART_EFR_ENHANCED_EN);
	// set ENHANCEDEN bit - EFR[4] to 1
	HWREG(baseAdd + UART_EFR) |= UART_EFR_ENHANCED_EN;
	// switch to Register operation mode = 0x7F
	HWREG(baseAdd + UART_LCR) = (UART_REG_OPERATIONAL_MODE);
	// retain current value of IER[4] (SLEEPMODE bit) and clear it
	sleepMdBitVal = HWREG(baseAdd + UART_IER) & UART_IER_SLEEP_MODE_IT;
	HWREG(baseAdd + UART_IER) &= ~(UART_IER_SLEEP_MODE_IT);
	// switch to Register Configuration Mode B = 0xBF
	HWREG(baseAdd + UART_LCR) = (UART_REG_CONFIG_MODE_B);
	// write to Divisor Latch Low(DLL) register
	HWREG(baseAdd + UART_DLL) = (divisorValue & 0x00FF);
	// write to Divisor Latch High(DLH) register
	HWREG(baseAdd + UART_DLH) = ((divisorValue & 0x3F00) >> 8);
	// switch to Register operation mode = 0x7F
	HWREG(baseAdd + UART_LCR) = (UART_REG_OPERATIONAL_MODE);
	// restore value of IER[4] (SLEEPMODE bit) to original
	HWREG(baseAdd + UART_IER) |= sleepMdBitVal;
	// switch to Register Configuration Mode B
	HWREG(baseAdd + UART_LCR) = (UART_REG_CONFIG_MODE_B);
	// restore value of EFR[4] to original value
	HWREG(baseAdd + UART_EFR) &= ~(UART_EFR_ENHANCED_EN);
	HWREG(baseAdd + UART_EFR) |= enhanFnBitVal;
	// set value of LCR Register
	HWREG(baseAdd + UART_LCR) = UARTparms;
	// clear MODESELECT field in MDR1
	HWREG(baseAdd + UART_MDR1) &= ~(UART_MDR1_MODE_SELECT);
	// enable UART...
	// set MODESELECT field in MDR1 to UART Operating Mode
	HWREG(baseAdd + UART_MDR1) |= (UARTmode & UART_MDR1_MODE_SELECT);
	return;
}

unsigned int UART_tx(unsigned int baseAdd, unsigned char *txbuf, unsigned int nobytes) {
	unsigned int i = 0;
	if(nobytes > 64) nobytes = 64;
	if(HWREG(baseAdd + UART_LSR) & (UART_LSR_TX_SR_E | UART_LSR_TX_FIFO_E)) {
		for(i = 0; i < nobytes; i++) {
			HWREG(baseAdd + UART_THR) = *txbuf++; // write to THR
		}
	}
	return i;
}

unsigned int UART_tx_big(unsigned int baseAdd, unsigned char *TxBuf, unsigned int TxBufSize) {
	unsigned int TxCount = 0;
	while(TxCount < TxBufSize) { // test if transmission is complete.
		TxCount += UART_tx(baseAdd, &TxBuf[TxCount], TxBufSize);
	}
	return 0;
}

int uart_getchar(unsigned int baseAdd)
{
    int ch = EOF;

    if (HWREG(baseAdd + UART_LSR) & LSR_RXFIFOE) {
        ch = HWREG(baseAdd + RHR) & RHR_MASK;
    }
    return ch;
}

void UART_rx (unsigned int baseAdd, unsigned char* buf, unsigned int size)
{

	unsigned int tmp;
	int val;
	tmp = 0;

	for (int i = 0 ; i < size ; i++)
	{
		buf[i] = 0;
	}


	while ( (tmp < size) && ((val = uart_getchar(baseAdd)) != -1 ))
	{
		buf[tmp] = (char) val;
		tmp++;
	}
}


//int run(void)
//{
//	devmem = (unsigned int) uartmem;
//	unsigned int UARTparms = 0;
//	char mybuf[10];
//	int val;
//
//	memset (mybuf, 0, 10);
//	UartFIFOConfigure(devmem);
//	UARTparms = 0x3; // p4033 - 8 data bits, 1 stop bit, no parity
//	UARTsetup(devmem, 115200, UARTparms, UART16x_OPER_MODE);
//
//	while (1)
//	{
//
//		activator_wait();
//		user_ping_spg ((*data_sink) );
//		sprintf(mybuf, "hello %d\n", (*data_sink % 10));
//		UART_tx(devmem, mybuf, 7);
//		UART_tx(devmem, "hello", 5);
//UART_rx (devmem, mybuf, 10);
//		printf ("UART= %s\n", mybuf);
//
//	}
//	return 0;
//}


