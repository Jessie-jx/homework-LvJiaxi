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
public class Phrs {

    private String word;
    private List<Phrs> phrs;
    private Phr phr;

    public void setPhr(Phr phr) {
        this.phr = phr;
    }

    public Phr getPhr() {
        return phr;
    }
    public void setWord(String word) {
         this.word = word;
     }
     public String getWord() {
         return word;
     }

    public void setPhrs(List<Phrs> phrs) {
         this.phrs = phrs;
     }
     public List<Phrs> getPhrs() {
         return phrs;
     }



}

