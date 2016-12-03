package Model;

/**
 * This classes job is it to read and write the header. The functions are Copied
 * from "AndroidGroupe A""
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

	public short extractIdFromHeader(byte[] header) {
		return (short) ((header[6] & ((1 << 6) - 1) << 8) | header[7]);
	}

	public ContentType extractContentTypeFromHeader(byte[] header) {
		return ContentType.values()[header[6] & (1 << 7)];
	}

	public int extractLengthFromHeader(byte[] header) {
		return (header[0] << 24) | (header[1] << 16) | (header[2] << 8) | header[3];
	}

	public short extractChecksumFromHeader(byte[] header) {
		return (short) ((header[4] << 8) | header[5]);
	}

	public boolean extractFaultyBitFromHeader(byte[] header) {
		int faultyBit = 1 << 6;
		return (header[6] & faultyBit) == faultyBit;
	}

	public boolean isChecksumCorrect(final byte[] message, final short checksum) {
		return createChecksum(message) == checksum;
	}
}
