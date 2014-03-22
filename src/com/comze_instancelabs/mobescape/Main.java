package com.comze_instancelabs.mobescape;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import com.comze_instancelabs.mobescape.V1_6.V1_6Dragon;
import com.comze_instancelabs.mobescape.V1_6.V1_6Wither;
/*import net.minecraft.server.v1_7_R1.AttributeInstance;
 import net.minecraft.server.v1_7_R1.EntityInsentient;
 import net.minecraft.server.v1_7_R1.EntityTypes;
 import net.minecraft.server.v1_7_R1.GenericAttributes;*/
/*import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
 import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
 import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;*/
import com.comze_instancelabs.mobescape.V1_7.V1_7Dragon;
import com.comze_instancelabs.mobescape.V1_7.V1_7Wither;

public class Main extends JavaPlugin implements Listener {

	/*
	 * 
	 * This is based off the ColorMatch arena system
	 *
	 */

	/*
	 * de setmainlobby
	 * 
	 * de createarena [name] de setlobby [name] de setfinish [name] de setbounds
	 * [name] [low/high] de savearena [name]
	 */

	public static Economy econ = null;

	/**
	 * arena -> whether ingame or not
	 */
	public static HashMap<String, Boolean> ingame = new HashMap<String, Boolean>();

	/**
	 * arena -> task
	 */
	public static HashMap<String, BukkitTask> tasks = new HashMap<String, BukkitTask>();

	/**
	 * player -> arena
	 */
	public static HashMap<Player, String> arenap = new HashMap<Player, String>();
	public static HashMap<String, String> arenap_ = new HashMap<String, String>();

	/**
	 * player -> inventory
	 */
	public static HashMap<Player, ItemStack[]> pinv = new HashMap<Player, ItemStack[]>();

	/**
	 * player -> whether lost or not
	 */
	public static HashMap<Player, String> lost = new HashMap<Player, String>();

	/**
	 * player -> kit
	 */
	public static HashMap<Player, String> pkit = new HashMap<Player, String>();

	/**
	 * arena -> countdown finished or not
	 */
	public static HashMap<String, Boolean> astarted = new HashMap<String, Boolean>();

	/**
	 * player -> kit
	 */
	public static HashMap<Player, Boolean> pkit_use = new HashMap<Player, Boolean>();

	/**
	 * player -> place
	 */
	public static HashMap<Player, Integer> pplace = new HashMap<Player, Integer>();

	
	int default_max_players = 4;
	int default_min_players = 3;

	boolean economy = true;
	int reward = 30;
	int itemid = 264;
	int itemamount = 1;
	boolean command_reward = false;
	String cmd = "";
	public boolean start_announcement = false;
	public boolean join_announcement = false;
	public boolean winner_announcement = false;
	public String dragon_name = "Ender Dragon";
	public double mob_speed = 1.0;
	public static boolean mode1_6 = false;
	public int destroy_radius = 10;
	public boolean last_man_standing = true;
	public boolean spawn_winnerfirework = true;
	public boolean spawn_falling_blocks = true;
	public boolean die_behind_mob = false;
	public boolean give_jumper_as_default_kit = true;
	public int kit_delay_in_seconds = 7;
	
	public int start_countdown = 5;

	public String saved_arena = "";
	public String saved_lobby = "";
	public String saved_mainlobby = "";
	public String not_in_arena = "";
	public String reloaded = "";
	public String arena_ingame = "";
	public String arena_invalid = "";
	public String arena_invalid_sign = "";
	public String you_fell = "";
	public String arena_invalid_component = "";
	public String you_won = "";
	public String starting_in = "";
	public String starting_in2 = "";
	public String arena_full = "";
	public String removed_arena = "";
	public String winner_an = "";
	public String noperm = "";
	public String nopermkit = "";
	public String saved_finish = "";
	public String saved_spawn = "";
	public String noperm_arena = "";
	public String kit_delay_message = "";
	public String your_place = "";
	
	public String sign_top = "";
	public String sign_second_join = "";
	public String sign_second_ingame = "";
	public String sign_second_restarting = "";

	// announcements
	public String starting = "";
	public String started = "";
	public String join_announcement_ = "";

	public String type = "dragon"; 
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		if (Bukkit.getVersion().contains("1.6.4") || Bukkit.getVersion().contains("1.6.2")) {
			mode1_6 = true;
			getLogger().info("Turned on 1.6.4 mode.");
		}
		registerEntities();

		getConfig().options().header("I recommend you to set auto_updating to true for possible future bugfixes. If use_economy is set to false, the winner will get the item reward.");
		getConfig().addDefault("config.auto_updating", true);
		getConfig().addDefault("config.start_countdown", 5);
		getConfig().addDefault("config.lobby_countdown", 5);
		getConfig().addDefault("config.default_max_players", 4);
		getConfig().addDefault("config.default_min_players", 3);
		getConfig().addDefault("config.use_economy_reward", true);
		getConfig().addDefault("config.money_reward_per_game", 30);
		getConfig().addDefault("config.itemid", 264); // diamond
		getConfig().addDefault("config.itemamount", 1);
		getConfig().addDefault("config.use_command_reward", false);
		getConfig().addDefault("config.command_reward", "pex user <player> group set DragonPro;msg <player> Nice, dude.");
		getConfig().addDefault("config.join_announcement", false);
		getConfig().addDefault("config.start_announcement", false);
		getConfig().addDefault("config.winner_announcement", false);
		getConfig().addDefault("config.mob_speed", 1.0D);
		getConfig().addDefault("config.mob_healthbar_name", "Ender Dragon");
		getConfig().addDefault("config.destroy_radius", 10);
		getConfig().addDefault("config.last_man_standing", true);
		getConfig().addDefault("config.spawn_winnerfirework", true);
		getConfig().addDefault("config.mob_type", "dragon");
		getConfig().addDefault("config.game_on_join", false);
		getConfig().addDefault("config.jumper_boost_factor", 1.2D);
		getConfig().addDefault("config.die_behind_mob", false);
		getConfig().addDefault("config.give_jumper_as_default_kit", true);
		getConfig().addDefault("config.kit_delay_in_seconds", 7);
		
		getConfig().addDefault("config.sign_top_line", "&6MobEscape");
		getConfig().addDefault("config.sign_second_line_join", "&a[Join]");
		getConfig().addDefault("config.sign_second_line_ingame", "&c[Ingame]");
		getConfig().addDefault("config.sign_second_line_restarting", "&6[Restarting]");
		getConfig().addDefault("config.spawn_falling_blocks", true);

		getConfig().addDefault("config.kits.jumper.description", "&eRightclick the iron axe to jump.");
		getConfig().addDefault("config.kits.jumper.uses", 4);
		getConfig().addDefault("config.kits.jumper.requires_money", false);
		getConfig().addDefault("config.kits.jumper.requires_permission", false);
		getConfig().addDefault("config.kits.jumper.money_amount", 100);
		getConfig().addDefault("config.kits.jumper.permission_node", "dragonescape.kits.jumper");
		getConfig().addDefault("config.kits.warper.description", "&eWarps you to a nearby player, good for when you're falling and almost dead.");
		getConfig().addDefault("config.kits.warper.uses", 1);
		getConfig().addDefault("config.kits.warper.requires_money", false);
		getConfig().addDefault("config.kits.warper.requires_permission", false);
		getConfig().addDefault("config.kits.warper.money_amount", 200);
		getConfig().addDefault("config.kits.warper.permission_node", "dragonescape.kits.warper");
		getConfig().addDefault("config.kits.tnt.description", "&eRightclick to place a tnt trap, which gives a player blindness for 5 seconds.");
		getConfig().addDefault("config.kits.tnt.uses", 2);
		getConfig().addDefault("config.kits.tnt.requires_money", false);
		getConfig().addDefault("config.kits.tnt.requires_permission", false);
		getConfig().addDefault("config.kits.tnt.money_amount", 300);
		getConfig().addDefault("config.kits.tnt.permission_node", "dragonescape.kits.tnt");

		getConfig().addDefault("strings.saved.arena", "&aSuccessfully saved arena.");
		getConfig().addDefault("strings.saved.lobby", "&aSuccessfully saved lobby.");
		getConfig().addDefault("strings.saved.finish", "&aSuccessfully saved finishline.");
		getConfig().addDefault("strings.saved.spawn", "&aSuccessfully saved spawn.");
		getConfig().addDefault("strings.removed_arena", "&cSuccessfully removed arena.");
		getConfig().addDefault("strings.not_in_arena", "&cYou don't seem to be in an arena right now.");
		getConfig().addDefault("strings.config_reloaded", "&6Successfully reloaded config.");
		getConfig().addDefault("strings.arena_is_ingame", "&cThe arena appears to be ingame.");
		getConfig().addDefault("strings.arena_invalid", "&cThe arena appears to be invalid.");
		getConfig().addDefault("strings.arena_invalid_sign", "&cThe arena appears to be invalid, because a join sign is missing.");
		getConfig().addDefault("strings.arena_invalid_component", "&2The arena appears to be invalid (missing components or misstyped arena)!");
		getConfig().addDefault("strings.you_fell", "&3You fell! Type &6/etm leave &3to leave.");
		getConfig().addDefault("strings.you_won", "&aYou won this round, awesome man! Here, enjoy your reward.");
		getConfig().addDefault("strings.starting_in", "&aStarting in &6");
		getConfig().addDefault("strings.starting_in2", "&a seconds.");
		getConfig().addDefault("strings.arena_full", "&cThis arena is full!");
		getConfig().addDefault("strings.starting_announcement", "&aStarting a new DragonEscape Game in &6");
		getConfig().addDefault("strings.started_announcement", "&aA new DragonEscape Round has started!");
		getConfig().addDefault("strings.winner_announcement", "&6<player> &awon the game on arena &6<arena>!");
		getConfig().addDefault("strings.join_announcement", "&6<player> &ajoined the game (<count>)!");
		getConfig().addDefault("strings.noperm", "&cYou don't have permission.");
		getConfig().addDefault("strings.nopermkit", "&cYou don't have permission to use this kit.");
		getConfig().addDefault("strings.noperm_arena", "&cYou don't have permission to join this arena.");
		getConfig().addDefault("strings.kit_delay_message", "&cYou can use your kit after a delay of <delay> seconds!");
		getConfig().addDefault("strings.your_place", "&eYou came in <place>!");

		getConfig().options().copyDefaults(true);
		if (getConfig().isSet("config.min_players")) {
			getConfig().set("config.min_players", null);
		}
		this.saveConfig();

		getConfigVars();

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
		}

		if (getConfig().getBoolean("config.auto_updating")) {
			Updater updater = new Updater(this, 75590, this.getFile(), Updater.UpdateType.DEFAULT, false);
		}


		if (economy) {
			if (!setupEconomy()) {
				getLogger().severe(String.format("[%s] - No iConomy dependency found! Disabling Economy.", getDescription().getName()));
				economy = false;
			}
		}

	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public void getConfigVars() {
		default_max_players = getConfig().getInt("config.default_max_players");
		default_min_players = getConfig().getInt("config.default_min_players");
		reward = getConfig().getInt("config.money_reward");
		itemid = getConfig().getInt("config.itemid");
		itemamount = getConfig().getInt("config.itemamount");
		economy = getConfig().getBoolean("config.use_economy_reward");
		command_reward = getConfig().getBoolean("config.use_command_reward");
		cmd = getConfig().getString("config.command_reward");
		start_countdown = getConfig().getInt("config.start_countdown");
		start_announcement = getConfig().getBoolean("config.start_announcement");
		winner_announcement = getConfig().getBoolean("config.winner_announcement");
		join_announcement = getConfig().getBoolean("config.join_announcement");
		mob_speed = getConfig().getDouble("config.mob_speed");
		destroy_radius = getConfig().getInt("config.destroy_radius");
		if (mob_speed < 0.05 || mob_speed > 10) {
			mob_speed = 1.0;
		}
		dragon_name = getConfig().getString("config.mob_healthbar_name").replaceAll("&", "§");
		last_man_standing = getConfig().getBoolean("config.last_man_standing");
		spawn_winnerfirework = getConfig().getBoolean("config.spawn_winnerfirework");
		
		type = getConfig().getString("config.mob_type");
		if(!type.equalsIgnoreCase("dragon") && !type.equalsIgnoreCase("wither")){
			type = "dragon";
		}
		spawn_falling_blocks = getConfig().getBoolean("config.spawn_falling_blocks");
		die_behind_mob = getConfig().getBoolean("config.die_behind_mob");
		give_jumper_as_default_kit = getConfig().getBoolean("config.give_jumper_as_default_kit");
		kit_delay_in_seconds = getConfig().getInt("config.kit_delay_in_seconds");

		saved_arena = getConfig().getString("strings.saved.arena").replaceAll("&", "§");
		removed_arena = getConfig().getString("strings.removed_arena").replaceAll("&", "§");
		saved_lobby = getConfig().getString("strings.saved.lobby").replaceAll("&", "§");
		saved_finish = getConfig().getString("strings.saved.finish").replaceAll("&", "§");
		saved_spawn = getConfig().getString("strings.saved.spawn").replaceAll("&", "§");
		saved_mainlobby = "" + ChatColor.GREEN + "Successfully saved main lobby";
		not_in_arena = getConfig().getString("strings.not_in_arena").replaceAll("&", "§");
		reloaded = getConfig().getString("strings.config_reloaded").replaceAll("&", "§");
		arena_ingame = getConfig().getString("strings.arena_is_ingame").replaceAll("&", "§");
		arena_invalid = getConfig().getString("strings.arena_invalid").replaceAll("&", "§");
		arena_invalid_sign = getConfig().getString("strings.arena_invalid_sign").replaceAll("&", "§");
		you_fell = getConfig().getString("strings.you_fell").replaceAll("&", "§");
		arena_invalid_component = getConfig().getString("strings.arena_invalid_component").replace("&", "§");
		you_won = getConfig().getString("strings.you_won").replaceAll("&", "§");
		starting_in = getConfig().getString("strings.starting_in").replaceAll("&", "§");
		starting_in2 = getConfig().getString("strings.starting_in2").replaceAll("&", "§");
		arena_full = getConfig().getString("strings.arena_full").replaceAll("&", "§");
		starting = getConfig().getString("strings.starting_announcement").replaceAll("&", "§");
		started = getConfig().getString("strings.started_announcement").replaceAll("&", "§");
		join_announcement_ = getConfig().getString("strings.join_announcement").replaceAll("&", "§");
		removed_arena = getConfig().getString("strings.removed_arena").replaceAll("&", "§");
		winner_an = getConfig().getString("strings.winner_announcement").replaceAll("&", "§");
		noperm = getConfig().getString("strings.noperm").replaceAll("&", "§");
		nopermkit = getConfig().getString("strings.nopermkit").replaceAll("&", "§");
		noperm_arena = getConfig().getString("strings.noperm_arena").replaceAll("&", "§");
		kit_delay_message = getConfig().getString("strings.kit_delay_message").replaceAll("&", "§");
		your_place = getConfig().getString("strings.your_place").replaceAll("&", "§");
		
		sign_top = getConfig().getString("config.sign_top_line").replaceAll("&", "§");
		sign_second_join = getConfig().getString("config.sign_second_line_join").replaceAll("&", "§");
		sign_second_ingame = getConfig().getString("config.sign_second_line_ingame").replaceAll("&", "§");
		sign_second_restarting = getConfig().getString("config.sign_second_line_restarting").replaceAll("&", "§");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("dragonescape") || cmd.getName().equalsIgnoreCase("escapethemob")) {
			if (args.length > 0) {
				String action = args[0];
				if (action.equalsIgnoreCase("createarena")) {
					// create arena
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.setup")) {
							String arenaname = args[1];
							getConfig().set(arenaname + ".name", arenaname);
							this.saveConfig();
							sender.sendMessage(saved_arena);
						} else {
							sender.sendMessage(noperm);
						}
					} else {
						sender.sendMessage("" + ChatColor.RED + "No arena submitted. Usage: /etm createarena [name]");
					}
				} else if (action.equalsIgnoreCase("removearena")) {
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.setup")) {
							String arenaname = args[1];
							if (getConfig().isSet(arenaname)) {
								getConfig().set(arenaname, null);
								this.saveConfig();
								File f = new File(this.getDataFolder() + "/" + arenaname);
								if (f.exists()) {
									f.delete();
								}
								sender.sendMessage(removed_arena);
							}
						} else {
							sender.sendMessage(noperm);
						}
					} else {
						sender.sendMessage("" + ChatColor.RED + "No arena submitted. Usage: /etm createarena [name]");
					}
				} else if (action.equalsIgnoreCase("savearena")) {
					if (args.length > 1) {
						if (!(sender instanceof Player)) {
							return true;
						}

						Player p = (Player) sender;

						if (isValidArena(args[1])) {
							File f = new File(this.getDataFolder() + "/" + args[1]);
							f.delete();

							sender.sendMessage("" + ChatColor.GREEN + "Arena is now saving, " + ChatColor.GOLD + "this might take a while" + ChatColor.GREEN + ".");
							saveArenaToFile(p.getName(), args[1]);
						} else {
							sender.sendMessage("" + ChatColor.RED + "The arena appears to be invalid (missing components)!");
						}
					} else {
						sender.sendMessage("" + ChatColor.RED + "Usage: " + ChatColor.DARK_GREEN + "/etm savearena [name]");
					}
				} else if (action.equalsIgnoreCase("setbounds")) {
					if (sender.hasPermission("mobescape.setup")) {
						if (args.length > 2) {
							String arena = args[1];
							String count = args[2];
							if (!count.equalsIgnoreCase("low") && !count.equalsIgnoreCase("high")) {
								sender.sendMessage("" + ChatColor.RED + "Second parameter invalid. Usage: /etm setbounds [arena] [low/high]");
								return true;
							}
							if (!getConfig().isSet(arena)) {
								sender.sendMessage("" + ChatColor.RED + "Could not find this arena.");
								return true;
							}

							if (!(sender instanceof Player)) {
								return true;
							}

							Player p = (Player) sender;

							getConfig().set(arena + ".boundary" + count + ".world", p.getWorld().getName());
							getConfig().set(arena + ".boundary" + count + ".loc.x", p.getLocation().getBlockX());
							if (count.equalsIgnoreCase("low")) {
								getConfig().set(arena + ".boundary" + count + ".loc.y", p.getLocation().getBlockY() - 1);
							} else {
								getConfig().set(arena + ".boundary" + count + ".loc.y", p.getLocation().getBlockY());
							}
							getConfig().set(arena + ".boundary" + count + ".loc.z", p.getLocation().getBlockZ());
							this.saveConfig();

							sender.sendMessage("" + ChatColor.YELLOW + "Successfully saved " + count + " boundary!");
						} else {
							sender.sendMessage("" + ChatColor.RED + "Usage: /etm setbounds [arena] [count].");
						}
					} else {
						sender.sendMessage(noperm);
					}
				} else if (action.equalsIgnoreCase("boundstool")) {
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.setup")) {
							Player p = (Player) sender;
							String arenaname = args[1];
							if (!getConfig().isSet(arenaname)) {
								sender.sendMessage("" + ChatColor.RED + "Could not find this arena.");
								return true;
							}
							Inventory inv = p.getInventory();
							ItemStack is = new ItemStack(369, 1);
							ItemMeta im = (ItemMeta) is.getItemMeta();
							im.setDisplayName("" + ChatColor.GREEN + "Boundary tool for arena " + ChatColor.YELLOW + "" + arenaname);
							is.setItemMeta(im);
							inv.addItem(is);
							sender.sendMessage("" + ChatColor.YELLOW + "Here's the boundary tool. Left click the lower left point and right click the higher right point.");
						} else {
							sender.sendMessage(noperm);
						}
					} else {
						sender.sendMessage("" + ChatColor.RED + "Usage: /etm boundstool [arena].");
					}
				} else if (action.equalsIgnoreCase("setflypoint")) {
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.setup")) {
							Player p = (Player) sender;
							String arenaname = args[1];

							String count = Integer.toString(this.getCurrentFlyPoint(arenaname));

							// overwrite functionality
							if (args.length > 2) {
								if (isNumeric(args[2])) {
									if (Integer.parseInt(args[2]) <= getCurrentFlyPoint(arenaname)) {
										count = args[2];
									}
								}
							}

							getConfig().set(arenaname + ".flypoint." + count + ".world", p.getWorld().getName());
							getConfig().set(arenaname + ".flypoint." + count + ".x", p.getLocation().getBlockX());
							getConfig().set(arenaname + ".flypoint." + count + ".y", p.getLocation().getBlockY());
							getConfig().set(arenaname + ".flypoint." + count + ".z", p.getLocation().getBlockZ());
							this.saveConfig();
							sender.sendMessage(ChatColor.GREEN + "Saved Fly point " + count);
						} else {
							sender.sendMessage(noperm);
						}
					}
				} else if (action.equalsIgnoreCase("removeflypoint")) {
					if (args.length > 2) {
						if (sender.hasPermission("mobescape.setup")) {
							Player p = (Player) sender;
							String arenaname = args[1];
							if (!isNumeric(args[2])) {
								sender.sendMessage(ChatColor.RED + "Usage: /etm removeflypoint [arena] [count]");
								return true;
							}
							if (!getConfig().isSet(arenaname + ".flypoint")) {
								sender.sendMessage(ChatColor.RED + "Could not find any flypoints for this arena.");
								return true;
							}

							int rem_count = Integer.parseInt(args[2]);

							if (rem_count < this.getCurrentFlyPoint(arenaname)) {
								getConfig().set(arenaname + ".flypoint." + Integer.toString(rem_count), null);
								this.saveConfig();
								sender.sendMessage(ChatColor.RED + "Removed Fly point " + Integer.toString(rem_count));
							}
						} else {
							sender.sendMessage(noperm);
						}
					}
				} else if (action.equalsIgnoreCase("setlobby")) {
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.setup")) {
							Player p = (Player) sender;
							String arenaname = args[1];
							getConfig().set(arenaname + ".lobby.world", p.getWorld().getName());
							getConfig().set(arenaname + ".lobby.loc.x", p.getLocation().getBlockX());
							getConfig().set(arenaname + ".lobby.loc.y", p.getLocation().getBlockY());
							getConfig().set(arenaname + ".lobby.loc.z", p.getLocation().getBlockZ());
							this.saveConfig();
							sender.sendMessage(saved_lobby);
						} else {
							sender.sendMessage(noperm);
						}
					}
				} else if (action.equalsIgnoreCase("setfinish")) {
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.setup")) {
							Player p = (Player) sender;
							String arenaname = args[1];
							getConfig().set(arenaname + ".finish.world", p.getWorld().getName());
							getConfig().set(arenaname + ".finish.loc.x", p.getLocation().getBlockX());
							getConfig().set(arenaname + ".finish.loc.y", p.getLocation().getBlockY());
							getConfig().set(arenaname + ".finish.loc.z", p.getLocation().getBlockZ());
							this.saveConfig();
							sender.sendMessage(saved_finish);
						} else {
							sender.sendMessage(noperm);
						}
					}
				} else if (action.equalsIgnoreCase("setspawn")) {
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.setup")) {
							Player p = (Player) sender;
							String arenaname = args[1];
							
							String count = Integer.toString(this.getCurrentSpawnIndex(arenaname));

							getConfig().set(arenaname + ".spawn." + count + ".world", p.getWorld().getName());
							getConfig().set(arenaname + ".spawn." + count + ".loc.x", p.getLocation().getX());
							getConfig().set(arenaname + ".spawn." + count + ".loc.y", p.getLocation().getY());
							getConfig().set(arenaname + ".spawn." + count + ".loc.z", p.getLocation().getZ());
							getConfig().set(arenaname + ".spawn." + count + ".loc.yaw", p.getLocation().getYaw());
							getConfig().set(arenaname + ".spawn." + count + ".loc.pitch", p.getLocation().getPitch());
							this.saveConfig();
							sender.sendMessage(saved_spawn + " Count: " + count);
						} else {
							sender.sendMessage(noperm);
						}
					}
				} else if (action.equalsIgnoreCase("setdragonspawn") || action.equalsIgnoreCase("setmobspawn")) {
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.setup")) {
							Player p = (Player) sender;
							String arenaname = args[1];
							getConfig().set(arenaname + ".dragonspawn.world", p.getWorld().getName());
							getConfig().set(arenaname + ".dragonspawn.loc.x", p.getLocation().getBlockX());
							getConfig().set(arenaname + ".dragonspawn.loc.y", p.getLocation().getBlockY());
							getConfig().set(arenaname + ".dragonspawn.loc.z", p.getLocation().getBlockZ());
							getConfig().set(arenaname + ".dragonspawn.loc.yaw", p.getLocation().getYaw());
							getConfig().set(arenaname + ".dragonspawn.loc.pitch", p.getLocation().getPitch());
							this.saveConfig();
							sender.sendMessage(saved_spawn);
						} else {
							sender.sendMessage(noperm);
						}
					}
				} else if (action.equalsIgnoreCase("setmainlobby")) {
					if (sender.hasPermission("mobescape.setup")) {
						Player p = (Player) sender;
						getConfig().set("mainlobby.world", p.getWorld().getName());
						getConfig().set("mainlobby.loc.x", p.getLocation().getBlockX());
						getConfig().set("mainlobby.loc.y", p.getLocation().getBlockY());
						getConfig().set("mainlobby.loc.z", p.getLocation().getBlockZ());
						this.saveConfig();
						sender.sendMessage(saved_mainlobby);
					} else {
						sender.sendMessage(noperm);
					}
				} else if (action.equalsIgnoreCase("leave")) {
					Player p = (Player) sender;
					if (arenap.containsKey(p)) {
						leaveArena(p, true, false);
					} else {
						p.sendMessage(not_in_arena);
					}
				} else if (action.equalsIgnoreCase("setreward")) {
					if(args.length > 3){
						String arena = args[1];
						String type = args[2];
						String amount = args[3];
						if(!type.equalsIgnoreCase("command") && !isNumeric(amount)){
							sender.sendMessage(ChatColor.RED + "Amount has to be a number.");
							return true;
						}
						if(type.equalsIgnoreCase("money")){
							setArenaDefaultRewards(arena);
							setArenaReward(arena, "money_reward_per_game", Integer.parseInt(amount));
						}else if(type.equalsIgnoreCase("itemid")){
							setArenaDefaultRewards(arena);
							setArenaReward(arena, "item_reward_id", Integer.parseInt(amount));
						}else if(type.equalsIgnoreCase("itemamount")){
							setArenaDefaultRewards(arena);
							setArenaReward(arena, "item_reward_amount", Integer.parseInt(amount));
						}else if(type.equalsIgnoreCase("command")){
							setArenaDefaultRewards(arena);
							setArenaCommandReward(arena, amount);
						}else{
							sender.sendMessage(ChatColor.RED + "Usage: /etm setreward [arena] [type] [amount]. [type] can be 'money', 'itemid', 'itemamount' or 'command'.");
							return true;
						}
						sender.sendMessage(ChatColor.GREEN + "Successfully saved arena reward for type " + type + " .");
					}else{
						sender.sendMessage(ChatColor.RED + "Usage: /etm setreward [arena] [type] [amount]. [type] can be 'money', 'itemid', 'itemamount' or 'command'.");
					}
				} else if (action.equalsIgnoreCase("endall")) {
					if (sender.hasPermission("mobescape.end")) {
						for (String arena : tasks.keySet()) {
							try {
								tasks.get(arena).cancel();
							} catch (Exception e) {

							}
						}
						ingame.clear();
						Bukkit.getScheduler().cancelTasks(this);
					} else {
						sender.sendMessage(noperm);
					}
				} else if (action.equalsIgnoreCase("setmaxplayers")) {
					if (sender.hasPermission("mobescape.setup")) {
						if (args.length > 2) {
							String arena = args[1];
							String playercount = args[2];
							if (!isNumeric(playercount)) {
								playercount = Integer.toString(default_max_players);
								sender.sendMessage("" + ChatColor.RED + "Playercount is invalid. Setting to default value.");
							}
							if (!getConfig().isSet(arena)) {
								sender.sendMessage("" + ChatColor.RED + "Could not find this arena.");
								return true;
							}
							this.setArenaMaxPlayers(arena, Integer.parseInt(playercount));
							sender.sendMessage("" + ChatColor.YELLOW + "Successfully set!");
						} else {
							sender.sendMessage("" + ChatColor.RED + "Usage: /etm setmaxplayers [arena] [count].");
						}
					}
				} else if (action.equalsIgnoreCase("setminplayers")) {
					if (sender.hasPermission("mobescape.setup")) {
						if (args.length > 2) {
							String arena = args[1];
							String playercount = args[2];
							if (!isNumeric(playercount)) {
								playercount = Integer.toString(default_min_players);
								sender.sendMessage("" + ChatColor.RED + "Playercount is invalid. Setting to default value.");
							}
							if (!getConfig().isSet(arena)) {
								sender.sendMessage("" + ChatColor.RED + "Could not find this arena.");
								return true;
							}
							this.setArenaMinPlayers(arena, Integer.parseInt(playercount));
							sender.sendMessage("" + ChatColor.YELLOW + "Successfully set!");
						} else {
							sender.sendMessage("" + ChatColor.RED + "Usage: /etm setminplayers [arena] [count].");
						}
					} else {
						sender.sendMessage(noperm);
					}
				} else if (action.equalsIgnoreCase("setdifficulty")) {
					if (sender.hasPermission("mobescape.setup")) {
						if (args.length > 2) {
							String arena = args[1];
							String difficulty = args[2];
							if (!isNumeric(difficulty)) {
								difficulty = "1";
								sender.sendMessage("" + ChatColor.RED + "Difficulty is invalid. Possible difficulties: 0, 1, 2.");
							}
							if (!getConfig().isSet(arena)) {
								sender.sendMessage("" + ChatColor.RED + "Could not find this arena.");
								return true;
							}
							sender.sendMessage("" + ChatColor.YELLOW + "Successfully set!");
						} else {
							sender.sendMessage("" + ChatColor.RED + "Usage: /etm setdifficulty [arena] [difficulty]. Difficulty can be 0, 1 or 2.");
						}
					}
				} else if (action.equalsIgnoreCase("setmobtype")) {
					if (sender.hasPermission("mobescape.setmobtype")) {
						if (args.length > 1) {
							String mobtype = args[1];
							if(mobtype.equalsIgnoreCase("wither") || mobtype.equalsIgnoreCase("dragon")){
								type = mobtype;
								getConfig().set("config.mob_type", mobtype);
								this.saveConfig();
								sender.sendMessage("" + ChatColor.YELLOW + "Successfully set!");
							}else{
								sender.sendMessage("" + ChatColor.RED + "Unknown mob. Possible ones: dragon, wither");
							}
						} else {
							sender.sendMessage("" + ChatColor.RED + "Usage: /etm setmobtype [mobtype].");
						}
					}
				} else if (action.equalsIgnoreCase("setarenavip")) {
					if (sender.hasPermission("mobescape.setup")) {
						if (args.length > 2) {
							if(args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")){
								this.setArenaNeedsPerm(args[1], Boolean.parseBoolean(args[2]));
								sender.sendMessage(ChatColor.GREEN + "Successfully set.");
							}else{
								sender.sendMessage(ChatColor.RED + "Usage: /etm setarenavip [arena] [true/false]");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "Usage: /etm setarenavip [arena] [true/false]");
						}
					}
				} else if (action.equalsIgnoreCase("join")) {
					if (args.length > 1) {
						if (isValidArena(args[1])) {
							Sign s = null;
							try {
								s = this.getSignFromArena(args[1]);
							} catch (Exception e) {
								getLogger().warning("No sign found for arena " + args[1] + ". May lead to errors.");
							}
							if (s != null) {
								if (s.getLine(1).equalsIgnoreCase(sign_second_join.toLowerCase())) {
									joinLobby((Player) sender, args[1]);
								} else {
									sender.sendMessage(arena_ingame);
								}
							} else {
								sender.sendMessage(arena_invalid_sign);
							}
						} else {
							sender.sendMessage(arena_invalid);
						}
					}
				} else if (action.equalsIgnoreCase("kit")) {
					if (args.length > 1) {
						String kit = args[1];
						if(!(sender instanceof Player)){
							return true;
						}
						Player p = (Player)sender;
						if(!arenap.containsKey(p)){
							sender.sendMessage(not_in_arena);
							return true;
						}
						
						if(kit.equalsIgnoreCase("jumper")){
							sender.sendMessage(getKitDescription("jumper"));
						}else if(kit.equalsIgnoreCase("warper")){
							sender.sendMessage(getKitDescription("warper"));
						}else if(kit.equalsIgnoreCase("tnt")){
							sender.sendMessage(getKitDescription("tnt"));
						}else{
							sender.sendMessage(ChatColor.RED + "Unknown Kit.");
							return true;
						}
						if(this.kitPlayerHasPermission(args[1], p)){
							if(this.kitRequiresMoney(args[1])){
								if(this.kitTakeMoney(p, args[1])){
									pkit.put(p, args[1]);
								}
							}else{
								pkit.put(p, args[1]);
							}
						}else{
							sender.sendMessage(nopermkit);
						}
					}
				} else if (action.equalsIgnoreCase("kitgui")) {
					if(!(sender instanceof Player)){
						return true;
					}
					Player p = (Player)sender;
					if(!arenap.containsKey(p)){
						sender.sendMessage(not_in_arena);
						return true;
					}
					openGUI(this, p.getName());
				} else if (action.equalsIgnoreCase("stop") || action.equalsIgnoreCase("end")) {
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.stop")) {
							final String arena = args[1];
							if (!ingame.containsKey(arena)) {
								sender.sendMessage(ChatColor.RED + "The arena appears to be not ingame.");
								return true;
							}
							int count = 0;
							for (Player p : arenap.keySet()) {
								if (arenap.get(p).equalsIgnoreCase(arena)) {
									count++;
								}
							}
							if (count < 1) {
								sender.sendMessage("" + ChatColor.RED + "Noone is in this arena.");
								return true;
							}
							if (ingame.get(arena)) {
								Bukkit.getScheduler().runTaskLater(this, new Runnable() {
									public void run() {
										stop(h.get(arena), arena);
									}
								}, 10);
							}
						} else {
							sender.sendMessage(noperm);
						}
					}
				} else if (action.equalsIgnoreCase("start")) {
					if (args.length > 1) {
						if (sender.hasPermission("mobescape.start")) {
							final String arena = args[1];
							if (!ingame.containsKey(arena)) {
								ingame.put(arena, false);
							}
							int count = 0;
							for (Player p : arenap.keySet()) {
								if (arenap.get(p).equalsIgnoreCase(arena)) {
									count++;
								}
							}
							if (count < 1) {
								sender.sendMessage("" + ChatColor.RED + "Noone is in this arena.");
								return true;
							}
							if(m.lobby_countdown_id.containsKey(arena)){
								sender.sendMessage(ChatColor.RED + "This arena is already starting.");
								return true;
							}
							if (!ingame.get(arena)) {
								ingame.put(arena, true);
								for (Player p_ : arenap.keySet()) {
									if (arenap.get(p_).equalsIgnoreCase(arena)) {
										final Player p__ = p_;
										Bukkit.getScheduler().runTaskLater(this, new Runnable() {
											public void run() {
												p__.teleport(getSpawnForPlayer(p__, arena));
											}
										}, 5);
									}
								}
								Bukkit.getScheduler().runTaskLater(this, new Runnable() {
									public void run() {
										start(arena);
									}
								}, 10);
							}
						} else {
							sender.sendMessage(noperm);
						}
					}
				} else if (action.equalsIgnoreCase("reload")) {
					if (sender.hasPermission("mobescape.reload")) {
						this.reloadConfig();
						getConfigVars();
						sender.sendMessage(reloaded);
					} else {
						sender.sendMessage(noperm);
					}
				} else if (action.equalsIgnoreCase("list")) {
					if (sender.hasPermission("mobescape.list")) {
						sender.sendMessage("" + ChatColor.GOLD + "-= Arenas =-");
						for (String arena : getConfig().getKeys(false)) {
							if (!arena.equalsIgnoreCase("mainlobby") && !arena.equalsIgnoreCase("strings") && !arena.equalsIgnoreCase("config")) {
								sender.sendMessage("" + ChatColor.DARK_GREEN + "" + arena);
							}
						}
					} else {
						sender.sendMessage(noperm);
					}
				} else {
					sender.sendMessage("" + ChatColor.GOLD + "-= MobEscape " + ChatColor.DARK_GREEN + "help: " + ChatColor.GOLD + "=-");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "To " + ChatColor.GOLD + "setup the main lobby " + ChatColor.DARK_GREEN + ", type in " + ChatColor.RED + "/etm setmainlobby");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "To " + ChatColor.GOLD + "setup " + ChatColor.DARK_GREEN + "a new arena, type in the following commands:");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm createarena [name]");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setlobby [name] " + ChatColor.GOLD + " - for the waiting lobby");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setspawn [name] " + ChatColor.GOLD + " - players spawn here");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setfinish [name] " + ChatColor.GOLD + " - the finish line");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setflypoint [name] " + ChatColor.GOLD + " - set at least two flypoints for the dragon!");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setbounds [name] " + ChatColor.GOLD + " - don't forget to set both high and low boundaries.");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm savearena [name] " + ChatColor.GOLD + " - save the arena");
					sender.sendMessage("");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "You can join with " + ChatColor.RED + "/etm join [name] " + ChatColor.DARK_GREEN + "and leave with " + ChatColor.RED + "/etm leave" + ChatColor.DARK_GREEN + ".");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "You can force an arena to start with " + ChatColor.RED + "/etm start [name]" + ChatColor.DARK_GREEN + ".");
					sender.sendMessage("" + ChatColor.DARK_GREEN + "You can select your kit with " + ChatColor.RED + "/etm kit " + ChatColor.DARK_GREEN + " or " + ChatColor.RED + "/etm kitgui");
				}
			} else {
				sender.sendMessage("" + ChatColor.GOLD + "-= MobEscape " + ChatColor.DARK_GREEN + "help: " + ChatColor.GOLD + "=-");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "To " + ChatColor.GOLD + "setup the main lobby " + ChatColor.DARK_GREEN + ", type in " + ChatColor.RED + "/etm setmainlobby");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "To " + ChatColor.GOLD + "setup " + ChatColor.DARK_GREEN + "a new arena, type in the following commands:");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm createarena [name]");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setlobby [name] " + ChatColor.GOLD + " - for the waiting lobby");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setspawn [name] " + ChatColor.GOLD + " - players spawn here");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setfinish [name] " + ChatColor.GOLD + " - the finish line");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setflypoint [name] " + ChatColor.GOLD + " - set at least two flypoints for the dragon!");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm setbounds [name] " + ChatColor.GOLD + " - don't forget to set both high and low boundaries.");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "/etm savearena [name] " + ChatColor.GOLD + " - save the arena");
				sender.sendMessage("");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "You can join with " + ChatColor.RED + "/etm join [name] " + ChatColor.DARK_GREEN + "and leave with " + ChatColor.RED + "/etm leave" + ChatColor.DARK_GREEN + ".");
				sender.sendMessage("" + ChatColor.DARK_GREEN + "You can force an arena to start with " + ChatColor.RED + "/etm start [name]" + ChatColor.DARK_GREEN + ".");
			}
			return true;
		}
		return false;
	}

	private boolean registerEntities() {
		if (mode1_6) {
			return V1_6Dragon.registerEntities();
		}
		return V1_7Dragon.registerEntities();
	}

	/*
	 * public Test spawnEnderdragon(String arena, Location t) { /*Object w =
	 * ((CraftWorld) t.getWorld()).getHandle();
	 * if(this.getDragonWayPoints(arena) == null){ getLogger().severe(
	 * "You forgot to set any FlyPoints! You need to have min. 2 and one of them has to be at finish."
	 * ); return null; } Test t_ = new Test(this, arena, t,
	 * (net.minecraft.server.v1_7_R1.World) ((CraftWorld)
	 * t.getWorld()).getHandle(), this.getDragonWayPoints(arena));
	 * ((net.minecraft.server.v1_7_R1.World) w).addEntity(t_,
	 * CreatureSpawnEvent.SpawnReason.CUSTOM); t_.setCustomName(dragon_name);
	 */
	// TODO: send entity invisibility packet
	// TODO: might get possible though with HoloAPI when MC 1.8 is released
	/*
	 * Slimey b = new Slimey(this, t, (net.minecraft.server.v1_7_R1.World)
	 * ((CraftWorld) t.getWorld()).getHandle());
	 * ((net.minecraft.server.v1_7_R1.World) w).addEntity(b,
	 * CreatureSpawnEvent.SpawnReason.CUSTOM); b.setCustomName(dragon_name);
	 * b.setInvisible(true); V1_7 v = new V1_7(); return v.spawnEnderdragon(m,
	 * arena, t); }
	 */

	/*
	 * public Test1_6 spawnEnderdragon1_6(String arena, Location t) { V1_6 v =
	 * new V1_6(); return v.spawnEnderdragon(m, arena, t); }
	 */

	/*
	 * public void setDragonSpeed(EnderDragon s, double speed) {
	 * AttributeInstance attributes = ((EntityInsentient) ((CraftLivingEntity)
	 * s).getHandle()).getAttributeInstance(GenericAttributes.d);
	 * attributes.setValue(speed); }
	 */

	public ArrayList<String> left_players = new ArrayList<String>();

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (arenap.containsKey(event.getPlayer())) {
			String arena = arenap.get(event.getPlayer());
			getLogger().info(arena);
			int count = 0;
			for (Player p_ : arenap.keySet()) {
				if (arenap.get(p_).equalsIgnoreCase(arena)) {
					count++;
				}
			}

			try {
				Sign s = this.getSignFromArena(arena);
				if (s != null) {
					s.setLine(1, sign_second_join);
					s.setLine(3, Integer.toString(count - 1) + "/" + Integer.toString(getArenaMaxPlayers(arena)));
					s.update();
				}
			} catch (Exception e) {
				getLogger().warning("You forgot to set a sign for arena " + arena + "! This might lead to errors.");
			}

			leaveArena(event.getPlayer(), true, true);
			left_players.add(event.getPlayer().getName());
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if (left_players.contains(event.getPlayer().getName())) {
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				public void run() {
					p.teleport(getMainLobby());
					p.setFlying(false);
				}
			}, 5);
			left_players.remove(event.getPlayer().getName());
		}
		
		if(getConfig().getBoolean("config.game_on_join")){
			int c = 0;
			final List<String> arenas = new ArrayList<String>();
			for (String arena : getConfig().getKeys(false)) {
				if (!arena.equalsIgnoreCase("mainlobby") && !arena.equalsIgnoreCase("strings") && !arena.equalsIgnoreCase("config")) {
					c++;
					arenas.add(arena);
				}
			}
			if(c < 1){
				getLogger().severe("Couldn't find any arena even though game_on_join was turned on. Please setup an arena to fix this!");
				return;
			}
			
			Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				public void run(){
					joinLobby(p, arenas.get(0));
				}
			}, 30L);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (arenap_.containsKey(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (arenap_.containsKey(p.getName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// if (arenap_.containsKey(event.getPlayer().getName())) {
		if (arenap.containsKey(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// if (arenap_.containsKey(event.getPlayer().getName())) {
		if (arenap.containsKey(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(arenap.containsKey(event.getPlayer()) && !arenap_.containsKey(event.getPlayer().getName())){
			final String arena_ = arenap.get(event.getPlayer());
			//getLogger().info(astarted.get(arena_).toString());
			if(ingame.get(arena_)){
				if (getDragonSpawn(arena_) != null) {
					final Player p = event.getPlayer();
					final Location temp = getSpawn(arena_, pspawn.get(p));
					if (p.getLocation().getBlockZ() > temp.getBlockZ() + 1 || p.getLocation().getBlockZ() < temp.getBlockZ() - 1 || p.getLocation().getBlockX() > temp.getBlockX() + 1 || p.getLocation().getBlockX() < temp.getBlockX() - 1) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							public void run() {
								p.teleport(temp);
							}
						}, 5);
					}
				}
			}
		}
		if (arenap_.containsKey(event.getPlayer().getName())) {
			if (lost.containsKey(event.getPlayer())) {
				Location l = getSpawn(lost.get(event.getPlayer()), pspawn.get(event.getPlayer()));
				final Location spectatorlobby = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 30, l.getBlockZ());
				if (event.getPlayer().getLocation().getBlockY() < spectatorlobby.getBlockY() || event.getPlayer().getLocation().getBlockY() > spectatorlobby.getBlockY()) {
					final Player p = event.getPlayer();
					final float b = p.getLocation().getYaw();
					final float c = p.getLocation().getPitch();
					final String arena = arenap.get(event.getPlayer());
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						@Override
						public void run() {
							try {
								p.setAllowFlight(true);
								p.setFlying(true);
								p.teleport(new Location(p.getWorld(), p.getLocation().getBlockX(), spectatorlobby.getBlockY(), p.getLocation().getBlockZ(), b, c));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, 5);
					p.sendMessage(you_fell);
					return;
				}
			}

			
			
			final String arena_ = arenap_.get(event.getPlayer().getName());

			if(event.getPlayer().getLocation().distance(getFinish(arena_)) < 2){
				if (ingame.get(arena_)) {
					stop(h.get(arena_), arena_);
				}
				return;
			}
			

			if (event.getPlayer().getLocation().getBlockY() < getLowBoundary(arenap_.get(event.getPlayer().getName())).getBlockY() - 3) {
				this.simulatePlayerFall(event.getPlayer());
				return;
			}
		}
	}
	
	@EventHandler
	public void onSignUse(PlayerInteractEvent event) {
		if (event.hasBlock()) {
			if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
				final Sign s = (Sign) event.getClickedBlock().getState();
				if (s.getLine(0).toLowerCase().contains(sign_top.toLowerCase())) {
					if (s.getLine(1).equalsIgnoreCase(sign_second_join)) {
						if (isValidArena(s.getLine(2))) {
							joinLobby(event.getPlayer(), s.getLine(2));
						} else {
							event.getPlayer().sendMessage(arena_invalid);
						}
					}
				}
			}

			if (event.hasItem()) {
				if (event.getItem().getTypeId() == 369) {
					if (event.getItem().hasItemMeta()) {
						ItemMeta im = event.getItem().getItemMeta();
						String itemname = im.getDisplayName();
						String arenaname = itemname.split("" + ChatColor.YELLOW + "")[1];
						if (getConfig().isSet(arenaname)) {
							if (event.getPlayer().hasPermission("mobescape.setup")) {
								try {
									Block b = event.getClickedBlock();
									Location l = b.getLocation();
									if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
										String count = "low";
										getConfig().set(arenaname + ".boundary" + count + ".world", l.getWorld().getName());
										getConfig().set(arenaname + ".boundary" + count + ".loc.x", l.getBlockX());
										getConfig().set(arenaname + ".boundary" + count + ".loc.y", l.getBlockY() - 1);
										getConfig().set(arenaname + ".boundary" + count + ".loc.z", l.getBlockZ());
										this.saveConfig();
										event.setCancelled(true);
										event.getPlayer().sendMessage("" + ChatColor.YELLOW + "Successfully saved " + count + " boundary!");
									} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
										String count = "high";
										getConfig().set(arenaname + ".boundary" + count + ".world", l.getWorld().getName());
										getConfig().set(arenaname + ".boundary" + count + ".loc.x", l.getBlockX());
										getConfig().set(arenaname + ".boundary" + count + ".loc.y", l.getBlockY());
										getConfig().set(arenaname + ".boundary" + count + ".loc.z", l.getBlockZ());
										this.saveConfig();
										event.setCancelled(true);
										event.getPlayer().sendMessage("" + ChatColor.YELLOW + "Successfully saved " + count + " boundary!");
									}
								} catch (NullPointerException e) {
									event.getPlayer().sendMessage("" + ChatColor.RED + "You must hit a block.");
								}
							} else {
								event.getPlayer().sendMessage(noperm);
							}
						} else {
							event.getPlayer().sendMessage("" + ChatColor.RED + "Could not find this arena.");
						}
					}
				}
			}
		}
		
		if (event.hasItem()) {
			final Player p = event.getPlayer();
			if(!arenap.containsKey(p)){
				return;
			}
			if(!pkit_use.containsKey(p)){
				pkit_use.put(p, true);
			}
			if(!pkit_use.get(p)){
				p.sendMessage(kit_delay_message.replaceAll("<delay>", Integer.toString(kit_delay_in_seconds)));
				return;
			}
			pkit_use.put(p, false);
			Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				public void run(){
					pkit_use.put(p, true);
				}
			}, kit_delay_in_seconds * 20);
			if(event.getItem().getTypeId() == 258){
				p.getInventory().removeItem(new ItemStack(Material.IRON_AXE, 1));
				p.updateInventory();
				Vector direction = p.getLocation().getDirection().multiply(getConfig().getDouble("config.jumper_boost_factor"));
				direction.setY(direction.getY() + 1.5);
				p.setVelocity(direction);
				//p.setVelocity(p.getVelocity().multiply(2D));
			}else if(event.getItem().getTypeId() == 368){
				p.getInventory().removeItem(new ItemStack(Material.ENDER_PEARL, 1));
				p.updateInventory();
				for(final Entity t : p.getNearbyEntities(40, 40, 40)){
					if(t instanceof Player){
						Bukkit.getScheduler().runTaskLater(this, new Runnable(){
							public void run(){
								p.teleport(t);
							}
						}, 5L);
					}
				}
				event.setCancelled(true);
			}else if(event.getItem().getTypeId() == 46){
				p.getInventory().removeItem(new ItemStack(Material.TNT, 1));
				p.updateInventory();
				p.getLocation().getWorld().dropItemNaturally(p.getLocation().add(1, 3, 1), new ItemStack(Material.TNT));
			}
		}
	}

	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent event){
		if(arenap.containsKey(event.getPlayer())){
			if(event.getItem().getItemStack().getType() == Material.TNT){
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1));
				event.getItem().remove();
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player p = event.getPlayer();
		if (event.getLine(0).toLowerCase().equalsIgnoreCase("dragonescape") || event.getLine(0).toLowerCase().equalsIgnoreCase("mobescape")) {
			if (event.getPlayer().hasPermission("mobescape.sign") || event.getPlayer().isOp()) {
				event.setLine(0, sign_top);
				if (!event.getLine(2).equalsIgnoreCase("")) {
					String arena = event.getLine(2);
					if (isValidArena(arena)) {
						getConfig().set(arena + ".sign.world", p.getWorld().getName());
						getConfig().set(arena + ".sign.loc.x", event.getBlock().getLocation().getBlockX());
						getConfig().set(arena + ".sign.loc.y", event.getBlock().getLocation().getBlockY());
						getConfig().set(arena + ".sign.loc.z", event.getBlock().getLocation().getBlockZ());
						this.saveConfig();
						p.sendMessage("" + ChatColor.DARK_GREEN + "Successfully created arena sign.");
					} else {
						p.sendMessage(arena_invalid_component);
						event.getBlock().breakNaturally();
					}
					event.setLine(1, sign_second_join);
					event.setLine(2, arena);
					event.setLine(3, "0/" + Integer.toString(getArenaMaxPlayers(arena)));
				}
			} else {
				event.getPlayer().sendMessage(noperm);
			}
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if (arenap.containsKey(event.getPlayer()) && !event.getPlayer().isOp()) {
			if (!event.getMessage().startsWith("/etm") && !event.getMessage().startsWith("/de") && !event.getMessage().startsWith("/dragonescape")) {
				event.getPlayer().sendMessage("" + ChatColor.RED + "Please use " + ChatColor.GOLD + "/etm leave " + ChatColor.RED + "to leave this minigame.");
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void EntityChangeBlockEvent(org.bukkit.event.entity.EntityChangeBlockEvent event) {
		if (event.getEntityType() == EntityType.FALLING_BLOCK) {
			for (String arena : getConfig().getKeys(false)) {
				if (!arena.equalsIgnoreCase("mainlobby") && !arena.equalsIgnoreCase("strings") && !arena.equalsIgnoreCase("config")){
					if(isValidArena(arena)){
						Cuboid c = new Cuboid(getLowBoundary(arena), getHighBoundary(arena));
						if(c.containsLocWithoutY(event.getBlock().getLocation())){
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (arenap_.containsKey(p.getName())) {
				p.setHealth(20D);
				event.setCancelled(true);
			}
		}
	}

	public Sign getSignFromArena(String arena) {
		Location b_ = new Location(getServer().getWorld(getConfig().getString(arena + ".sign.world")), getConfig().getInt(arena + ".sign.loc.x"), getConfig().getInt(arena + ".sign.loc.y"), getConfig().getInt(arena + ".sign.loc.z"));
		BlockState bs = b_.getBlock().getState();
		Sign s_ = null;
		if (bs instanceof Sign) {
			s_ = (Sign) bs;
		} else {
		}
		return s_;
	}

	public Location getLobby(String arena) {
		Location ret = null;
		if (isValidArena(arena)) {
			ret = new Location(Bukkit.getWorld(getConfig().getString(arena + ".lobby.world")), getConfig().getInt(arena + ".lobby.loc.x"), getConfig().getInt(arena + ".lobby.loc.y"), getConfig().getInt(arena + ".lobby.loc.z"));
		}
		return ret;
	}

	public Location getMainLobby() {
		Location ret;
		if (getConfig().isSet("mainlobby")) {
			ret = new Location(Bukkit.getWorld(getConfig().getString("mainlobby.world")), getConfig().getInt("mainlobby.loc.x"), getConfig().getInt("mainlobby.loc.y"), getConfig().getInt("mainlobby.loc.z"));
		} else {
			ret = null;
			getLogger().warning("A Mainlobby could not be found. This will lead to errors, please fix this with /etm setmainlobby.");
		}
		return ret;
	}

	public Location getSpawn(String arena, int count) {
		Location ret = null;
		if (isValidArena(arena)) {
			String entry = ".spawn." + Integer.toString(count) + ".";
			if(!getConfig().isSet(arena + entry)){
				entry = ".spawn.";
			}
			ret = new Location(Bukkit.getWorld(getConfig().getString(arena + entry + "world")), getConfig().getInt(arena + entry + "loc.x"), getConfig().getInt(arena + entry + "loc.y"), getConfig().getInt(arena + entry + "loc.z"), getConfig().getInt(arena + entry + "loc.yaw"), getConfig().getInt(arena + entry + "loc.pitch"));
		}
		return ret;
	}
	
	public HashMap<String, Integer> spawncount = new HashMap<String, Integer>();
	public HashMap<Player, Integer> pspawn = new HashMap<Player, Integer>();

	public Location getSpawn(String arena) {
		return getSpawn(arena, 0);
	}
	
	public Location getSpawnForPlayer(Player p, String arena) {
		if(!spawncount.containsKey(arena)){
			spawncount.put(arena, 0);
			pspawn.put(p, 0);
			spawncount.put(arena, spawncount.get(arena) + 1);
			return getSpawn(arena, 0);
		}
		
		if(spawncount.get(arena) < this.getCurrentSpawnIndex(arena)){
			Location ret = getSpawn(arena, spawncount.get(arena));
			pspawn.put(p, spawncount.get(arena));
			spawncount.put(arena, spawncount.get(arena) + 1);
			return ret;
		}else{
			spawncount.put(arena, 0);
		}
		pspawn.put(p, 0);
		return getSpawn(arena, 0);
	}

	public Location getDragonSpawn(String arena) {
		Location ret = null;
		if (isValidArena(arena)) {
			if (!getConfig().isSet(arena + ".dragonspawn")) {
				return null;
			}
			ret = new Location(Bukkit.getWorld(getConfig().getString(arena + ".dragonspawn.world")), getConfig().getInt(arena + ".dragonspawn.loc.x"), getConfig().getInt(arena + ".dragonspawn.loc.y"), getConfig().getInt(arena + ".dragonspawn.loc.z"), getConfig().getInt(arena + ".dragonspawn.loc.yaw"), getConfig().getInt(arena + ".dragonspawn.loc.pitch"));
		}
		return ret;
	}

	public Location getFinish(String arena) {
		Location ret = null;
		if (isValidArena(arena)) {
			ret = new Location(Bukkit.getWorld(getConfig().getString(arena + ".finish.world")), getConfig().getInt(arena + ".finish.loc.x"), getConfig().getInt(arena + ".spawn.loc.y"), getConfig().getInt(arena + ".finish.loc.z"));
		}
		return ret;
	}

	public Location getLowBoundary(String arena) {
		Location ret = null;
		if (isValidArena(arena)) {
			ret = new Location(Bukkit.getWorld(getConfig().getString(arena + ".boundarylow.world")), getConfig().getInt(arena + ".boundarylow.loc.x"), getConfig().getInt(arena + ".boundarylow.loc.y") + 2, getConfig().getInt(arena + ".boundarylow.loc.z"));
		}
		return ret;
	}

	public Location getHighBoundary(String arena) {
		Location ret = null;
		if (isValidArena(arena)) {
			ret = new Location(Bukkit.getWorld(getConfig().getString(arena + ".boundaryhigh.world")), getConfig().getInt(arena + ".boundaryhigh.loc.x"), getConfig().getInt(arena + ".boundaryhigh.loc.y") + 2, getConfig().getInt(arena + ".boundaryhigh.loc.z"));
		}
		return ret;
	}

	public boolean isValidArena(String arena) {
		if (getConfig().isSet(arena + ".spawn") && getConfig().isSet(arena + ".lobby") && getConfig().isSet(arena + ".boundarylow") && getConfig().isSet(arena + ".boundaryhigh")) {
			return true;
		}
		return false;
	}

	public HashMap<Player, Boolean> winner = new HashMap<Player, Boolean>();

	public void leaveArena(final Player p, boolean flag, boolean hmmthisbug) {
		try {
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				public void run() {
					if (p.isOnline()) {
						try {
							p.teleport(getMainLobby());
						} catch (Exception e) {
							getServer().getLogger().severe("Please send this error to the developers on bukkitdev!");
							e.printStackTrace();
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mvtp e:" + getMainLobby().getWorld() + ":" + getMainLobby().getX() + "," + getMainLobby().getY() + "," + getMainLobby().getZ() + ":0:0");
						}
						if(spawn_winnerfirework){
							spawnFirework(p);
						}
					}
				}
			}, 8);

			if (lost.containsKey(p)) {
				lost.remove(p);
			}

			final String arena = arenap.get(p);

			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				public void run() {
					if (p.isOnline()) {
						p.setAllowFlight(false);
						p.setFlying(false);
						removeScoreboard(arena, p);
					}
				}
			}, 16);

			if (flag) {
				if (arenap.containsKey(p)) {
					arenap.remove(p);
				}
			}
			if (arenap_.containsKey(p.getName())) {
				arenap_.remove(p.getName());
			}

			if (p.isOnline()) {
				p.getInventory().setContents(pinv.get(p));
				p.updateInventory();
			}

			if (winner.containsKey(p)) {
				
				this.getArenaReward(arena, p);
				
				if(spawn_winnerfirework){
					spawnFirework(p);
				}
			}

			int count = 0;
			for (Player p_ : arenap.keySet()) {
				if (arenap.get(p_).equalsIgnoreCase(arena)) {
					count++;
				}
			}

			if (hmmthisbug && count > 0) {
				getLogger().info("Sorry, I could not fix the game. Stopping now.");
				stop(h.get(arena), arena);
			}

			if (count < 2) {
				if (flag) {
					stop(h.get(arena), arena);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void joinLobby(final Player p, final String arena) {
		
		// very first check if arena needs perms and player has perm to join.
		if(this.arenaNeedsPerm(arena)){
			if(!p.hasPermission("mobescape.joinarena." + arena)){
				p.sendMessage(noperm_arena);
				return;
			}
		}
		
		// check first if max players are reached.
		int count_ = 0;
		for (Player p_ : arenap.keySet()) {
			if (arenap.get(p_).equalsIgnoreCase(arena)) {
				count_++;
			}
		}
		if (count_ > getArenaMaxPlayers(arena) - 1) {
			p.sendMessage(arena_full);
			return;
		}

		// continue
		arenap.put(p, arena);
		pinv.put(p, p.getInventory().getContents());
		p.setGameMode(GameMode.SURVIVAL);
		p.getInventory().clear();
		p.updateInventory();
		if(give_jumper_as_default_kit){
			pkit.put(p, "jumper");
		}
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			public void run() {
				p.teleport(getLobby(arena));
				p.setFoodLevel(20);
			}
		}, 4);

		int count = 0;
		for (Player p_ : arenap.keySet()) {
			if (arenap.get(p_).equalsIgnoreCase(arena)) {
				count++;
				
			}
		}
		
		for (Player p_ : arenap.keySet()) {
			if (arenap.get(p_).equalsIgnoreCase(arena)) {
				if(join_announcement){
					p_.sendMessage(join_announcement_.replace("<player>", p.getName()).replace("<count>", Integer.toString(count) + "/" + Integer.toString(getArenaMaxPlayers(arena))));
				}
			}
		}
		
		if (count > getArenaMinPlayers(arena) - 1) {
			final int lobby_c = getConfig().getInt("config.lobby_countdown");

			if(!m.lobby_countdown_id.containsKey(arena)){
				int t = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {
					public void run() {
						if (!m.lobby_countdown_count.containsKey(arena)) {
							m.lobby_countdown_count.put(arena, lobby_c);
						}
						int count = m.lobby_countdown_count.get(arena);
						for (Player p : m.arenap.keySet()) {
							if (m.arenap.get(p).equalsIgnoreCase(arena)) {
								p.sendMessage(ChatColor.GRAY + "Teleporting to arena in " + Integer.toString(count) + " seconds.");
							}
						}
						count--;
						m.lobby_countdown_count.put(arena, count);
						if (count < 0) {
							m.lobby_countdown_count.put(arena, lobby_c);
							
							for (Player p_ : arenap.keySet()) {
								final Player p__ = p_;
								if (arenap.get(p_).equalsIgnoreCase(arena)) {
									Bukkit.getScheduler().runTaskLater(m, new Runnable() {
										public void run() {
											p__.teleport(getSpawnForPlayer(p__, arena));
										}
									}, 7);
								}
							}
							Bukkit.getScheduler().runTaskLater(m, new Runnable() {
								public void run() {
									if (!ingame.containsKey(arena)) {
										ingame.put(arena, false);
									}
									if (!ingame.get(arena)) {
										start(arena);
									}
								}
							}, 10);
							
							
							Bukkit.getServer().getScheduler().cancelTask(m.lobby_countdown_id.get(arena));
						}
					}
					}, 5, 20).getTaskId();
					m.lobby_countdown_id.put(arena, t);
			}
		}

		if (!ingame.containsKey(arena)) {
			ingame.put(arena, false);
		}
		if (ingame.get(arena)) {
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				public void run() {
					p.teleport(getSpawnForPlayer(p, arena));
				}
			}, 7);
		}

		try {
			Sign s = this.getSignFromArena(arena);
			if (s != null) {
				s.setLine(3, Integer.toString(count) + "/" + Integer.toString(getArenaMaxPlayers(arena)));
				s.update();
			}
		} catch (Exception e) {
			getLogger().warning("You forgot to set a sign for arena " + arena + "! This may lead to errors.");
		}

	}

	final Main m = this;

	static Random r = new Random();

	
	final public HashMap<String, Integer> lobby_countdown_count = new HashMap<String, Integer>();
	final public HashMap<String, Integer> lobby_countdown_id = new HashMap<String, Integer>();
	
	
	final public HashMap<String, BukkitTask> h = new HashMap<String, BukkitTask>();
	final public HashMap<String, Integer> countdown_count = new HashMap<String, Integer>();
	final public HashMap<String, Integer> countdown_id = new HashMap<String, Integer>();
	final public HashMap<String, Double> dragon_move_increment = new HashMap<String, Double>();

	public BukkitTask start(final String arena) {
		if (mode1_6) {
			if(type.equalsIgnoreCase("dragon")){
				V1_6Dragon v = new V1_6Dragon();
				return v.start(this, arena);
			}else if(type.equalsIgnoreCase("wither")){
				V1_6Wither v = new V1_6Wither();
				return v.start(this, arena);
			}else{
				V1_6Dragon v = new V1_6Dragon();
				return v.start(this, arena);
			}
		}
		if(type.equalsIgnoreCase("dragon")){
			V1_7Dragon v_ = new V1_7Dragon();
			return v_.start(this, arena);
		}else if(type.equalsIgnoreCase("wither")){
			V1_7Wither v_ = new V1_7Wither();
			return v_.start(this, arena);
		}else{
			V1_7Dragon v_ = new V1_7Dragon();
			return v_.start(this, arena);
		}
		
	}

	public void reset(final String arena) {
		Runnable r = new Runnable() {
			public void run() {
				loadArenaFromFileSYNC(arena);
			}
		};
		new Thread(r).start();
	}

	public void stop(BukkitTask t, final String arena) {
		if (mode1_6) {
			V1_6Dragon v = new V1_6Dragon();
			v.stop(this, t, arena);
		} else {
			if(type.equalsIgnoreCase("dragon")){
				V1_7Dragon v = new V1_7Dragon();
				v.stop(this, t, arena);
			}else if(type.equalsIgnoreCase("wither")){
				V1_7Wither v = new V1_7Wither();
				v.stop(this, t, arena);
			}else{
				V1_7Dragon v = new V1_7Dragon();
				v.stop(this, t, arena);
			}
		}
		
		if(m.lobby_countdown_id.containsKey(arena)){
			try{
				Bukkit.getServer().getScheduler().cancelTask(m.lobby_countdown_id.get(arena));	
			}catch(Exception e){
				
			}
			m.lobby_countdown_id.remove(arena);
		}
		
		if(spawncount.containsKey(arena)){
			spawncount.remove(arena);
		}
	}

	public void clean() {
		for (Player p : arenap.keySet()) {
			if (!p.isOnline()) {
				leaveArena(p, false, false);
			}
		}
	}

	public void determineWinners(String arena) {
		for (Player p : arenap.keySet()) {
			if (arenap.get(p).equalsIgnoreCase(arena)) {
				if (!lost.containsKey(p)) {
					// this player is a winner
					p.sendMessage(you_won);

					if (winner_announcement) {
						getServer().broadcastMessage(winner_an.replaceAll("<player>", p.getName()).replaceAll("<arena>", arena));
					}

					winner.put(p, true);
				} else {
					lost.remove(p);
				}
			}
		}
	}

	public int getArenaMaxPlayers(String arena) {
		if (!getConfig().isSet(arena + ".max_players")) {
			setArenaMaxPlayers(arena, default_max_players);
		}
		return getConfig().getInt(arena + ".max_players");
	}

	public void setArenaMaxPlayers(String arena, int players) {
		getConfig().set(arena + ".max_players", players);
		this.saveConfig();
	}

	public int getArenaMinPlayers(String arena) {
		if (!getConfig().isSet(arena + ".min_players")) {
			setArenaMinPlayers(arena, default_min_players);
		}
		return getConfig().getInt(arena + ".min_players");
	}

	public void setArenaMinPlayers(String arena, int players) {
		getConfig().set(arena + ".min_players", players);
		this.saveConfig();
	}

	public boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+");
	}

	public void saveArenaToFile(String player, String arena) {
		File f = new File(this.getDataFolder() + "/" + arena);
		Cuboid c = new Cuboid(getLowBoundary(arena), getHighBoundary(arena));
		Location start = c.getLowLoc();
		Location end = c.getHighLoc();

		int width = end.getBlockX() - start.getBlockX();
		int length = end.getBlockZ() - start.getBlockZ();
		int height = end.getBlockY() - start.getBlockY();

		getLogger().info("BOUNDS: " + Integer.toString(width) + " " + Integer.toString(height) + " " + Integer.toString(length));
		getLogger().info("BLOCKS TO SAVE: " + Integer.toString(width * height * length));

		FileOutputStream fos;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(f);
			oos = new BukkitObjectOutputStream(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i <= width; i++) {
			for (int j = 0; j <= height; j++) {
				for (int k = 0; k <= length; k++) {
					Block change = c.getWorld().getBlockAt(start.getBlockX() + i, start.getBlockY() + j, start.getBlockZ() + k);

					// if(change.getType() != Material.AIR){
					ArenaBlock bl = new ArenaBlock(change);

					try {
						oos.writeObject(bl);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// }

				}
			}
		}

		try {
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Bukkit.getPlayerExact(player).sendMessage("" + ChatColor.GREEN + "Successfully saved arena to file.");
	}

	public void saveArenaToFile(String arena) {
		File f = new File(this.getDataFolder() + "/" + arena);
		Cuboid c = new Cuboid(getLowBoundary(arena), getHighBoundary(arena));
		Location start = c.getLowLoc();
		Location end = c.getHighLoc();

		int width = end.getBlockX() - start.getBlockX();
		int length = end.getBlockZ() - start.getBlockZ();
		int height = end.getBlockY() - start.getBlockY();

		getLogger().info("BOUNDS: " + Integer.toString(width) + " " + Integer.toString(height) + " " + Integer.toString(length));
		getLogger().info("BLOCKS TO SAVE: " + Integer.toString(width * height * length));

		FileOutputStream fos;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(f);
			oos = new BukkitObjectOutputStream(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i <= width; i++) {
			for (int j = 0; j <= height; j++) {
				for (int k = 0; k <= length; k++) {
					Block change = c.getWorld().getBlockAt(start.getBlockX() + i, start.getBlockY() + j, start.getBlockZ() + k);

					// if(change.getType() != Material.AIR){
					ArenaBlock bl = new ArenaBlock(change);

					try {
						oos.writeObject(bl);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// }

				}
			}
		}

		try {
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		getLogger().info("saved");
	}

	public void loadArenaFromFileASYNC(String arena) {
		File f = new File(this.getDataFolder() + "/" + arena);
		FileInputStream fis = null;
		BukkitObjectInputStream ois = null;
		try {
			fis = new FileInputStream(f);
			ois = new BukkitObjectInputStream(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (true) {
				Object b = null;
				try {
					b = ois.readObject();
				} catch (EOFException e) {
					getLogger().info("Finished restoring map for " + arena + ".");
				}

				if (b != null) {
					ArenaBlock ablock = (ArenaBlock) b;
					World w = ablock.getBlock().getWorld();

					if (!w.getBlockAt(ablock.getBlock().getLocation()).getType().toString().equalsIgnoreCase(ablock.getMaterial().toString())) {
						ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation()).setType(ablock.getMaterial());
					}
				} else {
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void loadArenaFromFileSYNC(final String arena) {
		int failcount = 0;
		final ArrayList<ArenaBlock> failedblocks = new ArrayList<ArenaBlock>();

		File f = new File(this.getDataFolder() + "/" + arena);
		FileInputStream fis = null;
		BukkitObjectInputStream ois = null;
		try {
			fis = new FileInputStream(f);
			ois = new BukkitObjectInputStream(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (true) {
				Object b = null;
				try {
					b = ois.readObject();
				} catch (EOFException e) {
					getLogger().info("Finished restoring map for " + arena + ".");
				}

				if (b != null) {
					ArenaBlock ablock = (ArenaBlock) b;
					try {
						Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
						if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString())) {
							b_.setType(ablock.getMaterial());
							b_.setData(ablock.getData());
							// .setTypeIdAndData(ablock.getMaterial().getId(), ablock.getData(), false);
						}
					} catch (IllegalStateException e) {
						failcount += 1;
						failedblocks.add(ablock);
					}
				} else {
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		getLogger().info("Failed to update " + Integer.toString(failcount) + " blocks due to spigots async exception.");
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				// restore spigot blocks!
				//getLogger().info("Trying to restore blocks affected by spigot exception..");
				for (ArenaBlock ablock : failedblocks) {
					getServer().getWorld(ablock.world).getBlockAt(new Location(getServer().getWorld(ablock.world), ablock.x, ablock.y, ablock.z)).setType(Material.WOOL);
					getServer().getWorld(ablock.world).getBlockAt(new Location(getServer().getWorld(ablock.world), ablock.x, ablock.y, ablock.z)).getTypeId();
					getServer().getWorld(ablock.world).getBlockAt(new Location(getServer().getWorld(ablock.world), ablock.x, ablock.y, ablock.z)).setType(ablock.getMaterial());
				}
				getLogger().info("Successfully finished!");

				Sign s = getSignFromArena(arena);
				if (s != null) {
					s.setLine(1, sign_second_join);
					s.setLine(3, "0/" + Integer.toString(getArenaMaxPlayers(arena)));
					s.update();
				}
			}
		}, 40L);

		return;
	}

	public String getDirectiontest(Float yaw) {
		yaw = yaw / 90;
		yaw = (float) Math.round(yaw);

		if (yaw == -4 || yaw == 0 || yaw == 4) {
			return "SOUTH";
		}
		if (yaw == -1 || yaw == 3) {
			return "EAST";
		}
		if (yaw == -2 || yaw == 2) {
			return "NORTH";
		}
		if (yaw == -3 || yaw == 1) {
			return "WEST";
		}
		return "";
	}

	public ArrayList<Vector> getDragonWayPoints(String arena) {
		ArrayList<Vector> ret = new ArrayList<Vector>();
		if (!getConfig().isSet(arena + ".flypoint")) {
			return null;
		} else {
			Set<String> f = getConfig().getConfigurationSection(arena + ".flypoint").getKeys(false);
			for (String key : f) {
				ret.add(new Vector(getConfig().getDouble(arena + ".flypoint." + key + ".x"), getConfig().getDouble(arena + ".flypoint." + key + ".y"), getConfig().getDouble(arena + ".flypoint." + key + ".z")));
			}
			return ret;
		}
	}
	
	public ArrayList<Location> getSpawns(String arena) {
		ArrayList<Location> ret = new ArrayList<Location>();
		if (!getConfig().isSet(arena + ".spawn.")) {
			return null;
		} else {
			int count = 0;
			Set<String> f = getConfig().getConfigurationSection(arena + ".spawn").getKeys(false);
			for (String key : f) {
				if(!key.equalsIgnoreCase("world") && !key.equalsIgnoreCase("loc")){
					ret.add(getSpawn(arena, count));
					count++;
				}
			}
			return ret;
		}
	}
	
	public int getCurrentSpawnIndex(String arena) {
		if (!getConfig().isSet(arena + ".spawn.")) {
			return 0;
		} else {
			int count = 0;
			Set<String> f = getConfig().getConfigurationSection(arena + ".spawn").getKeys(false);
			for (String key : f) {
				if(!key.equalsIgnoreCase("world") && !key.equalsIgnoreCase("loc")){
					count++;
				}
			}
			return count;
		}
	}

	public int getCurrentFlyPoint(String arena) {
		if (!getConfig().isSet(arena + ".flypoint")) {
			return 0;
		} else {
			int count = 0;
			Set<String> f = getConfig().getConfigurationSection(arena + ".flypoint").getKeys(false);
			for (String key : f) {
				count++;
			}
			return count;
		}
	}

	public static final void playBlockBreakParticles(final Location loc, final Material m) {
		playBlockBreakParticles(loc, m, Bukkit.getOnlinePlayers());
	}

	public static final void playBlockBreakParticles(final Location loc, final Material m, final Player... players) {
		if (mode1_6) {
			V1_6Dragon.playBlockBreakParticles(loc, m, players);
		}
		V1_7Dragon.playBlockBreakParticles(loc, m, players);
	}

	Scoreboard board;
	Objective objective;
	public HashMap<String, Integer> currentscore = new HashMap<String, Integer>();

	public void updateScoreboard() {

		for (Player pl : arenap.keySet()) {
			Player p = pl;
			if (board == null) {
				board = Bukkit.getScoreboardManager().getNewScoreboard();
			}
			if (objective == null) {
				objective = board.registerNewObjective("test", "dummy");
			}

			objective.setDisplaySlot(DisplaySlot.SIDEBAR);

			objective.setDisplayName("[" + arenap.get(p) + "]");

			for (Player pl_ : arenap.keySet()) {
				Player p_ = pl_;
				if (!lost.containsKey(pl_)) {
					int score = -(int) p_.getLocation().distance(getFinish(arenap.get(p)));
					if (currentscore.containsKey(pl_.getName())) {
						int oldscore = currentscore.get(pl_.getName());
						if (score > oldscore) {
							currentscore.put(pl_.getName(), score);
						} else {
							score = oldscore;
						}
					} else {
						currentscore.put(pl_.getName(), score);
					}
					try{
						if(p_.getName().length() < 15){
							objective.getScore(Bukkit.getOfflinePlayer("§a" + p_.getName())).setScore(score);
						}else{
							objective.getScore(Bukkit.getOfflinePlayer("§a" + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
						}
					}catch(Exception e){
					}
				} else if (lost.containsKey(pl_)){
					if (currentscore.containsKey(pl_.getName())) {
						int score = currentscore.get(pl_.getName());
						try{
							if(p_.getName().length() < 15){
								board.resetScores(Bukkit.getOfflinePlayer("§a" + p_.getName()));
								objective.getScore(Bukkit.getOfflinePlayer("§c" + p_.getName())).setScore(score);
							}else{
								board.resetScores(Bukkit.getOfflinePlayer("§a" + p_.getName().substring(0, p_.getName().length() - 3)));
								objective.getScore(Bukkit.getOfflinePlayer("§c" + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
							}
						}catch(Exception e){
						}
					}
				}
			}

			p.setScoreboard(board);
		}
	}

	public void removeScoreboard(String arena, Player p) {
		try {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sc = manager.getNewScoreboard();
			try{
				if(p.getName().length() < 15){
					board.resetScores(Bukkit.getOfflinePlayer("§c" + p.getName()));
					board.resetScores(Bukkit.getOfflinePlayer("§a" + p.getName()));
				}else{
					board.resetScores(Bukkit.getOfflinePlayer("§c" + p.getName().substring(0, p.getName().length() - 3)));
					board.resetScores(Bukkit.getOfflinePlayer("§a" + p.getName().substring(0, p.getName().length() - 3)));
				}
				
			}catch(Exception e){}

			sc.clearSlot(DisplaySlot.SIDEBAR);
			p.setScoreboard(sc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void spawnFirework(Player p) {
		Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.AQUA).withFade(Color.ORANGE).with(Type.STAR).trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}
	
	
	public static Entity[]  getNearbyEntities(Location l, int radius){
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16))/16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
            for (int chX = 0 -chunkRadius; chX <= chunkRadius; chX ++){
                for (int chZ = 0 -chunkRadius; chZ <= chunkRadius; chZ++){
                    int x=(int) l.getX(),y=(int) l.getY(),z=(int) l.getZ();
                    for (Entity e : new Location(l.getWorld(),x+(chX*16),y,z+(chZ*16)).getChunk().getEntities()){
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) radiusEntities.add(e);
                    }
                }
            }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
	
	
	public String getKitDescription(String kit){
		return getConfig().getString("config.kits." + kit + ".description").replaceAll("&", "§");
	}
	
	public int getKitUses(String kit){
		return getConfig().getInt("config.kits." + kit + ".uses");
	}
	
	public boolean kitRequiresMoney(String kit){
		return getConfig().getBoolean("config.kits." + kit + ".requires_money");
	}
	
	public boolean kitTakeMoney(Player p, String kit) {
		if (econ.getBalance(p.getName()) >= getConfig().getInt("config.kits." + kit + ".money_amount")) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), getConfig().getInt("config.kits." + kit + ".money_amount"));
			if (!r.transactionSuccess()) {
				p.sendMessage(String.format("An error occured: %s", r.errorMessage));
			}
			return true;
		} else {
			p.sendMessage("§4You don't have enough money!");
			return false;
		}
	}
	
	public boolean kitPlayerHasPermission(String kit, Player p){
		if(!getConfig().getBoolean("config.kits." + kit + ".requires_permission")){
			return true;
		}else{
			if(p.hasPermission(getConfig().getString("config.kits." + kit + ".permission_node"))){
				return true;
			}else{
				return false;
			}
		}
	}

	public void simulatePlayerFall(Player p){
		lost.put(p, arenap.get(p));
		final Player p__ = p;
		final String arena = arenap.get(p);
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			public void run() {
				try {
					Location l = getSpawn(arena);
					p__.teleport(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 30, l.getBlockZ()));
					p__.setAllowFlight(true);
					p__.setFlying(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 5);

		int count = 0;

		for (Player p_ : arenap.keySet()) {
			if (arenap.get(p_).equalsIgnoreCase(arena)) {
				if (!lost.containsKey(p_)) {
					count++;
				}
			}
		}
		
		pplace.put(p, count + 1);
		String place = Integer.toString(count + 1) + "th";
		if(count == 0){
			place = "1st";
		}else if(count == 1){
			place = "2nd";
		}else if(count == 2){
			place = "3rd";
		}
		p.sendMessage(your_place.replaceAll("<place>", place));
		
		if (last_man_standing) {
			if (count < 2) {
				stop(h.get(arena), arena);
			}
		} else {
			if (count < 1) {
				stop(h.get(arena), arena);
			}
		}
	}
	
	
	public boolean arenaNeedsPerm(String arena){
		if(!(getConfig().isSet(arena + ".needs_perm"))){
			setArenaNeedsPerm(arena, false);
			return false;
		}
		return getConfig().getBoolean(arena + ".needs_perm");
	}
	
	public void setArenaNeedsPerm(String arena, boolean val){
		getConfig().set(arena + ".needs_perm", val);
		this.saveConfig();
	}

	
	public void openGUI(final Main m, String p) {
		IconMenu iconm = new IconMenu("Shop", 18, new IconMenu.OptionClickEventHandler() {
			@Override
			public void onOptionClick(IconMenu.OptionClickEvent event) {
				String d = event.getName();
				Player p = event.getPlayer();
				
				String kit = d.toLowerCase();
				
				if(kit.equalsIgnoreCase("jumper")){
					p.sendMessage(getKitDescription("jumper"));
				}else if(kit.equalsIgnoreCase("warper")){
					p.sendMessage(getKitDescription("warper"));
				}else if(kit.equalsIgnoreCase("tnt")){
					p.sendMessage(getKitDescription("tnt"));
				}else{
					p.sendMessage(ChatColor.RED + "Unknown Kit.");
					event.setWillClose(true);
					return;
				}
				if(kitPlayerHasPermission(kit, p)){
					if(kitRequiresMoney(kit)){
						if(kitTakeMoney(p, kit)){
							pkit.put(p, kit);
						}
					}else{
						pkit.put(p, kit);
					}
				}else{
					p.sendMessage(nopermkit);
				}
				pkit.put(p, kit);
				event.setWillClose(true);
			}
		}, m)
		.setOption(3, new ItemStack(Material.IRON_AXE), "Jumper", getKitDescription("jumper"))
		.setOption(4, new ItemStack(Material.TNT), "Tnt", getKitDescription("tnt"))
		.setOption(5, new ItemStack(Material.ENDER_PEARL), "Warper", getKitDescription("warper"));

		iconm.open(Bukkit.getPlayerExact(p));
	}
	
	
	public void getArenaReward(String arena, Player p){
		if(!getConfig().isSet(arena + ".reward.use")){
			getConfig().set(arena + ".reward.use", false);
			this.saveConfig();
		}
		if(!getConfig().getBoolean(arena + ".reward.use")){
			if (economy) {
				EconomyResponse r = econ.depositPlayer(p.getName(), getConfig().getDouble("config.money_reward_per_game"));
				if (!r.transactionSuccess()) {
					getServer().getPlayer(p.getName()).sendMessage(String.format("An error occured: %s", r.errorMessage));
				}
			} else {
				p.getInventory().addItem(new ItemStack(Material.getMaterial(itemid), itemamount));
				p.updateInventory();
			}

			// command reward
			if (command_reward) {
				String[] t = cmd.replaceAll("<player>", p.getName()).split(";");
				System.out.println(t);
				for(String t_ : t){
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), t_);
				}
			}
			return;
		}
		if (economy) {
			EconomyResponse r = econ.depositPlayer(p.getName(), getConfig().getDouble(arena + ".reward.money_reward_per_game"));
			if (!r.transactionSuccess()) {
				getServer().getPlayer(p.getName()).sendMessage(String.format("An error occured: %s", r.errorMessage));
			}
		} else {
			p.getInventory().addItem(new ItemStack(Material.getMaterial(getConfig().getInt(arena + ".reward.item_reward_id")), getConfig().getInt(arena + ".reward.item_reward_amount")));
			p.updateInventory();
		}

		// command reward
		if (command_reward) {
			String cmd = getConfig().getString(arena + ".reward.commandreward");
			String[] t = cmd.replaceAll("<player>", p.getName()).split(";");
			System.out.println(t);
			for(String t_ : t){
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), t_);
			}
		}
	}
	
	public void setArenaReward(String arena, String component, Integer amount){
		getConfig().set(arena + ".reward." + component, amount);
		this.saveConfig();
	}
	
	public void setArenaCommandReward(String arena, String cmd){
		getConfig().set(arena + ".reward.commandreward", cmd);
		this.saveConfig();
	}
	
	public void setArenaDefaultRewards(String arena){
		if(!getConfig().isSet(arena + ".reward.use")){
			getConfig().set(arena + ".reward.use", true);
			this.saveConfig();
		}
		if(getConfig().getBoolean(arena + ".reward.use")){
			return;
		}
		setArenaReward(arena, "money_reward_per_game", (int)getConfig().getDouble("config.money_reward_per_game"));
		setArenaReward(arena, "item_reward_id", itemid);
		setArenaReward(arena, "item_reward_amount", itemamount);
		setArenaCommandReward(arena, cmd);
		getConfig().set(arena + ".reward.use", true);
		this.saveConfig();
	}
}
