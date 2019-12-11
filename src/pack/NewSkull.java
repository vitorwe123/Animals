package pack;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("deprecation")

public class NewSkull extends ItemStack{
	
	public NewSkull(int modelData, String owner, String name, List<String> lore, Inventory inv, int slot) {
		
		ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
		SkullMeta skullmeta = (SkullMeta) skull.getItemMeta();
		skullmeta.setCustomModelData(modelData);
		skullmeta.setOwner(owner);
		skullmeta.setDisplayName(name);
		skullmeta.setLore(lore);
		skull.setItemMeta(skullmeta);
		inv.setItem(slot, skull);
		
	}
	
}