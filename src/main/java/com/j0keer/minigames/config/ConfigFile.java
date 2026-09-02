package com.j0keer.minigames.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigFile {
    private final File file;
    private final Gson gson;
    private JsonObject root;

    public ConfigFile(File file) {
        this.file = file;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.root = new JsonObject();
        this.load();
    }

    public ConfigFile(String filePath) {
        this(new File(FabricLoader.getInstance().getConfigDir().resolve("minigames").toFile(), filePath));
    }

    public void load() {
        if (!file.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            JsonElement parsed = JsonParser.parseReader(reader);
            if (parsed != null && parsed.isJsonObject()) {
                this.root = parsed.getAsJsonObject();
            } else {
                this.root = new JsonObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.root = new JsonObject();
        }
    }

    public void save() {
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                this.gson.toJson(this.root, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        this.load();
    }

    public void set(String path, Object value) {
        if (path == null) {
            return;
        }

        String[] parts = path.split("\\.");
        JsonObject current = this.root;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (!current.has(part) || !current.get(part).isJsonObject()) {
                JsonObject next = new JsonObject();
                current.add(part, next);
                current = next;
            } else {
                current = current.getAsJsonObject(part);
            }
        }

        String lastKey = parts[parts.length - 1];

        if (value == null) {
            current.remove(lastKey);
            return;
        }

        if (value instanceof JsonElement jsonElement) {
            current.add(lastKey, jsonElement);
        } else if (value instanceof String stringValue) {
            current.addProperty(lastKey, stringValue);
        } else if (value instanceof Number numberValue) {
            current.addProperty(lastKey, numberValue);
        } else if (value instanceof Boolean booleanValue) {
            current.addProperty(lastKey, booleanValue);
        } else if (value instanceof Character charValue) {
            current.addProperty(lastKey, charValue);
        } else {
            current.add(lastKey, this.gson.toJsonTree(value));
        }
    }

    public JsonElement get(String path) {
        if (path == null || path.isEmpty()) {
            return this.root;
        }

        String[] parts = path.split("\\.");
        JsonObject current = this.root;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (!current.has(part) || !current.get(part).isJsonObject()) {
                return null;
            }
            current = current.getAsJsonObject(part);
        }

        return current.get(parts[parts.length - 1]);
    }

    public boolean contains(String path) {
        return get(path) != null;
    }

    public String getString(String path) {
        return getString(path, null);
    }

    public String getString(String path, String defaultValue) {
        JsonElement element = get(path);
        if (element != null && element.isJsonPrimitive()) {
            return element.getAsString();
        }
        return defaultValue;
    }

    public int getInt(String path) {
        return getInt(path, 0);
    }

    public int getInt(String path, int defaultValue) {
        JsonElement element = get(path);
        if (element != null && element.isJsonPrimitive()) {
            try {
                return element.getAsInt();
            } catch (Exception ignored) {
            }
        }
        return defaultValue;
    }

    public double getDouble(String path) {
        return getDouble(path, 0.0);
    }

    public double getDouble(String path, double defaultValue) {
        JsonElement element = get(path);
        if (element != null && element.isJsonPrimitive()) {
            try {
                return element.getAsDouble();
            } catch (Exception ignored) {
            }
        }
        return defaultValue;
    }

    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    public boolean getBoolean(String path, boolean defaultValue) {
        JsonElement element = get(path);
        if (element != null && element.isJsonPrimitive()) {
            try {
                return element.getAsBoolean();
            } catch (Exception ignored) {
            }
        }
        return defaultValue;
    }

    public JsonObject getJsonObject(String path) {
        JsonElement element = get(path);
        if (element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        return null;
    }

    public JsonArray getJsonArray(String path) {
        JsonElement element = get(path);
        if (element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        return null;
    }

    public JsonObject getRoot() {
        return this.root;
    }

    public File getFile() {
        return this.file;
    }
}
