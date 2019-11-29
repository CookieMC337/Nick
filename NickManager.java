
package api;

import java.util.Random;
import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import java.util.HashSet;
import org.bukkit.plugin.Plugin;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import com.mojang.authlib.properties.Property;
import java.util.Collection;
import com.mojang.authlib.GameProfile;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.HashMap;
import com.mycompany.nick.Nick;

public class NickManager
{
    private Nick plugin;
    public static HashMap<UUID, String> NamePlayers;
    
    public static void changeSkin(final Player p, final String skin) {
        final CraftPlayer cp = (CraftPlayer)p;
        GameProfile skingp = cp.getProfile();
        try {
            if (UUIDFetcher.getUUID(skin) == null) {
                skingp = GameProfileFetcher.fetch(UUIDFetcher.getUUID("Steve"));
            }
            else {
                skingp = GameProfileFetcher.fetch(UUIDFetcher.getUUID(skin));
            }
        }
        catch (Exception ex) {}
        final Collection<Property> props = (Collection<Property>)skingp.getProperties().get((Object)"textures");
        cp.getProfile().getProperties().removeAll((Object)"textures");
        cp.getProfile().getProperties().putAll((Object)"textures", (Iterable)props);
    }
    
    public static void updateSkin(final Player p) {
        try {
            if (!p.isOnline()) {
                return;
            }
            final CraftPlayer cp = (CraftPlayer)p;
            final EntityPlayer ep = cp.getHandle();
            final int entId = ep.getId();
            final PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { ep });
            final PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(new int[] { entId });
            final PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn((EntityHuman)ep);
            final PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { ep });
            final PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, 0, CraftItemStack.asNMSCopy(p.getItemInHand()));
            final PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, 4, CraftItemStack.asNMSCopy(p.getInventory().getHelmet()));
            final PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, 3, CraftItemStack.asNMSCopy(p.getInventory().getChestplate()));
            final PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, 2, CraftItemStack.asNMSCopy(p.getInventory().getLeggings()));
            final PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, 1, CraftItemStack.asNMSCopy(p.getInventory().getBoots()));
            final PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(p.getInventory().getHeldItemSlot());
            final Iterator var15 = ((CraftServer)Bukkit.getServer()).getOnlinePlayers().iterator();
            final PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(ep.dimension, ep.playerInteractManager.world.getDifficulty(), ep.playerInteractManager.world.G(), ep.playerInteractManager.getGameMode());
            while (var15.hasNext()) {
                final Player pOnline = var15.next();
                final CraftPlayer craftOnline = (CraftPlayer)pOnline;
                final PlayerConnection con = craftOnline.getHandle().playerConnection;
                if (pOnline.getName().equals(p.getName())) {
                    con.sendPacket((Packet)removeInfo);
                    con.sendPacket((Packet)addInfo);
                    con.teleport(p.getLocation());
                    con.sendPacket((Packet)slot);
                    con.sendPacket((Packet)respawn);
                    craftOnline.updateScaledHealth();
                    craftOnline.getHandle().triggerHealthUpdate();
                    craftOnline.updateInventory();
                    Bukkit.getScheduler().runTask((Plugin)Nick.getInstance(), (Runnable)new Runnable() {
                        @Override
                        public void run() {
                            craftOnline.getHandle().updateAbilities();
                        }
                    });
                    final PacketPlayOutPosition position = new PacketPlayOutPosition(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch(), (Set)new HashSet());
                    con.sendPacket((Packet)position);
                }
                else {
                    if (!pOnline.canSee(p)) {
                        continue;
                    }
                    con.sendPacket((Packet)removeEntity);
                    con.sendPacket((Packet)removeInfo);
                    con.sendPacket((Packet)addInfo);
                    con.sendPacket((Packet)addNamed);
                    con.sendPacket((Packet)itemhand);
                    con.sendPacket((Packet)helmet);
                    con.sendPacket((Packet)chestplate);
                    con.sendPacket((Packet)leggings);
                    con.sendPacket((Packet)boots);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void sendPacket(final Packet packet) {
        for (final Player all : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
        }
    }
    
    public static void changeNick(final Player p, final String nick) {
        final CraftPlayer cp = (CraftPlayer)p;
        try {
            final Field pF = cp.getProfile().getClass().getDeclaredField("name");
            pF.setAccessible(true);
            pF.set(cp.getProfile(), nick);
        }
        catch (Exception ex) {}
    }
    
    public static void updateForOther(final Player p) {
        final CraftPlayer cp = (CraftPlayer)p;
        final EntityPlayer ep = cp.getHandle();
        final int entId = ep.getId();
        final PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { ep });
        final PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(new int[] { entId });
        final PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn((EntityHuman)ep);
        final PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { ep });
        CraftPlayer cpAll;
        PlayerConnection con;
        final Packet packet;
        final Packet packet2;
        final Packet packet3;
        final Packet packet4;
        Bukkit.getOnlinePlayers().forEach(all -> {
            if (all != p) {
                cpAll = all;
                con = cpAll.getHandle().playerConnection;
                con.sendPacket(packet);
                con.sendPacket(packet2);
                con.sendPacket(packet3);
                con.sendPacket(packet4);
            }
        });
    }
    
    public static void nickPlayer(final Player p, final String nickname) {
        NickManager.NamePlayers.put(p.getUniqueId(), p.getName());
        changeSkin(p, nickname);
        changeNick(p, nickname);
        updateSkin(p);
        p.setDisplayName(nickname);
        final String nickmessage = Nick.getInstance().getConfig().getString("Nickmessage").replace("&", "§");
        p.sendMessage(nickmessage + nickname);
    }
    
    public static void unNickPlayer(final Player p) {
        changeSkin(p, NickManager.NamePlayers.get(p.getUniqueId()));
        changeNick(p, NickManager.NamePlayers.get(p.getUniqueId()));
        updateSkin(p);
        p.setDisplayName((String)NickManager.NamePlayers.get(p.getUniqueId()));
        NickManager.NamePlayers.remove(p.getUniqueId());
    }
    
    public static String getRealName(final UUID id) {
        return NickManager.NamePlayers.get(id);
    }
    
    public static String getrandomName() {
        final Random random;
        final Random rnd = random = new Random();
        Nick.getInstance();
        final int zufall = random.nextInt(Nick.Names.size());
        Nick.getInstance();
        final String Nicks = Nick.Names.get(zufall);
        return Nicks;
    }
    
    static {
        NickManager.NamePlayers = new HashMap<UUID, String>();
    }
}
