package Model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {


    //All Products

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static int nextPartId = 0;
    private static int productId = 0;

   // public static boolean partFound;

    //public static boolean productFound;

    //private static Part selectedPart = null;
    //private static Product selectedProduct = null;


    //Define methods
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static int incrementPartId(){
        nextPartId++;
        return nextPartId;
    }
    public static int incrementProductId() {
        productId++;
        return productId;
    }


    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partID) {

        for(Part pt : allParts) {
            if (pt.getId() == partID) {
              //  partFound = true;
                return pt;
            }
        }
        return null;
    }

    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> namedPart = FXCollections.observableArrayList();


        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part pt : allParts) {
            if (pt.getName().contains(partName)) {
                namedPart.add(pt);
            }
        }

        return namedPart;
    }

    public static ObservableList<Product> lookProduct(String productName) {
        ObservableList<Product> namedProduct = FXCollections.observableArrayList();

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product pr : allProducts) {
            if (pr.getName().contains(productName)) {
                namedProduct.add(pr);
            }
        }


        return namedProduct;
    }

    public static Product lookupProduct(int productId) {
        {

            for (int i = 0; i < allProducts.size(); i++) {
                Product pr = allProducts.get(i);

                if (pr.getId() == productId) {
                    return pr;
                }
            }

            return null;
        }
    }

    public static void updatePart(int index, Part selectedPart) {
        Inventory.getAllParts().set(index, selectedPart);

    }

    public static void updateProduct(int index, Product newProduct){
        Inventory.getAllProducts().set(index, newProduct);
    }


      public static boolean deletePart (Part selectedPart){
       // allParts.remove(selectedPart);

            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getPartId() == selectedPart.getPartId())
                {
                    if(allParts.get(i).getId() == selectedPart.getId()) {
                        allParts.remove(i);
                        return true;
                    }
                }
            }
          return false;
      }

      public static boolean deleteProduct (Product selectedProduct) {
          for (int i = 0; i < allProducts.size(); i++) {
              if (allProducts.get(i).getProductId() == selectedProduct.getProductId())
              {
                  if(allProducts.get(i).getId() == selectedProduct.getId()) {
                      allProducts.remove(i);
                      return true;
                  }
              }
          }
          return false;
      }

}


