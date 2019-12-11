package pack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Enums;

public class Main extends JavaPlugin implements Listener {
	
	FileConfiguration file = getConfig();
	Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.WHITE + "Animals");
	HashMap<String, Integer> hash = new HashMap<String, Integer>(); // Keep players in cooldown even if the player quits the server.
	Timer timer = new Timer(); // Manage player`s cooldown.
	boolean valid = true; // Checks if there is no wrong parameters in config.yml // datacheck().
	Material item = Material.getMaterial(file.get("ItemMaterial").toString());
	ConsoleCommandSender cs = getServer().getConsoleSender();
	
	
	public void onEnable() {
		saveDefaultConfig();
		datacheck();  // Checks if there is no wrong parameters in config.yml. Sets valid boolean.
		materialcheck(); // Checks if the material in config.yml really exists.
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		cs.sendMessage(ChatColor.RED + "Shutting down...");
	}
	
	
	
	
	
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e) {
		Player p = (Player) e.getPlayer();
		
		Action action = e.getAction();
		
		// Creates the animals icon on the inventory gui.
		// Tried to minimize the number of lines by creating the NewSkull class.
		
		if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) { // Opening the inventory.
			if(p.getInventory().getItemInMainHand().getType().equals(item)) {
				
				if(valid) { // If the parameters are ok.
					if (hash.containsKey(p.getName())) { // Checks if the players is in the cooldown list.
						
						p.sendMessage(ChatColor.RED + "You must wait " + ChatColor.WHITE + hash.get(p.getName()) + ChatColor.RED + " seconds to use again.");
						return;
						
					} else {
						//Pig
						List<String> piglore = new ArrayList<String>();
						piglore.add(" ");
						piglore.add(" " + ChatColor.WHITE + "Cost: " + ChatColor.GREEN + file.getInt("Pig.exp") +" exp" + " ");
						piglore.add(" " + ChatColor.WHITE + "Cooldown: " + ChatColor.RED + file.getInt("Pig.cooldown") +" seconds" + " ");
						piglore.add(" ");
						new NewSkull(01110000, "MHF_Pig", ChatColor.WHITE + "Pig", piglore, inv, 12);
						
						//Sheep
						List<String> sheeplore = new ArrayList<String>();
						sheeplore.add(" ");
						sheeplore.add(" " + ChatColor.WHITE + "Cost: " + ChatColor.GREEN + file.getInt("Sheep.exp") + " exp" + " ");
						sheeplore.add(" " + ChatColor.WHITE + "Cooldown: " + ChatColor.RED + file.getInt("Sheep.cooldown") + " seconds" + " ");
						sheeplore.add(" ");
						new NewSkull(01110011, "MHF_Sheep", ChatColor.WHITE + "Sheep", sheeplore, inv, 13);
						
						//Cow
						List<String> cowlore = new ArrayList<String>();
						cowlore.add(" ");
						cowlore.add(" " + ChatColor.WHITE + "Cost: " + ChatColor.GREEN + file.getInt("Cow.exp") + " exp" + " ");
						cowlore.add(" " + ChatColor.WHITE + "Cooldown: " + ChatColor.RED + file.getInt("Cow.cooldown") +" seconds" + " ");
						cowlore.add(" ");
						new NewSkull(01100011, "MHF_Cow", ChatColor.WHITE + "Cow", cowlore, inv, 14);
						p.openInventory(inv);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerClickInv(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try { // I wrote this to catch an exception thrown by clicking on another slot when the inventory is opened. 
			if(e.getCurrentItem().getItemMeta().hasCustomModelData()) { // This one here I decided to include in order to avoid some trouble with another skull. 
				
				// The CustomModelData is given by taking the first letter (Pig, P) and converting to binary.
				
				//Pig
				if(e.getCurrentItem().getItemMeta().getCustomModelData() == 01110000) {
					
					if(p.getLevel() >= file.getInt("Pig.exp")) {
						p.getWorld().spawn(p.getLocation(), Pig.class);
						p.setLevel(p.getLevel() - file.getInt("Pig.exp"));
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
						p.closeInventory();
						hash.put(p.getName(), file.getInt("Pig.cooldown")); // The time is unique for which animal and it can be changed in the config.yml.
						timer.schedule(new TimerTask() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(hash.get(p.getName()) < 1) {
									hash.remove(p.getName());
									cancel();
									return;
								}
								hash.put(p.getName(), hash.get(p.getName()) - 1);
								
							}
						}, 0, 1000);
						
					} else {
						p.closeInventory();
						p.sendMessage(ChatColor.RED + "You don`t have enough experience level."); // Sending a friendly message to a low level player.
						return;
					}
					

				}
				
				//Sheep
				if(e.getCurrentItem().getItemMeta().getCustomModelData() == 01110011) {
					
					if(p.getLevel() >= file.getInt("Sheep.exp")) {
						p.getWorld().spawn(p.getLocation(), Sheep.class);
						p.setLevel(p.getLevel() - file.getInt("Sheep.exp"));
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
						p.closeInventory();
						hash.put(p.getName(), file.getInt("Sheep.cooldown"));
						timer.schedule(new TimerTask() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(hash.get(p.getName()) < 1) {
									hash.remove(p.getName());
									cancel();
									//p.sendMessage(ChatColor.GREEN + "Time is " + ChatColor.WHITE + "over.");
									return;
								}
								hash.put(p.getName(), hash.get(p.getName()) - 1);
								
							}
						}, 0, 1000);
						
					} else {
						p.closeInventory();
						p.sendMessage(ChatColor.RED + "You don`t have enough experience level.");
						return;
					}
					
					
				}
				
				//Cow
				if(e.getCurrentItem().getItemMeta().getCustomModelData() == 01100011) {
					
					if(p.getLevel() >= file.getInt("Cow.exp")) {
						p.getWorld().spawn(p.getLocation(), Cow.class);
						p.setLevel(p.getLevel() - file.getInt("Cow.exp"));
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
						p.closeInventory();
						hash.put(p.getName(), file.getInt("Cow.cooldown"));
						timer.schedule(new TimerTask() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(hash.get(p.getName()) < 1) {
									hash.remove(p.getName());
									cancel();
									//p.sendMessage(ChatColor.GREEN + "Time is " + ChatColor.WHITE + "over.");
									return;
								}
								hash.put(p.getName(), hash.get(p.getName()) - 1);
								
							}
						}, 0, 1000);
					} else {
						p.closeInventory();
						p.sendMessage(ChatColor.RED + "You don`t have enough experience level.");
						return;
					}
					
					
				}
			}
		
	    } catch(NullPointerException ex) {
	    	return;
	    }
	}
	
	public void datacheck() { // This method has the task to check exp and cooldown parameters, one by one, of the configuration file in order to prohibit letters or any kind of character but numbers, avoiding any possible trouble.
		ArrayList<String> data = new ArrayList<String>();
		data.add("Pig.exp"); //0
		data.add("Pig.cooldown"); //1
		data.add("Sheep.exp"); //2
		data.add("Sheep.cooldown"); //3
		data.add("Cow.exp"); //4
		data.add("Cow.cooldown"); //5
		for(int i = 0; i < 6; i++) {
			if(!(file.get(data.get(i)) instanceof Integer)) {
				cs.sendMessage(ChatColor.RED + "Invalid config file. Invalid parameters -> " + ChatColor.WHITE + data.get(i) + ": " + file.get(data.get(i)));
				valid = false;
				return;
			}
			valid = true;
		}
	}
	
	public void materialcheck() { // Checks if the material in config.yml really exists.
		if(Enums.getIfPresent(Material.class, file.get("ItemMaterial").toString()).isPresent()) { 
			if(valid) {
				cs.sendMessage(ChatColor.GREEN + "Working... ");
			}
		} else {
			cs.sendMessage(ChatColor.RED + "Invalid config file. Invalid ItemMaterial -> " +  ChatColor.WHITE + file.get("ItemMaterial"));
			valid = false;
			return;
		}
	}
	
}