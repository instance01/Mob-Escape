package com.comze_instancelabs.mobescape.V1_7;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mobescape.Kits;
import com.comze_instancelabs.mobescape.Main;
import com.comze_instancelabs.mobescape.Slimey;
import com.comze_instancelabs.mobescape.V1_6.V1_6Dragon;
import com.comze_instancelabs.mobescape.V1_6.V1_6Wither;

import net.minecraft.server.v1_7_R1.EntityTypes;
import net.minecraft.server.v1_7_R1.PacketPlayOutWorldEvent;

public class V1_7Wither {

	public static HashMap<String, MEWither> wither = new HashMap<String, MEWither>();

	
	public static final void playBlockBreakParticles(final Location loc, final Material m, final Player... players) {
		@SuppressWarnings("deprecation")
		PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(2001, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), m.getId(), false);
		for (final Player p : players) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	
	public MEWither spawnWither(Main m, String arena, Location t) {
		/*if(dragons.containsKey(arena)){
			return wither.get(arena);
		}*/
		m.getLogger().info("WITHER SPAWNED " + arena + " " + t.toString());
		Object w = ((CraftWorld) t.getWorld()).getHandle();
		if(m.getDragonWayPoints(arena) == null){
			m.getLogger().severe("You forgot to set any FlyPoints! You need to have min. 2 and one of them has to be at finish.");
			return null;
		}
		MEWither t_ = new MEWither(m, arena, t, (net.minecraft.server.v1_7_R1.World) ((CraftWorld) t.getWorld()).getHandle(), m.getDragonWayPoints(arena));
		((net.minecraft.server.v1_7_R1.World) w).addEntity(t_, CreatureSpawnEvent.SpawnReason.CUSTOM);
		t_.setCustomName(m.dragon_name);

		return t_;
	}
	
	
	public BukkitTask start(final Main m, final String arena) {
		m.ingame.put(arena, true);
		m.astarted.put(arena, false);
		m.getLogger().info("STARTED");
		// start countdown timer
		if (m.start_announcement) {
			Bukkit.getServer().broadcastMessage(m.starting + " " + Integer.toString(m.start_countdown));
		}

		Bukkit.getServer().getScheduler().runTaskLater(m, new Runnable() {
			public void run() {
				// clear hostile mobs on start:
				for (Player p : m.arenap.keySet()) {
					p.playSound(p.getLocation(), Sound.CAT_MEOW, 1, 0);
					if (m.arenap.get(p).equalsIgnoreCase(arena)) {
						for (Entity t : p.getNearbyEntities(64, 64, 64)) {
							if (t.getType() == EntityType.ZOMBIE || t.getType() == EntityType.SKELETON || t.getType() == EntityType.CREEPER || t.getType() == EntityType.CAVE_SPIDER || t.getType() == EntityType.SPIDER || t.getType() == EntityType.WITCH || t.getType() == EntityType.GIANT) {
								t.remove();
							}
						}
						break;
					}
				}
			}
		}, 20L);

		int t = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {
			public void run() {
				if (!m.countdown_count.containsKey(arena)) {
					m.countdown_count.put(arena, m.start_countdown);
				}
				int count = m.countdown_count.get(arena);
				for (Player p : m.arenap.keySet()) {
					if (m.arenap.get(p).equalsIgnoreCase(arena)) {
						p.sendMessage(m.starting_in + count + m.starting_in2);
					}
				}
				count--;
				m.countdown_count.put(arena, count);
				if (count < 0) {
					m.countdown_count.put(arena, m.start_countdown);

					if (m.start_announcement) {
						Bukkit.getServer().broadcastMessage(m.started);
					}

					m.astarted.put(arena, true);
					
					// update sign
					Bukkit.getServer().getScheduler().runTask(m, new Runnable(){
						public void run(){
							Sign s = m.getSignFromArena(arena);
							if (s != null) {
								s.setLine(1, m.sign_second_ingame);
								s.update();
							}	
						}
					});
					
					for (final Player p : m.arenap.keySet()) {
						if (p.isOnline()) {
							if(m.pkit.containsKey(p)){
								String kit = m.pkit.get(p);
								
								if(kit.equalsIgnoreCase("jumper")){
									Kits.giveJumperKit(m, p);
								}else if(kit.equalsIgnoreCase("warper")){
									Kits.giveWarperKit(m, p);
								}else if(kit.equalsIgnoreCase("tnt")){
									Kits.giveTNTKit(m, p);
								}
								m.pkit.remove(p);
							}
						}
					}
					
					Bukkit.getServer().getScheduler().cancelTask(m.countdown_id.get(arena));
				}
			}
		}, 0, 20).getTaskId();
		m.countdown_id.put(arena, t);

		final String dir = m.getDirection(m.getSpawn(arena).getYaw());
		
		// spawn enderdragon
		if (dir.equalsIgnoreCase("south")) {
			Bukkit.getScheduler().runTask(m, new Runnable() {
				public void run() {
					try{
						boolean cont = true;
						if(m.getDragonSpawn(arena) != null){
							for(Entity e : m.getNearbyEntities(m.getDragonSpawn(arena), 40)){
								if(e.getType() == EntityType.WITHER){
									cont = false;
								}
							}
						}
						if(cont){
							if(m.getDragonSpawn(arena) != null){
								wither.put(arena, spawnWither(m, arena, m.getDragonSpawn(arena)));
								m.getLogger().info("DRAGON SPAWN");
							}else{
								m.getLogger().info("NORMAL SPAWN");
								wither.put(arena, spawnWither(m, arena, m.getSpawn(arena).add(0.0D, 0.0D, -3.0D)));
							}	
						}
					}catch(Exception e){
						m.stop(m.h.get(arena), arena);
						return;
					}
				}
			});
		} else if (dir.equalsIgnoreCase("north")) {
			Bukkit.getScheduler().runTask(m, new Runnable() {
				public void run() {
					try{
						boolean cont = true;
						if(m.getDragonSpawn(arena) != null){
							for(Entity e : m.getNearbyEntities(m.getDragonSpawn(arena), 40)){
								if(e.getType() == EntityType.WITHER){
									cont = false;
								}
							}
						}
						if(cont){
							if(m.getDragonSpawn(arena) != null){
								wither.put(arena, spawnWither(m, arena, m.getDragonSpawn(arena)));
								m.getLogger().info("DRAGON SPAWN");
							}else{
								m.getLogger().info("NORMAL SPAWN");
								wither.put(arena, spawnWither(m, arena, m.getSpawn(arena).add(0.0D, 0.0D, +3.0D)));
							}
						}
					}catch(Exception e){
						m.stop(m.h.get(arena), arena);
						return;
					}
				}
			});
		} else if (dir.equalsIgnoreCase("east")) {
			Bukkit.getScheduler().runTask(m, new Runnable() {
				public void run() {
					try{
						boolean cont = true;
						if(m.getDragonSpawn(arena) != null){
							for(Entity e : m.getNearbyEntities(m.getDragonSpawn(arena), 40)){
								if(e.getType() == EntityType.WITHER){
									cont = false;
								}
							}
						}
						if(cont){
							if(m.getDragonSpawn(arena) != null){
								wither.put(arena, spawnWither(m, arena, m.getDragonSpawn(arena)));
								m.getLogger().info("DRAGON SPAWN");
							}else{
								m.getLogger().info("NORMAL SPAWN");
								wither.put(arena, spawnWither(m, arena, m.getSpawn(arena).add(-3.0D, 0.0D, 0.0D)));	
							}
						}
					}catch(Exception e){
						m.stop(m.h.get(arena), arena);
						return;
					}
				}
			});
		} else if (dir.equalsIgnoreCase("west")) {
			Bukkit.getScheduler().runTask(m, new Runnable() {
				public void run() {
					try{
						boolean cont = true;
						if(m.getDragonSpawn(arena) != null){
							for(Entity e : m.getNearbyEntities(m.getDragonSpawn(arena), 40)){
								if(e.getType() == EntityType.WITHER){
									cont = false;
								}
							}
						}
						if(cont){
							if(m.getDragonSpawn(arena) != null){
								wither.put(arena, spawnWither(m, arena, m.getDragonSpawn(arena)));
								m.getLogger().info("DRAGON SPAWN");
							}else{
								m.getLogger().info("NORMAL SPAWN");
								wither.put(arena, spawnWither(m, arena, m.getSpawn(arena).add(3.0D, 0.0D, 0.0D)));
							}
						}
					}catch(Exception e){
						m.stop(m.h.get(arena), arena);
						return;
					}
				}
			});
		}

		final int d = 1;
		
		BukkitTask id__ = null;
		id__ = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {
			@Override
			public void run() {
				try {
					/*if (dir.equalsIgnoreCase("south")) {
						if (wither.get(arena).locZ > getFinish(arena).getBlockZ()) {
							stop(h.get(arena), arena);
							return;
						}
					} else if (dir.equalsIgnoreCase("north")) {
						if (wither.get(arena).locZ < getFinish(arena).getBlockZ()) {
							stop(h.get(arena), arena);
							return;
						}
					} else if (dir.equalsIgnoreCase("east")) {
						if (wither.get(arena).locX > getFinish(arena).getBlockX()) {
							stop(h.get(arena), arena);
							return;
						}
					} else if (dir.equalsIgnoreCase("west")) {
						if (wither.get(arena).locX < getFinish(arena).getBlockX()) {
							stop(h.get(arena), arena);
							return;
						}
					}*/

					for (final Player p : m.arenap.keySet()) {
						if (p.isOnline()) {
							if (m.arenap.get(p).equalsIgnoreCase(arena)) {
								m.arenap_.put(p.getName(), arena);
								
								Vector vv = wither.get(arena).getCurrentPosition();
								Location dragon = new Location(p.getWorld(), wither.get(arena).locX, wither.get(arena).locY, wither.get(arena).locZ);
								Location l = new Location(p.getWorld(), vv.getX(), vv.getY(), vv.getZ());
								if(p.getLocation().distance(l) - dragon.distance(l) > 3){
									m.simulatePlayerFall(p);
								}
							}
						}
					}

					final Location l = m.getSpawn(arena);
					if (m.dragon_move_increment.containsKey(arena)) {
						m.dragon_move_increment.put(arena, m.dragon_move_increment.get(arena) + 0.35D);
					} else {
						m.dragon_move_increment.put(arena, 0.25D);
					}

					Location l1 = m.getHighBoundary(arena);
					Location l2 = m.getLowBoundary(arena);
					int length1 = l1.getBlockX() - l2.getBlockX();
					int length2 = l1.getBlockY() - l2.getBlockY();
					int length3 = l1.getBlockZ() - l2.getBlockZ();
					boolean f = false;
					boolean f_ = false;
					if (l2.getBlockX() > l1.getBlockX()) {
						length1 = l2.getBlockX() - l1.getBlockX();
						f = true;
					}

					if (l2.getBlockZ() > l1.getBlockZ()) {
						length3 = l2.getBlockZ() - l1.getBlockZ();
						f_ = true;
					}

					if(!wither.containsKey(arena)){
						return;
					}
					if(wither.get(arena) == null){
						return;
					}
					
					String dir_ = m.getDirection(wither.get(arena).getBukkitEntity().getLocation().getYaw());

					
					if (dir_.equalsIgnoreCase("south")) {
						//wither.get(arena).setPosition(l.getX(), l.getY(), l.getZ() + m.dragon_move_increment.get(arena));

						Vector v = wither.get(arena).getNextPosition();
						if(v != null && wither.get(arena) != null){
							wither.get(arena).setPosition(v.getX(), v.getY(), v.getZ());
						}

						if(wither.get(arena) == null){
							return;
						}
						
						V1_7Wither.destroy(m, l1, l2, arena, length2);
					} else if (dir_.equalsIgnoreCase("north")) {
						//wither.get(arena).setPosition(l.getX(), l.getY(), l.getZ() - m.dragon_move_increment.get(arena));

						Vector v = wither.get(arena).getNextPosition();
						if(v != null && wither.get(arena) != null){
							wither.get(arena).setPosition(v.getX(), v.getY(), v.getZ());
						}
						
						if(wither.get(arena) == null){
							return;
						}

						V1_7Wither.destroy(m, l1, l2, arena, length2);
					} else if (dir_.equalsIgnoreCase("east")) {
						//wither.get(arena).setPosition(l.getX() + m.dragon_move_increment.get(arena), l.getY(), l.getZ());

						Vector v = wither.get(arena).getNextPosition();
						if(v != null && wither.get(arena) != null){
							wither.get(arena).setPosition(v.getX(), v.getY(), v.getZ());
						}

						if(wither.get(arena) == null){
							return;
						}

						V1_7Wither.destroy(m, l1, l2, arena, length2);
					} else if (dir_.equalsIgnoreCase("west")) {
						//wither.get(arena).setPosition(l.getX() - m.dragon_move_increment.get(arena), l.getY(), l.getZ());

						Vector v = wither.get(arena).getNextPosition();
						if(v != null && wither.get(arena) != null){
							wither.get(arena).setPosition(v.getX(), v.getY(), v.getZ());
						}
						
						if(wither.get(arena) == null){
							return;
						}

						V1_7Wither.destroy(m, l1, l2, arena, length2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Bukkit.getScheduler().runTask(m, new Runnable(){
					public void run(){
						//TODO reminder
						m.updateScoreboard();
					}
				});

			}
		}, 3 + 20 * m.start_countdown, 3);

		m.h.put(arena, id__);
		m.tasks.put(arena, id__);
		return id__;
	}
	
	
	
	
	public void stop(final Main m, BukkitTask t, final String arena) {
		m.ingame.put(arena, false);
		try {
			t.cancel();
		} catch (Exception e) {

		}

		try {
			removeWither(wither.get(arena));
			wither.put(arena, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		m.dragon_move_increment.put(arena, 0.0D);

		Bukkit.getScheduler().runTaskLater(m, new Runnable() {

			public void run() {
				m.countdown_count.put(arena, m.start_countdown);
				try {
					Bukkit.getServer().getScheduler().cancelTask(m.countdown_id.get(arena));
				} catch (Exception e) {
				}

				ArrayList<Player> torem = new ArrayList<Player>();
				if(m.astarted.containsKey(arena)){
					if(m.astarted.get(arena)){
						m.determineWinners(arena);
					}	
				}
				m.astarted.put(arena, false);
				for (Player p : m.arenap.keySet()) {
					if (m.arenap.get(p).equalsIgnoreCase(arena)) {
						m.leaveArena(p, false, false);
						m.removeScoreboard(arena, p);
						torem.add(p);
					}
				}

				for (Player p : torem) {
					m.arenap.remove(p);
				}
				torem.clear();

				m.winner.clear();
				m.currentscore.clear();

				Sign s = m.getSignFromArena(arena);
				if (s != null) {
					s.setLine(1, m.sign_second_restarting);
					s.setLine(3, "0/" + Integer.toString(m.getArenaMaxPlayers(arena)));
					s.update();
				}

				m.h.remove(arena);

				m.reset(arena);

				// clean out offline players
				m.clean();
			}

		}, 20); // 1 second
	}
	
	
	public void removeWither(MEWither t) {
		if (t != null) {
			t.getBukkitEntity().remove();
		}
	}
	
	
	public static void destroy(final Main m, final Location l, final Location l2, String arena, int length2){
		// south
		for (int i = 0; i < m.destroy_radius; i++) { // length1
			for (int j = 0; j < length2; j++) {
				final Block b;
				b = l.getWorld().getBlockAt(new Location(l.getWorld(), wither.get(arena).locX + (m.destroy_radius / 2) - i, l2.getBlockY() + j - 1, wither.get(arena).locZ + 3));
				Bukkit.getScheduler().runTask(m, new Runnable() {
					public void run() {
						if (b.getType() != Material.AIR) {
							playBlockBreakParticles(b.getLocation(), b.getType());
							if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
								l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData()).setMetadata("vortex", new FixedMetadataValue(m, "protected"));	
							}
							b.setType(Material.AIR);
						}
					}
				});
			}
		}
		
		// north
		for (int i = 0; i < m.destroy_radius; i++) { // length1
			for (int j = 0; j < length2; j++) {
				final Block b;
				b = l.getWorld().getBlockAt(new Location(l.getWorld(), wither.get(arena).locX + (m.destroy_radius / 2) - i, l2.getBlockY() + j - 1, wither.get(arena).locZ - 3));

				Bukkit.getScheduler().runTask(m, new Runnable() {
					public void run() {
						if (b.getType() != Material.AIR) {
							playBlockBreakParticles(b.getLocation(), b.getType());
							if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
								l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData()).setMetadata("vortex", new FixedMetadataValue(m, "protected"));	
							}
							b.setType(Material.AIR);
						}
					}
				});
			}
		}
		
		// east
		for (int i = 0; i < m.destroy_radius; i++) { // length1
			for (int j = 0; j < length2; j++) {
				final Block b;
				b = l.getWorld().getBlockAt(new Location(l.getWorld(), wither.get(arena).locX + 3, l2.getBlockY() + j - 1, wither.get(arena).locZ + (m.destroy_radius / 2) - i));

				Bukkit.getScheduler().runTask(m, new Runnable() {
					public void run() {
						if (b.getType() != Material.AIR) {
							playBlockBreakParticles(b.getLocation(), b.getType());
							if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
								l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData()).setMetadata("vortex", new FixedMetadataValue(m, "protected"));	
							}
							b.setType(Material.AIR);
						}
					}
				});
			}
		}
		
		// west
		for (int i = 0; i < m.destroy_radius; i++) { // length1
			for (int j = 0; j < length2; j++) {
				final Block b;
				b = l.getWorld().getBlockAt(new Location(l.getWorld(), wither.get(arena).locX - 3, l2.getBlockY() + j - 1, wither.get(arena).locZ + (m.destroy_radius / 2) - i));

				Bukkit.getScheduler().runTask(m, new Runnable() {
					public void run() {
						if (b.getType() != Material.AIR) {
							playBlockBreakParticles(b.getLocation(), b.getType());
							if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
								l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData()).setMetadata("vortex", new FixedMetadataValue(m, "protected"));	
							}
							b.setType(Material.AIR);
						}
					}
				});
			}
		}
	}

}
