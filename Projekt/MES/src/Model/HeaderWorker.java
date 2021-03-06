package Model;

import Model.HeaderWorker.ContentType;

/**
 * This classes job is it to read and write the header. The functions are Copied
 * from "AndroidGruppe A"
 * 
 * @author Niki+Nico
 *
 */

public class HeaderWorker {
	enum ContentType {
		STRING, RAW
	}

	public byte[] makeHeader(final byte[] payload, final ContentType contentType, final short id,
			final boolean lastMessageFaulty) {
		final int length = payload.length;

		final byte[] bytes = new byte[8];

		final short checksum = createChecksum(payload);

		short flagsAndId = id;
		flagsAndId |= (contentType.ordinal() << 15);
		if (lastMessageFaulty) {
			flagsAndId |= (1 << 14);
		}

		bytes[0] = (byte) (length >> 24);
		bytes[1] = (byte) (length >> 16);
		bytes[2] = (byte) (length >> 8);
		bytes[3] = (byte) length;

		bytes[4] = (byte) (checksum >> 8);
		bytes[5] = (byte) checksum;

		bytes[6] = (byte) (flagsAndId >> 8);
		bytes[7] = (byte) flagsAndId;
		return bytes;
	}

	private short createChecksum(final byte[] message) {
		short checksum = 0;

		for (byte b : message) {
			checksum += b; // the modulo is implicit
		}

		return checksum;
	}

	public boolean isChecksumCorrect(final byte[] message, final short checksum) {
		return createChecksum(message) == checksum;
	}

	private ContentType extractContentTypeFromHeader(byte[] header) {
		return ContentType.values()[(byteToInt(header[6]) & (1 << 7)) >> 7];
	}

	private int extractLengthFromHeader(byte[] header) {
		return (byteToInt(header[0]) << 24) | (byteToInt(header[1]) << 16) | (byteToInt(header[2]) << 8)
				| byteToInt(header[3]);
	}

	private short extractChecksumFromHeader(byte[] header) {
		return (short) ((byteToInt(header[4]) << 8) | byteToInt(header[5]));
	}

	private short extractIdFromHeader(byte[] header) {
		return (short) ((Byte.toUnsignedInt(header[6]) & 0b111111) << 8 | Byte.toUnsignedInt(header[7]));
	}

	private boolean extractFaultyBitFromHeader(byte[] header) {
		int faultyBit = 1 << 6;
		return (byteToInt(header[6]) & faultyBit) == faultyBit;
	}

	private int byteToInt(byte b) {
		return (b) & 0xFF;
	}

	/**
	 * Becomes an Byte-Array which contains the header. This header is read and
	 * saved in a "HeaderStorage" object. This object saves every values.
	 * 
	 * @param headerReceivedBytes
	 * @return headerValues
	 */
	public HeaderStorage getHeaderStorageObject(byte[] headerReceivedBytes) {
		HeaderStorage newhdrstor = new HeaderStorage();
		newhdrstor.checkSum = extractChecksumFromHeader(headerReceivedBytes);
		newhdrstor.length = extractLengthFromHeader(headerReceivedBytes);
		newhdrstor.faultyBit = extractFaultyBitFromHeader(headerReceivedBytes);
		newhdrstor.id = extractIdFromHeader(headerReceivedBytes);
		newhdrstor.contentType = extractContentTypeFromHeader(headerReceivedBytes);
		return newhdrstor;
	}

}

/**
 * Contains the Header attributes used to save and read the values.
 * 
 * @author niki
 *
 */
class HeaderStorage {
	public int length;
	public short checkSum;
	public short id;
	public boolean faultyBit;
	public ContentType contentType; // TODO
}
