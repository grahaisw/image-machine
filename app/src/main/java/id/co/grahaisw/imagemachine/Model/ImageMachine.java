package id.co.grahaisw.imagemachine.Model;

import android.graphics.Bitmap;

public class ImageMachine {

    private int id;
    private int machine_id;
    private Bitmap blob;

    ImageMachine(int id, int machine_id, Bitmap blob) {
        this.id = id;
        this.machine_id = machine_id;
        this.blob = blob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(int machine_id) {
        this.machine_id = machine_id;
    }

    public Bitmap getBlob() {
        return blob;
    }

    public void setBlob(Bitmap blob) {
        this.blob = blob;
    }
}
