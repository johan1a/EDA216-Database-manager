package cookieProduction;

import java.util.ArrayList;

public class PalletList {
	private ArrayList<Pallet> pallets;

	public PalletList() {
		pallets = new ArrayList<Pallet>();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("barcodeID ,  cookieName"
				+ ",  orderID ,  productionDate "
				+ "productionTime ,  deliveryDate ,  deliveryTime");
		for (Pallet pallet : pallets) {
			result.append(pallet.toString());
			result.append("\n");
		}
		return result.toString();
	}

	public void add(Pallet pallet) {
		pallets.add(pallet);

	}
}
