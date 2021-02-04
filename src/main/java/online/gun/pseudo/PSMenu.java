package online.gun.pseudo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.scheduler.BukkitRunnable;

public class PSMenu {
	private PseudoServerMain plugin;
	private Player player;
	private Inventory inventory;
	private PSFile psFile;
	private HashMap<ItemStack, String> itemMap = new HashMap<ItemStack, String>();
	private int page = 0;
	public PSMenu(PseudoServerMain plugin, Player player) {
		this.player = player;
		this.plugin = plugin;
		this.psFile = new PSFile(plugin, player);
		inventory = this.plugin.getServer().createInventory(this.player, InventoryType.HOPPER, "Set");
		fillMenu(psFile.getEquipmentItemStacks(), 0);
		inventory.setItem(4, new ItemStack(Material.BARRIER));
		player.openInventory(inventory);
	}
	public void fillMenu2(List<HashMap<ItemStack,String>> list, int start) {
		int j = 0;
		if (list==null) return;
		for (int i = start; j < list.size(); i ++) {
			if (i >= inventory.getSize()) break;
			ItemStack item = list.get(j).keySet().iterator().next();
			String name = list.get(j).get(item);
			itemMap.put(item,name);
			inventory.setItem(i, item);
			j ++;
		}
	}
	public void fillMenu(List<ItemStack> list, int start) {
		int j = 0;
		if (list==null) return;
		for (int i = start; j < list.size(); i ++) {
			//TODO add "page"
			if (i >= inventory.getSize()) break;
			inventory.setItem(i, list.get(j));
			j ++;
		}
	}
	int last;
	public void openNext(int slot, ItemStack item) {
		if (page == 0) {
			last = slot;
			if (slot==4) {
				player.closeInventory();
				return;
			}
			inventory = plugin.getServer().createInventory(player, 36, "Weapons");
			player.sendMessage(String.valueOf(page)+" "+String.valueOf(slot));
			fillMenu2(psFile.getWeaponItemStacks(slot), 9);
			new BukkitRunnable() {
	            public void run() {
	    	    	player.openInventory(inventory);
	    	    	page = 1;
	            }
			}.runTaskLater(plugin, 5);
		}
		else if (page == 1) {
			String weapon = itemMap.get(item);
			psFile.changeWeapon(weapon,last);
        	player.closeInventory();
		}
	}
	public void openCraft(int slot) {
		if (page==0) {
			last = slot;
			if (slot==4) {
				player.closeInventory();
				return;
			}
			inventory = plugin.getServer().createInventory(player, InventoryType.WORKBENCH, "Weapons");
			ItemStack weapon;
			HashMap<String, ItemStack> current_attachments_map;
			HashMap<String, List<ItemStack>> attachments_map;
			List<ItemStack> attachments = new ArrayList<ItemStack>();
			if (slot==0) {
				weapon = psFile.getMainWeapon();
				current_attachments_map = psFile.getCurrentAttachments(psFile.getMainWeaponName());
				attachments_map = psFile.getAllAttachments(psFile.getMainWeaponName(),"Main");
			}
			else if (slot==1) {
				weapon = psFile.getSubWeapon();
				current_attachments_map = psFile.getCurrentAttachments(psFile.getSubWeaponName());
				attachments_map = psFile.getAllAttachments(psFile.getSubWeaponName(),"Sub");
			}
			else {
				weapon = new ItemStack(Material.BARRIER);
				current_attachments_map = new HashMap<String, ItemStack>();
				attachments_map = new HashMap<String, List<ItemStack>>();
	        	player.closeInventory();
			}
			for(Iterator<String> iterator = attachments_map.keySet().iterator(); iterator.hasNext(); ) {
				String value = iterator.next();
				attachments.add(new ItemStack(Material.GREEN_STAINED_GLASS));
				for (ItemStack item : attachments_map.get(value)) {
					attachments.add(item);
				}
			}
			fillMenu(attachments, 10);
			inventory.setItem(0, weapon);
			inventory.setItem(2, current_attachments_map.get("sight"));
			inventory.setItem(4, current_attachments_map.get("burrel"));
			inventory.setItem(5, weapon);
			inventory.setItem(6, current_attachments_map.get("stock"));
			inventory.setItem(6, current_attachments_map.get("magazine"));
			String[] tops = {"   "," A "};
			String[] middles = {"BWD"," WD","BW "," W "};
			String[] bottoms = {" C ","   "};
			for (String top : tops) {
				for (String middle : middles) {
					for (String bottom : bottoms) {
						NamespacedKey key = new NamespacedKey(plugin, "pseudo-gun-"+plugin.duplicateNumber);
						ShapedRecipe weaponRecipe = new ShapedRecipe(key, weapon);
						weaponRecipe.shape(top,middle,bottom);
						if (top.contains("A"))
							weaponRecipe.setIngredient('A', Material.SLIME_BALL);
						if (middle.contains("B"))
							weaponRecipe.setIngredient('B', Material.BLUE_DYE);
						if (bottom.contains("C"))
						weaponRecipe.setIngredient('C', Material.YELLOW_DYE);
						if (bottom.contains("D"))
							weaponRecipe.setIngredient('D', Material.GREEN_DYE);
						weaponRecipe.setIngredient('W', weapon.getType());
						plugin.getServer().addRecipe(weaponRecipe);
						plugin.duplicateNumber ++;
					}
				}
			}
			new BukkitRunnable() {
	            public void run() {
	    	    	player.openInventory(inventory);
	    	    	page = 3;
	            }
			}.runTaskLater(plugin, 5);
		}
	}
	public Inventory getInventory() {
		return inventory;
	}
}
