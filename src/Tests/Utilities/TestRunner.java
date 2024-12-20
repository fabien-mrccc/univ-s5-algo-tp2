package Tests.Utilities;

import Tests.DirectedEdgeTest;
import Tests.GraphTest;
import Tests.NonDirectedEdgeTest;

public enum TestRunner {
    NON_DIRECTED_EDGE_TEST(NonDirectedEdgeTest.class),
    DIRECTED_EDGE_TEST(DirectedEdgeTest.class),
    GRAPH_TEST(GraphTest.class);

    private final Class<?> testClass;

    TestRunner(Class<?> testClass) {
        this.testClass = testClass;
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("-----------------");
        for (TestRunner testClass : TestRunner.values()) {
            try {
                System.out.println("Running " + testClass.name() + "...");
                testClass.getTestClass().getMethod("main", String[].class).invoke(null, (Object) new String[]{});
                System.err.println("-----------------");
            } catch (Exception e) {
                throw e.getCause();
            }
        }
    }

    public Class<?> getTestClass() {
        return testClass;
    }
}
