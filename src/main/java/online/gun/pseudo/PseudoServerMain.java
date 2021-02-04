package online.gun.pseudo;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import online.gun.pseudo.command.CommandManager;

public class PseudoServerMain extends JavaPlugin {
	public long duplicateNumber;
	private PSListener listener;
	private CommandManager commandManager;
	private HashMap<UUID, PSMenu> player_menus = new HashMap<UUID, PSMenu>();
	private HashMap<UUID, PSFile> player_files = new HashMap<UUID, PSFile>();
	@Override
	public void onEnable() {
		listener = new PSListener(this);
		commandManager = new CommandManager(this);
		getServer().getPluginManager().registerEvents(listener, this);
		getCommand("pgs").setExecutor(commandManager);
	}
	
	public void rightClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getMaterial()==Material.AIR) return;
		if (e.getMaterial()==Material.EMERALD) {
			player_menus.put(player.getUniqueId(),new PSMenu(this, player));
		}
	}
	
	public void writeWeapon (Player player, String name, String skin) {
		if (!player_files.containsKey(player.getUniqueId()))
			player_files.put(player.getUniqueId(), new PSFile(this, player));
		player_files.get(player.getUniqueId()).writeItemStack(player.getInventory().getItemInMainHand(), name, skin);
	}
	
	public void menuClickLeft (InventoryClickEvent e) {
		if (e.getViewers().size()==0) return;
		Player player = (Player)(e.getViewers().get(0));
		int num = e.getSlot();
		if (num==-999) return;
		if (!player_menus.containsKey(player.getUniqueId())) return;
		if (!player_menus.get(player.getUniqueId()).getInventory().equals(e.getInventory())) return;
		e.setCancelled(true);
		player_menus.get(player.getUniqueId()).openNext(num, e.getCurrentItem());
	}
	
	public void menuClickRight (InventoryClickEvent e) {
		if (e.getViewers().size()==0) return;
		Player player = (Player)(e.getViewers().get(0));
		int slot = e.getSlot();
		if (slot==-999) return;
		if (!player_menus.containsKey(player.getUniqueId())) return;
		if (!player_menus.get(player.getUniqueId()).getInventory().equals(e.getInventory())) return;
		e.setCancelled(true);
		player_menus.get(player.getUniqueId()).openCraft(slot);
	}
	
	public void giveWeapon(Player player, String weapon) {
		if (!player_files.containsKey(player.getUniqueId()))
			player_files.put(player.getUniqueId(),new PSFile(this, player));
		player_files.get(player.getUniqueId()).setDefaultWeaponsValue(0, weapon);
	}
}
