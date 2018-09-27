import javax.print.DocFlavor;
import java.lang.reflect.Field;
import java.util.*;

public class StableMatching {

    private static final String[] MEN_LIST = {"Frank", "Dennis", "Mac", "Charlie"};
    private static final String[] WOMEN_LIST = {"Rhea", "Mary", "Kate", "Jill"};

    private static final String[] FRANK_LIST = {"Kate", "Mary", "Rhea", "Jill"};
    private static final String[] DENNIS_LIST = {"Mary", "Jill", "Rhea", "Kate"};
    private static final String[] MAC_LIST = {"Kate", "Rhea", "Jill", "Mary"};
    private static final String[] CHARLIE_LIST = {"Rhea", "Mary", "Kate", "Jill"};

    private static final String[] RHEA_LIST = {"Frank", "Mac", "Dennis", "Charlie"};
    private static final String[] MARY_LIST = {"Mac", "Charlie", "Dennis", "Frank"};
    private static final String[] KATE_LIST = {"Dennis", "Mac", "Charlie", "Frank"};
    private static final String[] JILL_LIST = {"Charlie", "Dennis", "Frank", "Mac"};

    private Set<String> menSet;
    private Set<String> womenSet;
    private Map<String, List<String>> preferenceMap;
    private Map<String, Person> pairMap;

    public StableMatching() {
        menSet = new HashSet<>();
        womenSet = new HashSet<>();
        pairMap = new HashMap<>();

        for (String man: MEN_LIST) {
            menSet.add(man);
            pairMap.put(man, new Person(man));
        }
        for (String woman: WOMEN_LIST) {
            womenSet.add(woman);
            pairMap.put(woman, new Person(woman));
        }

        preferenceMap = setPreferenceMap();
        Set<Person> result = propose(menSet);
        System.out.println(result);
    }

    private Map<String, List<String>> setPreferenceMap() {
        Map<String, List<String>> map = new HashMap<>();

        map.put("Frank", new ArrayList<>());
        for (String name: FRANK_LIST) {
            map.get("Frank").add(name);
        }
        map.put("Dennis", new ArrayList<>());
        for (String name: DENNIS_LIST) {
            map.get("Dennis").add(name);
        }
        map.put("Mac", new ArrayList<>());
        for (String name: MAC_LIST) {
            map.get("Mac").add(name);
        }
        map.put("Charlie", new ArrayList<>());
        for (String name: CHARLIE_LIST) {
            map.get("Charlie").add(name);
        }



        map.put("Rhea", new ArrayList<>());
        for (String name: RHEA_LIST) {
            map.get("Rhea").add(name);
        }
        map.put("Mary", new ArrayList<>());
        for (String name: MARY_LIST) {
            map.get("Mary").add(name);
        }
        map.put("Kate", new ArrayList<>());
        for (String name: KATE_LIST) {
            map.get("Kate").add(name);
        }
        map.put("Jill", new ArrayList<>());
        for (String name: JILL_LIST) {
            map.get("Jill").add(name);
        }
        return map;
    }

    private Set<Person> propose(Set<String> proposerSet) {
        if (menSet.size() != womenSet.size()) {
            return null;
        }
        Set<Person> result = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        for (String proposer: proposerSet) {
            queue.add(proposer);
        }

        while (!queue.isEmpty()) {
            String proposer = queue.remove();
            Person proposePerson = pairMap.get(proposer);
            List<String> targets = preferenceMap.get(proposer);

            String target = targets.get(0);
            Person targetPerson = pairMap.get(target);
            // Not pair yet
            if (targetPerson.partner == null) {
                targetPerson.partner = proposer;
                proposePerson.partner = target;
                result.add(proposePerson);
            // Check if unstable
            } else {
                String enemy = targetPerson.partner;
                List<String> targetPriorityList = preferenceMap.get(target);
                int enemyPriority = targetPriorityList.indexOf(enemy);
                int proposerPriority = targetPriorityList.indexOf(proposer);
                // Stable
                if (enemyPriority < proposerPriority) {
                    targetPriorityList.remove(proposer);

                    List<String> proposerPriorityList = preferenceMap.get(proposer);
                    proposerPriorityList.remove(target);
                    queue.add(proposer);

                // Unstable
                } else {

                    List<String> enemyPriorityList = preferenceMap.get(enemy);
                    enemyPriorityList.remove(target);
                    targetPriorityList.remove(enemy);

                    proposePerson.partner = target;
                    targetPerson.partner = proposer;
                    result.add(proposePerson);

                    Person enemyPerson = pairMap.get(enemy);
                    result.remove(enemyPerson);
                    queue.add(enemy);
                }
            }
        }
        return result;
    }
}

class Person {
    String name;
    String partner;

    Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new String(name + ":" + partner);
    }
}
