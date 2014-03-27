package cookieProduction;

public class Pallet {
	private String barcodeID, cookieName, orderID, productionDate,
			deliveryDate, blockStatus;

	public Pallet(String barcodeID, String cookieName, String orderID,
			String productionDate, String deliveryDate, String blockStatus) {
		this.barcodeID = barcodeID;
		this.cookieName = cookieName;
		this.orderID = orderID;
		this.productionDate = productionDate;
		this.deliveryDate = deliveryDate;
		this.blockStatus = blockStatus;
	}

	@Override
	public String toString() {
		return barcodeID + ", " + cookieName + ", " + orderID + ", "
				+ productionDate + ", " + deliveryDate + ", " + blockStatus;
	}
}
