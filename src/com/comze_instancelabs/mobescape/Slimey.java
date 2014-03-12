package com.comze_instancelabs.mobescape;

import java.util.ArrayList;

import net.minecraft.server.v1_7_R1.DamageSource;
import net.minecraft.server.v1_7_R1.EntityBat;
import net.minecraft.server.v1_7_R1.EntityComplexPart;
import net.minecraft.server.v1_7_R1.EntityEnderDragon;
import net.minecraft.server.v1_7_R1.EntitySlime;
import net.minecraft.server.v1_7_R1.ItemStack;
import net.minecraft.server.v1_7_R1.MathHelper;
import net.minecraft.server.v1_7_R1.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Slimey extends EntitySlime {

	private boolean onGround = false;
	private ArrayList<Vector> points = new ArrayList();
	private int currentid;
	private double X;
	private double Y;
	private double Z;
	private Main m;
	private String arena;
	
	public Slimey(Main m, Location loc, World world) {
		super(world);
		this.m = m;
		setPosition(loc.getX(), loc.getY(), loc.getZ());
		yaw = loc.getYaw() + 180;
		while (yaw > 360) {
			yaw -= 360;
		}
		while (yaw < 0) {
			yaw += 360;
		}
		if (yaw < 45 || yaw > 315) {
			yaw = 0F;
		} else if (yaw < 135) {
			yaw = 90F;
		} else if (yaw < 225) {
			yaw = 180F;
		} else {
			yaw = 270F;
		}
	}

	@Override
	public void e() {
		return;
	}

	public boolean damageEntity(DamageSource damagesource, int i) {
		return false;
	}

	@Override
	public int getExpReward() {
		return 0;
	}

	public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
		return false;
	}

}