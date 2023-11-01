public class Main {
    public static void main(String[] args){
        Rbt tree = new Rbt();

        // Load some initial data
        tree.insert(5);
        tree.insert(15);
        tree.insert(2);

        // Show the tree
        while(true) {
            Vis.showTree(tree);
            if(!Vis.showMenu(tree)){
                break;
            }
        }
    }
}