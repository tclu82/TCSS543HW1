import java.util.*;

public class TCSS543HW1StableMatching {

    private static final String[] STUDENT_LIST = {"Frank", "Dennis", "Mac", "Charlie"};
    private static final String[] HOSPITAL_LIST = {"hospital1", "hospital2", "hospital3", "hospital4"};

    private static final String[] FRANK_LIST = {"hospital3", "hospital2", "hospital1", "hospital4"};
    private static final String[] DENNIS_LIST = {"hospital2", "hospital4", "hospital1", "hospital3"};
    private static final String[] MAC_LIST = {"hospital3", "hospital1", "hospital4", "hospital2"};
    private static final String[] CHARLIE_LIST = {"hospital1", "hospital2", "hospital3", "hospital4"};

    private static final String[] HOSPITAL1_LIST = {"Frank", "Mac", "Dennis", "Charlie"};
    private static final String[] HOSPITAL2_LIST = {"Mac", "Charlie", "Dennis", "Frank"};
    private static final String[] HOSPITAL3_LIST = {"Dennis", "Mac", "Charlie", "Frank"};
    private static final String[] HOSPITAL4_LIST = {"Charlie", "Dennis", "Frank", "Mac"};

    private Set<String> menSet;
    private Set<String> womenSet;
    private Map<String, List<String>> preferenceMap;
    private Map<String, Person> pairMap;

    public TCSS543HW1StableMatching() {
        menSet = new HashSet<>();
        womenSet = new HashSet<>();
        pairMap = new HashMap<>();

        for (String man: STUDENT_LIST) {
            menSet.add(man);
            pairMap.put(man, new Person(man));
        }
        for (String woman: HOSPITAL_LIST) {
            womenSet.add(woman);
            pairMap.put(woman, new Person(woman));
        }
        preferenceMap = setPreferenceMap();
    }

    public static void main(String... theArgs) {
        TCSS543HW1StableMatching matches = new TCSS543HW1StableMatching();
        Set<Person> result = matches.propose(matches.menSet);
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

        map.put("hospital1", new ArrayList<>());
        for (String name: HOSPITAL1_LIST) {
            map.get("hospital1").add(name);
        }
        map.put("hospital2", new ArrayList<>());
        for (String name: HOSPITAL2_LIST) {
            map.get("hospital2").add(name);
        }
        map.put("hospital3", new ArrayList<>());
        for (String name: HOSPITAL3_LIST) {
            map.get("hospital3").add(name);
        }
        map.put("hospital4", new ArrayList<>());
        for (String name: HOSPITAL4_LIST) {
            map.get("hospital4").add(name);
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