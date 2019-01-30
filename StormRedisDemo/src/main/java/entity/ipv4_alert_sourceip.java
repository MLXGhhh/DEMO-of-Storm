package entity;
/**
 *
 * 源IP告警统计
 * Created by hky on 2018/12/14.
 *
 * */
public class ipv4_alert_sourceip {
    private String RecordTime; //时间
    private String SourceIP; //源ip
    private String AttackWay;//攻击方向
    private String AttackCount;//攻击次数（安全事件数）
    private String id;

    public ipv4_alert_sourceip(String recordTime, String sourceIP, String attackWay, String attackCount, String id) {
        RecordTime = recordTime;
        SourceIP = sourceIP;
        AttackWay = attackWay;
        AttackCount = attackCount;
        this.id =id;
    }

    public String getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(String recordTime) {
        RecordTime = recordTime;
    }

    public String getSourceIP() {
        return SourceIP;
    }

    public void setSourceIP(String sourceIP) {
        SourceIP = sourceIP;
    }

    public String getAttackWay() {
        return AttackWay;
    }

    public void setAttackWay(String attackWay) {
        AttackWay = attackWay;
    }

    public String getAttackCount() {
        return AttackCount;
    }

    public void setAttackCount(String attackCount) {
        AttackCount = attackCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
