package Main;

public class Corner extends MapCoord{

    public void createRiver(boolean continuation) {

        if ((continuation && getElevation() > 0) && (!isRiver)) {

            setRiver();

            if (ne == null || se == null || sw == null || nw == null) {

            } else {

                Center target = (Center) ne;

                if (target.getElevation() >= se.getElevation() && !se.isRiver()) target = (Center) se;
                if (target.getElevation() >= ne.getElevation() && !ne.isRiver()) target = (Center) ne;
                if (target.getElevation() >= sw.getElevation() && !sw.isRiver()) target = (Center) sw;

                if (target.isRiver()) {

/*
                    ((Center) ne).createRiver(true);
                    ((Center) nw).createRiver(true);
                    ((Center) sw).createRiver(true);
                    ((Center) se).createRiver(true);*/

                } else {
                    target.createRiver(true);
                }

            }

        }
    }
}
