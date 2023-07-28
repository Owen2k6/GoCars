//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.server.EntityBoat;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BoatHandler {
    public final Boat boat;
    private Vector previousLocation;
    private Vector previousMotion;
    private boolean wasMovingLastTick;
    public Calendar cal;
    private int mode = 0;
    private int entityID = 0;
    public long delay = 0L;
    public boolean isAttacking = false;
    private boolean throttleChanged = false;
    private final double maxMomentum = 10.0;
    private double rotationMultipler = 0.0;
    private final double flyingMult = 1.0;
    private double throttle = 1.0;
    public double fromYaw = 0.0;
    public double toYaw = 0.0;
    private float hoverHeight = 0;
    private boolean goingDown = false;
    private boolean goingUp = false;
    private boolean firstRun = true;
    private final double DOWNWARD_DRIFT = -0.037999998673796664;
    private final double COMPENSATION = 0.038;
    private final double MAX_BUOYANCY = 0.1;
    private float MAX_HOVER_HEIGHT = 1;

    public BoatHandler(Boat newBoat, int newMode, int ID) {
        this.boat = newBoat;
        this.mode = newMode;
        this.entityID = ID;
        this.cal = Calendar.getInstance();
        this.previousMotion = this.boat.getVelocity().clone();
        this.previousLocation = this.getLocation().toVector().clone();
        if (this.isMoving()) {
            this.wasMovingLastTick = true;
        }

        GoCars.boats.put(this.boat.getEntityId(), this);
    }

    private double getYaw() {
        return (double) this.boat.getLocation().getYaw();
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
        Player p = (Player) this.boat.getPassenger();
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
        EntityBoat be = (EntityBoat) ((CraftBoat) this.boat).getHandle();
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
        this.goingDown = false;
        this.goingUp = false;
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

        if (this.throttle != 1.0) {
            this.throttleChanged = true;
        } else {
            this.throttleChanged = false;
        }

    }

    public void doRealisticFriction() {
        if (this.getPlayer() == null) {
            this.setMotion(this.getMotionX() * 0.53774, this.getMotionY(), this.getMotionZ() * 0.53774);
        }

    }

    public void speedUpBoat(double factor, Vector vel) {
        double curX = vel.getX();
        double curZ = vel.getZ();
        double newX = curX * factor;
        double newZ;
        if (Math.abs(newX) > 0.4) {
            if (newX < 0.0) {
                newX = -0.4;
            } else {
                newX = 0.4;
            }

            newZ = 0.0;
            if (curZ != 0.0) {
                newZ = 0.4 / Math.abs(curX / curZ);
                if (curZ < 0.0) {
                    newZ *= -1.0;
                }
            }

            this.setMotion(newX, vel.getY(), newZ);
        } else {
            newZ = curZ * factor;
            if (Math.abs(newZ) > 0.4) {
                if (newZ < 0.0) {
                    newZ = -0.4;
                } else {
                    newZ = 0.4;
                }

                newX = 0.0;
                if (curX != 0.0) {
                    newX = 0.4 / (curZ / curX);
                    if (curX < 0.0) {
                        newX *= -1.0;
                    }
                }

                this.setMotion(newX, vel.getY(), newZ);
            } else {
                this.setMotion(newX, vel.getY(), newZ);
            }
        }
    }

    public void movementHandler(Vector vel) {
        Player p = this.getPlayer();
        Vector newvel = this.boat.getVelocity();
        if (this.throttle != 1.0) {
            this.speedUpBoat(this.throttle, newvel);
        }

        if (this.mode == 0) {
            this.doNormal(vel);
        } else if (this.mode == 1) {
            this.doFlying(vel);
        } else if (this.mode == 2) {
            this.doUnderwater(vel);
        } else if (this.mode == 3) {
            this.doHover(vel);
        } else if (this.mode == 4) {
            this.doGlider(vel);
        }

        this.previousMotion = this.boat.getVelocity();
    }

    public void movementHandler(double MotionY) {
        this.setMotionY(MotionY);
    }

    public void doArmSwing() {
        if (this.isAttacking) {
            boolean var1 = false;
        } else if (this.mode != 4 && this.getItemInHandID() == 280) {
            this.changeThrottle(0.25);
            this.getPlayer().sendMessage(ChatColor.GREEN + "You accelerate the car, Your speed is now " + this.throttle + "x.");
            this.isAttacking = false;
        } else if (this.mode != 4 && this.getItemInHandID() == 336){
            this.setThrottle(0.0);
            this.getPlayer().sendMessage(ChatColor.DARK_RED + "You bloody idiot. You just slammed the brakes. You could've caused a massive crash!");
            this.isAttacking = false;
        }
    }

    public void doRightClick() {
        if (this.getItemInHandID() == 280) {
            this.changeThrottle(-0.25);
            this.getPlayer().sendMessage(ChatColor.GOLD + "You decelerate. Your speed is now " + this.throttle + "x");
        } else if (this.getItemInHandID() == 336) {
            this.setThrottle(0.0);
            this.getPlayer().sendMessage(ChatColor.DARK_RED + "You bloody idiot. You just slammed the brakes. You could've caused a massive crash!");
        }

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
        boolean boostSteering = false;
        if (playerVelocityX < 0.0 && currentX > 0.0 || playerVelocityX > 0.0 && currentX < 0.0) {
            boostSteering = true;
        }

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

    private void doGlider(Vector vel) {
        CraftEntity ce = (CraftEntity) this.boat.getPassenger();
        if (this.getBlockBeneath().getType() == Material.AIR) {
            vel.setY(-0.075);
        }

        if (vel.getY() < -0.075) {
            vel.setY(-0.075);
        }

        this.setMotion(vel.getX(), vel.getY(), vel.getZ());
    }

    private void doFlying(Vector vel) {
        if (this.goingUp) {
            if (vel.getY() <= 0.0) {
                this.goingUp = false;
                vel.setY(0.0);
            }

            this.setMotion(vel.getX(), vel.getY(), vel.getZ());
        } else if (this.goingDown) {
            if (vel.getY() <= 0.0) {
                vel.setY(vel.getY() + 0.25);
                if (vel.getY() >= 0.0) {
                    this.goingDown = false;
                }
            }

            this.setMotion(vel.getX(), vel.getY(), vel.getZ());
        } else if (vel.getY() <= 0.0) {
            if (this.boat.getVelocity().getY() <= 0.0 && this.boat.getVelocity().getY() >= -0.037999998673796664) {
                vel.setY(0.038);
            } else {
                vel.setY(0.0);
            }

            this.setMotion(vel.getX(), vel.getY(), vel.getZ());
        } else {
            if (this.boat.getVelocity().getY() <= 0.0 && this.boat.getVelocity().getY() >= -0.037999998673796664) {
                vel.setY(0.038);
            } else {
                vel.setY(0.0);
            }

            this.setMotion(vel.getX(), vel.getY(), vel.getZ());
        }
    }

    private void doUnderwater(Vector vel) {
        Player p = this.getPlayer();
        if (!this.goingUp) {
            vel.setY(vel.getY() - 0.03);
        } else {
            vel.setY(vel.getY() - 0.03);
        }

        if (vel.getY() > 0.1) {
            vel.setY(0.1);
        }

        if (!this.goingUp && vel.getY() > 0.0) {
            vel.setY(-0.15);
        }

        this.getLocation().setYaw((float) (this.getYaw() * 2.0));
        if (p.getRemainingAir() != p.getMaximumAir() && (Permission.genericCheck(p, "GoCars.player.air") || GoCars.helmets.contains("" + this.getHelmetID()) && Permission.genericCheck(p, "GoCars.items.helmets"))) {
            p.setRemainingAir(p.getMaximumAir());
            p.setMaximumAir(p.getMaximumAir());
        }

        if (this.goingUp) {
            vel.setY(vel.getY() - 0.009);
            if (vel.getY() <= 0.025) {
                this.goingUp = false;
                vel.setY(0.0);
            }

            this.setMotion(vel.getX(), vel.getY(), vel.getZ());
        } else if (this.goingDown) {
            if (vel.getY() <= -0.6) {
                vel.setY(-0.6);
                if (vel.getY() >= 0.0) {
                    this.goingDown = false;
                }
            }

            this.setMotion(vel.getX(), vel.getY(), vel.getZ());
        } else {
            this.setMotion(vel.getX(), vel.getY(), vel.getZ());
        }
    }

    private void doHover(Vector vel) {
        if (this.getItemInHandID() == 263 && Permission.genericCheck(this.getPlayer(), "GoCars.items.coal")) {
            this.MAX_HOVER_HEIGHT = 0.5F;
        } else {
            this.MAX_HOVER_HEIGHT = 0.5F;
        }

        int x = this.boat.getLocation().getBlockX();
        int y = this.boat.getLocation().getBlockY();
        int z = this.boat.getLocation().getBlockZ();
        boolean goDown = false;
        int blockY = 0;
        Block block = null;
        this.getLocation().setYaw((float) (this.getYaw() * 6.0));

        for (int i = 0; i != this.MAX_HOVER_HEIGHT + 64; ++i) {
            block = this.boat.getWorld().getBlockAt(x, y - blockY, z);
            if (block.getType() != Material.AIR) {
                if (block.getType() == Material.WATER) {
                }
                break;
            }

            ++blockY;
            if (i > this.MAX_HOVER_HEIGHT + 1) {
                goDown = true;
            }
        }

        this.hoverHeight = block.getY() + this.MAX_HOVER_HEIGHT * 2;
        if (this.boat.getLocation().getY() < (double) this.hoverHeight + 0.6) {
            this.setMotionY(0.35);
        } else if (goDown && this.boat.getLocation().getY() > (double) this.hoverHeight + 0.6) {
            this.setMotionY(-0.25);
        } else {
            this.setMotionY(0.0);
        }
    }

    private void doDrill() {
        for (int x = -2; x <= 2; ++x) {
            for (int z = -2; z <= 2; ++z) {
                for (int y = 4; y >= 1; --y) {
                    Block block = this.boat.getWorld().getBlockAt(this.boat.getLocation().getBlockX() - x, this.boat.getLocation().getBlockY() - y, this.boat.getLocation().getBlockZ() - z);
                    if (!block.getType().equals(Material.AIR) && block.getTypeId() != 7 && block.getTypeId() != 8 && block.getTypeId() != 9 && block.getTypeId() != 10 && block.getTypeId() != 11) {
                        Material mat = block.getType();
                        block.setType(Material.AIR);
                        this.boat.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(mat, 1));
                    }
                }
            }
        }

    }

    public void doYaw(Location from, Location to) {
        this.fromYaw = (double) from.getYaw();
        this.toYaw = (double) to.getYaw();
        if (this.toYaw >= this.fromYaw - 0.025 && this.toYaw <= this.fromYaw + 0.025) {
            to.setYaw((float) (this.fromYaw * 2.8));
        } else if (this.toYaw >= this.fromYaw - 0.7 && this.toYaw <= this.fromYaw + 0.7) {
            to.setYaw((float) (this.fromYaw * 5.3));
        } else if (this.toYaw >= this.fromYaw - 3.0 && this.toYaw <= this.fromYaw + 3.0) {
            to.setYaw((float) (this.fromYaw * 3.3));
        }

    }

    class DropTNT extends TimerTask {
        private Item i;

        public DropTNT(Item i) {
            this.i = i;
        }

        public void run() {
            //We. Don't. Need. This.

        }
    }
}
