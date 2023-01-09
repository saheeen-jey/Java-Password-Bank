


public class MyLinkedList {
	private Node head;
	private int entryID;
	private int numberOfHashes = 0;
	
	private static int settingState;
	
	public MyLinkedList(int entryID) {
		this.entryID = entryID;
		settingState = SettingState.getNumberOfPasswordRepeatsAccepted();
		String[] hashes = getPreviousHashes();
		for(int i =hashes.length - 1; i >= 0; i--) {
			this.add(hashes[i]);
		}
	}
	
	public class Node {
		Node next;
		String hash;
		
		public Node(String hash, Node next) {
			this.next = next;
			this.hash = hash;
		}
	}
	
	public boolean isRepeatingPassword(String inputHash) {
		for(Node temp = head; temp != null; temp = temp.next) {
			if(temp.hash.equals(inputHash))
				return true;
		}
		return false;
	}
	

	public void pop() {
		if(head == null) {
			return;
		}
		
		Node current = head;
		Node previous = null;
		while(current.next != null) {
			previous = current;
			current = current.next;
		}
		
		//If only the head is present, assigning previous.next will cause a null pointer exception.
		//Therefore the case of only one element in the link list should be considered.
		
		
		if(head.next != null) {
			previous.next = null;
		}
		else
			head = null;
		numberOfHashes--;
	}
	
	public void add(String hash) {
		
		head = new Node(hash, head);
		numberOfHashes++;
	}
	
	public int getNumberOfNodes() {
		return numberOfHashes;
	}
	
	private String[] getPreviousHashes() {
		String[] allEntries = Util.readATextFile(BankState.getUsername() + "hashes.txt");
		String[] dataOfLinkedList = new String[settingState];
		

		for(int lineNumber =1; lineNumber <= allEntries.length; lineNumber++) {
			if(allEntries[lineNumber-1].equals("" + entryID)) {
				for(int i = 0; i < settingState; i++) {
					dataOfLinkedList[i] = allEntries[lineNumber + i]; 
				}
				break;
			}
		}
		
		return dataOfLinkedList;
	}
	
	public void replacePreviousHashes() {
		String[] allEntries = Util.readATextFile(BankState.getUsername() + "hashes.txt");
		
		boolean isLineWithIDFound = false;
		
		for(int lineNumber =1; lineNumber <= allEntries.length; lineNumber++) {
			if(allEntries[lineNumber-1].equals("" + entryID)) {
				isLineWithIDFound = true;
				continue;
			}
			if(isLineWithIDFound) {
				int counter = 0;
				for(Node temp = head; temp != null; temp = temp.next) {
					Util.replaceLine(BankState.getUsername() + "hashes.txt", temp.hash, lineNumber + counter);
					counter++;
				}
			break;
			}
		}
	}

	public void printHashes() {
		System.out.println();
		for(Node temp = head; temp != null; temp = temp.next) {
			System.out.println(temp.hash);
		}
	}
	
	
}
