# barcode-scanner

This is the sample project for **Honeywell scanner YJ-HF500**. Because I couldn't find any manual in the Net, I decided to make this sample app.

For interacting with scanner from Java code you need:

1. Switch scanner to RS232-based COM-port mode (see device manual)
2. Install drivers for you device from www.honeywellaidc.com
3. Install **Honeywell_JavaPOS_Suite V.1.14.1.66** from https://hsmftp.honeywell.com/en (registration needed)
4. After that run **JavaPOS Configuration Utility** and add you scanner.
5. Run **JavaPOS Validation Utility** and check that your scanner works and can read data.
6. Go to `C:\Program Files\Honeywell\UPOS Suite\JavaPOS Suite` . You need these files here:
- HWHydraSO.jar
- jpos114.jar
- xerces.jar
- log4j-1.2.15.jar
- log4j.properties
- jniImgrSdk.dll
- res/jpos.xml
- jpos.properties

Also you can install **Intermec Scan Engine SDK (Version 1.6)**. If you do so, fo to 

`C:\Program Files (x86)\Intermec\Intermec Scan Engine SDK (Version 1.6)\UPOS 1.13 (OPOS & JavaPOS) Drivers for POS systems\JPOS\service\sample
`

Here you can find ScannerSample.java - the sample GUI app. It can be helpful.
 