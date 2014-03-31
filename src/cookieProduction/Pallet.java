package cookieProduction;

public class Pallet {
	private String barcodeID, cookieName, orderID, productionDate,
			deliveryDate, blockStatus;
	private String customerName;

	public Pallet(String barcodeID, String cookieName, String orderID,
			String productionDate, String deliveryDate, String blockStatus,String customerName) {
		this.barcodeID = barcodeID;
		this.cookieName = cookieName;
		this.orderID = orderID;
		this.productionDate = productionDate;
		this.deliveryDate = deliveryDate;
		this.blockStatus = blockStatus;
		this.customerName = customerName;
	}

	@Override
	public String toString() {
		return barcodeID + ", " + cookieName + ", " + orderID + ", "
				+ productionDate + ", " + deliveryDate + ", " + blockStatus + ", " + customerName;
	}
}
