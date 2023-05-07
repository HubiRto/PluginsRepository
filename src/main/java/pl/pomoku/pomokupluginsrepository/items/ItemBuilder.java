package pl.pomoku.pomokupluginsrepository.items;

import com.google.gson.Gson;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Material.BOW;
import static org.bukkit.Material.PLAYER_HEAD;
import static org.bukkit.enchantments.Enchantment.ARROW_INFINITE;
import static org.bukkit.enchantments.Enchantment.LUCK;

@SuppressWarnings("unused")
public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;
    private SkullMeta skullMeta;
    private Material material;
    private int amount = 1;
    private short damage = 0;
    private Component displayname;
    private List<Component> lore = new ArrayList<>();
    private List<ItemFlag> flags = new ArrayList<>();
    private boolean unsafeStackSize = false;
    private List<NamespacedKey> namespacedKeyList = new ArrayList<>();

    public ItemBuilder(Material material) {
        if (material == null) material = Material.AIR;
        this.item = new ItemStack(material);
        this.material = material;
        if (material == Material.PLAYER_HEAD) {
            this.skullMeta = (SkullMeta) item.getItemMeta();
        }
    }

    public ItemBuilder(Material material, int amount) {
        if (material == null) material = Material.AIR;
        if (amount > material.getMaxStackSize() || amount <= 0) amount = 1;
        this.amount = amount;
        this.item = new ItemStack(material, amount);
        this.material = material;
    }

    public ItemBuilder(Material material, int amount, Component displayname) {
        if (material == null) material = Material.AIR;
        Validate.notNull(displayname, "The Displayname is null.");
        this.item = new ItemStack(material, amount);
        this.material = material;
        if (((amount > material.getMaxStackSize()) || (amount <= 0)) && (!unsafeStackSize)) amount = 1;
        this.amount = amount;
        this.displayname = displayname;
    }

    public ItemBuilder(Material material, Component displayname) {
        if (material == null) material = Material.AIR;
        Validate.notNull(displayname, "The Displayname is null.");
        this.item = new ItemStack(material);
        this.material = material;
        this.displayname = displayname;
    }

    public ItemBuilder(ItemStack item) {
        Validate.notNull(item, "The Item is null.");
        this.item = item;
        if (item.hasItemMeta())
            this.meta = item.getItemMeta();
        this.material = item.getType();
        this.amount = item.getAmount();
        this.damage = item.getDurability();
        if (item.hasItemMeta())
            this.displayname = item.getItemMeta().displayName();
        if (item.hasItemMeta())
            this.lore = item.getItemMeta().lore();
        if (item.hasItemMeta())
            flags.addAll(item.getItemMeta().getItemFlags());
    }

    public ItemBuilder(FileConfiguration cfg, String path) {
        this(cfg.getItemStack(path));
    }

    @Deprecated
    public ItemBuilder(ItemBuilder builder) {
        Validate.notNull(builder, "The ItemBuilder is null.");
        this.item = builder.item;
        this.meta = builder.meta;
        this.material = builder.material;
        this.amount = builder.amount;
        this.damage = builder.damage;
        this.displayname = builder.displayname;
        this.lore = builder.lore;
        this.flags = builder.flags;
    }

    /**
     * Funkcja na ustawianie ilość w ItemBuilder'ze
     *
     * @param amount ilość przedmiotu
     * @return ustawia ilość przedmiotu w ItemBuilder'ze
     */
    public ItemBuilder amount(int amount) {
        if (((amount > material.getMaxStackSize()) || (amount <= 0)) && (!unsafeStackSize)) amount = 1;
        this.amount = amount;
        return this;
    }

    /**
     * Funkcja na dodawania obrażeń do ItemBuilder'a
     *
     * @param damage obrażenia, jakie ma mieć przedmiot
     * @return dodaje obrażenia do ItemBuilder'a
     */
    public ItemBuilder durability(short damage) {
        this.damage = damage;
        return this;
    }

    /**
     * Funkcja na dodawania materiału do ItemBuilder'a
     *
     * @param material materiał przedmiotu
     * @return dodaje materiał do ItemBuilder'a
     */
    public ItemBuilder material(Material material) {
        Validate.notNull(material, "The Material is null.");
        this.material = material;
        return this;
    }

    /**
     * Funkcja na dodawanie ItemMeta do ItemBuilder'a
     *
     * @param meta ItemMeta przedmiotu
     * @return dodaje ItemMeta do ItemBuilder'a
     */
    public ItemBuilder meta(ItemMeta meta) {
        Validate.notNull(meta, "The Meta is null.");
        this.meta = meta;
        return this;
    }

    /**
     * Funkcja na dodawanie enchant'u do ItemBuilder'a
     *
     * @param enchant enchant, który ma być dodany
     * @param level   poziom enchant'u, który ma być dodany
     * @return dodaje enchant do ItemBuilder'a
     */
    public ItemBuilder enchant(Enchantment enchant, int level, boolean b) {
        Validate.notNull(enchant, "The Enchantment is null.");
        if (meta == null) {
            meta = item.getItemMeta();
        }
        meta.addEnchant(enchant, level, b);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Funkcja na dodawanie nazwy do przedmiotu
     *
     * @param displayname nazwa przedmiotu
     * @return dodaje nazwę do ItemBuilder'a
     */
    public ItemBuilder displayname(Component displayname) {
        Validate.notNull(displayname, "The Displayname is null.");
        this.displayname = displayname;
        return this;
    }

    public ItemBuilder persistentData(NamespacedKey namespacedKey, PersistentDataType persistentDataType, Object object) {
        Validate.notNull(namespacedKey, "The namespaceKey is null.");
        if (meta == null) {
            this.meta = item.getItemMeta();
        }
        this.meta.getPersistentDataContainer().set(namespacedKey, persistentDataType, object);
        return this;
    }

    public ItemBuilder lore(Component line) {
        Validate.notNull(line, "The Line is null.");
        lore.add(line);
        return this;
    }

    public ItemBuilder lore(List<Component> lore) {
        Validate.notNull(lore, "The Lores are null.");
        //lore.replaceAll(textToTranslate -> andSymbol ? ChatColor.translateAlternateColorCodes('&', textToTranslate) : textToTranslate);
        if (meta == null) meta = item.getItemMeta();
        var loresLines = meta.lore();
        if (loresLines == null) loresLines = new ArrayList<>();
        loresLines.addAll(lore);

        this.lore = loresLines;
        return this;
    }

//    public ItemBuilder lore(String... lines) {
//        Validate.notNull(lines, "The Lines are null.");
//        for (String line : lines) {
//            lore(andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
//        }
//        return this;
//    }

    public ItemBuilder lore(Component line, int index) {
        Validate.notNull(line, "The Line is null.");
        lore.set(index, line);
        return this;
    }

    public ItemBuilder flag(ItemFlag flag) {
        Validate.notNull(flag, "The Flag is null.");
        flags.add(flag);
        return this;
    }

    public ItemBuilder flag(List<ItemFlag> flags) {
        Validate.notNull(flags, "The Flags are null.");
        this.flags = flags;
        return this;
    }

    public ItemBuilder glow() {
        enchant(material != BOW ? ARROW_INFINITE : LUCK, 10, false);
        flag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder playerHead(String texture) {
        if (material == Material.PLAYER_HEAD) {
            texture = "http://textures.minecraft.net/texture/" + texture;
            SkullMeta skullMeta = this.skullMeta;

            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            assert profileField != null;
            profileField.setAccessible(true);
            try {
                profileField.set(skullMeta, profile);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            this.skullMeta = skullMeta;
        }
        return this;
    }

    public ItemBuilder playerHead(Player player) {
        if (material == PLAYER_HEAD) {
            if (this.skullMeta == null) this.skullMeta = (SkullMeta) item.getItemMeta();
            this.skullMeta.setPlayerProfile(player.getPlayerProfile());
        }
        return this;
    }

    public Unsafe unsafe() {
        return new Unsafe(this);
    }

    public ItemBuilder unsafeStackSize(boolean allow) {
        this.unsafeStackSize = allow;
        return this;
    }

    public ItemBuilder toggleUnsafeStackSize() {
        unsafeStackSize(!unsafeStackSize);
        return this;
    }

    public Component getDisplayname() {
        return displayname;
    }

    public int getAmount() {
        return amount;
    }


    public short getDurability() {
        return damage;
    }

    public List<Component> getLores() {
        return lore;
    }

    public List<ItemFlag> getFlags() {
        return flags;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public ItemBuilder toConfig(FileConfiguration cfg, String path) {
        cfg.set(path, build());
        return this;
    }

    public ItemBuilder fromConfig(FileConfiguration cfg, String path) {
        return new ItemBuilder(cfg, path);
    }

    public static void toConfig(FileConfiguration cfg, String path, ItemBuilder builder) {
        cfg.set(path, builder.build());
    }

    public String toJson() {
        return new Gson().toJson(this);
    }


    public static String toJson(ItemBuilder builder) {
        return new Gson().toJson(builder);
    }

    public static ItemBuilder fromJson(String json) {
        return new Gson().fromJson(json, ItemBuilder.class);
    }

    public ItemBuilder applyJson(String json, boolean overwrite) {
        ItemBuilder b = new Gson().fromJson(json, ItemBuilder.class);
        if (overwrite)
            return b;
        if (b.displayname != null)
            displayname = b.displayname;
        if (b.material != null)
            material = b.material;
        if (b.lore != null)
            lore = b.lore;
        if (b.item != null)
            item = b.item;
        if (b.flags != null)
            flags = b.flags;
        damage = b.damage;
        amount = b.amount;
        return this;
    }

    public ItemStack build() {
        item.setType(material);
        item.setAmount(amount);
        item.setDurability(damage);
        if (skullMeta != null) {
            item.setItemMeta(skullMeta);
        }
        meta = item.getItemMeta();
        if (displayname != null) {
            meta.displayName(displayname);
        }
        if (lore.size() > 0) {
            meta.lore(lore);
        }
        if (flags.size() > 0) {
            for (ItemFlag f : flags) {
                meta.addItemFlags(f);
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    public class Unsafe {

        protected final ReflectionUtils utils = new ReflectionUtils();
        protected final ItemBuilder builder;

        public Unsafe(ItemBuilder builder) {
            this.builder = builder;
        }

        public Unsafe setString(String key, String value) {
            builder.item = utils.setString(builder.item, key, value);
            return this;
        }

        public String getString(String key) {
            return utils.getString(builder.item, key);
        }

        public Unsafe setInt(String key, int value) {
            builder.item = utils.setInt(builder.item, key, value);
            return this;
        }

        public int getInt(String key) {
            return utils.getInt(builder.item, key);
        }

        public Unsafe setDouble(String key, double value) {
            builder.item = utils.setDouble(builder.item, key, value);
            return this;
        }

        public double getDouble(String key) {
            return utils.getDouble(builder.item, key);
        }

        public Unsafe setBoolean(String key, boolean value) {
            builder.item = utils.setBoolean(builder.item, key, value);
            return this;
        }

        public boolean getBoolean(String key) {
            return utils.getBoolean(builder.item, key);
        }

        public boolean containsKey(String key) {
            return utils.hasKey(builder.item, key);
        }

        public ItemBuilder builder() {
            return builder;
        }

        public class ReflectionUtils {

            public String getString(ItemStack item, String key) {
                Object compound = getNBTTagCompound(getItemAsNMSStack(item));
                if (compound == null) {
                    compound = getNewNBTTagCompound();
                }
                try {
                    return (String) compound.getClass().getMethod("getString", String.class).invoke(compound, key);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            public ItemStack setString(ItemStack item, String key, String value) {
                Object nmsItem = getItemAsNMSStack(item);
                Object compound = getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setString", String.class, String.class).invoke(compound, key, value);
                    nmsItem = setNBTTag(compound, nmsItem);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return getItemAsBukkitStack(nmsItem);
            }

            public int getInt(ItemStack item, String key) {
                Object compound = getNBTTagCompound(getItemAsNMSStack(item));
                if (compound == null) {
                    compound = getNewNBTTagCompound();
                }
                try {
                    return (Integer) compound.getClass().getMethod("getInt", String.class).invoke(compound, key);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return -1;
            }

            public ItemStack setInt(ItemStack item, String key, int value) {
                Object nmsItem = getItemAsNMSStack(item);
                Object compound = getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setInt", String.class, Integer.class).invoke(compound, key, value);
                    nmsItem = setNBTTag(compound, nmsItem);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return getItemAsBukkitStack(nmsItem);
            }

            public double getDouble(ItemStack item, String key) {
                Object compound = getNBTTagCompound(getItemAsNMSStack(item));
                if (compound == null) {
                    compound = getNewNBTTagCompound();
                }
                try {
                    return (Double) compound.getClass().getMethod("getDouble", String.class).invoke(compound, key);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return Double.NaN;
            }

            public ItemStack setDouble(ItemStack item, String key, double value) {
                Object nmsItem = getItemAsNMSStack(item);
                Object compound = getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setDouble", String.class, Double.class).invoke(compound, key, value);
                    nmsItem = setNBTTag(compound, nmsItem);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return getItemAsBukkitStack(nmsItem);
            }

            public boolean getBoolean(ItemStack item, String key) {
                Object compound = getNBTTagCompound(getItemAsNMSStack(item));
                if (compound == null) {
                    compound = getNewNBTTagCompound();
                }
                try {
                    return (Boolean) compound.getClass().getMethod("getBoolean", String.class).invoke(compound, key);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return false;
            }

            public ItemStack setBoolean(ItemStack item, String key, boolean value) {
                Object nmsItem = getItemAsNMSStack(item);
                Object compound = getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setBoolean", String.class, Boolean.class).invoke(compound, key, value);
                    nmsItem = setNBTTag(compound, nmsItem);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return getItemAsBukkitStack(nmsItem);
            }

            public boolean hasKey(ItemStack item, String key) {
                Object compound = getNBTTagCompound(getItemAsNMSStack(item));
                if (compound == null) {
                    compound = getNewNBTTagCompound();
                }
                try {
                    return (Boolean) compound.getClass().getMethod("hasKey", String.class).invoke(compound, key);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return false;
            }

            public Object getNewNBTTagCompound() {
                String ver = Bukkit.getServer().getClass().getPackage().getName().split(".")[3];
                try {
                    return Class.forName("net.minecraft.server." + ver + ".NBTTagCompound").newInstance();
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            public Object setNBTTag(Object tag, Object item) {
                try {
                    item.getClass().getMethod("setTag", item.getClass()).invoke(item, tag);
                    return item;
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            public Object getNBTTagCompound(Object nmsStack) {
                try {
                    return nmsStack.getClass().getMethod("getTag").invoke(nmsStack);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            public Object getItemAsNMSStack(ItemStack item) {
                try {
                    Method m = getCraftItemStackClass().getMethod("asNMSCopy", ItemStack.class);
                    return m.invoke(getCraftItemStackClass(), item);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            public ItemStack getItemAsBukkitStack(Object nmsStack) {
                try {
                    Method m = getCraftItemStackClass().getMethod("asCraftMirror", nmsStack.getClass());
                    return (ItemStack) m.invoke(getCraftItemStackClass(), nmsStack);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            public Class<?> getCraftItemStackClass() {
                String ver = Bukkit.getServer().getClass().getPackage().getName().split(".")[3];
                try {
                    return Class.forName("org.bukkit.craftbukkit." + ver + ".inventory.CraftItemStack");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

        }
    }
}