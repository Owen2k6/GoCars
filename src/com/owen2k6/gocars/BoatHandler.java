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
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BoatHandler {
    public final Boat boat;
    private Vector previousMotion;
    private boolean wasMovingLastTick;
    public Calendar cal;
    private int mode = 0;
    private int entityID = 0;
    public long delay = 0L;
    public boolean isAttacking = false;
    private final double maxMomentum = 10.0;
    private final double rotationMultipler = 0.0;
    private final double flyingMult = 1.0;
    private double throttle = 1.0;
    public double fromYaw = 0.0;
    public double toYaw = 0.0;
    private final boolean firstRun = true;
    private final double DOWNWARD_DRIFT = -0.037999998673796664;
    private final double COMPENSATION = 0.038;
    private final double MAX_BUOYANCY = 0.1;
    private final String ownerUsername;

    public BoatHandler(Boat newBoat, int newMode, int ID, String ownerUsername) {
        this.boat = newBoat;
        this.mode = newMode;
        this.entityID = ID;
        this.cal = Calendar.getInstance();
        this.previousMotion = this.boat.getVelocity().clone();
        Vector previousLocation = this.getLocation().toVector().clone();
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
        return (double)this.boat.getLocation().getYaw();
    }

    public void setYaw(double fromYaw, double toYaw) {
        this.fromYaw = fromYaw;
        this.toYaw = toYaw;
    }

    private void setMotion(double motionX, double motionY, double motionZ) {
        Vector newVelocity = new Vector();
        newVelocity.setX(motionX);
        newVelocity.setY(motionY);
        newVelocity.setZ(motionZ);
        this.boat.setVelocity(newVelocity);
    }

    public void setMotionX(double motionX) {
        motionX = RangeHandler.range(motionX, 80.0, -80.0);
        this.setMotion(motionX, this.getMotionY(), this.getMotionZ());
    }

    public void setMotionY(double motionY) {
        motionY = RangeHandler.range(motionY, 10.0, -10.0);
        this.setMotion(this.getMotionX(), motionY, this.getMotionZ());
    }

    public void setMotionZ(double motionZ) {
        motionZ = RangeHandler.range(motionZ, 80.0, -80.0);
        this.setMotion(this.getMotionX(), this.getMotionY(), motionZ);
    }

    public double getMotionX() {
        return this.boat.getVelocity().getX();
    }

    public double getMotionY() {
        return this.boat.getVelocity().getY();
    }

    public int getX() {
        return this.boat.getLocation().getBlockX();
    }

    public int getY() {
        return this.boat.getLocation().getBlockY();
    }

    public int getZ() {
        return this.boat.getLocation().getBlockZ();
    }

    public double getMotionZ() {
        return this.boat.getVelocity().getZ();
    }

    public boolean getMovingLastTick() {
        return this.wasMovingLastTick;
    }

    public Block getBlockBeneath() {
        return this.boat.getWorld().getBlockAt(this.getX(), this.getY() - 1, this.getZ());
    }

    public int getBlockIdBeneath() {
        return this.boat.getWorld().getBlockAt(this.getX(), this.getY() - 1, this.getZ()).getTypeId();
    }

    public ItemStack getItemInHand() {
        return this.getPlayer().getItemInHand();
    }

    public int getItemInHandID() {
        return this.getPlayer().getItemInHand().getTypeId();
    }

    private int getHelmetID() {
        return this.getPlayer().getInventory().getHelmet().getTypeId();
    }

    private Player getPlayer() {
        Player p = (Player)this.boat.getPassenger();
        return p;
    }

    public int getEntityId() {
        return this.entityID;
    }

    private Location getLocation() {
        return this.boat.getLocation();
    }

    private Vector getMaxSpeedVector() {
        return new Vector(0.4, this.boat.getVelocity().getY(), 0.4);
    }

    public boolean isMoving() {
        return this.getMotionX() != 0.0 || this.getMotionY() != 0.0 || this.getMotionZ() != 0.0;
    }

    private boolean isGrounded() {
        EntityBoat be = (EntityBoat)((CraftBoat)this.boat).getHandle();
        return be.onGround;
    }

    public void stopBoat() {
        this.setMotion(0.0, 0.0, 0.0);
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

    public void resetValues() {
        boolean goingDown = false;
        boolean goingUp = false;
        this.delay = 0L;
    }

    private void setThrottle(double change) {
        this.throttle = change;
    }

    private void changeThrottle(double change) {
        this.throttle += change;
        if (this.throttle >= 5.0) {
            this.throttle = 5.0;
        } else if (this.throttle <= 0.0) {
            this.throttle = 0.0;
        }

        boolean throttleChanged = this.throttle != 1.0;

    }

    public void doRealisticFriction() {
        if (this.getPlayer() == null) {
            this.setMotion(this.getMotionX() * 0.53774, this.getMotionY(), this.getMotionZ() * 0.53774);
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
        this.throttle = 10.0;
        this.speedUpBoat(this.throttle, newvel);

        float MAX_HOVER_HEIGHT = 0.7F;
        int x = this.boat.getLocation().getBlockX();
        int y = this.boat.getLocation().getBlockY();
        int z = this.boat.getLocation().getBlockZ();
        boolean goDown = false;
        int blockY = 0;
        Block block = null;
        this.getLocation().setYaw((float)(this.getYaw() * 6.0));

        for(int i = 0; (float)i != MAX_HOVER_HEIGHT + 64.0F; ++i) {
            block = this.boat.getWorld().getBlockAt(x, y - blockY, z);
            if (block.getType() != Material.AIR) {
                if (block.getType() == Material.WATER) {
                }
                break;
            }

            ++blockY;
            if ((float)i > MAX_HOVER_HEIGHT + 1.0F) {
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

        this.previousMotion = this.boat.getVelocity();
    }

    public void movementHandler(double MotionY) {
        this.setMotionY(MotionY);
    }

    private void doNormal(Vector vel) {
        CraftEntity ce = (CraftEntity)this.boat.getPassenger();
        Vector playerVelocity = ce.getVelocity().clone();
        double playerVelocityX = playerVelocity.getX();
        double playerVelocityZ = playerVelocity.getZ();
        if ((playerVelocityX != 0.0 || playerVelocityZ != 0.0) && this.isGrounded()) {
            this.getLocation().setYaw((float)(this.getYaw() * 2.5));
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
            to.setYaw((float)(this.fromYaw * 2.8));
        } else if (this.toYaw >= this.fromYaw - 0.7 && this.toYaw <= this.fromYaw + 0.7) {
            to.setYaw((float)(this.fromYaw * 5.3));
        } else if (this.toYaw >= this.fromYaw - 3.0 && this.toYaw <= this.fromYaw + 3.0) {
            to.setYaw((float)(this.fromYaw * 3.3));
        }

    }
}
