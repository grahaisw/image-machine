package id.co.grahaisw.imagemachine.Model;

public class MachineImage {

    private int id;
    private int machine_id;
    private String uri;

    public MachineImage(int id, int machine_id, String uri) {
        this.id = id;
        this.machine_id = machine_id;
        this.uri = uri;
    }

    public MachineImage() {
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
