import java.util.*;

public class TCSS543HW1StableMatching {

    // Student list and hosiptal list, both list must have same size to fit the stable matching
    private static final String[] STUDENT_LIST = {"Frank", "Dennis", "Mac", "Charlie"};
    private static final String[] HOSPITAL_LIST = {"hospital1", "hospital2", "hospital3", "hospital4"};

    // Each student has its own priority of hospitals
    private static final String[] FRANK_LIST = {"hospital3", "hospital2", "hospital1", "hospital4"};
    private static final String[] DENNIS_LIST = {"hospital2", "hospital4", "hospital1", "hospital3"};
    private static final String[] MAC_LIST = {"hospital3", "hospital1", "hospital4", "hospital2"};
    private static final String[] CHARLIE_LIST = {"hospital1", "hospital2", "hospital3", "hospital4"};

    // Each hospital has its own priority of students
    private static final String[] HOSPITAL1_LIST = {"Frank", "Mac", "Dennis", "Charlie"};
    private static final String[] HOSPITAL2_LIST = {"Mac", "Charlie", "Dennis", "Frank"};
    private static final String[] HOSPITAL3_LIST = {"Dennis", "Mac", "Charlie", "Frank"};
    private static final String[] HOSPITAL4_LIST = {"Charlie", "Dennis", "Frank", "Mac"};

    // Set of student
    private Set<String> studentSet;
    // Set of hospital
    private Set<String> hospitalSet;
    // Map of proposer and priority
    private Map<String, List<String>> preferenceMap;
    // Map of name and proposer
    private Map<String, Proposer> pairMap;

    /**
     * Constructor to initialize all fields
     */
    public TCSS543HW1StableMatching() {
        studentSet = new HashSet<>();
        hospitalSet = new HashSet<>();
        pairMap = new HashMap<>();

        for (String student: STUDENT_LIST) {
            studentSet.add(student);
            pairMap.put(student, new Proposer(student));
        }
        for (String hospital: HOSPITAL_LIST) {
            hospitalSet.add(hospital);
            pairMap.put(hospital, new Proposer(hospital));
        }
        preferenceMap = setPreferenceMap();
    }

    /**
     * Main method
     *
     * Choose matches.studentSet or matches.hospitalSet according to student's or hospital's preference
     *
     * @param theArgs
     */
    public static void main(String... theArgs) {
        TCSS543HW1StableMatching matches = new TCSS543HW1StableMatching();
        System.out.println("Choose 1 if assign students to hospital");
        System.out.println("Choose 2 if assign hospital to student");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        while (1 > option || option > 2) {
            System.out.println("\nInvalid input");
            System.out.println("Choose 1 if assign students to hospital");
            System.out.println("Choose 2 if assign hospital to student");
            option = scanner.nextInt();
        }

        Set<Proposer> result = matches.propose(option == 1 ? matches.hospitalSet : matches.studentSet);
        System.out.println(result);
    }

    /**
     * Build a map of preference for each proposer
     *
     * @return Map<String, List<String>>
     */
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

    /**
     * Proposer set propse to target set
     *
     * @param proposerSet
     * @return
     */
    private Set<Proposer> propose(Set<String> proposerSet) {
        if (studentSet.size() != hospitalSet.size()) {
            return null;
        }
        Set<Proposer> result = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        for (String proposer: proposerSet) {
            queue.add(proposer);
        }

        while (!queue.isEmpty()) {
            String proposer = queue.remove();
            Proposer proposePerson = pairMap.get(proposer);
            List<String> targets = preferenceMap.get(proposer);

            String target = targets.get(0);
            Proposer targetPerson = pairMap.get(target);
            // Not pair yet
            if (targetPerson.target == null) {
                targetPerson.target = proposer;
                proposePerson.target = target;
                result.add(proposePerson);
                // Check if unstable
            } else {
                String enemy = targetPerson.target;
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

                    proposePerson.target = target;
                    targetPerson.target = proposer;
                    result.add(proposePerson);

                    Proposer enemyPerson = pairMap.get(enemy);
                    result.remove(enemyPerson);
                    queue.add(enemy);
                }
            }
        }
        return result;
    }
}

/**
 * Proposer class
 */
class Proposer {
    String name;
    String target;

    Proposer(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new String(name + ":" + target);
    }
}
