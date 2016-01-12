package trikita.promote;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimplePreferences implements SharedPreferences {
    private final Map<String, Object> items = new HashMap<>();
    public Map<String, ?> getAll() {
        return items;
    }

    private <T> T get(String key, T defValue) {
        return items.containsKey(key) ? (T) items.get(key) : defValue;
    }

    public String getString(String key, String defValue) {
        return get(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return get(key, defValues);
    }

    public int getInt(String key, int defValue) {
        return get(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return get(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return get(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return get(key, defValue);
    }

    public boolean contains(String key) {
        return items.containsKey(key);
    }

    public Editor edit() {
        return new Editor() {
            private <T> Editor put(String key, T value) {
                items.put(key, value);
                return this;
            }

            public Editor putString(String key, String value) {
                return put(key, value);
            }

            public Editor putStringSet(String key, Set<String> values) {
                return put(key, values);
            }

            public Editor putInt(String key, int value) {
                return put(key, value);
            }

            public Editor putLong(String key, long value) {
                return put(key, value);
            }

            public Editor putFloat(String key, float value) {
                return put(key, value);
            }

            public Editor putBoolean(String key, boolean value) {
                return put(key, value);
            }

            public Editor remove(String key) {
                items.remove(key);
                return this;
            }

            public Editor clear() {
                items.clear();
                return this;
            }

            public boolean commit() {
                return true;
            }

            public void apply() {
            }
        };
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}
}
