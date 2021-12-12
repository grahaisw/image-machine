package id.co.grahaisw.imagemachine.Model;

public class Machine {

    private int id;
    private String name;
    private String type;
    private int code;
    private String last_mt;

    public Machine(int id, String name, String type, int code, String last_mt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.code = code;
        this.last_mt = last_mt;
    }

    public Machine() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLast_mt() {
        return last_mt;
    }

    public void setLast_mt(String last_mt) {
        this.last_mt = last_mt;
    }
}
