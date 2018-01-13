package org.inventivetalent.paintingselector;

import org.bstats.bukkit.Metrics;
import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintingSelector extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		new Metrics(this);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ((command.getName().equalsIgnoreCase("paintingselector")) &&
				((sender instanceof Player)) &&
				(sender.hasPermission("paintingselector.use"))) {
			((Player) sender).getInventory().addItem(Selector());
			sender.sendMessage("§6[PaintingSelector]§e Here you go! §lSNEAK + RIGHT-CLICK §eto change the painting.");
		}
		return false;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getPlayer().getInventory().getItemInMainHand() == null) {
			return;
		}
		if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
			return;
		}
		if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) {
			return;
		}
		if ((e.getPlayer().getInventory().getItemInMainHand().getType().equals(Selector().getType())) && (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().startsWith("§6Painting Selector")) &&
				(e.getPlayer().hasPermission("paintingselector.use")) &&
				(e.getPlayer().isSneaking()) && ((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR))) {
			e.getPlayer().getInventory().setItemInMainHand(scrollTypes(e.getPlayer().getInventory().getItemInMainHand()));
			e.setCancelled(true);
		}
	}

	public ItemStack scrollTypes(ItemStack item) {
		ItemStack item1 = item.clone();
		ItemMeta meta = item1.getItemMeta();
		String prevName = meta.getDisplayName();

		String artName = prevName.substring(prevName.lastIndexOf("[") + 1, prevName.length() - 1);
		Art art;
		if ((Art.getByName(artName) != null) && (Art.getByName(artName).ordinal()+1<Art.values().length)) {
			art = Art.values()[Art.getByName(artName).ordinal()+1];
		} else {
			art = Art.values()[0];
		}
		meta.setDisplayName("§6Painting Selector §7[" + art.name() + "]");
		item.setItemMeta(meta);
		return item;
	}

	@EventHandler
	public void onPaintingSpawn(HangingPlaceEvent e) {
		if ((e.getEntity() instanceof Painting)) {
			if (e.getPlayer().getInventory().getItemInMainHand() == null) {
				return;
			}
			if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
				return;
			}
			if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) {
				return;
			}
			if ((e.getPlayer().getInventory().getItemInMainHand().getType().equals(Selector().getType())) && (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().startsWith("§6Painting Selector"))) {
				String prevName = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
				String artName = prevName.substring(prevName.lastIndexOf("[") + 1, prevName.length() - 1);
				Art art = Art.getByName(artName);
				Painting p = (Painting) e.getEntity();
				p.setArt(art);
			}
		}
	}

	public static ItemStack selector;

	public ItemStack Selector() {
		if (selector != null) {
			return selector;
		}
		ItemStack item = new ItemStack(Material.PAINTING);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6Painting Selector §7[" + Art.values()[0] + "]");
		item.setItemMeta(meta);
		selector = item;
		return item;
	}

}
