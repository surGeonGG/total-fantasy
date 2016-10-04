package Main;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class MapCoord {

    protected String biome;

    protected float elevation, moisture, heat;

    protected boolean isRiver, isLake;

    protected MapCoord north, east, west, south;

    protected MapCoord nw, ne, se, sw;

    public int x, y;

 /*   public void removeLocalMinima() {

        if (ne != null && sw != null && nw != null && se != null) {
            if (ne.getElevation() >= elevation && sw.getElevation() >= elevation && nw.getElevation() >= elevation
                    && se.getElevation() >= elevation) {

                float target = ne.getElevation();
                if (target > se.getElevation() && se.elevation < getElevation()) target = se.getElevation();
                if (target > sw.getElevation() && sw.elevation < getElevation()) target = sw.getElevation();
                if (target > nw.getElevation() && nw.elevation < getElevation()) target = nw.getElevation();

                elevation = target;

            }
        }
    }*/


    public void createRiver(boolean continuation, ArrayList<MapCoord> riverList) {

        if ((continuation || moisture > -1) && !isRiver &&  elevation > 0) {

            riverList.add(this);

            if (ne != null && sw != null && nw != null && se != null) {

                String smallestDistance = "";

                //find which direction has lowest elevation
                MapCoord target = findTarget();

                //in local minima
                if (target.getElevation() > elevation)
                    riverList.clear();

                //in even terrain
                else if (target.getElevation() == elevation) {
                    if (smallestDistance == "")
                        smallestDistance = determineSmallestDistance();

                    if(!riverList.contains(se) || !riverList.contains(sw) || !riverList.contains(ne) || !riverList.contains(nw))
                        handleRandoms(smallestDistance, riverList);

                //in descending terrain
                } else {

                    if (target.getElevation() > 0 && !target.isRiver())
                        target.createRiver(true, riverList);

                    else
                        paintRiver(riverList);

                }
            }
        }
    }

    private void handleRandoms(String smallestDistance, ArrayList<MapCoord> riverList) {

        Random rand = new Random();
        int randInt = rand.nextInt(7);
        MapCoord target = null;

        if (smallestDistance == "se")
            target = se;
        if (smallestDistance == "sw")
            target = sw;
        if (smallestDistance == "ne")
            target = ne;
        if (smallestDistance == "nw")
            target = nw;

        /*System.out.printf("x: %d y: %d tx: %d ty: %d SD: %s type: %s \n" , x, y, target.x-x, target.y-y, smallestDistance, target);
        System.out.println(target);
        System.out.println(randInt);
        System.out.println(!riverList.contains(se) + " " +!riverList.contains(sw) + " " +!riverList.contains(ne) + " " +!riverList.contains(nw));*/

        if (randInt <= 2 && !riverList.contains(target)) {

            if (target.isRiver())
                paintRiver(riverList);

            else
                target.createRiver(true, riverList);

        }
         else if (randInt == 3 && !riverList.contains(se)) {

            if (se.isRiver())
                paintRiver(riverList);

            else
                se.createRiver(true, riverList);
        }

        else if (randInt == 4 && !riverList.contains(sw)) {

            if (sw.isRiver())
                paintRiver(riverList);

            else
                sw.createRiver(true, riverList);
        }

        else if (randInt == 5 && !riverList.contains(ne)) {

            if (ne.isRiver())
                paintRiver(riverList);

            else
                ne.createRiver(true, riverList);
        }

        else if (randInt == 6 && !riverList.contains(nw)) {

            if (nw.isRiver())
                paintRiver(riverList);

            else
                nw.createRiver(true, riverList);
        }

        else if (!riverList.contains(se) || !riverList.contains(sw) || !riverList.contains(ne) || !riverList.contains(nw))
            handleRandoms(smallestDistance, riverList);
    }



    private String determineSmallestDistance() {

        int seDistToOcean;
        int swDistToOcean;
        int neDistToOcean;
        int nwDistToOcean;
        int distance = 0;
        String smallestDistance;

        seDistToOcean = se.findDistToOcean("se", distance);
        swDistToOcean = sw.findDistToOcean("sw", distance);
        neDistToOcean = ne.findDistToOcean("ne", distance);
        nwDistToOcean = nw.findDistToOcean("nw", distance);

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

       // System.out.println(smallestDistance);

        return smallestDistance;
    }




    public int findDistToOcean(String direction, int distance) {
        if (elevation <= 0 || isRiver)
            return distance;

        else {
            distance++;
           // System.out.println(x + " " + y + " " + direction + " " + distance + " " + elevation);
            if (direction == "se")
                if (se != null)
                    return se.findDistToOcean(direction, distance);
            if (direction == "sw")
                if (sw != null)
                    return sw.findDistToOcean(direction, distance);
            if (direction == "ne")
                if (ne != null)
                    return ne.findDistToOcean(direction, distance);
            if (direction == "nw")
                if (nw != null)
                    return nw.findDistToOcean(direction, distance);
            return 10000;
        }
    }



    private MapCoord findTarget() {
        MapCoord target = nw;

        if (target.getElevation() > se.getElevation() && !se.isRiver())
            target = se;
        if (target.getElevation() > ne.getElevation() && !ne.isRiver())
            target = ne;
        if (target.getElevation() > sw.getElevation() && !sw.isRiver())
            target = sw;

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

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
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

    public void setNorth(MapCoord north) {
        this.north = north;
    }

    public void setEast(MapCoord east) {
        this.east = east;
    }

    public void setWest(MapCoord west) {
        this.west = west;
    }

    public void setSouth(MapCoord south) {
        this.south = south;
    }

    public void setNw(MapCoord nw) {
        this.nw = nw;
    }

    public void setNe(MapCoord ne) {
        this.ne = ne;
    }

    public void setSe(MapCoord se) {
        this.se = se;
    }

    public void setSw(MapCoord sw) {
        this.sw = sw;
    }
}
