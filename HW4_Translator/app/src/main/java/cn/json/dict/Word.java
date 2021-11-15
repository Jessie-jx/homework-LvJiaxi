/**
 * Copyright 2021 json.cn
 */
package cn.json.dict;

import java.util.List;

/**
 * Auto-generated: 2021-11-09 21:39:43
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Word {

    private String usphone;
    private String ukphone;
    private List<Trs> trs;
    private List<TransList> transList;
    private String pos;
    private List<Wfs> wfs;

    public void setTrs(List<Trs> trs) {
        this.trs = trs;
    }
    public List<Trs> getTrs() {
        return trs;
    }

    public void setUsphone(String usphone) {
        this.usphone = usphone;
    }

    public String getUsphone() {
        return usphone;
    }

    public void setUkphone(String ukphone) {
        this.ukphone = ukphone;
    }

    public String getUkphone() {
        return ukphone;
    }

    public void setTransList(List<TransList> transList) {
        this.transList = transList;
    }

    public List<TransList> getTransList() {
        return transList;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getPos() {
        return pos;
    }

    public void setWfs(List<Wfs> wfs) {
        this.wfs = wfs;
    }

    public List<Wfs> getWfs() {
        return wfs;
    }

}