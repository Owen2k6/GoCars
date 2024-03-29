//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

import java.util.Calendar;

import net.minecraft.server.EntityBoat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BoatHandler {
    public final Boat boat;
    private boolean wasMovingLastTick;
    public Calendar cal;
    private int mode = 0;
    private int entityID = 0;
    public long delay = 0L;
    private double throttle = 1.0;
    public double fromYaw = 0.0;
    public double toYaw = 0.0;
    private final String ownerUsername;

    public BoatHandler(Boat newBoat, int newMode, int ID, String ownerUsername) {
        this.boat = newBoat;
        this.mode = newMode;
        this.entityID = ID;
        this.cal = Calendar.getInstance();
        this.ownerUsername = ownerUsername;
        if (this.isMoving()) {
            this.wasMovingLastTick = true;
        }

        GoCars.boats.put(this.boat.getEntityId(), this);
    }

    public String getOwnerUsername() {
        return this.ownerUsername;
    }

    public boolean isOwner(Player player) {
        return player != null && player.getName().equals(this.ownerUsername);
    }

    private double getYaw() {
        return (double) this.boat.getLocation().getYaw();
    }

    private void setMotion(double motionX, double motionY, double motionZ) {
        Vector newVelocity = new Vector();
        newVelocity.setX(motionX);
        newVelocity.setY(motionY);
        newVelocity.setZ(motionZ);
        this.boat.setVelocity(newVelocity);
    }

    public void setMotionY(double motionY) {
        motionY = RangeHandler.range(motionY, 10.0, -10.0);
        this.setMotion(this.getMotionX(), motionY, this.getMotionZ());
    }

    public double getMotionX() {
        return this.boat.getVelocity().getX();
    }

    public double getMotionY() {
        return this.boat.getVelocity().getY();
    }

    public double getMotionZ() {
        return this.boat.getVelocity().getZ();
    }

    public boolean getMovingLastTick() {
        return this.wasMovingLastTick;
    }

    private Player getPlayer() {
        Player p = (Player) this.boat.getPassenger();
        return p;
    }

    public int getEntityId() {
        return this.entityID;
    }

    private Location getLocation() {
        return this.boat.getLocation();
    }

    public boolean isMoving() {
        return this.getMotionX() != 0.0 || this.getMotionY() != 0.0 || this.getMotionZ() != 0.0;
    }

    private boolean isGrounded() {
        EntityBoat be = (EntityBoat) ((CraftBoat) this.boat).getHandle();
        return be.onGround;
    }

    public void setMode(int newMode) {
        this.mode = newMode;
    }

    public int getMode() {
        return this.mode;
    }

    public void updateCalendar() {
        Calendar current = Calendar.getInstance();
        if (this.cal.get(13) != current.get(13)) {
            this.cal = current;
        }

    }

    public void speedUpBoat(double factor, Vector vel) {
        double curX = vel.getX();
        double curZ = vel.getZ();
        double var10000 = curX * factor;
    }

    public void movementHandler(Vector vel) {
        Player p = this.getPlayer();
        Vector newvel = this.boat.getVelocity();
        this.throttle = 15.0;
        this.speedUpBoat(this.throttle, newvel);
        this.boat.setMaxSpeed(100000000);

        float MAX_HOVER_HEIGHT = 0.7F;
        int x = this.boat.getLocation().getBlockX();
        int y = this.boat.getLocation().getBlockY();
        int z = this.boat.getLocation().getBlockZ();
        boolean goDown = false;
        int blockY = 0;
        Block block = null;
        this.getLocation().setYaw((float) (this.getYaw() * 6.0));

        for (int i = 0; (float) i != MAX_HOVER_HEIGHT + 64.0F; ++i) {
            block = this.boat.getWorld().getBlockAt(x, y - blockY, z);
            if (block.getType() != Material.AIR) {
                if (block.getType() == Material.WATER) {
                }
                break;
            }

            ++blockY;
            if ((float) i > MAX_HOVER_HEIGHT + 1.0F) {
                goDown = true;
            }
        }

        float hoverHeight = (float) block.getY() + MAX_HOVER_HEIGHT * 2.0F;
        if (this.boat.getLocation().getY() < (double) hoverHeight + 0.6) {
            this.setMotionY(0.35);
        } else if (goDown && this.boat.getLocation().getY() > (double) hoverHeight + 0.6) {
            this.setMotionY(-0.25);
        } else {
            this.setMotionY(0.0);
        }
    }

    public void movementHandler(double MotionY) {
        this.setMotionY(MotionY);
    }

    private void doNormal(Vector vel) {
        CraftEntity ce = (CraftEntity) this.boat.getPassenger();
        Vector playerVelocity = ce.getVelocity().clone();
        double playerVelocityX = playerVelocity.getX();
        double playerVelocityZ = playerVelocity.getZ();
        if ((playerVelocityX != 0.0 || playerVelocityZ != 0.0) && this.isGrounded()) {
            this.getLocation().setYaw((float) (this.getYaw() * 2.5));
            this.speedUpBoat(10.0, this.boat.getVelocity());
        }

        double currentX = vel.getX();
        double currentZ = vel.getZ();
        boolean boostSteering = playerVelocityX < 0.0 && currentX > 0.0 || playerVelocityX > 0.0 && currentX < 0.0;

        if (!boostSteering && playerVelocityZ < 0.0 && currentZ > 0.0 || playerVelocityZ > 0.0 && currentZ < 0.0) {
            boostSteering = true;
        }

        if (boostSteering) {
            currentX = currentX / 1.2 + playerVelocityX;
            currentZ = currentZ / 1.2 + playerVelocityZ;
            this.setMotion(currentX, vel.getY(), currentZ);
        }

        if (this.cal.getTimeInMillis() >= this.delay + 3000L) {
            this.delay = 0L;
        }

    }

    public void doYaw(Location from, Location to) {
        this.fromYaw = from.getYaw();
        this.toYaw = to.getYaw();
        if (this.toYaw >= this.fromYaw - 0.025 && this.toYaw <= this.fromYaw + 0.025) {
            to.setYaw((float) (this.fromYaw * 2.8));
        } else if (this.toYaw >= this.fromYaw - 0.7 && this.toYaw <= this.fromYaw + 0.7) {
            to.setYaw((float) (this.fromYaw * 5.3));
        } else if (this.toYaw >= this.fromYaw - 3.0 && this.toYaw <= this.fromYaw + 3.0) {
            to.setYaw((float) (this.fromYaw * 3.3));
        }

    }
}
