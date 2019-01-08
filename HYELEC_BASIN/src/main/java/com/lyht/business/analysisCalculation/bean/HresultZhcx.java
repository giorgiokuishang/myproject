package com.lyht.business.analysisCalculation.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "H_RESULT_ZHCX")
public class HresultZhcx implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nm;
	
	private String stcd;
	
	private String pch;
	
	private Double ertc;
	
	private Double mj;
	
	private Double mc;
	
	private Double mjmc;

	@Id
	@Column(name = "NM",length=50,unique = true,nullable = false)
	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	@Column(name="STCD")
	public String getStcd() {
		return stcd;
	}

	public void setStcd(String stcd) {
		this.stcd = stcd;
	}
	@Column(name="PCH")
	public String getPch() {
		return pch;
	}

	public void setPch(String pch) {
		this.pch = pch;
	}
	@Column(name="ERTC")
	public Double getErtc() {
		return ertc;
	}

	public void setErtc(Double ertc) {
		this.ertc = ertc;
	}
	@Column(name="MJMC")
	public Double getMjmc() {
		return mjmc;
	}

	public void setMjmc(Double mjmc) {
		this.mjmc = mjmc;
	}

	@Column(name="MJ")
	public Double getMj() {
		return mj;
	}

	public void setMj(Double mj) {
		this.mj = mj;
	}
	@Column(name="MC")
	public Double getMc() {
		return mc;
	}

	public void setMc(Double mc) {
		this.mc = mc;
	}
}
