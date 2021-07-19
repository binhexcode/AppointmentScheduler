package lambdaexpression;

import java.sql.SQLException;

/**
 * Interface to assist lambda expressions.
 *
 * @author Rodney Agosto
 */
public interface LambdaTwoStrings {

    /**
     * Apply two string variables to the lambda expression.
     * @return Apply two string variables to the lambda expression.
     */
    public void apply(String text1, String text2) throws SQLException;

}
