package dev.dashboard.util;

public class PrintSysOutImp implements PrinterFace {
	
	
	public PrintSysOutImp (final String msg) {
		printMessage(msg);
		
	}
	@Override
	public void printMessage(final String msg) {
 		System.out.println(msg);
	}

}
