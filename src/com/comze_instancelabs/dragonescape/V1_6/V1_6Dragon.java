package com.comze_instancelabs.dragonescape.V1_6;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.server.v1_6_R3.EntityTypes;
import net.minecraft.server.v1_6_R3.Packet61WorldEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.dragonescape.Kits;
import com.comze_instancelabs.dragonescape.Main;

public class V1_6Dragon {

	public static HashMap<String, MEDragon1_6> dragons1_6 = new HashMap<String, MEDragon1_6>();

	public V1_6Dragon(){
	}
	
	public static boolean registerEntities() {
		/*try {
			Method a = EntityTypes.class.getDeclaredMethod("a", new Class<?>[] { Class.class, String.class, int.class });
			a.setAccessible(true);
			a.invoke(a, Slimey.class, "Slimey", 55);
		} catch (Exception ex) {
		}*/

		try {
			Method a = EntityTypes.class.getDeclaredMethod("a", new Class<?>[] { Class.class, String.class, int.class });
			a.setAccessible(true);
			a.invoke(a, MEWither1_6.class, "MEWither1_6", 64);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		try {
			Method a = EntityTypes.class.getDeclaredMethod("a", new Class<?>[] { Class.class, String.class, int.class });
			a.setAccessible(true);
			a.invoke(a, MEDragon1_6.class, "MEDragon1_6", 63);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	
	public static final void playBlockBreakParticles(final Location loc, final Material m, final Player... players) {
		@SuppressWarnings("deprecation")
		Packet61WorldEvent packet = new Packet61WorldEvent(2001, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), m.getId(), false);
		for (final Player p : players) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	
	public static MEDragon1_6 spawnEnderdragon1_6(Main m, String arena, Location t) {
		Object w = ((CraftWorld) t.getWorld()).getHandle();
		if(m.getDragonWayPoints(arena) == null){
			m.getLogger().severe("You forgot to set any FlyPoints! You need to have min. 2 and one of them has to be at finish.");
			return null;
		}
		MEDragon1_6 t_ = new MEDragon1_6(m, arena, t, (net.minecraft.server.v1_6_R3.World) ((CraftWorld) t.getWorld()).getHandle(), m.getDragonWayPoints(arena));
		((net.minecraft.server.v1_6_R3.World) w).addEntity(t_, CreatureSpawnEvent.SpawnReason.CUSTOM);
		t_.setCustomName(m.dragon_name);

		return t_;
	}

	
	public BukkitTask start(final Main m, final String arena) {
		m.ingame.put(arena, true);
		m.astarted.put(arena, false);
		
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
						if(m.getDragonSpawn(arena) != null){
							dragons1_6.put(arena, spawnEnderdragon1_6(m, arena, m.getDragonSpawn(arena)));
						}else{
							dragons1_6.put(arena, spawnEnderdragon1_6(m, arena, m.getSpawn(arena).add(0.0D, 0.0D, -3.0D)));
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
						if(m.getDragonSpawn(arena) != null){
							dragons1_6.put(arena, spawnEnderdragon1_6(m, arena, m.getDragonSpawn(arena)));
						}else{
							dragons1_6.put(arena, spawnEnderdragon1_6(m, arena, m.getSpawn(arena).add(0.0D, 0.0D, +3.0D)));
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
						if(m.getDragonSpawn(arena) != null){
							dragons1_6.put(arena, spawnEnderdragon1_6(m, arena, m.getDragonSpawn(arena)));
						}else{
							dragons1_6.put(arena, spawnEnderdragon1_6(m, arena, m.getSpawn(arena).add(-3.0D, 0.0D, 0.0D)));	
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
						if(m.getDragonSpawn(arena) != null){
							dragons1_6.put(arena, spawnEnderdragon1_6(m, arena, m.getDragonSpawn(arena)));
						}else{
							dragons1_6.put(arena, spawnEnderdragon1_6(m, arena, m.getSpawn(arena).add(3.0D, 0.0D, 0.0D)));
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
						if (dragons1_6.get(arena).locZ > getFinish(arena).getBlockZ()) {
							stop(h.get(arena), arena);
							return;
						}
					} else if (dir.equalsIgnoreCase("north")) {
						if (dragons1_6.get(arena).locZ < getFinish(arena).getBlockZ()) {
							stop(h.get(arena), arena);
							return;
						}
					} else if (dir.equalsIgnoreCase("east")) {
						if (dragons1_6.get(arena).locX > getFinish(arena).getBlockX()) {
							stop(h.get(arena), arena);
							return;
						}
					} else if (dir.equalsIgnoreCase("west")) {
						if (dragons1_6.get(arena).locX < getFinish(arena).getBlockX()) {
							stop(h.get(arena), arena);
							return;
						}
					}*/

					for (final Player p : m.arenap.keySet()) {
						if (p.isOnline()) {
							if (m.arenap.get(p).equalsIgnoreCase(arena)) {
								m.arenap_.put(p.getName(), arena);
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

					if(!dragons1_6.containsKey(arena)){
						return;
					}
					if(dragons1_6.get(arena) == null){
						return;
					}
					
					String dir_ = m.getDirection(dragons1_6.get(arena).getBukkitEntity().getLocation().getYaw());

					
					if (dir_.equalsIgnoreCase("south")) {
						//dragons1_6.get(arena).setPosition(l.getX(), l.getY(), l.getZ() + m.dragon_move_increment.get(arena));

						Vector v = dragons1_6.get(arena).getNextPosition();
						if(v != null && dragons1_6.get(arena) != null){
							dragons1_6.get(arena).setPosition(v.getX(), v.getY(), v.getZ());
						}

						if(dragons1_6.get(arena) == null){
							return;
						}
						
						/*for (int i = 0; i < m.destroy_radius; i++) {
							for (int j = 0; j < length2; j++) {
								final Block b;
								b = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons1_6.get(arena).locX + (m.destroy_radius / 2) - i, l2.getBlockY() + j - 1, dragons1_6.get(arena).locZ + 3));
								Bukkit.getScheduler().runTask(m, new Runnable() {
									public void run() {
										if (b.getType() != Material.AIR) {
											playBlockBreakParticles(b.getLocation(), b.getType());
											l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData()).setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											b.setType(Material.AIR);
										}
									}
								});
							}
						}*/
						V1_6Dragon.destroy(m, l1, l2, arena, length2);
					} else if (dir_.equalsIgnoreCase("north")) {
						//dragons1_6.get(arena).setPosition(l.getX(), l.getY(), l.getZ() - m.dragon_move_increment.get(arena));

						Vector v = dragons1_6.get(arena).getNextPosition();
						if(v != null && dragons1_6.get(arena) != null){
							dragons1_6.get(arena).setPosition(v.getX(), v.getY(), v.getZ());
						}
						
						if(dragons1_6.get(arena) == null){
							return;
						}
						
						/*for (int i = 0; i < m.destroy_radius; i++) {
							for (int j = 0; j < length2; j++) {
								final Block b;
								b = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons1_6.get(arena).locX + (m.destroy_radius / 2) - i, l2.getBlockY() + j - 1, dragons1_6.get(arena).locZ - 3));

								Bukkit.getScheduler().runTask(m, new Runnable() {
									public void run() {
										if (b.getType() != Material.AIR) {
											playBlockBreakParticles(b.getLocation(), b.getType());
											l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData()).setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											b.setType(Material.AIR);
										}
									}
								});
							}
						}*/
						V1_6Dragon.destroy(m, l1, l2, arena, length2);
					} else if (dir_.equalsIgnoreCase("east")) {
						//dragons1_6.get(arena).setPosition(l.getX() + m.dragon_move_increment.get(arena), l.getY(), l.getZ());

						Vector v = dragons1_6.get(arena).getNextPosition();
						if(v != null && dragons1_6.get(arena) != null){
							dragons1_6.get(arena).setPosition(v.getX(), v.getY(), v.getZ());
						}
						
						if(dragons1_6.get(arena) == null){
							return;
						}
						
						/*for (int i = 0; i < m.destroy_radius; i++) {
							for (int j = 0; j < length2; j++) {
								final Block b;
								b = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons1_6.get(arena).locX + 3, l2.getBlockY() + j - 1, dragons1_6.get(arena).locZ + (m.destroy_radius / 2) - i));

								Bukkit.getScheduler().runTask(m, new Runnable() {
									public void run() {
										if (b.getType() != Material.AIR) {
											playBlockBreakParticles(b.getLocation(), b.getType());
											l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData()).setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											b.setType(Material.AIR);
										}
									}
								});
							}
						}*/
						V1_6Dragon.destroy(m, l1, l2, arena, length2);
					} else if (dir_.equalsIgnoreCase("west")) {
						//dragons1_6.get(arena).setPosition(l.getX() - m.dragon_move_increment.get(arena), l.getY(), l.getZ());

						Vector v = dragons1_6.get(arena).getNextPosition();
						if(v != null && dragons1_6.get(arena) != null){
							dragons1_6.get(arena).setPosition(v.getX(), v.getY(), v.getZ());
						}
						
						if(dragons1_6.get(arena) == null){
							return;
						}
						
						/*for (int i = 0; i < m.destroy_radius; i++) {
							for (int j = 0; j < length2; j++) {
								final Block b;
								b = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons1_6.get(arena).locX - 3, l2.getBlockY() + j - 1, dragons1_6.get(arena).locZ + (m.destroy_radius / 2) - i));

								Bukkit.getScheduler().runTask(m, new Runnable() {
									public void run() {
										if (b.getType() != Material.AIR) {
											playBlockBreakParticles(b.getLocation(), b.getType());
											l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData()).setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											b.setType(Material.AIR);
										}
									}
								});
							}
						}*/
						V1_6Dragon.destroy(m, l1, l2, arena, length2);
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
			removeEnderdragon(dragons1_6.get(arena));
			dragons1_6.put(arena, null);
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
				if(m.astarted.get(arena)){
					m.determineWinners(arena);
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

	
	public void removeEnderdragon(MEDragon1_6 t) {
		if (t != null) {
			t.getBukkitEntity().remove();
		}
	}
	
	
	public static void destroy(final Main m, final Location l, final Location l2, String arena, int length2){
		// south
		for (int i = 0; i < m.destroy_radius; i++) { // length1
			for (int j = 0; j < length2; j++) {
				final Block b;
				b = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons1_6.get(arena).locX + (m.destroy_radius / 2) - i, l2.getBlockY() + j - 1, dragons1_6.get(arena).locZ + 3));
				Bukkit.getScheduler().runTask(m, new Runnable() {
					public void run() {
						if (b.getType() != Material.AIR) {
							playBlockBreakParticles(b.getLocation(), b.getType());
							if(b.getType() != Material.WATER && b.getType() != Material.LAVA){
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
				b = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons1_6.get(arena).locX + (m.destroy_radius / 2) - i, l2.getBlockY() + j - 1, dragons1_6.get(arena).locZ - 3));

				Bukkit.getScheduler().runTask(m, new Runnable() {
					public void run() {
						if (b.getType() != Material.AIR) {
							playBlockBreakParticles(b.getLocation(), b.getType());
							if(b.getType() != Material.WATER && b.getType() != Material.LAVA){
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
				b = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons1_6.get(arena).locX + 3, l2.getBlockY() + j - 1, dragons1_6.get(arena).locZ + (m.destroy_radius / 2) - i));

				Bukkit.getScheduler().runTask(m, new Runnable() {
					public void run() {
						if (b.getType() != Material.AIR) {
							playBlockBreakParticles(b.getLocation(), b.getType());
							if(b.getType() != Material.WATER && b.getType() != Material.LAVA){
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
				b = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons1_6.get(arena).locX - 3, l2.getBlockY() + j - 1, dragons1_6.get(arena).locZ + (m.destroy_radius / 2) - i));

				Bukkit.getScheduler().runTask(m, new Runnable() {
					public void run() {
						if (b.getType() != Material.AIR) {
							playBlockBreakParticles(b.getLocation(), b.getType());
							if(b.getType() != Material.WATER && b.getType() != Material.LAVA){
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
