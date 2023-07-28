//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Directions {
    public Directions() {
    }

    public static enum CompassDirection {
        NO_DIRECTION(-1),
        NORTH(0),
        NORTH_EAST(1),
        EAST(2),
        SOUTH_EAST(3),
        SOUTH(4),
        SOUTH_WEST(5),
        WEST(6),
        NORTH_WEST(7);

        private int id;
        private static Map<Integer, CompassDirection> map;

        private CompassDirection(int id) {
            this.id = id;
            add(id, this);
        }

        private static void add(int type, CompassDirection name) {
            if (map == null) {
                map = new HashMap();
            }

            map.put(type, name);
        }

        private static boolean isFacingNorth(double degrees, double leeway) {
            return 0.0 <= degrees && degrees < 45.0 + leeway || 315.0 - leeway <= degrees && degrees <= 360.0;
        }

        private static boolean isFacingEast(double degrees, double leeway) {
            return 45.0 - leeway <= degrees && degrees < 135.0 + leeway;
        }

        private static boolean isFacingSouth(double degrees, double leeway) {
            return 135.0 - leeway <= degrees && degrees < 225.0 + leeway;
        }

        private static boolean isFacingWest(double degrees, double leeway) {
            return 225.0 - leeway <= degrees && degrees < 315.0 + leeway;
        }

        public static CompassDirection getDirectionFromMinecartRotation(double degrees) {
            while(degrees < 0.0) {
                degrees += 360.0;
            }

            while(degrees > 360.0) {
                degrees -= 360.0;
            }

            CompassDirection direction = getDirectionFromRotation(degrees);
            double leeway = 15.0;
            if (!direction.equals(NORTH) && !direction.equals(SOUTH)) {
                if (direction.equals(EAST) || direction.equals(WEST)) {
                    if (isFacingNorth(degrees, leeway)) {
                        return NORTH;
                    }

                    if (isFacingSouth(degrees, leeway)) {
                        return SOUTH;
                    }
                }
            } else {
                if (isFacingEast(degrees, leeway)) {
                    return EAST;
                }

                if (isFacingWest(degrees, leeway)) {
                    return WEST;
                }
            }

            return direction;
        }

        public static Block getBlockTypeAhead(World w, CompassDirection efacingDir, int x, int y, int z, int below) {
            if (efacingDir == NORTH) {
                return w.getBlockAt(x - 1, y - below, z);
            } else if (efacingDir == EAST) {
                return w.getBlockAt(x, y - below, z - 1);
            } else if (efacingDir == SOUTH) {
                return w.getBlockAt(x + 1, y - below, z);
            } else {
                return efacingDir == WEST ? w.getBlockAt(x, y - below, z + 1) : null;
            }
        }

        public static CompassDirection getDirectionFromRotation(double degrees) {
            while(degrees < 0.0) {
                degrees += 360.0;
            }

            while(degrees > 360.0) {
                degrees -= 360.0;
            }

            if (isFacingNorth(degrees, 0.0)) {
                return NORTH;
            } else if (isFacingEast(degrees, 0.0)) {
                return EAST;
            } else if (isFacingSouth(degrees, 0.0)) {
                return SOUTH;
            } else if (isFacingWest(degrees, 0.0)) {
                return WEST;
            } else {
                return NO_DIRECTION;
            }
        }
    }
}
