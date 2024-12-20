package Tests.Utilities;

public class TestPrinter {

    /**
     * Prints a success message if the result matches the expected result.
     * Throws a RuntimeException if the result does not match the expected result.
     *
     * @param result The actual result of the test.
     * @param expectedResult The expected result of the test.
     */
    public static void result(boolean result, boolean expectedResult) throws TestExecutionException {
        if (result == expectedResult) {
            expectedResult();
        } else {
            unexpectedResult("result=" + result + ", expected_result=" + expectedResult);
        }
    }

    /**
     * Prints a success message if the result matches the expected result.
     * Throws a RuntimeException if the result does not match the expected result.
     *
     * @param result The actual result of the test.
     * @param expectedResult The expected result of the test.
     */
    public static void result(int result, int expectedResult) throws TestExecutionException {
        if (result == expectedResult) {
            expectedResult();
        }
        else {
            unexpectedResult("result=" + result + ", expected_result=" + expectedResult);
        }
    }

    /**
     * Prints "Test passed!" to the console.
     */
    private static void expectedResult() {
        System.out.println("Test passed!");
    }

    /**
     * Throws a TestExecutionException indicating an unexpected result.
     *
     * @throws RuntimeException If the result does not match the expected outcome.
     */
    private static void unexpectedResult(String message) throws TestExecutionException {
        throw new TestExecutionException(message);
    }
}
