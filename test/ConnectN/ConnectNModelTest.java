package ConnectN;

import junit.framework.TestCase;

public class ConnectNModelTest extends TestCase {

    public void testPlayMove() {

        ConnectNModel model = ConnectNModel.builder().players("Joe", "Sally").build();
        model.playMove(4);
        model.playMove(4);
        model.playMove(4);
        model.playMove(4);
    }

    public void testLowestPosition() {
    }
}