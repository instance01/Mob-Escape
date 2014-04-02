package com.comze_instancelabs.mobescape.mobtools;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mobescape.Main;
import com.comze_instancelabs.mobescape.V1_6.V1_6Dragon;
import com.comze_instancelabs.mobescape.V1_6.V1_6Wither;
import com.comze_instancelabs.mobescape.V1_7.V1_7Dragon;
import com.comze_instancelabs.mobescape.V1_7.V1_7Wither;
import com.comze_instancelabs.mobescape.V1_7._5.V1_7_5Dragon;
import com.comze_instancelabs.mobescape.V1_7._5.V1_7_5Wither;

public class Tools {

	
	public void stop(final Main m, BukkitTask t, final String arena, boolean mode1_6, boolean mode1_7_5, String type) {
		m.ingame.put(arena, false);
		try {
			t.cancel();
		} catch (Exception e) {

		}

		if(type.equalsIgnoreCase("dragon")){
			if(mode1_6){
				V1_6Dragon v = new V1_6Dragon();
				v.removeEnderdragon(arena);
			}else if(mode1_7_5){
				V1_7_5Dragon v = new V1_7_5Dragon();
				v.removeEnderdragon(arena);
			}else{
				V1_7Dragon v = new V1_7Dragon();
				v.removeEnderdragon(arena);
			}
		}else if(type.equalsIgnoreCase("wither")){
			if(mode1_6){
				V1_6Wither v = new V1_6Wither();
				v.removeWither(arena);
			}else if(mode1_7_5){
				V1_7_5Wither v = new V1_7_5Wither();
				v.removeWither(arena);
			}else{
				V1_7Wither v = new V1_7Wither();
				v.removeWither(arena);
			}
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
				if(!m.astarted.containsKey(arena)){
					m.astarted.put(arena, false);
				}
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

	
	public static void destroy(final Main m, final Location l, final Location l2, String arena, int length2, String type, boolean mode1_6, boolean mode1_7_5){
		// south
		for (int i = 0; i < m.destroy_radius; i++) { // length1
			for (int j = 0; j < m.destroy_radius; j++) {
				if(type.equalsIgnoreCase("dragon")){
					if(mode1_6){
						final V1_6Dragon v = new V1_6Dragon();
						for(final Block b : v.getLoc(m, l, arena, i, j - (m.destroy_radius / 3), l2)){
							Bukkit.getScheduler().runTask(m, new Runnable() {
								public void run() {
									if (b.getType() != Material.AIR) {
										v.playBlockBreakParticles(b.getLocation(), b.getType());
										if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
											FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
											fb.setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											fb.setVelocity(new Vector(0, 0.4, 0));
										}
										b.setType(Material.AIR);
									}
								}
							});
						}
					}else if(mode1_7_5){
						final V1_7_5Dragon v = new V1_7_5Dragon();
						for(final Block b : v.getLoc(m, l, arena, i, j - (m.destroy_radius / 3),l2)){
							Bukkit.getScheduler().runTask(m, new Runnable() {
								public void run() {
									System.out.println(b.getLocation().getY());
									if (b.getType() != Material.AIR) {
										v.playBlockBreakParticles(b.getLocation(), b.getType());
										if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
											FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
											fb.setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											fb.setVelocity(new Vector(0, 0.4, 0));
										}
										b.setType(Material.AIR);
									}
								}
							});
						}
					}else{
						final V1_7Dragon v = new V1_7Dragon();
						for(final Block b : v.getLoc(m, l, arena, i, j - (m.destroy_radius / 3),l2)){
							Bukkit.getScheduler().runTask(m, new Runnable() {
								public void run() {
									if (b.getType() != Material.AIR) {
										v.playBlockBreakParticles(b.getLocation(), b.getType());
										if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
											FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
											fb.setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											fb.setVelocity(new Vector(0, 0.4, 0));
										}
										b.setType(Material.AIR);
									}
								}
							});
						}
					}
				}else if(type.equalsIgnoreCase("wither")){
					if(mode1_6){
						final V1_6Wither v = new V1_6Wither();
						for(final Block b : v.getLoc(m, l, arena, i, j - (m.destroy_radius / 3),l2)){
							Bukkit.getScheduler().runTask(m, new Runnable() {
								public void run() {
									if (b.getType() != Material.AIR) {
										v.playBlockBreakParticles(b.getLocation(), b.getType());
										if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
											FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
											fb.setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											fb.setVelocity(new Vector(0, 0.4, 0));
										}
										b.setType(Material.AIR);
									}
								}
							});
						}
					}else if(mode1_7_5){
						final V1_7_5Wither v = new V1_7_5Wither();
						for(final Block b : v.getLoc(m, l, arena, i, j - (m.destroy_radius / 3),l2)){
							Bukkit.getScheduler().runTask(m, new Runnable() {
								public void run() {
									if (b.getType() != Material.AIR) {
										v.playBlockBreakParticles(b.getLocation(), b.getType());
										if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
											FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
											fb.setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											fb.setVelocity(new Vector(0, 0.4, 0));
										}
										b.setType(Material.AIR);
									}
								}
							});
						}
					}else{
						final V1_7Wither v = new V1_7Wither();
						for(final Block b : v.getLoc(m, l, arena, i, j - (m.destroy_radius / 3),l2)){
							Bukkit.getScheduler().runTask(m, new Runnable() {
								public void run() {
									if (b.getType() != Material.AIR) {
										v.playBlockBreakParticles(b.getLocation(), b.getType());
										if(b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks){
											FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
											fb.setMetadata("vortex", new FixedMetadataValue(m, "protected"));
											fb.setVelocity(new Vector(0, 0.4, 0));
										}
										b.setType(Material.AIR);
									}
								}
							});
						}
					}
				}
			}
		}
		
		
	}
}
