package net.sf.cram.encoding;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;


import net.sf.cram.EncodingID;
import net.sf.cram.EncodingParams;
import net.sf.cram.io.ByteBufferUtils;
import net.sf.cram.io.ExposedByteArrayOutputStream;

public class GolombLongEncoding implements Encoding<Long> {
	public static final EncodingID ENCODING_ID = EncodingID.GOLOMB;
	private int m;
	private int offset;

	public GolombLongEncoding() {
	}

	@Override
	public EncodingID id() {
		return ENCODING_ID;
	}

	public static EncodingParams toParam(int offset, int m) {
		GolombLongEncoding e = new GolombLongEncoding();
		e.offset = offset;
		e.m = m;
		return new EncodingParams(ENCODING_ID, e.toByteArray());
	}

	@Override
	public byte[] toByteArray() {
		ByteBuffer buf = ByteBuffer.allocate(10);
		ByteBufferUtils.writeUnsignedITF8(offset, buf);
		ByteBufferUtils.writeUnsignedITF8(m, buf);
		buf.flip();
		byte[] array = new byte[buf.limit()];
		buf.get(array);
		return array;
	}

	@Override
	public void fromByteArray(byte[] data) {
		ByteBuffer buf = ByteBuffer.wrap(data);
		offset = ByteBufferUtils.readUnsignedITF8(buf);
		m = ByteBufferUtils.readUnsignedITF8(buf);
	}

	@Override
	public BitCodec<Long> buildCodec(Map<Integer, InputStream> inputMap,
			Map<Integer, ExposedByteArrayOutputStream> outputMap) {
		return new GolombLongCodec(offset, m, true);
	}

}
