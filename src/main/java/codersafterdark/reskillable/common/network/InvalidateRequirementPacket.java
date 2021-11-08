package codersafterdark.reskillable.common.network;

import java.util.UUID;

import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InvalidateRequirementPacket implements IMessage, IMessageHandler<InvalidateRequirementPacket, IMessage> {
    private Class<? extends Requirement>[] cacheTypes;
    private UUID uuid;

    public InvalidateRequirementPacket() {

    }

    @SafeVarargs
	public InvalidateRequirementPacket(UUID uuid, Class<? extends Requirement>... cacheTypes) {
        this.uuid = uuid;
        this.cacheTypes = cacheTypes;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void fromBytes(ByteBuf buf) {
        this.uuid = new UUID(buf.readLong(), buf.readLong());
        this.cacheTypes = new Class[buf.readInt()];
        for (int i = 0; i < this.cacheTypes.length; i++) {
            try {
                Class<?> rClass = Class.forName(ByteBufUtils.readUTF8String(buf));
                if (Requirement.class.isAssignableFrom(rClass)) {
                    this.cacheTypes[i] = (Class<? extends Requirement>) rClass;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.uuid.getMostSignificantBits());
        buf.writeLong(this.uuid.getLeastSignificantBits());
        buf.writeInt(this.cacheTypes.length);
        for (Class<? extends Requirement> rClass : this.cacheTypes) {
            ByteBufUtils.writeUTF8String(buf, rClass.getName());
        }
    }

    @Override
    public IMessage onMessage(InvalidateRequirementPacket message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
        } else {
            Minecraft.getMinecraft().addScheduledTask(() -> handleMessage(message, ctx));
        }
        return null;
    }

    @SuppressWarnings("static-method")
	public IMessage handleMessage(InvalidateRequirementPacket message, MessageContext ctx) {
        if (message.cacheTypes.length == 0) {
            RequirementCache.getCache(message.uuid, ctx.side.isClient()).forceClear();
        } else {
            RequirementCache.invalidateCacheNoPacket(message.uuid, ctx.side.isClient(), message.cacheTypes);
        }
        return null;
    }
}