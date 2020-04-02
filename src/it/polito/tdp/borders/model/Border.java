package it.polito.tdp.borders.model;

public class Border {
    private int codiceConf;
    private Country statoA;
    private Country StatoB;
    private int anno;
	public Border(int codiceConf, Country statoA, Country statoB, int anno) {
		super();
		this.codiceConf = codiceConf;
		this.statoA = statoA;
		StatoB = statoB;
		this.anno = anno;
	}
	public int getCodiceConf() {
		return codiceConf;
	}
	public void setCodiceConf(int codiceConf) {
		this.codiceConf = codiceConf;
	}
	public Country getStatoA() {
		return statoA;
	}
	public void setStatoA(Country statoA) {
		this.statoA = statoA;
	}
	public Country getStatoB() {
		return StatoB;
	}
	public void setStatoB(Country statoB) {
		StatoB = statoB;
	}
	public int getAnno() {
		return anno;
	}
	public void setAnno(int anno) {
		this.anno = anno;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((StatoB == null) ? 0 : StatoB.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Border other = (Border) obj;
		if (StatoB == null) {
			if (other.StatoB != null)
				return false;
		} else if (!StatoB.equals(other.StatoB))
			return false;
		return true;
	}
	
    
    
}
