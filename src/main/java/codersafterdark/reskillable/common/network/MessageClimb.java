package codersafterdark.reskillable.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageClimb implements IMessage {

    private byte[] data;

    public MessageClimb() {
        this(new byte[]{0});
    }

    public MessageClimb(ByteBuf dataToSet) {
        this(dataToSet.array());
    }

    public MessageClimb(byte[] dataToSet) {
        if (dataToSet.length > 2097136) {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
		this.data = dataToSet;
    }

    @Override
	public void toBytes(ByteBuf buffer) {
        if (this.data.length > 2097136) {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
		buffer.writeShort(this.data.length);
		buffer.writeBytes(this.data);
    }

    @Override
	public void fromBytes(ByteBuf buffer) {
        this.data = new byte[buffer.readShort()];
        buffer.readBytes(this.data);
    }

    public byte[] getData() {
        return this.data;
    }

}
