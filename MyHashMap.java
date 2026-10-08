public class MyHashMap {

    // Node for linked list
    class Node {
        int key;
        String value;
        Node next;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }   

    // Node for tree
    class TreeNode {
        int key;
        String value;
        TreeNode left, right;

        TreeNode(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // Bucket array
    Node[] table = new Node[5];

    // Tree roots
    TreeNode[] trees = new TreeNode[5];

    // Find bucket
    int index(int key) {
        return key % table.length;
    }

    // Insert key-value pair
    void put(int key, String value) {

        int i = index(key);

        // If bucket is already a tree
        if (trees[i] != null) {
            trees[i] = insertTree(trees[i], key, value);
            return;
        }

        // Empty bucket
        if (table[i] == null) {
            table[i] = new Node(key, value);
            return;
        }

        // Collision
        Node current = table[i];
        int count = 1;

        while (true) {

            // Key already exists
            if (current.key == key) {
                current.value = value;
                return;
            }

            // Last node
            if (current.next == null)
                break;

            current = current.next;
            count++;
        }

        // Add new node
        current.next = new Node(key, value);
        count++;

        // Heavy collision → treeify
        if (count >= 8)
            treeify(i);
    }

    // Convert linked list into tree
    void treeify(int i) {

        Node current = table[i];
        TreeNode root = null;

        while (current != null) {

            root = insertTree(
                root,
                current.key,
                current.value
            );

            current = current.next;
        }

        trees[i] = root;
        table[i] = null; // removed the linkedlist

        System.out.println("Bucket " + i + " converted to tree");
    }

    // Insert into BST
    TreeNode insertTree(TreeNode root, int key, String value) {

        if (root == null)
            return new TreeNode(key, value);

        if (key < root.key) {
            root.left = insertTree(
                root.left, key, value
            );
        }
        else if (key > root.key) {
            root.right = insertTree(
                root.right, key, value
            );
        }
        else {
            root.value = value;
        }

        return root;
    }

    // Get value
    String get(int key) {

        int i = index(key);

        // Search tree
        if (trees[i] != null)
            return searchTree(trees[i], key);

        // Search linked list
        Node current = table[i];

        while (current != null) {

            if (current.key == key)
                return current.value;

            current = current.next;
        }

        return null;
    }

    // Search tree
    String searchTree(TreeNode root, int key) {

        if (root == null)
            return null;

        if (key == root.key)
            return root.value;

        if (key < root.key)
            return searchTree(root.left, key);

        return searchTree(root.right, key);
    }

    // Remove key
    void remove(int key) {

        int i = index(key);

        // Remove from tree
        if (trees[i] != null) {
            trees[i] = deleteTree(trees[i], key);
            return;
        }

        // Remove from linked list
        Node current = table[i];
        Node previous = null;

        while (current != null) {

            if (current.key == key) {

                if (previous == null)
                    table[i] = current.next;
                else
                    previous.next = current.next;

                return;
            }

            previous = current;
            current = current.next;
        }
    }

    // Delete from BST
    TreeNode deleteTree(TreeNode root, int key) {

        if (root == null)
            return null;

        if (key < root.key) {

            root.left =
                deleteTree(root.left, key);

        }
        else if (key > root.key) {

            root.right =
                deleteTree(root.right, key);

        }
        else {

            // No left child
            if (root.left == null)
                return root.right;

            // No right child
            if (root.right == null)
                return root.left;

            // Two children
            TreeNode smallest =
                findSmallest(root.right);

                // copy the smallest node data
            root.key = smallest.key;
            root.value = smallest.value;


            // delete duplicate
            root.right =
                deleteTree(root.right, smallest.key);
        }

        return root;
    }

    // Find smallest node
    TreeNode findSmallest(TreeNode root) {

        while (root.left != null)
            root = root.left;

        return root;
    }

    public static void main(String[] args) {

        MyHashMap map = new MyHashMap();

        // All these keys collide at bucket 1
        map.put(1, "Apple");
        map.put(6, "Mango");
        map.put(11, "Orange");
        map.put(16, "Banana");
        map.put(21, "Grapes");
        map.put(26, "Guava");
        map.put(31, "Papaya");
        map.put(36, "Watermelon");

        System.out.println(map.get(1));
        System.out.println(map.get(21));
        System.out.println(map.get(36));

        map.remove(21);

        System.out.println(map.get(21));
    }
}