package net.emuman.spigotutils.npc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * A wrapper for EntityPlayer that allows for the basic creation of fake player entities in a server.
 */
public class NPC {
    // I might turn this into an extension of EntityPlayer, as demonstrated in DarkSeraphim's
    // NPC library (https://gist.github.com/DarkSeraphim/6d211c389b7824cbc6ee), but I want to
    // first make sure that it works with Maven multi-version compatibility.

    private static final int RENDER_DISTANCE = 50;

    private EntityPlayer npc;
    private DataWatcher dataWatcher;
    protected final JavaPlugin plugin;
    private final Set<Player> displayedFor;
    private final Set<Player> inRenderDistance;
    private final String name;
    private final org.bukkit.World world;
    private final org.bukkit.inventory.ItemStack[] equipment;
    private static final ArrayList<NPC> npcList = new ArrayList<>();
    private static final JsonParser jsonParser = new JsonParser();

    private static final HashMap<String, String[]> PLAYER_SKIN_CACHE = new HashMap<>();

    public enum EquipmentSlot {
        MAIN_HAND,
        OFF_HAND,
        BOOTS,
        LEGGINGS,
        CHESTPLATE,
        HELMET
    }

    /**
     * Creates a new NPC.
     *
     * @param plugin        the plugin using the new NPC.
     * @param location      the location at which to spawn the new NPC.
     * @param name          the name of the new NPC.
     * @param skinTexture   the texture value of the skin to be used on the NPC.
     * @param skinSignature the signature value of the skin to be used on the NPC.
     */
    public NPC(JavaPlugin plugin, Location location, String name, String skinTexture, String skinSignature) {
        this.plugin = plugin;
        this.displayedFor = new HashSet<>();
        this.inRenderDistance = new HashSet<>();
        this.name = name;
        this.world = location.getWorld();
        this.equipment = new org.bukkit.inventory.ItemStack[6];

        if (name.length() > 16) return;
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
        npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        dataWatcher = npc.getDataWatcher();
        if (skinTexture != null && skinSignature != null) {
            gameProfile.getProperties().put("textures", new Property("textures", skinTexture, skinSignature));
            // https://www.spigotmc.org/threads/get-npc-second-layer.453988/
            dataWatcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 255);
        }

        npcList.add(this);

        // Periodically update to despawn for players who are not in render distance and vice versa
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : displayedFor) {
                    if (isInRenderDistance(player) && !inRenderDistance.contains(player)) {
                        inRenderDistance.add(player);
                        sendSpawnPackets(player);
                    } else if (!isInRenderDistance(player) && inRenderDistance.contains(player)) {
                        inRenderDistance.remove(player);
                        sendDespawnPackets(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    /**
     * Creates a new NPC with the default skin (Steve/Alex)
     *
     * @param plugin   the plugin using the new NPC.
     * @param location the location at which to spawn the new NPC.
     * @param name     the name of the new NPC.
     */
    public NPC(JavaPlugin plugin, Location location, String name) {
        this(plugin, location, name, null, null);
    }

    /**
     * Creates a new NPC.
     *
     * @param plugin   the plugin using the new NPC.
     * @param location the location at which to spawn the new NPC.
     * @param name     the name of the new NPC.
     * @param skinName the name of the player whose skin should be used on the NPC.
     */
    public NPC(JavaPlugin plugin, Location location, String name, String skinName) {
        // probably not great practice but the function is cached so idc
        this(plugin, location, name, getPlayerSkin(skinName)[0], getPlayerSkin(skinName)[1]);
    }

    /**
     * Sets the given equipment slot to a specified item.
     *
     * @param slot      the equipment slot to assign the item to.
     * @param itemStack the ItemStack to place in the specified equipment slot.
     */
    public void setEquipmentSlot(EquipmentSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        equipment[slot.ordinal()] = itemStack;
    }

    /**
     * Sends packets to spawn in the NPC for the given player.
     *
     * @param player the player to spawn the NPC in for.
     */
    private void sendSpawnPackets(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
        connection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), dataWatcher, true));
        // Have to wait a bit before sending this packet or else the skin doesn't load properly.
        new BukkitRunnable() {
            @Override
            public void run() {
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
            }
        }.runTaskLater(plugin, 50L);
        List<Pair<EnumItemSlot, ItemStack>> equipmentInfo = getEquipmentInfo();
        if (equipmentInfo.size() > 0) {
            connection.sendPacket(new PacketPlayOutEntityEquipment(npc.getId(), equipmentInfo));
        }
    }

    /**
     * Converts an integer value (can be taken from EquipmentSlot ordinals) into an EnumItemSlot.
     *
     * @param i the integer value of the item slot.
     * @return  the EnumItemSlot that corresponds to the integer (refer to NMS specifications or EquipmentSlot ordinals).
     */
    private EnumItemSlot convertToEnumItemSlot(int i) {
        switch (i) {
            case 0:
                return EnumItemSlot.MAINHAND;
            case 1:
                return EnumItemSlot.OFFHAND;
            case 2:
                return EnumItemSlot.FEET;
            case 3:
                return EnumItemSlot.LEGS;
            case 4:
                return EnumItemSlot.CHEST;
            default:
                return EnumItemSlot.HEAD;
        }
    }

    /**
     * Gets the equipment information for the given NPC, to be used in NMS class PacketPlayOutEntityEquipment.
     *
     * @return a list of the equipment information for the NPC.
     */
    private List<Pair<EnumItemSlot, ItemStack>> getEquipmentInfo() {
        List<Pair<EnumItemSlot, ItemStack>> equipmentInfo = new ArrayList<>();
        for (int i = 0; i < equipment.length; i++) {
            if (equipment[i] == null) continue;
            equipmentInfo.add(new Pair<>(convertToEnumItemSlot(i), CraftItemStack.asNMSCopy(equipment[i])));
        }
        return equipmentInfo;
    }

    /**
     * Spawns in the NPC for the player permanently (until player logs off or NPC#despawnForPlayer is called).
     *
     * @param player the player to spawn the NPC in for.
     */
    public void spawnForPlayer(Player player) {
        displayedFor.add(player);
    }

    /**
     * Sends packets to despawn the NPC for the given player.
     *
     * @param player the player to despawn the NPC for.
     */
    private void sendDespawnPackets(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
    }

    /**
     * Despawns the NPC for the given player.
     *
     * @param player the player to despawn the NPC for.
     */
    public void despawnForPlayer(Player player) {
        if (displayedFor.remove(player)) {
            sendDespawnPackets(player);
        }
    }

    /**
     * Gets the skin information for the player with the specified name.
     *
     * @param name the name of the player to get the skin of.
     * @return     a String array containing 1) the skin's texture value, and 2) the skin's texture signature, respectively.
     */
    private static String[] getPlayerSkin(String name) {
        if (PLAYER_SKIN_CACHE.containsKey(name)) {
            return PLAYER_SKIN_CACHE.get(name);
        }
        String[] result = new String[2];
        try {
            URL profileURL = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader profileReader = new InputStreamReader(profileURL.openStream());
            String uuid = jsonParser.parse(profileReader).getAsJsonObject().get("id").getAsString();
            profileURL = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            profileReader = new InputStreamReader(profileURL.openStream());
            JsonArray properties = jsonParser.parse(profileReader).getAsJsonObject().get("properties").getAsJsonArray();
            JsonObject property = null;
            for (JsonElement p : properties) {
                if (p.getAsJsonObject().get("name").getAsString().equals("textures")) {
                    property = p.getAsJsonObject();
                    break;
                }
            }
            if (property == null) return result;
            result = new String[] {property.get("value").getAsString(), property.get("signature").getAsString()};
            PLAYER_SKIN_CACHE.put(name, result);
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * @return the EntityPlayer object this NPC object wraps.
     */
    public EntityPlayer getEntityPlayer() {
        return npc;
    }

    /**
     * @return a list containing every created NPC.
     */
    public static ArrayList<NPC> getAllNPCs() {
        return npcList;
    }

    /**
     * Determines whether or not the specified player is within the specified render distance of the NPC.
     *
     * @param player the player to be tested.
     * @return       true if the player is within render distance, false otherwise.
     */
    public boolean isInRenderDistance(Player player) {
        Location playerLocation = player.getLocation();
        if (playerLocation.getWorld() == null) return false;
        if (!playerLocation.getWorld().equals(world)) return false;
        Vec3D npcLocation = npc.getPositionVector();
        double dx = playerLocation.getX() - npcLocation.x;
        double dy = playerLocation.getY() - npcLocation.y;
        double dz = playerLocation.getZ() - npcLocation.z;
        return ((int) (dx * dx + dy * dy + dz * dz) < RENDER_DISTANCE * RENDER_DISTANCE);
    }

    public void moveTo(Location location) {
        boolean isTeleport = (Math.abs(location.getX() - npc.locX()) > 8.0d ||
                Math.abs(location.getY() - npc.locY()) > 8.0d ||
                Math.abs(location.getZ() - npc.locZ()) > 8.0d);
        Location oldLocation = new Location(location.getWorld(), npc.locX(), npc.locY(), npc.locZ());
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        for (Player player : inRenderDistance) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            if (isTeleport) {
                connection.sendPacket(new PacketPlayOutEntityTeleport(npc));
            } else {
                connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(npc.getId(),
                        (short) ((location.getX() * 32.0d - oldLocation.getX() * 32.0d) * 128),
                        (short) ((location.getY() * 32.0d - oldLocation.getY() * 32.0d) * 128),
                        (short) ((location.getZ() * 32.0d - oldLocation.getZ() * 32.0d) * 128),
                        (byte) (location.getYaw() * 256 / 360),
                        (byte) (location.getPitch() * 256 / 360),
                        false));
                connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
            }
        }
    }

    /**
     * @return the name of the NPC.
     */
    public String getName() {
        return name;
    }

}
