package Main;

public class Center extends MapCoord {

    public void createRiver(boolean continuation) {

        if ((moisture >= 1f || continuation) && (elevation > 0) && (elevation < 0.98f) && (!isRiver)) {

            System.out.println("before. function: " + isRiver() + " var: " + isRiver);
            setRiver();
            System.out.println("after. function: " + isRiver() + " var: " + isRiver);

            Corner target = (Corner) ne;

            if (target.getElevation() >= se.getElevation() && !se.isRiver()) target = (Corner) se;
            if (target.getElevation() >= ne.getElevation() && !ne.isRiver()) target = (Corner) ne;
            if (target.getElevation() >= sw.getElevation() && !sw.isRiver()) target = (Corner) sw;

            if (target.isRiver()) {


                ((Corner) ne).createRiver(true);
                ((Corner) nw).createRiver(true);
                ((Corner) sw).createRiver(true);
                ((Corner) se).createRiver(true);

            } else {
                target.createRiver(true);
            }

        }

    }
}
