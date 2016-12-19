package InvertedIndex;

import java.util.*;

public class BuildInvertedIndexHelper {
    Map<String, Map<String, Integer>> invertedIndex = new HashMap();

    public void sortMap() {
        for(String key : invertedIndex.keySet()) {
            Map<String, Integer> map = invertedIndex.get(key);
            Map<String, Integer> orderedMap = sortByValue(map);
            invertedIndex.put(key, orderedMap);
        }
    }

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    public void addWordsToInvertedIndex(String word, String docID, int points) {
        Map<String, Integer> lsOfDocID = null;
        if (invertedIndex.containsKey(word)) {
            lsOfDocID = invertedIndex.get(word);
            if (lsOfDocID.containsKey(docID))  lsOfDocID.put(docID, lsOfDocID.get(docID)+points);
            else lsOfDocID.put(docID, points);
            invertedIndex.put(word, lsOfDocID);
        } else {
            lsOfDocID = new HashMap();
            lsOfDocID.put(docID, points);
            invertedIndex.put(word, lsOfDocID);
        }
    }

    public Map<String, Integer> getDocWithFreq(String word) {
        return this.invertedIndex.get(word);
    }
}
