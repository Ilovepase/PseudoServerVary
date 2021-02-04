package online.gun.pseudo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PSFile {
	PseudoServerMain plugin;
	String fileName;
	Player player;
	File weaponsfile;
	File setsFile;
	File gunsFile;
	FileConfiguration weaponsFileConfiguration;
	FileConfiguration setsFileConfiguration;
	FileConfiguration gunsFileConfiguration;
	public PSFile(PseudoServerMain plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
		this.fileName = player.getUniqueId().toString();
		weaponsfile = new File(plugin.getDataFolder(), "player_data/"+fileName+"/weapons.yml");
		setsFile = new File(plugin.getDataFolder(), "player_data/"+fileName+"/sets.yml");
		gunsFile = new File(plugin.getDataFolder(), "guns.yml");
		weaponsFileConfiguration = YamlConfiguration.loadConfiguration(weaponsfile);
		setsFileConfiguration = YamlConfiguration.loadConfiguration(setsFile);
		gunsFileConfiguration = YamlConfiguration.loadConfiguration(gunsFile);
		if (!weaponsfile.exists()) setDefaultValue();
		saveFile(weaponsFileConfiguration, weaponsfile);
		saveFile(setsFileConfiguration, setsFile);
	}
	
	public List<ItemStack> getEquipmentItemStacks() {
		ItemStack main = getEquipmentItemStack("Main");
		ItemStack sub = getEquipmentItemStack("Sub");
		ItemStack knife = getEquipmentItemStack("Knife");
		ItemStack grenade = getEquipmentItemStack("Grenade");
		return Arrays.asList(main, sub, knife, grenade);
	}
	
	public void changeWeapon(String weapon,int num) {
		String category;
		if (num==0) category = "Main";
		else if (num==1) category = "Sub";
		else if (num==2) category = "Knife";
		else if (num==3) category = "Grenade";
		else return;
		setsFileConfiguration.set("Equip."+category, weapon);
		saveFile(setsFileConfiguration, setsFile);
	}
	
	public List<HashMap<ItemStack,String>> getWeaponItemStacks(int num) {
		String category = "";
		if (num==0) category = "Main";
		else if (num==1) category = "Sub";
		else if (num==2) category = "Knife";
		else if (num==3) category = "Grenade";
		else return null;
		List<HashMap<ItemStack, String>> weaponList = new ArrayList<HashMap<ItemStack,String>>();
		for (String weapon : getWeapons(num)) {
			String skin = setsFileConfiguration.getString(category+"."+weapon+".skin");
			if (skin==null) skin = "default";
			ItemStack item = gunsFileConfiguration.getItemStack(weapon+".skin."+skin);
			final ItemStack _item = item;
			final String _weapon = weapon;
			@SuppressWarnings("serial")
			HashMap<ItemStack, String> map = new HashMap<ItemStack, String>() {
				{
					put(_item,_weapon);
				}
			};
			weaponList.add(map);
		}
		return weaponList;
	}
	
	public ItemStack getMainWeapon() {
		String weapon = setsFileConfiguration.getString("Equip.Main");
		String skin = setsFileConfiguration.getString(weapon+".skin");
		if (skin==null) skin = "default";
		ItemStack weaponStack = gunsFileConfiguration.getItemStack(weapon+".skin."+skin);
		return weaponStack;
	}
	
	public ItemStack getSubWeapon() {
		String weapon = setsFileConfiguration.getString("Equip.Sub");
		String skin = setsFileConfiguration.getString(weapon+".skin");
		if (skin==null) skin = "default";
		ItemStack weaponStack = gunsFileConfiguration.getItemStack(weapon+".skin."+skin);
		return weaponStack;
	}
	
	public HashMap<String,List<ItemStack>> getAllAttachments (String weapon, String category) {
		 List<String> sights = weaponsFileConfiguration.getStringList("Weapons."+category+"."+weapon+".attachments.sights");
		 List<String> magazines = weaponsFileConfiguration.getStringList("Weapons."+category+"."+weapon+".attachments.magazines");
		 List<String> barrels = weaponsFileConfiguration.getStringList("Weapons."+category+"."+weapon+".attachments.burrels");
		 List<String> stocks = weaponsFileConfiguration.getStringList("Weapons."+category+"."+weapon+".attachments.stocks");
		 HashMap<String, List<ItemStack>> map = new HashMap<String, List<ItemStack>>();
		 map.put("sight",getAttachments(sights));
		 map.put("magazine",getAttachments(magazines));
		 map.put("barrel",getAttachments(barrels));
		 map.put("stock",getAttachments(stocks));
		 return map;
	}
	
	public List<ItemStack> getAttachments (List<String> attachments) {
		List<ItemStack> itemStacks = new ArrayList<ItemStack>();
		for (String attachment : attachments) {
			itemStacks.add(gunsFileConfiguration.getItemStack(attachment));
		}
		return itemStacks;
	}
	
	public String getMainWeaponName() {
		String weapon = setsFileConfiguration.getString("Equip.Main");
		return weapon;
	}
	
	public String getSubWeaponName() {
		String weapon = setsFileConfiguration.getString("Equip.Sub");
		return weapon;
	}
	
	public void addSetsValue() {
		
	}
	
	public HashMap<String, ItemStack> getCurrentAttachments (String weapon) {
		String sight = setsFileConfiguration.getString(weapon+".attachments.sight");
		String magazine = setsFileConfiguration.getString(weapon+".attachments.magazine");
		String barrel = setsFileConfiguration.getString(weapon+".attachments.barrel");
		String stock = setsFileConfiguration.getString(weapon+".attachments.stock");
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		if (sight==null || magazine==null || barrel==null || stock==null) return new HashMap<String, ItemStack>();
		map.put("sight", gunsFileConfiguration.getItemStack(sight));
		map.put("magazine", gunsFileConfiguration.getItemStack(magazine));
		map.put("barrel", gunsFileConfiguration.getItemStack(barrel));
		map.put("stock", gunsFileConfiguration.getItemStack(stock));
		return map;
	}
	
	public ItemStack getEquipmentItemStack(String category) {
		String equip = setsFileConfiguration.getString("Equip."+category);
		String skin = setsFileConfiguration.getString(category+"."+equip+".skin");
		if (skin==null) skin = "default";
		String path = equip+".skin."+skin;
		player.sendMessage(equip+".skin."+skin);
		if (!gunsFileConfiguration.isItemStack(path)) return new ItemStack(Material.IRON_HOE);
		return gunsFileConfiguration.getItemStack(path);
	}
	
	public void writeItemStack(ItemStack item, String name, String skin) {
		gunsFileConfiguration.createSection(name+"."+skin);
		gunsFileConfiguration.set(name+"."+skin, item);
		saveFile(gunsFileConfiguration, gunsFile);
	}
	
	//セーブ
	public void saveFile(FileConfiguration fc, File f) {
		try {
			fc.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//持っている武器をcategoryで全て返すメソッド
	public List<String> getWeapons(int num) {
		String category = "";
		if (num==0) category = "Main";
		else if (num==1) category = "Sub";
		else if (num==2) category = "Knife";
		else if (num==3) category = "Grenade";
		else return null;
		String path = "Weapons."+category;
		//if (!weaponsFileConfiguration.contains(path)) weaponsFileConfiguration.set(path, "default");
		Set<String> set = weaponsFileConfiguration.getConfigurationSection(path).getKeys(false);
		List<String> weaponsList = new ArrayList<String>();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			weaponsList.add(iterator.next());
		}
		return weaponsList;
	}
	
	//新規に武器を与える際に呼ばれるメソッド
	public void setDefaultValue() {

		weaponsFileConfiguration.createSection("Sort");
		weaponsFileConfiguration.set("Sort", 7);
		
		setDefaultWeaponsValue(0, "M16");
		setDefaultWeaponsValue(1, "G17");
		setDefaultWeaponsValue(2, "knife");
		setDefaultWeaponsValue(3, "M67");

		setsFileConfiguration.createSection("Equip.Main");
		setsFileConfiguration.createSection("Equip.Sub");
		setsFileConfiguration.createSection("Equip.Knife");
		setsFileConfiguration.createSection("Equip.Grenade");
		setsFileConfiguration.set("Equip.Main", "M16");
		setsFileConfiguration.set("Equip.Sub", "G17");
		setsFileConfiguration.set("Equip.Knife", "knife");
		setsFileConfiguration.set("Equip.Grenade", "M67");
		
		saveFile(setsFileConfiguration, setsFile);
		saveFile(weaponsFileConfiguration, weaponsfile);
	}
	
	public boolean setAttachmentValue(int num, String name, String category2, String name2) {
		String category;
		if (num==0) category = "Main";
		else if (num==1) category = "Sub";
		else if (num==2) category = "Knife";
		else if (num==3) category = "Grenade";
		else category = "CategoryError";
		String path = "Weapons."+category+"."+name+".attachments."+category2;
		List<String> attachments = weaponsFileConfiguration.getStringList(path);
		if (!gunsFileConfiguration.getStringList(name+".attachments."+category2).contains(name2)) return false;
		attachments.add(name2);
		weaponsFileConfiguration.set(path,attachments);
		return true;
	}
	
	//ネイティブな武器を入力するクラス
	public boolean setDefaultWeaponsValue(int num, String name) {
		String skins[] = {"default"};
		String attachments[] = {"default"};
		String category;
		if (num==0) category = "Main";
		else if (num==1) category = "Sub";
		else if (num==2) category = "Knife";
		else if (num==3) category = "Grenade";
		else category = "CategoryError";
		String path = "Weapons."+category+"."+name;
		//武器が既に存在する場合はfalseを返す
		if (weaponsFileConfiguration.contains(path))
			return false;
		if (!gunsFileConfiguration.contains(name)) {
			player.sendMessage("§4guns.ymlに登録されていない武器です！");
			return false;
		}
		weaponsFileConfiguration.createSection(path+".exp");
		weaponsFileConfiguration.createSection(path+".date");
		weaponsFileConfiguration.createSection(path+".lastuse");
		weaponsFileConfiguration.createSection(path+".skins");
		weaponsFileConfiguration.createSection(path+".attachments");
		weaponsFileConfiguration.createSection(path+".attachments.sights");
		weaponsFileConfiguration.createSection(path+".attachments.magazines");
		weaponsFileConfiguration.createSection(path+".attachments.barrels");
		weaponsFileConfiguration.createSection(path+".attachments.stocks");
		
		Calendar calendar = Calendar.getInstance();
		weaponsFileConfiguration.set(path+".exp", 0);
		weaponsFileConfiguration.set(path+".date",calendar);
		weaponsFileConfiguration.set(path+".skins", skins);
		weaponsFileConfiguration.set(path+".attachments.sights",attachments);
		weaponsFileConfiguration.set(path+".attachments.magazines",attachments);
		weaponsFileConfiguration.set(path+".attachments.barrels",attachments);
		weaponsFileConfiguration.set(path+".attachments.stocks",attachments);
		saveFile(weaponsFileConfiguration, weaponsfile);
		return true;
	}
}
