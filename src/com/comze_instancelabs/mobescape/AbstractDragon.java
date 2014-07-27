package com.comze_instancelabs.mobescape;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public interface AbstractDragon {

	public BukkitTask start(final Main m, final String arena);

	public void removeEnderdragon(String arena);

	public void stop(final Main m, BukkitTask t, final String arena);

	public void destroy(final Main m, final Location l, final Location l2, String arena, int length2);

	public Block[] getLoc(Main m, final Location l, String arena, int i, int j, Location l2);

	public void playBlockBreakParticles(final Location loc, final Material m, final Player... players);
	
}
