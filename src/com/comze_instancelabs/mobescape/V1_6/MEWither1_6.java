package com.comze_instancelabs.mobescape.V1_6;

import java.util.ArrayList;

import net.minecraft.server.v1_6_R3.DamageSource;
import net.minecraft.server.v1_6_R3.EntityComplexPart;
import net.minecraft.server.v1_6_R3.EntityWither;
import net.minecraft.server.v1_6_R3.World;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mobescape.Main;

public class MEWither1_6 extends EntityWither {

	private boolean onGround = false;
	private ArrayList<Vector> points = new ArrayList();
	private int currentid;
	private double X;
	private double Y;
	private double Z;
	private Main m;
	private String arena;
	
	public MEWither1_6(Main m, String arena, Location loc, World world, ArrayList<Vector> p) {
		super(world);
		this.m = m;
		this.arena = arena;
		currentid = 0;
		this.points = p;
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
		
		double disX = (this.locX - points.get(currentid).getX());
		double disY = (this.locY - points.get(currentid).getY());
		double disZ = (this.locZ - points.get(currentid).getZ());

		double tick = Math.sqrt(disX * disX + disY * disY + disZ * disZ) * 2 / m.mob_speed * Math.pow(0.98, currentid);

		this.X = (Math.abs(disX) / tick);
		this.Y = (Math.abs(disY) / tick);
		this.Z = (Math.abs(disZ) / tick);
	}

	@Override
	public void c() {
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

	public Vector getNextPosition() {
		
		double tempx = this.locX;
		double tempy = this.locY;
		double tempz = this.locZ;


		if (tempx < points.get(currentid).getX())
			tempx += this.X;
		else {
			tempx -= this.X;
		}

		if ((int) tempy < points.get(currentid).getY()) {
			tempy += this.Y;
		} else {
			tempy -= this.Y;
		}

		if (tempz < points.get(currentid).getZ())
			tempz += this.Z;
		else {
			tempz -= this.Z;
		}


		if (((Math.abs((int) tempx - points.get(currentid).getX()) == 0) && (Math.abs((int) tempz - points.get(currentid).getZ()) <= 3)) || ((Math.abs((int) tempz - points.get(currentid).getZ()) == 0) && (Math.abs((int) tempx - points.get(currentid).getX()) <= 3) && (Math.abs((int) tempy - points.get(currentid).getY()) <= 5))) {
			if (currentid < points.size() - 1) {
				currentid += 1;
			} else {
				// finish
				m.stop(m.h.get(arena), arena);
			}

			double disX = (this.locX - points.get(currentid).getX());
			double disY = (this.locY - points.get(currentid).getY());
			double disZ = (this.locZ - points.get(currentid).getZ());
			
			double tick_ = Math.sqrt(disX * disX + disY * disY + disZ * disZ) * 2 / m.mob_speed * Math.pow(0.98, currentid);

			this.X = (Math.abs(disX) / tick_);
			this.Y = (Math.abs(disY) / tick_);
			this.Z = (Math.abs(disZ) / tick_);
			
			if (this.locX < points.get(currentid).getX()) {
				if (this.locZ > points.get(currentid).getZ()) {
					this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z));
				} else {
					this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) - 270F;
				}
			} else { // (this.locX > points.get(currentid).getX())
				if (this.locZ > points.get(currentid).getZ()) {
					this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) - 90F;
				} else {
					this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) - 180F;
				}
			}

		}

		if (this.locX < points.get(currentid).getX()) {
			if (this.locZ > points.get(currentid).getZ()) {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z));
			} else {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) + 90F;
			}
		} else { // (this.locX > points.get(currentid).getX())
			if (this.locZ > points.get(currentid).getZ()) {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) - 90F;
			} else {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) - 180F;
			}
		}
		
		return new Vector(tempx, tempy, tempz);
	}
	
	
	public static float getLookAtYaw(Vector motion) {
        double dx = motion.getX();
        double dz = motion.getZ();
        double yaw = 0;

        if (dx != 0) {
            if (dx < 0) {
                yaw = 1.5 * Math.PI;
            } else {
                yaw = 0.5 * Math.PI;
            }
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = Math.PI;
        }
        return (float) (-yaw * 180 / Math.PI - 90);
    }

}