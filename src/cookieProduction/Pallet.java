package cookieProduction;

public class Pallet {
	private String barcodeID, cookieName, orderID, productionDate,
			productionTime, deliveryDate, deliveryTime;

	public Pallet(String barcodeID, String cookieName, String orderID,
			String productionDate, String productionTime, String deliveryDate,
			String deliveryTime) {
		this.barcodeID = barcodeID;
		this.cookieName = cookieName;
		this.orderID = orderID;
		this.productionDate = productionDate;
		this.productionTime = productionTime;
		this.deliveryDate = deliveryDate;
		this.deliveryTime = deliveryTime;
	}
	@Override
	public String toString(){
		return barcodeID + ", " +  cookieName + ", " +  orderID + ", " +  productionDate + ", " + 
		productionTime + ", " +  deliveryDate + ", " +  deliveryTime;
	}
}
