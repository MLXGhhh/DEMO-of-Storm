package entity;
/**
 *
 * 安全告警日志
 * Created by hky on 2018/12/10.
 *
 * */
public class alert_ipv4_realtime {
    private String Record_Time;
    private String Source_IP;//源IP
    private String Source_Port;//源端口
    private String Destination_IP;//目的IP
    private String Destination_Port;//目的端口
    private String Protocol;//协议
    private String Signature;//告警信息
    private String Category;//告警类型
    private String Severity;//告警级别
    private String id;

    public alert_ipv4_realtime() {
    }

    public alert_ipv4_realtime(String record_Time, String source_IP, String source_Port, String destination_IP, String destination_Port, String protocol, String signature, String category, String severity, String id) {
        Record_Time = record_Time;
        Source_IP = source_IP;
        Source_Port = source_Port;
        Destination_IP = destination_IP;
        Destination_Port = destination_Port;
        Protocol = protocol;
        Signature = signature;
        Category = category;
        Severity = severity;
        this.id = id;
    }

    public String getRecord_Time() {
        return Record_Time;
    }

    public void setRecord_Time(String record_Time) {
        Record_Time = record_Time;
    }

    public String getSource_IP() {
        return Source_IP;
    }

    public void setSource_IP(String source_IP) {
        Source_IP = source_IP;
    }

    public String getSource_Port() {
        return Source_Port;
    }

    public void setSource_Port(String source_Port) {
        Source_Port = source_Port;
    }

    public String getDestination_IP() {
        return Destination_IP;
    }

    public void setDestination_IP(String destination_IP) {
        Destination_IP = destination_IP;
    }

    public String getDestination_Port() {
        return Destination_Port;
    }

    public void setDestination_Port(String destination_Port) {
        Destination_Port = destination_Port;
    }

    public String getProtocol() {
        return Protocol;
    }

    public void setProtocol(String protocol) {
        Protocol = protocol;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSeverity() {
        return Severity;
    }

    public void setSeverity(String severity) {
        Severity = severity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
