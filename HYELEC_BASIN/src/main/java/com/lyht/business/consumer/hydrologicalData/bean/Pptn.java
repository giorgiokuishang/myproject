package com.lyht.business.consumer.hydrologicalData.bean;

import java.io.Serializable;

import javax.persistence.*;

import com.sun.org.glassfish.gmbal.Description;

@Entity
@Table(name = "ST_PPTN_R")
@SuppressWarnings("restriction")
public class Pptn implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Description(key="primary",value="编码")
	private String nm;
	
	@Description(key="primary",value="测站编码")
	private String stcd;
	
	@Description(key="display",value="开始时间")
	private String tm;
	
	@Description(key="display",value="结束时间")
	private String etm;
	
	@Description(key="display",value="时段降水量")
	private Double drp;
	
	@Description(key="display",value="时段长")
	private Double intv;
	
	@Description(key="display",value="降水历时")
	private Double pdr;
	
	@Description(key="display",value="日降水量")
	private Double dyp;
	
	@Description(key="display",value="天气状况")
	private String wth;
	
	public Pptn() {
       
    }
	@Id
	@Column(name="NM")
	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}
	
	@Column(name = "STCD",length=8 )
	public String getStcd() {
		return stcd;
	}

	public void setStcd(String stcd) {
		this.stcd = stcd;
	}
	
	@Column(name = "TM")
	public String getTm() {
		return tm;
	}

	public void setTm(String tm) {
		this.tm = tm;
	}
	@Column(name = "ETM")
	public String getEtm() {
		return etm;
	}

	public void setEtm(String etm) {
		this.etm = etm;
	}
	
	@Column(name = "DRP")
	public Double getDrp() {
		return drp;
	}

	public void setDrp(Double drp) {
		this.drp = drp;
	}

	@Column(name = "INTV")
	public Double getIntv() {
		return intv;
	}

	public void setIntv(Double intv) {
		this.intv = intv;
	}

	@Column(name = "PDR")
	public Double getPdr() {
		return pdr;
	}

	public void setPdr(Double pdr) {
		this.pdr = pdr;
	}

	@Column(name = "DYP")
	public Double getDyp() {
		return dyp;
	}

	public void setDyp(Double dyp) {
		this.dyp = dyp;
	}

	@Column(name = "WTH",length=1)
	public String getWth() {
		return wth;
	}

	public void setWth(String wth) {
		this.wth = wth;
	}
	
}
