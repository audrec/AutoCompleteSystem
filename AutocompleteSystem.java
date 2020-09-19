public class AutocompleteSystem {
    
    class TrieNode {
        Map<Character, TrieNode> children;
        Map<String, Integer> count;
        boolean isWord;
        
        TrieNode() {
            this.children = new HashMap<Character, TrieNode>();
            this.count = new HashMap<String, Integer>();
            this.isWord = false;
        }
    }
    
    private TrieNode root;
    private StringBuilder sb;
    
    public AutocompleteSystem(String[] sentences, int[] hotness) {
        root = new TrieNode();
        sb = new StringBuilder();
        
        for (int i = 0; i < sentences.length; i++) {
            insert(sentences[i], hotness[i]);
        }
    }
    
    private void insert(String s, int time) {
        TrieNode cur = root;
        for (int i = 0; i < s.length(); i++) {
            TrieNode next = cur.children.get(s.charAt(i));
            if (next == null) {
                next = new TrieNode();
                cur.children.put(s.charAt(i), next);
            }
            cur = next; 
            cur.count.put(s, cur.count.getOrDefault(s, 0) + time);
        }
        cur.isWord = true;
    }

    private TrieNode search(String s) {
        TrieNode cur = root;
        for (int i = 0; i < s.length(); i++) {
            TrieNode next = cur.children.get(s.charAt(i));
            if (next == null) {
                return null;
            }
            cur = next;
        }
        return cur;
    }
    
    public List<String> input(char c) {
        if (c == '\0') {
            // save the end result to the trie
            insert(sb.toString(), 1);
            sb = new StringBuilder();
            return new ArrayList<>();
        }
        
        sb.append(c);
        TrieNode cur = search(sb.toString());
        if (cur == null) {
            return new ArrayList<>();
        }
        
        Queue<String> queue = new PriorityQueue<>(3, (a, b) -> {
            if (cur.count.get(a) == cur.count.get(b)) {
                return a.compareTo(b);
            }
            return cur.count.get(b) - cur.count.get(a);
        });
        
        for (Map.Entry<String, Integer> entry : cur.count.entrySet()) {
            queue.offer(entry.getKey());
        }
        
        List<String> result = new ArrayList<>();
        int count = 0;
        while (!queue.isEmpty() && count < 10) {
            result.add(queue.poll());
            count++;
        }

        return result;
    }
}
 
