package com.gam.nocr.ems.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class NistParser {
	private static char FS = (char) 28;
	private static char GS = (char) 29;

	// It's just for TEST. The default value should be FALSE.
	// to test set it TRUE.
	private static boolean sizeIsChangable = true;

	public static NistResult parseNistData(byte[] nistData) {
		try {

			byte[] nistBuff = nistData;

			// Generateing the new file with null data
			return generateNullDataNIST(nistBuff);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	private static String getAmpFingers(Amp[] ampFings) {

		String ampFingers = "";
		try {

			for (int i = 0; i < 10; i++) {
				if (ampFings[i] != null && ampFings[i].isAmp()) {
					ampFingers += "0";
				} else
					ampFingers += "1";
			}
			if (ampFingers.length() < 10)
				return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ampFingers;

	}

	private static int[] setImagesSize(byte[] nistBuff) {
		int[] imageSizes = new int[13];

		for (int i = 0; i < 13; i++)
			imageSizes[i] = 0;

		ByteArrayInputStream inStream = new ByteArrayInputStream(nistBuff, 0,
				nistBuff.length);

		String[] fieldID = null;
		String[] fieldVal = null;
		// Read Header Type 1 , 2
		while (true) {
			fieldID = readFieldIDFromBuff(inStream);
			if (fieldID[0].toString().equals("1")
					|| fieldID[0].toString().equals("2")) {
				if (!convertToString(fieldID).equals("1.015")
						&& !convertToString(fieldID).equals("2.202")) {
					fieldVal = readFieldValueFromBuff(inStream, (byte) GS);
					if (convertToString(fieldID).equals("2.002")
							&& fieldVal.length > 2) {
						int available = inStream.available();
						int start = nistBuff.length - available
								- (fieldVal.length + 1);
						inStream = new ByteArrayInputStream(nistBuff, start,
								nistBuff.length);
						fieldVal = readFieldValueFromBuff(inStream, (byte) FS);
					}
				} else
					fieldVal = readFieldValueFromBuff(inStream, (byte) FS);
				if (convertToString(fieldID).substring(0, 2).equals("14")) {
					break;
				}
			} else
				break;

		}

		int FingerIndex = 1;
		int FingerDataLen = 1;
		byte[] FinegrImage = null;

		while (convertToString(fieldID).substring(0, 2).equals("14")) {
			if (convertToString(fieldID).equals("14.001")) {
				int available = inStream.available();
				int position = nistBuff.length - available;
				FingerDataLen = (Integer.parseInt(convertToString(fieldVal)))
						- fieldID.length - fieldVal.length - 3 + position;
			}
			if (convertToString(fieldID).equals("14.999")) {
				FingerIndex++;
			}

			// check for EOF
			if (inStream.available() == 0)
				break;
			// Read New Field IDav
			fieldID = readFieldIDFromBuff(inStream);

			// Read Field value according to Field ID
			if (!convertToString(fieldID).equals("14.999"))
				fieldVal = readFieldValueFromBuff(inStream, (byte) GS);
			else {
				// / if FieldID == "14.999"
				// / we should read the image data
				int available = inStream.available();
				int currentPosition = (nistBuff.length) - (available);
				FingerDataLen -= currentPosition;
				if (FingerDataLen > 0) {
					FinegrImage = new byte[FingerDataLen];
					inStream.read(FinegrImage, 0, FingerDataLen);
					imageSizes[FingerIndex - 1] = FingerDataLen;
				} else
					FinegrImage = null;
				// After image data the ending code FS is located.
				fieldVal = readFieldValueFromBuff(inStream, (byte) FS);
			}
		}
		try {
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return imageSizes;
	}

	private static NistResult generateNullDataNIST(byte[] nistBuff) {
		Amp[] ampFings = new Amp[10];
		for (int i = 0; i < 10; i++) {

			Amp amp = new Amp();
			ampFings[i] = amp;
		}

		int j = 0;
		int[] imageSizes = null;
		if (sizeIsChangable)// TODO: for test just
			imageSizes = setImagesSize(nistBuff);
		ByteArrayInputStream input = new ByteArrayInputStream(nistBuff, 0,
				nistBuff.length);
		ByteArrayOutputStream newStream = new ByteArrayOutputStream();
//		byte[] newStreamByte = new byte[nistBuff.length];
		byte[] newStreamByte = null;
		String[] fieldID = null;
		String[] fieldVal = null;
		// Read Header Type 1 , 2
		while (true) {
			fieldID = readFieldIDFromBuff(input);
			if (fieldID[0].equals("1") || fieldID[0].equals("2")) {

				if (!convertToString(fieldID).equals("1.015")
						&& !convertToString(fieldID).equals("2.202")) {
					fieldVal = readFieldValueFromBuff(input, (byte) GS);
					if (convertToString(fieldID).equals("2.002")
							&& fieldVal.length > 2) {
						int available = input.available();
						int start = nistBuff.length - available
								- (fieldVal.length + 1);
						input = new ByteArrayInputStream(nistBuff, start,
								nistBuff.length);
						fieldVal = readFieldValueFromBuff(input, (byte) FS);
					}
				} else {
					fieldVal = readFieldValueFromBuff(input, (byte) FS);
					if (convertToString(fieldID).equals("2.202")) {
						String[] fieldVal2 = fieldVal;
						String scanner = fieldVal2.toString();
					}

				}
				if (convertToString(fieldID).substring(0, 2).equals("14")) {
					break;
				}
			} else
				break;

		}
		int fingerIndex = 1;
		int fingerDataLen = 1;
		int lastPosition = 0;
		byte[] finegrImage = null;

		while (convertToString(fieldID).substring(0, 2).equals("14")) {
			if (convertToString(fieldID).equals("14.001")) {
				{
					if (sizeIsChangable)// TODO: just for test
					{
						String newsize = String.format("%0" + fieldVal.length
								+ "d",
								(Integer.parseInt(convertToString(fieldVal)))
										- imageSizes[fingerIndex - 1]);
						char[] charArray = newsize.toCharArray();
						int available = input.available();
						int currentPosition = (nistBuff.length) - (available);

						int position = currentPosition - (fieldVal.length) - 1;

						for (int i = 0; i < fieldVal.length; i++) {
							nistBuff[position] = ((byte) charArray[i]);
							position++;
						}
						nistBuff[position] = (byte) GS;
					}
					int available = input.available();
					int currentPosition = (nistBuff.length) - (available);
					fingerDataLen = (Integer
							.parseInt(convertToString(fieldVal)))
							- fieldID.length
							- fieldVal.length
							- 3
							+ currentPosition;
				}

			}
			if (convertToString(fieldID).equals("14.999")) {
				fingerIndex++;
			}
			// check for EOF
			if (input.available() == 0)
				break;
			// Read New Field ID
			fieldID = readFieldIDFromBuff(input);
			// Read Field value according to Field ID
			if (!convertToString(fieldID).equals("14.999")) {
				fieldVal = readFieldValueFromBuff(input, (byte) GS);
				if (convertToString(fieldID).equals("14.018")
						&& fingerIndex < 11)
					ampFings[fingerIndex - 1].setAmp(true);

				if (convertToString(fieldID).equals("14.020")
						&& ampFings[fingerIndex - 1].isAmp())
					ampFings[fingerIndex - 1]
							.setComment(convertToString(fieldVal));

			} else {
				// / if FieldID == "14.999"
				// / we should read the image data
				int currentPosition1 = (nistBuff.length) - (input.available());
				// FingerDataLen -= input.read();
				fingerDataLen -= currentPosition1;

//				if (newStream.size() == 0) {
				if(newStreamByte==null) {
					
				newStreamByte=new byte[nistBuff.length];
					int length = newStreamByte.length;
					for (j = 0; j < currentPosition1; j++) {
						newStreamByte[j] = nistBuff[j];
					}
//					newStream.write(nistBuff, 0, currentPosition1);
					lastPosition = currentPosition1 + fingerDataLen;
				} else {
					int currentPosition2 = (nistBuff.length)
							- (input.available());

					ByteArrayOutputStream temp = new ByteArrayOutputStream(
							currentPosition2 - lastPosition);
					temp.write(nistBuff, lastPosition, currentPosition2
							- lastPosition);
					lastPosition = currentPosition2 + fingerDataLen;
//					newStream.write(temp.toByteArray(), 0, temp.size());
					byte[] tempArray = temp.toByteArray();
					for (int i = 0; i < temp.size(); i++) {
						newStreamByte[j] = tempArray[i];
						j++;
					}

				}

				if (fingerDataLen > 0) {

					finegrImage = new byte[fingerDataLen];
					input.read(finegrImage, 0, fingerDataLen);

				}
				// else
				// FinegrImage = null;
				// After image data the ending code FS is located.
				fieldVal = readFieldValueFromBuff(input, (byte) FS);
			}
		}
		ByteArrayOutputStream outMSTest = new ByteArrayOutputStream();
		outMSTest.write(newStreamByte, 0, j);
//		outMSTest.write(newStream.toByteArray(), 0, j);
		byte[] OutputNISTBuffTest = outMSTest.toByteArray();
		try {
			input.close();
			newStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new NistResult(OutputNISTBuffTest, getAmpFingers(ampFings));
	}

	private static String convertToString(String[] fieldID) {
		return Arrays.toString(fieldID).replaceAll(" ", "").replaceAll(",", "")
				.replace("]", "").replace("[", "");
	}

	private static String[] readFieldIDFromBuff(ByteArrayInputStream inStream) {
		char endChar = ':';
		return readFieldValueFromBuff(inStream, (byte) endChar);

	}

	private static String[] readFieldValueFromBuff(
			ByteArrayInputStream inStream, byte EndingChar /* GS */) {
		ArrayList<String> res = new ArrayList<String>();

		char mb;
		while (true) {
			mb = (char) inStream.read();
			if (mb == EndingChar)
				return res.toArray(new String[res.size()]);
			String str = "" + mb;
			res.add(str);
		}
	}

}

class Amp {
	private boolean isAmp;
	private String comment;

	public boolean isAmp() {
		return isAmp;
	}

	public void setAmp(boolean isAmp) {
		this.isAmp = isAmp;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
