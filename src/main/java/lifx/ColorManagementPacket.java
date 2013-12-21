package lifx;

import java.awt.Color;
import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ColorManagementPacket extends Packet {
	private static final Log LOG = LogFactory.getLog(ColorManagementPacket.class);
	private final Color color; 
	
	public ColorManagementPacket(byte[] targetMacAddress, Color color) {
        super(targetMacAddress, new byte[] {0x66, 0x00});
        this.color = color;
    }

    @Override
    public byte[] toByteArray() {
        byte[] result = new byte[] {};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(super.toByteArray());
            outputStream.write(0x00);
            outputStream.write(0x00);
            outputStream.write(colorToBytes(color));
            outputStream.write(0xFF);
            outputStream.write(0xFF);
            outputStream.write(0xFF);
            outputStream.write(0xFF);
            outputStream.write(0x00);
            outputStream.write(0x00);
            outputStream.write(0x00);
            outputStream.write(0x00);
            outputStream.write(0x00);
            outputStream.write(0x00);
            result = outputStream.toByteArray();
            outputStream.close();
            result[0] = 0x32;
        } catch (IOException e) {
            LOG.error("Error building packet");
        }
        return result;
    }
    
    private byte[] colorToBytes(Color color) {
    	int byteValue = 0;
    	byteValue = (color.getRed() & 0x1f) << 11;
        byteValue |= (color.getGreen() & 0x3f) << 5;
        byteValue |= (color.getBlue() & 0x1f) << 0;
    	byte[] result = new byte[2];
    	result[0] = (byte)(byteValue >> 8);
    	result[1] = (byte)byteValue;
    	return result;
    }

}