package com.j0keer.minigames.client.mixin;

import com.j0keer.minigames.client.utils.ChatActions;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    public void inject(Text message, CallbackInfo ci){
        String text = message.getString();
        boolean cancel = ChatActions.onChat(text);
        if (cancel) {
            ci.cancel();
        }
    }

}
