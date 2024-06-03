package objects;

public class Lego {
	protected int legoID;
	protected String legoName;
	protected int legoMinAge, legoPieces;
	protected double legoPrice;
	public Lego(int legoID, String legoName, int legoMinAge, int legoPieces, double legoPrice) {
		super();
		this.legoID = legoID;
		this.legoName = legoName;
		this.legoMinAge = legoMinAge;
		this.legoPieces = legoPieces;
		this.legoPrice = legoPrice;
	}
	public int getLegoID() {
		return legoID;
	}
	public void setLegoID(int legoID) {
		this.legoID = legoID;
	}
	public String getLegoName() {
		return legoName;
	}
	public void setLegoName(String legoName) {
		this.legoName = legoName;
	}
	public int getLegoMinAge() {
		return legoMinAge;
	}
	public void setLegoMinAge(int legoMinAge) {
		this.legoMinAge = legoMinAge;
	}
	public int getLegoPieces() {
		return legoPieces;
	}
	public void setLegoPieces(int legoPieces) {
		this.legoPieces = legoPieces;
	}
	public double getLegoPrice() {
		return legoPrice;
	}
	public void setLegoPrice(int legoPrice) {
		this.legoPrice = legoPrice;
	}
}
