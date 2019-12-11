# Animals

<br/>
<p align="center">
<img src="https://github.com/vitorwe123/Animals/blob/master/Images/Screenshot_3.png">
</p>
<br/>

## Description
> **Animals** is a Minecraft multiplayer plugin developed by me which **opens an Inventory with three differente options of animals to spawn**.  

> **Each** animal has different values of cost (exp level) and cooldown. These values can be set in the *configuration file*.  

> **This inventory** can be opened when **right clicking with the specified item** on players hand, which also can be set on the *configuration file*.  

<br/>
<p align="right">
<img src="https://github.com/vitorwe123/Animals/blob/master/Images/Screenshot_4.png" width="363.5" height="290">
</p>

## Configuration file
> The *config.yml* is useful to **change each value** of cost, cooldown and even the item you will be holding to open the inventory.  

> **The parameters** for exp: and cooldown: **MUST** be numbers. The parameters for MaterialItem: **MUST** be the material of the item you want to use.

> *Otherwise, it won`t work*.  

```
Pig:
  exp: 2
  cooldown: 15
Sheep:
  exp: 3
  cooldown: 30
Cow:
  exp: 5
  cooldown: 60
ItemMaterial: STICK
```

<br/>

## Extra information
> *This plugin was made using 1.14 Minecraft version*.  

> **The following methods were written to avoid any trouble** with the *configuration file*.
```
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
 ```
<p align="left">
<img src="https://github.com/vitorwe123/Animals/blob/master/Images/Screenshot_1.png">
</p>
<br/>
<br/>

```
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
```
<p align="right">
<img src="https://github.com/vitorwe123/Animals/blob/master/Images/Screenshot_2.png">
</p>
<br/>
<br/>

> Another **useful** written line is the **CustomModelData condition**.
> It gives a *number* to each of the skulls in the inventory in order to the player dont get in trouble with another animal skull.

```
//Pig
if(e.getCurrentItem().getItemMeta().getCustomModelData() == 01110000) {
```
