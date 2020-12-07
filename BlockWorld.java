import java.util.*;

class Action {
    private String name;
    private Block arg1;
    private Block arg2;

    public static String UNSTACK = "unstack";
    public static String STACK = "stack";
    public static String PICKUP = "pickup";
    public static String PUTDOWN = "putdown";

    public Action(String name, Block arg1, Block arg2) {
        this.name = name;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Action(String name, Block arg1) {
        this(name, arg1, null);
    }

    /**
     * Find what changes applying `this` would imply.
     * @return Pair of what to remove and what to add.
     */
    public Pair<ArrayList<Predicate>, ArrayList<Predicate>> apply() {
        ArrayList<Predicate> toRemove = new ArrayList<Predicate>();
        ArrayList<Predicate> toAdd = new ArrayList<Predicate>();

        if (name.equals(UNSTACK)) {
            toRemove.add(new Predicate(Predicate.ON, arg1, arg2));
            toRemove.add(new Predicate(Predicate.ARMEMPTY));
            toRemove.add(new Predicate(Predicate.CLEAR, arg1));
            toAdd.add(new Predicate(Predicate.HOLD, arg1));
            toAdd.add(new Predicate(Predicate.CLEAR, arg2));
        } else if (name.equals(STACK)) {
            toRemove.add(new Predicate(Predicate.HOLD, arg1));
            toRemove.add(new Predicate(Predicate.CLEAR, arg2));
            toAdd.add(new Predicate(Predicate.ON, arg1, arg2));
            toAdd.add(new Predicate(Predicate.CLEAR, arg1));
            toAdd.add(new Predicate(Predicate.ARMEMPTY));
        } else if (name.equals(PICKUP)) {
            toRemove.add(new Predicate(Predicate.ARMEMPTY));
            toRemove.add(new Predicate(Predicate.ONTABLE, arg1));
            toRemove.add(new Predicate(Predicate.CLEAR, arg1));
            toAdd.add(new Predicate(Predicate.HOLD, arg1));
        } else if (name.equals(PUTDOWN)) {
            toRemove.add(new Predicate(Predicate.HOLD, arg1));
            toAdd.add(new Predicate(Predicate.ARMEMPTY));
            toAdd.add(new Predicate(Predicate.ONTABLE, arg1));
            toAdd.add(new Predicate(Predicate.CLEAR, arg1));
        }

        return new Pair<ArrayList<Predicate>, ArrayList<Predicate>>(toRemove, toAdd);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("(");
        if (arg1 != null) {
            sb.append(arg1);
            if (arg2 != null) {
                sb.append(", ");
                sb.append(arg2);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Action)) {
            return super.equals(obj);
        }
        Action other = (Action) obj;

        boolean ok = name.equals(other.name);
        if (arg1 != null) {
            ok = ok && arg1.equals(other.arg1);
        }
        if (arg2 != null) {
            ok = ok && arg2.equals(other.arg2);
        }

        return ok;
    }
}
class Block {
    private String name;

    public Block(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Block)) {
            return super.equals(obj);
        }
        Block other = (Block) obj;
        return name.equals(other.name);
    }
}


public class BlockWorld {

    static boolean DEBUG = true;

    public static void main(String[] args) throws Exception {
        Block A = new Block("A");
        Block B = new Block("B");
        Block C = new Block("C");
        Block D = new Block("D");
        Block E = new Block("E");
        Block F = new Block("F");
        Block G = new Block("G");

        // Simple setup.
        ArrayList<Predicate> world = new ArrayList<Predicate>(Arrays.asList(
                new Predicate(Predicate.ON, B, A),
                new Predicate(Predicate.ONTABLE, A),
                new Predicate(Predicate.ONTABLE, C),
                new Predicate(Predicate.ONTABLE, D),
                new Predicate(Predicate.CLEAR, B),
                new Predicate(Predicate.CLEAR, C),
                new Predicate(Predicate.CLEAR, D),
                new Predicate(Predicate.ARMEMPTY)
        ));

        ArrayList<Predicate> targetWorld = new ArrayList<Predicate>(Arrays.asList(
                new Predicate(Predicate.ON, C, A),
                new Predicate(Predicate.ON, B, D),
                new Predicate(Predicate.ONTABLE, A),
                new Predicate(Predicate.ONTABLE, D),
                new Predicate(Predicate.CLEAR, C),
                new Predicate(Predicate.CLEAR, B),
                new Predicate(Predicate.ARMEMPTY)
        ));

        // Complex setup.
//        ArrayList<Predicate> world = new ArrayList<Predicate>(Arrays.asList(
//                new Predicate(Predicate.ONTABLE, C),
//                new Predicate(Predicate.ONTABLE, G),
//                new Predicate(Predicate.CLEAR, A),
//                new Predicate(Predicate.CLEAR, D),
//                new Predicate(Predicate.ON, A, B),
//                new Predicate(Predicate.ON, B, C),
//                new Predicate(Predicate.ON, D, E),
//                new Predicate(Predicate.ON, E, F),
//                new Predicate(Predicate.ON, F, G),
//                new Predicate(Predicate.ARMEMPTY)
//        ));
//
//        ArrayList<Predicate> targetWorld = new ArrayList<Predicate>(Arrays.asList(
//                new Predicate(Predicate.ONTABLE, A),
//                new Predicate(Predicate.ONTABLE, F),
//                new Predicate(Predicate.CLEAR, B),
//                new Predicate(Predicate.CLEAR, E),
//                new Predicate(Predicate.ON, B, D),
//                new Predicate(Predicate.ON, D, G),
//                new Predicate(Predicate.ON, G, A),
//                new Predicate(Predicate.ON, E, C),
//                new Predicate(Predicate.ON, C, F),
//                new Predicate(Predicate.ARMEMPTY)
//        ));

        System.out.println("From " + world);
        System.out.println("To " + targetWorld);
        System.out.println();

        Solver solver = new Solver(world, targetWorld);

        long now = System.currentTimeMillis();
        ArrayList<Action> solution = solver.findSolution();
        long now2 = System.currentTimeMillis();

        for (Action action: solution) {
            ArrayList<Predicate> newWorld = solver.performAction(world, action);

            if (DEBUG) {
                System.out.println("Applying " + action);
                System.out.println("To " + world);
                System.out.println("Got " + newWorld);
                System.out.println();
            }

            world = newWorld;
        }
        System.out.println("Took " + solution.size() +  " steps.");
        System.out.println("Took " + (now2 - now) +  " ms.");
    }
}
class Pair<T1, T2> {
    private T1 fst;
    private T2 snd;

    public Pair(T1 fst, T2 snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public T1 getFst() {
        return fst;
    }

    public T2 getSnd() {
        return snd;
    }

}
class Predicate implements Comparable {
    private String name;
    private Block arg1;
    private Block arg2;

    public static String ON = "on";
    public static String ONTABLE = "ontable";
    public static String CLEAR = "clear";
    public static String HOLD = "hold";
    public static String ARMEMPTY = "armempty";

    public Predicate(String name, Block arg1, Block arg2) {
        this.name = name;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Predicate(String name, Block arg1) {
        this(name, arg1, null);
    }

    public Predicate(String name) {
        this(name, null, null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("(");
        if (arg1 != null) {
            sb.append(arg1);
            if (arg2 != null) {
                sb.append(", ");
                sb.append(arg2);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public Block getArg1() {
        return arg1;
    }

    public Block getArg2() {
        return arg2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Predicate)) {
            return super.equals(obj);
        }
        Predicate other = (Predicate) obj;

        boolean ok = name.equals(other.name);
        if (arg1 != null) {
            ok = ok && arg1.equals(other.arg1);
        }
        if (arg2 != null) {
            ok = ok && arg2.equals(other.arg2);
        }

        return ok;
    }

    @Override
    public int compareTo(Object o) {
        Predicate other = (Predicate) o;
        int c = name.compareTo(other.name);
        if (c == 0 && arg1 != null) {
            c = arg1.getName().compareTo(other.getName());
            if (c == 0 && arg2 != null) {
                c = arg2.getName().compareTo(other.getName());
            }
        }
        return c;
    }
}

class ProblemState implements Comparable {
    ProblemState parent;
    ArrayList<Predicate> world;
    Action actionTaken;
    int g, h;

    public ProblemState(ProblemState parent, ArrayList<Predicate> world, Action actionTaken, int g, int h) {
        this.parent = parent;
        this.world = world;
        this.actionTaken = actionTaken;
        this.g = g;
        this.h = h;
    }

    public int getF() {
        return g + h;
    }

    @Override
    public int compareTo(Object o) {
        ProblemState other = (ProblemState) o;
        return Integer.compare(this.getF(), other.getF());
    }
}
class Solver {
    private ArrayList<Predicate> startWorld;
    private ArrayList<Predicate> targetWorld;

    public Solver(ArrayList<Predicate> startWorld, ArrayList<Predicate> targetWorld) {
        this.startWorld = new ArrayList<Predicate>(startWorld);
        this.targetWorld = new ArrayList<Predicate>(targetWorld);
    }

    /**
     * Runs A* and finds the list of actions that need to be executed to get to `targetWorld`.
     */
    public ArrayList<Action> findSolution() {
        PriorityQueue<ProblemState> open = new PriorityQueue<ProblemState>();

        open.add(new ProblemState(null, startWorld, null, 0, solutionDistance(startWorld, targetWorld)));
        boolean found = false;
        ProblemState solutionState = null;
        while (!open.isEmpty() && !found) {
            ProblemState best = open.poll();

            ArrayList<Action> options = getOptions(best.world);
            for (Action action: options) {
                ArrayList<Predicate> newWorld = performAction(best.world, action);
                ProblemState successor = new ProblemState(best,newWorld, action,best.g + 1,
                        solutionDistance(newWorld, targetWorld));

                if (worldEquals(newWorld, targetWorld)) {
                    found = true;
                    solutionState = successor;
                    break;
                }

                open.add(successor);
            }
        }
        return makeSolution(solutionState);
    }

    /**
     * Applies given action to given world. Returns a new world.
     */
    public ArrayList<Predicate> performAction(ArrayList<Predicate> world, Action action) {
        ArrayList<Predicate> newWorld = new ArrayList<Predicate>();
        newWorld.addAll(world);

        Pair<ArrayList<Predicate>, ArrayList<Predicate>> changes = action.apply();
        ArrayList<Predicate> toRemove = changes.getFst();
        ArrayList<Predicate> toAdd = changes.getSnd();

        newWorld.removeAll(toRemove);
        newWorld.addAll(toAdd);

        return newWorld;
    }

    /**
     * Get a list of all the possible actions that `world` allows.
     */
    private ArrayList<Action> getOptions(ArrayList<Predicate> world) {
        ArrayList<Action> options = new ArrayList<Action>();
        boolean isArmEmpty = false;
        Block heldBock = null;

        for (Predicate p: world) {
            if (p.getName().equals(Predicate.ARMEMPTY)) {
                isArmEmpty = true;
            }
            if (p.getName().equals(Predicate.HOLD)) {
                heldBock = p.getArg1();
            }
        }

        // unstack
        if (isArmEmpty) {
            for (Predicate p1 : world) {
                if (p1.getName().equals(Predicate.ON)) {
                    Block top = p1.getArg1();
                    Block bottom = p1.getArg2();
                    // find if it is clear
                    boolean isClear = false;
                    for (Predicate p2 : world) {
                        if (p2.getName().equals(Predicate.CLEAR) && p2.getArg1().equals(top)) {
                            isClear = true;
                            break;
                        }
                    }
                    if (isClear) {
                        options.add(new Action(Action.UNSTACK, top, bottom));
                    }
                }
            }
        }

        // stack
        if (!isArmEmpty) {
            for (Predicate p1 : world) {
                if (p1.getName().equals(Predicate.CLEAR)) {
                    Block bottom = p1.getArg1();
                    options.add(new Action(Action.STACK, heldBock, bottom));
                }
            }
        }

        // pickup
        if (isArmEmpty) {
            for (Predicate p1 : world) {
                if (p1.getName().equals(Predicate.ONTABLE)) {
                    Block block = p1.getArg1();
                    // find if it is clear
                    boolean isClear = false;
                    for (Predicate p2 : world) {
                        if (p2.getName().equals(Predicate.CLEAR) && p2.getArg1().equals(block)) {
                            isClear = true;
                            break;
                        }
                    }
                    if (isClear) {
                        options.add(new Action(Action.PICKUP, block));
                    }
                }
            }
        }

        // putdown
        if (!isArmEmpty) {
            options.add(new Action(Action.PUTDOWN, heldBock));
        }

        return options;
    }

    /**
     * Construct the list of actions having found the solution node.
     */
    private ArrayList<Action> makeSolution(ProblemState end) {
        ArrayList<Action> solution = new ArrayList<Action>();
        ProblemState iter = end;
        while (iter.parent != null) {
            solution.add(iter.actionTaken);
            iter = iter.parent;
        }
        Collections.reverse(solution);
        return solution;
    }

    /**
     * Heuristic for A*. The amount of predicates in `world` that are not part of `targetWorld`.
     */
    private int solutionDistance(ArrayList<Predicate> world,
                                       ArrayList<Predicate> targetWorld) {
        int contained = 0;
        for (Predicate p: world) {
            if (targetWorld.contains(p)) {
                contained += 1;
            }
        }
        return world.size() - contained;
    }

    private boolean worldEquals(ArrayList<Predicate> world,
                                ArrayList<Predicate> targetWorld) {
        return world.containsAll(targetWorld) && targetWorld.containsAll(world);
    }
}