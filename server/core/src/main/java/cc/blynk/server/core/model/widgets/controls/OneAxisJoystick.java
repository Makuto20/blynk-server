package cc.blynk.server.core.model.widgets.controls;

import cc.blynk.server.core.model.widgets.OnePinWidget;
import io.netty.channel.ChannelHandlerContext;

import static cc.blynk.server.core.protocol.enums.Command.HARDWARE;
import static cc.blynk.utils.BlynkByteBufUtil.makeStringMessage;

/**
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 21.03.15.
 */
public class OneAxisJoystick extends OnePinWidget implements HardwareSyncWidget {

    public boolean autoReturnOn;

    public boolean horizontal;

    @Override
    public void send(ChannelHandlerContext ctx, int msgId) {
        String body = makeHardwareBody();
        if (body != null) {
            ctx.write(makeStringMessage(HARDWARE, msgId, body), ctx.voidPromise());
        }
    }

    @Override
    public String getModeType() {
        return "out";
    }

    @Override
    public boolean isPWMSupported() {
        return pwmMode;
    }

    @Override
    public int getPrice() {
        return 200;
    }

}
