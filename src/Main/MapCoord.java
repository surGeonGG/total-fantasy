package Main;

import java.util.ArrayList;
import java.util.Random;

public class MapCoord {

    public String biome;
    public float smoothElevation, moisture, heat, biomeElevation;
    public boolean isRiver, isLake, isMountainTop, isMountainSide, isOcean;
    public MapCoord[] neighbours = new MapCoord[6];
    public static final int NW = 0, NE = 1, SE = 2, SW = 3, E = 4, W = 5;
    public int x, y;

    public boolean createRiver(boolean continuation, ArrayList<MapCoord> riverList) {
        if ((continuation || moisture > 0.2f) && !isRiver && !isOcean) {
            if (neighbours[NE] == null && neighbours[SW] == null && neighbours[NW] == null && neighbours[SE] == null) return false;

            riverList.add(this);
            String smallestDistance = "";
            //find which direction has lowest smoothElevation
            MapCoord target = findTarget();
            //checks if in even terrain or local min
            if (target.getSmoothElevation() >= smoothElevation) {
                if (smallestDistance == "")
                    smallestDistance = determineSmallestDistance();
                if (riverList.contains(neighbours[SE]) && riverList.contains(neighbours[NE])
                        && riverList.contains(neighbours[NW]) && riverList.contains(neighbours[SW]))
                    return false;
                target = handleRandoms(smallestDistance, riverList);
                if (target.isOcean || (target.isRiver && !riverList.contains(target)))
                    paintRiver(riverList);
                else target.createRiver(true, riverList);
            } else {
                if (target.isOcean || (target.isRiver && !riverList.contains(target)))
                    paintRiver(riverList);
                else target.createRiver(true, riverList);
            }
        }
        return true;
    }

    private MapCoord handleRandoms(String smallestDistance, ArrayList<MapCoord> riverList) {
        Random rand = new Random();
        int randInt = rand.nextInt(7);
        MapCoord target = null;
        if (smallestDistance == "se")
            target = neighbours[SE];
        if (smallestDistance == "sw")
            target = neighbours[SW];
        if (smallestDistance == "ne")
            target = neighbours[NE];
        if (smallestDistance == "nw")
            target = neighbours[NW];
        if (randInt <= 3  && !riverList.contains(neighbours[randInt])) {
            target = neighbours[randInt];
        }
        return target;
    }



    private String determineSmallestDistance() {
        int seDistToOcean;
        int swDistToOcean;
        int neDistToOcean;
        int nwDistToOcean;
        int distance = 0;
        String smallestDistance;
        seDistToOcean = neighbours[SE].findDistToOcean("se", distance);
        swDistToOcean = neighbours[SW].findDistToOcean("sw", distance);
        neDistToOcean = neighbours[NE].findDistToOcean("ne", distance);
        nwDistToOcean = neighbours[NW].findDistToOcean("nw", distance);
        smallestDistance = "se";
        distance = seDistToOcean;
        if (neDistToOcean < distance) {
            smallestDistance = "ne";
            distance = neDistToOcean;
        }
        if (nwDistToOcean < distance) {
            smallestDistance = "nw";
            distance = nwDistToOcean;
        }
        if (swDistToOcean < distance) {
            smallestDistance = "sw";
        }
        return smallestDistance;
    }

    public int findDistToOcean(String direction, int distance) {
        if (isOcean || isRiver)
            return distance;
        else {
            distance++;
           // System.out.println(x + " " + y + " " + direction + " " + distance + " " + smoothElevation);
            if (direction == "se")
                if (neighbours[SE] != null)
                    return neighbours[SE].findDistToOcean(direction, distance);
            if (direction == "sw")
                if (neighbours[SW] != null)
                    return neighbours[SW].findDistToOcean(direction, distance);
            if (direction == "ne")
                if (neighbours[NE] != null)
                    return neighbours[NE].findDistToOcean(direction, distance);
            if (direction == "nw")
                if (neighbours[NW] != null)
                    return neighbours[NW].findDistToOcean(direction, distance);
            return 10000;
        }
    }

    private MapCoord findTarget() {
        MapCoord target = neighbours[NW];
        if (target.getSmoothElevation() > neighbours[SE].getSmoothElevation() && !neighbours[SE].isRiver())
            target = neighbours[SE];
        if (target.getSmoothElevation() > neighbours[NE].getSmoothElevation() && !neighbours[NE].isRiver())
            target = neighbours[NE];
        if (target.getSmoothElevation() > neighbours[SW].getSmoothElevation() && !neighbours[SW].isRiver())
            target = neighbours[SW];
        return target;
    }

    private void paintRiver(ArrayList<MapCoord> riverList) {
        for (MapCoord riverSegment : riverList) {
            riverSegment.setRiver();
        }
    }

    public String getBiome() {
        return biome;
    }

    public void setBiome(String biome) {
        this.biome = biome;
    }

    public float getSmoothElevation() {
        return smoothElevation;
    }

    public void setSmoothElevation(float smoothElevation) {
        this.smoothElevation = smoothElevation;
    }

    public float getBiomeElevation() {
        return biomeElevation;
    }

    public void setBiomeElevation(float biomeElevation) {
        this.biomeElevation = biomeElevation;
    }

    public float getMoisture() {
        return moisture;
    }

    public void setMoisture(float moisture) { this.moisture = moisture; }

    public float getHeat() { return heat; }

    public void setHeat(float heat) { this.heat = heat; }

    public void setRiver() {
        isRiver = true;
    }

    public void setLake() {
        isLake = true;
    }

    public boolean isRiver() {

        return isRiver;
    }

    public boolean isLake() {

        return isLake;
    }

    public void setEast(MapCoord e) {
        this.neighbours[E] = e;
    }

    public void setWest(MapCoord w) {
        this.neighbours[W] = w;
    }

    public void setNw(MapCoord nw) {
        this.neighbours[NW] = nw;
    }

    public void setNe(MapCoord ne) {
        this.neighbours[NE] = ne;
    }

    public void setSe(MapCoord se) {
        this.neighbours[SE] = se;
    }

    public void setSw(MapCoord sw) {
        this.neighbours[SW] = sw;
    }
}
