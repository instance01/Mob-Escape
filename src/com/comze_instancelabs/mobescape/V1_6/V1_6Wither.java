package com.comze_instancelabs.mobescape.V1_6;

import java.util.HashMap;

import net.minecraft.server.v1_6_R3.Packet61WorldEvent;

import org.bukkit.Bukkit;
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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mobescape.AbstractWither;
import com.comze_instancelabs.mobescape.Kits;
import com.comze_instancelabs.mobescape.Main;
import com.comze_instancelabs.mobescape.mobtools.Tools;

public class V1_6Wither implements AbstractWither {

	public static HashMap<String, MEWither1_6> wither1_6 = new HashMap<String, MEWither1_6>();

	public V1_6Wither(){
	}
	
	
	public void playBlockBreakParticles(final Location loc, final Material m, final Player... players) {
		@SuppressWarnings("deprecation")
		Packet61WorldEvent packet = new Packet61WorldEvent(2001, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), m.getId(), false);
		for (final Player p : players) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	
	public static MEWither1_6 spawnWither1_6(Main m, String arena, Location t) {
		Object w = ((CraftWorld) t.getWorld()).getHandle();
		if(m.getDragonWayPoints(arena) == null){
			m.getLogger().severe("You forgot to set any FlyPoints! You need to have min. 2 and one of them has to be at finish.");
			return null;
		}
		MEWither1_6 t_ = new MEWither1_6(m, arena, t, (net.minecraft.server.v1_6_R3.World) ((CraftWorld) t.getWorld()).getHandle(), m.getDragonWayPoints(arena));
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

		Bukkit.getScheduler().runTask(m, new Runnable() {
			public void run() {
				try{
					if(m.getDragonSpawn(arena) != null){
						wither1_6.put(arena, spawnWither1_6(m, arena, m.getDragonSpawn(arena)));
					}else{
						wither1_6.put(arena, spawnWither1_6(m, arena, m.getSpawn(arena)));
					}
				}catch(Exception e){
					m.stop(m.h.get(arena), arena);
					return;
				}
			}
		});

		final int d = 1;
		
		BukkitTask id__ = null;
		id__ = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {
			@Override
			public void run() {
				try {
					/*if (dir.equalsIgnoreCase("south")) {
						if (wither1_6.get(arena).locZ > getFinish(arena).getBlockZ()) {
							stop(h.get(arena), arena);
							return;
						}
					} else if (dir.equalsIgnoreCase("north")) {
						if (wither1_6.get(arena).locZ < getFinish(arena).getBlockZ()) {
							stop(h.get(arena), arena);
							return;
						}
					} else if (dir.equalsIgnoreCase("east")) {
						if (wither1_6.get(arena).locX > getFinish(arena).getBlockX()) {
							stop(h.get(arena), arena);
							return;
						}
					} else if (dir.equalsIgnoreCase("west")) {
						if (wither1_6.get(arena).locX < getFinish(arena).getBlockX()) {
							stop(h.get(arena), arena);
							return;
						}
					}*/

					for (final Player p : m.arenap.keySet()) {
						if (p.isOnline()) {
							if (m.arenap.get(p).equalsIgnoreCase(arena)) {
								m.arenap_.put(p.getName(), arena);
								
								if(m.die_behind_mob){
									Vector vv = wither1_6.get(arena).getCurrentPosition();
									Vector vv_ = wither1_6.get(arena).getCurrentPositionNext();
									Location dragon = new Location(p.getWorld(), wither1_6.get(arena).locX, wither1_6.get(arena).locY, wither1_6.get(arena).locZ);
									Location l = new Location(p.getWorld(), vv.getX(), vv.getY(), vv.getZ());
									Location l_ = new Location(p.getWorld(), vv_.getX(), vv_.getY(), vv_.getZ());
									if(p.getLocation().distance(l) - dragon.distance(l) > 10 && p.getLocation().distance(l_) - dragon.distance(l_) > 10){
										m.simulatePlayerFall(p);
									}
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

					if(!wither1_6.containsKey(arena)){
						return;
					}
					if(wither1_6.get(arena) == null){
						return;
					}
					
					Vector v = wither1_6.get(arena).getNextPosition();
					if(v != null && wither1_6.get(arena) != null){
						wither1_6.get(arena).setPosition(v.getX(), v.getY(), v.getZ());
					}

					if(wither1_6.get(arena) == null){
						return;
					}

					V1_6Wither.destroyStatic(m, l1, l2, arena, length2);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Bukkit.getScheduler().runTask(m, new Runnable(){
					public void run(){
						//TODO reminder
						m.updateScoreboard(arena);
					}
				});

			}
		}, 3 + 20 * m.start_countdown, 3);

		m.h.put(arena, id__);
		m.tasks.put(arena, id__);
		return id__;
	}
	
	public void removeWither(String arena){
		try {
			removeWither(wither1_6.get(arena));
			wither1_6.put(arena, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop(final Main m, BukkitTask t, final String arena) {
		Tools t_ = new Tools();
		t_.stop(m, t, arena, true, false, "wither");
	}

	
	public void removeWither(MEWither1_6 t) {
		if (t != null) {
			t.getBukkitEntity().remove();
		}
	}
	
	public Block[] getLoc(Main m, final Location l, String arena, int i, int j, Location l2){
		Block[] b = new Block[4];
		b[0] = l.getWorld().getBlockAt(new Location(l.getWorld(), wither1_6.get(arena).locX + (m.destroy_radius / 2) - i, wither1_6.get(arena).locY + j - 1, wither1_6.get(arena).locZ + 3));
		b[1] = l.getWorld().getBlockAt(new Location(l.getWorld(), wither1_6.get(arena).locX + (m.destroy_radius / 2) - i, wither1_6.get(arena).locY + j - 1, wither1_6.get(arena).locZ - 3));
		b[2] = l.getWorld().getBlockAt(new Location(l.getWorld(), wither1_6.get(arena).locX + 3, wither1_6.get(arena).locY + j - 1, wither1_6.get(arena).locZ + (m.destroy_radius / 2) - i));
		b[3] = l.getWorld().getBlockAt(new Location(l.getWorld(), wither1_6.get(arena).locX - 3, wither1_6.get(arena).locY + j - 1, wither1_6.get(arena).locZ + (m.destroy_radius / 2) - i));

		return b;
	}
	
	public static void destroyStatic(final Main m, final Location l, final Location l2, String arena, int length2){
		Tools.destroy(m, l, l2, arena, length2, "wither", true, false);
	}
	
	public void destroy(final Main m, final Location l, final Location l2, String arena, int length2){
		Tools.destroy(m, l, l2, arena, length2, "wither", true, false);
	}

}
