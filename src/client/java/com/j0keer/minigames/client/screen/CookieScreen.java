package com.j0keer.minigames.client.screen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.j0keer.minigames.Constants;
import com.j0keer.minigames.enums.CookieType;
import com.j0keer.minigames.client.objects.cookie.CookieState;
import com.j0keer.minigames.client.objects.cookie.ParticleManager;
import com.j0keer.minigames.client.objects.screen.ScreenVibration;
import com.j0keer.minigames.config.ConfigFile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CookieScreen extends Screen {
    private final Map<String, Float> paintedPixels;
    private List<String> default_pixels;
    private boolean drawing = false;
    private final CookieType cookieType;
    private int fails = 0;
    private String lastKey = null;
    private final ScreenVibration screenVibration = new ScreenVibration();
    private final ParticleManager particleManager = new ParticleManager();
    private List<String> break_1;
    private List<String> break_2;
    private List<String> break_3;
    private final Map<String, Integer> break_colors;
    private CookieState state = CookieState.PLAYING;

    public CookieScreen(CookieType type) {
        super(Text.literal("cookie.game.screen"));
        this.cookieType = type;
        ConfigFile config = new ConfigFile("cookies.json");

        this.paintedPixels = new HashMap<>();
        this.break_colors = new HashMap<>();

        // 1. Cargar y parsear el JSON (desde resources del mod)
        JsonObject rootJson = config.getRoot();

        if (rootJson != null) {
            // 2. Cargar píxeles por defecto del tipo de galleta
            this.default_pixels = getListFromJson(rootJson, cookieType.name());
            for (String pixel : default_pixels) {
                paintedPixels.put(pixel, 1.0f);
            }

            // 3. Cargar las etapas de rotura
            this.break_1 = getListFromJson(rootJson, "break_1");
            this.break_2 = getListFromJson(rootJson, "break_2");
            this.break_3 = getListFromJson(rootJson, "break_3");

            // 4. Asignar los colores
            int color = 0xffa56021;
            applyBreakColors(break_1, color);
            applyBreakColors(break_2, color);
            applyBreakColors(break_3, color);
        }
    }

    private List<String> getListFromJson(JsonObject json, String key) {
        List<String> list = new ArrayList<>();
        if (json.has(key) && json.get(key).isJsonArray()) {
            JsonArray array = json.getAsJsonArray(key);
            for (JsonElement element : array) {
                list.add(element.getAsString());
            }
        }
        return list;
    }

    private void applyBreakColors(List<String> pixels, int color) {
        if (pixels != null) {
            for (String pixel : pixels) {
                break_colors.put(pixel, color);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        float vibrationX = screenVibration.getX();
        float vibrationY =  screenVibration.getY();

        int cookieWidth = (int) (Math.min(height, width) * 0.8);
        int cookieHeight = (int) (Math.min(height, width) * 0.8);
        Identifier cookie = cookieType.getNormal().getID();
        int cookieX = (int) (((float) width / 2) - ((float) cookieWidth / 2) + vibrationX);
        int cookieY = (int) (((float) height / 2) - ((float) cookieHeight / 2) + vibrationY);

        switch (state) {
            case PLAYING -> {
                context.drawTexture(cookie, cookieX, cookieY, 0, 0, cookieWidth, cookieHeight, cookieWidth, cookieHeight);

                float blockSizeX = (float) cookieWidth / 37.0f;
                float blockSizeY = (float) cookieHeight / 37.0f;

                for (Map.Entry<String, Float> entry : paintedPixels.entrySet()) {
                    String[] coords = entry.getKey().split(" ");
                    int pixelX = Integer.parseInt(coords[0]);
                    int pixelY = Integer.parseInt(coords[1]);
                    float opacity = entry.getValue();

                    float blockX = cookieX + pixelX * blockSizeX;
                    float blockY = cookieY + pixelY * blockSizeY;

                    float blockEndX = blockX + blockSizeX + 1;
                    float blockEndY = blockY + blockSizeY + 1;

                    int alpha = (int) (opacity * 255);
                    int color = (alpha << 24) | 0x7e3a09;
                    context.fill((int) blockX, (int) blockY, (int) blockEndX, (int) blockEndY, color);
                }

                if (fails > 0) {
                    for (String text : break_1) {
                        String[] coords = text.split(" ");
                        int pixelX = Integer.parseInt(coords[0]);
                        int pixelY = Integer.parseInt(coords[1]);

                        float blockX = cookieX + pixelX * blockSizeX;
                        float blockY = cookieY + pixelY * blockSizeY;

                        float blockEndX = blockX + blockSizeX + 1;
                        float blockEndY = blockY + blockSizeY + 1;

                        if (!default_pixels.contains(text))
                            context.fill((int) blockX, (int) blockY, (int) blockEndX, (int) blockEndY, break_colors.get(text));
                    }

                    if (fails > 4) {
                        for (String text : break_2) {
                            String[] coords = text.split(" ");
                            int pixelX = Integer.parseInt(coords[0]);
                            int pixelY = Integer.parseInt(coords[1]);

                            float blockX = cookieX + pixelX * blockSizeX;
                            float blockY = cookieY + pixelY * blockSizeY;

                            float blockEndX = blockX + blockSizeX + 1;
                            float blockEndY = blockY + blockSizeY + 1;

                            if (!default_pixels.contains(text))
                                context.fill((int) blockX, (int) blockY, (int) blockEndX, (int) blockEndY, break_colors.get(text));
                        }
                    }

                    if (fails > 8) {
                        for (String text : break_3) {
                            String[] coords = text.split(" ");
                            int pixelX = Integer.parseInt(coords[0]);
                            int pixelY = Integer.parseInt(coords[1]);

                            float blockX = cookieX + pixelX * blockSizeX;
                            float blockY = cookieY + pixelY * blockSizeY;

                            float blockEndX = blockX + blockSizeX + 1;
                            float blockEndY = blockY + blockSizeY + 1;

                            if (!default_pixels.contains(text))
                                context.fill((int) blockX, (int) blockY, (int) blockEndX, (int) blockEndY, break_colors.get(text));
                        }
                    }

                }
            }
            case WINNER -> {
                cookie = cookieType.getCompleted().getID();
                context.drawTexture(cookie, cookieX, cookieY, 0, 0, cookieWidth, cookieHeight, cookieWidth, cookieHeight);
            }
            case LOSER -> {
                cookie = cookieType.getBroken().getID();
                context.drawTexture(cookie, cookieX, cookieY, 0, 0, cookieWidth, cookieHeight, cookieWidth, cookieHeight);
            }
        }

        particleManager.render(context, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            drawing = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            drawing = false;
            lastKey = null;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (drawing && button == 0 && (state != CookieState.LOSER && state != CookieState.WINNER)) {
            float cookieWidth = (float) (Math.min(height, width) * 0.8);
            float cookieHeight = (float) (Math.min(height, width) * 0.8);
            float cookieX = ((float) width / 2) - (cookieWidth / 2);
            float cookieY = ((float) height / 2) - (cookieHeight / 2);

            float blockSizeX = cookieWidth / 37.0f;
            float blockSizeY = cookieHeight / 37.0f;

            if (mouseX >= cookieX && mouseX < cookieX + cookieWidth && mouseY >= cookieY && mouseY < cookieY + cookieHeight) {
                int gridX = (int) ((mouseX - cookieX) / blockSizeX);
                int gridY = (int) ((mouseY - cookieY) / blockSizeY);
                String key = gridX + " " + gridY;

                if (!key.equals(lastKey)) {
                    lastKey = key;

                    if (paintedPixels.containsKey(key)) {
                        playSound("crunch", ThreadLocalRandom.current().nextFloat(0.5f, 1.3f), 0.1f);
                        float opacity = paintedPixels.get(key) - 0.03f;
                        if (opacity <= 0.5f) {
                            paintedPixels.remove(key);
                        } else {
                            paintedPixels.put(key, opacity);
                        }

                        if (paintedPixels.isEmpty()) {
                            win();
                        }
                        particleManager.addParticle((int) mouseX, (int) mouseY, modifyColor(0xff7e3a09));
                    } else if (!default_pixels.contains(key)) {
                        fails++;
                        screenVibration.startVibration(0.2f, 4);
                        if (fails >= 13) {
                            fail();
                        }
                    }
                }
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private int modifyColor(int baseColor) {
        int alpha = (baseColor >> 24) & 0xFF;
        int red = (baseColor >> 16) & 0xFF;
        int green = (baseColor >> 8) & 0xFF;
        int blue = baseColor & 0xFF;

        float factor = 0.5f + (ThreadLocalRandom.current().nextFloat() * 0.8f);

        red = Math.min(255, Math.max(0, (int) (red * factor)));
        green = Math.min(255, Math.max(0, (int) (green * factor)));
        blue = Math.min(255, Math.max(0, (int) (blue * factor)));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private void win() {
        state = CookieState.WINNER;
        runCommand("wiigane");
    }

    private void fail() {
        state = CookieState.LOSER;
        runCommand("ayperdi");
    }

    @Override
    public void renderInGameBackground(DrawContext context) {

    }

    private void playSound(String sound, float pitch, float volume) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.of(Constants.MOD_ID, sound)), pitch, volume));
    }

    @Override
    public void tick() {
        super.tick();
        screenVibration.update();
        particleManager.update();
    }

    private void runCommand(String command) {
        assert Objects.requireNonNull(client).player != null;
        assert client.player != null;
        client.player.networkHandler.sendChatCommand(command);
    }
}
