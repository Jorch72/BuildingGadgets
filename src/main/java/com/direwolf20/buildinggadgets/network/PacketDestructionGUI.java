package com.direwolf20.buildinggadgets.network;

import com.direwolf20.buildinggadgets.items.DestructionTool;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDestructionGUI implements IMessage {

    int left, right, up, down, depth;

    @Override
    public void fromBytes(ByteBuf buf) {
        left = buf.readInt();
        right = buf.readInt();
        up = buf.readInt();
        down = buf.readInt();
        depth = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(left);
        buf.writeInt(right);
        buf.writeInt(up);
        buf.writeInt(down);
        buf.writeInt(depth);
    }

    public PacketDestructionGUI() {

    }

    public PacketDestructionGUI(int l, int r, int u, int d, int dep) {
        left = l;
        right = r;
        up = u;
        down = d;
        depth = dep;
    }

    public static class Handler implements IMessageHandler<PacketDestructionGUI, IMessage> {
        @Override
        public IMessage onMessage(PacketDestructionGUI message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketDestructionGUI message, MessageContext ctx) {
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            ItemStack heldItem = playerEntity.getHeldItem(EnumHand.MAIN_HAND);
            if (!(heldItem.getItem() instanceof DestructionTool)) {
                heldItem = playerEntity.getHeldItemOffhand();
                if (!(heldItem.getItem() instanceof DestructionTool)) {
                    return;
                }
            }
            DestructionTool.setToolValue(heldItem, message.left, "left");
            DestructionTool.setToolValue(heldItem, message.right, "right");
            DestructionTool.setToolValue(heldItem, message.up, "up");
            DestructionTool.setToolValue(heldItem, message.down, "down");
            DestructionTool.setToolValue(heldItem, message.depth, "depth");
        }
    }
}