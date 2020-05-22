package ru.dkx86.scanner;

import com.honeywell.jpos.scanner.ScannerConsts;
import com.honeywell.jpos.scanner.ScannerDataEvent;
import jpos.JposConst;
import jpos.JposException;
import jpos.Scanner;
import jpos.events.*;

import java.nio.charset.StandardCharsets;

public final class BarcodeScanner {
    private final String serviceName;
    private final Scanner scanner;
    private final ScannerDataListener dataListener;
    private final ScannerDirectIOListener directIOListener;
    private final ScannerErrorListener errorListener;
    private boolean autoReady;

    /**
     * @param serviceName scanner's service name from jpos.xml
     */
    public BarcodeScanner(String serviceName) {
        this.serviceName = serviceName;
        scanner = new Scanner();
        dataListener = new ScannerDataListener();
        directIOListener = new ScannerDirectIOListener();
        errorListener = new ScannerErrorListener();
    }

    /**
     * Open scanner, claim it and set enabled.
     */
    public void initialize() {
        try {
            scanner.open(serviceName);
            scanner.claim(ScannerConsts.DEFAULT_CLAIM_INFINITE_TIMEOUT);
            scanner.setDeviceEnabled(true);

            scanner.addDataListener(dataListener);
            scanner.addDirectIOListener(directIOListener);
            scanner.addErrorListener(errorListener);
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    public void setAutoReady(boolean autoReady) {
        this.autoReady = autoReady;
    }

    /**
     * Set scanner ready to read data.
     */
    public void setReadyForReading() {
        if (!isInitialized()) {
            System.err.println("Scanner is not initialized");
            return;
        }

        try {
            scanner.setDataEventEnabled(true);
        } catch (JposException e) {
            e.printStackTrace();
        }
    }


    /**
     * Disable data events, disable scanner, release claim and close it.
     */
    public void shutdown() {
        try {
            if (scanner.getDataEventEnabled())
                scanner.setDataEventEnabled(false);
            if (scanner.getDeviceEnabled())
                scanner.setDeviceEnabled(false);
            if (scanner.getClaimed())
                scanner.release();
            scanner.removeDataListener(dataListener);
            scanner.removeDirectIOListener(directIOListener);
            scanner.removeErrorListener(errorListener);
            scanner.close();
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    private boolean isInitialized() {
        try {
            return scanner.getClaimed() && scanner.getDeviceEnabled();
        } catch (JposException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String jposErrToString(int errorCode) {
        String error = "UNKNOWN ERROR CODE";
        switch (errorCode) {
            case 101:
                error = "JPOS_E_CLOSED";
                break;
            case 102:
                error = "JPOS_E_CLAIMED";
                break;
            case 103:
                error = "POS_E_NOTCLAIMED";
                break;
            case 104:
                error = "JPOS_E_NOSERVICE";
                break;
            case 105:
                error = "JPOS_E_DISABLED";
                break;
            case 106:
                error = "JPOS_E_ILLEGAL";
                break;
            case 107:
                error = "JPOS_E_NOHARDWARE";
                break;
            case 108:
                error = "JPOS_E_OFFLINE";
                break;
            case 109:
                error = "JPOS_E_NOEXIST";
                break;
            case 110:
                error = "JPOS_E_EXISTS";
                break;
            case 111:
                error = "JPOS_E_FAILURE";
                break;
            case 112:
                error = "JPOS_E_TIMEOUT";
                break;
            case 113:
                error = "JPOS_E_BUSY";
                break;
            case 114:
                error = "JPOS_E_EXTENDED";
                break;
            case 115:
                error = "JPOS_E_DEPRECATED";
                break;
            default:
                break;
        }

        return error;
    }

    private String jposErrLocusToString(int errorLocus) {
        String errLocusStr = "UNKOWN ERROR LOCUS";

        switch (errorLocus) {
            case 1:
                errLocusStr = "JPOS_EL_OUTPUT";
                break;
            case 2:
                errLocusStr = "JPOS_EL_INPUT";
                break;
            case 3:
                errLocusStr = "JPOS_EL_INPUT_DATA";
                break;
            default:
                break;
        }

        return errLocusStr;
    }

    private static class ScannerDirectIOListener implements DirectIOListener {
        @Override
        public void directIOOccurred(DirectIOEvent directIOEvent) {
            //do nothing
        }
    }

    private final class ScannerDataListener implements DataListener {

        @Override
        public void dataOccurred(DataEvent dataEvent) {
            final byte[] data = ((ScannerDataEvent) dataEvent).data.scanLabel;
            if (data == null) {
                System.err.println("Scanned data is empty");
                return;
            }

            String readResult = new String(data, StandardCharsets.UTF_8);
            System.out.println("\n---> READ RESULT -> " + readResult);

            if (autoReady)
                BarcodeScanner.this.setReadyForReading();
        }
    }

    private class ScannerErrorListener implements ErrorListener {
        @Override
        public void errorOccurred(ErrorEvent errorEvent) {
            String data = "ErrorEvent: ErrorCode = " + jposErrToString(errorEvent.getErrorCode()) +
                    ", ErrorCodeExtended = " + errorEvent.getErrorCodeExtended() +
                    ", ErrorLocus = " + jposErrLocusToString(errorEvent.getErrorLocus()) +
                    ", ErrorResponse = " + errorEvent.getErrorResponse();
            System.err.println("ErrorEvent: ErrorCode = " + data);
            if (errorEvent.getErrorLocus() == JposConst.JPOS_EL_INPUT_DATA)
                errorEvent.setErrorResponse(JposConst.JPOS_ER_CLEAR);
            else
                errorEvent.setErrorResponse(JposConst.JPOS_ER_CONTINUEINPUT);
        }
    }
}
