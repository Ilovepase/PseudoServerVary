package online.gun.pseudo;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PSListener implements Listener {
	PseudoServerMain plugin;
	public PSListener(PseudoServerMain plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			plugin.rightClick(e);
	}
	@EventHandler
	public void onMenuClick(InventoryClickEvent e) {
		if (e.isLeftClick())
			plugin.menuClickLeft(e);
		else if (e.isRightClick())
			plugin.menuClickRight(e);
	}
}
