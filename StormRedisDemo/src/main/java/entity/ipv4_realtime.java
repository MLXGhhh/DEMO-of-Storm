package entity;
/**
 * ipv4实时数据流
 *
 * */
public class ipv4_realtime {
    private String Time;//记录时间
    private String SourceiP;//源IP
    private String Sourceport;//源端口
    private String DestinationiP;//目的IP
    private String Destinationport;//目的端口
    private String Protocols;//协议类型
    private String id;

    public ipv4_realtime() {
    }

    public ipv4_realtime(String time, String sourceiP, String sourceport, String destinationiP, String destinationport, String protocols, String id) {
        Time = time;
        SourceiP = sourceiP;
        Sourceport = sourceport;
        DestinationiP = destinationiP;
        Destinationport = destinationport;
        Protocols = protocols;
        this.id = id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getSourceiP() {
        return SourceiP;
    }

    public void setSourceiP(String sourceiP) {
        SourceiP = sourceiP;
    }

    public String getSourceport() {
        return Sourceport;
    }

    public void setSourceport(String sourceport) {
        Sourceport = sourceport;
    }

    public String getDestinationiP() {
        return DestinationiP;
    }

    public void setDestinationiP(String destinationiP) {
        DestinationiP = destinationiP;
    }

    public String getDestinationport() {
        return Destinationport;
    }

    public void setDestinationport(String destinationport) {
        Destinationport = destinationport;
    }

    public String getProtocols() {
        return Protocols;
    }

    public void setProtocols(String protocols) {
        Protocols = protocols;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
