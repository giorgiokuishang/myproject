package com.lyht.business.datasearch.formbean;

import java.io.Serializable;

import com.lyht.base.hibernate.common.PageResults;

@SuppressWarnings("rawtypes")
public class DataSerchFormBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private PageResults pageBean=new PageResults(); // 分页
    private Integer page;
    private Integer limit;
    private Integer id; // id
    private String sjly;//数据来源，全部，三部门
    private String xmmc; // 项目名称
    private String sheng; // 省级
    private String shi; // 市级
    private String xian; // 县级
    //private String yjzl; // 一级支流
    private String xmszly; // 项目所在流域
    //private String jthl; // 具体支流
    private String jszt; // 建设运营状态
    private String tcny; // 投产时间
    private String tcnyEnd; // 选择结束时间
    private double zjrl; // 装机容量
    private double zjrlMax; // 装机容量最大
    private String kffs; // 开发方式
    //private double sjnfdl; // 设计年发电量
    //private double sjfdl; // 2017年实际发电量
    //private double ztz; // 总投资
    private String tzxz; // 投资性质
    //private String tzly; // 投资来源
    private String bwqk; // 并网情况
    //private String zhltqk; // 综合利用情况
    //private double zrk; // 总库容
    //private double bg; // 坝高
    //private double swdj; // 上网电价
    private String sfyxmhz; // 项目审批状况
    private String sffhgh; // 规划符合状况
    private String sffhghhp; // 是否符合规划环评
    private String sftgjghbys; // 是否通过竣工环保验收
    private String sftgxmhp; // 是否通过项目环评
    private String hpsjzytcsj; // 环评时间早于投产时间
    private String sfyjgbg; // 是否有竣工报告
    private String stllxfcs; // 生态流量泄放措施
    private String stljkss; // 生态流量监控设施
    private String gycs; // 过鱼措施
    private String zzflcs; // 增殖放流措施
    private String sfsjzrbhq; // 是否涉及自然保护区
    private String hxq; // 涉及核心区
    private String bxsfcztsgk; // 坝下是否存在脱水干涸
    private String hcq; // 涉及缓冲区
    private String sys; // 涉及试验区
    private String dztcsjybhqslsjxhgx; // 电站投产时间与保护区设立时间先后关系
    private String wfq; // 保护区未分区
    

    public String getSjly() {
		return sjly;
	}


	public void setSjly(String sjly) {
		this.sjly = sjly;
	}


	public Integer getId() {
        return id;
    }

   
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public String getTcnyEnd() {
        return tcnyEnd;
    }
    
    public void setTcnyEnd(String tcnyEnd) {
        this.tcnyEnd = tcnyEnd;
    }
    
    public double getZjrlMax() {
        return zjrlMax;
    }
    
    public void setZjrlMax(double zjrlMax) {
        this.zjrlMax = zjrlMax;
    }

    public String getStllxfcs() {
        return stllxfcs;
    }

    public void setStllxfcs(String stllxfcs) {
        this.stllxfcs = stllxfcs;
    }

    public String getStljkss() {
        return stljkss;
    }

    public void setStljkss(String stljkss) {
        this.stljkss = stljkss;
    }

    public String getGycs() {
        return gycs;
    }

    public void setGycs(String gycs) {
        this.gycs = gycs;
    }

    public String getZzflcs() {
        return zzflcs;
    }

    public void setZzflcs(String zzflcs) {
        this.zzflcs = zzflcs;
    }

    public String getSfsjzrbhq() {
        return sfsjzrbhq;
    }

    public void setSfsjzrbhq(String sfsjzrbhq) {
        this.sfsjzrbhq = sfsjzrbhq;
    }

    public String getHxq() {
        return hxq;
    }

    public void setHxq(String hxq) {
        this.hxq = hxq;
    }

    public String getBxsfcztsgk() {
        return bxsfcztsgk;
    }

    public void setBxsfcztsgk(String bxsfcztsgk) {
        this.bxsfcztsgk = bxsfcztsgk;
    }

    public String getHcq() {
        return hcq;
    }

    public void setHcq(String hcq) {
        this.hcq = hcq;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getDztcsjybhqslsjxhgx() {
        return dztcsjybhqslsjxhgx;
    }

    public void setDztcsjybhqslsjxhgx(String dztcsjybhqslsjxhgx) {
        this.dztcsjybhqslsjxhgx = dztcsjybhqslsjxhgx;
    }

    public String getWfq() {
        return wfq;
    }

    public void setWfq(String wfq) {
        this.wfq = wfq;
    }

    public String getSfyxmhz() {
        return sfyxmhz;
    }

    public void setSfyxmhz(String sfyxmhz) {
        this.sfyxmhz = sfyxmhz;
    }

    public String getSffhgh() {
        return sffhgh;
    }

    public void setSffhgh(String sffhgh) {
        this.sffhgh = sffhgh;
    }

    public String getSffhghhp() {
        return sffhghhp;
    }

    public void setSffhghhp(String sffhghhp) {
        this.sffhghhp = sffhghhp;
    }

    public String getSftgjghbys() {
        return sftgjghbys;
    }

    public void setSftgjghbys(String sftgjghbys) {
        this.sftgjghbys = sftgjghbys;
    }

    public String getSftgxmhp() {
        return sftgxmhp;
    }

    public void setSftgxmhp(String sftgxmhp) {
        this.sftgxmhp = sftgxmhp;
    }

    public String getHpsjzytcsj() {
        return hpsjzytcsj;
    }

    public void setHpsjzytcsj(String hpsjzytcsj) {
        this.hpsjzytcsj = hpsjzytcsj;
    }

    public String getSfyjgbg() {
        return sfyjgbg;
    }

    public void setSfyjgbg(String sfyjgbg) {
        this.sfyjgbg = sfyjgbg;
    }

    public void setZjrl(double zjrl) {
        this.zjrl = zjrl;
    }

    public String getTzxz() {
        return tzxz;
    }

    public void setTzxz(String tzxz) {
        this.tzxz = tzxz;
    }

    public String getBwqk() {
        return bwqk;
    }

    public void setBwqk(String bwqk) {
        this.bwqk = bwqk;
    }
    
    public PageResults getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageResults pageBean) {
        this.pageBean = pageBean;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getSheng() {
        return sheng;
    }

    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getXian() {
        return xian;
    }

    public void setXian(String xian) {
        this.xian = xian;
    }

    public String getXmszly() {
        return xmszly;
    }

    public void setXmszly(String xmszly) {
        this.xmszly = xmszly;
    }

    public String getJszt() {
        return jszt;
    }

    public void setJszt(String jszt) {
        this.jszt = jszt;
    }

    public String getTcny() {
        return tcny;
    }

    public void setTcny(String tcny) {
        this.tcny = tcny;
    }

    public double getZjrl() {
        return zjrl;
    }

    public void setZjrl(Integer zjrl) {
        this.zjrl = zjrl;
    }

    public String getKffs() {
        return kffs;
    }

    public void setKffs(String kffs) {
        this.kffs = kffs;
    }
    
}
