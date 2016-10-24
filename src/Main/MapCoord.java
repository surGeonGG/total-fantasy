package Main;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class MapCoord {

    public String biome;

    public float elevation, moisture, heat, biomeElevation;

    public boolean isRiver, isLake, isMountainTop, isMountainSide;

    public MapCoord e, w, nw, ne, se, sw;

    public int x, y;


    public boolean createRiver(boolean continuation, ArrayList<MapCoord> riverList) {

        if ((continuation || moisture > 0.3f) && !isRiver &&  elevation >= BiomeGenerator.OCEAN_LEVEL) {

            if (ne == null && sw == null && nw == null && se == null) return false;

            riverList.add(this);



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

                if (target.getElevation() >= BiomeGenerator.OCEAN_LEVEL && !target.isRiver())
                    target.createRiver(true, riverList);

                else
                    paintRiver(riverList);


            }
        }
        return true;
    }




    private void handleRandoms(String smallestDistance, ArrayList<MapCoord> riverList) {

        Random rand = new Random();
        int randInt = rand.nextInt(7);
        //int randInt = ;
        MapCoord target = null;

        if (smallestDistance == "se")
            target = se;
        if (smallestDistance == "sw")
            target = sw;
        if (smallestDistance == "ne")
            target = ne;
        if (smallestDistance == "nw")
            target = nw;

        if (randInt == 3 && !riverList.contains(se)) {
            target = se;
        }

        else if (randInt == 4 && !riverList.contains(sw)) {
            target = sw;
        }

        else if (randInt == 5 && !riverList.contains(ne)) {
            target = ne;
        }

        else if (randInt == 6 && !riverList.contains(nw)) {
            target = nw;
        }

        if (target.isRiver())
            paintRiver(riverList);

        else
            target.createRiver(true, riverList);

        /*else if (!riverList.contains(se) || !riverList.contains(sw) || !riverList.contains(ne) || !riverList.contains(nw))
            handleRandoms(smallestDistance, riverList);*/
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

       //System.out.println(smallestDistance);

        return smallestDistance;
    }




    public int findDistToOcean(String direction, int distance) {
        if (elevation < BiomeGenerator.OCEAN_LEVEL || isRiver)
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

    public boolean createMountain(int size) {


        if (ne == null || sw == null || nw == null || se == null || elevation < 0.7f) return false;

        isMountainTop = true;

        Random rand = new Random();
        size--;

        int randomNumber = rand.nextInt(4);

        if (size < 2) return true;
        System.out.println(randomNumber);
        switch (randomNumber) {
            case 0:
                se.createMountain(size);
                break;
            case 1:
                sw.createMountain(size);
                break;
            case 2:
                ne.createMountain(size);
                break;
            case 3:
                nw.createMountain(size);
                break;
        }

        int slopeSize = 10+rand.nextInt(15);

        createMountainSide(slopeSize);

        return true;

    }

    public boolean createMountainSide(int size) {

        Random rand = new Random();
        size -= 1+rand.nextInt(3);

        if (ne == null || sw == null || nw == null || se == null || elevation < 0.6f || size < 2) return false;

        if (!se.isMountainSide && !se.isMountainTop) se.createMountainSide(size);
        if (!sw.isMountainSide && !sw.isMountainTop) sw.createMountainSide(size);
        if (!ne.isMountainSide && !ne.isMountainTop) ne.createMountainSide(size);
        if (!nw.isMountainSide && !nw.isMountainTop) nw.createMountainSide(size);
        if (!e.isMountainSide && !e.isMountainTop) e.createMountainSide(size);
        if (!w.isMountainSide && !w.isMountainTop) w.createMountainSide(size);

        if (!isMountainTop)
            isMountainSide = true;

        return true;

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
        this.e = e;
    }

    public void setWest(MapCoord w) {
        this.w = w;
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
