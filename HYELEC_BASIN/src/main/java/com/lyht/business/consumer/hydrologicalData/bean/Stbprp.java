package com.lyht.business.consumer.hydrologicalData.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.sun.org.glassfish.gmbal.Description;

@Entity
@Table(name = "ST_STBPRP_B")
@SuppressWarnings("restriction")
public class Stbprp implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Description(key="primary",value="测站编码")
	private String stcd;
	
	@Description(key="display",value="测站名称")
	private String stnm;
	
	@Description(key="display",value="河流名称")
	private String rvnm;
	
	@Description(key="display",value="水系名称")
	private String hnnm;
	
	@Description(key="display",value="流域名称")
	private String bsnm;

	@Description(key="display",value="经度")
	private Double lgtd1;
	
	@Description(key="display",value="纬度")
	private Double lttd1;
	
	@Description(key="display",value="站址")
	private String stlc;
	
	@Description(key="display",value="行政区划码")
	private String addvcd;
	
	private String addvcdname;
	
	@Description(key="display",value="基面名称")
	private String dtmnm;
	
	@Description(key="display",value="基面高程")
	private Double dtmel;
	
	@Description(key="display",value="基面修正值")
	private Double dtpr;
	
	@Description(key="display",value="站类")
	private String sttp;
	
	@Description(key="display",value="报汛等级")
	private String frgrd;
	
	@Description(key="display",value="建站年月")
	private String esstym;
	
	@Description(key="display",value="始报年月")
	private String bgfrym;
	
	@Description(key="display",value="隶属单位")
	private String atcunit;
	
	@Description(key="display",value="管理单位")
	private String admauth;
	
	@Description(key="display",value="领导机关")
	private String locality;
	
	@Description(key="display",value="测站岸别")
	private String stbk;
	
	@Description(key="display",value="测站方位")
	private Double stazt;
	
	@Description(key="display",value="至河口距离")
	private Double dstrvm;
	
	@Description(key="display",value="集水面积")
	private Double drna;
	
	@Description(key="display",value="拼音码")
	private String phcd;
	
	@Description(key="display",value="田间持水量")
	private Double fieldcap;
	
	@Description(key="display",value="启用标识")
	private String usesymb;
	
	@Description(key="display",value="备注")
	private String comments;
	
	@Description(key="display",value="修改时间戳")
	private String moditime;

	public Stbprp() {
	}

	public Stbprp(
			String stcd, 
			String stnm, 
			String rvnm, 
			String hnnm, 
			String bsnm, 
			Double lgtd1, 
			Double lttd1,
			String stlc, 
			String addvcd, 
			String dtmnm, 
			Double dtmel, 
			Double dtpr, 
			String sttp, 
			String frgrd,
			String esstym, 
			String bgfrym, 
			String atcunit, 
			String admauth, 
			String locality, 
			String stbk, 
			Double stazt,
			Double dstrvm, 
			Double drna, 
			String phcd, 
			Double fieldcap, 
			String usesymb, 
			String comments, 
			String moditime) {
		super();
		this.stcd = stcd;
		this.stnm = stnm;
		this.rvnm = rvnm;
		this.hnnm = hnnm;
		this.bsnm = bsnm;
		this.lgtd1 = lgtd1;
		this.lttd1 = lttd1;
		this.stlc = stlc;
		this.addvcd = addvcd;
		this.dtmnm = dtmnm;
		this.dtmel = dtmel;
		this.dtpr = dtpr;
		this.sttp = sttp;
		this.frgrd = frgrd;
		this.esstym = esstym;
		this.bgfrym = bgfrym;
		this.atcunit = atcunit;
		this.admauth = admauth;
		this.locality = locality;
		this.stbk = stbk;
		this.stazt = stazt;
		this.dstrvm = dstrvm;
		this.drna = drna;
		this.phcd = phcd;
		this.fieldcap = fieldcap;
		this.usesymb = usesymb;
		this.comments = comments;
		this.moditime = moditime;
	}
	
	@Id
	@Column(name = "STCD",length=8 )
	public String getStcd() {
		return stcd;
	}

	public void setStcd(String stcd) {
		this.stcd = stcd;
	}
	@Column(name = "STNM",length=30 )
	public String getStnm() {
		return stnm;
	}

	public void setStnm(String stnm) {
		this.stnm = stnm;
	}
	@Column(name = "RVNM",length=30 )
	public String getRvnm() {
		return rvnm;
	}

	public void setRvnm(String rvnm) {
		this.rvnm = rvnm;
	}
	@Column(name = "HNNM",length=30 )
	public String getHnnm() {
		return hnnm;
	}

	public void setHnnm(String hnnm) {
		this.hnnm = hnnm;
	}
	@Column(name = "BSNM",length=30 )
	public String getBsnm() {
		return bsnm;
	}

	public void setBsnm(String bsnm) {
		this.bsnm = bsnm;
	}
	@Column(name = "LGTD1" )
	public Double getLgtd1() {
		return lgtd1;
	}

	public void setLgtd1(Double lgtd1) {
		this.lgtd1 = lgtd1;
	}
	@Column(name = "LTTD1" )
	public Double getLttd1() {
		return lttd1;
	}

	public void setLttd1(Double lttd1) {
		this.lttd1 = lttd1;
	}
	@Column(name = "STLC",length=50 )
	public String getStlc() {
		return stlc;
	}

	public void setStlc(String stlc) {
		this.stlc = stlc;
	}
	@Column(name = "ADDVCD",length=6 )
	public String getAddvcd() {
		return addvcd;
	}

	public void setAddvcd(String addvcd) {
		this.addvcd = addvcd;
	}
	@Column(name = "DTMNM",length=16 )
	public String getDtmnm() {
		return dtmnm;
	}

	public void setDtmnm(String dtmnm) {
		this.dtmnm = dtmnm;
	}
	@Column(name = "DTMEL" )
	public Double getDtmel() {
		return dtmel;
	}

	public void setDtmel(Double dtmel) {
		this.dtmel = dtmel;
	}
	@Column(name = "DTPR" )
	public Double getDtpr() {
		return dtpr;
	}

	public void setDtpr(Double dtpr) {
		this.dtpr = dtpr;
	}
	@Column(name = "STTP",length=2 )
	public String getSttp() {
		return sttp;
	}

	public void setSttp(String sttp) {
		this.sttp = sttp;
	}
	@Column(name = "FRGRD",length=1 )
	public String getFrgrd() {
		return frgrd;
	}

	public void setFrgrd(String frgrd) {
		this.frgrd = frgrd;
	}
	@Column(name = "ESSTYM",length=6 )
	public String getEsstym() {
		return esstym;
	}

	public void setEsstym(String esstym) {
		this.esstym = esstym;
	}
	@Column(name = "BGFRYM",length=6 )
	public String getBgfrym() {
		return bgfrym;
	}

	public void setBgfrym(String bgfrym) {
		this.bgfrym = bgfrym;
	}
	@Column(name = "ATCUNIT",length=20 )
	public String getAtcunit() {
		return atcunit;
	}

	public void setAtcunit(String atcunit) {
		this.atcunit = atcunit;
	}
	@Column(name = "ADMAUTH",length=20 )
	public String getAdmauth() {
		return admauth;
	}

	public void setAdmauth(String admauth) {
		this.admauth = admauth;
	}
	@Column(name = "LOCALITY",length=20 )
	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}
	@Column(name = "STBK",length=1 )
	public String getStbk() {
		return stbk;
	}

	public void setStbk(String stbk) {
		this.stbk = stbk;
	}
	@Column(name = "STAZT" )
	public Double getStazt() {
		return stazt;
	}

	public void setStazt(Double stazt) {
		this.stazt = stazt;
	}
	@Column(name = "DSTRVM" )
	public Double getDstrvm() {
		return dstrvm;
	}

	public void setDstrvm(Double dstrvm) {
		this.dstrvm = dstrvm;
	}
	@Column(name = "DRNA" )
	public Double getDrna() {
		return drna;
	}

	public void setDrna(Double drna) {
		this.drna = drna;
	}
	@Column(name = "PHCD",length=6 )
	public String getPhcd() {
		return phcd;
	}

	public void setPhcd(String phcd) {
		this.phcd = phcd;
	}
	@Column(name = "FIELDCAP" )
	public Double getFieldcap() {
		return fieldcap;
	}

	public void setFieldcap(Double fieldcap) {
		this.fieldcap = fieldcap;
	}
	@Column(name = "USESYMB",length=1 )
	public String getUsesymb() {
		return usesymb;
	}

	public void setUsesymb(String usesymb) {
		this.usesymb = usesymb;
	}
	@Column(name = "COMMENTS",length=200 )
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	@Column(name = "MODITIME" )
	public String getModitime() {
		return moditime;
	}

	public void setModitime(String moditime) {
		this.moditime = moditime;
	}
	@Transient
	public String getAddvcdname() {
		return addvcdname;
	}

	public void setAddvcdname(String addvcdname) {
		this.addvcdname = addvcdname;
	}
	@Override
	public String toString() {
		return "Stbprp [stcd=" + stcd + ", stnm=" + stnm + ", rvnm=" + rvnm + ", hnnm=" + hnnm + ", bsnm=" + bsnm
				+ ", lgtd1=" + lgtd1 + ", lttd1=" + lttd1 + ", stlc=" + stlc + ", addvcd=" + addvcd + ", dtmnm=" + dtmnm
				+ ", dtmel=" + dtmel + ", dtpr=" + dtpr + ", sttp=" + sttp + ", frgrd=" + frgrd + ", esstym=" + esstym
				+ ", bgfrym=" + bgfrym + ", atcunit=" + atcunit + ", admauth=" + admauth + ", locality=" + locality
				+ ", stbk=" + stbk + ", stazt=" + stazt + ", dstrvm=" + dstrvm + ", drna=" + drna + ", phcd=" + phcd
				+ ", fieldcap=" + fieldcap + ", usesymb=" + usesymb + ", comments=" + comments + ", moditime="
				+ moditime + "]";
	}
	
}
