package utility;

public class Position {
    int distanceTo101;
    int distanceTo102;
    int distanceTo103;
    int distanceTo104;
    String posXYZ;

    public Position(int distanceTo101, int distanceTo102, int distanceTo103, int distanceTo104) {
        this.distanceTo101 = distanceTo101;
        this.distanceTo102 = distanceTo102;
        this.distanceTo103 = distanceTo103;
        this.distanceTo104 = distanceTo104;
    }

    public int getDistanceTo101() {
        return distanceTo101;
    }

    public void setDistanceTo101(int distanceTo101) {
        this.distanceTo101 = distanceTo101;
    }

    public int getDistanceTo102() {
        return distanceTo102;
    }

    public void setDistanceTo102(int distanceTo102) {
        this.distanceTo102 = distanceTo102;
    }

    public int getDistanceTo103() {
        return distanceTo103;
    }

    public void setDistanceTo103(int distanceTo103) {
        this.distanceTo103 = distanceTo103;
    }

    public int getDistanceTo104() {
        return distanceTo104;
    }

    public void setDistanceTo104(int distanceTo104) {
        this.distanceTo104 = distanceTo104;
    }

    public String getPosXYZ() {
        return posXYZ;
    }

    public void setPosXYZ(String posXYZ) {
        this.posXYZ = posXYZ;
    }
}
