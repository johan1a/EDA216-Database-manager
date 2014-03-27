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
				+ ",  orderID ,  productionDate, "
				+ " deliveryDate ,  deliveryTime");
		result.append("\n");
		for (Pallet pallet : pallets) {
			result.append(pallet.toString());
			result.append("\n");
		}
		return result.toString();
	}

	public void add(Pallet pallet) {
		pallets.add(pallet);

	}

	public boolean isEmpty() {
		return pallets.size() == 0;
	}
}
